package sk.uniba.fmph.dcs.game_board;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.player_board.*;
import sk.uniba.fmph.dcs.player_board.PlayerBoard;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.List;


public class RewardMenuTest {

    private RewardMenu rewardMenu;
    private Player mockPlayer1;
    private Player mockPlayer2;
    private PlayerBoardGameBoardFacade mockBoard1;
    private PlayerBoardGameBoardFacade mockBoard2;
    private Effect mockEffect1;
    private Effect mockEffect2;

    @Before
    public void setUp() {
        mockBoard1 = new PlayerBoardGameBoardFacade(new PlayerBoard());
        mockBoard2 = new PlayerBoardGameBoardFacade(new PlayerBoard());
        mockPlayer1 = new Player(new PlayerOrder(1, 4), mockBoard1);
        mockPlayer2 = new Player(new PlayerOrder(2,4), mockBoard2);

        // Mocking effects
        mockEffect1 = Effect.WOOD; // Dummy effect
        mockEffect2 = Effect.STONE; // Dummy effect

        // Creating a RewardMenu instance
        rewardMenu = new RewardMenu(List.of(mockPlayer1, mockPlayer2));
    }

    @Test
    public void testInitiate() {
        rewardMenu.initiate(new Effect[]{mockEffect1, mockEffect2});
        // Check if the rewards list was correctly initialized
        assertTrue(rewardMenu.State().contains(mockEffect1.toString()));
        assertTrue(rewardMenu.State().contains(mockEffect2.toString()));
    }

    @Test
    public void testTakeRewardSuccess() {
        rewardMenu.initiate(new Effect[]{mockEffect1, mockEffect2});

        boolean result = rewardMenu.takeReward(mockPlayer1.getPlayerOrder(), mockEffect1);
        assertTrue(result);

        // Check that the reward was removed from the list
        assertFalse(rewardMenu.State().contains(mockEffect1.toString()));

    }

    @Test
    public void testTakeRewardFailInvalidReward() {
        rewardMenu.initiate(new Effect[]{mockEffect2});

        boolean result = rewardMenu.takeReward(mockPlayer1.getPlayerOrder(), mockEffect1);
        assertFalse(result);
    }

    @Test
    public void testTakeRewardFailPlayerNotInQueue() {
        rewardMenu.initiate(new Effect[]{mockEffect1});

        rewardMenu.takeReward(mockPlayer1.getPlayerOrder(), mockEffect1);
        boolean result = rewardMenu.takeReward(mockPlayer1.getPlayerOrder(), mockEffect1);

        assertFalse(result);
    }

    @Test
    public void testTryMakeActionNoActionPossible() {
        HasAction result = rewardMenu.tryMakeAction(mockPlayer1.getPlayerOrder());
        assertEquals(HasAction.NO_ACTION_POSSIBLE, result);
    }

    @Test
    public void testTryMakeActionAutomaticActionDone() {
        rewardMenu.initiate(new Effect[]{mockEffect1});

        HasAction result = rewardMenu.tryMakeAction(mockPlayer1.getPlayerOrder());
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, result);

    }

    @Test
    public void testTryMakeActionWaitingForPlayerAction() {
        rewardMenu.initiate(new Effect[]{mockEffect1, mockEffect2});

        HasAction result = rewardMenu.tryMakeAction(mockPlayer1.getPlayerOrder());
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, result);
    }

    @Test
    public void testState() {
        rewardMenu.initiate(new Effect[]{mockEffect1, mockEffect2});

        String state = rewardMenu.State();

        // Verify that the state contains the expected elements
        assertTrue(state.contains("rewards"));
        assertTrue(state.contains("players"));
        assertTrue(state.contains("remainingPlayers"));
    }
}
