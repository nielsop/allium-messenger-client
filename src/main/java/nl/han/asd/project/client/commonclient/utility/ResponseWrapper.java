package nl.han.asd.project.client.commonclient.utility;

import com.google.protobuf.GeneratedMessage;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

//import nl.han.onionmessenger.commonclient.HanRoutingProtocol;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 15-4-2016
 */
public class ResponseWrapper {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ResponseWrapper.class);
    public Socket socket;
    private HanRoutingProtocol.EncryptedWrapper.Type responseType;

    public ResponseWrapper(final HanRoutingProtocol.EncryptedWrapper.Type responseType, final Socket socket) {
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
            try {
                switch (responseType) {
                    case CLIENTRESPONSE:
                        break;
                    case CLIENTLOGOUTRESPONSE:
                        break;
                    case CLIENTLOGINRESPONSE:
                        return HanRoutingProtocol.ClientLoginResponse.parseDelimitedFrom(is);
                    case CLIENTREGISTERRESPONSE:
                        return HanRoutingProtocol.ClientRegisterResponse.parseDelimitedFrom(is);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return null;
    }

}
