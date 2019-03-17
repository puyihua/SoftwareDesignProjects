import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void Deck(){
        Deck d = new Deck();

        assertEquals(52, d.getCards().size(), "Deck: the card amount is wrong!");

    }

    @Test
    void dealCard() {
        Deck d = new Deck();

        ArrayList<Card> c = d.dealCard();

        assertEquals(6, c.size(), "Deck: cards dealt is wrong!");

    }

    @Test
    void cardAfterDealt(){
        Deck d = new Deck();
        d.dealCard();

        assertEquals(52-6, d.getCards().size(),"Deck: the card amount is wrong!");
    }

    @Test
    void noRepeatingCard(){
        Deck d = new Deck();
        ArrayList<Card> cards = d.getCards();

        HashMap<String, Integer> cardMap = new HashMap<>();

        for(Card c: cards){
            if(cardMap.get(c.getName())==null)
                cardMap.put(c.getName(), 1);
            else
                cardMap.put(c.getName(), cardMap.get(c.getName())+1);
        }
        for(int i :cardMap.values()){
            assertEquals(1,i, "Deck: repeating cards!");
        }
    }


    @Test
    void reShuffle(){
        Deck d = new Deck();
        d.dealCard();
        d.dealCard();
        d.dealCard();
        d.dealCard();
        d.dealCard();
        d.dealCard();
        d.dealCard();
        d.dealCard();

        d.dealCard();

        assertEquals(52-6, d.getCards().size(), "Deck: reshuffling is wrong!");

    }
}