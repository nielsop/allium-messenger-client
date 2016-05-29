package nl.han.asd.project.client.commonclient.connection;

import java.io.IOException;
import java.net.Socket;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;

import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.commonservices.internal.utility.Check;
import nl.han.asd.project.protocol.HanRoutingProtocol.Wrapper;
import nl.han.asd.project.protocol.HanRoutingProtocol.Wrapper.Builder;

public class SocketHandler implements AutoCloseable {

    private String host;
    private int port;

    private IEncryptionService encryptionService;

    private Socket socket;

    public SocketHandler(String host, int port) {
        this.host = Check.notNull(host, "host");
        this.port = port;
    }

    public SocketHandler(String host, int port, IEncryptionService encryptionService) {
        this(host, port);

        this.encryptionService = encryptionService;
    }

    public void write(Wrapper wrapper) throws IOException {
        Check.notNull(wrapper, "wrapper");

        if (socket == null) {
            socket = new Socket(host, port);
        }

        wrapper.writeDelimitedTo(socket.getOutputStream());
    }

    public GeneratedMessage writeAndRead(Wrapper wrapper) throws IOException {
        write(wrapper);

        Wrapper responseWrapper = Wrapper.parseDelimitedFrom(socket.getInputStream());

        if (encryptionService == null) {
            return Parser.parseFrom(responseWrapper);
        } else {
            byte[] decryptedData = encryptionService.decryptData(responseWrapper.getData().toByteArray());

            Builder wrapperBuilder = Wrapper.newBuilder();
            wrapperBuilder.setType(responseWrapper.getType());
            wrapperBuilder.setData(ByteString.copyFrom(decryptedData));

            return Parser.parseFrom(wrapperBuilder.build());
        }
    }

    @Override
    public void close() throws IOException {
        socket.close();
        socket = null;
    }

}
