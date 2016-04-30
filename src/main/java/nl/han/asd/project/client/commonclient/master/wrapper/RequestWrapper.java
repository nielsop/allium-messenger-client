package nl.han.asd.project.client.commonclient.master.wrapper;

import com.google.protobuf.GeneratedMessage;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by niels on 4/30/2016.
 */
public class RequestWrapper {

    private GeneratedMessage message;
    private Socket socket;

    public RequestWrapper(final GeneratedMessage message, final Socket socket) {
        this.message = message;
        this.socket = socket;
    }

    /**
     * Writes delimited to socket
     */
    public void writeToSocket() {
        try {
            message.writeDelimitedTo(socket.getOutputStream());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

