package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.PlayerOrder;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.Player;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.ActionResult;

import java.util.*;

import org.json.JSONObject;

public class BuildingTile implements InterfaceFigureLocationInternal {
    private final Stack<Building> building;
    private final ArrayList<PlayerOrder> figures;
    private static final int MAX_FIGURES = 1;
    
    public BuildingTile(Collection<Building> building) {
        this.building = new Stack<>();
        this.building.addAll(building);
        this.figures = new ArrayList<>();
    }

    @Override
    public boolean placeFigures(Player player, int figureCount) {
        if (figureCount != MAX_FIGURES || !figures.isEmpty()) {
            return false;
        }
        figures.add(player.getPlayerOrder());
        return true;
    }

    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if (count != MAX_FIGURES || !figures.isEmpty()) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        if (!player.getPlayerBoard().hasFigures(count)) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }
    public Building getBuildingOnTop(){
        return building.peek();
    }
    @Override
    public ActionResult makeAction(Player player, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        if (figures.isEmpty() || !figures.get(0).equals(player.getPlayerOrder())) {
            return ActionResult.FAILURE;
        }
        
        Collection<Effect> resources = inputResources;
        
        OptionalInt points = building.pop().build(resources);
        if (points.isEmpty()) {
            return ActionResult.FAILURE;
        }

        //Remove resources from player
        if (!player.getPlayerBoard().takeResources(resources)) {
            return ActionResult.FAILURE;
        }
        player.getPlayerBoard().takeResources(resources);

        
        // Give points to player
        player.getPlayerBoard().giveEffect(List.of(new Effect[]{Effect.BUILDING}));
        return ActionResult.ACTION_DONE;
    }

    @Override
    public boolean skipAction(Player player) {
        if (figures.isEmpty() || !figures.get(0).equals(player.getPlayerOrder())) {
            return false;
        }
        figures.clear();
        return true;
    }

    @Override
    public HasAction tryToMakeAction(Player player) {
        if (figures.isEmpty() || !figures.get(0).equals(player.getPlayerOrder())) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    @Override
    public boolean newTurn() {
        figures.clear();
        return false;
    }

    public String state() {
        Map<String, Object> state = Map.of(
                "buildings", building.stream().map(Building::state).toList(),
            "figures", figures.stream().map(PlayerOrder::getOrder).toList()
        );
        return new JSONObject(state).toString();
    }
}
