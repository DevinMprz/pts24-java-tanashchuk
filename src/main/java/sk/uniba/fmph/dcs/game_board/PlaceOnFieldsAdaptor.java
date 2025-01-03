package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.*;

import java.util.Collection;


public class PlaceOnFieldsAdaptor implements InterfaceFigureLocationInternal {
    private final ToolMakerHutFields fields;
    /**
     * Constructs a new {@code PlaceOnFieldsAdaptor} with the specified {@link ToolMakerHutFields}.
     *
     * @param toolMakerHutFields the {@link ToolMakerHutFields} instance used for managing fields
     */
    public PlaceOnFieldsAdaptor(final ToolMakerHutFields toolMakerHutFields){
        this.fields = toolMakerHutFields;
    }

    /**
     * Attempts to place the specified number of figures on the fields location for the given player.
     * If successful, the player's figures are deducted accordingly.
     *
     * @param player the player attempting to place figures
     * @param figureCount the number of figures to place
     * @return {@code true} if the figures were successfully placed; {@code false} otherwise
     */
    @Override
    public boolean placeFigures(Player player, int figureCount) {
        if(tryToPlaceFigures(player, figureCount).equals(HasAction.AUTOMATIC_ACTION_DONE)){
            if(fields.placeOnFields(player)){
                player.getPlayerBoard().takeFigures(figureCount);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the specified number of figures can be placed on the fields location for the given player.
     *
     * @param player the player attempting to place figures
     * @param count the number of figures to check
     * @return a {@link HasAction} indicating whether the action is possible
     */
    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if(!player.getPlayerBoard().hasFigures(count)){
            return HasAction.NO_ACTION_POSSIBLE;
        }

        if(count != 1){
            return HasAction.NO_ACTION_POSSIBLE;
        }

        if(!fields.canPlaceOnFields(player)){
            return HasAction.NO_ACTION_POSSIBLE;
        }

        return HasAction.AUTOMATIC_ACTION_DONE;
    }

    /**
     * Executes an action on the fields location for the given player.
     *
     * @param player the player performing the action
     * @param inputResources the input resources required for the action
     * @param outputResources the output resources generated by the action
     * @return an {@link ActionResult} indicating the outcome of the action
     */
    @Override
    public ActionResult makeAction(Player player, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        if(fields.actionFields(player)){
            return ActionResult.ACTION_DONE;
        }
        return ActionResult.FAILURE;
    }

    /**
     * Indicates whether the action can be skipped at the fields location.
     * This implementation always returns {@code false}, as skipping is not allowed.
     *
     * @param player the player attempting to skip the action
     * @return {@code false}, as skipping is not supported
     */
    @Override
    public boolean skipAction(Player player) {
        return false;
    }

    /**
     * Does nothing.
     *
     * @param player the player attempting to make an action
     */
    @Override
    public HasAction tryToMakeAction(Player player) {
        return null;
    }

    /**
     * Resets the fields location for a new turn. This implementation always returns {@code false},
     * indicating no reset behavior is required.
     *
     * @return {@code false}, as no reset is implemented
     */
    @Override
    public boolean newTurn() {
        return false;
    }
    /**
     * Returns the current state of the fields location as a string.
     *
     * @return the state of the fields location
     */
    @Override
    public String state() {
        return fields.state();
    }
}