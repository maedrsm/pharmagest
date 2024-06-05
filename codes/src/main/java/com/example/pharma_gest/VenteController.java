package com.example.pharma_gest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.Random;


public class VenteController implements Initializable{

    @FXML
    private TableView<PharmaVente.medicaments> affichageMedicaments;
    @FXML
    private TableColumn<PharmaVente.medicaments, Integer> id;
    @FXML
    private TableColumn<PharmaVente.medicaments, String> name;
    @FXML
    private TableColumn<PharmaVente.medicaments, Integer> qty;
    @FXML
    private TableColumn<PharmaVente.medicaments, Float> price;
    @FXML
    private TextField quantityInput;
    private List<String> receiptList = new ArrayList<>();
    @FXML
    private TextArea receipt;
    @FXML
    private TextField selectedItem;

    private int receiptTotal;
    private int totalMed;
    private int quantiteMed;
    private String selectedNom;
    private int selectedPrice;

    private List<String> nomMed = new ArrayList<>();
    private List<Integer> qteMed;
    private List<Float> prxMed;
    private List<Integer> ttlMed;


    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    ObservableList<PharmaVente.medicaments> medicamentsList = FXCollections.observableArrayList();

    private void refreshTable() throws SQLException {
        medicamentsList.clear();
        query = "SELECT * FROM medicaments";
        preparedStatement = connection.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("id_medic");
            String name = resultSet.getString("name");
            Integer quantity = resultSet.getInt("quantity");
            float price = resultSet.getFloat("price");

            medicamentsList.add(new PharmaVente.medicaments(id, quantity, name, price));}
        affichageMedicaments.setItems(medicamentsList);}

    private void loadData() throws SQLException {
        connection = DatabaseConnection.getConnection();
        refreshTable();
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));}

    private void profileDisplay(ActionEvent event){
        query = "SELECT * FROM useraccounts";}


    @FXML
    public void InputValue(ActionEvent event){
        Button clickedButton = (Button) event.getSource();
        String quantity = quantityInput.getText();
        String quantityMed = quantity + clickedButton.getText();

        quantityInput.setText(quantityMed);}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            nomMed = new ArrayList<>();
            qteMed = new ArrayList<>();
            prxMed = new ArrayList<>();
            ttlMed = new ArrayList<>();
            loadData();
            setupRowClickListener();
        } catch (SQLException e) {
            e.printStackTrace();}}

    private void setupRowClickListener() {
        affichageMedicaments.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                PharmaVente.medicaments selectedMedicament = newSelection;
                String selectedNom = selectedMedicament.getName();
                Integer selectedQty = selectedMedicament.getQty();
                float selectedPrice = selectedMedicament.getPrice();

                selectedItem.setText("Nom: " + selectedNom + ", Mg: " + ", Prix: " + selectedPrice);}});}


    //Bouttons ajouter, supprimer, vider et imprimer

    @FXML
    public void addReceipt(ActionEvent event) {
        try {
            PharmaVente.medicaments selectedMedicament = affichageMedicaments.getSelectionModel().getSelectedItem();
            if (selectedMedicament == null) {
                return;}

            selectedNom = selectedMedicament.getName();
            selectedPrice = (int) selectedMedicament.getPrice();

            String quantity = quantityInput.getText().trim();
            int quantityValue = Integer.parseInt(quantity);
            int total = (int) (quantityValue * selectedPrice);

            String receiptString = selectedNom + "  " + "                                 " + selectedPrice + "x" + quantityValue ;
            receiptList.add(receiptString);
            receiptList.add("                                                              total: " + total + "rs");
            receiptTotal+= total;

            nomMed.add(selectedNom);
            prxMed.add((float) selectedPrice);
            ttlMed.add(total);
            qteMed.add(quantityValue);

            updateReceiptTextArea();
            quantityInput.clear();
        } catch (NumberFormatException e) {
            e.printStackTrace();}}

    private void updateReceiptTextArea() {
        receipt.clear();
        receipt.appendText
                ("                       PHARMAGEST" + "\n" + "      *** La Pharmacie qui prend soin de vous ***\n" + " --------------------------------------------------------\n");
        for (String receiptString : receiptList) {
            receipt.appendText(receiptString + "\n");}}

    @FXML
    private void afficherTotal(ActionEvent event){
        receipt.appendText(" --------------------------------------------------------\n" + "                     " +"TOTAL: " + receiptTotal + "rs\n");}

    public void printReceipt(ActionEvent event)throws SQLException{
        Random rand = new Random();
        int receiptNumber = rand.nextInt(999999);

        query = "INSERT INTO recu (total, vendeur, numero) VALUES (?, 'maeva',?)";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, receiptTotal);
        preparedStatement.setInt(2, receiptNumber);
        preparedStatement.executeUpdate();

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        receipt.appendText("\n printed on the " + date + "\n"+ " @ " + time);

        for (int i = 0; i < nomMed.size(); i++){
            String item1 = nomMed.get(i);
            int item2 = qteMed.get(i);
            int item3 = ttlMed.get(i);
            float item4 = prxMed.get(i);
            try {
                query = "INSERT INTO recu_details (nom_medicament, prix_medicament, quantite_medicament, total, id_recu) VALUES (?, ?, ?, ?,?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, nomMed.get(i));
                preparedStatement.setFloat(2, prxMed.get(i));
                preparedStatement.setInt(3, qteMed.get(i));
                preparedStatement.setInt(4, ttlMed.get(i));
                preparedStatement.setInt(5, receiptNumber);
                preparedStatement.executeUpdate();}
            finally {
                if (preparedStatement != null) {
                    preparedStatement.close();}}}}

    @FXML
    public void switchSceneExit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
            Parent nextSceneRoot = loader.load();
            Scene nextScene = new Scene(nextSceneRoot);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(nextScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();}}

    FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));

    @FXML
    public void logOut(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent nextSceneRoot = loader.load();
            Scene nextScene = new Scene(nextSceneRoot);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(nextScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();}}

    FXMLLoader logOut = new FXMLLoader(getClass().getResource("login.fxml"));}


