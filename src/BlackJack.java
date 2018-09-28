import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class BlackJack {

    private static int charlies = 0;
    private static int blackjacks = 0;
    private static int tripsevens = 0;
    private static int wins = 0;
    private static int reglosses = 0;
    private static int busts = 0;
    private static final int NUM_HANDS = 1000000;
    private static final int NUM_PLAYERS = 5; //doesn't include the dealer
    private static final int BLACKJACK_CHANGE = 4;
    private static final int FIVE_CARD_CHARLIE_CHANGE = 9;
    private static final int THREE_SEVENS_CHANGE = 25;
    private static final int BUST_CHANGE = -2;
    private static final int LOSS_CHANGE = -2;
    private static final int WIN_CHANGE = 1;
    private static final int STAND = 0;
    private static final int HIT = 1;
    private static final int SPLIT = 2;
    private static final int[][][] decisionTable =
            {{  {HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT},
                {HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT},
                {HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT},
                {HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT},
                {HIT, HIT, STAND, STAND, STAND, HIT, HIT, HIT, HIT, HIT},
                {STAND, STAND, STAND, STAND, STAND, HIT, HIT, HIT, HIT, HIT},
                {STAND, STAND, STAND, STAND, STAND, HIT, HIT, HIT, HIT, HIT},
                {STAND, STAND, STAND, STAND, STAND, HIT, HIT, HIT, HIT, HIT},
                {STAND, STAND, STAND, STAND, STAND, HIT, HIT, HIT, HIT, HIT},
                {STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND},
                {STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND}},

            {   {HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT},
                {HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT},
                {HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT},
                {HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT},
                {HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT},
                {STAND, STAND, STAND, STAND, STAND, STAND, STAND, HIT, HIT, HIT},
                {STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND},
                {STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND}},

            {   {HIT, HIT, SPLIT, SPLIT, SPLIT, SPLIT, HIT, HIT, HIT, HIT},
                {HIT, HIT, SPLIT, SPLIT, SPLIT, SPLIT, HIT, HIT, HIT, HIT},
                {HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT},
                {HIT, SPLIT, SPLIT, SPLIT, SPLIT, HIT, HIT, HIT, HIT, HIT},
                {SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, HIT, HIT, HIT, HIT},
                {SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT},
                {SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, HIT, SPLIT, SPLIT, HIT, HIT},
                {SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT}}};

    //first level array is hard (0), soft (1), split (2)
    //second level array is player hand: 4-8 (0), 18+ (18), all other numbers are numeric value
    //third level array is dealers card value (1 - 10) with ace is 1

    public static void main(String[] args) {

        for(int i = 0; i < NUM_HANDS; i++) {
            //create a deck of cards
            List<Card> deck = new ArrayList<>();
            populateStandardDeck(deck);
            Hand dealer = new Hand();
            List<Hand> players = new ArrayList<>();
            Collections.shuffle(deck);

            //deal the cards
            for (int j = 0; j < NUM_PLAYERS; j++) {
                players.add(new Hand());
            }
            deal(deck, dealer, players);

            //play the hand
            playHands(deck, dealer, players);

        }

        int total = 0;
        total += blackjacks * BLACKJACK_CHANGE;
        total += charlies * FIVE_CARD_CHARLIE_CHANGE;
        total += wins * WIN_CHANGE;
        total += tripsevens * THREE_SEVENS_CHANGE;
        total += busts * BUST_CHANGE;
        total += reglosses * LOSS_CHANGE;

        System.out.println("number of hands: " + NUM_HANDS);
        System.out.println("number of players: " + NUM_PLAYERS);
        System.out.println("avg blackchip change per hand: " + (total / (double)(NUM_HANDS)));
        System.out.println("blackjack: " + (blackjacks / (double)(NUM_HANDS * NUM_PLAYERS) * 100) + "%");
        System.out.println("charlie: " + (charlies / (double)(NUM_HANDS * NUM_PLAYERS) * 100) + "%");
        System.out.println("tripseven: " + (tripsevens / (double)(NUM_HANDS * NUM_PLAYERS) * 100) + "%");
        System.out.println("win: " + (wins / (double)(NUM_HANDS * NUM_PLAYERS) * 100) + "%");
        System.out.println("loss: " + ((reglosses + busts) / (double)(NUM_HANDS * NUM_PLAYERS) * 100) + "%");


    }

    private static void populateStandardDeck(List<Card> deck) {
        String[] suits = {"Spades", "Hearts", "Clubs", "Diamonds"};
        String[] values = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};

        for(String suit : suits) {
            for(String value : values) {
                deck.add(new Card(value, suit));
            }
        }
    }

    //assumes there are enough cards to handle all the players
    private static void deal(List<Card> deck, Hand dealer, List<Hand> players) {
        dealer.addVisible(deck.remove(0));
        dealer.add(deck.remove(0));

        for(Hand player : players) {
            player.add(deck.remove(0));
            player.add(deck.remove(0));
        }
    }

    private static void playHands(List<Card> deck, Hand dealer, List<Hand> players) {
        int dealerTotal = getDealerTotal(deck, dealer);
        for(Hand player : players) {
            playHand(deck, dealer.getVisible(), dealerTotal, player);
        }
        return;
    }

    private static int getDealerTotal(List<Card> deck, Hand dealer) {

        int sum = 0;
        for(Card card : dealer.cards) {
            sum += card.getNumericValue();
        }

        while(sum < 17) {
            if(dealer.hasAce() && sum + 10 >= 17 && sum + 10 <=21) {
                return sum + 10;
            }
            //hit
            Card newCard = deck.remove(0);
            sum += newCard.getNumericValue();
            dealer.add(newCard);
            if(sum > 21) {
                return 0;
            }
        }
        return sum;
    }

    //modifies wins, losses, blackjacks, etc
    private static void playHand(List<Card> deck, Card dealerVisible, int dealerTotal, Hand player) {
        int playerSum = 0;
        for(int i = 0; i < player.cards.size(); i++) {
            playerSum += player.cards.get(i).getNumericValue();
        }
        if(player.cards.size() == 5){
            charlies += 1;
        }
        if(player.cards.size() == 3 && player.cards.get(0).getNumericValue() == 7 && player.cards.get(1).getNumericValue() == 7 && + player.cards.get(2).getNumericValue() == 7) {
            tripsevens += 1;
        }
        if(playerSum == 21) {
            blackjacks += 1;
        }

        if(playerSum > 21) {
            busts++;
            return;
        }

        boolean isSplit = (player.cards.get(0).getNumericValue() == player.cards.get(1).getNumericValue()) && (player.cards.size() == 2) && (player.cards.get(0).getNumericValue() < 10) && (player.cards.get(0).getNumericValue() != 5);
        boolean isSoft = false;
        if(player.hasAce() && playerSum + 10 <= 21) {
            playerSum = playerSum + 10;
            isSoft = true;
        }
        int decision = getDecision(playerSum, isSoft, isSplit, dealerVisible);
        if(decision == STAND) {
            if(playerSum > dealerTotal) {
                wins++;
                return;
            }
            else if(playerSum < dealerTotal) {
                reglosses++;
                return;
            }
            else {
                return;
            }
        }
        else if(decision == HIT) {
            player.add(deck.remove(0));
            playHand(deck, dealerVisible, dealerTotal, player);
            return;
        }
        else { //decision must be split
            Hand newHand = new Hand();
            newHand.add(player.cards.remove(0)); //split
            player.add(deck.remove(0)); //hit original hand
            newHand.add(deck.remove(0)); //hit new hand
            playHand(deck, dealerVisible, dealerTotal, player);
            playHand(deck, dealerVisible, dealerTotal, newHand);
            return;
        }
    }

    private static int getDecision(int playerSum, boolean isSoft, boolean isSplit, Card dealerVisible) {
        //get dealerindex
        int dealerValue = dealerVisible.getNumericValue();
        int dealerIndex;
        if(dealerValue == 1) {
            dealerIndex = 9;
        }
        else {
            dealerIndex = dealerValue - 2;
        }

        //get type
        int type = 0;
        if(isSoft) {
            type = 1;
        }
        if(isSplit) { //split trumps everything
            type = 2;
        }


        if(type == 0) {
            if(playerSum >= 4 && playerSum <= 8) {
                return decisionTable[0][0][dealerIndex];
            }
            else if(playerSum >= 18) {
                return decisionTable[0][10][dealerIndex];
            }
            else {
                return decisionTable[0][playerSum - 8][dealerIndex];
            }
        }
        else if(type == 1) {
            if(playerSum >= 20) {
                return decisionTable[1][7][dealerIndex];
            }
            else {
                return decisionTable[1][playerSum - 13][dealerIndex];
            }
        }
        else { //type == 2
            if(playerSum == 2) { //aces
                return decisionTable[2][7][dealerIndex];
            }
            else if(playerSum < 10){
                return decisionTable[2][(playerSum / 2) - 2][dealerIndex];
            }
            else {
                return decisionTable[2][(playerSum / 2) - 3][dealerIndex];
            }
        }
    }
    /*
    //plays a given player's hand, returns the change in black chip value
    //bust returns 0, loss returns 1, win returns 2
    private static int playHands(List<Card> deck, Card dealersUpCard, Hand player) {

        List<Hand> additionalHands = new ArrayList<>();
        boolean bust = false;
        boolean standing = false;
        int playerSum = player.cards.get(0).getNumericValue() + player.cards.get(1).getNumericValue();
        boolean split = player.cards.get(0).getNumericValue() == player.cards.get(1).getNumericValue();
        int handType = SOFT;
        if(dealersUpCard.getValue().equals("Ace") && !split) {
            handType = HARD;
        }


        while(!bust && !standing) {
            int decision = decide(handType, playerSum, dealersUpCard.getNumericValue());
            switch(decision) {
                case 0:
                    Hand additionalHand = new Hand();
                    additionalHand.add(player.cards.remove(0));
                    additionalHands.add(additionalHand);

            }
        }


        return 0;
    }

    //returns 0 for bust, otherwise returns total
    //private static int playHand(Hand hand, int dealerUpCardValue, List<Card> deck) {

    }
    // returns 0 for split, 1 for stand, 2 for hit
    private static int decide(int handType, int playerSum, int dealerUpCardValue) {

    }
    */
}
