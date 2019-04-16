import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client {

    private String ip;
    private int port;

    private ConnThread connthread = new ConnThread();
    private Consumer<Serializable> callback;           // pass the command to the UI


    Client(String ip, int port, Consumer<Serializable> callback) {
        this.callback = callback;

        connthread.setDaemon(true);
        this.ip = ip;
        this.port = port;
    }

    public void startConn() throws Exception {
        connthread.start();
    }

    public void send(Serializable data) throws Exception {
        System.out.println("Client sent:" + data);
        connthread.out.writeObject(data);
    }

    public void closeConn() throws Exception {
        connthread.socket.close();
    }


    class ConnThread extends Thread {
        private Socket socket;
        private ObjectOutputStream out;

        public void run() {
            try(
                    Socket socket = new Socket(getIP(), getPort());
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                this.socket = socket;
                this.out = out;
                socket.setTcpNoDelay(true);

                while(true) {
                    Serializable data = (Serializable) in.readObject();
                    System.out.println("client received:" + data);
                    callback.accept(data);
                }

            }
            catch(Exception e) {
                callback.accept("$ Connection closed\n");
                System.out.println(e);
            }
        }
    }


    protected String getIP() {
        return this.ip;
    }

    protected int getPort() {
        return this.port;
    }


}