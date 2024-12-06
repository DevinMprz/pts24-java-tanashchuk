package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;
import sk.uniba.fmph.dcs.game_board.InterfaceFigureLocationInternal;

import java.util.*;

/**
 * The {@code ResourceSource} class represents a resource-producing location on the game board, such as
 * a forest, quarry, or river. Players can place figures to collect resources and use tools to enhance
 * the resource collection process.
 *
 * <p>This class implements the {@link InterfaceFigureLocationInternal} interface, providing functionality
 * for placing figures, making actions, and managing the state of the resource source.
 */
public class ResourceSource implements InterfaceFigureLocationInternal {

    private String name;
    private final Effect resource;
    private int maxFigures;
    private int maxFigureColors;
    private ArrayList<PlayerOrder> figures;
    private CurrentThrow currentThrow;
    private Player currentPlayer;

    /**
     * Constructs a new {@code ResourceSource} with the specified properties.
     *
     * @param name the name of the resource source (e.g., "Wood forest")
     * @param resource the type of resource produced (e.g., {@link Effect#WOOD})
     * @param maxFigures the maximum number of figures that can be placed on this resource source
     * @param maxFigureColors the maximum number of players who can place figures on this resource source
     */
    public ResourceSource(String name, Effect resource, int maxFigures, int maxFigureColors) {
        this.name = name;
        this.resource = resource;
        this.maxFigures = maxFigures;
        this.maxFigureColors = maxFigureColors;
        this.figures = new ArrayList<>();
        this.currentThrow = null;
        this.currentPlayer = null;
    }

    /**
     * Places figures on the resource source for a player if the placement is valid.
     *
     * @param player the player placing figures
     * @param figureCount the number of figures to place
     * @return {@code true} if the figures were successfully placed; {@code false} otherwise
     */
    @Override
    public boolean placeFigures(Player player, int figureCount) {
        if (this.figures.contains(player.getPlayerOrder())) {
            return false;
        }
        if (this.figures.size() + figureCount > this.maxFigures) {
            return false;
        }
        if (!player.getPlayerBoard().hasFigures(figureCount)) {
            return false;
        }
        ArrayList<PlayerOrder> figureColors = new ArrayList<>();
        for (PlayerOrder figure : this.figures) {
            if (!figureColors.contains(figure)) {
                figureColors.add(figure);
            }
        }
        if (figureColors.size() >= maxFigureColors) {
            return false;
        }
        for (int i = 0; i < figureCount; i++) {
            this.figures.add(player.getPlayerOrder());
        }
        player.getPlayerBoard().takeFigures(figureCount);
        return true;
    }

    /**
     * Checks if the player can place figures on the resource source and attempts to place them.
     *
     * @param player the player attempting to place figures
     * @param count the number of figures to place
     * @return a {@link HasAction} indicating whether the action was successful
     */
    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if (this.placeFigures(player, count)) {
            return HasAction.AUTOMATIC_ACTION_DONE;
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    /**
     * Handles resource collection actions for a player. This includes managing tool usage and finalizing
     * resource collection.
     *
     * @param player the player performing the action
     * @param inputResources the input effects (e.g., tools) used in the action
     * @param outputResources the output effects (not used here)
     * @return an {@link ActionResult} indicating the result of the action
     */
    @Override
    public ActionResult makeAction(Player player, Collection<Effect> inputResources, Collection<Effect> outputResources) {

        if (currentPlayer == null || currentThrow == null) {
            int countPlayerFigures = (int) figures.stream()
                    .filter(playerOrder -> playerOrder.equals(player.getPlayerOrder()))
                    .count();
            currentPlayer = player;
            currentThrow = new CurrentThrow();
            currentThrow.initiate(player, resource, countPlayerFigures);
            return ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE;
        }
        if (player != currentPlayer) {
            return ActionResult.FAILURE;
        }
        if (inputResources.isEmpty()) {
            currentThrow.finishUsingTools();
            currentThrow = null;
            currentPlayer = null;
            while (figures.contains(player.getPlayerOrder())) {
                figures.remove(player.getPlayerOrder());
            }
            return ActionResult.ACTION_DONE;
        }
        for (Effect inputResource : inputResources) {
            if (inputResource != Effect.TOOL) {
                return ActionResult.FAILURE;
            }
        }
        int usedToolCount = 0;
        for (Effect effect : inputResources) {
            for (int i = 0; i < 6; i++) {
                if (currentThrow.useTool(i)) {
                    usedToolCount++;
                    break;
                }
            }
        }
        if (usedToolCount != inputResources.size()) {
            return ActionResult.FAILURE;
        }
        figures.remove(player.getPlayerOrder());
        currentThrow.finishUsingTools();
        return ActionResult.ACTION_DONE;
    }
    //Cannot skip action
    @Override
    public boolean skipAction(Player player) {
        return false;
    }
    public ArrayList<PlayerOrder> getFigures() {
        return figures;
    }

    /**
     * Checks if the player can take an action at the resource source.
     *
     * @param player the player attempting the action
     * @return a {@link HasAction} indicating the feasibility of the action
     */
    @Override
    public HasAction tryToMakeAction(Player player) {
        if (!figures.contains(player.getPlayerOrder())){
            return HasAction.NO_ACTION_POSSIBLE;
        }
        if (currentThrow != null && currentThrow.canUseTools() && this.currentPlayer == player) {
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }
        if (currentPlayer == null && figures.contains(player.getPlayerOrder())) {
            currentPlayer = player;
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    /**
     * Resets the resource source for a new turn. Clears the figures, current player, and ongoing throw.
     *
     * @return {@code true} if the resource source was successfully reset; {@code false} otherwise
     */
    @Override
    public boolean newTurn() {
        this.currentThrow = null;
        this.currentPlayer = null;
        return false;
    }

    /**
     * Returns a JSON string representing the current state of the resource source.
     *
     * <p>The JSON object contains:
     * <ul>
     *   <li>{@code "name"}: the name of the resource source</li>
     *   <li>{@code "resource"}: the type of resource produced</li>
     *   <li>{@code "maxFigures"}: the maximum number of figures allowed</li>
     *   <li>{@code "maxFigureColors"}: the maximum number of players allowed</li>
     *   <li>{@code "figures"}: the list of player orders currently occupying the resource source</li>
     *   <li>{@code "currentThrow"}: the current throw in progress</li>
     *   <li>{@code "currentPlayer"}: the player currently performing actions</li>
     * </ul>
     *
     * @return a JSON string representation of the resource source's state
     */
    @Override
    public String state() {
        Map<String, Object> state;
        if (figures.isEmpty()) {
             state = Map.of(
                    "name", name,
                    "resource", resource,
                    "maxFigures", maxFigures,
                    "maxFigureColors", maxFigureColors,
                    "figures", new ArrayList<>(),
                    "currentThrow", currentThrow,
                    "currentPlayer", currentPlayer
            );
        }else{
            state = Map.of(
                    "name", name,
                    "resource", resource,
                    "maxFigures", maxFigures,
                    "maxFigureColors", maxFigureColors,
                    "figures", List.copyOf(figures),
                    "currentThrow", currentThrow == null ? "null" : currentThrow.state(),
                    "currentPlayer", currentPlayer == null ? "null" : currentPlayer
            );
        }
        return new JSONObject(state).toString();
    }
}
