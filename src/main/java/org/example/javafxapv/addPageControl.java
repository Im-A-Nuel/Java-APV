package org.example.javafxapv;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class addPageControl {
    public void OnBackbtn3ClickButton(ActionEvent event) {
        // Dapatkan stage saat ini dari tombol yang ditekan
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Tutup stage untuk kembali ke halaman sebelumnya
        stage.close();
    }
}
