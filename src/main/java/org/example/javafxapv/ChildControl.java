package org.example.javafxapv;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;

import static org.example.javafxapv.ChildControl.Mode.*;


public class ChildControl {

    public TableColumn actionColumn;
    public TextField keysearch;
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
    private ChoiceBox filter;


    public enum Mode {
        VIEW, EDIT, DELETE
    }

    private Mode mode = Mode.VIEW; // Mode default adalah VIEW


    private String user;

    private ObservableList<Voucher> vouchers;

    public void onSearchButtonClick(ActionEvent event) {
        String search = keysearch.getText();

        ObservableList<Voucher> vouchers = SearchVoucherFromDatabase(user, search);

        tableView.setItems(vouchers);

    }

    public void onAddButtonClick(ActionEvent event) {
        try {
            // load addPage
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addPage.fxml"));
            Parent root = loader.load();

            // ambil kontrol add
            addPageControl control = loader.getController();
            control.setUsername(getUser());


            // buat stage popup baru
            Stage popupadd = new Stage();
            popupadd.initModality(Modality.APPLICATION_MODAL);
            popupadd.setScene(new Scene(root));
            popupadd.showAndWait();
            updateTableView();

        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void saveData(){
        vouchers = tableView.getItems();
    }

    public void reloadData(){
        tableView.setItems(vouchers);
    }

    public void updateTableView() {
        // Ambil data voucher dari database atau sumber data lainnya
        ObservableList<Voucher> updatedVouchers = getVoucherFromDatabase(user);

        // Set data ke TableView
        tableView.setItems(updatedVouchers);
    }


    // Metode untuk menampilkan popup detailPage
    private void showDetailPopup(Voucher voucher) {
        try {
            // Load detailPage.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("detailPage.fxml"));
            Parent root = loader.load();

            // Inisialisasi data di popup
            detailPageControl controller = loader.getController();
            controller.initData(voucher);

            // Buat stage baru untuk popup
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL); // Atur modality agar tetap modal
            popupStage.setScene(new Scene(root));
            popupStage.showAndWait(); // Tampilkan popup dan tunggu sampai ditutup
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showEditPopup(Voucher voucher){
        try {
            // load editPage
            FXMLLoader loader = new FXMLLoader(getClass().getResource("editPage.fxml"));
            Parent root = loader.load();

            // inisialiasi data popup
            editPageControl control = loader.getController();
            control.showData(voucher);
            control.setUsername(getUser());

            //buat stage popup
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setScene(new Scene(root));
            popup.showAndWait();
            updateTableView();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void delVoucher(Voucher voucher){
        String user = getUser();
        int id = voucher.getIdVoucher();

        String query = "Delete from voucher where idVoucher = ? and username = ?";

        try(
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/user", "root", "");
                PreparedStatement ps = con.prepareStatement(query);
            )
        {
            ps.setInt(1,id);
            ps.setString(2,user);

            int hasil = ps.executeUpdate();

            if(hasil == 1){
                String st = "Voucher dihapus";
                JOptionPane.showMessageDialog(null, st);
                System.out.println(getUser()  + " Menghapus Voucher");
            }else {
                String st = "Gagal Menghapus Voucher";
                JOptionPane.showMessageDialog(null, st);
                System.out.println(getUser() + " "  + st);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @FXML
    public void initialize2(){
        ObservableList<String> items = FXCollections.observableArrayList("Makanan & Minuman", "Fashion", "Produk Digital", "Travel", "Game");
        filter.setItems(items);
//        filter.setValue("Pilih Kategori");

        filter.setOnAction(e -> {
            String selectedCategory = (String) filter.getValue();
            if (selectedCategory != null) { // pastikan nilai yang dipilih tidak null
                String searchQuery = selectedCategory;
                ObservableList<Voucher> searchResult = SearchVoucherFromDatabase(user, searchQuery);
                tableView.setItems(searchResult);
            } else {
                // Penanganan jika tidak ada item yang dipilih
                // Misalnya, memperbarui tabel atau tampilan lainnya dengan semua data
                ObservableList<Voucher> allVouchers = SearchVoucherFromDatabase(user, ""); // Parameter kosong akan mengembalikan semua voucher
                tableView.setItems(allVouchers);
            }
            filter.setValue(selectedCategory);
        });


    }

    @FXML
    private void initialize1() {
        // Mengikat kolom dengan properti yang sesuai
        idVoucherColumn.setCellValueFactory(new PropertyValueFactory<>("IdVoucher"));
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("namaVoucher"));
        jenisColumn.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        tanggalColumn.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        kategoriColumn.setCellValueFactory(new PropertyValueFactory<>("kategori"));

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

                            showDetailPopup(voucher);

                        });

                        // Atur aksi untuk tombol edit
                        editButton.setOnAction(event -> {
                            Voucher voucher = getTableView().getItems().get(getIndex());

                            showEditPopup(voucher);

                        });

                        // Atur aksi untuk tombol delete
                        deleteButton.setOnAction(event -> {
                            Voucher voucher = getTableView().getItems().get(getIndex());
                            // Lakukan sesuatu saat tombol delete ditekan
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Konfirmasi Hapus");
                            alert.setHeaderText("Menghapus voucher " + voucher.getNamaVoucher() + " ?");

                            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

                            alert.showAndWait().ifPresent(buttonType -> {
                                if (buttonType == ButtonType.YES){
                                    delVoucher(voucher);
                                    ChildControl.this.updateTableView();
                                }else{

                                }
                            });
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

    private ObservableList<Voucher> SearchVoucherFromDatabase(String username, String search){
        ObservableList<Voucher> vouchers = FXCollections.observableArrayList();
        String query = "SELECT * FROM voucher WHERE username = ? AND idVoucher LIKE ? OR nama LIKE ? OR jenis LIKE ? OR tanggalKadaluwarsa LIKE ? OR kategori LIKE ?";

        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/user", "root", "");
            PreparedStatement ps = connection.prepareStatement(query)){

            ps.setString(1, username);
            ps.setString(2, "%" + search + "%");
            ps.setString(3, "%" + search + "%");
            ps.setString(4, "%" + search + "%");
            ps.setString(5, "%" + search + "%");
            ps.setString(6, "%" + search + "%");

            try(ResultSet resultSet = ps.executeQuery()){
                while(resultSet.next()){
                    // ambil data voucuher dari hasil query
                    int idVoucher = resultSet.getInt("idVoucher");
                    String usern = resultSet.getString("username");
                    String nama = resultSet.getString("nama");
                    String jenis = resultSet.getString("jenis");
                    Date tanggal = resultSet.getDate("tanggalKadaluwarsa");
                    String kategori = resultSet.getString("kategori");
                    String instruksi = resultSet.getString("instruksi");
                    String batasan = resultSet.getString("batasan");

                    vouchers.add(new Voucher(idVoucher, usern ,nama, jenis, tanggal, kategori, instruksi, batasan));
                    initialize1();
                    initialize2();
                }

                System.out.println("Searching in database");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return vouchers;
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
                    String usern = resultSet.getString("username");
                    String nama = resultSet.getString("nama");
                    String jenis = resultSet.getString("jenis");
                    Date tanggal = resultSet.getDate("tanggalKadaluwarsa");
                    String kategori = resultSet.getString("kategori");
                    String instruksi = resultSet.getString("instruksi");
                    String batasan = resultSet.getString("batasan");

                    vouchers.add(new Voucher(idVoucher, usern ,nama, jenis, tanggal, kategori, instruksi, batasan));
//                    System.out.println(idVoucher + " " + nama + " " + jenis + " " + tanggal + " " + kategori);
                    initialize1();
                    initialize2();
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
            System.out.println(user + " Logout");
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public void setLoginSuccessHandler(EventHandler<LoginControl.LoginSuccessEvent> loginSuccessEventHandler) {
    }

    public void onRefreshButtonClick() {
        updateTableView();
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

    public String getUser() {
        return user;
    }
}
