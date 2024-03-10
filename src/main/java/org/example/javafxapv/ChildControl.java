package org.example.javafxapv;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

public class ChildControl {

    @FXML
    private Button logout;

    @FXML
    private TableView<Voucher> tableView;

    @FXML
    private TableColumn<Voucher, Integer> idVoucherColumn;

    @FXML
    private TableColumn<Voucher, String> namaColumn;

    @FXML
    private TableColumn<Voucher, String> jenisColumn;

    @FXML
    private TableColumn<Voucher, java.sql.Date> tanggalColumn;

    @FXML
    private TableColumn<Voucher, String> kategoriColumn;

    @FXML
    private Label namelabel;

    private String user;


    @FXML
    private void initialize1() {
        // Mengikat kolom dengan properti yang sesuai
        idVoucherColumn.setCellValueFactory(new PropertyValueFactory<>("IdVoucher"));
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("namaVoucher"));
        jenisColumn.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        tanggalColumn.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        kategoriColumn.setCellValueFactory(new PropertyValueFactory<>("kategori"));
    }

    private ObservableList<Voucher> getVoucherFromDatabase(String username){
        ObservableList<Voucher> vouchers = FXCollections.observableArrayList();
        String query = "SELECT * FROM voucher WHERE username = ?";

        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/user", "root", "");
        PreparedStatement statement = connection.prepareStatement(query)){

            statement.setString(1, username);

            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    // ambil data voucuher dari hasil query
                    int idVoucher = resultSet.getInt("idVoucher");
                    String nama = resultSet.getString("nama");
                    String jenis = resultSet.getString("jenis");
                    Date tanggal = resultSet.getDate("tanggalKadaluwarsa");
                    String kategori = resultSet.getString("kategori");

                    vouchers.add(new Voucher(idVoucher, nama, jenis, tanggal, kategori));
                    System.out.println(idVoucher + " " + nama + " " + jenis + " " + tanggal + " " + kategori);
                    initialize1();
                }

                System.out.println("Database Acces");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return vouchers;
    }

    public void handleLoginSucces(LoginControl.LoginSuccessEvent event){
        String username = event.getUsername();
        user = event.getUsername();
        namelabel.setText(username);

        // ambil data dari database berdasarka username pengguna
        ObservableList<Voucher> vouchers = getVoucherFromDatabase(user);

        //set isi table
        tableView.setItems(vouchers);

        //refresh
//        tableView.refresh();

    }



    @FXML
    private void initialize() {
        LoginControl loginControl = new LoginControl();
        loginControl.addLoginSuccessHandler(new EventHandler<LoginControl.LoginSuccessEvent>() {
            @Override
            public void handle(LoginControl.LoginSuccessEvent event) {
                handleLoginSucces(event);
            }
        });
    }


    public void onLogoutButtonClick() {
        try{
           FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("loginPage.fxml"));

           Scene scene = new Scene(fxmlLoader.load());
           Stage stage = (Stage) logout.getScene().getWindow();

           stage.setScene(scene);
           stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public void setLoginSuccessHandler(EventHandler<LoginControl.LoginSuccessEvent> loginSuccessEventHandler) {
    }

    public void onRefreshButtonClick() {
        tableView.refresh();
    }
}
