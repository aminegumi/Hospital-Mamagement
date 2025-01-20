package org.clinique.dao;



import org.clinique.database.DatabaseUtil;
import org.clinique.metier.Medecin;
import org.clinique.metier.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedecinDAO {

    public static void addMedecin(Medecin medecin) {
        String query = "INSERT INTO medecin (nom, prenom, specialite, photo_profil, email, password, num_tel, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, medecin.getNom());
            statement.setString(2, medecin.getPrenom());
            statement.setString(3, medecin.getSpecialite());
            statement.setString(4, medecin.getPhotoProfil());
            statement.setString(5, medecin.getEmail());
            statement.setString(6, medecin.getPassword());
            statement.setString(7, medecin.getNumTel());
            statement.setString(8, medecin.getStatus().name());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    medecin.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Medecin getMedecinById(int id) {
        String query = "SELECT * FROM medecin WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Medecin(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("specialite"),
                        resultSet.getString("photo_profil"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("num_tel"),
                        Status.valueOf(resultSet.getString("status"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Medecin> getAllMedecins() {
        List<Medecin> medecins = new ArrayList<>();
        String query = "SELECT * FROM medecin";
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Medecin medecin = new Medecin(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("specialite"),
                        resultSet.getString("photo_profil"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("num_tel"),
                        Status.valueOf(resultSet.getString("status"))
                );
                medecins.add(medecin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medecins;
    }

    public static void updateMedecin(Medecin medecin) {
        String query = "UPDATE medecin SET nom = ?, prenom = ?, specialite = ?, photo_profil = ?, email = ?, password = ?, num_tel = ?, status = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, medecin.getNom());
            statement.setString(2, medecin.getPrenom());
            statement.setString(3, medecin.getSpecialite());
            statement.setString(4, medecin.getPhotoProfil());
            statement.setString(5, medecin.getEmail());
            statement.setString(6, medecin.getPassword());
            statement.setString(7, medecin.getNumTel());
            statement.setString(8, medecin.getStatus().name());
            statement.setInt(9, medecin.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteMedecin(int id) {
        String query = "DELETE FROM medecin WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Log the ID to delete
            System.out.println("Attempting to delete Medecin with ID: " + id);

            // Execute the deletion
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();

            // Log the outcome
            System.out.println("Rows deleted: " + rowsAffected);

            // Optional: Explicit commit (if auto-commit is disabled)
            connection.commit();

            if (rowsAffected == 0) {
                System.err.println("No record found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
        }
    }


    public List<Medecin> getMedecinsBySpecialite(String specialite) {
        List<Medecin> medecins = new ArrayList<>();
        String query = "SELECT * FROM medecin WHERE specialite = ?";
        System.out.println("DELETING.......");
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, specialite);
            ResultSet resultSet = statement.executeQuery();
            System.out.println("DELETING......resultSet.");
            while (resultSet.next()) {
                Medecin medecin = new Medecin(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("specialite"),
                        resultSet.getString("photo_profil"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("num_tel"),
                        Status.valueOf(resultSet.getString("status"))
                );
                medecins.add(medecin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medecins;
    }

    public Medecin getMedecinByEmail(String email) {
        String query = "SELECT * FROM medecin WHERE email = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Medecin(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("specialite"),
                        resultSet.getString("photo_profil"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("num_tel"),
                        Status.valueOf(resultSet.getString("status"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<Medecin> getAllMedecinsForDisplay() {
        List<Medecin> medecins = new ArrayList<>();
        String query = "SELECT id, nom, prenom FROM medecin";
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Medecin medecin = new Medecin();
                medecin.setId(resultSet.getInt("id"));
                medecin.setNom(resultSet.getString("nom"));
                medecin.setPrenom(resultSet.getString("prenom"));
                medecins.add(medecin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medecins;
    }
}
