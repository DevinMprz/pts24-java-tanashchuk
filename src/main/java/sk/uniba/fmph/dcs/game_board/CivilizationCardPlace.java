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

    public CivilizationCardPlace(final CivilizationCardPlace nextPlace, final CivilizationCardDeck deck,
                                 final int requiredResources) {
        this.requiredResources = requiredResources;
        this.nextPlace = nextPlace;
        this.deck = deck;
        this.nextCard = this.next();
        this.figures = new ArrayList<>();
    }

    public Optional<CivilizationCard> next() {

        if (this.nextCard != null && this.nextCard.isPresent()) {
            return this.nextCard;
        }

        if (this.nextPlace == null) {
            this.nextCard = this.deck.getTop();
        } else {
            this.nextCard = this.nextPlace.next();
        }
        return this.nextCard;
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
        if (!this.figures.contains(player.playerOrder())) {
            return ActionResult.FAILURE;
        }

        if (inputResources.length != this.requiredResources) {
            return ActionResult.FAILURE;
        }

        if (outputResources.length != 0) {
            return ActionResult.FAILURE;
        }
        int resourceCount = 0;
        for (Effect effect : inputResources) {
            if (effect.isResource()) {
                resourceCount++;
            }
        }
        if (resourceCount != this.requiredResources) {
            return ActionResult.FAILURE;
        }
        if (!player.playerBoard().takeResources(List.of(inputResources))) {
            return ActionResult.FAILURE;
        }

        this.figures.remove(player.playerOrder());
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
