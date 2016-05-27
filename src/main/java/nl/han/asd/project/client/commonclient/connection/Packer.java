package nl.han.asd.project.client.commonclient.connection;

import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessage;
import nl.han.asd.project.client.commonclient.cryptography.EncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.lang.reflect.Field;
import java.rmi.activation.UnknownObjectException;
import java.util.List;

/**
 * Wrapper that wraps EncryptedWrapper.
 */
public class Packer {
    private EncryptionService encryptionService = null;

    public Packer(final EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    public byte[] getMyPublicKey() {
        return encryptionService.getPublicKey();
    }

    /**
     * Packs a builder inside an EncryptedWrapper message.
     * @param originalBuilder Any Builder from the protocol buffer class.
     * @return The byte array that represents the EncryptedWrapper.
     */
    public HanRoutingProtocol.Wrapper pack(final GeneratedMessage.Builder originalBuilder) {
        HanRoutingProtocol.Wrapper.Builder builder = HanRoutingProtocol.Wrapper.newBuilder();

        HanRoutingProtocol.Wrapper.Type type = protocolMessageDescriptorToWrapperType(
                originalBuilder.getDescriptorForType());
        builder.setType(type);

        byte[] buffer = originalBuilder.build().toByteArray();
        builder.setData(ByteString.copyFrom(buffer));

        return builder.build();
    }

    /**
     * Unpacks a byte array that was read from the input stream.
     * @param packed EncryptedWrapper that needs to be unpacked.
     * @return The unpacked version of the encrypted wrapper.
     */
    public UnpackedMessage unpack(final HanRoutingProtocol.Wrapper packed) {
        byte[] buffer = packed.getData().toByteArray();
        HanRoutingProtocol.Wrapper.Type type = packed.getType();
        GeneratedMessage message = wrapperTypeToProtocolMessage(type);
        return new UnpackedMessage(buffer, type, message);
    }

    /**
     * Converts an EncryptedWrapper.Type to its matching protocol buffer class.
     * @param type The EncryptedWrapper.Type that needs to be converted.
     * @param <T> Any class, derived from GeneratedMessage.
     * @return An class that extends from GeneratedMessage which can be used to decode the data inside an EncryptedWrapper.
     * @throws UnknownObjectException
     */
    private <T extends GeneratedMessage> T wrapperTypeToProtocolMessage(final HanRoutingProtocol.Wrapper.Type type) {
        String name = type.name();

        List<Descriptors.Descriptor> descriptorList = HanRoutingProtocol.getDescriptor().getMessageTypes();
        for (Descriptors.Descriptor descriptor : descriptorList) {
            if (descriptor.getName().equalsIgnoreCase(name)) {
                try {
                    //nl.han.asd.project.protocol.HanRoutingProtocol$ClientLoginRequest
                    String internalName = String
                            .format("%s$%s", HanRoutingProtocol.class.getCanonicalName(), descriptor.getName());
                    Field defaultInstanceField = Class.forName(internalName).getDeclaredField("DEFAULT_INSTANCE");
                    defaultInstanceField.setAccessible(true);
                    Object defaultInstanceValue = defaultInstanceField.get(null);
                    return (T) defaultInstanceValue;
                } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
                    throw new PackerException(e);
                }
            }
        }
        return null;
    }

    /**
     * Converts a descriptor taken from the Builder class to convert it to a valid EncryptedWrapper.Type.
     * @param classDescriptor The descriptor type of a builder.
     * @return The EncryptedWrapper.Type that is equivalent to the descriptor type.
     */
    private HanRoutingProtocol.Wrapper.Type protocolMessageDescriptorToWrapperType(
            final Descriptors.Descriptor classDescriptor) {
        String name = classDescriptor.getFullName();
        String capitalizedCleanName = name.toUpperCase();
        return HanRoutingProtocol.Wrapper.Type.valueOf(capitalizedCleanName);
    }
}
