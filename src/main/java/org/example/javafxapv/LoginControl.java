package org.example.javafxapv;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;




public class LoginControl {
    public Button reset;
    public Button login;
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button signup;
    private PreparedStatement statement;

    // Event untuk keberhasilan login
    private EventHandler<LoginSuccessEvent> loginSuccessHandler;

    // Method untuk menambah event handler ke event LoginSuccessEvent
    public void addLoginSuccessHandler(EventHandler<LoginSuccessEvent> handler) {
        loginSuccessHandler = handler;
    }
//
//    // Buat objek EventHandler<LoginSuccessEvent>
//    EventHandler<LoginSuccessEvent> loginSuccessEventHandler = new EventHandler<LoginControl.LoginSuccessEvent>() {
//        @Override
//        public void handle(LoginControl.LoginSuccessEvent event) {
//            // Lakukan sesuatu ketika login berhasil
//        }
//    };

    @FXML
    protected void onResetButtonClick() {
        username.setText("");
        password.setText("");
    }

    public static class LoginSuccessEvent extends ActionEvent {
        private final String username;

        public LoginSuccessEvent(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }
    }


    @FXML
    protected void onSignupButtonClick(){
        // pakai try catch untuk error handling
        try {
            // untuk memanggil source laman yang akan dituju
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registerPage.fxml"));

            // untuk membuat scene baru
            Scene scene = new Scene(fxmlLoader.load());

            // untuk mendapatkan stage tampilan
            Stage stage =  (Stage) signup.getScene().getWindow();

            // tampilkan scene
            stage.setScene(scene);
            stage.show();

        }catch (IOException e){
            System.out.println("Error loading FXML file: " + e.getMessage());
        }
    }

    public void onLoginButtonClick() {
        String inputusername = username.getText();
        String inputpass = password.getText();

        // query
        String query = "SELECT * FROM data WHERE username = ? AND password = ?";

        try (
                // koneksi ke database mysql
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/user", "root", "");
                // variable untuk username & pass
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            // atur isi tempat simpan username & pass
            statement.setString(1, inputusername);
            statement.setString(2, inputpass);

            // jalankan query
            try (ResultSet resultSet = statement.executeQuery()) {
                // Check if the result set contains any rows
                if (resultSet.next()) {
                    // Login successful
                    String user = username.getText();

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("child.fxml"));

                        Scene scene = new Scene(fxmlLoader.load());
                        Stage stage = (Stage) login.getScene().getWindow();

                        stage.setScene(scene);
                        stage.show();

                        // Membuat objek LoginControl dari FXML Loader
                        ChildControl childControl = fxmlLoader.getController();

                        // Membuat objek EventHandler untuk menangani peristiwa login yang berhasil
                        EventHandler<LoginControl.LoginSuccessEvent> loginSuccessEventHandler = new EventHandler<LoginControl.LoginSuccessEvent>() {
                            @Override
                            public void handle(LoginControl.LoginSuccessEvent event) {
                                childControl.handleLoginSucces(event);
                            }
                        };

                        // Menambahkan event handler ke LoginControl
                        childControl.setLoginSuccessHandler(loginSuccessEventHandler);

                        // Panggil event handler saat login berhasil
                        childControl.handleLoginSucces(new LoginControl.LoginSuccessEvent(user));

                        System.out.println(user + " Login Successful");

                    }catch(IOException e){
                        e.printStackTrace();
                    }

                } else {
                    // Invalid username or password
                    String st = "Username atau Password salah!!";
                    JOptionPane.showMessageDialog(null, st);
                    System.out.println("Invalid username or password");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
