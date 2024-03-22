package org.example.javafxapv;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;

public class detailPageControl {


    public Button backbtn;

    @FXML
    private Label idLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label jenisLabel;

    @FXML
    private Label kategoriLabel;

    @FXML
    private Label masaLabel;

    @FXML
    private Label instruksiLabel;

    @FXML
    private Label batasanLabel;


    @FXML
    private void OnbackbtnClickButton(ActionEvent event) {
        // Dapatkan stage saat ini dari tombol yang ditekan
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Tutup stage untuk kembali ke halaman sebelumnya
        stage.close();
    }

    public void initData(Voucher voucher) {
        // Lakukan inisialisasi data sesuai kebutuhan, misalnya:
        idLabel.setText(String.valueOf(voucher.getIdVoucher()));
        nameLabel.setText(voucher.getNamaVoucher());
        jenisLabel.setText(voucher.getJenis());
        kategoriLabel.setText(voucher.getKategori());

        // Konversi tanggal menjadi format teks
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String tanggalFormatted = dateFormat.format(voucher.getTanggal());

        masaLabel.setText(tanggalFormatted);

        instruksiLabel.setText(voucher.getInstruksi());
        batasanLabel.setText((voucher.getBatasan()));


    }
}

