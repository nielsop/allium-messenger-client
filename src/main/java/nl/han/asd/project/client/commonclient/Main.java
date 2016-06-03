package nl.han.asd.project.client.commonclient;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.login.InvalidCredentialsException;

import java.io.IOException;

public class Main {

    private static CommonClientGateway commonClientGateway;

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new CommonClientModule());
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
    }

    private static void defaultType() {
        try {
            commonClientGateway.registerRequest("user", "password", "password");
            commonClientGateway.loginRequest("user", "password");

            Thread.sleep(60000);
        } catch (IOException | MessageNotSentException | InvalidCredentialsException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
