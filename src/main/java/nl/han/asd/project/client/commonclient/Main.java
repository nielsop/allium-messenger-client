package nl.han.asd.project.client.commonclient;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.heartbeat.IHeartbeatService;
import nl.han.asd.project.client.commonclient.login.InvalidCredentialsException;
import nl.han.asd.project.client.commonclient.message.IMessageReceiver;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.store.IContactStore;

import java.io.IOException;

public class Main {

    private static CommonClientGateway commonClientGateway;
    private static Injector injector;

    public static void main(String[] args) {
        injector = Guice.createInjector(new CommonClientModule());
        commonClientGateway = injector.getInstance(CommonClientGateway.class);

        if (System.getenv("integration-enabled") == null) {
            System.out.println( "This application should not be instantiated this way." +
                                "Please use a graphical user interface.");
            return;
        }

        String integrationType = System.getenv("integration-type");

        if (integrationType == null) {
            System.out.println("Set an integration type to let the application perform actions.");
            return;
        }

        if (integrationType.equals("default")) {
            defaultType();
        }

        if (integrationType.equals("echo")) {
            echoType();
        }
    }

    private static void echoType() {
        try {
            commonClientGateway.registerRequest("user", "password", "password");
            commonClientGateway.loginRequest("user", "password");

            commonClientGateway.subscribeReceivedMessages(new IMessageReceiver() {
                @Override
                public void receivedMessage(Message message) {
                    commonClientGateway.addContact(message.getSender().getUsername());
                    commonClientGateway.sendMessage(message);
                }

                @Override
                public void confirmedMessage(String messageId) {

                }
            });

            Thread.sleep(60000);
        } catch (IOException | MessageNotSentException | InvalidCredentialsException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void defaultType() {
        try {
            commonClientGateway.registerRequest("user", "password", "password");
            commonClientGateway.loginRequest("user", "password");
            IHeartbeatService instance = injector.getInstance(IHeartbeatService.class);
            IContactStore constactStore = injector.getInstance(IContactStore.class);
            instance.startHeartbeatFor(constactStore.getCurrentUser());

            Thread.sleep(60000);
        } catch (IOException | MessageNotSentException | InvalidCredentialsException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
