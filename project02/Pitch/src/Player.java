import java.util.ArrayList;

public class Player {
    private String name;
    private int score = 0;
    private int tmpScore = 0;
    private int bidScore = 0;
    private ArrayList<Card> cards;
    private ArrayList<Card> cardWon;

    Player(){
        this.cardWon = new ArrayList<>();
        this.cards = new ArrayList<>();
    };

    Player(String name){
        this.name = name;
        this.cardWon = new ArrayList<>();
        this.cards = new ArrayList<>();
    }

    public ArrayList<Card> getCards(){
        return this.cards;
    }

    public void addCards(Card c){
        this.cards.add(c);
    }

    public void setBid(int bidScore){
        this.bidScore = bidScore;
    }

    public int getBid(){
        return this.bidScore;
    }


    public void getDealtCard(PitchDealer dealer){
        this.cards = dealer.dealHand();
        for(Card c: this.cards){
            c.setOwner(this.name);
        }
    }

    private boolean checkSuit(int suit){
        boolean result = false;
        for(Card c: this.cards){
            if(c.getSuit()==suit){
                result = true;
                break;
            }
        }
        return result;
    }


    public Card playCard(int index, int trump, int suit) {

        Card errorCard = new Card(99, 99);  // placeholder and error indicator

        if(suit == 5){
            Card ptr = this.cards.get(index);
            this.cards.set(index, errorCard);
            return ptr;
        }

        int intendSuit = cards.get(index).getSuit();
        if ((trump == suit) && checkSuit(trump) && (intendSuit != trump)) {
            return errorCard;
        }
        if (checkSuit(suit) && (intendSuit != suit) && (intendSuit != trump)) {
            return errorCard;
        }
        Card ptr = this.cards.get(index);
        this.cards.set(index, errorCard);
        return ptr;
    }

    public ArrayList<Card> showCard(){
        return this.cards;
    }

    public Integer getGamePoint(){
        int sum = 0;
        for(Card c: this.cardWon){
            sum += c.getGamePoint();
        }
        return sum;
    }

    public Integer getScore(){
        return this.score;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo(){
        String txt = this.name + "\n";
        txt += "Current Score:" + this.score + "\n";
        txt += "Bid Score:" + this.bidScore;

        return txt;
    }

    public void addCardWon(Card c){
        this.cardWon.add(c);
    }

    public int getTmpScore() {
        return this.tmpScore;
    }

    public void setTmpScore(int n){
        this.tmpScore = n;
    }

    public void refreshScore() {
        if (this.bidScore == 0) {return;}
        else if (this.bidScore <= this.tmpScore) {
            this.score += this.tmpScore;
            this.bidScore = 0;
            this.tmpScore = 0;
        } else {
            this.score -= this.bidScore;
            this.bidScore = 0;
            this.tmpScore = 0;
        }
    }
}
