package Programming3.chatsys.threads;

import Programming3.chatsys.data.ChatMessage;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeoutException;

public class MessageQueue{
    Queue<ChatMessage> queue = new LinkedList<ChatMessage>();
    public Queue send(ChatMessage message) {
            queue.offer(message);
        return queue;
    }

    public ChatMessage getMessage(int waitTime) throws InterruptedException {
        ChatMessage msg = new ChatMessage();
        while (true){
            msg = queue.poll();
            if(msg!=null){
                return msg;
            }else {
                Thread.sleep(waitTime);
            }
        }
    }
}
