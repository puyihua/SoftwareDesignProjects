import java.net.Socket;
import java.util.HashMap;

public class ConnectedClientList {
    private Integer counter;
    private HashMap<String, ConnectedClient> clients;

    ConnectedClientList(){
        this.clients = new HashMap<>();
        this.counter = 0;
    }

    public String add(Socket s){
        String name = "Player" + this.counter;
        this.clients.put(name, new ConnectedClient(s));
        this.counter++;
        return name;
    }

    public String getNameList(){
        String nameList = "";
        for(String s: this.clients.keySet()){
            nameList += s + "\n";
        }
        return nameList;
    }

    public ConnectedClient get(String name){
        return this.clients.get(name);
    }

    public HashMap<String, ConnectedClient> getAll(){
        return this.clients;
    }

    public void remove(String name){
        this.clients.get(name).close();
        this.clients.remove(name);
    }

    public Integer clientNum(){
        return this.clients.size();
    }

    public void closeConn(){
        for(ConnectedClient c: this.clients.values()){
            c.close();
        }
    }


}

class ConnectedClient{
    private boolean canPlay;
    private Socket socket;

    ConnectedClient(Socket s){
        this.socket = s;
        this.canPlay = false;
    }

    public void setCanPlay(boolean canPlay) {
        this.canPlay = canPlay;
    }

    public boolean isCanPlay() {
        return canPlay;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public void close(){
        try {
            this.socket.close();
        }
        catch (Exception e){

        }
    }

}
