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

public class GUI extends Application {
    private Stage stage;
    public PresentationLayer pLayer;

    public enum Page {
        LOGIN, REGISTER
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
                scene = new Scene(new PaneLogin(this).getGrid(), 400, 300);
                break;
            case REGISTER:
                scene = new Scene(new PaneRegister(this).getGrid(), 400, 300);
                break;
        }
        stage.setScene(scene);
        stage.show();
    }

    //TODO: fix dependency injection
    /*@Inject
    public void setPresentationLayer(PresentationLayer pLayer) {
        this.pLayer = pLayer;
    }*/
}
