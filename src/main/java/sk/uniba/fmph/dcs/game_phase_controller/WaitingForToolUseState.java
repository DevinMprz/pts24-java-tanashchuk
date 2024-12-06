package sk.uniba.fmph.dcs.game_phase_controller;

import sk.uniba.fmph.dcs.stone_age.*;

import java.util.Collection;

public class WaitingForToolUseState implements InterfaceGamePhaseState {

    private InterfaceToolUse toolUse;
    private PlayerOrder currentPlayer;

    public WaitingForToolUseState(PlayerOrder player) {
        currentPlayer = player;
    }

    @Override
    public ActionResult placeFigures(PlayerOrder player, Location location, int figuresCount) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult makeAction(PlayerOrder player, Location location, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult skipAction(PlayerOrder player, Location location) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult useTools(PlayerOrder player, int toolIndex) {
        if(player.equals(currentPlayer)) {
            if(toolUse.canUseTools()){
                toolUse.useTool(toolIndex);           //nothing stops player from using same tool several times
                return ActionResult.ACTION_DONE;
            }
        }
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult noMoreToolsThisThrow(PlayerOrder player) {
        toolUse.finishUsingTools();
        return ActionResult.ACTION_DONE;              //ACTION_DONE_ALL_PLAYERS_TAKE_A_REWARD?
    }

    @Override
    public ActionResult feedTribe(PlayerOrder player, Collection<Effect> resources) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult doNotFeedThisTurn(PlayerOrder player) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult makeAllPlayersTakeARewardChoice(PlayerOrder player, Effect reward) {
        return ActionResult.FAILURE;
    }

    @Override
    public HasAction tryToMakeAutomaticAction(PlayerOrder player) {
        if (player.equals(currentPlayer)) {                     //???
            if(toolUse.canUseTools()) return HasAction.WAITING_FOR_PLAYER_ACTION;
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return  HasAction.NO_ACTION_POSSIBLE;
    }
}
