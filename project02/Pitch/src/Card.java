import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class Card {
    private String name;
    private Integer suit;
    private Integer number;
    private Integer gamePoint = 0;
    private String owner;

    private boolean colorRed = false;

    Card(Integer suit, Integer number){

        if(suit==99 && number==99){
            this.name = "";
            this.suit = 5;
        }
        else {
            this.suit = suit;
            this.number = number;

            switch (suit) {
                case 0:
                    this.name = "♠";
                    break;
                case 1:
                    this.name = "♣";
                    break;
                case 2:
                    this.name = "♦";
                    this.colorRed = true;
                    break;
                case 3:
                    this.name = "♥";
                    this.colorRed = true;
                    break;
            }

            switch (number) {
                case 10:
                    this.gamePoint = 10;
                    this.name += 10;
                    break;
                case 11:
                    this.name += "J";
                    this.gamePoint = 1;
                    break;
                case 12:
                    this.name += "Q";
                    this.gamePoint = 2;
                    break;
                case 13:
                    this.name += "K";
                    this.gamePoint = 3;
                    break;
                case 14:
                    this.name += "A";
                    this.gamePoint = 4;
                    break;
                default:
                    this.name += number;
            }
        }

    }

    public int getRank(Integer trump, Integer suitLed){
        int rank = this.number;

        if (this.suit == trump)
            rank += 1000;
        else{
            if(this.suit == suitLed)
                rank += 100;
        }
        return rank;
    }

    public int getRank(Integer trump){
        int rank = this.number;

        if (this.suit == trump)
            rank += 1000;

        return rank;
    }

    public String getName(){
        return this.name;
    }

    public boolean getColor(){
        return colorRed;
    }

    public int getGamePoint(){
        return this.gamePoint;
    }

    public void setOwner(String s){
        this.owner = s;
    }

    public String getOwner(){
        return this.owner;
    }

    public Integer getSuit(){
        return this.suit;
    }

    public Integer getNumber(){
        return this.number;
    }

}
