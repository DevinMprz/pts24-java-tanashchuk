package sk.uniba.fmph.dcs.stone_age;

import sk.uniba.fmph.dcs.game_board.GameBoard;
import sk.uniba.fmph.dcs.game_phase_controller.*;
import sk.uniba.fmph.dcs.player_board.PlayerBoard;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;

import java.util.*;

/**
 * The StoneAgeGame class manages the core game logic and interaction between game phases, players, and the game board.
 */
public class StoneAgeGame implements InterfaceStoneAgeGame {

    private final Map<Integer, Player> players;
    private final List<Player> forCreatingGamePhase;
    private final StoneAgeObservable observable;
    private final InterfaceGamePhaseController gamePhaseController;
    private final InterfaceGetState gameBoard;

    public StoneAgeGame(final int amountOfPlayers, final StoneAgeObservable observer) {
        this.players = createPlayers(amountOfPlayers);
        this.forCreatingGamePhase = new ArrayList<>(players.values());
        this.gameBoard = new GameBoard(forCreatingGamePhase);
        this.gamePhaseController = new GamePhaseController(createController(), players.get(0).getPlayerOrder());
        this.observable = observer;
    }

    private Map<Integer, Player> createPlayers(int amountOfPlayers) {
        Map<Integer, Player> players = new HashMap<>();
        for (int i = 0; i < amountOfPlayers; i++) {
            players.put(i, new Player(
                    new PlayerOrder(i, amountOfPlayers),
                    new PlayerBoardGameBoardFacade(new PlayerBoard())
            ));
        }
        return players;
    }

    private Map<GamePhase, InterfaceGamePhaseState> createController() {
        GameBoard board = (GameBoard) gameBoard;
        return Map.of(
                GamePhase.GAME_END, new GameEndState(forCreatingGamePhase),
                GamePhase.ALL_PLAYERS_TAKE_A_REWARD, new AllPlayersTakeARewardState(),
                GamePhase.WAITING_FOR_TOOL_USE, new WaitingForToolUseState(null),
                GamePhase.NEW_ROUND, new NewRoundState(board.getAllLocations(), forCreatingGamePhase),
                GamePhase.FEED_TRIBE, new FeedTribeState(forCreatingGamePhase),
                GamePhase.MAKE_ACTION, new MakeActionState(board.getAllLocations()),
                GamePhase.PLACE_FIGURES, new PlaceFiguresState(board.getAllLocations(), forCreatingGamePhase)
        );
    }

    private void notifyObserver() {
        observable.notify(gameBoard.state());
        observable.notify(gamePhaseController.state());
        players.values().forEach(player -> {
            PlayerBoardGameBoardFacade facade = (PlayerBoardGameBoardFacade) player.getPlayerBoard();
            observable.notify(facade.getPlayerBoard().state());
        });
    }

    @Override
    public boolean placeFigures(int playerId, Location location, int figuresCount) {
        return performAction(playerId, () ->
                gamePhaseController.placeFigures(players.get(playerId).getPlayerOrder(), location, figuresCount)
        );
    }

    @Override
    public boolean makeAction(int playerId, Location location, Collection<Effect> usedResources, Collection<Effect> desiredResources) {
        return performAction(playerId, () ->
                gamePhaseController.makeAction(players.get(playerId).getPlayerOrder(), location, usedResources, desiredResources)
        );
    }

    @Override
    public boolean skipAction(int playerId, Location location) {
        return performAction(playerId, () ->
                gamePhaseController.skipAction(players.get(playerId).getPlayerOrder(), location)
        );
    }

    @Override
    public boolean useTools(int playerId, int toolIndex) {
        return performAction(playerId, () ->
                gamePhaseController.useTools(players.get(playerId).getPlayerOrder(), toolIndex)
        );
    }

    @Override
    public boolean noMoreToolsThisThrow(int playerId) {
        return performAction(playerId, () ->
                gamePhaseController.noMoreToolsThisThrow(players.get(playerId).getPlayerOrder())
        );
    }

    @Override
    public boolean feedTribe(int playerId, Collection<Effect> resources) {
        return performAction(playerId, () ->
                gamePhaseController.feedTribe(players.get(playerId).getPlayerOrder(), resources)
        );
    }

    @Override
    public boolean doNotFeedThisTurn(int playerId) {
        return performAction(playerId, () ->
                gamePhaseController.doNotFeedThisTurn(players.get(playerId).getPlayerOrder())
        );
    }

    @Override
    public boolean makeAllPlayersTakeARewardChoice(int playerId, Effect reward) {
        return performAction(playerId, () ->
                gamePhaseController.makeAllPlayersTakeARewardChoice(players.get(playerId).getPlayerOrder(), reward)
        );
    }

    private boolean performAction(int playerId, Action action) {
        if (!players.containsKey(playerId)) {
            return false;
        }
        boolean result = action.execute();
        notifyObserver();
        return result;
    }

    @FunctionalInterface
    private interface Action {
        boolean execute();
    }
}
