import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Pitch extends Application implements DealerType {

    private Player player;
    private ArrayList<AIPlayer> players;
    private PitchDealer dealer;

    private Integer playerNum = 0;
    private Integer trick;
    private Integer winnerIndex = 0;
    private Integer trump = 5;
    private Integer curSuit = 5;
    private boolean winAll = true;
    private boolean aiWinBid = false;
    private boolean gameOver = false;
    private ArrayList<String> winnerList;

    private ArrayList<Card> trickBuffer;

    private Stage myStage;

    private Scene welcomeScene;
    private Scene gameScene;

    private Button nxtHand, exit, newGame;

    private Label gameLabel;
    private Label pointLabel;
    private Label playerLabel;
    private ArrayList<Label> AILabels;

    private AnchorPane anchorBtns;
    private GridPane playerGrid;
    private GridPane gameGrid;
    private HashMap<String, Label> gameGridFill;
    private ArrayList<Button> allCardsButton;



    public static void main(String[] args) {


        launch(args);

    }


    private void createAndSetBg(String url, BorderPane pane){
        pane.setPrefSize(900, 685);
        pane.setPadding(new Insets(50));

        Image imageBack = new Image(url);
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(imageBack, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background bg = new Background(backgroundImage);
        pane.setBackground(bg);
    }

    private VBox getBtn(String name, EventHandler<ActionEvent> event){
        Button btn = new Button(name);
        btn.setOnAction(event);

        VBox vBox = new VBox();
        vBox.setPrefWidth(96);
        vBox.setPrefHeight(40);

        btn.setMinWidth(vBox.getPrefWidth());
        btn.setMinHeight(vBox.getPrefHeight());
        vBox.getChildren().add(btn);

        return vBox;
    }

    private VBox getBtn_card(Card card, EventHandler<ActionEvent> event){
        Button btn = new Button(card.getName());
        btn.setDisable(true);
        String css = "-fx-font-size: 23px;";

        if (card.getColor()) {
            css += "-fx-text-fill: red";
        }
        btn.setStyle(css);
        btn.setOnAction(event);
        this.allCardsButton.add(btn);
        VBox vBox = new VBox();
        vBox.setPrefWidth(80);
        vBox.setPrefHeight(120);

        btn.setMinWidth(vBox.getPrefWidth());
        btn.setMinHeight(vBox.getPrefHeight());
        vBox.getChildren().add(btn);
        return vBox;
    }

    private void playerLabelRefresh(){
        // refresh labels
        String txt;
        if(this.trick<6){
            txt = "\n\nCurrent Trick: " +(1+this.trick);
        }
        else{
            txt = "\n\nCurrent Hand is OVER!";
        }

        if(this.gameOver) {
            txt += "\n\nGAME OVER!\n\n";
            txt += "Winner:\n";
            for (String s : this.winnerList) {
                txt += s + "\n";
            }
        }
        this.gameLabel.setText(txt);

        this.playerLabel.setText(this.player.getInfo());

        for(int k=1; k< this.playerNum; k++){
            int finalK = k;
            String info = this.players.get(finalK).getInfo();
            this.AILabels.get(finalK-1).setText(info);
        }

    }

    private void gameGridInitial(){
        this.gameGridFill.put("00", new Label("Trick"));
        this.gameGridFill.put("10", new Label("1"));
        this.gameGridFill.put("20", new Label("2"));
        this.gameGridFill.put("30", new Label("3"));
        this.gameGridFill.put("40", new Label("4"));
        this.gameGridFill.put("50", new Label("5"));
        this.gameGridFill.put("60", new Label("6"));

        this.gameGridFill.put("01", new Label("Player1"));
        for(int j=1;j<7;j++){
            this.gameGridFill.put(j+"1", new Label());
        }

        for(int i=1;i<this.playerNum;i++){
            this.gameGridFill.put("0"+(i+1), new Label("AI"+i));
            for(int j=1;j<7;j++){
                this.gameGridFill.put(""+j+(i+1), new Label());
            }
        }

        this.gameGridFill.put("0"+(this.playerNum+1), new Label("Winner"));
        for(int j=1;j<7;j++){
            this.gameGridFill.put(""+j+(this.playerNum+1), new Label());
        }


        for(HashMap.Entry<String, Label> p: this.gameGridFill.entrySet()){
            this.gameGrid.add(p.getValue(), Integer.valueOf(p.getKey())%10, Integer.valueOf(p.getKey())/10);

        }
        this.gameGrid.setMinSize(100, 300);
        this.gameGrid.setPadding(new Insets(40, 10, 10, 10));
        this.gameGrid.setHgap(40);
        this.gameGrid.setVgap(20);
        
    }

    private void gameGridRefresh(){
        for(int i=0; i<this.trickBuffer.size(); i++){
            int x = i % this.playerNum + 1;
            int y = i / this.playerNum + 1;
            this.gameGridFill.get(""+y+x).setText(this.trickBuffer.get(i).getName());
        }
    }

    private void tmpScorePlus(String name){
        if(name == "Player1"){
            this.player.setTmpScore(this.player.getTmpScore()+1);
        }
        else{
            int index = Integer.valueOf(name.substring(name.length()-1));
            this.players.get(index).setTmpScore(this.players.get(index).getTmpScore()+1);
        }
    }


    private void setWelcomeScene(){
        this.playerNum = 0;

        BorderPane pane = new BorderPane();
        createAndSetBg("resources/table.png", pane);

        Label welcomeLabel = new Label("Pitch v1.0");
        welcomeLabel.setFont(new Font("Arial", 60));

        Label authorLabel = new Label("By Yihua Pu \nProject for UIC CS342 \nypu5@uic.edu");
        authorLabel.setFont(new Font("Arial", 18));

        Label warningLabel = new Label();


        // Player Num Setting Buttons
        Button twoPlayers = new Button("2 Players");
        twoPlayers.setOnAction(event -> {
            playerNum = 2;
            warningLabel.setText(" ");
        });
        Button threePlayers = new Button("3 Players");
        threePlayers.setOnAction(event -> {
            playerNum = 3;
            warningLabel.setText(" ");
        });
        Button fourPlayers = new Button("4 Players");
        fourPlayers.setOnAction(event -> {
            playerNum = 4;
            warningLabel.setText(" ");
        });

        // End Game
        Button closeBtn = new Button("Quit");
        closeBtn.setOnAction(event -> {
            Stage stage = (Stage) closeBtn.getScene().getWindow();
            stage.close();
        });


        // Start Game
        Button startBtn = new Button("Start");
        startBtn.setOnAction(event -> {
            if(playerNum == 0) {
                warningLabel.setText("Please select player numbers!");
                warningLabel.setFont(new Font("Arial", 16));
                warningLabel.setTextFill(Color.web("#F00"));
            }
            else {
                this.setGameScene();
                this.myStage.setScene(this.gameScene);
            }
        });


        VBox infoBox = new VBox();
        infoBox.setSpacing(20);
        infoBox.getChildren().add(welcomeLabel);
        infoBox.getChildren().add(authorLabel);
        infoBox.getChildren().add(warningLabel);
        pane.setTop(infoBox);

        TilePane playerSelect = new TilePane();
        playerSelect.setPadding(new Insets(70, 0, 0, 0));
        HBox hbox2 = new HBox(8);
        hbox2.getChildren().addAll(twoPlayers, threePlayers, fourPlayers);
        playerSelect.getChildren().add(hbox2);
        pane.setCenter(playerSelect);


        GridPane startCloseGame = new GridPane();
        startCloseGame.setHgap(10);
        startCloseGame.setVgap(10);
        startCloseGame.add(startBtn, 0, 0);
        startCloseGame.add(closeBtn, 1, 0);
        pane.setBottom(startCloseGame);


        this.welcomeScene = new Scene(pane);

        //this.myStage.setScene(gameScene);

    }

    private void setGameScene(){
        this.gameOver = false;
        this.winnerList = new ArrayList<>();

        // create player1
        this.player =  new Player("Player1");

        // create AIs
        this.players = new ArrayList<>();
        this.players.add(new AIPlayer("PlaceHolder"));

        for (int i=1; i<this.playerNum ; i++){
            AIPlayer tmp = new AIPlayer("AI"+i);
            this.players.add(tmp);
        }

        // create dealer
        this.dealer = this.createDealer();

        this.handScene();

    }

    private void handScene(){
        this.player.setBid(0);
        this.trick = 0;
        this.trump = 5;
        this.curSuit = 5;
        this.trickBuffer = new ArrayList<>();
        this.AILabels = new ArrayList<>();
        this.playerGrid = new GridPane();
        this.gameGrid = new GridPane();
        this.gameGridFill = new HashMap<>();
        this.allCardsButton = new ArrayList<>();
        this.anchorBtns = new AnchorPane();

        this.winAll = true;
        this.aiWinBid = false;

        //BorderPane pane = new BorderPane();
        //createAndSetBg("resources/table2.png", pane);

        // deal cards
        this.player.getDealtCard(this.dealer);
        for(AIPlayer p : this.players){
            p.getDealtCard(this.dealer);
        }

        // AI bit
        for (int j=1; j<this.playerNum ; j++){
            this.players.get(j).setBid();
        }

        // user bid
        String[] bids = {"No bid", "2", "3", "4", "Smudge"};
        Integer[] bidNum = {0, 2, 3, 4, 5};

        TilePane allBidsBtn = new TilePane();
        allBidsBtn.setPrefRows(1);
        for(int i=0; i<5; i++){
            int finalI = i;
            EventHandler<ActionEvent> e = event -> {
                // player bit
                this.player.setBid(bidNum[finalI]);
                playerLabelRefresh();

                // remove bid buttons
                playerGrid.getChildren().remove(allBidsBtn);

                //enable card buttons
                for(Button b: this.allCardsButton){
                    b.setDisable(false);
                }

                //who win the bid?
                int maxbid = this.player.getBid();
                int maxBidIndex = 0;
                for(int j=1; j<this.playerNum; j++){
                    if(this.players.get(j).getBid()>maxbid){
                        maxbid = this.players.get(j).getBid();
                        maxBidIndex = j;
                    }
                }
                this.winnerIndex = maxBidIndex;
                this.bidScene();
            };
            allBidsBtn.getChildren().add(this.getBtn(bids[i], e));
        }



        // GUI
        playerGrid.setPadding(new Insets(10, 10, 10, 10));
        playerGrid.setVgap(5);
        playerGrid.setHgap(5);

        // btns: next hand, restart, quit
        nxtHand = new Button("Start Next Hand");
        nxtHand.setOnAction(event -> {
            this.handScene();
            this.myStage.setScene(this.gameScene);
        });

        exit = new Button("Exit");
        exit.setOnAction(event -> {
            this.playerNum = 0;
            this.myStage.setScene(this.welcomeScene);
        });
        newGame = new Button("Start New Game");
        newGame.setOnAction(event -> {
            this.setGameScene();
            this.myStage.setScene(this.gameScene);
        });


        // who won the game points?
        this.pointLabel = new Label();


        VBox vb = new VBox(10, nxtHand, exit, newGame, this.pointLabel);//, this.newGame);

        // get AI player label
        HBox AIInfo = new HBox();
        for(int i=1; i< this.playerNum; i++){
            int finalI = i;
            String info = this.players.get(finalI).getInfo();
            this.AILabels.add(new Label(info));
        }
        AIInfo.getChildren().addAll(AILabels);
        //AIInfo.setHgap(0);
        //AIInfo.setVgap(30);


        // show player's card
        TilePane allPlayerCards = new TilePane();
        allPlayerCards.setMinSize(500, 130);
        int i = 0;
        for(Card c : this.player.showCard()){
            int finalI = i;
            EventHandler<ActionEvent> e = event -> {
                if(winnerIndex==0){  // if the play won the last trick, she/he can play any card.
                    this.curSuit = 5;
                }
                Card cc = this.player.playCard(finalI, this.trump, this.curSuit);

                if(cc.getName()=="") {  // the player doesn't follow the rule when playing a card.
                    this.gameLabel.setText("Please follow the rule\nwhen playing cards!");
                    return;
                }
                if((this.trick == 0)&(this.aiWinBid == false)){
                    this.trump = cc.getSuit();
                    this.curSuit = cc.getSuit();
                }


                if(this.winnerIndex == 0){
                    this.trickBuffer.add(cc);
                    this.curSuit = cc.getSuit();
                }
                else
                    this.trickBuffer.set(this.trick*this.playerNum, cc);
                Button b = (Button) event.getSource();
                b.setDisable(true);

                trickScene();   // jumpy to another function

            };
            allPlayerCards.getChildren().add(getBtn_card(c, e));
            i++;
        }


//        playerGrid.add(allBidsBtn, 0, 0);
//        playerGrid.add(allPlayerCards, 0, 1);
//
//        this.playerLabel = new Label(this.player.getInfo());
//        playerGrid.add(this.playerLabel,1,0);
//
//        this.gameLabel = new Label();
//        playerGrid.add(this.gameLabel, 1, 1);
//
//
//        pane.setTop(AIInfo);
//        pane.setBottom(playerGrid);
//        pane.setRight(vb);
//
//        gameGridInitial();
//        pane.setCenter(this.gameGrid);

        playerGrid.add(AIInfo,0,0);
        gameGridInitial();
        playerGrid.add(this.gameGrid,0,1);
        playerGrid.add(allBidsBtn, 0,2);
        playerGrid.add(allPlayerCards,0,3);

        playerGrid.add(vb,1,1);
        this.playerLabel = new Label(this.player.getInfo());
        playerGrid.add(this.playerLabel, 1,2);
        this.gameLabel = new Label();
        playerGrid.add(this.gameLabel,1,3);

        GridPane pane = playerGrid;

        pane.setPrefSize(800, 680);
        pane.setPadding(new Insets(50));

        pane.setStyle("-fx-background-color: #e9e8e8;");

        this.gameScene = new Scene(playerGrid);

    }

    private void bidScene(){
        if(this.winnerIndex!=0) {
            this.aiWinBid = true;
            Card emptyFiller = new Card(99, 99);
            for (int i = 0; i < this.winnerIndex; i++) {
                this.trickBuffer.add(emptyFiller);
            }
            Card firstCard;
            firstCard = this.players.get(this.winnerIndex).playCard(this.trump, this.curSuit);
            this.trump = firstCard.getSuit();
            this.curSuit = firstCard.getSuit();
            this.trickBuffer.add(firstCard);
            for (int i = this.winnerIndex+1; i < this.playerNum; i++) {
                this.trickBuffer.add(this.players.get(i).playCard(this.trump, this.curSuit));
            }
        }
        gameGridRefresh();
    }


    private void trickScene(){

        int curPos = this.trick * this.playerNum;
        int endPos = (1 + this.trick) * this.playerNum;

        if(winnerIndex ==0) {
            for (int j = 1; j < this.playerNum; j++) {
                this.trickBuffer.add(this.players.get(j).playCard(this.trump, this.curSuit));
            }
        }
        else{
//            for (int i = this.winnerIndex; i < this.playerNum; i++) {
//                this.trickBuffer.add(this.players.get("AI" + i).playCard());
//            }
            for(int j = 1; j < this.winnerIndex; j++){
                this.trickBuffer.set(curPos+j, this.players.get(j).playCard(this.trump, this.curSuit));
            }
        }


        // who win?
        Card maxRankCard = this.trickBuffer.get(curPos);
        for(int i=curPos; i <endPos; i++){
            Card cur = this.trickBuffer.get(i);
            if(cur.getRank(this.trump, this.curSuit) > maxRankCard.getRank(this.trump, this.curSuit))
                maxRankCard = cur;
        }
        String winner = maxRankCard.getOwner();
        if(winner == "Player1"){
            this.winnerIndex = 0;
        }
        else{
            this.winnerIndex = Integer.valueOf(winner.substring(winner.length()-1));
        }

        // winner won these cards
        for(int i=curPos; i <endPos; i++){
            Card cur = this.trickBuffer.get(i);
            if(this.winnerIndex == 0)
                this.player.addCardWon(cur);
            else
                this.players.get(this.winnerIndex).addCardWon(cur);
        }
        this.gameGridFill.get(""+(this.trick+1)+(this.playerNum+1)).setText(winner);


        // winner start the next trick
        this.trick++;
        playerLabelRefresh();
        if(this.trick!=6) {
            if (winner != "Player1") {
                this.winAll = false;

                Card emptyFiller = new Card(99, 99);
                for (int i = 0; i < this.winnerIndex; i++) {
                    this.trickBuffer.add(emptyFiller);
                }

                Card firstCard;
                firstCard = this.players.get(this.winnerIndex).playCard(this.trump, this.curSuit);
                this.trickBuffer.add(firstCard);
                this.curSuit = firstCard.getSuit();
                for (int i = this.winnerIndex+1; i < this.playerNum; i++) {
                    this.trickBuffer.add(this.players.get(i).playCard(this.trump, this.curSuit));
                }

            }
            gameGridRefresh();
        }
        else{
            gameGridRefresh();
            handOver();
        }
    }


    private void handOver(){

        this.player.setTmpScore(0);
        for(int i=1; i<this.playerNum; i++){
            this.players.get(i).setTmpScore(0);
        }

        ArrayList<Card> allTrump = new ArrayList<>();
        for(Card c: this.trickBuffer){
            if(c.getRank(this.trump, this.curSuit)>1000){
                allTrump.add(c);
            }
        }

        // High low jack points
        String pointText = "Who wons points:\n";
        Card high = allTrump.get(0);
        Card low  = allTrump.get(0);

        for(Card c: allTrump){
            if(c.getRank(this.trump) > high.getRank(this.trump))
                high = c;
            if(c.getRank(this.trump) < low.getRank(this.trump))
                low = c;
            if(c.getRank(this.trump) == 1011){
                tmpScorePlus(c.getOwner());
                pointText += "jack: " + c.getOwner() +"\n";
            }
        }
        tmpScorePlus(high.getOwner());
        pointText += "high: " + high.getOwner() +"\n";
        tmpScorePlus(low.getOwner());
        pointText += "low: " + low.getOwner() +"\n";

        // Game point
        int[] gamePoint = new int[this.playerNum];
        gamePoint[0] = this.player.getGamePoint();
        for(int i=1; i<this.playerNum; i++){
            gamePoint[i] = this.players.get(i).getGamePoint();
        }
        int gamePointMaxIndex = 0;
        for(int i=1; i< this.playerNum; i++){
            if (gamePoint[i] > gamePoint[gamePointMaxIndex])
                gamePointMaxIndex = i;
        }
        if(gamePointMaxIndex == 0) {
            tmpScorePlus("Player1");
            pointText += "game: " + "Player1" +"\n";
        }
        else {
            tmpScorePlus("AI" + gamePointMaxIndex);
            pointText += "game: " + "AI" + gamePointMaxIndex +"\n";
        }

        // Smudge
        if(this.player.getBid()==5){
            if((this.player.getTmpScore()==4)&&(this.winAll)){
                this.tmpScorePlus("Player1");
            }
        }

        // show "who wons the game points?"
        this.pointLabel.setText(pointText);

        //refresh the overall score
        this.player.refreshScore();
        for(int i=1; i<this.playerNum; i++){
            this.players.get(i).refreshScore();
        }

        if(this.player.getScore()>=7){
            this.gameOver = true;
            this.winnerList.add(this.player.getName());
        }
        for(int i=1;i<this.playerNum;i++){
            if(this.players.get(i).getScore()>=7){
                this.gameOver = true;
                this.winnerList.add(this.players.get(i).getName());
            }
        }
        if(this.gameOver){
            this.nxtHand.setDisable(true);
        }


        playerLabelRefresh();

    }


    public void start(Stage primaryStage) throws Exception {
        this.myStage = primaryStage;
        this.myStage.setTitle("Pitch");

        setWelcomeScene();

        this.myStage.setScene(this.welcomeScene);


        this.myStage.show();

    }

    @Override
    public PitchDealer createDealer() {
        return new PitchDealer();
    }
}

