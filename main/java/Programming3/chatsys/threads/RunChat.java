package Programming3.chatsys.threads;

import Programming3.chatsys.data.TextDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunChat {
    public static void main(String[] args){
        ExecutorService exec = Executors.newCachedThreadPool();
        TextDatabase TextDatabase = new TextDatabase("messages_test.txt","user_test.txt");
        ThreadServer server = new ThreadServer(TextDatabase);
        ThreadClient client1 = new ThreadClient(server,"client1");
        ThreadClient client2 = new ThreadClient(server,"client2");
        exec.submit(new ThreadServer(TextDatabase));
        exec.submit(client1);
        exec.submit(client2);
    }
}
