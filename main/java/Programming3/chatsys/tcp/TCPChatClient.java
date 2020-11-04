package Programming3.chatsys.tcp;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class TCPChatClient {
    private String serverHost;
    private int serverPort;
    private BufferedReader reader;
    private BufferedWriter writer;
    private Socket socket;

    public TCPChatClient(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    protected Socket initServerSocket(String serverHost, int serverPort) throws IOException {
        return new Socket(serverHost, serverPort);
    }

    public void connect() throws IOException {
        this.socket = this.initServerSocket(serverHost, serverPort);
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void send(String message) throws IOException {
        System.out.println("Sending message " + message);
        this.writer.write("POST " + message + "\r\n");
        this.writer.flush();
    }

    public void getMessages(int n) throws IOException {
        System.out.println("Asking for " + n + " messages");
        this.writer.write("GET " + n + "\r\n");
        this.writer.flush();
        String line = this.reader.readLine();
        String[] split = line.split(" ");
        if (split.length > 1 && split[0].equals("MESSAGES")) {
            n = Integer.parseInt(split[1]);
        } else {
            System.err.println("Wrong message from server: " + line);
            return;
        }
        for (int i = 0; i < n; i++) {
            line = this.reader.readLine();
            split = line.split(" ");
            if (split.length > 1 && split[0].equals("MESSAGE")) {
                System.out.println("Message " + i + ": " + line.substring("MESSAGE ".length()));
            } else {
                System.err.println("Wrong message from server: " + line);
                return;
            }
        }
    }

    public void disconnect() throws IOException {
        this.reader.close();
        this.writer.close();
        this.socket.close();
    }

    public static void main(String[] args) {
        TCPChatClient client = new TCPChatClient("localhost", 1042);
        try {
            client.connect();
            client.getMessages(1);
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
