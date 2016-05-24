package nl.han.asd.project.client.commonclient.connection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;

import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol.Wrapper;
import nl.han.asd.project.protocol.HanRoutingProtocol.Wrapper.Type;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ConnectionService.class, Parser.class })
public class ConnectionServiceTest {

    @Mock
    private IEncryptionService encryptionServiceMock;

    @Mock
    private File fileMock;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test(expected = IllegalArgumentException.class)
    public void hostPortConstructorNullHost() throws Exception {
        new ConnectionService(null, 1024);
    }

    @Test(expected = IllegalArgumentException.class)
    public void encrytionServiceHostPortPublicKeyConstructorNullEncryptionService() throws Exception {
        new ConnectionService(null, "localhost", 1024, new byte[] { 1, 2, 3 });
    }

    @Test(expected = IllegalArgumentException.class)
    public void encrytionServiceHostPortPublicKeyConstructorNullHost() throws Exception {
        new ConnectionService(encryptionServiceMock, null, 1024, new byte[] { 1, 2, 3 });

    }

    @Test(expected = IllegalArgumentException.class)
    public void encrytionServiceHostPortPublicKeyConstructorNullPublicKey() throws Exception {
        new ConnectionService(encryptionServiceMock, "localhost", 1024, (byte[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void encrytionServiceHostPortKeyFileConstructorNullEncryptionService() throws Exception {
        new ConnectionService(null, "localhost", 1024, fileMock);

    }

    @Test(expected = IllegalArgumentException.class)
    public void encrytionServiceHostPortKeyFileConstructorNullHost() throws Exception {
        new ConnectionService(encryptionServiceMock, null, 1024, fileMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void encrytionServiceHostPortKeyFileConstructorNullKeyfile() throws Exception {
        new ConnectionService(encryptionServiceMock, "localhost", 1024, (File) null);
    }

    @Test
    public void wrapMessageEncryptedKeyBytes() throws Exception {
        byte[] key = "public key".getBytes();
        Type type = Type.CLIENTLOGINREQUEST;

        byte[] messageBytes = "ClientLoginRequest - bytes".getBytes();
        GeneratedMessage message = Mockito.mock(GeneratedMessage.class);
        when(message.toByteArray()).thenReturn(messageBytes);

        byte[] encryptedByted = "encrypted (ClientLoginRequest - bytes)".getBytes();
        when(encryptionServiceMock.encryptData(eq(key), eq(messageBytes))).thenReturn(encryptedByted);

        Wrapper wrapper = new ConnectionService(encryptionServiceMock, "hostname", 1024, key).wrap(message, type);

        Wrapper expectedWrapper = Wrapper.newBuilder().setData(ByteString.copyFrom(encryptedByted)).setType(type)
                .build();

        assertEquals(expectedWrapper, wrapper);
    }

    @Test
    public void wrapMessageEncryptedKeyfile() throws Exception {
        byte[] key = "keyfile contents".getBytes();

        File file = folder.newFile();

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
        dataOutputStream.write(key);
        dataOutputStream.close();

        when(fileMock.length()).thenReturn(10l);

        new ConnectionService(encryptionServiceMock, "localhost", 1024, file);

        Type type = Type.CLIENTLOGINREQUEST;

        byte[] messageBytes = "ClientLoginRequest - bytes".getBytes();
        GeneratedMessage message = Mockito.mock(GeneratedMessage.class);
        when(message.toByteArray()).thenReturn(messageBytes);

        byte[] encryptedByted = "encrypted (ClientLoginRequest - bytes)".getBytes();
        when(encryptionServiceMock.encryptData(eq(key), eq(messageBytes))).thenReturn(encryptedByted);

        Wrapper wrapper = new ConnectionService(encryptionServiceMock, "localhost", 1024, file).wrap(message, type);

        Wrapper expectedWrapper = Wrapper.newBuilder().setData(ByteString.copyFrom(encryptedByted)).setType(type)
                .build();

        assertEquals(expectedWrapper, wrapper);
    }

    @Test
    public void wrapMessageNotEncrypted() throws Exception {
        Type type = Type.CLIENTLOGINREQUEST;

        byte[] messageBytes = "ClientLoginRequest - bytes".getBytes();
        GeneratedMessage message = Mockito.mock(GeneratedMessage.class);
        when(message.toByteString()).thenReturn(ByteString.copyFrom(messageBytes));

        Wrapper wrapper = new ConnectionService("hostname", 1024).wrap(message, type);

        Wrapper expectedWrapper = Wrapper.newBuilder().setData(ByteString.copyFrom(messageBytes)).setType(type).build();

        assertEquals(expectedWrapper, wrapper);
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeTimeoutNullWrapper() throws Exception {
        new ConnectionService("localhost", 1024).write(null, 10, TimeUnit.SECONDS);
    }

    @Test
    public void writeTimoutNewSocket() throws Exception {
        ConnectionService connectionService = new ConnectionService("localhost", 1024);

        Socket socketMock1 = mock(Socket.class);
        OutputStream outputStreamMock1 = mock(OutputStream.class);
        when(socketMock1.getOutputStream()).thenReturn(outputStreamMock1);
        whenNew(Socket.class).withParameterTypes(String.class, int.class).withArguments(eq("localhost"), eq(1024))
                .thenAnswer(invocation -> {
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (Exception e) {
                    }

                    return socketMock1;
                });

        GeneratedMessage message1 = mock(GeneratedMessage.class);

        Thread t = new Thread(() -> {
            try {
                connectionService.write(message1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();

        // sleep to prevent the resetting of whenNew before the first thread
        // has a chance to call the constructor
        Thread.currentThread().sleep(2);

        Socket socketMock2 = mock(Socket.class);
        OutputStream outputStreamMock2 = mock(OutputStream.class);
        when(socketMock2.getOutputStream()).thenReturn(outputStreamMock2);
        whenNew(Socket.class).withParameterTypes(String.class, int.class).withArguments(eq("localhost"), eq(1024))
                .thenReturn(socketMock2);

        GeneratedMessage message2 = mock(GeneratedMessage.class);
        connectionService.write(message2, 1, TimeUnit.MILLISECONDS);

        t.interrupt();
        t.join();

        verify(socketMock1, times(1)).close();
        verify(socketMock2, times(1)).close();

        verify(message1).writeDelimitedTo(eq(outputStreamMock1));
        verify(message2).writeDelimitedTo(eq(outputStreamMock2));
    }

    @Test
    public void writeTimoutExistingSocket() throws Exception {
        ConnectionService connectionService = new ConnectionService("localhost", 1024);

        Socket socketMock1 = mock(Socket.class);
        OutputStream outputStreamMock1 = mock(OutputStream.class);
        when(socketMock1.getOutputStream()).thenReturn(outputStreamMock1);
        whenNew(Socket.class).withParameterTypes(String.class, int.class).withArguments(eq("localhost"), eq(1024))
                .thenAnswer(invocation -> {
                    Thread.currentThread().sleep(10);
                    return socketMock1;
                });

        GeneratedMessage message1 = mock(GeneratedMessage.class);

        Thread t = new Thread(() -> {
            try {
                connectionService.write(message1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
        Thread.currentThread().sleep(2);

        GeneratedMessage message2 = mock(GeneratedMessage.class);
        connectionService.write(message2, 20, TimeUnit.MILLISECONDS);

        verify(socketMock1, times(1)).close();

        verify(message1).writeDelimitedTo(eq(outputStreamMock1));
        verify(message2).writeDelimitedTo(eq(outputStreamMock1));
    }

    @Test
    public void writeTimoutReopenSocket() throws Exception {
        ConnectionService connectionService = new ConnectionService("localhost", 1024);

        Socket socketMock1 = mock(Socket.class);
        OutputStream outputStreamMock1 = mock(OutputStream.class);
        when(socketMock1.getOutputStream()).thenReturn(outputStreamMock1);
        whenNew(Socket.class).withParameterTypes(String.class, int.class).withArguments(eq("localhost"), eq(1024))
                .thenReturn(socketMock1);

        GeneratedMessage message1 = mock(GeneratedMessage.class);
        connectionService.write(message1, 20, TimeUnit.MILLISECONDS);

        verify(socketMock1).close();

        GeneratedMessage message2 = mock(GeneratedMessage.class);
        connectionService.write(message2, 20, TimeUnit.MILLISECONDS);

        verify(message1).writeDelimitedTo(eq(outputStreamMock1));
        verify(message2).writeDelimitedTo(eq(outputStreamMock1));
    }

    @Test
    public void writeValid() throws Exception {
        ConnectionService connectionService = new ConnectionService("localhost", 1024);

        Socket socketMock1 = mock(Socket.class);
        OutputStream outputStreamMock1 = mock(OutputStream.class);
        when(socketMock1.getOutputStream()).thenReturn(outputStreamMock1);
        whenNew(Socket.class).withParameterTypes(String.class, int.class).withArguments(eq("localhost"), eq(1024))
                .thenReturn(socketMock1);

        GeneratedMessage message1 = mock(GeneratedMessage.class);
        connectionService.write(message1);

        verify(socketMock1).close();

        verify(message1).writeDelimitedTo(eq(outputStreamMock1));
    }

    @Test
    public void writeAndReadTimoutExistingSocket() throws Exception {
        ConnectionService connectionService = new ConnectionService("localhost", 1024);

        Socket socketMock1 = mock(Socket.class);
        OutputStream outputStreamMock1 = mock(OutputStream.class);
        when(socketMock1.getOutputStream()).thenReturn(outputStreamMock1);
        whenNew(Socket.class).withParameterTypes(String.class, int.class).withArguments(eq("localhost"), eq(1024))
                .thenAnswer(invocation -> {
                    Thread.currentThread().sleep(10);
                    return socketMock1;
                });

        GeneratedMessage message1 = mock(GeneratedMessage.class);

        GeneratedMessage response1 = mock(GeneratedMessage.class);

        mockStatic(Parser.class);
        when(Parser.parseFrom(any())).thenReturn(response1);

        Wrapper wrapper = Wrapper.newBuilder().setType(Type.CLIENTLOGINREQUEST).setData(ByteString.EMPTY).build();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        wrapper.writeDelimitedTo(byteArrayOutputStream);

        InputStream in = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        when(socketMock1.getInputStream()).thenReturn(in);

        Thread t = new Thread(() -> {
            try {
                connectionService.writeAndRead(message1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
        Thread.currentThread().sleep(2);

        GeneratedMessage response2 = mock(GeneratedMessage.class);
        PowerMockito.when(Parser.parseFrom(any())).thenReturn(response2);

        Wrapper wrapper2 = Wrapper.newBuilder().setType(Type.CLIENTLOGINREQUEST)
                .setData(ByteString.copyFrom("byte string 2".getBytes())).build();

        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        wrapper2.writeDelimitedTo(byteArrayOutputStream2);

        InputStream in2 = new ByteArrayInputStream(byteArrayOutputStream2.toByteArray());

        when(socketMock1.getInputStream()).thenReturn(in2);

        GeneratedMessage message2 = mock(GeneratedMessage.class);
        GeneratedMessage m = connectionService.writeAndRead(message2, 20, TimeUnit.MILLISECONDS);

        verify(socketMock1, times(1)).close();

        verify(message1).writeDelimitedTo(eq(outputStreamMock1));
        verify(message2).writeDelimitedTo(eq(outputStreamMock1));

        assertEquals(response2, m);
    }
}
