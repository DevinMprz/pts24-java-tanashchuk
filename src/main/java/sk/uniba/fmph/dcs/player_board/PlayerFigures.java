package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;

import java.util.Map;

public class PlayerFigures implements InterfaceGetState {
    private int totalFigures;
    private int figures;
    private final int MAX_FIGURES = 10;

    /**
     * Constructs a new {@code PlayerFigures} instance with the initial number of figures set to 5.
     */
    public PlayerFigures(){
        this.totalFigures = 5;
        this.figures = 5;
    }

    /**
     * Adds a new figure to the player's total figures, provided the maximum limit has not been reached.
     * The maximum number of figures is defined by {@code MAX_FIGURES}.
     */
    public void addNewFigure(){
        if(totalFigures < MAX_FIGURES){
            totalFigures++;
        }
    }

    /**
     * Checks if the player has the specified number of available figures.
     *
     * @param count the number of figures to check
     * @return {@code true} if the player has at least {@code count} figures available and {@code count} is non-negative;
     *         {@code false} otherwise
     */
    public boolean hasFigures(int count){
        return figures >= count && count >= 0;
    }

    /**
     * Returns the total number of figures the player has.
     *
     * @return the total number of figures
     */
    public int getTotalFigures() {
        return totalFigures;
    }

    /**
     * Deducts the specified number of figures from the player's available figures if they have enough.
     *
     * @param count the number of figures to take
     * @return {@code true} if the figures were successfully deducted; {@code false} otherwise
     */
    public boolean takeFigures(int count){
        if(hasFigures(count)){
            figures = figures - count;
            return true;
        }else{
            return  false;
        }
    }

    /**
     * Resets the player's available figures to their total figures at the start of a new turn.
     */
    public void newTurn (){
        figures = totalFigures;
    }


    /**
     * Returns a JSON string representing the current state of the player's figures.
     *
     * <p>The JSON object contains the following fields:
     * <ul>
     *   <li>{@code "figures"}: the number of available figures</li>
     *   <li>{@code "totalFigures"}: the total number of figures</li>
     * </ul>
     *
     * @return a JSON string representation of the player's figures state
     */
    @Override
    public String state(){
        Map<String, Object> state = Map.of(
                "figures", figures,
                "totalFigures", totalFigures
        );

        return new JSONObject(state).toString();
    }

    public int getFigures() {
        return figures;
    }

}