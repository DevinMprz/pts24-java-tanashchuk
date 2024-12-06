package sk.uniba.fmph.dcs.player_board;

import org.junit.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerToolsTest {

    private PlayerTools playerTools;


    @Test
    public void tryToAddOneTool() throws NoSuchFieldException, IllegalAccessException {
        playerTools = new PlayerTools();

        playerTools.addTool();
        assertEquals(1, playerTools.getTotalToolsCount());
        assertArrayEquals(new boolean[]{false, false, false}, playerTools.getUsedTools());
        assertArrayEquals(new int[]{1, -1, -1, -1, -1, -1}, playerTools.getTools());
    }

    @Test
    public void tryToAddSomeTools(){
        playerTools = new PlayerTools();

        for(int i = 0; i < 7; i++){
            playerTools.addTool();
        }
        assertEquals(7, playerTools.getTotalToolsCount());
        assertArrayEquals(new boolean[]{false, false, false}, playerTools.getUsedTools());
        assertArrayEquals(new int[]{3, 2, 2, -1, -1, -1}, playerTools.getTools());
    }

    @Test
    public void addMoreThanPossibleTools(){

        playerTools = new PlayerTools();

        for(int i = 0; i < 15; i++){
            playerTools.addTool();
        }
        assertEquals(12, playerTools.getTotalToolsCount());
        assertArrayEquals(new boolean[]{false, false, false}, playerTools.getUsedTools());
        assertArrayEquals(new int[]{4, 4, 4, -1, -1, -1}, playerTools.getTools());
    }

    @Test
    public void addSingleUseTool(){
        playerTools = new PlayerTools();

        for(int i = 0; i < 7; i++){
            playerTools.addTool();
        }

        playerTools.addSingleUseTool(2);
        assertArrayEquals(new boolean[]{false, false, false}, playerTools.getUsedTools());
        assertEquals(9, playerTools.getTotalToolsCount());
        assertEquals(9, playerTools.getRoundToolsCount());
        assertArrayEquals(new int[]{3, 2, 2, 2, -1, -1}, playerTools.getTools());



        //Add another SingleUseTool with power 3
        playerTools.addSingleUseTool(3);
        assertEquals(12, playerTools.getRoundToolsCount());
        assertEquals(12, playerTools.getTotalToolsCount());
        assertArrayEquals(new boolean[]{false, false, false}, playerTools.getUsedTools());
        assertArrayEquals(new int[]{3, 2, 2, 2, 3, -1}, playerTools.getTools());

    }
    @Test
    public void useTool(){
        playerTools = new PlayerTools();

        for(int i = 0; i < 7; i++){
            playerTools.addTool();
        }

        assertEquals(Optional.of(2), playerTools.useTool(2));
        assertEquals(7, playerTools.getTotalToolsCount());
        assertArrayEquals(new boolean[]{false, false, true}, playerTools.getUsedTools());
        assertEquals(5, playerTools.getRoundToolsCount());
        assertArrayEquals(new int[]{3, 2, 2, -1, -1, -1}, playerTools.getTools());

        assertEquals(Optional.empty(), playerTools.useTool(2));
        assertEquals(Optional.empty(), playerTools.useTool(3));
        assertEquals(Optional.empty(), playerTools.useTool(6));
    }

    @Test
    public void useSingleUseTool(){
        playerTools = new PlayerTools();

        for(int i = 0; i < 7; i++){
            playerTools.addTool();
        }

        playerTools.addSingleUseTool(2);
        playerTools.addSingleUseTool(3);
        assertEquals(12, playerTools.getTotalToolsCount());
        assertArrayEquals(new boolean[]{false, false, false}, playerTools.getUsedTools());



        assertEquals(Optional.of(2), playerTools.useTool(3));
        assertEquals(10, playerTools.getTotalToolsCount());
        assertArrayEquals(new boolean[]{false, false, false}, playerTools.getUsedTools());
        assertEquals(10, playerTools.getRoundToolsCount());
        assertArrayEquals(new int[]{3, 2, 2, -1, 3, -1}, playerTools.getTools());

        assertEquals(Optional.of(3), playerTools.useTool(4));
        assertEquals(7, playerTools.getTotalToolsCount());
        assertArrayEquals(new boolean[]{false, false, false}, playerTools.getUsedTools());
        assertEquals(7, playerTools.getRoundToolsCount());
        assertArrayEquals(new int[]{3, 2, 2, -1, -1, -1}, playerTools.getTools());

        //Use SingleUsesTool not in order
        playerTools.addSingleUseTool(2);
        playerTools.addSingleUseTool(3);
        playerTools.addSingleUseTool(4);

        assertEquals(Optional.of(3), playerTools.useTool(4));
        assertEquals(13, playerTools.getTotalToolsCount());
        assertArrayEquals(new boolean[]{false, false, false}, playerTools.getUsedTools());
        assertEquals(13, playerTools.getRoundToolsCount());
        assertArrayEquals(new int[]{3, 2, 2, 2, -1, 4}, playerTools.getTools());


        assertEquals(Optional.of(4),playerTools.useTool(5));
        assertEquals(9, playerTools.getTotalToolsCount());
        assertArrayEquals(new boolean[]{false, false, false}, playerTools.getUsedTools());
        assertEquals(9, playerTools.getRoundToolsCount());
        assertArrayEquals(new int[]{3, 2, 2, 2, -1, -1}, playerTools.getTools());


        assertEquals(Optional.of(2),playerTools.useTool(3));
        assertEquals(7, playerTools.getTotalToolsCount());
        assertArrayEquals(new boolean[]{false, false, false}, playerTools.getUsedTools());
        assertEquals(7, playerTools.getRoundToolsCount());
        assertArrayEquals(new int[]{3, 2, 2, -1, -1, -1}, playerTools.getTools());
    }


    @Test
    public void testHasSufficientTools(){
        playerTools = new PlayerTools();

        for(int i = 0; i < 7; i++){
            playerTools.addTool();
        }

        assertTrue(playerTools.hasSufficientTools(5));
        assertTrue(playerTools.hasSufficientTools(7));
        assertFalse(playerTools.hasSufficientTools(8));

        for(int i = 0; i < 5; i++){
            playerTools.addTool();
        }

        assertTrue(playerTools.hasSufficientTools(12));
        assertFalse(playerTools.hasSufficientTools(13));

        playerTools.addSingleUseTool(3);
        assertTrue(playerTools.hasSufficientTools(15));

        playerTools.useTool(2);

        assertFalse(playerTools.hasSufficientTools(12));
        assertTrue(playerTools.hasSufficientTools(11));

        playerTools.newTurn();
        assertTrue(playerTools.hasSufficientTools(15));

        playerTools.useTool(3);
        assertTrue(playerTools.hasSufficientTools(12));
        assertFalse(playerTools.hasSufficientTools(15));

        playerTools.newTurn();

        assertFalse(playerTools.hasSufficientTools(15));
    }
}