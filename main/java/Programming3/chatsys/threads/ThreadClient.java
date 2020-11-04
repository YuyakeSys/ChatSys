package Programming3.chatsys.threads;

import Programming3.chatsys.data.ChatMessage;

public class ThreadClient extends MessageQueue implements Runnable {
    private static String name;
    final private ThreadServer server;

    public ThreadClient (ThreadServer server, String name){
        this.server = server;
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
    MessageQueue mq = new MessageQueue();
    ChatMessage cmg = new ChatMessage();
    ChatMessage chatMessage = new ChatMessage();

    @Override
    public void run(){
        System.out.println("Client starts"+this.getName());
        while(true){
            ThreadClient client = new ThreadClient(server,name);
            server.register(client);
            System.out.println("Hello World");
                try {
                    cmg = mq.getMessage(100);
                    System.out.println(cmg.format());
                }catch (InterruptedException e){
                    server.unregister(client);
                    break;
                }

        }
    }
}
