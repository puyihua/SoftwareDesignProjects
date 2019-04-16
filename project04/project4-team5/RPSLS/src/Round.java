public class Round {
    private Gesture g1,g2;
    private Integer counter;
    private String winner;

    Round(){
        this.counter = 0;
    }

    public void addGesture(Gesture g){
        if(this.counter==0){
            this.g1 = g;
            this.counter++;
        }
        else {
            this.counter++;
            this.g2 = g;
            if (g1.compare(g2) == 0)
                this.winner = "DRAW";
            else if (g1.compare(g2) > 0)
                this.winner = g1.getOwner();
            else
                this.winner = g2.getOwner();
        }
    }

    public String getWinner() {
        return winner;
    }

    public boolean roundFinish(){
        return (this.counter==2);
    }
}
