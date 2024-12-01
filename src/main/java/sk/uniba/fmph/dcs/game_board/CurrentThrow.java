package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;
import java.util.Optional;

import java.util.*;

/**
 * The {@code CurrentThrow} class represents the process of managing a dice throw for a player in the game.
 * It calculates throw results, allows the use of tools to modify the results, and applies the results
 * as resources or effects to the player.
 *
 * <p>This class implements the {@link InterfaceToolUse} interface to manage tool usage during the throw.
 */
public class CurrentThrow implements InterfaceToolUse {

    private Effect throwsFor;
    private int throwResult;
    private Throw throw_ = new Throw();
    private Player player;
    private int dices;
    private int[] dicesResults;
    private boolean toolsUsed = false;
    private boolean finished = false;

    /**
     * Initializes a new dice throw for a player, specifying the target effect and number of dice to roll.
     * This method calculates the initial throw result and stores the dice outcomes.
     *
     * @param player the player performing the throw
     * @param effect the effect for which the dice are being thrown (e.g., resource type)
     * @param dices the number of dice to roll
     */
    public void initiate(Player player, Effect effect, int dices) {
        this.throwsFor = effect;
        this.player = player;
        this.dices = dices;
        int[] dicesResults = throw_.throw_(dices);
        this.dicesResults = dicesResults;
        throwResult = Arrays.stream(dicesResults).reduce(0, Integer::sum);
    }

    /**
     * Uses a tool to modify the throw result. The tool adds its value to the throw result.
     *
     * @param idx the index of the tool to use
     * @return {@code true} if the tool was successfully used; {@code false} otherwise
     */
    @Override
    public boolean useTool(int idx) {
        if (this.finished) {
            return false;
        }

        Optional<Integer> a = player.playerBoard().useTool(idx);
        if (a.isPresent()) {
            throwResult += a.get();
            toolsUsed = true;
            return true;
        }
        return false;
    }

    /**
     * Checks if the player can use tools during this throw.
     *
     * @return {@code true} if the player has at least one tool available; {@code false} otherwise
     */
    @Override
    public boolean canUseTools() {
        return this.player.playerBoard().hasSufficientTools(1);
    }

    /**
     * Finalizes the tool usage and applies the throw result as resources or effects to the player.
     *
     * @return {@code true} if the process was successfully finished; {@code false} otherwise
     */
    @Override
    public boolean finishUsingTools() {
        if (this.finished) {
            return false;
        }

        if (!throwsFor.isResourceOrFood()) {
            return false;
        }

        List<Effect> effects = new ArrayList<>();

        switch (throwsFor) {
            case WOOD:
                for (int i = 0; i < Math.floorDiv(throwResult, throwsFor.points()); i++) {
                    effects.add(Effect.WOOD);
                }
                break;
            case CLAY:
                for (int i = 0; i < Math.floorDiv(throwResult, throwsFor.points()); i++) {
                    effects.add(Effect.CLAY);
                }
                break;
            case STONE:
                for (int i = 0; i < Math.floorDiv(throwResult, throwsFor.points()); i++) {
                    effects.add(Effect.STONE);
                }
                break;
            case GOLD:
                for (int i = 0; i < Math.floorDiv(throwResult, throwsFor.points()); i++) {
                    effects.add(Effect.GOLD);
                }
                break;
            case FOOD:
                for (int i = 0; i < Math.floorDiv(throwResult, throwsFor.points()); i++) {
                    effects.add(Effect.FOOD);
                }
                break;
            default:
                break;
        }

        player.playerBoard().giveEffect(effects);
        finished = true;
        return true;
    }

    /**
     * Returns a JSON string representing the current state of the throw.
     *
     * <p>The JSON object contains the following fields:
     * <ul>
     *   <li>{@code "throwsFor"}: the target effect of the throw</li>
     *   <li>{@code "throwResult"}: the total result of the throw</li>
     *   <li>{@code "player"}: the player performing the throw</li>
     *   <li>{@code "dices"}: the number of dice rolled</li>
     *   <li>{@code "dicesResults"}: an array of individual dice results</li>
     *   <li>{@code "toolsUsed"}: whether tools have been used in this throw</li>
     * </ul>
     *
     * @return a JSON string representation of the throw state
     */
    public String state() {
        Map<String, Object> state = Map.of(
                "throwsFor", throwsFor,
                "throwResult", throwResult,
                "player", player,
                "dices", dices,
                "dicesResults", dicesResults,
                "toolsUsed", toolsUsed
        );
        return new JSONObject(state).toString();
    }
}


