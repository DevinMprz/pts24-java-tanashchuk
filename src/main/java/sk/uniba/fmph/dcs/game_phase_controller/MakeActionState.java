package sk.uniba.fmph.dcs.game_phase_controller;

import sk.uniba.fmph.dcs.game_board.PlaceOnHutAdaptor;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * The {@code MakeActionState} class represents the "Make Action" phase in the Stone Age game.
 * During this phase, the current player performs actions with their placed figures on the board.
 * This class implements the {@link InterfaceGamePhaseState} interface and provides the logic
 * for handling player actions specific to this phase.
 */
public class MakeActionState implements InterfaceGamePhaseState {

    /**
     * A map of all locations on the game board and their corresponding figure locations.
     * This map is used to delegate actions to the appropriate location.
     */
    private final Map<Location, InterfaceFigureLocation> places;
    private final List<PlayerOrder> usedPlayers = new ArrayList<>();

    /**
     * The player who is currently taking their turn during the "Make Action" phase.
     */
    //private final PlayerOrder currentPlayer;

    public MakeActionState(){
        places = null;
    }

    /**
     * Constructs a new {@code MakeActionState} with the specified places and current player.
     *
     * @param places         a map of locations to their corresponding figure locations
     * //@param currentPlayer  the player who is currently taking their turn
     */
    public MakeActionState(Map<Location, InterfaceFigureLocation> places) {
        this.places = places;
        //this.currentPlayer = currentPlayer;
    }

    /**
     * Players cannot place figures during the "Make Action" phase.
     * This method always returns {@link ActionResult#FAILURE}.
     *
     * @param playerOrder   the player attempting to place figures
     * @param location      the location where the player wants to place figures
     * @param figuresCount  the number of figures the player wants to place
     * @return {@code ActionResult.FAILURE} indicating the action is not allowed
     */
    @Override
    public ActionResult placeFigures(PlayerOrder playerOrder, Location location, int figuresCount) {
        // Players cannot place figures during the 'Make Action' phase
        return ActionResult.FAILURE;
    }

    /**
     * Allows the current player to perform an action at a specified location.
     * The action is delegated to the corresponding {@link InterfaceFigureLocation}.
     *
     * @param player           the player attempting to make an action
     * @param location         the location where the action is to be made
     * @param inputResources   resources provided as input for the action
     * @param outputResources  resources expected as output from the action
     * @return the result of the action as an {@link ActionResult}
     */
    @Override
    public ActionResult makeAction(PlayerOrder player, Location location, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        // Check if the player is the current player
//        if (!player.equals(currentPlayer)) {
//            return ActionResult.FAILURE;
//        }

        InterfaceFigureLocation figureLocation = places.get(location);
        if (figureLocation == null) {
            return ActionResult.FAILURE;
        }

        // Attempt to make the action at the specified location
        return figureLocation.makeAction(player, inputResources, outputResources);
    }

    /**
     * Allows the current player to skip an action at a specified location.
     * The skip action is delegated to the corresponding {@link InterfaceFigureLocation}.
     *
     * @param player    the player attempting to skip an action
     * @param location  the location where the action is to be skipped
     * @return {@code ActionResult.ACTION_DONE} if the action was successfully skipped,
     *         {@code ActionResult.FAILURE} otherwise
     */
    @Override
    public ActionResult skipAction(PlayerOrder player, Location location) {
        // Check if the player is the current player
//        if (!player.equals(currentPlayer)) {
//            return ActionResult.FAILURE;
//        }

        InterfaceFigureLocation figureLocation = places.get(location);
        if (figureLocation == null) {
            return ActionResult.FAILURE;
        }

        boolean success = figureLocation.skipAction(player);
        return success ? ActionResult.ACTION_DONE : ActionResult.FAILURE;
    }

