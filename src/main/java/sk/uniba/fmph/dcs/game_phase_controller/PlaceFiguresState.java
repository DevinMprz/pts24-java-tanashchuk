package sk.uniba.fmph.dcs.game_phase_controller;

import java.util.*;

import sk.uniba.fmph.dcs.stone_age.*;

/**
 * Represents the game phase where players place their figures on locations.
 * Only figure placement actions are valid during this phase.
 *
 * Note: This class does not check if it's the player's turn - that validation
 * is handled by GamePhaseController before any actions are delegated here.
 * This separation of concerns keeps turn management logic in one place.
 */
public class PlaceFiguresState implements InterfaceGamePhaseState {

    /**
     * Maps game locations to their corresponding figure placement handlers
     */
    private final Map<Location, InterfaceFigureLocation> places;
    private final List<Player> players;


    /**
     * Creates a new PlaceFiguresState with the given location mappings
     *
     * @param places Map of locations to their figure placement handlers
     */
    public PlaceFiguresState(Map<Location, InterfaceFigureLocation> places, List<Player> players) {
        this.places = places;
        this.players = players;
    }

    /**
     * Attempts to place figures at the specified location
     *
     * @param player The player attempting to place figures
     * @param location The location where figures should be placed
     * @param figuresCount Number of figures to place
     * @return ACTION_DONE if placement successful, FAILURE otherwise
     */
    @Override
    public ActionResult placeFigures(PlayerOrder player, Location location, int figuresCount) {
        InterfaceFigureLocation place = places.get(location);
        if (place == null) {
            return ActionResult.FAILURE;
        }

        if(place.placeFigures(player, figuresCount)){
            return ActionResult.ACTION_DONE;
        }else{
            return ActionResult.FAILURE;
        }
    }

    /**
     * Checks if the player can make any automatic figure placements
     *
     * @param player The player to check for possible actions
     * @return WAITING_FOR_PLAYER_ACTION if player has figures to place,
     *         NO_ACTION_POSSIBLE if no figures can be placed
     */
    @Override
    public HasAction tryToMakeAutomaticAction(PlayerOrder player) {
        for(Player currPl : players){
            if(currPl.getPlayerOrder().equals(player)
                    && currPl.getPlayerBoard().hasFigures(1)){
                return HasAction.WAITING_FOR_PLAYER_ACTION;
            }
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    // The following methods return FAILURE as they are not valid actions during the Place Figures phase

    @Override
    public ActionResult makeAction(PlayerOrder player, Location location,
                                   Collection<Effect> inputResources, Collection<Effect> outputResources) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult skipAction(PlayerOrder player, Location location) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult useTools(PlayerOrder player, int toolIndex) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult noMoreToolsThisThrow(PlayerOrder player) {
        return ActionResult.FAILURE;
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
}