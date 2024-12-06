package sk.uniba.fmph.dcs.game_board;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

import static org.junit.Assert.*;

public class ResourceSourceTest {
    private ResourceSource resourceSource;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    @Before
    public void setUp() {
        player1 = new Player(new PlayerOrder(1, 4), new PlayerBoardGameBoardFacade(new sk.uniba.fmph.dcs.player_board.PlayerBoard()));
        player2 = new Player(new PlayerOrder(2, 4), new PlayerBoardGameBoardFacade(new sk.uniba.fmph.dcs.player_board.PlayerBoard()));
        player3 = new Player(new PlayerOrder(3, 4), new PlayerBoardGameBoardFacade(new sk.uniba.fmph.dcs.player_board.PlayerBoard()));
        player4 = new Player(new PlayerOrder(4, 4), new PlayerBoardGameBoardFacade(new sk.uniba.fmph.dcs.player_board.PlayerBoard()));
        resourceSource = new ResourceSource("Wood forest", Effect.WOOD, 5, 2);
    }

    @Test
    public void testPlaceFigures_Success() {
        boolean result = resourceSource.placeFigures(player1, 3);
        assertTrue(result);
       // assertEquals(3, resourceSource.getFigures().size());
    }

    @Test
    public void testPlaceFigures_ExceedMaxFigures() {
        boolean result1 = resourceSource.placeFigures(player1, 3);
        assertTrue(result1);
        boolean result2 = resourceSource.placeFigures(player2, 3);
        assertFalse(result2);
    }

    @Test
    public void testPlaceFigures_ExceedMaxColors() {
        resourceSource.placeFigures(player1, 2);
        resourceSource.placeFigures(player2, 2);
        boolean result = resourceSource.placeFigures(player3, 1);
        assertFalse(result);
    }

    @Test
    public void testTryToPlaceFigures_Success() {
        HasAction result = resourceSource.tryToPlaceFigures(player1, 2);
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, result);
    }

    @Test
    public void testTryToPlaceFigures_Failure() {
        resourceSource.placeFigures(player1, 4);
        HasAction result = resourceSource.tryToPlaceFigures(player2, 2);
        assertEquals(HasAction.NO_ACTION_POSSIBLE, result);
    }

    @Test
    public void testMakeAction_Success() {
        resourceSource.placeFigures(player1, 2);
        ActionResult result = resourceSource.makeAction(player1, new ArrayList<>(), new ArrayList<>());
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, result);
    }

    @Test
    public void testSkipAction_NotAllowed() {
        boolean result = resourceSource.skipAction(player1);
        assertFalse(result);
    }

    @Test
    public void testTryToMakeAction_Success() {
        resourceSource.placeFigures(player1, 2);
        HasAction result = resourceSource.tryToMakeAction(player1);
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, result);
    }

    @Test
    public void testTryToMakeAction_NoActionPossible() {
        HasAction result = resourceSource.tryToMakeAction(player2);
        assertEquals(HasAction.NO_ACTION_POSSIBLE, result);
    }

    @Test
    public void testState_ContainsFiguresAndProperties() {
        resourceSource.placeFigures(player1, 2);
        String state = resourceSource.state();
        assertTrue(state.contains("Wood forest"));
        assertTrue(state.contains("figures"));
        assertTrue(state.contains("maxFigures"));
    }
}
