package org.example.javafxapv;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class editPageControl {

    public TextField idfield;
    public TextField namafield;
    public TextField jenisfield;
    public DatePicker tanggalpick;
    public TextArea instruksifield;
    public TextArea batasanfield;
    @FXML
    private ChoiceBox<String> category;

    public Button backbtn2;

    public void OnBackbtn2ClickButton(ActionEvent event) {
        // Dapatkan stage saat ini dari tombol yang ditekan
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Tutup stage untuk kembali ke halaman sebelumnya
        stage.close();
    }

    public void showData(Voucher voucher){
        idfield.setText(String.valueOf(voucher.getIdVoucher()));
        namafield.setText(voucher.getNamaVoucher());
        jenisfield.setText(voucher.getJenis());
        category.setValue(voucher.getKategori());

        // Konversi Date menjadi LocalDate
        Instant instant = Instant.ofEpochMilli(voucher.getTanggal().getTime());
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        tanggalpick.setValue(localDate);
        instruksifield.setText(voucher.getInstruksi());
        batasanfield.setText(voucher.getBatasan());
    }

}
