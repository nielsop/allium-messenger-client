package nl.han.asd.project.client.commonclient.connection;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.protobuf.GeneratedMessage;

import nl.han.asd.project.protocol.HanRoutingProtocol;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ SocketHandler.class, Socket.class, HanRoutingProtocol.Wrapper.class })
public class SocketHandlerTest {

    String host = "localhost";
    int port = 1337;

    @Test
    public void testWrite() throws Exception {

        Socket socketMock = mock(Socket.class);
        whenNew(Socket.class).withParameterTypes(String.class, int.class).withArguments(eq(host), eq(port))
        .thenReturn(socketMock);

        GeneratedMessage generatedMessageMock = mock(GeneratedMessage.class);
        OutputStream outputStreamMock = mock(OutputStream.class);

        when(socketMock.getOutputStream()).thenReturn(outputStreamMock);

        new SocketHandler(host, port).write(generatedMessageMock);
        verify(generatedMessageMock).writeDelimitedTo(eq(outputStreamMock));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteNullWrapper() throws Exception {
        new SocketHandler(host, port).write(null);
    }

    @Test
    public void testWriteExistingSocketOnRepeatingWrite() throws Exception {
        Socket socketMock = mock(Socket.class);
        whenNew(Socket.class).withParameterTypes(String.class, int.class).withArguments(eq(host), eq(port))
        .thenReturn(socketMock).thenReturn(mock(Socket.class));

        GeneratedMessage generatedMessageMock = mock(GeneratedMessage.class);
        GeneratedMessage generatedMessageMock2 = mock(GeneratedMessage.class);
        OutputStream outputStreamMock = mock(OutputStream.class);

        when(socketMock.getOutputStream()).thenReturn(outputStreamMock);

        SocketHandler socketHandler = new SocketHandler(host, port);
        socketHandler.write(generatedMessageMock);
        verify(generatedMessageMock).writeDelimitedTo(eq(outputStreamMock));
        socketHandler.write(generatedMessageMock2);
        verify(generatedMessageMock2).writeDelimitedTo(eq(outputStreamMock));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteAndReadNullWrapper() throws Exception {
        new SocketHandler("localhost", 1337).writeAndRead(null);
    }

    @Test
    public void testWriteAndReadVerifyWrapperCall() throws Exception {
        //Variables
        SocketHandler socketHandler = new SocketHandler(host, port);
        GeneratedMessage generatedMessageMock = mock(GeneratedMessage.class);
        InputStream inputStreamMock = mock(InputStream.class);
        Socket socketMock = mock(Socket.class);

        //Mock Behavior
        mockStatic(HanRoutingProtocol.Wrapper.class);
        whenNew(Socket.class).withAnyArguments().thenReturn(socketMock);
        when(socketMock.getInputStream()).thenReturn(inputStreamMock);

        //Execute
        socketHandler.writeAndRead(generatedMessageMock);

        verifyStatic();
        HanRoutingProtocol.Wrapper.parseDelimitedFrom(eq(inputStreamMock));
    }
}
