package Programming3.chatsys.threads;

import Programming3.chatsys.data.ChatMessage;
import Programming3.chatsys.data.ReadWriteTextDatabase;
import Programming3.chatsys.data.TextDatabase;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadServer extends MessageQueue implements Runnable{
    private ThreadServer server;
    MessageQueue mq = new MessageQueue();
    TextDatabase Database = new ReadWriteTextDatabase("messages_test.txt","user_test.txt");
    MessageQueue messageQueue = new MessageQueue();
    private ReentrantLock lock = new ReentrantLock();
    private static String name;


    public static String getName() {
        return name;
    }


    private Set<ThreadClient> clients = new HashSet<>();

    public ThreadServer(TextDatabase textDatabase){
        this.Database = textDatabase;
    }

    /**
     *  The run method
     */
    @Override
    public void run(){
        System.out.println("Server starts");
        while(true){
            try {
                ChatMessage cmg = new ChatMessage();
                lock.lock();
                cmg = mq.getMessage(100);
                if(cmg!=null) {
                    System.out.println(cmg.format());
                    Database.addMessage(cmg.getMessage(), cmg.getUserName());
                    cmg = new ChatMessage();
                    lock.unlock();
                }
            }catch (InterruptedException e){
                System.out.println("Server interrupted");
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    //add to the set
    public void register(ThreadClient client){
        this.clients.add(client);
    }

    //unregister
    public void unregister(ThreadClient client){
        this.clients.remove(client);
    }

    public void forwardMessage(ChatMessage message){
        for(ThreadClient client: clients){
            if(!client.getName().equals(message.getUserName()))
            client.send(message);
            System.out.println("Forward message"+message.format());
        }

    }
}
