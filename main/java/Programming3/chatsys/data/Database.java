package Programming3.chatsys.data;
import java.io.File;
import java.util.*;
import java.io.*;

public class Database {
    int Max_Id=0;
    public String file = "messages_test.txt";
    List<ChatMessage> arrayList = new ArrayList<ChatMessage>();
    ChatMessage cm = new ChatMessage();
    public List readMessages() throws FileNotFoundException,IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String str = null;
        while ((str = in.readLine()) != null) {
            cm.parse(str);
            arrayList.add(cm);
        }
        in.close();
        return arrayList;
    }
    public void addMessage(ChatMessage message) throws Exception {
        if (message.getMessage().indexOf('\n') >= 0) {
            throw new IllegalArgumentException("message contains a line feed");
        }
        List<ChatMessage> check = this.readMessages();
        for(int i=0;i<check.size()-1;i++) {
            if(check.get(i).getId()>=Max_Id){
                Max_Id = check.get(i).getId();
            }
        }
        if(message.getId()>Max_Id){
            message.save(file);
        }else {
            throw new Exception("Error Id");
        }
        /*
        cm = new ChatMessage();
        Scanner sc=new Scanner(new FileReader(file));
        String st;
        String line=null;
        while((sc.hasNextLine()&&(line=sc.nextLine())!=null)){
            if(!sc.hasNextLine())
                cm.parse(line);
        }
        if(cm.getId()>=message.getId ()){
            throw new Exception("Id error");
        }
        else{
            message.save(file);
        }*/

        }
    }
