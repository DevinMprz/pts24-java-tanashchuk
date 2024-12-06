package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.InterfaceFigureLocation;
import sk.uniba.fmph.dcs.stone_age.Player;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.*;

public class FigureLocationAdaptor implements InterfaceFigureLocation {
    private final InterfaceFigureLocationInternal figureLocation;
    private final List<Player> players;


    public FigureLocationAdaptor(final InterfaceFigureLocationInternal figureLocation, List<Player> players) {
        this.figureLocation = figureLocation;
        this.players = players;
    }

    // Helper method to find the Player object by PlayerOrder
    private Player getPlayerOrder(PlayerOrder player) {
        for (Player pl : players) {
            if (pl.getPlayerOrder().equals(player)) {
                return pl;
            }
        }
        return null;
    }

    // Places the specified number of figures for the given player
    @Override
    public boolean placeFigures(PlayerOrder player, int figureCount) {
        Player p = getPlayerOrder(player);
        if (p != null) {
            return figureLocation.placeFigures(p, figureCount);
        }
        return false;
    }

    // Checks if figures can be placed for the given player
    @Override
    public HasAction tryToPlaceFigures(PlayerOrder player, int count) {
        Player pl = getPlayerOrder(player);
        if (pl != null) {
            return figureLocation.tryToPlaceFigures(pl, count);
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    // Performs an action for the player using input and output resources
    @Override
    public ActionResult makeAction(PlayerOrder player, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        Player pl = getPlayerOrder(player);
        if (pl != null) {
            return figureLocation.makeAction(pl, inputResources, outputResources);
        }
        return ActionResult.FAILURE;
    }

    // Skips the action
    @Override
    public boolean skipAction(PlayerOrder player) {
        Player pl = getPlayerOrder(player);
        if (pl != null) {
            return figureLocation.skipAction(pl);
        }
        return false;
    }
    public sk.uniba.fmph.dcs.game_board.InterfaceFigureLocationInternal getFigureLocation() {
        return figureLocation;
    }
    // Attempts to perform an action for the given player
    @Override
    public HasAction tryToMakeAction(PlayerOrder player) {
        Player pl = getPlayerOrder(player);
        if (pl != null) {
            return figureLocation.tryToMakeAction(pl);
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    @Override
    public boolean newTurn() {
        return figureLocation.newTurn();
    }

    @Override
    public String state() {
        return figureLocation.state();
    }
}