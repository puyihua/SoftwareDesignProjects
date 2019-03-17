import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    @Test
    void constructorTest(){
        Card c1 = new Card(99,99);
        assertEquals(5, c1.getSuit(), "Card: errorCard wrong!");

        Card c2 = new Card(0,3);
        assertEquals("♠3", c2.getName(), "Card: name is wrong!");

        Card c3 = new Card(3,14);
        assertEquals("♥A", c3.getName(), "Card: name is wrong!");

    }

    @Test
    void getRankTest1(){
        int trump = 0;
        int suit = 0;
        Card c1 = new Card(0,3);
        assertEquals(1003, c1.getRank(trump), "Card rank is wrong");

        assertEquals(1003, c1.getRank(trump,suit), "Card rank is wrong");

    }

    @Test
    void getRankTest2(){
        int trump = 0;
        int suit = 0;

        Card c1 = new Card(0,3);
        Card c2 =new Card(1,4);

        trump = 1;
        assertEquals(103, c1.getRank(trump,suit), "Card rank is wrong");

        assertEquals(1004, c2.getRank(trump,suit), "Card rank is wrong");
    }

    @Test
    void getOwnerTest(){
        Card c1 = new Card(0,3);
        c1.setOwner("PYH");

        assertEquals("PYH", c1.getOwner(), "Card Owner is wrong!");
    }

    @Test
    void getSuitTest(){
        Card c1 = new Card(3,10);

        assertEquals(3, c1.getSuit(),"Card suit error!");

        assertEquals(10,c1.getNumber(), "Card number error!");
    }


}