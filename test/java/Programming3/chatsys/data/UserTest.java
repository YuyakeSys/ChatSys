package Programming3.chatsys.data;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class UserTest {
    User user1;
    User user2;
    @Before
    public void setUp(){
        user1 = new User("Amy","Alice","123");//
        user1.save("User_test.txt");
        user2 = new User("Black","John","4567");//
        user2.save("User_test.txt");
    }
    @Test
    public void format() {
        assertEquals("Amy"+ "\t" +"Alice" + "\t" +"123"+"\t"+0,user1.format());
    }

    @Test
    public void parse() {
        user1.parse( user1.format());
        assertEquals("Amy", user1.getUserName());
    }

    @Test
    public void save() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("User_test.txt"));
        String st = in.readLine();
        System.out.print(st);
        in.close();
        assertEquals("Amy"+ "\t" +"Alice" + "\t" +"123"+"\t"+0,st);
        }


    /*@AfterClass
    public static void clean(){
        File del_file = new File("User_test.txt");
        del_file.delete();
    }
    */
}

