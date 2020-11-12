package Programming3.chatsys.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
/**
 * @author Chester Meng
 * 2020.11.3
 * Java 1.8
 * @return
 */
public abstract class TextDatabaseItem {

    /**
     * Save the file to the message/user database
     * @param file
     */
    public void save(File file) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file, true))) {
            out.write(this.format() + "\n");
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(file + " cannot be opened", e);
        }
    }

    public abstract String format();


}
