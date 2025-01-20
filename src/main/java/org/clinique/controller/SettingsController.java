package org.clinique.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.clinique.dao.AdministrateurDAO;
import org.clinique.metier.Administrateur;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ImageView photoProfilView;

    @FXML
    private TableView<Administrateur> administrateurTable;
    @FXML
    private TableColumn<Administrateur, Integer> idColumn;
    @FXML
    private TableColumn<Administrateur, String> usernameColumn;
    @FXML
    private TableColumn<Administrateur, String> passwordColumn;
    @FXML
    private TableColumn<Administrateur, String> photoProfilColumn;

    @FXML
    private TextField searchField;

    private ObservableList<Administrateur> administrateurList = FXCollections.observableArrayList();
    private String photoProfilPath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
            passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
            photoProfilColumn.setCellValueFactory(new PropertyValueFactory<>("photoProfil"));

            loadAdministrateurs();
            setupSearch();

            administrateurTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedAdministrateur) -> {
                if (selectedAdministrateur != null) {
                    populateFields(selectedAdministrateur);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateFields(Administrateur administrateur) {
        usernameField.setText(administrateur.getUsername());
        passwordField.setText(administrateur.getPassword());
        // Assuming 'photoProfil' is a file path, set the image in the ImageView
        if (administrateur.getPhotoProfil() != null) {
            File file = new File(administrateur.getPhotoProfil());
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                photoProfilView.setImage(image);
            }
        }
        photoProfilPath = administrateur.getPhotoProfil();
    }


    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTable(newValue);
        });
    }

    private void filterTable(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            administrateurTable.setItems(administrateurList);
            return;
        }

        ObservableList<Administrateur> filteredList = FXCollections.observableArrayList();
        for (Administrateur administrateur : administrateurList) {
            if (administrateur.getUsername().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(administrateur);
            }
        }
        administrateurTable.setItems(filteredList);
    }

    @FXML
    private void handleExportToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(administrateurTable.getScene().getWindow());

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("ID,Username,Password,Photo de Profil");
                for (Administrateur administrateur : administrateurTable.getItems()) {
                    writer.println(
                            administrateur.getId() + "," +
                                    administrateur.getUsername() + "," +
                                    administrateur.getPassword() + "," +
                                    administrateur.getPhotoProfil()
                    );
                }
                showAlert("Success", "Data exported to CSV successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to export data to CSV.");
            }
        }
    }


    private void loadAdministrateurs() {
        administrateurList.clear();
        administrateurList.addAll(AdministrateurDAO.getAllAdministrateurs());
        administrateurTable.setItems(administrateurList);
    }

    @FXML
    private void handleAddAdministrateur() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String photoProfil = photoProfilPath;

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        Administrateur administrateur = new Administrateur();
        administrateur.setUsername(username);
        administrateur.setPassword(password);
        administrateur.setPhotoProfil(photoProfil);

        AdministrateurDAO.addAdministrateur(administrateur);
        loadAdministrateurs();
        clearFields();
    }

    @FXML
    private void handleUpdateAdministrateur() {
        Administrateur selectedAdministrateur = administrateurTable.getSelectionModel().getSelectedItem();
        if (selectedAdministrateur == null) {
            showAlert("Erreur", "Veuillez sélectionner un administrateur à modifier.");
            return;
        }

        selectedAdministrateur.setUsername(usernameField.getText());
        selectedAdministrateur.setPassword(passwordField.getText());
        selectedAdministrateur.setPhotoProfil(photoProfilPath);

        AdministrateurDAO.updateAdministrateur(selectedAdministrateur);
        loadAdministrateurs();
        clearFields();
    }

    @FXML
    private void handleDeleteAdministrateur() {
        Administrateur selectedAdministrateur = administrateurTable.getSelectionModel().getSelectedItem();
        if (selectedAdministrateur == null) {
            showAlert("Erreur", "Veuillez sélectionner un administrateur à supprimer.");
            return;
        }

        AdministrateurDAO.deleteAdministrateur(selectedAdministrateur.getId());
        loadAdministrateurs();
        clearFields();
    }

    @FXML
    private void handleUploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une photo de profil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(photoProfilView.getScene().getWindow());
        if (selectedFile != null) {
            // Get the current directory of the application
            String currentDirectory = System.getProperty("user.dir");

            // Define the path to the 'images' folder within the current directory
            File imagesDirectory = new File(currentDirectory, "images");

            // Check if the 'images' directory exists, if not, create it
            if (!imagesDirectory.exists()) {
                imagesDirectory.mkdirs(); // Create the 'images' folder if it doesn't exist
            }

            // Create a new file path for the image in the 'images' folder
            File newFile = new File(imagesDirectory, selectedFile.getName());

            try {
                // Copy the selected file to the 'images' folder
                Files.copy(selectedFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Set the image for the photo profile view
                Image image = new Image(newFile.toURI().toString());
                photoProfilView.setImage(image);

                // Save the path of the new file in the photoProfilPath variable
                photoProfilPath = newFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to upload photo.");
            }
        }
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        photoProfilView.setImage(null);
        photoProfilPath = null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}