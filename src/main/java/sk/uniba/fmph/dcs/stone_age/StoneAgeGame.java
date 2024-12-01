package sk.uniba.fmph.dcs.stone_age;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoneAgeGame implements InterfaceStoneAgeGame{
    private final Map<Integer, PlayerOrder> players;
    private final StoneAgeObservable observable;
    private final InterfaceGamePhaseController gamePhaseController;
    private final InterfaceGetState playerBoard;
    private final InterfaceGetState gameBoard;

    public StoneAgeGame(final int amountOfPlayers, final StoneAgeObservable observable, final InterfaceGamePhaseController gamePhaseController,
                        final InterfaceGetState playerBoard, InterfaceGetState gameBoard) {
        players = new HashMap<>();
        for (int i = 1; i <= amountOfPlayers; i++) {
            PlayerOrder po = new PlayerOrder(i, amountOfPlayers);
            players.put(i, po);
        }
        this.observable = observable;
        this.gamePhaseController = gamePhaseController;
        this.gameBoard = gameBoard;
        this.playerBoard = playerBoard;
    }

    private void notifyObserver(){
        observable.notify(gameBoard.state());
        observable.notify(playerBoard.state());
        observable.notify(gamePhaseController.state());
    }

    @Override
    public boolean placeFigures(int playerId, Location location, int figuresCount) {
        if(players.containsKey(playerId)){
            return false;
        }

        boolean figuresResult = gamePhaseController.placeFigures(players.get(playerId), location, figuresCount);
        notifyObserver();
        return figuresResult;
    }

    @Override
    public boolean makeAction(int playerId, Location location, Effect[] usedResources, Effect[] desiredResources) {
        if(players.containsKey(playerId)){
            return false;
        }

        boolean actionResult = gamePhaseController.makeAction(players.get(playerId), location, List.of(usedResources), List.of(desiredResources));
        notifyObserver();
        return actionResult;
    }

    @Override
    public boolean skipAction(int playerId, Location location) {
        if(players.containsKey(playerId)){
            return false;
        }

        boolean skipActionResult = gamePhaseController.skipAction(players.get(playerId), location);
        notifyObserver();
        return skipActionResult;
    }

    @Override
    public boolean useTools(int playerId, int toolIndex) {
        if(players.containsKey(playerId)){
            return false;
        }

        boolean useToolsResult = gamePhaseController.useTools(players.get(playerId), toolIndex);
        notifyObserver();
        return useToolsResult;
    }

    @Override
    public boolean noMoreToolsThisThrow(int playerId) {
        if(players.containsKey(playerId)){
            return false;
        }

        boolean noMoreToolsResult = gamePhaseController.noMoreToolsThisThrow(players.get(playerId));
        notifyObserver();
        return noMoreToolsResult;
    }

    @Override
    public boolean feedTribe(int playerId, Effect[] resources) {
        if(players.containsKey(playerId)){
            return false;
        }

        boolean feedTribeResult = gamePhaseController.feedTribe(players.get(playerId), List.of(resources));
        notifyObserver();
        return feedTribeResult;
    }

    @Override
    public boolean doNotFeedThisTurn(int playerId) {
        if(players.containsKey(playerId)){
            return false;
        }

        boolean doNotFeedResult = gamePhaseController.doNotFeedThisTurn(players.get(playerId));
        notifyObserver();
        return doNotFeedResult;
    }

    @Override
    public boolean makeAllPlayersTakeARewardChoice(int playerId, Effect reward) {
        if(players.containsKey(playerId)){
            return false;
        }

        boolean allTakeRewardResult = gamePhaseController.makeAllPlayersTakeARewardChoice(players.get(playerId), reward);
        notifyObserver();
        return allTakeRewardResult;
    }
}


