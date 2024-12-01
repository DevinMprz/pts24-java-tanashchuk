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

//    private GetCard getCardImmediateEffectPerformer;
//    private AllPlayersTakeAReward allPlayersTakeARewardEffectPerformer;
//    private GetSomethingFixed getSomethingFixedPerformer;
//    private GetSomethingChoice getSomethingChoicePerformer;


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
            }else{
                nextCard = deck.getTop();
            }
        }
        return nextCard;
    }

    @Override
    public boolean placeFigures(Player player, int figureCount) {
        if (!player.playerBoard().hasFigures(figureCount)) {
            return false;
        }

        if (figureCount != 1) {
            return false;
        }

        if (!this.figures.isEmpty()) {
            return false;
        }

        player.playerBoard().takeFigures(figureCount);
        this.figures.add(player.playerOrder());
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
    public ActionResult makeAction(Player player, Effect[] inputResources, Effect[] outputResources) {
        List<Effect> input = Arrays.asList(inputResources);

        if (!figures.equals(player.playerOrder())) {
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

        if (!player.playerBoard().takeResources(input)) {
            return ActionResult.FAILURE;
        }


        List<ImmediateEffect> immediateEffects = nextCard.get().getImmediateEffectType();

//        getCardImmediateEffectPerformer = new GetCard(deck);
//        getSomethingFixedPerformer = new GetSomethingFixed();
//        getSomethingChoicePerformer = new GetSomethingChoice(2);
//        allPlayersTakeARewardEffectPerformer = new AllPlayersTakeAReward();
//        getSomethingThrow = new GetSomethingThrow(inputResources);

        for(ImmediateEffect immediateEffect: immediateEffects){
//            boolean result = switch (immediateEffect){
//                case ThrowGold -> getSomethingThrow.perform(player, Effect.GOLD);
//                case ThrowStone -> getSomethingThrow.perform(player, Effect.STONE);
//                case ThrowClay -> getSomethingThrow.perform(player, Effect.CLAY);
//                case ThrowWood -> getSomethingThrow.perform(player, Effect.WOOD);
//                case POINT -> getSomethingFixedPerformer.perform(player, ImmediateEffect.POINT);
//                case WOOD -> getSomethingFixedPerformer.perform(player, Effect.WOOD);
//                case CLAY -> getSomethingFixedPerformer.perform(player, Effect.CLAY);
//                case STONE -> getSomethingFixedPerformer.perform(player, Effect.STONE);
//                case GOLD -> getSomethingFixedPerformer.perform(player, Effect.GOLD);
//                case CARD -> getSomethingFixedPerformer.perform(player, Effect.CARD);
//                case FOOD -> getSomethingFixedPerformer.perform(player, Effect.FOOD);
//                case ArbitraryResource:
//                    for(Effect inputResource: inputResources){
//                        getSomethingChoicePerformer.performEffect(player, inputResource);
//                    }
//                    case AllPlayersTakeReward -> allPlayersTakeARewardEffectPerformer.perform(player, Effect.BUILDING);
//                    default -> false;
//            };
//            if(!result){
//                return ActionResult.FAILURE;
//            }
        }

        figures = null;
        nextCard = Optional.empty();
        return ActionResult.ACTION_DONE;
    }

    @Override
    public boolean skipAction(Player player) {
        if (!this.figures.contains(player.playerOrder())) {
            return false;
        }

        this.figures.remove(player.playerOrder());
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
        if (!this.figures.contains(player.playerOrder())) {
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
