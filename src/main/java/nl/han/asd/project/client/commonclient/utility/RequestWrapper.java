package nl.han.asd.project.client.commonclient.utility;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.SocketException;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 15-4-2016
 */
public class RequestWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestWrapper.class);
    private GeneratedMessage message;
    private Socket socket;

    /**
     * Creates a new request wrapper. Will automatically transform the request into an encrypted request.
     *
     * @param message The request.
     * @param requestType The request type.
     * @param socket The socket to write to and read from.
     */
    public RequestWrapper(final GeneratedMessage message, final HanRoutingProtocol.Wrapper.Type requestType,
            final Socket socket) {
        this.message = HanRoutingProtocol.Wrapper.newBuilder().setType(requestType).setData(message.toByteString())
                .build();
        this.socket = socket;
    }

    public <T extends GeneratedMessage> T writeAndRead(Class<T> classDescriptor) {
        writeToSocket();
        try {
            HanRoutingProtocol.Wrapper response;
            if ((response = HanRoutingProtocol.Wrapper.parseDelimitedFrom(socket.getInputStream())) != null) {
                return parseFrom(classDescriptor, response.getData().toByteArray());
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    private <T extends GeneratedMessage> T parseFrom(Class<T> classDescriptor, byte[] data)
            throws SocketException, InvalidProtocolBufferException {
        try {
            Field defaultInstanceField = classDescriptor.getDeclaredField("DEFAULT_INSTANCE");
            defaultInstanceField.setAccessible(true);
            T defaultInstance = (T) defaultInstanceField.get(null);
            return (T) defaultInstance.getParserForType().parseFrom(data);
        } catch (IllegalAccessException | IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (NoSuchFieldException e) {
            LOGGER.error(e.getMessage(), e);
            throw new InvalidProtocolBufferException("Invalid class provided.");
        }
        return null;
    }

    private void writeToSocket() {
        try {
            final GeneratedMessage m = message;
            message.writeDelimitedTo(socket.getOutputStream());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
