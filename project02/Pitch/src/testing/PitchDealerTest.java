import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PitchDealerTest {

    @Test
    void PitDealer(){
        PitchDealer p = new PitchDealer();
        Deck d = p.getDeck();

        assertEquals(52, d.getCards().size(), "Dealer: wrong cards amount");
    }

    @Test
    void dealHand(){
        PitchDealer p = new PitchDealer();
        p.dealHand();
        Deck d = p.getDeck();

        assertEquals(52-6, d.getCards().size(), "Dealer: wrong cards amount");
    }

    @Test
    void reshuffle(){
        PitchDealer p = new PitchDealer();
        p.dealHand();
        p.dealHand();
        p.dealHand();
        p.dealHand();
        p.dealHand();
        p.dealHand();
        p.dealHand();
        p.dealHand();
        p.dealHand();

        Deck d = p.getDeck();

        assertEquals(52-6, d.getCards().size(), "Dealer: wrong cards amount");

    }

    @Test
    void noRepeatingCard(){
        PitchDealer p = new PitchDealer();
        Deck d = p.getDeck();
        ArrayList<Card> cards = d.getCards();

        HashMap<String, Integer> cardMap = new HashMap<>();

        for(Card c: cards){
            if(cardMap.get(c.getName())==null)
                cardMap.put(c.getName(), 1);
            else
                cardMap.put(c.getName(), cardMap.get(c.getName())+1);
        }
        for(int i :cardMap.values()){
            assertEquals(1,i, "Dealer: repeating cards!");
        }
    }

    @Test
    void dealDifferentCards(){
        PitchDealer p = new PitchDealer();
        ArrayList<Card> c1, c2;

        c1 = p.dealHand();
        c2 = p.dealHand();

        Card firstCard = c1.get(0);

        for(Card c: c2){
            assertNotEquals(c.getName(),firstCard.getName(),"Dealer: repeating cards");
        }
    }

}