package nl.han.asd.project.client.commonclient.connection;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by BILLPOORTS on 22-4-2016.
 */
public class PackerTest {

    private final Packer packer = new Packer();

    @Test
    public void TestPacking() throws InvalidProtocolBufferException {
        byte[] packedData = pack();
        HanRoutingProtocol.EncryptedWrapper wrapper = HanRoutingProtocol.EncryptedWrapper.parseFrom(packedData);
    }

    public byte[] pack() {
        // Simulate a builder..
        HanRoutingProtocol.ClientLoginRequest.Builder builder = HanRoutingProtocol.ClientLoginRequest.newBuilder();
        builder.setPublicKey("test");
        builder.setUsername("test");
        builder.setPassword("test");

        // Pack..
        byte[] packed = packer.pack(builder, "publicKeyTest");

        return packed;
    }

    public ParsedMessage unpack(byte[] packed)
    {
        ParsedMessage unpacked = packer.unpack(packed);

        return unpacked;
    }


    @Test
    public void TestUnpacking() throws InvalidProtocolBufferException {
        byte[] packedData = pack();
        ParsedMessage unpackedMessage = unpack(packedData);

        HanRoutingProtocol.ClientLoginRequest.parseFrom(unpackedMessage.getData());

        assertEquals(unpackedMessage.getDataType(), HanRoutingProtocol.EncryptedWrapper.Type.CLIENTLOGINREQUEST);
        assertEquals(unpackedMessage.getDataMessage().getClass(), HanRoutingProtocol.ClientLoginRequest.class);

        HanRoutingProtocol.ClientLoginRequest clr = (HanRoutingProtocol.ClientLoginRequest)unpackedMessage.getDataMessage().getParserForType().parseFrom(unpackedMessage.getData());
    }

}
