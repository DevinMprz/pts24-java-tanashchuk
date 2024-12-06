package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

public class CivilizationCardPlace implements InterfaceFigureLocationInternal {

    private int requiredResources;
    private ArrayList<PlayerOrder> figures;
    private CivilizationCardDeck deck;
    private Optional<CivilizationCard> nextCard;
    private CivilizationCardPlace nextPlace;

    private GetSomethingChoice getSomethingChoicePerformer;
    private GetSomethingThrow getSomethingThrow;

    private GetCard getCardImmediateEffectPerformer;
    private AllPlayersTakeAReward allPlayersTakeARewardEffectPerformer;
    private GetSomethingFixed getSomethingFixedPerformer;


    public CivilizationCardPlace(final CivilizationCardPlace nextPlace, final CivilizationCardDeck deck,
                                 final int requiredResources) {
        this.requiredResources = requiredResources;
        this.nextPlace = nextPlace;
        this.deck = deck;
        this.nextCard = this.next();
        this.figures = new ArrayList<>();

    }

    public Optional<CivilizationCard> next() {

        if(nextCard != null && nextCard.isPresent()){
            Optional<CivilizationCard> temp = nextCard;
            nextCard = Optional.empty();
            return temp;
        }else{
            if(nextPlace != null){
                nextCard = nextPlace.next();
                nextPlace.nextCard = nextPlace.next();
            }else{
                nextCard = deck.getTop();
            }
        }
        return nextCard;
    }

    @Override
    public boolean placeFigures(Player player, int figureCount) {
        if (!player.getPlayerBoard().hasFigures(figureCount)) {
            return false;
        }

        if (figureCount != 1) {
            return false;
        }

        if (!this.figures.isEmpty()) {
            return false;
        }

        player.getPlayerBoard().takeFigures(figureCount);
        this.figures.add(player.getPlayerOrder());
        return true;
    }

    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if (this.placeFigures(player, count)) {
            return HasAction.AUTOMATIC_ACTION_DONE;
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    @Override
    public ActionResult makeAction(Player player, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        List<Effect> input = (List<Effect>) inputResources;

        if (!figures.contains(player.getPlayerOrder())) {
            return ActionResult.FAILURE;
        }

        if (input.size() != requiredResources) {
            return ActionResult.FAILURE;
        }

        for (Effect effect : input) {
            if (!effect.isResource()) {
                return ActionResult.FAILURE;
            }
        }

        if (!player.getPlayerBoard().takeResources(input)) {
            return ActionResult.FAILURE;
        }


        if (nextCard.isPresent()){
            List<ImmediateEffect> immediateEffects = nextCard.get().getImmediateEffectType();

            getCardImmediateEffectPerformer = new GetCard(deck);
            getSomethingFixedPerformer = new GetSomethingFixed();
            getSomethingChoicePerformer = new GetSomethingChoice(2);
            allPlayersTakeARewardEffectPerformer = new AllPlayersTakeAReward();
            getSomethingThrow = new GetSomethingThrow(((List<Effect>) inputResources).get(0));

            for(ImmediateEffect immediateEffect: immediateEffects){
                boolean result = switch (immediateEffect){
                    case ThrowGold -> getSomethingThrow.performEffect(player, Effect.GOLD);
                    case ThrowStone -> getSomethingThrow.performEffect(player, Effect.STONE);
                    case ThrowClay -> getSomethingThrow.performEffect(player, Effect.CLAY);
                    case ThrowWood -> getSomethingThrow.performEffect(player, Effect.WOOD);
                    case POINT -> getSomethingFixedPerformer.performEffect(player, Effect.POINT);
                    case WOOD -> getSomethingFixedPerformer.performEffect(player, Effect.WOOD);
                    case CLAY -> getSomethingFixedPerformer.performEffect(player, Effect.CLAY);
                    case STONE -> getSomethingFixedPerformer.performEffect(player, Effect.STONE);
                    case GOLD -> getSomethingFixedPerformer.performEffect(player, Effect.GOLD);
                    case CARD -> {
                        GetCard performer = new GetCard(deck);
                        performer.performEffect(player, null);
                        yield true;
                    }
                    case FOOD -> getSomethingFixedPerformer.performEffect(player, Effect.FOOD);
                    case ArbitraryResource -> {
                        for(Effect inputResource: inputResources){
                            getSomethingChoicePerformer.performEffect(player, inputResource);
                        }
                        yield true;
                    }
                    case AllPlayersTakeReward -> allPlayersTakeARewardEffectPerformer.performEffect(player, Effect.BUILDING);
                    case Tool -> { player.getPlayerBoard().giveEffect(List.of(Effect.TOOL)); yield true; }
                    case Field -> { player.getPlayerBoard().giveEffect(List.of(Effect.FIELD)); yield true; }
                    case OneTimeTool2 -> { player.getPlayerBoard().giveEffect(List.of(Effect.ONE_TIME_TOOL2)); yield true; }
                    case OneTimeTool3 -> { player.getPlayerBoard().giveEffect(List.of(Effect.ONE_TIME_TOOL3)); yield true; }
                    case OneTimeTool4 -> { player.getPlayerBoard().giveEffect(List.of(Effect.ONE_TIME_TOOL4)); yield true; }
                };
                if(!result){
                    return ActionResult.FAILURE;
                }
            }
            player.getPlayerBoard().giveEndOfGameEffect(nextCard.get().getEndOfGameEffectType());
        }

        figures.remove(player.getPlayerOrder());
        nextCard = Optional.empty();
        return ActionResult.ACTION_DONE;
    }

    @Override
    public boolean skipAction(Player player) {
        if (!this.figures.contains(player.getPlayerOrder())) {
            return false;
        }

        this.figures.remove(player.getPlayerOrder());
        return true;
    }

    public int getRequiredResources() {
        return requiredResources;
    }
    public int getFiguresCount() {
        return figures.size();
    }
    public Optional<CivilizationCard> getNextCard() {
        return nextCard;
    }

    @Override
    public HasAction tryToMakeAction(Player player) {
        if (!this.figures.contains(player.getPlayerOrder())) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    @Override
    public boolean newTurn() {
        if (!this.figures.isEmpty()) {
            return false;
        }

        this.nextCard = this.next();
        return true;
    }

    @Override
    public String state() {
        Map<String, Object> state = new HashMap<>();
        state.put("Card", this.nextCard.isEmpty() ? "None" : "Present");
        state.put("Cost", this.requiredResources);
        state.put("Figures", this.figures.size());
        return new JSONObject(state).toString();
    }
}
