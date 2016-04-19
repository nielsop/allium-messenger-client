package nl.han.asd.client.commonclient.utility;

import com.google.protobuf.GeneratedMessage;
import nl.han.asd.project.protocol.HanRoutingProtocol;
//import nl.han.onionmessenger.commonclient.HanRoutingProtocol;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 15-4-2016
 */
public class ResponseWrapper {

    private ResponseType responseType;
    public Socket socket;

    public ResponseWrapper(final ResponseType responseType, final Socket socket) {
        this.responseType = responseType;
        this.socket = socket;
    }

    public GeneratedMessage read() throws IOException {
        InputStream is = null;
        try {
            is = socket.getInputStream();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        if (is != null) {
            switch (responseType) {
                case CLIENT_LOGIN:
                    return HanRoutingProtocol.ClientLoginResponse.parseDelimitedFrom(is);
                case CLIENT_REGISTRATION:
                    return HanRoutingProtocol.ClientRegisterResponse.parseDelimitedFrom(is);
            }
        }
        return null;
    }

    public enum ResponseType {
        CLIENT_LOGIN,
        CLIENT_REGISTRATION
    }
}
