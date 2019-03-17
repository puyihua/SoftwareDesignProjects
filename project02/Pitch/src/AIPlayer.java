import java.util.ArrayList;

public class AIPlayer extends Player {
    private String name;
    private int score = 0;
    private int tmpScore = 0;
    private int bidScore = 0;
    private ArrayList<Card> cards;
    private ArrayList<Card> cardWon;

    AIPlayer(){
        this.cards = new ArrayList<>();
        this.cardWon = new ArrayList<>();
    }

    AIPlayer(String name){
        this.name = name;
        this.cards = new ArrayList<>();
        this.cardWon = new ArrayList<>();
        super.setName(name);
    }

    public ArrayList<Card> getCards(){
        return this.cards;
    }

    private Integer findTrump(){
        int record[] = new int[]{0,0,0,0};
        for(Card c: cards){
            record[c.getSuit()]++;
        }
        int max = 0;
        int maxindex = 0;
        for(int i=0;i<4;i++){
            if(record[i]>max){
                max = record[i];
                maxindex = i;
            }
        }
        return maxindex;
    }

    public void addCards(Card c){
        this.cards.add(c);
    }

    public void setBid(){
        int bid = 0;
        int trump = findTrump();

        ArrayList<Integer> allTrumpCard = new ArrayList<>();
        for(Card c: cards){
            if(c.getSuit()==trump){
                allTrumpCard.add(c.getNumber());
            }
        }

        boolean high = false;
        boolean low = false;
        boolean jack = false;
        boolean game = false;

        for(int i: allTrumpCard){
            if(i>11)
                high = true;
            if(i<5)
                low = true;
            if(i==11)
                jack = true;
            if(i==10)
                game = true;
        }
        bid += high? 1:0;
        bid += low? 1:0;
        bid += jack? 1:0;
        bid += game? 1:0;

        if(bid <2)
            this.bidScore = 0;
        else
            this.bidScore = bid;

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

    public Card playCard(int trump, int suit){
        int index = 10;
        if(trump == 5){                                 // the AI will decide the trump suit
            for(int i=0;i<cards.size();i++){
                if(cards.get(i).getSuit() == findTrump()){
                    index = i;
                    break;                              // find the suit which this AI has most,
                                                        // then play one of this suit
                }
            }
        }
        else if(suit==5){
            int max = cards.get(0).getNumber();
            for(int i=0;i<cards.size();i++){           // the AI will lead a trick
                if(cards.get(i).getNumber()>max){
                    max = cards.get(i).getNumber();    // play the card which has the most card number
                    index = i;
                }
            }
        }
        else{                                          // the AI neither lead a hand or lead a trick
            for(int i=0;i<cards.size();i++) {
                if (cards.get(i).getSuit() == trump) { // if has trump, play one of trump cards
                    index = i;
                    break;
                }
            }
            if(index==10){                             // no trump, so play one of the suit led
                for(int i=0;i<cards.size();i++) {
                    if (cards.get(i).getSuit() == suit) {
                        index = i;
                        break;
                    }
                }
            }
            if(index==10){                             // no trump, no suit led,  so the first card in the hand
                index = 0;
            }

        }
        return cards.remove(index);
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
        txt += "Current Score:" + this.score + "                    \n";
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
