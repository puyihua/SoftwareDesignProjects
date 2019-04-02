import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class RpslsClient extends Application {

    Stage myStage;

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
        conn.closeConn();
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
            waitScene();

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

    private void waitScene(){}
}
