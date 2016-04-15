package nl.han.asd.project.client.commonclient.utility;

import com.google.protobuf.GeneratedMessage;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 15-4-2016
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
