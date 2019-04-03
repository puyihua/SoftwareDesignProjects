import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class RpslsClient extends Application {

    Stage myStage;
    private TextField messages = new TextField();

    String ip;
    String port;


    public static void main(String[] args) {
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) {
        this.myStage = primaryStage;
        connectScene();
    }

    @Override
    public void stop() throws Exception{
        //conn.closeConn();
    }


    private void connectScene(){

        Label ipLabel = new Label("IP  :");
        Label portLabel = new Label("Port:");
        TextField ipTextField = new TextField("0.0.0.0");
        TextField portTextField = new TextField("5555");
        Button startConnect = new Button("Connect!");
        Label warnLabel = new Label();

        startConnect.setOnAction(event -> {
            if((ipTextField.getText()=="")||(portTextField.getText()=="")){
                warnLabel.setText("Please input valid IP and Port!");
                return;
            }

            this.ip = ipTextField.getText();
            this.port = portTextField.getText();
            //waitScene();
            //TODO
            gameScene();

        });

        GridPane connectGrid = new GridPane();
        connectGrid.setPadding(new Insets(70));
        connectGrid.add(ipLabel, 0, 0);
        connectGrid.add(ipTextField, 1, 0);
        connectGrid.add(portLabel, 0, 1);
        connectGrid.add(portTextField, 1,1);

        connectGrid.add(startConnect, 0, 2);
        connectGrid.add(warnLabel, 1,2);

        Scene scene = new Scene(connectGrid, 400, 200);
        this.myStage.setScene(scene);
        this.myStage.setTitle("Rock, Paper, Scissors, Lizard, Spock");
        this.myStage.show();

    }

    private void waitScene(){
        Label lb = new Label("waiting for other to connect...");
        Pane pane = new Pane(lb);
        pane.setPadding(new Insets(20));
        Scene scene = new Scene(lb, 400, 200);

        this.myStage.setScene(scene);
        this.myStage.setTitle("Rock, Paper, Scissors, Lizard, Spock");
        this.myStage.show();
    }

    private void gameScene(){

        Image rockImg = new Image("/img/rock.png");
        ImageView rockView = new ImageView(rockImg);
        rockView.setFitHeight(30);
        rockView.setFitWidth(30);
        rockView.setPreserveRatio(true);
        
        Image paperImg = new Image("/img/paper.png");
        ImageView paperView = new ImageView(paperImg);
        paperView.setFitWidth(30);
        paperView.setFitHeight(30);
        paperView.setPreserveRatio(true);

        Image scissorsImg = new Image("/img/scissors.png");
        ImageView scissorsView = new ImageView(scissorsImg);
        scissorsView.setFitWidth(30);
        scissorsView.setFitHeight(30);
        scissorsView.setPreserveRatio(true);

        Image lizardImg = new Image("/img/lizard.png");
        ImageView lizardView = new ImageView(lizardImg);
        lizardView.setFitWidth(30);
        lizardView.setFitHeight(30);
        lizardView.setPreserveRatio(true);

        Image spockImg = new Image("/img/spock.png");
        ImageView spockView = new ImageView(spockImg);
        spockView.setFitWidth(30);
        spockView.setFitHeight(30);
        spockView.setPreserveRatio(true);
        
        
        Button rock = new Button();
        rock.setGraphic(rockView);

        Button paper = new Button();
        paper.setGraphic(paperView);

        Button scissors = new Button();
        scissors.setGraphic(scissorsView);

        Button lizard = new Button();
        lizard.setGraphic(lizardView);

        Button spock = new Button();
        spock.setGraphic(spockView);

        HBox hb1 = new HBox(5, rock, paper, scissors, lizard, spock);


        Button startNew = new Button("New Game");
        startNew.setDisable(true);

        Button closeBtn = new Button("Quit!");
        closeBtn.setOnAction(event -> {
            Stage stage = (Stage) closeBtn.getScene().getWindow();
            stage.close();
        });

        HBox hb2= new HBox(20, startNew, closeBtn);

        messages.setPrefHeight(300);
        VBox root = new VBox(20,messages, hb1, hb2);
        root.setPrefSize(350, 450);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root);
        this.myStage.setScene(scene);
        this.myStage.setTitle("Rock, Paper, Scissors, Lizard, Spock");
        this.myStage.show();
    }
}
