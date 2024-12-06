package sk.uniba.fmph.dcs.stone_age;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.game_board.*;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.*;

public class StoneAgeGameTest {
    private StoneAgeGame game;
    private GameBoard testBoard;
    private Map<Integer, Player> playersMap;
    private StoneAgeObservable observable;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        observable = new StoneAgeObservable();
        game = new StoneAgeGame(4, observable);

        testBoard = getPrivateField(game, "gameBoard");
        playersMap = getPrivateField(game, "players");
    }

    @Test
    public void testInitializationOfStoneAge() throws NoSuchFieldException, IllegalAccessException {
        List<Player> toTestPlayers = getPrivateField(game, "forCreatingGamePhase");
        InterfaceGamePhaseController testGameController = getPrivateField(game, "gamePhaseController");
        StoneAgeObservable testObserver = getPrivateField(game, "observable");

        assertFalse(playersMap.isEmpty());
        assertFalse(toTestPlayers.isEmpty());
        assertNotNull(testGameController);
        assertNotNull(testBoard);
        assertNotNull(testObserver);

        List<Player> playersFromMap = new ArrayList<>(playersMap.values());
        assertEquals(toTestPlayers, playersFromMap);
    }

    @Test
    public void testPlaceFigures() throws NoSuchFieldException, IllegalAccessException {
        Map<Location, InterfaceFigureLocation> testLocations = getPrivateField(testBoard, "locations");

        assertTrue(game.placeFigures(0, Location.HUNTING_GROUNDS, 3));
        assertFalse(game.placeFigures(0, Location.HUNTING_GROUNDS, 3));
        assertFalse(game.placeFigures(0, Location.HUNTING_GROUNDS, 2));
        assertFalse(game.placeFigures(0, Location.BUILDING_TILE1, 1));
        assertFalse(game.placeFigures(2, Location.HUNTING_GROUNDS, 3));
        assertFalse(game.placeFigures(2, Location.BUILDING_TILE1, 1));
        assertTrue(game.placeFigures(1, Location.HUNTING_GROUNDS, 3));
        assertFalse(game.placeFigures(3, Location.HUNTING_GROUNDS, 2));
        assertTrue(game.placeFigures(2, Location.HUNTING_GROUNDS, 2));
        assertTrue(game.placeFigures(3, Location.HUNTING_GROUNDS, 3));
    }

    @Test
    public void testIntegrationTest() throws NoSuchFieldException, IllegalAccessException {
        Map<Location, InterfaceFigureLocation> testLocations = getPrivateField(testBoard, "locations");

        performInitialPlacements();
        performInitialActions();

        playersMap.get(1).getPlayerBoard().takeResources(List.of(Effect.FOOD));
        playersMap.get(2).getPlayerBoard().takeResources(List.of(Effect.FOOD));

        performBuildingAction(testLocations, Location.BUILDING_TILE3);
        assertTrue(game.doNotFeedThisTurn(1));

        makeBuildingTileEmpty(testLocations, Location.BUILDING_TILE3);
        assertTrue(game.feedTribe(2, List.of(Effect.STONE, Effect.STONE, Effect.STONE, Effect.STONE, Effect.STONE)));
    }

    private void performInitialPlacements() {
        assertTrue(game.placeFigures(0, Location.HUNTING_GROUNDS, 3));
        assertTrue(game.placeFigures(1, Location.QUARRY, 3));
        assertTrue(game.placeFigures(2, Location.TOOL_MAKER, 1));
        assertTrue(game.placeFigures(3, Location.CIVILISATION_CARD1, 1));

        assertTrue(game.placeFigures(0, Location.CLAY_MOUND, 2));
        assertTrue(game.placeFigures(1, Location.HUNTING_GROUNDS, 2));
        assertTrue(game.placeFigures(2, Location.QUARRY, 4));
        assertTrue(game.placeFigures(3, Location.FOREST, 4));
    }

    private void performInitialActions() {
        assertTrue(game.makeAction(0, Location.HUNTING_GROUNDS, List.of(), List.of()));
        assertTrue(game.makeAction(1, Location.QUARRY, List.of(), List.of()));
        assertTrue(game.makeAction(3, Location.CLAY_MOUND, List.of(), List.of()));
        assertTrue(game.makeAction(0, Location.CLAY_MOUND, List.of(), List.of()));
        assertFalse(game.makeAction(1, Location.HUNTING_GROUNDS, List.of(), List.of()));
        assertTrue(game.makeAction(2, Location.QUARRY, List.of(Effect.TOOL), List.of()));
        assertTrue(game.makeAction(3, Location.CIVILISATION_CARD1, List.of(Effect.CLAY), List.of()));
    }

    private void performBuildingAction(Map<Location, InterfaceFigureLocation> testLocations, Location location) throws NoSuchFieldException, IllegalAccessException {
        FigureLocationAdaptor locationAdaptor = (FigureLocationAdaptor) testLocations.get(location);
        BuildingTile buildingTile = getPrivateField(locationAdaptor, "figureLocation");
        Building building = buildingTile.getBuildingOnTop();

        if (building instanceof SimpleBuilding) {
            List<Effect> resources = getPrivateField(building, "requiredResources");
            playersMap.get(0).getPlayerBoard().giveEffect(resources);
            assertTrue(game.makeAction(0, location, resources, List.of()));
        } else if (building instanceof VariableBuilding) {
            int resourceCount = getPrivateField(building, "numberOfResources");
            List<Effect> resources = createResourceList(resourceCount, Effect.WOOD, Effect.STONE);
            playersMap.get(0).getPlayerBoard().giveEffect(resources);
            assertTrue(game.makeAction(0, location, resources, List.of()));
        } else if (building instanceof ArbitraryBuilding) {
            int maxResources = getPrivateField(building, "maxNumberOfResources");
            List<Effect> resources = createResourceList(maxResources, Effect.WOOD);
            playersMap.get(0).getPlayerBoard().giveEffect(resources);
            assertTrue(game.makeAction(0, location, resources, List.of()));
        }
    }

    private void makeBuildingTileEmpty(Map<Location, InterfaceFigureLocation> testLocations, Location location) throws NoSuchFieldException, IllegalAccessException {
        FigureLocationAdaptor locationAdaptor = (FigureLocationAdaptor) testLocations.get(location);
        BuildingTile buildingTile = getPrivateField(locationAdaptor, "figureLocation");
        Stack<Building> buildings = getPrivateField(buildingTile, "buildings");
        buildings.clear();
    }

    private List<Effect> createResourceList(int count, Effect... effects) {
        List<Effect> resourceList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            resourceList.add(effects[i % effects.length]);
        }
        return resourceList;
    }

    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(Object instance, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(instance);
    }
}
