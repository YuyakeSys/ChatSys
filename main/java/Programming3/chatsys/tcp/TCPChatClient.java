package Programming3.chatsys.tcp;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
/**
 * @author Chester Meng
 * 2020.11.3
 * Java 1.8
 * @return
 */
public class TCPChatClient {
    private String host;
    private int port;
    private BufferedReader reader;
    private BufferedWriter writer;
    private Socket socket;

    public TCPChatClient(String serverHost, int serverPort){
        this.host = serverHost;
        this.port = serverPort;
    }

    /**
     * create the SSL serversocket
     * @param host host
     * @param port port
     * @return
     * @throws IOException
     */
    protected Socket initServerSocket(String host, int port) throws IOException {
        System.setProperty("javax.net.ssl.trustStore", "cctruststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "123456");
        SocketFactory factory = SSLSocketFactory.getDefault();
        SSLSocket Socket = (SSLSocket) factory.createSocket(host, port);
        return Socket;
    }

    public void connect() throws IOException {
        this.socket = this.initServerSocket(host, port);
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void send(String message) throws IOException {
        System.out.println("Sending message " + message);
        this.writer.write("POST " + message + "\r\n");
        this.writer.flush();
    }

    /**
     * Get the messages sending from the server
     * @param n the number of lines to read
     * @throws IOException
     */
    public void getMessages(int n) throws IOException {
        System.out.println("Asking for " + n + " messages");
        this.writer.write("GET" +" "+"recent"+" "+ "messages"+" "+ n + "\r\n");
        this.writer.flush();
        String line = this.reader.readLine();
        System.out.println(line);
        String[] split = line.split(" ");
        if (split.length > 1 && split[0].equals("Message")) {
            n = Integer.parseInt(split[1]);
        } else {
            System.err.println("Wrong message from server: " + line);
            return;
        }
        for (int i = 0; i < n; i++) {
            line = this.reader.readLine();
            split = line.split(" ");
            if (split.length > 1 && split[0].equals("Message:")) {
                System.out.println("Message " + i + ": " + line.substring("Message ".length()));
            } else {
                System.err.println("Wrong message from server: " + line);
                return;
            }
        }
    }

    /**
     * Disconnect from the client
     * @throws IOException
     */
    public void disconnect() throws IOException {
        this.reader.close();
        this.writer.close();
        //Socket socket = new Socket("localhost", 1042);
        this.socket.close();
    }

    /**
     * Test the function getMessages(2)
     * @param args
     */
    public static void main(String[] args) throws IOException {
        TCPChatClient client = new TCPChatClient("localhost", 1042);
        try {
            client.connect();
            client.getMessages(2);
            client.disconnect();
        } catch (UnknownHostException e) {
            System.err.println("Cannot reach the host");
        } catch (SocketTimeoutException e) {
            System.err.println("Connection closed due to timeout");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
