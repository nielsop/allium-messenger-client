package unit.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Jevgeni on 19-4-2016.
 */
public class Server {
    protected volatile boolean isRunning = true;
    protected int i = 0;
    protected ExecutorService threadPool =
            Executors.newFixedThreadPool(10);

    public void Start(final int port) throws IOException {
        final ServerSocket serverSocket = new ServerSocket(port);
        Runnable runnable = () -> {
            try {
                while (isRunning) {
                    Socket socket = serverSocket.accept();
                    i++;
                    log(String.format("New connection: %d", i));
                    threadPool.execute(new Worker(socket));
                }
            } catch (IOException e) {
                log("Server failed.");
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void Stop() {
        isRunning = false;
    }


    public void log(Object o) {
        System.out.println(o.toString());
    }
}
