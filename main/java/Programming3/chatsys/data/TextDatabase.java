package Programming3.chatsys.data;
import java.io.File;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

/**
 *Chester Meng
 * 2020.11.3
 * Java 1.8
 * @return
 */
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

    /**
     * read USER Database
     * @return list of chat messages
     */
    public List readMessages(){
        try{BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(this.message_Db)));
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

    /**
     * Add the message to the database
     * @param message
     * @param userName the user who sent the messasge
     * @return the chatmessage
     */
    public ChatMessage addMessage(String message, String userName) {
        if (message.indexOf('\n') >= 0) {
            throw new IllegalArgumentException("message contains a line feed");
        }
        ChatMessage chatMessage = new ChatMessage(this.lastId(this.readMessages())+1, userName, message);
        chatMessage.save(this.message_Db);
        return chatMessage;
    }

    /**
     * read USER Database
     * @return map of the user with the key(username)
     * @throws IOException
     */
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

    /**
     * Get the messages larger than the user's last read id
     * @param userName
     * @return the list of unread messages
     * @throws IOException
     */
        @Override
        public List<ChatMessage> getUnreadMessages(String userName) throws IOException {
        List<ChatMessage> messages = this.readMessages();
        List<ChatMessage> cmg = new LinkedList<ChatMessage>();
        final int lri = this.readUsers().get(userName).getLastReadId();
        for(int i = 0; i < messages.size();i++){
           if(messages.get(i).getUserName().equals(userName)){
               cmg.add(messages.get(i));
           }
       }
            for(int i=cmg.size()-1;i>=0;i--){
                if(cmg.get(i).getId() <= lri){
                    cmg.remove(i);
                }
            }
            if(cmg.size() > 0) {
                this.updateLastReadId(userName, cmg);
            }
           return cmg;
    }

    /**
     * update a user's last_read_id
     * @param userName
     * @param messages the messages of the user
     * @throws IOException
     **/
    private void updateLastReadId(String userName, List<ChatMessage> messages) throws IOException {
        Map<String,User> m = this.readUsers();
        int lrd = this.lastId(messages);
        m.get(userName).setLastReadId(lrd);
        this.user_Db.delete();
        for(User u : m.values()){
            u.save(this.user_Db);
        }
    }


    /**
     * to know whether logged successfully
     * @param userName
     * @param password
     * @return whether log in successfully(true/false)
     * @throws IOException
     */
    public boolean authenticate(String userName, String password) throws IOException {
        User user = this.readUsers().get(userName);
        if (user.getPassword().equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Find out the last id of messages
     * @param messages the list of messages
     * @return the last id
     */
    private int lastId(List<ChatMessage> messages) {
        try {
            return messages.stream().max(Comparator.comparing(ChatMessage::getId)).get().getId();
        } catch(NoSuchElementException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Add a user to the database
     * @param user the added user
     */
    public boolean register(User user) throws IOException {
        Map<String, User> rgstUsers = this.readUsers();
        if (rgstUsers.get(user.getUserName()) == null) {
            user.save(this.user_Db);
            return true;
        } else {
            return false;
        }
    }
}
