package nl.han.asd.project.client.commonclient.connection;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by BILLPOORTS on 22-4-2016.
 */
public class PackerTest {

    private final byte[] EMPTY_PUBLICKEY_BYTES = new byte[] { 0x00 };
    private Packer packer = null;

    @Before
    public void initPacker() {
        final Injector injector = Guice.createInjector(new EncryptionModule());
        packer = new Packer(injector.getInstance(IEncryptionService.class));
    }

    @Test
    public void testPacking() throws InvalidProtocolBufferException {
        HanRoutingProtocol.Wrapper packedData = pack();
        UnpackedMessage unpackedData = unpack(packedData);
        HanRoutingProtocol.ClientLoginRequest wrapper = HanRoutingProtocol.ClientLoginRequest
                .parseFrom(unpackedData.getData());
    }

    @Test
    public void testUnpacking() throws InvalidProtocolBufferException {
        HanRoutingProtocol.Wrapper packedData = pack();
        UnpackedMessage unpackedMessage = unpack(packedData);

        HanRoutingProtocol.ClientLoginRequest.parseFrom(unpackedMessage.getData());

        assertEquals(unpackedMessage.getDataType(), HanRoutingProtocol.Wrapper.Type.CLIENTLOGINREQUEST);
        assertEquals(unpackedMessage.getDataMessage().getClass(), HanRoutingProtocol.ClientLoginRequest.class);

        HanRoutingProtocol.ClientLoginRequest clr = (HanRoutingProtocol.ClientLoginRequest) unpackedMessage
                .getDataMessage().getParserForType().parseFrom(unpackedMessage.getData());
    }

    HanRoutingProtocol.Wrapper pack() {
        // Simulate a builder..
        HanRoutingProtocol.ClientLoginRequest.Builder builder = HanRoutingProtocol.ClientLoginRequest.newBuilder();
        builder.setPublicKey(ByteString.copyFrom(packer.getMyPublicKey()));
        builder.setUsername("test");
        builder.setPassword("test");

        // Pack..
        HanRoutingProtocol.Wrapper packed = packer.pack(builder, EMPTY_PUBLICKEY_BYTES);

        return packed;
    }

    UnpackedMessage unpack(HanRoutingProtocol.Wrapper packed) {
        UnpackedMessage unpacked = packer.unpack(packed);

        return unpacked;
    }

}
