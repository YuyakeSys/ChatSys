package Programming3.chatsys.data;
import java.io.*;
import java.sql.Timestamp;
import java.util.Objects;
// The set messages method are learnt from the cod examples, which is the same in the User class.

public class ChatMessage extends TextDatabaseItem{
    private int id;
    private String userName;
    private Timestamp timestamp;
    private String message;
    User user1 = new User();

    public ChatMessage(int id, String userName, Timestamp timestamp, String message) {
        this.init(id, userName, timestamp, message);
    }

    public ChatMessage(int id, String userName, long timestamp, String message) {
        this.init(id, userName, new Timestamp(timestamp), message);
    }
    public ChatMessage(int id, String userName, String message) {
        super();
        this.init(id, userName, System.currentTimeMillis(), message);
    }
    public ChatMessage(){
        this.id=-1;
        this.userName="null";
        this.message="null";
        this.timestamp=null;
    }

    private void init(int id, String userName, Timestamp timestamp, String message) {
        if (message.indexOf('\n') >= 0) {
            throw new IllegalArgumentException("message contains a line feed");
        }
        if (!user1.userNameIsValid(userName)) {
            throw new IllegalArgumentException("Invalid userName");
        }
        this.id = id;
        this.userName = userName;
        this.timestamp = timestamp;
        this.message = message;
    }

    private void init(int id, String userName, long timestamp, String message) {
        this.init(id, userName, new Timestamp(timestamp), message);
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof ChatMessage)) return false;
        ChatMessage that = (ChatMessage) o;
        return id == that.id &&
                userName.equals(that.userName) &&
                timestamp.equals(that.timestamp) &&
                message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, timestamp, message);
    }


    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", author_name='" + userName + '\'' +
                ", time=" + timestamp +
                ", message='" + message + '\'' +
                '}';
    }
    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String format(){
        return this.id + "\t" + this.userName + "\t" +this.timestamp.getTime()+"\t"+this.message;
    }

    public void parse(String formatted){
        String data[] = formatted.split("\t",4);
        if (data.length < 4){
            throw new IllegalArgumentException("This string is too short");
        }else {
            this.init(Integer.parseInt(data[0]), data[1], Long.parseLong(data[2]), data[3]);
            //offer values
        }
    }//return the value to each var
    public void save(String filename) {
        this.save(new File(filename));
    }
    public void save(File file) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file, true))) {
            out.write(this.format() + "\n");
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(file + " cannot be opened", e);
        }
    }
}
