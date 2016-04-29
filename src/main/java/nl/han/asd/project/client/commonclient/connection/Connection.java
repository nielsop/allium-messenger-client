package nl.han.asd.project.client.commonclient.connection;

import nl.han.asd.project.client.commonclient.utility.Validation;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

/**
 * Provides basic socket operations used solely by the ConnectionService.
 */
class Connection {
    private volatile boolean isRunning;

    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private final IConnectionPipe connectionService;

    private int sleepTime = 25; // default

    public Connection(final IConnectionPipe service)
    {
        connectionService = service;

        inputStream = null;
        outputStream = null;
        socket = null;
        isRunning = false;
    }

    /**
     * Opens a socket to a hostname and port combination.
     * @param hostName Internet protocol address.
     * @param portNumber Port number to connect to.
     * @throws IllegalArgumentException A parameter has an invalid value.
     * @throws SocketException Connection or streams failed.
     */
    public void open(final String hostName, final int portNumber) throws IllegalArgumentException, SocketException {
        final Validation validation = new Validation();
        validation.validateAddress(hostName);
        validation.validatePort(portNumber);

        try {
            // connect to the socket.
            socket = new Socket(hostName, portNumber);
        } catch (IOException e) {
            throw new SocketException("Couldn't connect to the given endpoint.");
        }

        try {
            // attempt to get both input and output streams.
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            throw new SocketException("An error occurred while opening the streams on the connected socket.");
        }
    }

    /**
     * Writes data to the output stream.
     * @param data Data to write to the stream.
     * @throws SocketException Writing to stream failed.
     * @throws IllegalAccessException A parameter has an invalid value.
     */
    public void write(final byte[] data) throws IllegalArgumentException, SocketException {
        if (data == null)
            throw new IllegalArgumentException("data");

        try {
            outputStream.write(data);
        } catch (IOException | NullPointerException e) {
            throw new SocketException("An error occurred while trying to write data to the stream.");
        }
    }

    /**
     * Reads data from the input stream.
     * @return A byte array containing the data and null if no data was read and no exception occurred.
     * @throws SocketException Connection or streams failed.
     */
    public byte[] read() throws SocketException {
        byte[] buffer = new byte[1024];
        byte[] data = null;

        int bytesRead;

        try {
            // synchronize so only one operation is executed in a multi threaded environment.
            // note that the code in the block underneath here should be the only accessor to the input stream.
            synchronized (this) {
                // -1: EOF
                //  0: NOTHING TO READ
                // >0: DATA
                bytesRead = inputStream.read(buffer);
            }
        } catch (IOException | NullPointerException e) {
            // something went wrong while reading the data from the stream.
            // if the connection was ever / is connected, attempt to close it.
            if (isConnected())
            {
                // we cannot fully determine if the connection is still established so attempt to close it and ignore
                // any exception.
                try {
                    socket.close();
                } catch (Exception anyException) { // catch any exception
                }
            }

            throw new SocketException("An error occurred while trying to read data from the stream.");
        }

        if (bytesRead > 0) {
            // copyOf builds a new array, thus it removes the unnecessary 'empty' indices and returns a
            // perfectly sized array.
            data = Arrays.copyOf(buffer, bytesRead);
        }

        return data;
    }

    /**
     * Reads from the input stream on an asynchronous way.
     * <b>Note that this method requires this class to be instantiated using any instance implementing IConnectionService. </b>
     */
    public void readAsync() {
        // There cannot be multiple instances of the async reader on one socket.
        if (!isRunning) {
            isRunning = true;

            Runnable readTask = () -> {
                while (isRunning) {
                    byte[] data = null;
                    try {
                        // Read data from stream using original Read.
                        data = read();

                        // Notify ConnectionService that new data is available.
                        connectionService.onReceiveRead(data);

                        try {
                            // Sleep for a number of seconds.
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            // If another thread, within the ~#ms sleep time, accesses our code.
                            // (which results in a InterruptedException), so clear the current state.
                            // Note this doesn't affect the socket.
                            Thread.interrupted();
                        }

                    } catch (SocketException e) {
                        // Something bad happened. Abort.
                        isRunning = false;
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
     * @return Current sleep time.
     */
    public int getSleepTime() {
        return sleepTime;
    }

    /**
     * Sets sleep time. Sleep time represents the amount of milliseconds the asynchronous thread
     * will sleep in between its process of reading data from the input stream.
     * @param sleepTime Amount of milliseconds needed to wait.
     * @throws IllegalArgumentException A parameter has an invalid value.
     */
    public void setSleepTime(final int sleepTime) throws IllegalArgumentException {
        if (sleepTime < 0)
            throw new IllegalArgumentException("Must be at least 1.");

        this.sleepTime = sleepTime;
    }

    /**
     * Checks if the socket connection is still available.
     * @return False if closed, True if open.
     */
    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
}
