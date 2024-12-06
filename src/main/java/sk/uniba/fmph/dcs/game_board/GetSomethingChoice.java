package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.Player;
import java.util.List;
import java.util.ArrayList;

public class GetSomethingChoice implements EvaluateCivilizationCardImmediateEffect{
    private int totalNumber;

    public GetSomethingChoice(final int totalNumber){
        this.totalNumber = totalNumber;
    }

    //Player gets a resource choice
    @Override
    public boolean performEffect(final Player player, final Effect choice) {
        if (!choice.isResource() || totalNumber == 0) {
            return false;
        }
        totalNumber--;

        List<Effect> effectToGive = new ArrayList<>();
        effectToGive.add(choice);

        player.getPlayerBoard().giveEffect(effectToGive);
        return true;
    }

}
