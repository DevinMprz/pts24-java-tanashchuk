package sk.uniba.fmph.dcs.game_board;

import java.util.Random;

public class Throw {

    // I can't use the method name "throw" because it causes a syntax error.
    public static int[] throw_(int dices){
        int[] result = new int[dices];
        Random rand = new Random();
        for (int i = 0; i < dices; i++) {
            result[i] = rand.nextInt(6)+1;
        }
        return result;
    }
}
