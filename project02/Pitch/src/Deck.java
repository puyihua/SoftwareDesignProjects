import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    private ArrayList<Card> cards;

    Deck(){
        this.give52Cards();
    }

    public ArrayList<Card> getCards(){
        return this.cards;
    }

    private void give52Cards(){
        this.cards = new ArrayList<>();

        int suits[] = new int[]{0, 1, 2, 3};

        int numbers[] = new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};

        for (int i : suits)
            for(int j : numbers){
                this.cards.add(new Card(i, j));
            }
        // shuffle
        Collections.shuffle(this.cards);
    }

    public ArrayList<Card> dealCard() {
        ArrayList<Card> tmp = new ArrayList<>();

        if (this.cards.size() < 6){
            this.give52Cards();
        }

        for(int i =0; i<6; i++){
            tmp.add(this.cards.remove(0));
        }
        return tmp;
    }
}
