package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class PlayerTools implements InterfaceGetState {
    private final int[] tools;
    private final boolean[] usedTools;
    private final int maxMultiplyUseTools = 3;
    private final int maxToolsCount = 6;
    private int totalToolsCount;
    private int roundToolsCount;

    public PlayerTools(){
        this.tools  = new int[6];
        this.usedTools = new boolean[3];
        Arrays.fill(tools, -1);
        Arrays.fill(usedTools, false);
    }

    public void newTurn(){
        Arrays.fill(usedTools, false);
        roundToolsCount = totalToolsCount;
    }

    public void addTool(){
        if(totalToolsCount < 12) {
            int position = totalToolsCount % 3;
            int value = 1 + totalToolsCount / 3;
            tools[position] = value;
            totalToolsCount++;
            roundToolsCount++;
        }
    }

    public boolean addSingleUseTool(int strength) {
        for (int i = maxMultiplyUseTools; i < tools.length; i++) {
            if (tools[i] == -1) {
                tools[i] = strength;
                totalToolsCount += strength;
                roundToolsCount += strength;
                return true;
            }
        }
        return false;
    }



    public Optional<Integer> useTool(int index) {
        Optional<Integer> toReturn = Optional.empty();
        if(index >= maxToolsCount){
            return toReturn;
        }
        if (index > 2){
            if(tools[index] != -1){
                toReturn = Optional.of(tools[index]);
                totalToolsCount -= tools[index];
                roundToolsCount -= tools[index];
                tools[index] = -1;
            }
        } else {
            if(tools[index] != -1 && !usedTools[index]) {
                roundToolsCount = roundToolsCount - tools[index];
                usedTools[index] = true;
                toReturn = Optional.of(tools[index]);
            }

        }
        return toReturn;
    }

    public boolean hasSufficientTools(int goal){
        return goal <= roundToolsCount;
    }


    @Override
    public String state() {
        Map<String, Object> state = Map.of(
                "tools", tools,
                "usedTools", usedTools,
                "totalToolsCount", totalToolsCount,
                "roundToolsCount", roundToolsCount
        );
        return new JSONObject(state).toString();
    }

    public int getTotalTools() {
        return this.totalToolsCount;
    }

    public int getTotalToolsCount() {
        return totalToolsCount;
    }

    public boolean[] getUsedTools() {
        return usedTools;
    }

    public int[] getTools() {
        return tools;
    }

    public int getRoundToolsCount() {
        return roundToolsCount;
    }
}
