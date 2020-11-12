package Programming3.chatsys.data;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class TextDatabaseTest {
    ChatMessage cm;

    /**
     * Single test has passed
     **/
    @Before
    public void setUp()  {
        cm = new ChatMessage(11, "John", 123123123, "Test");
        cm.save("messages_test.txt");
        User user1 = new User("John", "Jony", "1234");
        user1.save("user_test.txt");
    }


    @AfterClass public static void clean(){
     File del_file = new File("messages_test.txt");
     del_file.delete();
     File del_file_user = new File("user_test.txt");
     del_file_user.delete();
     }



    @Test
    public void readMessages() throws IOException {
        TextDatabase db = new TextDatabase("messages_test.txt", "user_test.txt");
        //cm.save("messages_test.txt");
        List<ChatMessage> list = db.readMessages();
        System.out.println(list.size());
        System.out.println(list.get(0));
        ChatMessage t = list.get(0);
        assertEquals(11, t.getId());
    }

    @Test
    public void addMessages() throws Exception {
        TextDatabase db = new TextDatabase("messages_test.txt", "user_test.txt");
        //ChatMessage cmg1 = new ChatMessage(10,"A",123123123,"tt");
        ChatMessage cmg2 = new ChatMessage(18, "AB", 123123123, "tt");
        //db.file="messages_test.txt";
        db.addMessage("tt", "AB");
        System.out.println(db.Max_Id);
        //db.addMessage(cmg2);

    }

    /**
     * in this test we have to insert users in the userTest first
     * also didn't add the assert just by reading it
     *
     * @throws IOException
     */
    @Test
    public void readUsers() throws IOException {
        TextDatabase db = new TextDatabase("messages_test.txt", "user_test.txt");
        Map<String, User> map = db.readUsers();
        Set set = map.entrySet();
        Iterator iterator = set.iterator();

        while (iterator.hasNext()) {
            Map.Entry mapentry = (Map.Entry) iterator.next();
            System.out.println(mapentry.getKey() + "   " + mapentry.getValue());

        }

    }

    @Test
    public void getUnreadMessages() throws IOException {
        TextDatabase db = new ReadWriteTextDatabase("messages_test.txt", "user_test.txt");
        List<ChatMessage> cm = db.getUnreadMessages("John");
            assertEquals("John",cm.get(0).getUserName());
    }
}


