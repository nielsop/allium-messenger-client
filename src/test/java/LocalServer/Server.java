package LocalServer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.han.asd.project.client.commonclient.connection.Packer;
import nl.han.asd.project.client.commonclient.cryptography.CryptographyService;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Jevgeni on 19-4-2016.
 */
public class Server {
    private volatile boolean isRunning = true;
    private Packer packer;
    private int i = 0;
    private ExecutorService threadPool =
            Executors.newFixedThreadPool(10);

    public Server() {
        final Injector injector = Guice.createInjector(new EncryptionModule());
        packer = new Packer(new CryptographyService(injector.getInstance(IEncryptionService.class)));
    }

    public void Start(final int port) throws IOException {
        final ServerSocket serverSocket = new ServerSocket(port);
        Runnable runnable = () -> {
            try {
                while(isRunning) {
                    Socket socket = serverSocket.accept();
                    i++;
                    log(String.format("New connection: %d", i));
                    threadPool.execute(new Worker(socket, packer));
                }
            } catch (IOException e) {
                log("Server failed.");
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void Stop()
    {
        isRunning = false;
    }

    public void getPublicKey() {

    }

    public void log(Object o){
        System.out.println(o.toString());
    }
}
