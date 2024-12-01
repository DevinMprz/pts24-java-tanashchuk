package sk.uniba.fmph.dcs.game_board;
import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;
import sk.uniba.fmph.dcs.stone_age.Location;
import sk.uniba.fmph.dcs.stone_age.Player;

import java.util.*;

/**
 * The {@code GameBoard} class represents the central game board in the Stone Age game.
 * It manages all locations on the board, including resource sources, tool-making huts,
 * fields, buildings, and civilization card slots.
 *
 * <p>This class implements the {@link InterfaceGetState} interface to provide a JSON-based
 * representation of the game board's state for external use.
 */
public class GameBoard implements InterfaceGetState {

    private final Map<Location, InterfaceFigureLocationInternal> locations = new HashMap<>();

    /**
     * Constructs a new {@code GameBoard} instance with the given players, buildings, and civilization cards.
     * Initializes all locations on the board, including tool-making, resource sources, building tiles,
     * and civilization card slots.
     *
     * @param players a collection of players participating in the game
     * @param buildings an array of {@link Building} objects to be placed on the building tiles
     * @param civilizationCards an array of {@link CivilizationCard} objects for the card deck
     */
    public GameBoard(Collection<Player> players, Building[] buildings, CivilizationCard[] civilizationCards) {
        ToolMakerHutFields fields = new ToolMakerHutFields(players.size());

        // Tool Maker, Hut, and Field locations
        locations.put(Location.TOOL_MAKER, new PlaceOnToolMakerAdaptor(fields));
        locations.put(Location.HUT, new PlaceOnHutAdaptor());
        locations.put(Location.FIELD, new PlaceOnFieldsAdaptor(fields));

        // Resource sources
        locations.put(Location.FOREST, new ResourceSource("Wood forest", Effect.WOOD, Integer.MAX_VALUE, 4));
        locations.put(Location.CLAY_MOUND, new ResourceSource("Clay mound", Effect.CLAY, Integer.MAX_VALUE, 4));
        locations.put(Location.QUARRY, new ResourceSource("Stone quarry", Effect.STONE, Integer.MAX_VALUE, 4));
        locations.put(Location.RIVER, new ResourceSource("Gold river", Effect.GOLD, Integer.MAX_VALUE, 4));

        // Building tiles
        locations.put(Location.BUILDING_TILE1, new BuildingTile(buildings[0]));
        locations.put(Location.BUILDING_TILE2, new BuildingTile(buildings[1]));
        locations.put(Location.BUILDING_TILE3, new BuildingTile(buildings[2]));
        locations.put(Location.BUILDING_TILE4, new BuildingTile(buildings[3]));

        // Civilization card slots
        CivilizationCardDeck deck = new CivilizationCardDeck(civilizationCards);
        var ccp1 = new CivilizationCardPlace(null, deck, 1);
        var ccp2 = new CivilizationCardPlace(ccp1, deck, 2);
        var ccp3 = new CivilizationCardPlace(ccp2, deck, 3);
        var ccp4 = new CivilizationCardPlace(ccp3, deck, 4);

        locations.put(Location.CIVILISATION_CARD1, ccp1);
        locations.put(Location.CIVILISATION_CARD2, ccp2);
        locations.put(Location.CIVILISATION_CARD3, ccp3);
        locations.put(Location.CIVILISATION_CARD4, ccp4);
    }

    /**
     * Returns a JSON string representing the current state of the game board.
     *
     * <p>The JSON object contains key-value pairs where:
     * <ul>
     *   <li>The key is a {@link Location} representing the location type.</li>
     *   <li>The value is a string representation of the location's state.</li>
     * </ul>
     *
     * @return a JSON string representation of the game board's state
     */
    @Override
    public String state() {
        Map<Location, String> state = new HashMap<>();
        locations.forEach((key, value) -> state.put(key, value.state()));
        return new JSONObject(state).toString();
    }
}
