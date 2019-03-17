import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void getBid() {
        Player p = new Player();
        p.setBid(5);

        assertEquals(5, p.getBid(), "Player: the bid getter and setter failed!");
    }

    @Test
    void getDealtCard() {
        Player p = new Player();
        PitchDealer dealer =  new PitchDealer();
        p.getDealtCard(dealer);
        ArrayList<Card> cards = p.getCards();

        assertEquals(6, cards.size(), "Player: doesn't get enough cards!");
    }

    @Test
    void playCard() {
        Player p = new Player();
        int suit = 0;
        int trump = 1;

        p.addCards(new Card(0,2));
        p.addCards(new Card(1,3));
        p.addCards(new Card(2,3));

        assertEquals(5, p.playCard(2, trump, suit).getSuit(), "Player: playing cards doesn't follow the rules!");
    }

    @Test
    void getGamePoint() {
        Player p = new Player();

        p.addCardWon(new Card(0,10));
        p.addCardWon(new Card(1,11));
        p.addCardWon(new Card(2,12));
        p.addCardWon(new Card(2,13));
        p.addCardWon(new Card(0,14));
        p.addCardWon(new Card(1,3));

        assertEquals(10+1+2+3+4, p.getGamePoint(), "Player: returned total Game point is wrong!");
    }

    @Test
    void refreshScore() {
        Player p = new Player();

        p.setBid(5);
        p.setTmpScore(3);
        p.refreshScore();

        assertEquals(-5, p.getScore(), "Player: the score calculation is wrong!");
    }
}