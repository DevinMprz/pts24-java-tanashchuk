package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class GetSomethingThrow implements EvaluateCivilizationCardImmediateEffect {
    private final Effect resource;

    //This variable is for tests
    private int sum;
    public GetSomethingThrow(final Effect resource) {
        this.resource = resource;
    }

    @Override
    public boolean performEffect(final Player player, final Effect choice) {
        if (choice != this.resource) {
            return false;
        }
        int[] dicesResults = Throw.throw_(2);
        sum = Arrays.stream(dicesResults).reduce(0, Integer::sum);;
        List<Effect> toReturn = new ArrayList<>();
        for(int i = 0; i < sum/choice.points(); i++){
            toReturn.add(choice);
        }
        player.getPlayerBoard().giveEffect(toReturn);
        return true;
    }
}
