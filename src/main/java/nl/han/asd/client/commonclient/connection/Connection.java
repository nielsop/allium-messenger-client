package nl.han.asd.client.commonclient.connection;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Observer;

/**
 * Created by Jevgeni on 15-4-2016.
 */
class Connection {
    private boolean isRunning = false;

    private Socket socket = null;
    private OutputStream outputStream = null;
    private InputStream inputStream = null;

    public Connection(String hostName, int portNumber) throws IOException {
        socket = new Socket(hostName, portNumber);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
    }

    public synchronized void Write(byte[] data) throws IOException {
        outputStream.write(data);
    }

    public synchronized byte[] Read() {
        byte[] buffer = new byte[1024];
        byte[] data = null;
        try {
            int bytesRead = inputStream.read(buffer);

            data = new byte[bytesRead];
            data = Arrays.copyOf(buffer, bytesRead);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public synchronized void Read(Observer observer)
    {
        Runnable readTask = () -> {
            while (isRunning == true)
            {
                byte[] data = Read();
                observer.update(null, data);

                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread readThread = new Thread(readTask);
        readThread.start();
    }

    public synchronized void Close() throws IOException {
        isRunning = false;
        socket.close();
    }

    public boolean isConnected()
    {
        return socket.isConnected(); // && socket.isBound(); ??
    }

}
