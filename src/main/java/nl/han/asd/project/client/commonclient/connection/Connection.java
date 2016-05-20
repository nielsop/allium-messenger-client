package nl.han.asd.project.client.commonclient.connection;

import nl.han.asd.project.client.commonclient.utility.Validation;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * Provides basic socket operations used solely by the ConnectionService.
 *
 * @author Jevgeni Geursten
 */
class Connection {

    private static final Logger LOGGER = LoggerFactory.getLogger(Connection.class);
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    /**
     * Initializes the class.
     *
     */
    public Connection() {
        inputStream = null;
        outputStream = null;
        socket = null;
    }

    /**
     * Opens a socket to a hostname and port combination.
     *
     * @param hostName   Internet protocol address.
     * @param portNumber Port number to connect to.
     * @throws IllegalArgumentException A parameter has an invalid value.
     * @throws IOException          Connection or streams failed.
     */
    public void open(final String hostName, final int portNumber)
            throws IOException {
        if (Validation.isValidAddress(hostName) && Validation.isValidPort(portNumber)) {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(hostName, portNumber), 7000);
                socket.setSoTimeout(5000);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
                throw new SocketException(
                        "Couldn't connect to the given endpoint.");
            }

            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
        } else {
            throw new SocketException(
                    "Invalid hostname or port number specified!");
        }
    }

    /**
     * Writes data to the output stream.
     *
     * @param wrapper Data wrapped inside the EncryptedWrapper class to be send over the socket.
     * @throws IOException Writing to stream failed.
     */
    public void write(final HanRoutingProtocol.Wrapper wrapper)
            throws IOException {
            wrapper.writeDelimitedTo(outputStream);
    }

    /**
     * Reads data from the input stream.
     *
     * @return An EncryptedWrapper that contains the real object.
     * @throws IOException Reading to stream failed.
     */
    public HanRoutingProtocol.Wrapper read() throws IOException {
        HanRoutingProtocol.Wrapper wrapper;
        synchronized (this) {
            wrapper = HanRoutingProtocol.Wrapper.parseDelimitedFrom(inputStream);
        }
        return wrapper;
    }

    /**
     * Closes the sockets and their streams.
     *
     * @throws IOException Either the sockets or streams had trouble closing down.
     */
    public void close() throws IOException {
        if (isConnected())
            socket.close();
    }

    /**
     * Checks if the socket connection is still available.
     *
     * @return False if closed, True if open.
     */
    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

}
