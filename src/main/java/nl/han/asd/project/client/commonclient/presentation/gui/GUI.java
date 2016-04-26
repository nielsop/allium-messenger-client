package nl.han.asd.project.client.commonclient.presentation.gui;

/**
 * Created by Marius on 19-04-16.
 */

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.han.asd.project.client.commonclient.CommonclientModule;
import nl.han.asd.project.client.commonclient.presentation.PresentationLayer;
import nl.han.asd.project.client.commonclient.presentation.gui.view.auth.PaneLogin;
import nl.han.asd.project.client.commonclient.presentation.gui.view.auth.PaneRegister;
import nl.han.asd.project.client.commonclient.presentation.gui.view.PaneConfirmation;
import nl.han.asd.project.client.commonclient.presentation.gui.view.PaneDashboard;
import nl.han.asd.project.client.commonclient.presentation.gui.view.PaneSettings;

public class GUI extends Application {
    private Stage stage;
    public PresentationLayer pLayer;

    public enum Page {
        LOGIN, REGISTER, DASHBOARD, CONTACTS, CHAT, SETTINGS, CONFIRMATION
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Injector injector = Guice.createInjector(new CommonclientModule());
        injector.injectMembers(this);
        try {
            stage = primaryStage;
            stage.setTitle("Ui Berichter");
            setStage(Page.LOGIN);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setStage(Page page) {
        Scene scene = null;
        switch(page) {
            case LOGIN:
                scene = new Scene(new PaneLogin(this).getGridPane(), 800, 600);
                break;
            case REGISTER:
                scene = new Scene(new PaneRegister(this).getGridPane(), stage.getWidth(), stage.getHeight());
                break;
            case DASHBOARD:
                scene = new Scene(new PaneDashboard(this).getBorderPane(), stage.getWidth(), stage.getHeight());
                break;
            case SETTINGS:
                scene = new Scene(new PaneSettings(this).getPane(), stage.getWidth(), stage.getHeight());
                break;
            case CONFIRMATION:
                scene = new Scene(new PaneConfirmation(this).getPane(), stage.getWidth(), stage.getHeight());
                break;
        }
        stage.setScene(scene);
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    @Inject
    public void setPresentationLayer(PresentationLayer pLayer) {
        this.pLayer = pLayer;
    }
}
