import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class RpslsServer extends Application{

    private Server conn;
    private Label numClient;
    private ArrayList<Integer> records;
    private TextArea messages = new TextArea();

    private Parent createContent() {


        Label portLabel = new Label("Input Port:");
        TextField input = new TextField();
        Button startServe = new Button("Start Server");
        Button closeServe = new Button("Close Server");
        closeServe.setDisable(true);
        Label serverStatus = new Label(" ");

        HBox hb1 = new HBox(20, portLabel, input);
        HBox hb2 = new HBox(20, startServe, closeServe, serverStatus);

        Label labelNumClient = new Label("Client #:");
        this.numClient = new Label("0");

        HBox hb3 = new HBox(20, labelNumClient, numClient);

        startServe.setOnAction(event -> {
            Integer port = 0;
            try {
                port = Integer.valueOf(input.getText());
            }
            catch (Exception e){
                serverStatus.setText("invalid port!!!");
                return;
            }
            if(port<1000){
                serverStatus.setText("invalid port!!!");
                return;
            }
            startServe.setDisable(true);
            closeServe.setDisable(false);
            serverStatus.setText("Running...");
            messages.appendText("Waiting for connecting...");

            this.records = new ArrayList<>();
            this.conn = new Server(port, data-> {
                Platform.runLater(()->{
                    messages.appendText(data.toString() + "\n");
                });
            });
            try{this.conn.startConn();}
            catch(Exception e){
            }
        });

        closeServe.setOnAction(event -> {
            startServe.setDisable(false);
            closeServe.setDisable(true);
            serverStatus.setText("Stopped");
            messages.clear();

            try{this.conn.closeConn();}
            catch(Exception e){
            }
        });

        messages.setPrefHeight(300);

        VBox root = new VBox(25, hb1, hb2, hb3, messages);
        root.setPrefSize(350, 500);
        root.setPadding(new Insets(20));

        return root;

    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception{
        conn.closeConn();
    }


}
