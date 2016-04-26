package nl.han.asd.project.client.commonclient.connection;

import com.google.protobuf.GeneratedMessage;
import nl.han.asd.project.protocol.HanRoutingProtocol;

/**
 * Created by Jevgeni on 23-4-2016.
 */
public class ParsedMessage {
    private HanRoutingProtocol.EncryptedWrapper.Type dataType;
    private GeneratedMessage dataMessage;
    private byte[] data;

    public ParsedMessage(byte[] data, HanRoutingProtocol.EncryptedWrapper.Type type, GeneratedMessage message) {
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
    public HanRoutingProtocol.EncryptedWrapper.Type getDataType() {
        return dataType;
    }
}
