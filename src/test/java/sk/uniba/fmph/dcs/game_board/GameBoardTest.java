package sk.uniba.fmph.dcs.game_board;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.player_board.PlayerBoard;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;
import sk.uniba.fmph.dcs.stone_age.*;

import java.lang.reflect.Field;
import java.util.*;
    // locations, building tiles, cardplace
public class GameBoardTest {

    private GameBoard gameBoard;
    private List<Player> players;
        private String getBuildingType(Building building) {
            if (building instanceof SimpleBuilding simpleBuilding) {
                return switch (simpleBuilding.getRequiredResources().toString()) {
                    case "[WOOD, WOOD, CLAY]" -> "SimpleBuilding1";
                    case "[WOOD, STONE, GOLD]" -> "SimpleBuilding2";
                    case "[WOOD, STONE, STONE]" -> "SimpleBuilding3";
                    case "[STONE, CLAY, CLAY]" -> "SimpleBuilding4";
                    case "[STONE, STONE, WOOD]" -> "SimpleBuilding5";
                    case "[WOOD, CLAY, STONE]" -> "SimpleBuilding6";
                    case "[WOOD, WOOD, WOOD]" -> "SimpleBuilding7";
                    case "[STONE, STONE, STONE]" -> "SimpleBuilding8";
                    case "[WOOD, CLAY, GOLD]" -> "SimpleBuilding9";
                    case "[STONE, CLAY, GOLD]" -> "SimpleBuilding10";
                    default -> "Unknown SimpleBuilding";
                };
            } else if (building instanceof VariableBuilding) {
                return "VariableBuilding";
            } else if (building instanceof ArbitraryBuilding arbitraryBuilding) {
                return switch (arbitraryBuilding.getMaxNumberOfResources()) {
                    case 1 -> "ArbitraryBuilding1";
                    case 2 -> "ArbitraryBuilding2";
                    case 3 -> "ArbitraryBuilding3";
                    case 4 -> "ArbitraryBuilding4";
                    case 5 -> "ArbitraryBuilding5";
                    case 6 -> "ArbitraryBuilding6";
                    case 7 -> "ArbitraryBuilding7";
                    default -> "Unknown ArbitraryBuilding";
                };
            }
            return "Unknown Building";
        }

    @Before
    public void setUp() {
        // Create mock players
        players = new ArrayList<>();

        Player mockPlayer1 = new Player(new PlayerOrder(1, 4), new PlayerBoardGameBoardFacade(new PlayerBoard()));
        Player mockPlayer2 = new Player(new PlayerOrder(2, 4), new PlayerBoardGameBoardFacade(new PlayerBoard()));
        Player mockPlayer3 = new Player(new PlayerOrder(3, 4), new PlayerBoardGameBoardFacade(new PlayerBoard()));
        Player mockPlayer4 = new Player(new PlayerOrder(4, 4), new PlayerBoardGameBoardFacade(new PlayerBoard()));
        players.add(mockPlayer1);
        players.add(mockPlayer2);
        players.add(mockPlayer3);
        players.add(mockPlayer4);

        // Initialize the GameBoard
        gameBoard = new GameBoard(players);
    }

