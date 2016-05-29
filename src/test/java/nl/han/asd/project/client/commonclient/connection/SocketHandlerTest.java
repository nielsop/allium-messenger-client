//package nl.han.asd.project.client.commonclient.connection;
//
//import static org.mockito.Matchers.eq;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.powermock.api.mockito.PowerMockito.mock;
//import static org.powermock.api.mockito.PowerMockito.mockStatic;
//import static org.powermock.api.mockito.PowerMockito.verifyStatic;
//import static org.powermock.api.mockito.PowerMockito.whenNew;
//
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.Socket;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//
//import com.google.protobuf.GeneratedMessage;
//
//import nl.han.asd.project.protocol.HanRoutingProtocol;
//import nl.han.asd.project.protocol.HanRoutingProtocol.Wrapper;
//
//@RunWith(PowerMockRunner.class)
//@PrepareForTest({ SocketHandler.class, Socket.class, Wrapper.class })
//public class SocketHandlerTest {
//
//    String host = "localhost";
//    int port = 1337;
//
//    @Test(expected = IllegalArgumentException.class)
//    public void constructorNullHost() throws Exception {
//        new SocketHandler(null, 1024);
//    }
//
//    @Test
//    public void constructorNullEncryptionService() throws Exception {
//        new SocketHandler("localhost", 1024, null);
//    }
//
//    @Test
//    public void testWrite() throws Exception {
//        Socket socketMock = mock(Socket.class);
//        whenNew(Socket.class).withParameterTypes(String.class, int.class).withArguments(eq(host), eq(port))
//        .thenReturn(socketMock);
//
//        GeneratedMessage generatedMessageMock = mock(GeneratedMessage.class);
//        OutputStream outputStreamMock = mock(OutputStream.class);
//
//        when(socketMock.getOutputStream()).thenReturn(outputStreamMock);
//
//        new SocketHandler(host, port).write(generatedMessageMock);
//        verify(generatedMessageMock).writeDelimitedTo(eq(outputStreamMock));
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testWriteNullWrapper() throws Exception {
//        new SocketHandler(host, port).write(null);
//    }
//
//    @Test
//    public void testWriteExistingSocketOnRepeatingWrite() throws Exception {
//        Socket socketMock = mock(Socket.class);
//        whenNew(Socket.class).withParameterTypes(String.class, int.class).withArguments(eq(host), eq(port))
//        .thenReturn(socketMock).thenReturn(mock(Socket.class));
//
//        Wrapper wrapperMock1 = mock(Wrapper.class);
//        Wrapper wrapperMock2 = mock(Wrapper.class);
//        OutputStream outputStreamMock = mock(OutputStream.class);
//
//        when(socketMock.getOutputStream()).thenReturn(outputStreamMock);
//
//        SocketHandler socketHandler = new SocketHandler(host, port);
//        socketHandler.write(wrapperMock1);
//        verify(wrapperMock1).writeDelimitedTo(eq(outputStreamMock));
//        socketHandler.write(wrapperMock2);
//        verify(wrapperMock2).writeDelimitedTo(eq(outputStreamMock));
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testWriteAndReadNullWrapper() throws Exception {
//        new SocketHandler("localhost", 1337).writeAndRead(null);
//    }
//
//    @Test
//    public void testWriteAndReadVerifyWrapperCall() throws Exception {
//        //Variables
//        SocketHandler socketHandler = new SocketHandler(host, port);
//        Wrapper wrapperMock = mock(Wrapper.class);
//        InputStream inputStreamMock = mock(InputStream.class);
//        Socket socketMock = mock(Socket.class);
//
//        //Mock Behavior
//        mockStatic(HanRoutingProtocol.Wrapper.class);
//        whenNew(Socket.class).withAnyArguments().thenReturn(socketMock);
//        when(socketMock.getInputStream()).thenReturn(inputStreamMock);
//
//        //Execute
//        socketHandler.writeAndRead(wrapperMock);
//
//        verifyStatic();
//        HanRoutingProtocol.Wrapper.parseDelimitedFrom(eq(inputStreamMock));
//    }
//
//    @Test
//    public void testCloseSocket() throws Exception {
//        Socket socketMock = mock(Socket.class);
//        whenNew(Socket.class).withParameterTypes(String.class, int.class).withArguments(eq(host), eq(port))
//        .thenReturn(socketMock);
//
//        Wrapper wrapperMock = mock(Wrapper.class);
//        OutputStream outputStreamMock = mock(OutputStream.class);
//
//        when(socketMock.getOutputStream()).thenReturn(outputStreamMock);
//
//        SocketHandler sh = new SocketHandler(host, port);
//        sh.write(wrapperMock);
//        verify(wrapperMock).writeDelimitedTo(eq(outputStreamMock));
//
//        Wrapper wrapper = mock(Wrapper.class);
//        System.err.println(wrapper);
//
//        sh.close();
//        verify(socketMock).close();
//    }
//
//}
