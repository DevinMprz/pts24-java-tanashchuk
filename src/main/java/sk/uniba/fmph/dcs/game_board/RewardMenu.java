package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * The {@code RewardMenu} class manages the reward distribution process in the game. It allows players
 * to claim rewards from a menu based on their turn order and checks the availability of rewards
 * before granting them.
 *
 * <p>This class implements the {@link InterfaceTakeReward} interface to handle reward-related actions
 * and supports automatic or manual claiming of rewards depending on the context.
 */
public class RewardMenu implements InterfaceTakeReward {

    private ArrayList<Effect> rewards;
    private ArrayList<Player> players;
    private ArrayList<PlayerOrder> remainingPlayers;

    /**
     * Constructs a new {@code RewardMenu} with the specified players. It initializes the list of
     * remaining players eligible to claim rewards in turn order.
     *
     * @param players an array of players participating in the reward menu
     */
    public RewardMenu(Collection<Player> players) {
        this.players = new ArrayList<>(players);
        this.remainingPlayers = new ArrayList<>();
        this.rewards = new ArrayList<>();
        for (Player player : players) {
            remainingPlayers.add(player.getPlayerOrder());
        }
    }

    /**
     * Initializes the reward menu with the specified list of effects (rewards).
     *
     * @param menu an array of {@link Effect} objects representing the available rewards
     */
    public void initiate(Effect[] menu) {
        this.rewards.addAll(List.of(menu));
    }

    /**
     * Allows a player to claim a reward from the menu, provided the reward is available and the player
     * is eligible to claim it.
     *
     * @param player the {@link PlayerOrder} of the player attempting to take a reward
     * @param reward the {@link Effect} representing the reward to be claimed
     * @return {@code true} if the reward was successfully claimed; {@code false} otherwise
     */
    @Override
    public boolean takeReward(PlayerOrder player, Effect reward) {
        if (!rewards.contains(reward)) {
            return false;
        }
        if (!remainingPlayers.contains(player)) {
            return false;
        }
        for (Player p : players) {
            if (p.getPlayerOrder().equals(player)) {
                remainingPlayers.remove(player);
                rewards.remove(reward);
                p.getPlayerBoard().giveEffect(List.of(reward));
                return true;
            }
        }
        return false;
    }

    /**
     * Determines the possible action for the specified player with respect to the reward menu.
     *
     * @param player the {@link PlayerOrder} of the player attempting to take an action
     * @return a {@link HasAction} indicating the action's feasibility:
     *         <ul>
     *           <li>{@code NO_ACTION_POSSIBLE} if no action can be performed</li>
     *           <li>{@code AUTOMATIC_ACTION_DONE} if an automatic action was taken</li>
     *           <li>{@code WAITING_FOR_PLAYER_ACTION} if the player must take an action manually</li>
     *         </ul>
     */
    @Override
    public HasAction tryMakeAction(PlayerOrder player) {
        if (rewards.isEmpty()) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        if (!remainingPlayers.contains(player)) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        if (rewards.size() == 1) {
            for (Player p : players) {
                if (p.getPlayerOrder().equals(player)) {
                    p.getPlayerBoard().giveEffect(rewards);
                    remainingPlayers.remove(player);
                    return HasAction.AUTOMATIC_ACTION_DONE;
                }
            }
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    /**
     * Returns a JSON string representing the current state of the reward menu.
     *
     * <p>The JSON object contains the following fields:
     * <ul>
     *   <li>{@code "rewards"}: the list of available rewards</li>
     *   <li>{@code "players"}: the array of players in the reward menu</li>
     *   <li>{@code "remainingPlayers"}: the list of players still eligible to claim rewards</li>
     * </ul>
     *
     * @return a JSON string representation of the reward menu state
     */
    public String State() {
        Map<String, Object> state = Map.of(
                "rewards", rewards,
                "players", players,
                "remainingPlayers", remainingPlayers
        );
        return new JSONObject(state).toString();
    }
}
