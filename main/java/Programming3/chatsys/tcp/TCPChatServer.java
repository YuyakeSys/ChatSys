package Programming3.chatsys.tcp;
/**
 * @author Chester Meng
 * 2020.11.3
 * Java 1.8
 * @return
 */
import Programming3.chatsys.data.*;

import javax.net.SocketFactory;
import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TCPChatServer {
    private Database database = new ReadWriteTextDatabase("messages_test.txt","user_test.txt") ;
    private int port;
    private int timeout;
    private boolean isRunning = false;
    private ServerSocket sever;
    private List<ChatMessage> messages = Collections.synchronizedList(new ArrayList<>());

    /**
     * init the TCP Sever
     * @param port
     * @param timeout
     */
    public void init(int port, int timeout){
        this.port = port;
        this.timeout = timeout;
    }

    public TCPChatServer(int port, int timeout){
        this.init(port,timeout);
    }

    /**
     * start
     */
    public void start() throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        String keyFile = "cckeystore.jks";
        String keyFilePass = "123456";
        String keyPass = "123456";
        SSLServerSocket sslSocket = null;
        KeyStore ks;
        KeyManagerFactory kmf;
        SSLContext SSLc = null;
        ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(keyFile), keyFilePass.toCharArray());
        kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, keyPass.toCharArray());
        SSLc = SSLContext.getInstance("SSLv3");
        SSLc.init(kmf.getKeyManagers(),null,null);
        SSLServerSocketFactory factory = SSLc.getServerSocketFactory();
        sslSocket = (SSLServerSocket) factory.createServerSocket(port);
        try{
            this.isRunning = true;
            while(this.isRunning){
                acceptClient(sslSocket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Accept internet connection
     * @author Chester Meng
     * @param server
     */
    private void acceptClient(SSLServerSocket server){
        try{
            SSLSocket socket = (SSLSocket)server.accept();
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

    /**
     * Get recent messages
     * @param n
     * @return
     * @throws IOException
     */
    public List<ChatMessage> getMessages(int n) throws IOException {
        List<ChatMessage> cmgs = database.readMessages();
        n = Math.min(cmgs.size(), n);
        return cmgs.subList(cmgs.size() - n, cmgs.size());
    }


    public static void main(String[] args) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        TCPChatServer server = new TCPChatServer(1042,42000);
        server.start();
    }

}


