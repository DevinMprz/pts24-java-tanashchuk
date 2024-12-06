package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.ArrayList;
import java.util.Map;

public class ToolMakerHutFields{
    private PlayerOrder toolMakerFigures;
    private PlayerOrder hutFigures;
    private PlayerOrder fieldsFigures;
    private boolean canPlaceOnHut;
    private boolean canPlaceOnField;
    private boolean canPlaceOnToolMaker;
    private final int restriction;


    public ToolMakerHutFields(final int numberOfPlayers){
        if(numberOfPlayers<4){
            this.restriction = 2;
        }else{
            this.restriction = 3;
        }
        canPlaceOnHut = true;
        canPlaceOnField = true;
        canPlaceOnToolMaker = true;
    }

    private boolean checkRestriction(){
        int result = 0;
        if(toolMakerFigures != null) {
            result++;
        }
        if(fieldsFigures != null){
            result++;
        }
        if(hutFigures != null){
            result++;
        }

        return result < restriction;
    }



    public boolean placeOnToolMaker(final Player player){
        if(!canPlaceOnToolMaker(player)){
            return false;
        }

        toolMakerFigures = player.getPlayerOrder();
        return true;
    }

    public boolean actionToolMaker(final Player player){
        if(!player.getPlayerOrder().equals(toolMakerFigures)){
            return false;
        }
        ArrayList<Effect> list = new ArrayList<>();
        list.add(Effect.TOOL);
        player.getPlayerBoard().giveEffect(list);
        canPlaceOnToolMaker = false;
        toolMakerFigures = null;
        return true;
    }
    //
    public boolean canPlaceOnToolMaker(final Player player){
        return toolMakerFigures == null && canPlaceOnToolMaker && checkRestriction();
    }

    public boolean placeOnHut(final Player player){
        if(!canPlaceOnHut(player)){
            return false;
        }

        hutFigures = player.getPlayerOrder();
        return true;
    }

    public boolean canPlaceOnHut(final Player player){
        return hutFigures == null && canPlaceOnHut && checkRestriction();
    }

    public boolean actionHut(final Player player){
        if(!player.getPlayerOrder().equals(hutFigures)){
            return false;
        }

        player.getPlayerBoard().giveFigure();
        canPlaceOnHut = false;
        hutFigures = null;
        return true;
    }

    public boolean placeOnFields(final Player player){
        if(!canPlaceOnFields(player)){
            return false;
        }

        fieldsFigures = player.getPlayerOrder();
        return true;

    }

    public boolean actionFields(final Player player){
        if(!player.getPlayerOrder().equals(fieldsFigures)){
            return false;
        }
        ArrayList<Effect> list = new ArrayList<>();
        list.add(Effect.FIELD);
        player.getPlayerBoard().giveEffect(list);
        fieldsFigures = null;
        canPlaceOnField = false;
        return true;
    }

    public boolean canPlaceOnFields(final Player player){
        return fieldsFigures == null && canPlaceOnField && checkRestriction();
    }
    public boolean newTurn(){
        canPlaceOnHut = true;
        canPlaceOnField = true;
        canPlaceOnToolMaker = true;
        toolMakerFigures = null;
        hutFigures = null;
        fieldsFigures = null;
        return false;
    }

    public String state(){
        Map<String, String> state = Map.of(
                "toolMakerFigures", toolMakerFigures.toString(),
                "hutFigures", hutFigures.toString(),
                "fieldsFigures", fieldsFigures.toString());
        return new JSONObject(state).toString();
    }


}