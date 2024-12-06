package sk.uniba.fmph.dcs.game_board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sk.uniba.fmph.dcs.stone_age.*;

public class PlaceOnHutAdaptor implements InterfaceFigureLocationInternal, InterfaceGetState {
    private final ToolMakerHutFields huts;
    private final List<PlayerOrder> usedPlayers = new ArrayList<>();
    public PlaceOnHutAdaptor(ToolMakerHutFields toolMakerHutFields){
        this.huts = toolMakerHutFields;
    }
    @Override
    public boolean placeFigures(Player player, int figureCount) {
        if(huts.placeOnHut(player)){
            player.getPlayerBoard().takeFigures(figureCount);
            return true;
        }
        return false;
    }

    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if(!player.getPlayerBoard().hasFigures(count)){
            return HasAction.NO_ACTION_POSSIBLE;
        }

        if(count != 2){
            return HasAction.NO_ACTION_POSSIBLE;
        }

        if(!huts.canPlaceOnHut(player)){
            return HasAction.NO_ACTION_POSSIBLE;
        }

        if(!placeFigures(player, count)){
            return HasAction.NO_ACTION_POSSIBLE;
        }

        return HasAction.AUTOMATIC_ACTION_DONE;
    }

    @Override
    public ActionResult makeAction(Player player, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        if(huts.actionHut(player)){
            return ActionResult.ACTION_DONE;
        }
        return ActionResult.FAILURE;
    }

    @Override
    public boolean skipAction(Player player) {
        return false;
    }

    @Override
    public HasAction tryToMakeAction(Player player) {
        if(makeAction(player, List.of(new Effect[0]), List.of(new Effect[0])) == ActionResult.ACTION_DONE){
            return HasAction.AUTOMATIC_ACTION_DONE;
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    @Override
    public boolean newTurn() {
        return huts.newTurn();
    }

    @Override
    public String state() {
        return huts.state();
    }
}