package Programming3.chatsys.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.regex.Pattern;
/**
 * @author Chester Meng
 * 2020.11.3
 * Java 1.8
 * @return
 */
public class User extends TextDatabaseItem {
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");
    private String userName;
    private String fullName;
    private String password;
    private int lastReadId;
    //The username could only contain letters, numbers and _
    public static boolean userNameIsValid(String userName) {
        return USERNAME_PATTERN.matcher(userName).find();}
    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", password='" + password + '\'' +
                ", lastReadId=" + lastReadId +
                '}';
    }
    //give value to the object
    public  void init(String userName, String fullName, String password, int lastReadId){
        if (!userNameIsValid(userName)) {
            throw new IllegalArgumentException("Invalid userName");
        }
        this.userName = userName;
        this.fullName = fullName;
        this.password = password;
        this.lastReadId = lastReadId;
    }

    /**
     * Constructor of the user
     * @param userName
     * @param fullName
     * @param password
     */
    public User(String userName, String fullName, String password) {
        this.init(userName, fullName, password, 0);}
    public User() {
        this.init("NULL","NULL", "NULL", 0);}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return lastReadId == user.lastReadId &&
                userName.equals(user.userName) &&
                fullName.equals(user.fullName) &&
                password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, fullName, password, lastReadId);
    }

    public String getUserName() {
        return userName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }

    public int getLastReadId() {
        return lastReadId;
    }

    public void setLastReadId(int lastReadId) {
        this.lastReadId = lastReadId;
    }

    public String format(){
        return this.userName + "\t" + this.fullName + "\t" + this.password+"\t"+this.lastReadId;
    }

    /**
     * parse a formatted string
     * @param formatted
     */
    public void parse(String formatted) {
        String data[] = formatted.split("\t");
        if (data.length < 4){
            throw new IllegalArgumentException("Too short for parse");
        }else{
            this.init(data[0],data[1],data[2],Integer.parseInt(data[3]));
        }
    }
    public void save(String filename) {
        this.save(new File(filename));
    }

}
