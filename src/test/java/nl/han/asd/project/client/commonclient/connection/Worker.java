package nl.han.asd.project.client.commonclient.connection;

import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * Created by Jevgeni on 26-4-2016.
 */
class Worker implements Runnable {
    private Socket clientSocket = null;
    private Packer packer = null;
    private byte[] publicKey = null;

    public Worker(Socket clientSocket, Packer packer, byte[] publicKey) {
        this.clientSocket = clientSocket;
        this.packer = packer;
        this.publicKey = publicKey;
    }

    @Override public void run() {
        InputStream input = null;
        OutputStream output = null;

        try {
            input = clientSocket.getInputStream();
            output = clientSocket.getOutputStream();

            try {
                HanRoutingProtocol.Wrapper wrapper = HanRoutingProtocol.Wrapper
                        .parseDelimitedFrom(input);
                if (wrapper != null) {
                    try {
                        UnpackedMessage message = packer.unpack(wrapper);

                        HanRoutingProtocol.ClientLoginRequest request = HanRoutingProtocol.ClientLoginRequest
                                .parseFrom(message.getData());
                        HanRoutingProtocol.ClientLoginResponse.Builder builder = HanRoutingProtocol.ClientLoginResponse
                                .newBuilder();
                        builder.setSecretHash(String.format("%s:%s", request.getUsername(),
                                request.getPassword()));
                        builder.setStatus(
                                HanRoutingProtocol.ClientLoginResponse.Status
                                        .valueOf(
                                                HanRoutingProtocol.ClientLogoutResponse.Status.SUCCES_VALUE));

                        System.out.println(
                                "Request processed as 'protocol' request.");
                        wrapper = packer.pack(builder, this.publicKey);
                    } catch (InvalidProtocolBufferException e) {
                        System.out.println(
                                "Request processed as 'normal' request.");
                    }

                    wrapper.writeDelimitedTo(output);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            output.close();
            input.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
