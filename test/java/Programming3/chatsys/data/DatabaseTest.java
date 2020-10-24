package Programming3.chatsys.data;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import sun.plugin2.message.Message;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class DatabaseTest{
    ChatMessage cm;
    @Before
    public void setUp() throws Exception {
        cm = new ChatMessage(11,"ACE",123123123,"Test");
        cm.save("messages_test.txt");
    }

    @AfterClass
    public static void clean(){
        File del_file = new File("messages_test.txt");
        del_file.delete();
    }


    @Test
    public void readMessages() throws IOException {
        Database db = new Database();
        db.file="messages_test.txt";
        cm.save("messages_test.txt");
        db.readMessages();
        Object t = db.arrayList.get(0);
        assertEquals(cm,t);
    }
    @Test
    public void addMessages() throws Exception {
        Database db = new Database();
        ChatMessage cmg1 = new ChatMessage(10,"A",123123123,"tt");
        //ChatMessage cmg2 = new ChatMessage(18,"AB",123123123,"tt");
        //db.file="messages_test.txt";
        db.addMessage(cmg1);
        System.out.println(db.Max_Id);
        //db.addMessage(cmg2);

    }
}