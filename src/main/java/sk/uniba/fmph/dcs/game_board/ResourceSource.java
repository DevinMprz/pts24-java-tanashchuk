package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;
import sk.uniba.fmph.dcs.game_board.InterfaceFigureLocationInternal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public  class ResourceSource implements InterfaceFigureLocationInternal {

    private String name;
    private final Effect resource;
    private int maxFigures;
    private int maxFigureColors;
    private ArrayList<PlayerOrder> figures;
    private CurrentThrow currentThrow;
    private Player currentPlayer;

    public ResourceSource(String name, Effect resource, int maxFigures, int maxFigureColors) {
        this.name = name;
        this.resource = resource;
        this.maxFigures = maxFigures;
        this.maxFigureColors = maxFigureColors;
        this.figures = new ArrayList<>();
        this.currentThrow = null;
        this.currentPlayer = null;
    }

    @Override
    public boolean placeFigures(Player player, int figureCount) {
        if (this.figures.contains(player.playerOrder())) {
            return false;
        }

        if (this.figures.size() + figureCount > this.maxFigures) {
            return false;
        }

        if (!player.playerBoard().hasFigures(figureCount)) {
            return false;
        }

        ArrayList<PlayerOrder> figureColors = new ArrayList<>();
        for (PlayerOrder figure : this.figures) {
            if (!figureColors.contains(figure)) {
                figureColors.add(figure);
            }
        }

        if (figureColors.size() >= maxFigureColors){
            return false;
        }

        for (int i = 0; i < figureCount; i++) {
            this.figures.add(player.playerOrder());
        }
        return true;
    }

    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if (this.placeFigures(player, count)) {
            return HasAction.AUTOMATIC_ACTION_DONE;
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    @Override
    public ActionResult makeAction(Player player, Effect[] inputResources, Effect[] outputResources) {
        if(currentPlayer == null && currentThrow == null){
            int countPlayerFigures = 0;

            for(PlayerOrder playerOrder: figures){
                if(playerOrder == player.playerOrder()){
                    countPlayerFigures++;
                }
            }

            currentPlayer = player;
            currentThrow = new CurrentThrow();
            currentThrow.initiate(player,resource, countPlayerFigures);
            return ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE;
        }

        if(player != currentPlayer){
            return ActionResult.FAILURE;
        }

        if(inputResources.length == 0){
            currentThrow.finishUsingTools();
            currentThrow = null;
            currentPlayer = null;
            return ActionResult.ACTION_DONE;
        }

        for (Effect inputResource : inputResources) {
            if (inputResource != Effect.TOOL) {
                return ActionResult.FAILURE;
            }
        }

        int usedToolCount = 0;
        for(Effect effect : inputResources){
            for(int i = 0; i < 6; i++){
                if(currentThrow.useTool(i)){
                    usedToolCount++;
                    break;
                }
            }
        }
        if(usedToolCount != inputResources.length){
            return ActionResult.FAILURE;
        }
        return ActionResult.ACTION_DONE;

    }

    @Override
    public boolean skipAction(Player player) {
        return false;
    }

    @Override
    public HasAction tryToMakeAction(Player player) {
        if(currentThrow.canUseTools() && this.currentPlayer == player){
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }

        if(currentPlayer == null && figures.contains(player.playerOrder())){
            makeAction(player, new Effect[0], new Effect[0]);
            return HasAction.AUTOMATIC_ACTION_DONE;
        }

        return HasAction.NO_ACTION_POSSIBLE;
    }

    @Override
    public boolean newTurn() {
        if(figures.isEmpty()) {
            this.currentThrow = null;
            this.currentPlayer = null;
            return true;
        }
        return false;
    }

    @Override
    public String state() {
        Map<String, Object> state = Map.of(
                "name", name,
                "resource", resource,
                "maxFigures", maxFigures,
                "maxFigureColors", maxFigureColors,
                "figures", figures.stream().map(PlayerOrder::getOrder).toList(),
                "currentThrow", currentThrow,
                "currentPlayer", currentPlayer
        );
        return new JSONObject(state).toString();
    }
}