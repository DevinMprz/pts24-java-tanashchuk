package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.Player;

import java.util.ArrayList;
import java.util.List;

public class GetSomethingFixed implements EvaluateCivilizationCardImmediateEffect {
    public GetSomethingFixed(){}

    @Override
    public boolean performEffect(final Player player, final Effect choice) {
        player.getPlayerBoard().giveEffect(List.of(choice));
        return true;
    }
}