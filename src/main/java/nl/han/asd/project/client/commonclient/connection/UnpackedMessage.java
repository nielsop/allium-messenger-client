package nl.han.asd.project.client.commonclient.connection;

import com.google.protobuf.GeneratedMessage;
import nl.han.asd.project.protocol.HanRoutingProtocol;

/**
 * An class that holds specific data about a message, an instance of this class is returned
 * whenever data is read from the connected socket.
 */
public class UnpackedMessage {
    private final HanRoutingProtocol.Wrapper.Type dataType;
    private final GeneratedMessage dataMessage;
    private final byte[] data;

    public UnpackedMessage(final byte[] data, final HanRoutingProtocol.Wrapper.Type type,
            final GeneratedMessage message) {
        this.data = data;
        this.dataType = type;
        this.dataMessage = message;
    }

    /**
     * Data contains the underlying message.
     * @return Underlying message that was received.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * The type of protocol message that 'getData' returns.
     * @return Type of protocol message.
     */
    public GeneratedMessage getDataMessage() {
        return dataMessage;
    }

    /**
     * The type of the EncryptedWrapper class, this matches the 'getDataMessage' type.
     * @return Type of the EncryptedWrapper.Type matching 'getData'
     */
    public HanRoutingProtocol.Wrapper.Type getDataType() {
        return dataType;
    }

    /**
     * Checks if the data message inside this instance matches @classDescriptor
     * @param classDescriptor ClassDescriptor of the class to check against.
     * @param <T> GeneratedMessage
     * @return True if the classes match, False if they don't
     */
    public <T extends GeneratedMessage> boolean matchDataMessage(final Class<T> classDescriptor) {
        return dataMessage.getClass() == classDescriptor;
    }

    /**
     * Checks if the data type inside this instance matches the @type
     * @param type Type to check against
     * @return True if the types match, False if they dont'.
     */
    public boolean matchDataType(HanRoutingProtocol.Wrapper.Type type) {
        return dataType == type;
    }
}
