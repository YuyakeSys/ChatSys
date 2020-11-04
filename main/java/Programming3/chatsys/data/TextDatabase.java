package Programming3.chatsys.data;
import java.io.File;
import java.util.*;
import java.io.*;

public class TextDatabase implements Database {
    int Max_Id=0;
    private final File message_Db;
    private final File user_Db;

    List<ChatMessage> arrayList = new ArrayList<ChatMessage>();
    ChatMessage cm = new ChatMessage();
    public TextDatabase(String msg_txt_name, String user_txt_name){
        this.message_Db = new File(msg_txt_name);
        this.user_Db = new File(user_txt_name);
    }

    public TextDatabase(File message_Db, File user_Db){
        this.message_Db = message_Db;
        this.user_Db = user_Db;
    }

    @Override
    public List readMessages(){
        try{BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(message_Db)));
        String str = null;
        while ((str = in.readLine()) != null) {
            cm.parse(str);
            arrayList.add(cm);
            cm = new ChatMessage();
        }
        in.close();}catch (IOException e){
            e.printStackTrace();
        }
        return arrayList;
    }
    @Override
    public void addMessage(ChatMessage message) throws Exception {
        if (message.getMessage().indexOf('\n') >= 0) {
            throw new IllegalArgumentException("message contains a line feed");
        }
        List<ChatMessage> check = this.readMessages();
        for (int i = 0; i < check.size(); i++) {
            if (check.get(i).getId() >= Max_Id) {
                Max_Id = check.get(i).getId();
            }
        }
        if (message.getId() > Max_Id) {
            message.save(message_Db);
        } else {
            throw new Exception("Error Id");
        }
    }
    @Override
    public Map<String,User> readUsers() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(user_Db)));
        Map<String,User> m= new HashMap<>();
        User userDb = new User();
        String unp = null;
        String usn;
        while((unp = in.readLine()) != null) {
            userDb.parse(unp);
            usn = userDb.getUserName();
            boolean c = m.containsKey(usn);
            if (c==false) {
                m.put(usn ,userDb);
                userDb = new User();
            }
        }
        in.close();
        return m;
        }

        @Override
        public List<ChatMessage> getUnreadMessages(String userName) throws IOException {
        String usnLg = null;
        int lg = -1;
        TextDatabase db = new TextDatabase("messages_test.txt","user_test.txt");
        List<ChatMessage> messages = db.readMessages();
        List<ChatMessage> cmg = new ArrayList<>();
        int lri = this.readUsers().get(userName).getLastReadId();
        for(int i = 0; i < messages.size();i++){
           if(messages.get(i).getUserName().equals(userName)){
               cmg.add(messages.get(i));
           }
       }
            for(int i = 0; i < cmg.size();i++){
                if(cmg.get(i).getId() <= lri){
                    cmg.remove(i);
                }
            }
       if (cmg.size() > 0){
           this. updateLastReadId(userName, cmg);
       }
       return cmg;
    }

        private void updateLastReadId(String userName, List<ChatMessage> messages) throws IOException {
        Map<String,User> m = this.readUsers();
        int lrd = this.lastId(messages);
        m.get(userName).setLastReadId(lrd);
        this.user_Db.delete();
        for(User u : m.values()){
            u.save(this.user_Db);
            }
        }

    public boolean authenticate(String userName, String password) throws IOException {
        User user = this.readUsers().get(userName);
        if (user.getPassword().equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    private int lastId(List<ChatMessage> messages) {
        try {
            return messages.stream().max(Comparator.comparing(ChatMessage::getId)).get().getId();
        } catch(NoSuchElementException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
