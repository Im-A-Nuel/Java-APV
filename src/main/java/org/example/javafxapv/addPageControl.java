package org.example.javafxapv;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.sql.*;

public class addPageControl {
    public Button backbtn3;
    public Button savebtn;
    public TextField idinput;
    public TextField namainput;
    public TextField jenisinput;
    public DatePicker tanggalinput;
    public TextArea instruksiinput;
    public TextArea batasaninput;
    public ChoiceBox categoryinput;

    private String username;


    public void OnBackbtn3ClickButton(ActionEvent event) {
        // Dapatkan stage saat ini dari tombol yang ditekan
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Tutup stage untuk kembali ke halaman sebelumnya
        stage.close();
    }

    public void onSaveButtonClick(ActionEvent event) {
        int inId = Integer.parseInt(idinput.getText());
        String inname = namainput.getText();
        String injenis = jenisinput.getText();
        Date intanggal = Date.valueOf(tanggalinput.getValue());
        String kategori = (String) categoryinput.getValue();
        String ininstruksi = instruksiinput.getText();
        String inbatasan = batasaninput.getText();


        String query = "INSERT INTO voucher VALUE(?,?,?,?,?,?,?,?)";
        try(

                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/user", "root", "");
                PreparedStatement ps = con.prepareStatement(query);
        ){
            ps.setInt(1,inId);
            ps.setString(2,getUsername());
            ps.setString(3, inname);
            ps.setString(4,injenis);
            ps.setDate(5,intanggal);
            ps.setString(6, kategori);
            ps.setString(7,ininstruksi);
            ps.setString(8, inbatasan);

            int hasil = ps.executeUpdate();

            if(hasil == 1){
                String st = " Berhasil Menambahkan data ";
                JOptionPane.showMessageDialog(null, st);
                System.out.println(getUsername() + st);
            }else {
                String st = " Gagal menambahkan data ";
                JOptionPane.showMessageDialog(null, st);
                System.out.println(st);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }



    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername(){
        return username;
    }
}
