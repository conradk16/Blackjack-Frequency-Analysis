import java.util.List;
import java.util.ArrayList;

public class Hand {
    public List<Card> cards;
    private Card visibleCard;

    public Hand() {
        cards = new ArrayList<Card>();
        visibleCard = null;
    }

    public void add(Card c) {
        cards.add(c);
    }

    public void addVisible(Card c) {
        cards.add(c);
        visibleCard = c;
    }

    public Card getVisible() {
        return visibleCard;
    }

    public boolean hasAce() {
        for(Card card : cards) {
            if(card.getValue().equals("Ace")) {
                return true;
            }
        }
        return false;
    }
}
