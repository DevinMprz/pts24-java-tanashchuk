package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
public class PlayerCivilizationCards implements InterfaceGetState {
    private final Map<EndOfGameEffect, Integer> endOfGameEffectMap;

    public PlayerCivilizationCards(){
        endOfGameEffectMap = new HashMap<>();
    }

    public void addEndOfGameEffects(Collection<EndOfGameEffect> effects){
        for (EndOfGameEffect effect : effects) {
            if (endOfGameEffectMap.containsKey(effect)) {
                endOfGameEffectMap.put(effect, endOfGameEffectMap.get(effect) + 1);
            } else {
                endOfGameEffectMap.put(effect, 1);
            }
        }
    }

    public int calculateEndOfGameCivilizationCardPoints(int buildings, int tools, int fields, int figures){
        int sumOfEndOfGameEffects = 0;
        Map<EndOfGameEffect, Integer> greenCards = new HashMap<>();
        for(EndOfGameEffect effect: endOfGameEffectMap.keySet()){
            switch (effect){
                case FARMER -> sumOfEndOfGameEffects += fields * endOfGameEffectMap.get(effect);
                case TOOL_MAKER -> sumOfEndOfGameEffects += tools * endOfGameEffectMap.get(effect);
                case BUILDER -> sumOfEndOfGameEffects += buildings * endOfGameEffectMap.get(effect);
                case SHAMAN -> sumOfEndOfGameEffects += figures * endOfGameEffectMap.get(effect);
                default -> greenCards.put(effect, endOfGameEffectMap.get(effect));
            }
        }

        while (true){
            int countCurrentCards = 0;
            for(EndOfGameEffect effect: greenCards.keySet()){
                int numberOfCards = greenCards.get(effect);
                if(numberOfCards > 0){
                    countCurrentCards++;
                    greenCards.put(effect, numberOfCards - 1);
                }
            }
            if(countCurrentCards == 0){
                break;
            }
            int toCheck = (int) Math.pow(countCurrentCards, 2);
            sumOfEndOfGameEffects += toCheck;
        }

        return sumOfEndOfGameEffects;
    }


    @Override
    public String state() {
        Map<String, Object> state = Map.of(
                "endOfGameEffectMap", endOfGameEffectMap
        );

        return new JSONObject(state).toString();
    }

    public Map<EndOfGameEffect, Integer> getEndOfGameEffectMap() {
        return endOfGameEffectMap;
    }
}