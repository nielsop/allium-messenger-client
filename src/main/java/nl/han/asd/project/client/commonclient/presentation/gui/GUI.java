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
import nl.han.asd.project.client.commonclient.presentation.PresentationLayer;
import nl.han.asd.project.client.commonclient.presentation.gui.controller.auth.LoginController;
import nl.han.asd.project.client.commonclient.presentation.gui.view.PaneConfirmation;
import nl.han.asd.project.client.commonclient.presentation.gui.view.PaneDashboard;
import nl.han.asd.project.client.commonclient.presentation.gui.view.PaneSettings;
import nl.han.asd.project.client.commonclient.presentation.gui.view.auth.Register;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GUI extends Application {
    private static final Logger logger = LoggerFactory.getLogger(GUI.class);
    public PresentationLayer pLayer;
    private Stage stage;
    private Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    public Scene getScene() {
        return scene;
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
            setStage(Page.LOGIN);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public void setStage(Page page) {
        switch (page) {
            case LOGIN:
                scene = buildScene(new LoginController(this).getGridPane());
                break;
            case REGISTER:
                scene = buildScene(new Register(this).getGridPane());
                break;
            case DASHBOARD:
                scene = buildScene(new PaneDashboard(this).getBorderPane());
                break;
            case SETTINGS:
                scene = buildScene(new PaneSettings(this).getGridPane());
                break;
            case CONFIRMATION:
                scene = buildScene(new PaneConfirmation(this).getPane());
                break;
            default:
                scene = buildScene(new LoginController(this).getGridPane());
                break;
        }
        stage.setScene(scene);
        stage.show();
    }

    private Scene buildScene(Parent pane) {
        if (scene != null)
            return new Scene(pane, scene.getWidth(), scene.getHeight());
        else
            return new Scene(pane);
    }

    @Inject
    public void setPresentationLayer(PresentationLayer pLayer) {
        this.pLayer = pLayer;
    }

    public enum Page {
        LOGIN, REGISTER, DASHBOARD, CONTACTS, CHAT, SETTINGS, CONFIRMATION
    }
}
