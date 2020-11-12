package Programming3.chatsys.tcp;


import Programming3.chatsys.data.*;

import java.io.*;
import java.net.Socket;


import Programming3.chatsys.data.ChatMessage;

import java.util.List;
import java.util.Map;
/**
 * @author Chester Meng
 * 2020.11.3
 * Java 1.8
 * @return
 */
public class TCPChatServerSession implements Runnable{
    private TCPChatServer server;
    BufferedWriter writer;
    BufferedReader reader;
    private Database database = new ReadWriteTextDatabase("messages_test.txt","user_test.txt") ;
    private Socket socket;
    private int i = 0;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status = "unauthenticated";
    private Map<String,User> map;

    public  TCPChatServerSession(){
    }

    public TCPChatServerSession(Database database, Socket socket, TCPChatServer server){
        this.init(database, socket, server);
    }

    public void init(Database database, Socket socket, TCPChatServer server) {
        this.database = database;
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            this.persistence();
            this.initInputOutOPut();
            System.out.println("Responding to:" + socket);
            for (String line = this.reader.readLine(); line!=null; line = this.reader.readLine()) {
                System.out.println("Message:" + line);
                this.handleMessage(line);
            }
        }catch (IOException e){
            System.err.println("Error reading from the client");
        }finally {
            try{
                this.socket.close();
            }catch (IOException e){

            }
        }
    }

    /**
     * Split the messages input with " "
     * @param line The message send by the client in line
     */
    public void handleMessage(String line) {
        String[] split = line.split(" ");
        if(split.length > 1){
            switch (split[0]){
                case "GET": {
                    switch (split[1]) {
                        case "recent": {
                            this.handleGETRecentMessage(split);
                            break;
                        }
                        case "unread": {
                            this.handleUnreadMessages();
                            break;
                        }
                        default:
                            this.sendError("Unexpected error");
                    }
                    break;
                }
                case "REGISTER":{
                    this.handleRegister(line);
                    break;
                }
                case "LOGIN":{
                    this.handleLogin(split);
                    break;
                }
                case "POST":{
                    this.handlePostMessage(split);
                    break;
                }
                case "OK":{
                    break;
                }
                default:
                    this.sendError("Messages");
            }
        }

    }

    /**
     * Post function
     * @param message message sent by the client
     */
    private void handlePostMessage(String[] message){
        if(this.getStatus().equals("Authenticated")) {
            database.addMessage(message[1], this.userName);
            this.sendOk();
        }else {
            this.sendError( "Unauthenticated");
        }
    }

    /**
     * Send message to the client
     * @param messages
     * @throws IOException
     */
    private void sendMessages(List<ChatMessage> messages) throws IOException{
        System.out.println("Sending " + messages.size() + "to" + socket);
        this.writer.write("Message " + messages.size() + "\r\n");
        for (ChatMessage message: messages) {
            this.sendMessage(message);
        }
        writer.flush();
    }

    private void handleUnreadMessages(){
        if(this.status.equals("Authenticated")){
        try {
            this.sendMessages(database.getUnreadMessages(this.getUserName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        }else {
            this.sendError("ERROR unauthenticated user\r\n");
        }

    }

    /**
     * Regist user with
     * Usename Fullname Password
     * @param message
     */
    private void handleRegister(String message) {
        try {
            String[] split = message.split(" ");
            map = database.readUsers();
            User rgstUsr = new User(split[1], message.substring(message.indexOf(" "),message.lastIndexOf(" ")), split[split.length-1]);
            if (database.register(rgstUsr)) {
                this.sendOk();
            } else {
                writer.write("ERROR username already taken\r\n");
                writer.flush();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * a method can be used to send messages to the
     * @param message
     * @throws IOException
     */
    private void sendMessage(ChatMessage message) throws IOException {
        this.writer.write("Message: " + message + "\r\n");
    }

    /**
     * Method used for login
     * @param split
     */
    private void handleLogin(String[] split)  {
        if (split.length == 3) {
            try {
                this.userName = split[1];
                if (database.authenticate(split[1],split[2])) {
                    this.status = "Authenticated";
                    this.sendOk();
                } else {
                    this.sendError("Wrong username or password");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            this.sendError("Log in format");
        }
    }

    /**
     * get recent messagse
     * @param message
     */
    private void handleGETRecentMessage(String[] message) {
        if(message.length > 1 ) {
            try {
                i = Integer.parseInt(message[3]);
                this.sendMessages(this.server.getMessages(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            this.sendError("no number provided");
        }
    }

    /**
     * Initialize the stream
     * @throws IOException
     */
    private void initInputOutOPut() throws IOException {
        this.writer = new BufferedWriter(
                new OutputStreamWriter(this.socket.getOutputStream(), "UTF-8")
        );
        this.reader = new BufferedReader(
                new InputStreamReader(this.socket.getInputStream(), "UTF-8")
        );
    }

    /**
     * Send Ok message
     */
    private void sendOk() {
        try {
            this.writer.write("OK\r\n");
            this.writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Send error message
     * @param message
     */
    private void sendError(String message) {
        try {
            this.writer.write("Error: " + message + "\r\n");
            this.writer.flush();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Check whether the database exists
     */
    private void  persistence(){
        File user = new File("user_test.txt");
        File message = new File("messages_test.txt");
        if(!user.exists()){
            try{
                user.createNewFile();
                message.createNewFile();
                User user1 = new User("user1","User1","mypassword");
                User user2 = new User("user_2","Full Name","PassWord");
                user1.save("user_test.txt");
                user2.save("user_test.txt");
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

}

