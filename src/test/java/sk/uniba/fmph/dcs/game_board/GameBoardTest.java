package sk.uniba.fmph.dcs.game_board;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

public class GameBoardTest {

    private GameBoard gameBoard;
    private List<Player> players;
    private Building[] buildings;
    private CivilizationCard[] civilizationCards;

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

    @Before
    public void setUp() {
        // Create mock players
        players = new ArrayList<>();

        MockPlayerBoard mockBoard1 = new MockPlayerBoard();
        MockPlayerBoard mockBoard2 = new MockPlayerBoard();
        MockPlayer mockPlayer1 = new MockPlayer(0, mockBoard1);
        MockPlayer mockPlayer2 = new MockPlayer(1, mockBoard2);
        players.add(mockPlayer1);
        players.add(mockPlayer2);

        // Create mock buildings
        buildings = new Building[4];
        for (int i = 0; i < 4; i++) {
            buildings[i] = new SimpleBuilding(List.of(Effect.WOOD));
        }

        // Create mock civilization cards
        civilizationCards = new CivilizationCard[4];
        for (int i = 0; i < 4; i++) {
            civilizationCards[i] = new CivilizationCard(List.of(ImmediateEffect.CLAY), List.of(EndOfGameEffect.FARMER));
        }

        // Initialize the GameBoard
        gameBoard = new GameBoard(players, buildings, civilizationCards);
    }

    @Test
    public void testInitialization() {
        String state = gameBoard.state();

        // Verify that key locations are present in the state
        assertTrue(state.contains("TOOL_MAKER"));
        assertTrue(state.contains("HUT"));
        assertTrue(state.contains("FIELD"));
        assertTrue(state.contains("FOREST"));
        assertTrue(state.contains("CLAY_MOUND"));
        assertTrue(state.contains("QUARY"));
        assertTrue(state.contains("RIVER"));
        assertTrue(state.contains("BUILDING_TILE1"));
        assertTrue(state.contains("BUILDING_TILE2"));
        assertTrue(state.contains("BUILDING_TILE3"));
        assertTrue(state.contains("BUILDING_TILE4"));
        assertTrue(state.contains("CIVILISATION_CARD1"));
        assertTrue(state.contains("CIVILISATION_CARD2"));
        assertTrue(state.contains("CIVILISATION_CARD3"));
        assertTrue(state.contains("CIVILISATION_CARD4"));
    }

    @Test
    public void testLocationsState() {
        // Check that each location's state is non-empty and valid
        String state = gameBoard.state();

        assertTrue(state.contains("Wood forest"));
        assertTrue(state.contains("Clay mound"));
        assertTrue(state.contains("Stone quarry"));
        assertTrue(state.contains("Gold river"));
    }

    @Test
    public void testBuildingTilesInitialization() {
        // Verify that each building tile has been initialized correctly
        String state = gameBoard.state();

        assertTrue(state.contains("Building1"));
        assertTrue(state.contains("Building2"));
        assertTrue(state.contains("Building3"));
        assertTrue(state.contains("Building4"));
    }

    @Test
    public void testCivilizationCardInitialization() {
        // Verify that each civilization card has been initialized correctly
        String state = gameBoard.state();

        assertTrue(state.contains("Card1"));
        assertTrue(state.contains("Card2"));
        assertTrue(state.contains("Card3"));
        assertTrue(state.contains("Card4"));
    }

    @Test
    public void testStateFormat() {
        // Verify the overall format of the state is a valid JSON string
        String state = gameBoard.state();

        assertTrue(state.startsWith("{"));
        assertTrue(state.endsWith("}"));
        assertTrue(state.contains("TOOL_MAKER"));
    }
}
