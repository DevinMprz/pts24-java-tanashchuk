package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;
import java.util.Optional;

import java.util.*;

public class CurrentThrow implements InterfaceToolUse {
    private Effect throwsFor;
    private int throwResult;
    private Throw throw_ = new Throw();
    private Player player;
    private int dices;
    private int[] dicesResults;
    private boolean toolsUsed = false;
    private boolean finished = false;


    public void initiate(Player player, Effect effect, int dices){
        this.throwsFor = effect;
        this.player = player;
        this.dices = dices;
        int[] dicesResults = throw_.throw_(dices);
        this.dicesResults = dicesResults;
        throwResult = Arrays.stream(dicesResults).reduce(0, Integer::sum);
    }

    @Override
    public boolean useTool(int idx) {
        if (this.finished) {
            return false;
        }

        Optional<Integer> a = player.playerBoard().useTool(idx);
        if(a.isPresent()){
            throwResult += a.get();
            toolsUsed = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean canUseTools() {
        return this.player.playerBoard().hasSufficientTools(1);
    }

    @Override
    public boolean finishUsingTools() {
        if (this.finished) {
            return false;
        }

        if (!throwsFor.isResourceOrFood()){
            return false;
        }
        List<Effect> effects = new ArrayList<>();

        switch(throwsFor){
            case WOOD:
                for (int i = 0; i < Math.floorDiv(throwResult, throwsFor.points()); i++) {
                    effects.add(Effect.WOOD);
                }
                break;
            case CLAY:
                for (int i = 0; i < Math.floorDiv(throwResult, throwsFor.points()); i++) {
                    effects.add(Effect.CLAY);
                }
                break;
            case STONE:
                for (int i = 0; i < Math.floorDiv(throwResult, throwsFor.points()); i++) {
                    effects.add(Effect.STONE);
                }
                break;
            case GOLD:
                for (int i = 0; i < Math.floorDiv(throwResult, throwsFor.points()); i++) {
                    effects.add(Effect.GOLD);
                }
                break;
            case FOOD:
                for (int i = 0; i < Math.floorDiv(throwResult, throwsFor.points()); i++) {
                    effects.add(Effect.FOOD);
                }
            default:
                break;
        }

        player.playerBoard().giveEffect(effects);
        return true;
    }

    public String state(){
        Map<String, Object> state = Map.of(
                "throwsFor", throwsFor,
                "throwResult", throwResult,
                "player", player,
                "dices", dices,
                "dicesResults", dicesResults,
                "toolsUsed", toolsUsed
        );
        return new JSONObject(state).toString();
    }
}

