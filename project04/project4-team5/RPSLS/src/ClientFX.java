import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

public class ClientFX extends Application {

    private String ip;
    private Integer port;
    private String name;

    private Stage myStage;
    private Client conn;
    private TextArea messages = new TextArea();
    private Label warnLabel;
    private Button rock, paper, scissors, lizard, spock;
    private Label playerName = new Label();

    private ObservableList<String> opponents = FXCollections.observableArrayList();
    private ComboBox challengerSelect = new ComboBox(opponents);
    private Button challengeBtn = new Button("Challenge!");
    private Button closeBtn = new Button("Quit!");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.myStage = primaryStage;
        connectScene();
    }


    @Override
    public void stop() throws Exception {
        try {
            conn.send("QUIT");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            conn.closeConn();
        }
    }

    private Client createClient() {
        return new Client(this.ip, this.port,
                data -> { // commands passed in from network
                    Platform.runLater(()->{
                        String s = data.toString().trim();
                        if (s.contains("NAME#")) {
                            this.name = getParam(s);
                            setName(name);
                            writeMessage("You have been assigned the name: " + this.name);
                            writeMessage("Please select an opponent to play...");
                        } else if (s.startsWith("CONNECTED#")) {
                            for (String opponent: getParams(s))
                            challengerSelect.getItems().add(opponent);
                        } else if (s.startsWith("DISCONNECTED#")) {
                            String player = getParam(s);
                            challengerSelect.getItems().remove(player);
                            writeMessage(player + " has quit...");
                        } else if (s.contains("REJECT")) {
                            disableButton();
                            challengerSelect.setDisable(false);
                            challengeBtn.setDisable(false);
                            writeMessage("Opponent cannot be played...");
                        } else if (s.contains("ACCEPT")) {
                            writeMessage("You are playing: " + getParam(s));
                            enableButton();
                            closeBtn.setDisable(true);
                            challengerSelect.setDisable(true);
                            challengeBtn.setDisable(true);
                        } else if (s.contains("DRAW#")) {
                            disableButton();
                            closeBtn.setDisable(false);
                            challengerSelect.setDisable(false);
                            challengeBtn.setDisable(false);
                            writeMessage("You tied with " + getParam(s));
                        } else if (s.contains("WIN#")) {
                            disableButton();
                            closeBtn.setDisable(false);
                            challengerSelect.setDisable(false);
                            challengeBtn.setDisable(false);
                            writeMessage("You won against " + getParam(s));
                        } else if (s.contains("LOSE#")) {
                            disableButton();
                            closeBtn.setDisable(false);
                            challengerSelect.setDisable(false);
                            challengeBtn.setDisable(false);
                            writeMessage("You lost against " + getParam(s));
                        } else if (s.contains("$")) {
                            writeMessage("Server is going offline...");
                            connectScene(); // go to connect scene to reconnect
                        } else {
                            writeMessage("[DEBUG] Unknown command from server: " + s);
                        }
                    });
                }

        );
    }

    // Returns the parameter sent by the server
    private String getParam(String command) {
        return command.split("#")[1];
    }

    // Returns an array of params sent by the server
    private String[] getParams(String command) {
        String[] params = command.split("#");
        return Arrays.copyOfRange(params, 1, params.length);
    }

    // Writes a message to the text box with a newline
    private void writeMessage(String message) {
        messages.appendText(message + "\n");
    }

    // First scene: allows player to connect to server
    private void connectScene() {
        Label ipLabel = new Label("IP  :");
        Label portLabel = new Label("Port:");
        TextField ipTextField = new TextField("127.0.0.1");
        TextField portTextField = new TextField("5555");
        Button startConnect = new Button("Connect!");
        this.warnLabel = new Label();

        startConnect.setOnAction(event -> {
            if((ipTextField.getText().equals("")) || (portTextField.getText().equals(""))) {
                warnLabel.setText("Please input valid IP and Port!");
                return;
            }

            this.ip = ipTextField.getText();
            this.port = Integer.valueOf(portTextField.getText());
            gameScene(); // switch scene
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

    private void gameScene() {
        // Set default params
        messages.clear();
        challengerSelect.getItems().clear();
        challengerSelect.setDisable(false);
        challengeBtn.setDisable(false);
        closeBtn.setDisable(false);
        name = null;

        // Set up connection
        this.conn = createClient();
        try {
            this.conn.startConn();
        } catch (Exception ex) {
            ex.printStackTrace();

        }

        setName(name);

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

        this.rock = new Button();
        this.rock.setGraphic(rockView);
        this.rock.setOnAction(event -> playButtonFunc(0));

        this.paper = new Button();
        this.paper.setGraphic(paperView);
        this.paper.setOnAction(event -> playButtonFunc(1));

        this.scissors = new Button();
        this.scissors.setGraphic(scissorsView);
        this.scissors.setOnAction(event -> playButtonFunc(2));

        this.lizard = new Button();
        this.lizard.setGraphic(lizardView);
        this.lizard.setOnAction(event -> playButtonFunc(3));

        this.spock = new Button();
        this.spock.setGraphic(spockView);
        this.spock.setOnAction(event -> playButtonFunc(4));

        HBox playButtons = new HBox(5, rock, paper, scissors, lizard, spock);

        disableButton();

        // Challenge button
        challengeBtn.setOnAction(event -> {
            try {
                conn.send("CHALLENGE#" + challengerSelect.getValue());
                challengerSelect.setDisable(true);
                challengeBtn.setDisable(true);
                closeBtn.setDisable(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Quit game button
        closeBtn.setOnAction(event -> {
            try {
                //conn.send("QUIT");
                stop();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Stage stage = (Stage) closeBtn.getScene().getWindow();
            stage.close();
        });

        HBox controls = new HBox(20, challengerSelect, challengeBtn, closeBtn);

        messages.setPrefHeight(300);
        VBox root = new VBox(20, messages, playButtons, playerName, controls);
        root.setPrefSize(350, 450);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root);
        this.myStage.setScene(scene);
        this.myStage.setTitle("Rock, Paper, Scissors, Lizard, Spock");
        this.myStage.show();
    }

    private void disableButton() {
        this.rock.setDisable(true);
        this.paper.setDisable(true);
        this.scissors.setDisable(true);
        this.lizard.setDisable(true);
        this.spock.setDisable(true);
    }

    private void enableButton() {
        this.rock.setDisable(false);
        this.paper.setDisable(false);
        this.scissors.setDisable(false);
        this.lizard.setDisable(false);
        this.spock.setDisable(false);
    }

    // Updates the player's name
    private void setName(String name) {
        this.playerName.setText("Playing as " + name);
    }

    private void playButtonFunc(Integer index){
        ArrayList<String> playMap = new ArrayList<>(Arrays.asList("ROCK", "PAPER", "SCISSORS", "LIZARD", "SPOCK"));
        String message = playMap.get(index);
        this.disableButton();

        try {
            conn.send("PLAY#" + message);
        }
        catch(Exception ex) {
            writeMessage("Exception occurred while sending message");
            ex.printStackTrace();
        }

    }
}