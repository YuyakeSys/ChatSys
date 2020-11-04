package Programming3.chatsys.tcp;
/**
 *Chester Meng
 * 2020.11.3
 * Java 1.8
 * @return
 */
import Programming3.chatsys.data.ChatMessage;
import Programming3.chatsys.data.Database;
import Programming3.chatsys.data.TextDatabase;
import Programming3.chatsys.data.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TCPChatServer {
    private TextDatabase database = new TextDatabase("messages_test.txt","user_test.txt") ;
    private int port;
    private int timeout;
    private boolean isRunning = false;
    private ServerSocket sever;
    private List<ChatMessage> messages = Collections.synchronizedList(new ArrayList<>());

    public void init(int port, int timeout){
        this.port = port;
        this.timeout = timeout;
    }

    public TCPChatServer(int port, int timeout)
    {
        this.init(port,timeout);
    }

    public void start(){
        try(ServerSocket server = new ServerSocket(port)){
            this.isRunning = true;
            while(this.isRunning){
                acceptClient(server);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Accept internet connection
     * @author Chester Meng
     * @param server
     */
    private void acceptClient(ServerSocket server){
        try{
            Socket socket = server.accept();
            System.out.println("Connected to" + socket);
            socket.setSoTimeout(timeout);
            Thread task = new Thread(new TCPChatServerSession(database, socket, this));
            task.start();
        }catch (SocketTimeoutException e){
            System.err.println("Socket timeout");
        } catch (IOException e){
            System.err.println("Socket error");
            e.printStackTrace();
        }
        /**
         * Stop the server
         */

    }
    public void stop(){
        this.isRunning = false;
        try{ ServerSocket sever = new ServerSocket(port);
            this.isRunning = false;
            sever.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMessage(ChatMessage message) {
        this.messages.add(message);
    }

    public List<ChatMessage> getMessages(int n){
        List<ChatMessage> cmgs = database.readMessages();
        n = Math.min(cmgs.size(), n);
        return cmgs.subList(cmgs.size() - n, cmgs.size());
    }


    public static void main(String[] args){
        TCPChatServer server = new TCPChatServer(1042,4200000);
        server.start();
    }

}


