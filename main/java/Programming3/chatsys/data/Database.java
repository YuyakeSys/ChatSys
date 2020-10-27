package Programming3.chatsys.data;
import java.io.File;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

public class Database {
    int Max_Id=0;
    public String file = "messages_test.txt";
    List<ChatMessage> arrayList = new ArrayList<ChatMessage>();
    ChatMessage cm = new ChatMessage();

    public List readMessages() throws FileNotFoundException,IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String str = null;
        while ((str = in.readLine()) != null) {
            cm.parse(str);
            arrayList.add(cm);
        }
        in.close();
        return arrayList;
    }
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
            message.save(file);
        } else {
            throw new Exception("Error Id");
        }
    }
    public Map<String,User> readUsers() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("user_test.txt")));
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

        public List<ChatMessage> getUnreadMessages(String userName) throws IOException {
        Database db = new Database();
        String usnLg = null;
        int lg = -1;
        List<ChatMessage> messages = db.readMessages();
        List<ChatMessage> cmg = new ArrayList<>();
        int lri = db.readUsers().get(userName).getLastReadId();
        for(int i = 0; i < messages.size();i++){
           if(messages.get(i).getId()>lri){
               cmg.add(messages.get(i));
           }
       }
       for(int i = 0; i < cmg.size();i++){
           if(cmg.get(i).getId()>lg){
               lg = cmg.get(i).getId();
               usnLg = cmg.get(i).getUserName();
           }
       }
        db.updateLastReadId(usnLg,lg);
       cmg = db.readMessages() ;
       return cmg;
    }

        private void updateLastReadId(String userName, int id) throws IOException {
        Database db = new Database();
        Map<String,User> m = db.readUsers();
        User upUser = m.get(userName);
        upUser.setLastReadId(id);
        m.remove(userName);
        m.put(userName,upUser);
        File del_file = new File("User_test.txt");
        del_file.delete();
        File file = new File("User_test.txt");
        for(User u :m.values()){
            u.save(file);
            }
        }
}
