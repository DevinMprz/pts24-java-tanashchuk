package sk.uniba.fmph.dcs.game_board;
import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;
import sk.uniba.fmph.dcs.stone_age.Location;
import sk.uniba.fmph.dcs.stone_age.Player;

import java.util.*;

public class GameBoard implements InterfaceGetState {
    private final Map<Location, InterfaceFigureLocationInternal> locations = new HashMap<>();
    public GameBoard(Collection<Player> players, Building[] buildings, CivilizationCard[] civilizationCards) {
        ToolMakerHutFields fields = new ToolMakerHutFields(players.size());

        locations.put(Location.TOOL_MAKER, new PlaceOnToolMakerAdaptor(fields));
        locations.put(Location.HUT, new PlaceOnHutAdaptor());
        locations.put(Location.FIELD, new PlaceOnFieldsAdaptor(fields));

        locations.put(Location.FOREST, new ResourceSource("Wood forest", Effect.WOOD, Integer.MAX_VALUE, 4));
        locations.put(Location.CLAY_MOUND, new ResourceSource("Clay mound", Effect.CLAY, Integer.MAX_VALUE, 4));
        locations.put(Location.QUARRY, new ResourceSource("Stone quarry", Effect.STONE, Integer.MAX_VALUE, 4));
        locations.put(Location.RIVER, new ResourceSource("Gold river", Effect.GOLD, Integer.MAX_VALUE, 4));

        locations.put(Location.BUILDING_TILE1, new BuildingTile(buildings[0]));
        locations.put(Location.BUILDING_TILE2, new BuildingTile(buildings[1]));
        locations.put(Location.BUILDING_TILE3, new BuildingTile(buildings[2]));
        locations.put(Location.BUILDING_TILE4, new BuildingTile(buildings[3]));

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

    @Override
    public String state() {
        Map<Location, String> state = new HashMap<>();
        locations.forEach((key, value) -> state.put(key, value.state()));
        return new JSONObject(state).toString();
    }
}