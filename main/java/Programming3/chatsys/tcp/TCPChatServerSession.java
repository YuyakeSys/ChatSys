package Programming3.chatsys.tcp;


import Programming3.chatsys.data.ChatMessage;
import Programming3.chatsys.data.Database;
import Programming3.chatsys.data.TextDatabase;

import java.io.*;
import java.net.Socket;


import Programming3.chatsys.data.ChatMessage;
import Programming3.chatsys.data.TextDatabase;
import Programming3.chatsys.data.User;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class TCPChatServerSession implements Runnable{
    private TCPChatServer server;
    BufferedWriter writer;
    BufferedReader reader;
    private TextDatabase database = new TextDatabase("messages_test.txt","user_test.txt") ;
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

    public TCPChatServerSession(TextDatabase database, Socket socket, TCPChatServer server){
        this.init(database, socket, server);
    }

    public void init(TextDatabase database, Socket socket, TCPChatServer server){
        this.database = database;
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
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
                    this.handleRegister(split);
                    break;
                }
                case "LOGIN":{
                    this.handleLogin(split);
                    break;
                }
                case "POST":{
                    this.handlePostMessage(line);
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

    private void handlePostMessage(String message){
        if(this.getStatus().equals("Authenticated")) {
            this.sendOk();
        }else {
            this.sendError( "Unauthenticated");
        }
    }


    private void sendMessages(List<ChatMessage> messages) throws IOException{
        System.out.println("Sending  " + messages.size() + "to" + socket);
        this.writer.write("Message  " + messages.size() + "\r\n");
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

    private void handleRegister(String[] split) {
        try {
            map = database.readUsers();
            User rgstUsr = new User(split[1], split[2], split[3]);
            if (!map.containsKey(split[1])) {
                rgstUsr.save("user_test.txt");
                this.sendOk();
            } else {
                writer.write("ERROR username already taken\r\n");
                writer.flush();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void sendMessage(ChatMessage message) throws IOException {
        this.writer.write("Message: " + message + "\r\n");
    }

    private void handleLogin(String[] split)  {
        if (split.length == 3) {
            try {
                String username = split[1];
                this.userName = username;
                if (split[2].equals(database.readUsers().get(username).getPassword())) {
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



    private void initInputOutOPut() throws IOException {
        this.writer = new BufferedWriter(
                new OutputStreamWriter(this.socket.getOutputStream(), "UTF-8")
        );
        this.reader = new BufferedReader(
                new InputStreamReader(this.socket.getInputStream(), "UTF-8")
        );
    }

    private void sendOk() {
        try {
            this.writer.write("OK\r\n");
            this.writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void sendError(String message) {
        try {
            this.writer.write("Error: " + message + "\r\n");
            this.writer.flush();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

}

