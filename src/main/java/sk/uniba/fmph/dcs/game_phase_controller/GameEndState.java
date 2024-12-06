package sk.uniba.fmph.dcs.game_phase_controller;

import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.Collection;
import java.util.List;

public class GameEndState implements InterfaceGamePhaseState {
    private List<Player> players;
    public GameEndState(List<Player> players){
        this.players = players;
    }
    @Override
    public ActionResult placeFigures(PlayerOrder player, Location location, int figuresCount) {
        return null;
    }

    @Override
    public ActionResult makeAction(PlayerOrder player, Location location, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        return null;
    }

    @Override
    public ActionResult skipAction(PlayerOrder player, Location location) {
        return null;
    }

    @Override
    public ActionResult useTools(PlayerOrder player, int toolIndex) {
        return null;
    }

    @Override
    public ActionResult noMoreToolsThisThrow(PlayerOrder player) {
        return null;
    }

    @Override
    public ActionResult feedTribe(PlayerOrder player, Collection<Effect> resources) {
        return null;
    }

    @Override
    public ActionResult doNotFeedThisTurn(PlayerOrder player) {
        return null;
    }

    @Override
    public ActionResult makeAllPlayersTakeARewardChoice(PlayerOrder player, Effect reward) {
        return null;
    }

    @Override
    public HasAction tryToMakeAutomaticAction(PlayerOrder player) {
        for(Player key: players){
            PlayerBoardGameBoardFacade plGm = (PlayerBoardGameBoardFacade) key.getPlayerBoard();
            plGm.getPlayerBoard().addEndOfGamePoints();
            break;
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }
}