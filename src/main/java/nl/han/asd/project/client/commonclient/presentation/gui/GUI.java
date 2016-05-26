package nl.han.asd.project.client.commonclient.presentation.gui;

/**
 * Created by Marius on 19-04-16.
 */

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.han.asd.project.client.commonclient.CommonclientModule;
import nl.han.asd.project.client.commonclient.presentation.CommonClientGateway;
import nl.han.asd.project.client.commonclient.presentation.gui.controller.DashboardController;
import nl.han.asd.project.client.commonclient.presentation.gui.controller.auth.LoginController;
import nl.han.asd.project.client.commonclient.presentation.gui.controller.auth.RegisterController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GUI extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(GUI.class);
    public CommonClientGateway pLayer;
    private Stage stage;
    private Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Page page) {
        switch (page) {
            case LOGIN:
                scene = buildScene(new LoginController(this).getGridPane());
                break;
            case REGISTER:
                scene = buildScene(new RegisterController(this).getGridPane());
                break;
            case DASHBOARD:
                scene = buildScene(new DashboardController(this).getBorderPane());
                break;
            default:
                scene = buildScene(new LoginController(this).getGridPane());
                break;
        }
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void start(Stage primaryStage) {
        Injector injector = Guice.createInjector(new CommonclientModule());
        injector.injectMembers(this);
        try {
            stage = primaryStage;
            stage.setTitle("Ui Berichter");
            stage.setMinWidth(640);
            stage.setMinHeight(360);
            setScene(Page.LOGIN);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    private Scene buildScene(Parent pane) {
        if (scene != null)
            return new Scene(pane, scene.getWidth(), scene.getHeight());
        else
            return new Scene(pane);
    }

    @Inject
    public void setPresentationLayer(CommonClientGateway pLayer) {
        this.pLayer = pLayer;
    }

    public enum Page {
        LOGIN, REGISTER, DASHBOARD, CONTACTS, CHAT, SETTINGS, CONFIRMATION
    }
}
