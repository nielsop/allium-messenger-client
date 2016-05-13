package nl.han.asd.project.client.commonclient.connection;

/**
 * Created by Marius on 25-04-16.
 */

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
    private static final Logger logger = LoggerFactory.getLogger(Connection.class);

    private int sleepTime = 25; // default

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
     * @throws SocketException          Connection or streams failed.
     */
    public void open(final String hostName, final int portNumber) throws SocketException {
        if (Validation.isValidAddress(hostName) && Validation.isValidPort(portNumber)) {
            try {
                socket = new Socket(hostName, portNumber);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
                throw new SocketException("Couldn't connect to the given endpoint.");
            }
            try {
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
                throw new SocketException("An error occurred while opening the streams on the connected socket.");
            }
        } else {
            throw new SocketException("Invalid hostname or portnumber specified!");
        }
    }

    /**
     * Writes data to the output stream.
     * @param wrapper Data wrapped inside the EncryptedWrapper class to be send over the socket.
     * @throws SocketException Writing to stream failed.
     * @throws IllegalArgumentException A parameter has an invalid value.
     */
    public void write(final HanRoutingProtocol.Wrapper wrapper) throws IllegalArgumentException, SocketException {
        if (wrapper == null) {
            logger.error("Parameter wrapper was null.");
            throw new IllegalArgumentException("wrapper");
        }

        try {
            wrapper.writeDelimitedTo(outputStream);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new SocketException("An error occurred while trying to write data to the stream.");
        }
    }

    /**
     * Reads data from the input stream.
     * @return An EncryptedWrapper that contains the real object.
     * @throws SocketException Connection or streams failed.
     */
    public HanRoutingProtocol.Wrapper read() throws SocketException {
        HanRoutingProtocol.Wrapper wrapper = null;
        try {
            synchronized (this) {
                wrapper = HanRoutingProtocol.Wrapper.parseDelimitedFrom(inputStream);
            }
            if (isConnected()) {
                socket.close();
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new SocketException("An error occurred while trying to read data from the stream.");
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
                    } catch (SocketException e) {
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

        if (outputStream != null)
            outputStream.close();

        if (inputStream != null)
            inputStream.close();

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