    /**
     * Tool usage is not directly handled in this class during the "Make Action" phase.
     * This method always returns {@link ActionResult#FAILURE}.
     *
     * @param playerOrder  the player attempting to use a tool
     * @param toolIndex    the index of the tool being used
     * @return {@code ActionResult.FAILURE} indicating the action is not allowed
     */
    @Override
    public ActionResult useTools(PlayerOrder playerOrder, int toolIndex) {
        return ActionResult.FAILURE;
    }

    /**
     * Indicating that the player will not use more tools for the current throw.
     * This method always returns {@link ActionResult#FAILURE} as tool usage is not handled here.
     *
     * @param playerOrder  the player indicating no more tool usage
     * @return {@code ActionResult.FAILURE} indicating the action is not allowed
     */
    @Override
    public ActionResult noMoreToolsThisThrow(PlayerOrder playerOrder) {
        return ActionResult.FAILURE;
    }

    /**
     * Feeding the tribe is not handled during the "Make Action" phase.
     * This method always returns {@link ActionResult#FAILURE}.
     *
     * @param playerOrder  the player attempting to feed their tribe
     * @param resources    the resources being used to feed the tribe
     * @return {@code ActionResult.FAILURE} indicating the action is not allowed
     */
    @Override
    public ActionResult feedTribe(PlayerOrder playerOrder, Collection<Effect> resources) {
        // Feeding the tribe happens during the FeedTribe phase
        return ActionResult.FAILURE;
    }

    /**
     * Indicating that the player chooses not to feed their tribe this turn.
     * This method always returns {@link ActionResult#FAILURE} as it's not applicable in this phase.
     *
     * @param playerOrder  the player choosing not to feed their tribe
     * @return {@code ActionResult.FAILURE} indicating the action is not allowed
     */
    @Override
    public ActionResult doNotFeedThisTurn(PlayerOrder playerOrder) {
        // Feeding the tribe happens during the FeedTribe phase
        return ActionResult.FAILURE;
    }

    /**
     * Making all players take a reward choice is not applicable during the "Make Action" phase.
     * This method always returns {@link ActionResult#FAILURE}.
     *
     * @param playerOrder  the player attempting to make a reward choice
     * @param reward       the reward being chosen
     * @return {@code ActionResult.FAILURE} indicating the action is not allowed
     */
    @Override
    public ActionResult makeAllPlayersTakeARewardChoice(PlayerOrder playerOrder, Effect reward) {
        // Not applicable during the 'Make Action' phase
        return ActionResult.FAILURE;
    }

    /**
     * Attempts to perform any automatic actions for the current player during the "Make Action" phase.
     * Checks if the player has any actions to perform and returns the appropriate {@link HasAction} status.
     *
     * @param player  the player attempting to perform automatic actions
     * @return {@code HasAction.WAITING_FOR_PLAYER_ACTION} if the player has actions to perform,
     *         {@code HasAction.NO_ACTION_POSSIBLE} if no actions are possible,
     *         {@code HasAction.AUTOMATIC_ACTION_DONE} if an automatic action was performed
     */
    @Override
    public HasAction tryToMakeAutomaticAction(PlayerOrder player) {
        List<HasAction> playerActions = new ArrayList<>();

        for(Location key: places.keySet()){
            InterfaceFigureLocation playerLoc = places.get(key);
            HasAction playerAction = playerLoc.tryToMakeAction(player);
            if((key == Location.HUT || key == Location.FIELD || key == Location.TOOL_MAKER)
                    && playerAction == HasAction.AUTOMATIC_ACTION_DONE){
                playerActions.add(HasAction.AUTOMATIC_ACTION_DONE);
            }
            if(playerAction == HasAction.WAITING_FOR_PLAYER_ACTION || playerAction == HasAction.AUTOMATIC_ACTION_DONE){
                playerActions.add(HasAction.WAITING_FOR_PLAYER_ACTION);
            }
        }
        if(playerActions.isEmpty()){
            return HasAction.NO_ACTION_POSSIBLE;
        }
        if(playerActions.contains(HasAction.AUTOMATIC_ACTION_DONE)){
            return HasAction.AUTOMATIC_ACTION_DONE;
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }
}