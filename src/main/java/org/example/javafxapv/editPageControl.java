package org.example.javafxapv;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.sql.*;
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
    private String username;
    public Button backbtn2;

    public void OnBackbtn2ClickButton(ActionEvent event) {
        // Dapatkan stage saat ini dari tombol yang ditekan
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Tutup stage untuk kembali ke halaman sebelumnya
        stage.close();
    }

    public void showData(Voucher voucher) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void OnSavekbtn2ClickButton(ActionEvent event) {

        int id = Integer.parseInt(idfield.getText());
        String newname = namafield.getText();
        String newjenis = jenisfield.getText();
        Date newtanggal = Date.valueOf(tanggalpick.getValue());
        String newkategori = (String) category.getValue();
        String newinstruksi = instruksifield.getText();
        String newbatasan = batasanfield.getText();


        try{
            String query = "update voucher set nama = ?, jenis = ?, tanggalKadaluwarsa = ?, kategori = ?, instruksi = ?, batasan = ? where idVoucher = ?";
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/user", "root", "");
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1,newname);
            ps.setString(2, newjenis);
            ps.setDate(3, newtanggal);
            ps.setString(4, newkategori);
            ps.setString(5, newinstruksi);
            ps.setString(6, newbatasan);
            ps.setInt(7, id);

            int hasil = ps.executeUpdate();

            if(hasil == 1){
                String st = " Berhasil Mengubah data ";
                JOptionPane.showMessageDialog(null, st);
                System.out.println(getUsername()+ " Merubah data Voucher");
            }else{
                System.out.println("Gagal");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}