package Programming3.chatsys.threads;

import Programming3.chatsys.data.TextDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunChat {
    public static void main(String[] args){
        ExecutorService exec = Executors.newCachedThreadPool();
        TextDatabase TextDatabase = new TextDatabase("messages_test.txt","user_test.txt");
        ThreadServer server = new ThreadServer(TextDatabase);
        exec.submit(new ThreadServer(TextDatabase));
        exec.submit(new ThreadClient(server,"client1"));
        exec.submit(new ThreadClient(server,"client2"));
    }
}
