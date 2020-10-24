package Programming3.chatsys.data;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.sql.Timestamp;

import static org.junit.Assert.*;

public class ChatMessageTest {


    ChatMessage cm;
    @Before
    public void setUp(){
        cm = new ChatMessage(1,"ACE",123123123,"Test");//

    }
    @Test
    public void format() {
        assertEquals(1+ "\t" +"ACE" + "\t" +123123123+"\t"+"Test",cm.format());
    }

    @Test
    public void parse() {
        cm.parse(cm.format());
        assertEquals("ACE",cm.getUserName());
        assertEquals(1,cm.getId());
    }

     @Test
    public void save() throws IOException {
        cm.save("messages_test.txt");
        BufferedReader in = new BufferedReader(new FileReader("messages_test.txt"));
        String st = in.readLine();//connect the char to string
        System.out.print(st);
        in.close();
        assertEquals(1+ "\t" +"ACE" + "\t" +123123123+"\t"+"Test",st);
    }
    @AfterClass
    public static void clean(){
        File del_file = new File("messages_test.txt");
        del_file.delete();
    }
}
