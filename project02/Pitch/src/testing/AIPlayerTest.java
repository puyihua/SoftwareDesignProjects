import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AIPlayerTest {
    @Test
    void getBid() {
        AIPlayer p = new AIPlayer();
        p.addCards(new Card(0,11));
        p.addCards(new Card(0,12));
        p.addCards(new Card(0,3));
        p.setBid();

        assertEquals(3, p.getBid(), "AIPlayer: the bid getter and setter failed!");
    }

    @Test
    void getDealtCard() {
        AIPlayer p = new AIPlayer();
        PitchDealer dealer =  new PitchDealer();
        p.getDealtCard(dealer);
        ArrayList<Card> cards = p.getCards();

        assertEquals(6, cards.size(), "AIPlayer: doesn't get enough cards!");
    }

    @Test
    void playCard() {
        AIPlayer p = new AIPlayer();
        int suit = 0;
        int trump = 1;

        p.addCards(new Card(0,2));
        p.addCards(new Card(1,3));
        p.addCards(new Card(2,3));

        assertEquals(1, p.playCard(trump, suit).getSuit(), "AIPlayer: playing cards doesn't follow the rules!");
    }

    @Test
    void getGamePoint() {
        AIPlayer p = new AIPlayer();

        p.addCardWon(new Card(0,10));
        p.addCardWon(new Card(1,11));
        p.addCardWon(new Card(2,12));
        p.addCardWon(new Card(2,13));
        p.addCardWon(new Card(0,14));
        p.addCardWon(new Card(1,3));

        assertEquals(10+1+2+3+4, p.getGamePoint(), "AIPlayer: returned total Game point is wrong!");
    }

    @Test
    void refreshScore() {
        AIPlayer p = new AIPlayer();

        p.addCards(new Card(0,11));
        p.addCards(new Card(0,12));
        p.addCards(new Card(0,3));
        p.setBid();   // p.bid = 3
        p.setTmpScore(2);
        p.refreshScore();

        assertEquals(-3, p.getScore(), "AIPlayer: the score calculation is wrong!");
    }

}