package org.example.javafxapv;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.awt.*;
import java.io.IOException;
import java.sql.*;

import static org.example.javafxapv.ChildControl.Mode.*;


public class ChildControl {

    public TableColumn actionColumn;
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

    @FXML
    private ChoiceBox<String> filter;

//    @FXML
//    private ImageView imageView;

    public enum Mode {
        VIEW, EDIT, DELETE
    }

    private Mode mode = Mode.VIEW; // Mode default adalah VIEW


    private String user;

    // Metode untuk mengatur gambar ke ImageView
//    public void setImage() {
//        // Membuat objek Image dengan menggunakan path file gambar
//        Image image = new Image("bucket.png");
//
//        // Mengatur gambar ke ImageView
//        imageView.setImage(image);
//    }

//    @FXML
//    public void initializeimage(){
//        String imagePath = new File("bucket.png").toURI().toString();
//        Image image = new Image(imagePath);
//        imageView.setImage(image);
//    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @FXML
    public void initialize2(){
        ObservableList<String> items = FXCollections.observableArrayList("Kategori","Makanan & Minuman", "Fashion", "Produk Digital", "Travel", "Game");
        filter.setItems(items);
//        filter.getSelectionModel().selectFirst();
        filter.setValue("Kategori");
    }

    @FXML
    private void initialize1() {
        // Mengikat kolom dengan properti yang sesuai
        idVoucherColumn.setCellValueFactory(new PropertyValueFactory<>("IdVoucher"));
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("namaVoucher"));
        jenisColumn.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        tanggalColumn.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        kategoriColumn.setCellValueFactory(new PropertyValueFactory<>("kategori"));

//
//        // Menambahkan kolom tombol untuk setiap mode
//        TableColumn<Voucher, Void> actionColumn = new TableColumn<>("Action");
        // Menambahkan tombol ke setiap baris
        Callback<TableColumn<Voucher, Void>, TableCell<Voucher, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Voucher, Void> call(final TableColumn<Voucher, Void> param) {
                final TableCell<Voucher, Void> cell = new TableCell<>() {
                    private final Button detailButton = new Button("Detail");
                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Delete");

                    {
                        // Atur aksi untuk tombol detail
                        detailButton.setOnAction(event -> {
                            Voucher voucher = getTableView().getItems().get(getIndex());
                            // Lakukan sesuatu saat tombol detail ditekan
                        });

                        // Atur aksi untuk tombol edit
                        editButton.setOnAction(event -> {
                            Voucher voucher = getTableView().getItems().get(getIndex());
                            // Lakukan sesuatu saat tombol edit ditekan
                        });

                        // Atur aksi untuk tombol delete
                        deleteButton.setOnAction(event -> {
                            Voucher voucher = getTableView().getItems().get(getIndex());
                            // Lakukan sesuatu saat tombol delete ditekan
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Tampilkan tombol sesuai dengan mode
                            switch (mode) {
                                case VIEW:
                                    setGraphic(detailButton);
                                    break;
                                case EDIT:
                                    setGraphic(editButton);
                                    break;
                                case DELETE:
                                    setGraphic(deleteButton);
                                    break;
                            }
                        }
                    }
                };
                return cell;
            }
        };

//        tableView.getColumns().add(actionColumn);
        actionColumn.setCellFactory(cellFactory);


        // Menambahkan kolom aksi ke TableView


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
                    initialize2();
//                    setImage();
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

    public void onEditButtonClick() {
        setMode(EDIT);
        onRefreshButtonClick();
    }

    public void onDeleteButtonClick() {
        setMode(DELETE);
        onRefreshButtonClick();
    }

    public void onViewButtonClick(ActionEvent actionEvent) {
        setMode(VIEW);
        onRefreshButtonClick();
    }
}
