package sk.uniba.fmph.dcs.game_board;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.List;
import java.util.Optional;

public class CurrentThrowTest {

    private CurrentThrow currentThrow;
    private Player mockPlayer;
    private Effect mockEffect;

    private static class MockPlayer implements Player {
        private final PlayerOrder order;
        private final PlayerBoard board;

        public MockPlayer(int orderNum, PlayerBoard board) {
            this.order = new PlayerOrder(orderNum, 2);
            this.board = board;
        }

        @Override
        public PlayerOrder playerOrder() {
            return order;
        }

        @Override
        public InterfacePlayerBoardGameBoard playerBoard() {
            return (InterfacePlayerBoardGameBoard) board;
        }
    }
    private static class MockPlayerBoard implements PlayerBoard {
        private boolean hasFiguresResponse = true;

        @Override
        public boolean hasFigures(int count) {
            return hasFiguresResponse;
        }

        @Override
        public void giveEffect(Effect[] stuff) {

        }

        public void setHasFiguresResponse(boolean response) {
            this.hasFiguresResponse = response;
        }
    }
    @Before
    public void setUp() {
        currentThrow = new CurrentThrow();

        MockPlayerBoard mockPlayerBoard = new MockPlayerBoard();
        // Mocking a player
        mockPlayer = new MockPlayer(0, mockPlayerBoard);
        mockPlayer.playerBoard().giveEffect(List.of(new Effect[]{Effect.ONE_TIME_TOOL2}));
        // Mocking an effect (assuming Effect is an enum or a class)
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
        // Assuming the player starts with tools available
        currentThrow.initiate(mockPlayer, mockEffect, 3);

        boolean result = currentThrow.useTool(1); // Tool index 1
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
        currentThrow.initiate(mockPlayer, mockEffect, 3);
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