    @Test
    public void testInitialization() {

        assertNotNull(gameBoard.getAllLocations().get(Location.BUILDING_TILE1));
        assertNotNull(gameBoard.getAllLocations().get(Location.BUILDING_TILE2));
        assertNotNull(gameBoard.getAllLocations().get(Location.BUILDING_TILE3));
        assertNotNull(gameBoard.getAllLocations().get(Location.BUILDING_TILE4));

        assertNotNull(gameBoard.getAllLocations().get(Location.CIVILISATION_CARD1));
        assertNotNull(gameBoard.getAllLocations().get(Location.CIVILISATION_CARD2));
        assertNotNull(gameBoard.getAllLocations().get(Location.CIVILISATION_CARD3));
        assertNotNull(gameBoard.getAllLocations().get(Location.CIVILISATION_CARD4));

        assertNotNull(gameBoard.getAllLocations().get(Location.FOREST));
        assertNotNull(gameBoard.getAllLocations().get(Location.QUARRY));
        assertNotNull(gameBoard.getAllLocations().get(Location.CLAY_MOUND));
        assertNotNull(gameBoard.getAllLocations().get(Location.RIVER));
        assertNotNull(gameBoard.getAllLocations().get(Location.HUNTING_GROUNDS));


        assertNotNull(gameBoard.getAllLocations().get(Location.TOOL_MAKER));
        assertNotNull(gameBoard.getAllLocations().get(Location.HUT));
        assertNotNull(gameBoard.getAllLocations().get(Location.FIELD));

    }
    @Test
    public void testInteractionWithForest() throws NoSuchFieldException, IllegalAccessException {
        InterfaceFigureLocation forest = gameBoard.getAllLocations().get(Location.FOREST);

        //Testing adding figures to forest
        assertTrue(forest.placeFigures(players.get(0).getPlayerOrder(), 5));
        //Adding more than possible figures to forest (already has 5)
        assertEquals(HasAction.NO_ACTION_POSSIBLE, forest.tryToPlaceFigures(players.get(1).getPlayerOrder(), 3));

        //Try to make action without tools
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, forest.tryToMakeAction(players.get(0).getPlayerOrder()));
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, forest.makeAction(players.get(0).getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));

        //Get needed tools to test currentThrow
        Field location = FigureLocationAdaptor.class.getDeclaredField("figureLocation");
        location.setAccessible(true);

        Field currentThrow = ResourceSource.class.getDeclaredField("currentThrow");
        currentThrow.setAccessible(true);

        Field dicesResults = CurrentThrow.class.getDeclaredField("throwResult");
        dicesResults.setAccessible(true);

        ResourceSource rs = (ResourceSource)location.get(forest);
        CurrentThrow round =  (CurrentThrow) currentThrow.get(rs);
        int results = (int) dicesResults.get(round);
        int countOfWood = results/Effect.WOOD.points();
        //Testing if player gets wood points
        assertEquals(ActionResult.ACTION_DONE, forest.makeAction(players.get(0).getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, forest.tryToMakeAction(players.get(0).getPlayerOrder()));
        for(int i = 0; i < countOfWood; i++){
            assertTrue(players.get(0).getPlayerBoard().takeResources(List.of(Effect.WOOD)));
        }
        assertFalse(players.get(0).getPlayerBoard().takeResources(List.of(Effect.WOOD)));


        players.get(1).getPlayerBoard().giveEffect(List.of(Effect.TOOL, Effect.TOOL));
        assertTrue(forest.placeFigures(players.get(1).getPlayerOrder(), 5));
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, forest.tryToMakeAction(players.get(1).getPlayerOrder()));
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, forest.makeAction(players.get(1).getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));

        rs = (ResourceSource)location.get(forest);
        round =  (CurrentThrow) currentThrow.get(rs);
        results = (int) dicesResults.get(round);
        countOfWood = (results + 1)/Effect.WOOD.points();
        //Then check if player get specify countOfFood stone points
        assertEquals(ActionResult.ACTION_DONE, forest.makeAction(players.get(1).getPlayerOrder(), List.of(Effect.TOOL, Effect.TOOL), List.of(new Effect[0])));
        assertEquals(HasAction.NO_ACTION_POSSIBLE,forest.tryToMakeAction(players.get(1).getPlayerOrder()));
        for(int i = 0; i < countOfWood; i++){
            assertTrue(players.get(1).getPlayerBoard().takeResources(List.of(Effect.WOOD)));
        }
        assertFalse(players.get(1).getPlayerBoard().takeResources(List.of(Effect.WOOD)));

        assertFalse(forest.newTurn());

    }
    @Test
    public void testInteractionWithClayMound() throws NoSuchFieldException, IllegalAccessException {
            InterfaceFigureLocation clayMound = gameBoard.getAllLocations().get(Location.CLAY_MOUND);

            // Testing adding figures to clay mound
            assertTrue(clayMound.placeFigures(players.get(0).getPlayerOrder(), 5));
            // Adding more than possible figures to clay mound (already has 5)
            assertEquals(HasAction.NO_ACTION_POSSIBLE, clayMound.tryToPlaceFigures(players.get(1).getPlayerOrder(), 3));

            // Try to make action without tools
            assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, clayMound.tryToMakeAction(players.get(0).getPlayerOrder()));
            assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, clayMound.makeAction(players.get(0).getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));

            // Get needed tools to test currentThrow
            Field location = FigureLocationAdaptor.class.getDeclaredField("figureLocation");
            location.setAccessible(true);

            Field currentThrow = ResourceSource.class.getDeclaredField("currentThrow");
            currentThrow.setAccessible(true);

            Field dicesResults = CurrentThrow.class.getDeclaredField("throwResult");
            dicesResults.setAccessible(true);

            ResourceSource rs = (ResourceSource) location.get(clayMound);
            CurrentThrow round = (CurrentThrow) currentThrow.get(rs);
            int results = (int) dicesResults.get(round);
            int countOfClay = results / Effect.CLAY.points();
            // Testing if player gets clay points
            assertEquals(ActionResult.ACTION_DONE, clayMound.makeAction(players.get(0).getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));
            assertEquals(HasAction.NO_ACTION_POSSIBLE, clayMound.tryToMakeAction(players.get(0).getPlayerOrder()));
            for (int i = 0; i < countOfClay; i++) {
                assertTrue(players.get(0).getPlayerBoard().takeResources(List.of(Effect.CLAY)));
            }
            assertFalse(players.get(0).getPlayerBoard().takeResources(List.of(Effect.CLAY)));

            players.get(1).getPlayerBoard().giveEffect(List.of(Effect.TOOL, Effect.TOOL));
            assertTrue(clayMound.placeFigures(players.get(1).getPlayerOrder(), 5));
            assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, clayMound.tryToMakeAction(players.get(1).getPlayerOrder()));
            assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, clayMound.makeAction(players.get(1).getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));

            rs = (ResourceSource) location.get(clayMound);
            round = (CurrentThrow) currentThrow.get(rs);
            results = (int) dicesResults.get(round);
            countOfClay = (results + 2) / Effect.CLAY.points();
            // Then check if player gets specified count of clay points
            assertEquals(ActionResult.ACTION_DONE, clayMound.makeAction(players.get(1).getPlayerOrder(), List.of(Effect.TOOL, Effect.TOOL), List.of(new Effect[0])));
            assertEquals(HasAction.NO_ACTION_POSSIBLE, clayMound.tryToMakeAction(players.get(1).getPlayerOrder()));
            for (int i = 0; i < countOfClay; i++) {
                assertTrue(players.get(1).getPlayerBoard().takeResources(List.of(Effect.CLAY)));
            }
            assertFalse(players.get(1).getPlayerBoard().takeResources(List.of(Effect.CLAY)));

            assertFalse(clayMound.newTurn());
        }
    @Test
    public void testInteractionWithQuarry() throws NoSuchFieldException, IllegalAccessException {
            InterfaceFigureLocation quarry = gameBoard.getAllLocations().get(Location.QUARRY);

            // Testing adding figures to quarry
            assertTrue(quarry.placeFigures(players.get(0).getPlayerOrder(), 5));
            // Adding more than possible figures to quarry (already has 5)
            assertEquals(HasAction.NO_ACTION_POSSIBLE, quarry.tryToPlaceFigures(players.get(1).getPlayerOrder(), 3));

            // Try to make action without tools
            assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, quarry.tryToMakeAction(players.get(0).getPlayerOrder()));
            assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, quarry.makeAction(players.get(0).getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));

            // Get needed tools to test currentThrow
            Field location = FigureLocationAdaptor.class.getDeclaredField("figureLocation");
            location.setAccessible(true);

            Field currentThrow = ResourceSource.class.getDeclaredField("currentThrow");
            currentThrow.setAccessible(true);

            Field dicesResults = CurrentThrow.class.getDeclaredField("throwResult");
            dicesResults.setAccessible(true);

            ResourceSource rs = (ResourceSource) location.get(quarry);
            CurrentThrow round = (CurrentThrow) currentThrow.get(rs);
            int results = (int) dicesResults.get(round);
            int countOfStone = results / Effect.STONE.points();
            // Testing if player gets stone points
            assertEquals(ActionResult.ACTION_DONE, quarry.makeAction(players.get(0).getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));
            assertEquals(HasAction.NO_ACTION_POSSIBLE, quarry.tryToMakeAction(players.get(0).getPlayerOrder()));
            for (int i = 0; i < countOfStone; i++) {
                assertTrue(players.get(0).getPlayerBoard().takeResources(List.of(Effect.STONE)));
            }
            assertFalse(players.get(0).getPlayerBoard().takeResources(List.of(Effect.STONE)));

            players.get(1).getPlayerBoard().giveEffect(List.of(Effect.TOOL, Effect.TOOL));
            assertTrue(quarry.placeFigures(players.get(1).getPlayerOrder(), 5));
            assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, quarry.tryToMakeAction(players.get(1).getPlayerOrder()));
            assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, quarry.makeAction(players.get(1).getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));

            rs = (ResourceSource) location.get(quarry);
            round = (CurrentThrow) currentThrow.get(rs);
            results = (int) dicesResults.get(round);
            countOfStone = (results + 2) / Effect.STONE.points();
            // Then check if player gets specified count of stone points
            assertEquals(ActionResult.ACTION_DONE, quarry.makeAction(players.get(1).getPlayerOrder(), List.of(Effect.TOOL, Effect.TOOL), List.of(new Effect[0])));
            assertEquals(HasAction.NO_ACTION_POSSIBLE, quarry.tryToMakeAction(players.get(1).getPlayerOrder()));
            for (int i = 0; i < countOfStone; i++) {
                assertTrue(players.get(1).getPlayerBoard().takeResources(List.of(Effect.STONE)));
            }
            assertFalse(players.get(1).getPlayerBoard().takeResources(List.of(Effect.STONE)));

            assertFalse(quarry.newTurn());
        }
    @Test
    public void testInteractionWithRiver() throws NoSuchFieldException, IllegalAccessException {
            InterfaceFigureLocation river = gameBoard.getAllLocations().get(Location.RIVER);

            // Testing adding figures to river
            assertTrue(river.placeFigures(players.get(0).getPlayerOrder(), 5));
            // Adding more than possible figures to river (already has 5)
            assertEquals(HasAction.NO_ACTION_POSSIBLE, river.tryToPlaceFigures(players.get(1).getPlayerOrder(), 3));

            // Try to make action without tools
            assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, river.tryToMakeAction(players.get(0).getPlayerOrder()));
            assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, river.makeAction(players.get(0).getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));

            // Get needed tools to test currentThrow
            Field location = FigureLocationAdaptor.class.getDeclaredField("figureLocation");
            location.setAccessible(true);

            Field currentThrow = ResourceSource.class.getDeclaredField("currentThrow");
            currentThrow.setAccessible(true);

            Field dicesResults = CurrentThrow.class.getDeclaredField("throwResult");
            dicesResults.setAccessible(true);

            ResourceSource rs = (ResourceSource) location.get(river);
            CurrentThrow round = (CurrentThrow) currentThrow.get(rs);
            int results = (int) dicesResults.get(round);
            int countOfGold = results / Effect.GOLD.points();
            // Testing if player gets gold points
            assertEquals(ActionResult.ACTION_DONE, river.makeAction(players.get(0).getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));
            assertEquals(HasAction.NO_ACTION_POSSIBLE, river.tryToMakeAction(players.get(0).getPlayerOrder()));
            for (int i = 0; i < countOfGold; i++) {
                assertTrue(players.get(0).getPlayerBoard().takeResources(List.of(Effect.GOLD)));
            }
            assertFalse(players.get(0).getPlayerBoard().takeResources(List.of(Effect.GOLD)));

            players.get(1).getPlayerBoard().giveEffect(List.of(Effect.TOOL, Effect.TOOL));
            assertTrue(river.placeFigures(players.get(1).getPlayerOrder(), 5));
            assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, river.tryToMakeAction(players.get(1).getPlayerOrder()));
            assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, river.makeAction(players.get(1).getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));

            rs = (ResourceSource) location.get(river);
            round = (CurrentThrow) currentThrow.get(rs);
            results = (int) dicesResults.get(round);
            countOfGold = (results + 4) / Effect.GOLD.points();
            // Then check if player gets specified count of gold points
            assertEquals(ActionResult.ACTION_DONE, river.makeAction(players.get(1).getPlayerOrder(), List.of(Effect.TOOL, Effect.TOOL), List.of(new Effect[0])));
            assertEquals(HasAction.NO_ACTION_POSSIBLE, river.tryToMakeAction(players.get(1).getPlayerOrder()));
            for (int i = 0; i < countOfGold; i++) {
                assertTrue(players.get(1).getPlayerBoard().takeResources(List.of(Effect.GOLD)));
            }
            assertFalse(players.get(1).getPlayerBoard().takeResources(List.of(Effect.GOLD)));

            assertFalse(river.newTurn());
        }
    @Test
    public void testInteractionWithHuntingGrounds() throws NoSuchFieldException, IllegalAccessException {
            InterfaceFigureLocation huntingGrounds = gameBoard.getAllLocations().get(Location.HUNTING_GROUNDS);

            // Testing adding figures to hunting grounds
            assertTrue(huntingGrounds.placeFigures(players.get(0).getPlayerOrder(), 5));
            // Adding figures ti hunting grounds when there are already 5 but there is no upper limit
            assertEquals(HasAction.AUTOMATIC_ACTION_DONE, huntingGrounds.tryToPlaceFigures(players.get(1).getPlayerOrder(), 3));

            // Try to make action without tools
            assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, huntingGrounds.tryToMakeAction(players.get(0).getPlayerOrder()));
            assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, huntingGrounds.makeAction(players.get(0).getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));

            // Get needed tools to test currentThrow
            Field location = FigureLocationAdaptor.class.getDeclaredField("figureLocation");
            location.setAccessible(true);

            Field currentThrow = ResourceSource.class.getDeclaredField("currentThrow");
            currentThrow.setAccessible(true);

            Field dicesResults = CurrentThrow.class.getDeclaredField("throwResult");
            dicesResults.setAccessible(true);

            ResourceSource rs = (ResourceSource) location.get(huntingGrounds);
            CurrentThrow round = (CurrentThrow) currentThrow.get(rs);
            int results = (int) dicesResults.get(round);
            int countOfFood = results / Effect.FOOD.points();
            // Testing if player gets food points
            //Start food
            for(int i = 0; i < 12; i++){
                assertTrue(players.get(0).getPlayerBoard().takeResources(List.of(Effect.FOOD)));
            }
            assertFalse(players.get(0).getPlayerBoard().takeResources(List.of(Effect.FOOD)));

            assertEquals(ActionResult.ACTION_DONE, huntingGrounds.makeAction(players.get(0).getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));
            assertEquals(HasAction.NO_ACTION_POSSIBLE, huntingGrounds.tryToMakeAction(players.get(0).getPlayerOrder()));
            for (int i = 0; i < countOfFood; i++) {
                assertTrue(players.get(0).getPlayerBoard().takeResources(List.of(Effect.FOOD)));
            }
            assertFalse(players.get(0).getPlayerBoard().takeResources(List.of(Effect.FOOD)));

            players.get(1).getPlayerBoard().giveEffect(List.of(Effect.TOOL, Effect.TOOL));
            //Trying to place player 1 figures, but there are already from row 320 (testing placing figures when there are already 5)
            assertFalse(huntingGrounds.placeFigures(players.get(1).getPlayerOrder(), 5));
            assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, huntingGrounds.tryToMakeAction(players.get(1).getPlayerOrder()));
            assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, huntingGrounds.makeAction(players.get(1).getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));

            rs = (ResourceSource) location.get(huntingGrounds);
            round = (CurrentThrow) currentThrow.get(rs);
            results = (int) dicesResults.get(round);
            countOfFood = (results + 2) / Effect.FOOD.points();
            // Then check if player gets specified count of food points

            //Start food
            for(int i = 0; i < 12; i++){
                assertTrue(players.get(1).getPlayerBoard().takeResources(List.of(Effect.FOOD)));
            }
            assertFalse(players.get(1).getPlayerBoard().takeResources(List.of(Effect.FOOD)));

            assertEquals(ActionResult.ACTION_DONE, huntingGrounds.makeAction(players.get(1).getPlayerOrder(), List.of(Effect.TOOL, Effect.TOOL), List.of(new Effect[0])));
            assertEquals(HasAction.NO_ACTION_POSSIBLE, huntingGrounds.tryToMakeAction(players.get(1).getPlayerOrder()));
            for (int i = 0; i < countOfFood; i++) {
                assertTrue(players.get(1).getPlayerBoard().takeResources(List.of(Effect.FOOD)));
            }
            assertFalse(players.get(1).getPlayerBoard().takeResources(List.of(Effect.FOOD)));

            assertFalse(huntingGrounds.newTurn());
        }
    @Test
    public void testInteractionWithHut() {
        InterfaceFigureLocation hut = gameBoard.getAllLocations().get(Location.HUT);
        InterfaceFigureLocation toPlaceFigures = gameBoard.getAllLocations().get(Location.HUNTING_GROUNDS);

        //Trying to place more than one figure on hut
        assertEquals(HasAction.NO_ACTION_POSSIBLE, hut.tryToPlaceFigures(players.get(0).getPlayerOrder(), 6));
        //Player have 0 figures to place
        assertTrue(toPlaceFigures.placeFigures(players.get(1).getPlayerOrder(), 5));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, hut.tryToPlaceFigures(players.get(1).getPlayerOrder(), 2));
        //Hut already have figure
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, hut.tryToPlaceFigures(players.get(0).getPlayerOrder(), 2));
        assertFalse(hut.placeFigures(players.get(0).getPlayerOrder(), 2));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, hut.tryToPlaceFigures(players.get(2).getPlayerOrder(), 2));
        assertFalse(hut.placeFigures(players.get(2).getPlayerOrder(), 1));

        //Player receive one figure
        assertEquals(ActionResult.ACTION_DONE, hut.makeAction(players.get(0).getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));
        assertFalse(((PlayerBoardGameBoardFacade) players.get(0).getPlayerBoard()).newTurn());
        assertTrue(players.get(0).getPlayerBoard().hasFigures(6));
        assertTrue(players.get(0).getPlayerBoard().takeFigures(6));
        assertFalse(players.get(0).getPlayerBoard().hasFigures(1));
    }
    @Test
    public void testInteractionWithToolMaker(){
        InterfaceFigureLocation toolMaker = gameBoard.getAllLocations().get(Location.TOOL_MAKER);
        InterfaceFigureLocation toPlaceFigures = gameBoard.getAllLocations().get(Location.HUNTING_GROUNDS);

        //Trying to place more than one figure on toolMaker
        assertEquals(HasAction.NO_ACTION_POSSIBLE, toolMaker.tryToPlaceFigures(players.get(0).getPlayerOrder(), 2));
        //Player have 0 figures to place
        assertTrue(toPlaceFigures.placeFigures(players.get(1).getPlayerOrder(), 5));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, toolMaker.tryToPlaceFigures(players.get(1).getPlayerOrder(), 1));
        //Hut already have figure
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, toolMaker.tryToPlaceFigures(players.get(0).getPlayerOrder(), 1));
        assertFalse(toolMaker.placeFigures(players.get(0).getPlayerOrder(), 1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, toolMaker.tryToPlaceFigures(players.get(2).getPlayerOrder(), 1));
        assertFalse(toolMaker.placeFigures(players.get(2).getPlayerOrder(), 1));
        //Player receive one tool
        assertEquals(ActionResult.ACTION_DONE, toolMaker.makeAction(players.get(0).getPlayerOrder(), List.of(new Effect[0]), List.of(new Effect[0])));
        assertTrue(players.get(0).getPlayerBoard().hasSufficientTools(1));
        assertEquals(Optional.of(1), players.get(0).getPlayerBoard().useTool(0));
        assertFalse(players.get(0).getPlayerBoard().hasSufficientTools(1));
    }
    @Test
    public void testPlaceFiguresOnBuildingTiles() {
        //Testing adding figures to different building tiles
        assertTrue(gameBoard.getAllLocations().get(Location.BUILDING_TILE1).placeFigures(players.get(0).getPlayerOrder(), 1));
        assertFalse(gameBoard.getAllLocations().get(Location.BUILDING_TILE1).placeFigures(players.get(1).getPlayerOrder(), 3));
        assertTrue(gameBoard.getAllLocations().get(Location.BUILDING_TILE2).placeFigures(players.get(0).getPlayerOrder(), 1));
        assertFalse(gameBoard.getAllLocations().get(Location.BUILDING_TILE2).placeFigures(players.get(1).getPlayerOrder(), 3));
        assertTrue(gameBoard.getAllLocations().get(Location.BUILDING_TILE3).placeFigures(players.get(0).getPlayerOrder(), 1));
        assertFalse(gameBoard.getAllLocations().get(Location.BUILDING_TILE3).placeFigures(players.get(1).getPlayerOrder(), 3));
        assertTrue(gameBoard.getAllLocations().get(Location.BUILDING_TILE4).placeFigures(players.get(0).getPlayerOrder(), 1));
        assertFalse(gameBoard.getAllLocations().get(Location.BUILDING_TILE4).placeFigures(players.get(1).getPlayerOrder(), 3));
        //Testing adding figures from one player to one building tile
        assertFalse(gameBoard.getAllLocations().get(Location.BUILDING_TILE1).placeFigures(players.get(0).getPlayerOrder(), 1));
        assertFalse(gameBoard.getAllLocations().get(Location.BUILDING_TILE2).placeFigures(players.get(0).getPlayerOrder(), 1));
        assertFalse(gameBoard.getAllLocations().get(Location.BUILDING_TILE3).placeFigures(players.get(0).getPlayerOrder(), 1));
        assertFalse(gameBoard.getAllLocations().get(Location.BUILDING_TILE4).placeFigures(players.get(0).getPlayerOrder(), 1));
    }
    //Not sure how to test that without that crazy switch case, because the building at the building tile is random.
    @Test
    public void testInteractionWithBuildingTiles() {
        FigureLocationAdaptor building = (FigureLocationAdaptor) gameBoard.getAllLocations().get(Location.BUILDING_TILE1);

        assertTrue(building.placeFigures(players.get(0).getPlayerOrder(), 1));
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, building.tryToMakeAction(players.get(0).getPlayerOrder()));
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, building.tryToMakeAction(players.get(0).getPlayerOrder()));
        players.get(0).getPlayerBoard().giveEffect(List.of(Effect.WOOD, Effect.WOOD, Effect.WOOD, Effect.WOOD, Effect.WOOD, Effect.WOOD, Effect.WOOD));

        BuildingTile bt = (BuildingTile) building.getFigureLocation();
        String buildingType = getBuildingType(bt.getBuildingOnTop());

        switch (buildingType) {
            case "SimpleBuilding1" ->
                    assertEquals(ActionResult.ACTION_DONE, building.makeAction(players.get(0).getPlayerOrder(), List.of(Effect.WOOD, Effect.WOOD, Effect.CLAY), List.of(Effect.BUILDING)));
            case "SimpleBuilding2" ->
                    assertEquals(ActionResult.ACTION_DONE, building.makeAction(players.get(0).getPlayerOrder(), List.of(Effect.WOOD, Effect.STONE, Effect.GOLD), List.of(Effect.BUILDING)));
            case "SimpleBuilding3" ->
                    assertEquals(ActionResult.ACTION_DONE, building.makeAction(players.get(0).getPlayerOrder(), List.of(Effect.WOOD, Effect.STONE, Effect.STONE), List.of(Effect.BUILDING)));
            case "SimpleBuilding4" ->
                    assertEquals(ActionResult.ACTION_DONE, building.makeAction(players.get(0).getPlayerOrder(), List.of(Effect.STONE, Effect.CLAY, Effect.CLAY), List.of(Effect.BUILDING)));
            case "SimpleBuilding5" ->
                    assertEquals(ActionResult.ACTION_DONE, building.makeAction(players.get(0).getPlayerOrder(), List.of(Effect.STONE, Effect.STONE, Effect.WOOD), List.of(Effect.BUILDING)));
            case "SimpleBuilding6" ->
                    assertEquals(ActionResult.ACTION_DONE, building.makeAction(players.get(0).getPlayerOrder(), List.of(Effect.WOOD, Effect.CLAY, Effect.STONE), List.of(Effect.BUILDING)));
            case "SimpleBuilding7" ->
                    assertEquals(ActionResult.ACTION_DONE, building.makeAction(players.get(0).getPlayerOrder(), List.of(Effect.WOOD, Effect.WOOD, Effect.WOOD), List.of(Effect.BUILDING)));
            case "SimpleBuilding8" ->
                    assertEquals(ActionResult.ACTION_DONE, building.makeAction(players.get(0).getPlayerOrder(), List.of(Effect.STONE, Effect.STONE, Effect.STONE), List.of(Effect.BUILDING)));
            case "SimpleBuilding9" ->
                    assertEquals(ActionResult.ACTION_DONE, building.makeAction(players.get(0).getPlayerOrder(), List.of(Effect.WOOD, Effect.CLAY, Effect.GOLD), List.of(Effect.BUILDING)));
            case "SimpleBuilding10" ->
                    assertEquals(ActionResult.ACTION_DONE, building.makeAction(players.get(0).getPlayerOrder(), List.of(Effect.STONE, Effect.CLAY, Effect.GOLD), List.of(Effect.BUILDING)));
            case "VariableBuilding" ->
                    assertEquals(ActionResult.ACTION_DONE, building.makeAction(players.get(0).getPlayerOrder(), List.of(Effect.WOOD, Effect.STONE, Effect.WOOD, Effect.STONE), List.of(Effect.BUILDING)));
            case "ArbitraryBuilding1" ->
                    assertEquals(ActionResult.ACTION_DONE, building.makeAction(players.get(0).getPlayerOrder(), List.of(Effect.WOOD), List.of(Effect.BUILDING)));
            case "ArbitraryBuilding2" ->
                    assertEquals(ActionResult.ACTION_DONE, building.makeAction(players.get(0).getPlayerOrder(), List.of(Effect.WOOD, Effect.WOOD), List.of(Effect.BUILDING)));
            case "ArbitraryBuilding3" ->
                    assertEquals(ActionResult.ACTION_DONE, building.makeAction(players.get(0).getPlayerOrder(), List.of(Effect.WOOD, Effect.WOOD, Effect.WOOD), List.of(Effect.BUILDING)));
            case "ArbitraryBuilding4" ->
                    assertEquals(ActionResult.ACTION_DONE, building.makeAction(players.get(0).getPlayerOrder(), List.of(Effect.WOOD, Effect.WOOD, Effect.WOOD, Effect.WOOD), List.of(Effect.BUILDING)));
            case "ArbitraryBuilding5" ->
                    assertEquals(ActionResult.ACTION_DONE, building.makeAction(players.get(0).getPlayerOrder(), List.of(Effect.WOOD, Effect.WOOD, Effect.WOOD, Effect.WOOD, Effect.WOOD), List.of(Effect.BUILDING)));
            case "ArbitraryBuilding6" ->
                    assertEquals(ActionResult.ACTION_DONE, building.makeAction(players.get(0).getPlayerOrder(), List.of(Effect.WOOD, Effect.WOOD, Effect.WOOD, Effect.WOOD, Effect.WOOD, Effect.WOOD), List.of(Effect.BUILDING)));
            case "ArbitraryBuilding7" ->
                    assertEquals(ActionResult.ACTION_DONE, building.makeAction(players.get(0).getPlayerOrder(), List.of(Effect.WOOD, Effect.WOOD, Effect.WOOD, Effect.WOOD, Effect.WOOD, Effect.WOOD, Effect.WOOD), List.of(Effect.BUILDING)));
            default -> fail("Unknown building type: " + buildingType);
        }
    }
    @Test
    public void testPlaceFiguresOnCivilisationCards() {
        assertTrue(gameBoard.getAllLocations().get(Location.CIVILISATION_CARD1).placeFigures(players.get(0).getPlayerOrder(), 1));
        assertFalse(gameBoard.getAllLocations().get(Location.CIVILISATION_CARD1).placeFigures(players.get(1).getPlayerOrder(), 3));
        assertTrue(gameBoard.getAllLocations().get(Location.CIVILISATION_CARD2).placeFigures(players.get(0).getPlayerOrder(), 1));
        assertFalse(gameBoard.getAllLocations().get(Location.CIVILISATION_CARD2).placeFigures(players.get(1).getPlayerOrder(), 3));
        assertTrue(gameBoard.getAllLocations().get(Location.CIVILISATION_CARD3).placeFigures(players.get(0).getPlayerOrder(), 1));
        assertFalse(gameBoard.getAllLocations().get(Location.CIVILISATION_CARD3).placeFigures(players.get(1).getPlayerOrder(), 3));
        assertTrue(gameBoard.getAllLocations().get(Location.CIVILISATION_CARD4).placeFigures(players.get(0).getPlayerOrder(), 1));
        assertFalse(gameBoard.getAllLocations().get(Location.CIVILISATION_CARD4).placeFigures(players.get(1).getPlayerOrder(), 3));
        //Testing adding figures from one player to one civilisation card
        assertFalse(gameBoard.getAllLocations().get(Location.CIVILISATION_CARD1).placeFigures(players.get(0).getPlayerOrder(), 1));
        assertFalse(gameBoard.getAllLocations().get(Location.CIVILISATION_CARD2).placeFigures(players.get(0).getPlayerOrder(), 1));
        assertFalse(gameBoard.getAllLocations().get(Location.CIVILISATION_CARD3).placeFigures(players.get(0).getPlayerOrder(), 1));
        assertFalse(gameBoard.getAllLocations().get(Location.CIVILISATION_CARD4).placeFigures(players.get(0).getPlayerOrder(), 1));
    }
    @Test
    public void testInteractionWithCardPlace(){
        InterfaceFigureLocation cp1 = gameBoard.getAllLocations().get(Location.CIVILISATION_CARD1);
        InterfaceFigureLocation cp2 = gameBoard.getAllLocations().get(Location.CIVILISATION_CARD2);
        InterfaceFigureLocation cp3 = gameBoard.getAllLocations().get(Location.CIVILISATION_CARD3);
        InterfaceFigureLocation cp4 = gameBoard.getAllLocations().get(Location.CIVILISATION_CARD4);


        players.get(0).getPlayerBoard().giveEffect(Arrays.asList(Effect.STONE, Effect.GOLD));
        assertTrue(cp1.placeFigures(players.get(0).getPlayerOrder(), 1));

        // player2 tries to make an action without any figure placed
        assertEquals(ActionResult.FAILURE, cp1.makeAction(players.get(1).getPlayerOrder(), Collections.emptyList(), Arrays.asList(Effect.STONE, Effect.WOOD)));

        // player1 tries to make an action with empty and incorrect input resources
        assertEquals(ActionResult.FAILURE, cp1.makeAction(players.get(0).getPlayerOrder(), Collections.emptyList(), Arrays.asList(Effect.STONE, Effect.WOOD)));
        assertEquals(ActionResult.FAILURE, cp1.makeAction(players.get(0).getPlayerOrder(), List.of(Effect.FOOD), Arrays.asList(Effect.STONE, Effect.WOOD)));

        // player1 has correct input but lacks the resources in PlayerBoard
        assertEquals(ActionResult.FAILURE, cp1.makeAction(players.get(0).getPlayerOrder(), List.of(Effect.WOOD), Arrays.asList(Effect.STONE, Effect.WOOD)));

        // player1 makes an action with correct input and sufficient resources
        assertEquals(ActionResult.ACTION_DONE, cp1.makeAction(players.get(0).getPlayerOrder(), List.of(Effect.STONE), Arrays.asList(Effect.STONE, Effect.WOOD)));

    }
}
