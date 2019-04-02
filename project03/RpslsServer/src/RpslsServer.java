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
    private ArrayList<Integer> records;
    private TextArea messages = new TextArea();

    private Parent createContent() {
        messages.setPrefHeight(550);

        Label portLabel = new Label("Input Port:");
        TextField input = new TextField();
        Button startServe = new Button("Start Server");
        Button closeServe = new Button("Close Server");
        closeServe.setDisable(true);
        Label serverStatus = new Label(" ");

        HBox hb1 = new HBox(20, portLabel, input);
        HBox hb2 = new HBox(20, startServe, closeServe, serverStatus);

        startServe.setOnAction(event -> {
            startServe.setDisable(true);
            closeServe.setDisable(false);
            serverStatus.setText("Running...");

            Integer port = Integer.valueOf(input.getText());

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

            try{this.conn.closeConn();}
            catch(Exception e){
            }
        });


        VBox root = new VBox(20, hb1, hb2);
        root.setPrefSize(400, 400);
        root.setPadding(new Insets(50));

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
