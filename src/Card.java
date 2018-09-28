public class Card {
    private final String value;
    private final String suit;

    public Card(String value, String suit) {
        this.value = new String(value);
        this.suit = new String(suit);
    }

    public String getValue() {
        return value;
    }

    public String getSuit() {
        return suit;
    }

    public int getNumericValue() {
        switch(value) {
            case "Ace":
                return 1;
            case "Two":
                return 2;
            case "Three":
                return 3;
            case "Four":
                return 4;
            case "Five":
                return 5;
            case "Six":
                return 6;
            case "Seven":
                return 7;
            case "Eight":
                return 8;
            case "Nine":
                return 9;
            case "Ten":
                return 10;
            case "Jack":
                return 10;
            case "Queen":
                return 10;
            case "King":
                return 10;
            default:
                return 0;
        }
    }

}
