package sk.uniba.fmph.dcs.game_phase_controller;

import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class FeedTribeState implements InterfaceGamePhaseState {

    private final List<Player> playerTribes;

    public FeedTribeState(List<Player> playerTribes) {
        this.playerTribes = playerTribes;
    }

    @Override
    public ActionResult placeFigures(final PlayerOrder player, final Location location, final int figuresCount) {
        return ActionResult.FAILURE;
    }
    @Override
    public ActionResult makeAction(final PlayerOrder player, final Location location,
                                   final Collection<Effect> inputResources, final Collection<Effect> outputResources) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult skipAction(final PlayerOrder player, final Location location) {
        return ActionResult.FAILURE;

    }

    @Override
    public ActionResult useTools(final PlayerOrder player, final int toolIndex) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult noMoreToolsThisThrow(final PlayerOrder player) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult feedTribe(final PlayerOrder player, final Collection<Effect> resources) {
        for(Player key: playerTribes){
            if(key.getPlayerOrder().equals(player)){
                InterfaceFeedTribe pl = (InterfaceFeedTribe) key.getPlayerBoard();
                if (pl.feedTribe(resources)) {
                    return ActionResult.ACTION_DONE;
                }
                break;
            }
        }
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult doNotFeedThisTurn(final PlayerOrder player) {
        for(Player key: playerTribes){
            if(key.getPlayerOrder().equals(player)){
                InterfaceFeedTribe pl = (InterfaceFeedTribe) key.getPlayerBoard();
                if (pl.doNotFeedThisTurn()) {
                    return ActionResult.ACTION_DONE;
                }
                break;
            }
        }
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult makeAllPlayersTakeARewardChoice(final PlayerOrder player, final Effect reward) {
        return ActionResult.FAILURE;
    }

    @Override
    public HasAction tryToMakeAutomaticAction(final PlayerOrder player) {
        for(Player key: playerTribes){
            if(key.getPlayerOrder().equals(player)){
                InterfaceFeedTribe pl = (InterfaceFeedTribe) key.getPlayerBoard();
                if (pl.isTribeFed()) {
                    return HasAction.NO_ACTION_POSSIBLE;
                }
                if (pl.feedTribeIfEnoughFood()) {
                    return HasAction.AUTOMATIC_ACTION_DONE;
                }
                break;
            }
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }
}