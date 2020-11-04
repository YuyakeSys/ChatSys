package Programming3.chatsys.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Database {
    List readMessages() throws FileNotFoundException, IOException;

    void addMessage(ChatMessage message) throws Exception;

    Map<String, User> readUsers() throws IOException;

    List<ChatMessage> getUnreadMessages(String userName) throws IOException;

    public boolean authenticate(String userName, String password) throws IOException;


}
