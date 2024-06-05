package com.example.pharma_gest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class PharmaController {
    @FXML
    private Button cancelButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void setCancelButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();}
    public boolean validateLogin() {
        String verifyLogin = "SELECT count(1) FROM useraccounts WHERE username = ? AND mdp_pharm = ?";
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();

            if (connectDB != null) {
                PreparedStatement preparedStatement = connectDB.prepareStatement(verifyLogin);
                preparedStatement.setString(1, usernameTextField.getText());
                preparedStatement.setString(2, passwordPasswordField.getText());

                ResultSet queryResult = preparedStatement.executeQuery();

                if (queryResult.next() && queryResult.getInt(1) == 1) {
                    loginMessageLabel.setText("Bienvenue !");
                    return true;
                } else {
                    loginMessageLabel.setText("Login failed. Please try again.");}
            } else {
                loginMessageLabel.setText("Error connecting to the database.");}
        } catch (SQLException e) {
            e.printStackTrace();}
        return false;}

    public void setLoginButtonOnAction(ActionEvent event) {
        if (!usernameTextField.getText().isBlank() && !passwordPasswordField.getText().isBlank()) {
            if (validateLogin()) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                    Parent nextSceneRoot = loader.load();
                    Scene nextScene = new Scene(nextSceneRoot);

                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.setScene(nextScene);
                    currentStage.show();
                } catch (IOException e) {
                    e.printStackTrace();}}
        } else {
            loginMessageLabel.setText("Erreur! Connexion Impossible!");}}

    @FXML
    public void switchSceneVente(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("vente.fxml"));
            Parent nextSceneRoot = loader.load();
            Scene nextScene = new Scene(nextSceneRoot);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(nextScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();}}

    @FXML
    public void switchSceneMedicament(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("medicaments.fxml"));
            Parent nextSceneRoot = loader.load();
            Scene nextScene = new Scene(nextSceneRoot);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(nextScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();}}

    FXMLLoader loader = new FXMLLoader(getClass().getResource("medicaments.fxml"));

}

