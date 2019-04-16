import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ServerFX extends Application {

    private Server conn;
    private Integer port;

    private TextArea messages = new TextArea();
    private Label numClientLabel;    // show number of clients connecting to this server
    private Label serverStatus; // show waiting information
    private Stage myStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.myStage = primaryStage;
        this.myStage.setScene(new Scene(createContent()));
        this.myStage.show();
    }

    @Override
    public void stop() throws Exception{
        conn.closeConn();
    }

    private Server createServer() {
        return new Server(this.port,
                data-> { Platform.runLater(()->{
                    String s = data.toString();
                    if(s.contains("PLAYERNUM#")){
                        this.numClientLabel.setText(s.split("#")[1]);
                    }
                    if(s.contains("REFRESHLIST#")){
                        s = s.replace("REFRESHLIST#","");
                        this.messages.setText(s);
                    }
                });}
        );
    }

    private Parent createContent() {

        Label portLabel = new Label("Input Port:");
        TextField input = new TextField("5555");
        Button startServe = new Button("Start Server");
        Button closeServe = new Button("Close Server");
        closeServe.setDisable(true);
        this.serverStatus = new Label("");

        HBox hb1 = new HBox(20, portLabel, input);
        HBox hb2 = new HBox(20, startServe, closeServe);

        Label labelNumClient = new Label("Client #:");
        this.numClientLabel = new Label("0");

        HBox hb3 = new HBox(20, labelNumClient, numClientLabel, serverStatus);

        startServe.setOnAction(event -> {
            Integer portTmp = 0;
            try {
                portTmp = Integer.valueOf(input.getText());
            }
            catch (Exception e){
                serverStatus.setText("invalid port");
                return;
            }
            if(portTmp<1000){
                serverStatus.setText("invalid port");
                return;
            }
            this.port = portTmp;
            startServe.setDisable(true);
            closeServe.setDisable(false);
            serverStatus.setText("");

            this.conn = createServer();
            try{this.conn.startConn();}
            catch(Exception e){
            }
        });

        closeServe.setOnAction(event -> {
            startServe.setDisable(false);
            closeServe.setDisable(true);
            serverStatus.setText("Stopped");
            messages.clear();
            this.numClientLabel.setText("0");

            try{this.conn.closeConn();}
            catch(Exception e){
            }
        });

        messages.setPrefHeight(300);

        Label clientListLabel = new Label("Connected Client List:");
        VBox root = new VBox(15, hb1, hb2, hb3, clientListLabel, messages);
        root.setPrefSize(300, 500);
        root.setPadding(new Insets(20));

        return root;
    }
}
