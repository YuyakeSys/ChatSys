package Programming3.chatsys.cli;
import Programming3.chatsys.data.TextDatabase;
import Programming3.chatsys.data.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class RegisterUser {
    List<String> ck = new ArrayList<>();
    TextDatabase db = new TextDatabase("messages_test.txt","user_test.txt");

    public static void main(String[] args) throws IOException {
        TextDatabase Db = new TextDatabase("messages_test.txt","user_test.txt");
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

    public void check(String str) throws IOException {
        TextDatabase db = new TextDatabase("messages_test.txt","user_test.txt");
        Map<String, User> m = db.readUsers();
        if (m.containsKey(str)) {
            System.out.println("Exist userName");
            System.exit(1);
        }
    }
}




