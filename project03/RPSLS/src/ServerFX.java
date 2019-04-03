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
import java.util.ArrayList;

public class ServerFX extends Application {

    private Server conn;
    private Integer port;

    private TextArea messages = new TextArea();
    private Label numClient;
    private Label pointInfo;
    private Stage myStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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
                data-> { Platform.runLater(()->{messages.appendText(data.toString() + "\n");});},
                data-> {
                    Platform.runLater(()->{
                        String s = data.toString();
                        if(s.contains("#playerNum")){
                            this.numClient.setText(s.substring(10));
                        }
                        if(s.contains("#point:")){
                            s = s.replace("#point:","");
                            this.pointInfo.setText(s);
                        }
                        if(s.contains("#startNewGame")){
                            this.messages.clear();
                        }
                    });
                }
        );
    }

    private Parent createContent() {

        this.pointInfo = new Label("test");
        Label portLabel = new Label("Input Port:");
        TextField input = new TextField("5555");
        Button startServe = new Button("Start Server");
        Button closeServe = new Button("Close Server");
        closeServe.setDisable(true);
        Label serverStatus = new Label(" ");

        HBox hb1 = new HBox(20, portLabel, input);
        HBox hb2 = new HBox(20, startServe, closeServe, serverStatus);

        Label labelNumClient = new Label("Client #:");
        this.numClient = new Label("0");

        HBox hb3 = new HBox(20, labelNumClient, numClient, this.pointInfo);

        startServe.setOnAction(event -> {
            Integer portTmp = 0;
            try {
                portTmp = Integer.valueOf(input.getText());
            }
            catch (Exception e){
                serverStatus.setText("invalid port!!!");
                return;
            }
            if(portTmp<1000){
                serverStatus.setText("invalid port!!!");
                return;
            }
            this.port = portTmp;
            startServe.setDisable(true);
            closeServe.setDisable(false);
            serverStatus.setText("Wait connecting");
            //messages.appendText("");

            //this.records = new ArrayList<>();
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

            try{this.conn.closeConn();}
            catch(Exception e){
            }
        });

        messages.setPrefHeight(300);

        VBox root = new VBox(25, hb1, hb2, hb3, messages);
        root.setPrefSize(400, 500);
        root.setPadding(new Insets(20));

        return root;
    }
}
