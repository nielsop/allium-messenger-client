package nl.han.asd.project.client.commonclient.connection;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Observable;

/**
 * Provides basic socket operations used solely by the ConnectionService.
 */
class Connection extends Observable {
    private volatile boolean isRunning = false;

    private Socket socket = null;
    private OutputStream outputStream = null;
    private InputStream inputStream = null;

    private int sleepTime = 25; // default

    public Connection() { }

    public void open(String hostName, int portNumber) throws IllegalArgumentException, SocketException {
        if (hostName == null || hostName.length() < 7)
            throw new IllegalArgumentException("hostname");
        if (portNumber <= 10)
            throw new IllegalArgumentException("portnumber");

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

    public void write(byte[] data) throws SocketException {
        if (data == null)
            throw new IllegalArgumentException("data");

        try {
            outputStream.write(data);
        } catch (IOException e) {
            throw new SocketException("Error writing data to socket stream.");
        }
    }

    public byte[] read() throws SocketException {
        byte[] buffer = new byte[1024];
        byte[] data = null;

        int bytesRead = -1;

        try {
            // synchronize so only one operation is executed in a multi threaded environment.
            // note that the code in the block underneath here should be the only accessor to the inputstream.
            synchronized (this) {
                // -1: EOF
                //  0: NOTHING TO READ
                // >0: DATA
                bytesRead = inputStream.read(buffer);
            }
        } catch (IOException e) {
            // something went wrong while reading the data from the stream.

            if (isConnected() && bytesRead == -1)
            {
                // if the connection was ever / is connected, attempt to close it.
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
                        setChanged();
                        notifyObservers(data);

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

    public void stopReadAsync() {
        if (!isRunning)
            isRunning = false;
    }

    public void close() throws IOException {
        isRunning = false;

        if (outputStream != null)
            outputStream.close();

        if (inputStream != null)
            inputStream.close();

        if (isConnected())
            socket.close();
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) throws IllegalArgumentException {
        if (sleepTime < 0)
            throw new IllegalArgumentException("Must be at least 1.");

        this.sleepTime = sleepTime;
    }

    public boolean isConnected()
    {
        if (socket == null) return false;

        return socket.isConnected() && !socket.isClosed();
    }

}
