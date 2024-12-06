package sk.uniba.fmph.dcs.game_board;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.player_board.PlayerBoard;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.List;
import java.util.Optional;

public class CurrentThrowTest {

    private CurrentThrow currentThrow;
    private Player mockPlayer;
    private Effect mockEffect;


    @Before
    public void setUp() {
        currentThrow = new CurrentThrow();
        // Mocking a player
        mockPlayer = new Player(new PlayerOrder(1, 4), new PlayerBoardGameBoardFacade(new PlayerBoard()));
        mockEffect = Effect.WOOD;
    }

    @Test
    public void testInitiate() {
        currentThrow.initiate(mockPlayer, mockEffect, 3);

        String state = currentThrow.state();
        assertTrue(state.contains("throwsFor"));
        assertTrue(state.contains("WOOD"));
        assertTrue(state.contains("dices"));
        assertTrue(state.contains("dicesResults"));
    }

    @Test
    public void testUseToolSuccess() {
        mockPlayer.getPlayerBoard().giveEffect(List.of(Effect.TOOL));
        // Assuming the player starts with tools available
        currentThrow.initiate(mockPlayer, mockEffect, 3);

        boolean result = currentThrow.useTool(0); // Tool index 1
        assertTrue(result);
    }

    @Test
    public void testUseToolFailureWhenFinished() {
        currentThrow.initiate(mockPlayer, mockEffect, 3);
        currentThrow.finishUsingTools();

        boolean result = currentThrow.useTool(1);
        assertFalse(result);
    }

    @Test
    public void testCanUseTools() {
        mockPlayer.getPlayerBoard().giveEffect(List.of(Effect.TOOL));
        currentThrow.initiate(mockPlayer, mockEffect, 4);
        boolean result = currentThrow.canUseTools();
        assertTrue(result);
    }

    @Test
    public void testFinishUsingToolsSuccess() {
        currentThrow.initiate(mockPlayer, Effect.WOOD, 3);

        boolean result = currentThrow.finishUsingTools();
        assertTrue(result);
    }

    @Test
    public void testFinishUsingToolsFailureWhenNotResourceOrFood() {
        // Assuming there's an Effect that is not a resource or food
        Effect invalidEffect = Effect.ONE_TIME_TOOL2; // Example invalid effect
        currentThrow.initiate(mockPlayer, invalidEffect, 3);

        boolean result = currentThrow.finishUsingTools();
        assertFalse(result);
    }

    @Test
    public void testState() {
        currentThrow.initiate(mockPlayer, mockEffect, 3);
        String state = currentThrow.state();

        assertTrue(state.contains("throwsFor"));
        assertTrue(state.contains("WOOD"));
        assertTrue(state.contains("dices"));
        assertTrue(state.contains("dicesResults"));
    }
}
