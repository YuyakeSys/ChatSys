package Programming3.chatsys.data;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Objects;

public class User {
    private String userName;
    private String fullName;
    private String password;
    private int lastReadId;
    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", password='" + password + '\'' +
                ", lastReadId=" + lastReadId +
                '}';
    }

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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLastReadId() {
        return lastReadId;
    }

    public void setLastReadId(int lastReadId) {
        this.lastReadId = lastReadId;
    }

    public String format(){
        return userName + "\t" + fullName + "\t" +password+"\t"+lastReadId+"\r\n";
    }
    public void parse(String formatted){
        String data[] = formatted.split("\t");
        this.userName = data[0];
        this.fullName = data[1];
        this.password = data[2];
        this.lastReadId = Integer.parseInt(data[3]);
    }
}
