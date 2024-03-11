package org.example.javafxapv;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class coba extends Application{


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Mengakses versi JavaFX runtime saat ini
        String javafxVersion = System.getProperty("javafx.version");
        System.out.println("JavaFX Runtime Version: " + javafxVersion);

        // Mengakhiri aplikasi
        Platform.exit();
    }
}
