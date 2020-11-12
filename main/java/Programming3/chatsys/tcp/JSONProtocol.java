package Programming3.chatsys.tcp;

import org.json.JSONTokener;
import org.omg.CORBA.portable.InputStream;

import java.io.*;
import java.net.Socket;

public class JSONProtocol {
    private BufferedReader reader;
    private BufferedWriter writer;
    private JSONTokener tokener;
    public JSONProtocol(InputStream input, OutputStream output) throws UnsupportedEncodingException {
        this.init(input, output);
    }

    public JSONProtocol(Socket socket) throws IOException {
        this.init((InputStream) socket.getInputStream(), socket.getOutputStream());
    }

    private void init(InputStream input, OutputStream output) throws UnsupportedEncodingException {
        this.reader = new BufferedReader(
                new InputStreamReader(input, "UTF-8")
        );
        this.writer = new BufferedWriter(
                new OutputStreamWriter(output, "UTF-8")
        );
        this.tokener = new JSONTokener(this.reader);
    }

}
