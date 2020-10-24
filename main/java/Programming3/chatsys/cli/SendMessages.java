package Programming3.chatsys.cli;

import Programming3.chatsys.data.ChatMessage;
import Programming3.chatsys.data.Database;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class SendMessages {
    public static void main(String[] args) throws IOException {
        String filename = "messages_test.txt";
        Scanner scan = new Scanner(System.in);
        System.out.println("userName:");
        String str = scan.next();
        File file = new File(filename);
        int last_id = -1;

        if (!file.exists() || file.length() == 0) {
            last_id = 0;
        } else {
            Database db = new Database();
            List<ChatMessage> check = db.readMessages();
            for (int i = 0; i < check.size() - 1; i++) {
                if (check.get(i).getId() >= last_id) {
                    last_id = check.get(i).getId();
                }
            }
        }
        System.out.println("TextMessage:");
        String text = scan.next();
        last_id++;
        ChatMessage cmg = new ChatMessage(last_id, str, text);
        cmg.save("messages_test.txt");
        System.out.println("Sent");
        last_id++;
        System.out.println("Continue?");
        while (true) {
            String ss = scan.nextLine();
            if (ss.equals("y") || ss.equals("Y")) {
                System.out.println("TextMessage:");
                String text2 = scan.next();
                ChatMessage cmg2 = new ChatMessage(last_id, str, text2);
                cmg2.save("messages_test.txt");
                last_id++;
                System.out.println("Continue?");
            } else if (ss.equals("n") || ss.equals("N")) {
                break;
            }
        }
        scan.close();
    }
}
