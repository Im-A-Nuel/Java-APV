package org.example.javafxapv;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.sql.*;
import java.io.IOException;
import java.util.stream.Stream;


public class RegisterControl {
    @FXML
    private Button back;

    @FXML
    private Button reset;

    @FXML
    private TextField firstname;

    @FXML
    private TextField lastname;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField RePass;
    private javax.swing.JOptionPane JOptionPane;

    public void onbackButtonClick(ActionEvent actionEvent) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("loginPage.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) back.getScene().getWindow();

            stage.setScene(scene);
            stage.show();

        }catch (IOException e){
            e.printStackTrace();
        }

    }


    public void onResetButtonClick() {
        firstname.setText("");
        lastname.setText("");
        username.setText("");
        password.setText("");
        RePass.setText("");
    }

    public void onSignupButtonClick() {
        String inputusername = username.getText();
        String inputpass = password.getText();
        String inputfirtsname = firstname.getText();
        String inputlastname = lastname.getText();
        String reinputpass = RePass.getText();

        String query = "INSERT INTO data(username, password, firstname, lastname) VALUE(?,?,?,?)";

        if(Stream.of(inputfirtsname, inputusername, inputpass, inputlastname).allMatch(String::isEmpty)){
            String st = "Silahkan lengkapi data anda!!";
            JOptionPane.showMessageDialog(null, st);
            System.out.println("Failed to registered user");
        }else{
            if(!(inputpass.equals(reinputpass))){
                String st = "Ulangi Password anda!!";
                JOptionPane.showMessageDialog(null, st);
                System.out.println("Failed to registered user");
            }else{

                try(
                        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/user", "root", "");
                        PreparedStatement statement = connection.prepareStatement(query);
                ) {
                    statement.setString(1,inputusername);
                    statement.setString(2,inputpass);
                    statement.setString(3,inputfirtsname);
                    statement.setString(4,inputlastname);

                    int rowsAffected = statement.executeUpdate();

                    if(rowsAffected > 0){
                        String st = "User registered successfully";
                        JOptionPane.showMessageDialog(null, st);
                        System.out.println("User registered successfully");
                    }else{
                        String st = "Failed to registered user";
                        JOptionPane.showMessageDialog(null, st);
                        System.out.println("Failed to registered user");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }

            }

        }



    }
}
