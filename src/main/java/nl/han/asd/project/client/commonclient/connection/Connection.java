package nl.han.asd.project.client.commonclient.connection;

import nl.han.asd.project.client.commonclient.utility.Validation;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Provides basic socket operations used solely by the ConnectionService.
 *
 * @author Jevgeni Geursten
 */
class Connection {

    private static final Logger LOGGER = LoggerFactory.getLogger(Connection.class);
    private final IConnectionPipe connectionService;
    private volatile boolean isRunning;
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    private int sleepTime = 25; // default

    /**
     * Initializes the class.
     *
     * @param service Instance of the ConnectionService masked as IConnectionPipe that is calling this method.
     */
    public Connection(final IConnectionPipe service) {
        connectionService = service;
        inputStream = null;
        outputStream = null;
        socket = null;
        isRunning = false;
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
                socket = new Socket(hostName, portNumber);
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
     * Reads from the input stream on an asynchronous way.
     * <b>Note that this method requires this class to be instantiated using any instance implementing IConnectionService. </b>
     */
    public void readAsync() {
        if (!isRunning) {
            isRunning = true;
            Runnable readTask = () -> {
                while (isRunning) {
                    HanRoutingProtocol.Wrapper wrapper = null;
                    try {
                        wrapper = read();
                        connectionService.onReceiveRead(wrapper);
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        Thread.interrupted();
                        LOGGER.error(e.getMessage(), e);
                    } catch (IOException e) {
                        isRunning = false;
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            };
            Thread readThread = new Thread(readTask);
            readThread.start();
        }
    }

    /**
     * Stops reading data asynchronously.
     */
    public void stopReadAsync() {
        if (isRunning)
            isRunning = false;
    }

    /**
     * Closes the sockets and their streams.
     *
     * @throws IOException Either the sockets or streams had trouble closing down.
     */
    public void close() throws IOException {
        isRunning = false;

        if (isConnected())
            socket.close();
    }

    /**
     * Returns the current sleep time. Sleep time represents the amount of milliseconds the asynchronous thread
     * will sleep in between its process of reading data from the input stream.
     *
     * @return Current sleep time.
     */
    public int getSleepTime() {
        return sleepTime;
    }

    /**
     * Sets sleep time. Sleep time represents the amount of milliseconds the asynchronous thread
     * will sleep in between its process of reading data from the input stream.
     *
     * @param sleepTime Amount of milliseconds needed to wait.
     * @throws IllegalArgumentException A parameter has an invalid value.
     */
    public void setSleepTime(final int sleepTime) {
        if (sleepTime < 0)
            throw new IllegalArgumentException("Must be at least 1.");

        this.sleepTime = sleepTime;
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
