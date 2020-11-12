package Programming3.chatsys.cli;
import Programming3.chatsys.data.TextDatabase;
import Programming3.chatsys.data.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
/**
 *Chester Meng
 * 2020.11.3
 * Java 1.8
 * @return
 */

public class RegisterUser {
    List<String> ck = new ArrayList<>();
    TextDatabase db = new TextDatabase("messages_test.txt","user_test.txt");

    public static void main(String[] args) throws IOException {
        TextDatabase Db = new TextDatabase("messages_test.txt","user_test.txt");
        File user = new File("user_test.txt");
        if(!user.exists()){
            try{
                user.createNewFile();
                User user1 = new User("user1","User1","mypassword");
                User user2 = new User("user_2","“Full Name","PassWord");
                user1.save("user_test.txt");
                user2.save("user_test.txt");
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        RegisterUser re = new RegisterUser();
        Scanner scan = new Scanner(System.in);
        System.out.println("--------Register------");
        System.out.println("User name; full name; password(separate with space)");
        String UserName_1 = scan.next();
        String FullName_1 = scan.next();
        String Password_1 = scan.next();
        re.check(UserName_1);
        User new_user = new User(UserName_1, FullName_1, Password_1);
        new_user.save("user_test.txt");
    }

    /**
     * check whether the username exists
     * @param str
     * @throws IOException
     */
    public void check(String str) throws IOException {
        TextDatabase db = new TextDatabase("messages_test.txt","user_test.txt");
        Map<String, User> m = db.readUsers();
        if (m.containsKey(str)) {
            System.out.println("Exist userName");
            System.exit(1);
        }
    }
}




