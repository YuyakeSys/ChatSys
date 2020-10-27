package Programming3.chatsys.cli;

import java.io.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;


public class ReadMessages {
    public static void main(String[] args) throws IOException, FileNotFoundException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the number of recent messages:");
        long lines = Long.parseLong(scan.next());
        if (lines == 0) {
            try {
                Scanner sc = new Scanner(new File("messages_test.txt"));
                //按行读取test.txt文件内容
                while (sc.hasNextLine()) {
                    System.out.println(sc.nextLine());

                }
            } catch (FileNotFoundException e) {

            }
        } else {
            readLastNLine(new File("messages_test.txt"), lines);
        }
    }

    public static List<String> readLastNLine(File file, long numRead) {
        List<String> result = new ArrayList<String>();
        long count = 0;
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            return null;
        }

        RandomAccessFile fileRead = null;
        try {
            fileRead = new RandomAccessFile(file, "r");
            //length
            long length = fileRead.length();
            if (length == 0L) {
                return result;
            } else {
                //init pos
                long pos = length - 1;
                while (pos > 0) {
                    pos--;
                    fileRead.seek(pos);
                    //\n means read the last line
                    if (fileRead.readByte() == '\n') {
                        //get the temporary line
                        String line = fileRead.readLine();
                        //save
                        result.add(line);
                        System.out.println(line);

                        count++;
                        if (count == numRead) {
                            break;
                        }
                    }
                }
                if (pos == 0) {
                    fileRead.seek(0);
                    result.add(fileRead.readLine());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileRead != null) {
                try {
                    fileRead.close();
                } catch (Exception e) {
                }
            }
        }

        return result;

    }

}

