package org.clinique.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminPanelController {
    @FXML
    private Label welcomeLabel;
    @FXML
    private VBox mainContent;

    @FXML
    private Button dashboardButton;
    @FXML
    private Button patientsButton;
    @FXML
    private Button medecinsButton;
    @FXML
    private Button rendezVousButton;
    @FXML
    private Button chambresButton;
    @FXML
    private Button dossierMedicalButton;
    @FXML
    private Button facturesButton;
    @FXML
    private Button infirmiersButton;
    @FXML
    private Button settingsButton;


    private Button selectedButton;

    @FXML
    private Button aiAssistantButton;

    @FXML
    private void initialize() {

        selectButton(dashboardButton);
        loadView("/org/clinique/DashboardView.fxml");
    }

    @FXML
    private void loadDashboard() {
        selectButton(dashboardButton);
        loadView("/org/clinique/DashboardView.fxml");
    }

    @FXML
    private void loadPatient() {
        selectButton(patientsButton);
        loadView("/org/clinique/PatientView.fxml");
    }

    @FXML
    private void loadMedecins() {
        selectButton(medecinsButton);
        loadView("/org/clinique/MedecinView.fxml");
    }

    @FXML
    private void loadRendezVous() {
        selectButton(rendezVousButton);
        loadView("/org/clinique/RendezVousView.fxml");
    }

    @FXML
    private void loadChambres() {
        selectButton(chambresButton);
        loadView("/org/clinique/ChambreView.fxml");
    }

    @FXML
    private void loadDossierMedical() {
        selectButton(dossierMedicalButton);
        loadView("/org/clinique/DossierMedicalView.fxml");
    }

    @FXML
    private void loadFactures() {
        selectButton(facturesButton);
        loadView("/org/clinique/FactureView.fxml");
    }

    @FXML
    private void loadInfirmiers() {
        selectButton(infirmiersButton);
        loadView("/org/clinique/InfirmierView.fxml");
    }

    @FXML
    private void loadSettings() {
        selectButton(settingsButton);
        loadView("/org/clinique/SettingsView.fxml");
    }

    @FXML
    private void loadAiAssistant() {
        selectButton(aiAssistantButton);
        loadView("/org/clinique/AiAssistantView.fxml");
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/clinique/AuthenticationView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) mainContent.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the login view.");
        }
    }

    private void loadView(String fxmlPath) {
        try {
            System.out.println("Loading FXML: " + fxmlPath);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();

            mainContent.getChildren().clear();
            mainContent.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the view: " + fxmlPath);
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setWelcomeMessage(String username) {
        welcomeLabel.setText("Merhba Bik " + username + "!");
    }


    private void selectButton(Button button) {
        if (selectedButton != null) {
            selectedButton.setStyle("-fx-background-color: #05696b; -fx-text-fill: white;");
        }
        selectedButton = button;
        selectedButton.setStyle("-fx-background-color: #034647; -fx-text-fill: white;");
        selectedButton.setPrefWidth(150);
        selectedButton.setPrefHeight(40);
    }
}