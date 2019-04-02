import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class Server {

    private int port;
    private int userNum;

    private ConnThread connthread = new ConnThread();
    private Consumer<Serializable> callback;

    public Server(int port, Consumer<Serializable> callback) {
        this.port = port;
        this.callback = callback;
        this.userNum = 0;
        connthread.setDaemon(true);
    }

    public void startConn() throws Exception{
        connthread.start();
    }

    public void send(Serializable data) throws Exception{
        connthread.out.writeObject(data);
    }

    public void closeConn() throws Exception{
        connthread.socket.close();
    }

    protected int getPort(){
        return this.port;
    }

    class ConnThread extends Thread{
        private Socket socket;
        private ObjectOutputStream out;

        public void run() {
            try(ServerSocket server = new ServerSocket(getPort());
                Socket socket =  server.accept();
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())){

                this.socket = socket;
                this.out = out;
                socket.setTcpNoDelay(true);

                while(true) {
                    Serializable data = (Serializable) in.readObject();
                    callback.accept(data);
                }

            }
            catch(Exception e) {
                callback.accept("connection Closed");
            }
        }
    }

}

