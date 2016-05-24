package nl.han.asd.project.client.commonclient.connection;

import com.google.inject.Inject;
import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessage;
import nl.han.asd.project.client.commonclient.cryptography.CryptographyService;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.lang.reflect.Field;
import java.rmi.activation.UnknownObjectException;
import java.util.List;

/**
 * Wrapper that wraps EncryptedWrapper.
 *
 * @author Jevgeni Geurtsen
 */
class Packer {
    private IEncryptionService encryptionService = null;

    /**
     * Initializes the class
     *
     * @param encryptionService An instance that implements IEncryptionService.
     */
    public Packer(final IEncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    /**
     * Returns the public key used by the CryptographyService.
     *
     * @return Returns the public key used by the CryptographyService.
     */
    public byte[] getMyPublicKey() {
        return encryptionService.getPublicKey();
    }

    /**
     * Packs a builder inside an EncryptedWrapper message.
     *
     * @param originalBuilder Any Builder from the protocol buffer class.
     * @param receiverPublicKey The public key that should be included inside the EncryptedWrapper message.
     * @return The byte array that represents the EncryptedWrapper.
     */
    public HanRoutingProtocol.Wrapper pack(final GeneratedMessage.Builder originalBuilder,
            final byte[] receiverPublicKey) {
        HanRoutingProtocol.Wrapper.Builder builder = HanRoutingProtocol.Wrapper.newBuilder();

        HanRoutingProtocol.Wrapper.Type type = protocolMessageDescriptorToWrapperType(
                originalBuilder.getDescriptorForType());
        builder.setType(type);

        byte[] buffer = originalBuilder.build().toByteArray();
        buffer = encryptionService.encryptData(receiverPublicKey, buffer);
        builder.setData(ByteString.copyFrom(buffer));

        return builder.build();
    }

    /**
     * Unpacks a Wrapper instance that was read from the input stream.
     *
     * @param wrapper Wrapper that needs to be unpacked.
     * @return The unpacked version of the encrypted wrapper.
     */
    public UnpackedMessage unpack(final HanRoutingProtocol.Wrapper wrapper) {
        byte[] buffer = encryptionService.decryptData(wrapper.getData().toByteArray());
        HanRoutingProtocol.Wrapper.Type type = wrapper.getType();
        GeneratedMessage message = wrapperTypeToProtocolMessage(type);

        return new UnpackedMessage(buffer, type, message);
    }

    /**
     * Converts an EncryptedWrapper.Type to its matching protocol buffer class.
     *
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

        throw new PackerException(String.format("Cannot find GeneratedMessage for %s.", type.name()));
    }

    /**
     * Converts a descriptor taken from the Builder class to convert it to a valid EncryptedWrapper.Type.
     *
     * @param classDescriptor The descriptor type of a builder.
     * @return The EncryptedWrapper.Type that is equivalent to the descriptor type.
     */
    private HanRoutingProtocol.Wrapper.Type protocolMessageDescriptorToWrapperType(
            final Descriptors.Descriptor classDescriptor) {
        String name = classDescriptor.getFullName();
        String capitalizedCleanName = name.toUpperCase();

        HanRoutingProtocol.Wrapper.Type type;
        try {
            type = HanRoutingProtocol.Wrapper.Type.valueOf(capitalizedCleanName);
        } catch (IllegalArgumentException e) {
            throw new PackerException(String.format("Cannot find Type for %s",
                    classDescriptor.getName()));
        }

        return type;
    }
}
