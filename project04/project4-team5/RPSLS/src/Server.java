import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.function.Consumer;

public class Server {

    private int port;

    private ConnThread connthread;
    private Consumer<Serializable> callback;

    Server(int port, Consumer<Serializable> callback) {
        this.callback = callback;
        this.connthread = new ConnThread();
        this.connthread.setDaemon(true);
        this.port = port;
    }

    public void startConn(){
        connthread.start();
    }

    public void sendAll(Serializable data) throws Exception{
        System.out.println("Server Send All:"+ data);
        for( ClientThread t: connthread.sockets.values()){
            t.out.writeObject(data);
        }
    }

    public void send(Serializable data, ClientThread t) throws Exception{
        System.out.println("Server Sent:" + data);
        t.out.writeObject(data);
    }

    public void closeConn() throws Exception{
        for( ClientThread t: connthread.sockets.values()) {
            t.socket.close();
        }
        connthread.server.close();
    }


    protected int getPort() {
        return port;
    }

    private void updateClientNumFX(){
        callback.accept("PLAYERNUM#" + connthread.sockets.size());
    }

    private void refreshClientList(){
        String base = "REFRESHLIST#";
        for(String s : connthread.sockets.keySet()){
            base += s + "\n";
        }
        callback.accept(base);
    }


    class ConnThread extends Thread{
        private ServerSocket server;
        private Integer counter;
        private HashMap<String, ClientThread> sockets;

        ConnThread(){
            this.counter = 0;
            this.sockets = new HashMap<>();
        }

        public void run() {
            try{
                this.server = new ServerSocket(getPort());
                while(true) {
                    Socket s = server.accept();
                    String name = "Player" + this.counter;

                    ClientThread t = new ClientThread(s, name);
                    String allPreviousClient = "CONNECTED#";
                    for(String n: sockets.keySet()){
                        allPreviousClient += n + "#";
                    }
                    send(allPreviousClient, t);

                    sendAll("CONNECTED#" + name);

                    this.counter++;
                    this.sockets.put(name, t);

                    // inform this client about its name
                    send("NAME#" + name, t);

                    // update client number in JavaFX
                    updateClientNumFX();

                    // refresh client list in JavaFX
                    refreshClientList();


                    t.start();
                }

            }
            catch(Exception e) {
                System.out.println(e);
                callback.accept("connection Closed, this is from the callback of cnnthread in server.java");
            }
        }
    }

    public class ClientThread extends Thread {
        private Socket socket;
        private String name;
        private Boolean canPlay;
        private ObjectOutputStream out;
        private ObjectInputStream in;

        private ClientThread opponent;
        private Round round;


        ClientThread(Socket s, String name){
            this.socket = s;
            this.name = name;
            this.canPlay = false;

            try {
                this.socket.setTcpNoDelay(true);
                this.out = new ObjectOutputStream(this.socket.getOutputStream());
                this.in = new ObjectInputStream(this.socket.getInputStream());
            }
            catch (Exception e) {
                System.out.println(e);
                System.out.println("this is from ClientThread, Sever.java");
            }
        }

        public void run() {
            try{
                while(true) {
                    String data = in.readObject().toString();
                    System.out.println("Server received:"+data);

                    if (data.contains("QUIT")) {
                        Server.this.connthread.sockets.remove(this.name);
                        refreshClientList();
                        sendAll("DISCONNECTED#" + this.name);
                        this.socket.close();
                        break;
                    }

                    if(this.canPlay == false) {

                        if (data.contains("CHALLENGE#")) {
                            String player1 = this.name;
                            String player2 = data.replace("CHALLENGE#", "");

                            if (Server.this.connthread.sockets.get(player2).canPlay) {   // player2 is playing with other
                                send("REJECT", this);
                                //return;
                            }
                            else {
                                this.opponent = Server.this.connthread.sockets.get(player2);
                                this.opponent.opponent = this;

                                this.canPlay = true;
                                this.opponent.canPlay = true;

                                this.round = new Round();
                                this.opponent.round = this.round;

                                send("ACCEPT#"+player2, this);
                                send("ACCEPT#"+player1, this.opponent);

                                //return;
                            }
                        }
                    }
                    else{
                        if (data.contains("PLAY#")){
                            String s = data.replace("PLAY#", "");
                            this.round.addGesture(new Gesture(s, this.name));

                            if(this.round.roundFinish()){
                                String winnerName = this.round.getWinner();
                                if (winnerName.equals("DRAW")){
                                    send("DRAW#"+this.opponent.name, this);
                                    send("DRAW#"+this.name, this.opponent);

                                    this.canPlay = false;
                                    this.opponent.canPlay = false;

                                    this.round = null;
                                    this.opponent.round = null;
                                }
                                else{
                                    ClientThread winner = Server.this.connthread.sockets.get(winnerName);
                                    send("WIN#" + winner.opponent.name, winner);
                                    send("LOSE#" + winner.name, winner.opponent);

                                    winner.canPlay = false;
                                    winner.opponent.canPlay = false;

                                    winner.round = null;
                                    winner.opponent.round = null;
                                }
                            }
                        }
                    }
                }
            }
            catch(Exception e) {
                System.out.println(e + "this is from run() in ClientThread in Server.java");

                callback.accept("connection Closed, this is from clientThread");
            }
        }

    }

}