package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

public class CivilizationCardDeck {
    private Stack<CivilizationCard> cardsDeck;

    public CivilizationCardDeck(Collection<CivilizationCard> cardDeck){
        this.cardsDeck = new Stack<>();
        for (CivilizationCard card : cardDeck) {
            this.cardsDeck.push(card);
        }
    }

    public Optional<CivilizationCard> getTop(){
        if (this.cardsDeck.empty()) {
            return Optional.empty();
        }

        return Optional.of(this.cardsDeck.pop());
    }
    public String state() {
        Map<String, Integer> cardCount = Map.of("cardsLeft", this.cardsDeck.size());

        return new JSONObject(cardCount).toString();
    }
}
