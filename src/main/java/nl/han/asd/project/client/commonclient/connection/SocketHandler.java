package nl.han.asd.project.client.commonclient.connection;

import com.google.protobuf.GeneratedMessage;
import nl.han.asd.project.commonservices.internal.utility.Check;
import nl.han.asd.project.protocol.HanRoutingProtocol.Wrapper;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Bram Heijmink on 25-5-2016.
 */
public class SocketHandler {

    private String host;
    private int port;

    private Socket socket;

    public SocketHandler(String host, int port){
        this.host = host;
        this.port = port;
    }

    public <T extends GeneratedMessage> void write(T wrapper) throws IOException {
        Check.notNull(wrapper,"wrapper");

        if(socket == null){
            socket = new Socket(host,port);
        }
        wrapper.writeDelimitedTo(socket.getOutputStream());
    }

    public <T extends  GeneratedMessage> GeneratedMessage writeAndRead(T wrapper)
            throws IOException {
        write(wrapper);
        return Wrapper.parseDelimitedFrom(socket.getInputStream());
    }


}
