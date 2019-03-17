import java.util.ArrayList;
import java.util.Collections;

public class PitchDealer implements Dealer {

    private Deck deck;

    PitchDealer(){
        this.deck = new Deck();
    }

    public Deck getDeck() {
        return this.deck;
    }

    @Override
    public ArrayList<Card> dealHand() {
        return this.deck.dealCard();
    }


}
