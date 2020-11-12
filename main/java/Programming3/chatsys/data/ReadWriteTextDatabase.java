package Programming3.chatsys.data;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 * @author Chester Meng
 * 2020.11.3
 * Java 1.8
 * @return
 */
public class ReadWriteTextDatabase extends TextDatabase{
    private final ReadWriteLock usersLock = new ReentrantReadWriteLock();
    private final ReadWriteLock messagesLock = new ReentrantReadWriteLock();

    public ReadWriteTextDatabase(String msg_txt_name, String user_txt_name) {
        super(msg_txt_name, user_txt_name);
    }
    public ReadWriteTextDatabase(File message_Db, File user_Db) {
        super(message_Db, user_Db);
    }

    @Override
    public List readMessages(){
        this.readLockMessages();
        try{
            return  super.readMessages();
        }finally {
            this.readUnlockMessages();
        }
    }

    @Override
    public ChatMessage addMessage(String message, String userName)  {
        this.writeLockMessages();
        try {
            return super.addMessage(message, userName);
        }finally {
            this.writeLockMessages();
        }
    }

    @Override
    public Map<String,User> readUsers() throws IOException {
        this.readLockUsers();
        try {
            return super.readUsers();
        } finally {
            this.readUnlockUsers();
        }
    }

    @Override
    public List<ChatMessage> getUnreadMessages(String userName) throws IOException {
        this.writeLockUsers();
        this.readLockMessages();
        try {
            return super.getUnreadMessages(userName);
        } finally {
            this.readUnlockMessages();
            this.writeUnlockUsers();
        }
    }

    @Override
    public boolean authenticate(String userName, String password) throws IOException {
        this.readLockUsers();
        try {
           return super.authenticate(userName, password);
        }finally{
            this.readUnlockUsers();
        }
    }

    @Override
    public boolean register(User user) throws IOException {
        this.writeLockUsers();
        try {
            return super.register(user);
        } finally {
            this.writeUnlockUsers();
        }
    }

    /**
     * Read and write methods got in the examples
     */
    protected void readLockUsers() {
        System.out.println("Trying to acquire users read lock.");
        this.usersLock.readLock().lock();
        System.out.println("Users read lock acquired.");
    }

    protected void readLockMessages() {
        System.out.println("Trying to acquire messages read lock.");
        this.messagesLock.readLock().lock();
        System.out.println("Messages read lock acquired.");
    }

    protected void readUnlockUsers() {
        System.out.println("Releasing users read lock.");
        this.usersLock.readLock().unlock();
    }

    protected void readUnlockMessages() {
        System.out.println("Releasing messages read lock.");
        this.messagesLock.readLock().unlock();
    }

    protected void writeLockUsers() {
        System.out.println("Trying to acquire users write lock.");
        this.usersLock.writeLock().lock();
        System.out.println("Users write lock acquired.");
    }

    protected void writeLockMessages() {
        System.out.println("Trying to acquire messages write lock.");
        this.messagesLock.writeLock().lock();
        System.out.println("Messages write lock acquired.");
    }

    protected void writeUnlockUsers() {
        System.out.println("Releasing users write lock.");
        this.usersLock.writeLock().unlock();
    }

    protected void writeUnlockMessages() {
        System.out.println("Releasing messages write lock.");
        this.messagesLock.writeLock().unlock();
    }
}
