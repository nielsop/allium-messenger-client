package nl.han.asd.project.client.commonclient.connection;

import com.google.protobuf.GeneratedMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.OutputStream;
import java.net.Socket;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Bram Heijmink on 25-5-2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SocketHandler.class, Socket.class})
public class SocketHandlerTest {

    @Before public void setUp() throws Exception {

    }

    @After public void tearDown() throws Exception {

    }

    @Test public void testWrite() throws Exception {
        String host = "localhost";
        int port = 1337;

        Socket socketMock = mock(Socket.class);
        whenNew(Socket.class).withParameterTypes(String.class,int.class).withArguments(eq(host),eq(port)).thenReturn(socketMock);

        GeneratedMessage generatedMessageMock = mock(GeneratedMessage.class);
        OutputStream outputStreamMock = mock(OutputStream.class);

        when(socketMock.getOutputStream()).thenReturn(outputStreamMock);

        new SocketHandler(host,port).write(generatedMessageMock);
        verify(generatedMessageMock).writeDelimitedTo(eq(outputStreamMock));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteNullWrapper() throws Exception {
        new SocketHandler("localhost",1337).write(null);
    }

    @Test
    public void testWriteExistingSocketOnRepeatingWrite() throws Exception{
        String host = "localhost";
        int port = 1337;

        Socket socketMock = mock(Socket.class);
        whenNew(Socket.class).withParameterTypes(String.class,int.class).withArguments(eq(host),eq(port)).thenReturn(socketMock).thenReturn(mock(Socket.class));

        GeneratedMessage generatedMessageMock = mock(GeneratedMessage.class);
        GeneratedMessage generatedMessageMock2 = mock(GeneratedMessage.class);
        OutputStream outputStreamMock = mock(OutputStream.class);

        when(socketMock.getOutputStream()).thenReturn(outputStreamMock);

        SocketHandler socketHandler = new SocketHandler(host,port);
        socketHandler.write(generatedMessageMock);
        verify(generatedMessageMock).writeDelimitedTo(eq(outputStreamMock));
        socketHandler.write(generatedMessageMock2);
        verify(generatedMessageMock2).writeDelimitedTo(eq(outputStreamMock));
    }

    @Test public void testWriteAndRead() throws Exception {

    }
}
