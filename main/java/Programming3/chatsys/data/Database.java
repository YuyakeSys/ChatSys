package Programming3.chatsys.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * @author Chester Meng
 * 2020.11.3
 * Java 1.8
 * @return
 */
public interface Database {
    /**
     * The fuctions can be seen in the textdatabase
     */
    List readMessages() throws FileNotFoundException, IOException;

    ChatMessage addMessage(String userName, String message);;

    Map<String, User> readUsers() throws IOException;

    List<ChatMessage> getUnreadMessages(String userName) throws IOException;

    public boolean authenticate(String userName, String password) throws IOException;

    boolean register(User user) throws IOException;

}
