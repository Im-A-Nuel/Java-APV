package org.example.javafxapv;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;
    Label welcom1;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Selamat datang Imanuel");
//        welcom1.setText("Hai");
    }
}