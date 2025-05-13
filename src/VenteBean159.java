import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PC
 */
public class VenteBean159 {
 
    
    
private int NumeroVent;
    private String DateVent;
    private int NumeroClient;
    private String Prix;
    private String quantite;
    private int numeroProduit;

    public int getNumeroVent() {
        return NumeroVent;
    }

    public void setNumeroVent(int NumeroVent) {
        this.NumeroVent = NumeroVent;
    }

    public String getDateVent() {
        return DateVent;
    }

    public void setDateVent(String DateVent) {
        this.DateVent = DateVent;
    }

    public int getNumeroClient() {
        return NumeroClient;
    }

    public void setNumeroClient(int NumeroClient) {
        this.NumeroClient = NumeroClient;
    }

    public String getPrix() {
        return Prix;
    }

    public void setPrix(String Prix) {
        this.Prix = Prix;
    }

    public String getQuantite() {
        return quantite;
    }

    public void setQuantite(String quantite) {
        this.quantite = quantite;
    }

    public int getNumeroProduit() {
        return numeroProduit;
    }

    public void setNumeroProduit(int numeroProduit) {
        this.numeroProduit = numeroProduit;
    }
    
    
    

public void ajouteVent(VenteBean159 pst) throws SQLException {
    int nb = 0;
    try {
        String req = "INSERT INTO Vent (  NumClient,DateVent) VALUES (?,?)";
        PreparedStatement ko = dbb.conx.prepareStatement(req);
       // ko.setString(1, pst.getId());
               ko.setInt(1, pst.getNumeroClient());
               ko.setString(2, pst.getDateVent());
       
        nb = ko.executeUpdate(); // Execute the INSERT statement
        
        if (nb > 0) {
            JOptionPane.showMessageDialog(null, "Devis ajouté avec succès.", null, JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du Produit.", null, JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        if (ex.getErrorCode() == -1605) {
            JOptionPane.showMessageDialog(null, "Code Fournisseur déjà existant", null, JOptionPane.INFORMATION_MESSAGE);
            System.out.println(ex.getErrorCode() + " " + ex.getMessage());
        } else {
            JOptionPane.showMessageDialog(null, "Erreur SQL: " + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }
}
public void ajouteDeatilCom(VenteBean159 pst) throws SQLException {
//    // Check if NumeroCommand is already inserted
//    String checkQuery = "SELECT COUNT(*) FROM FactueVent     WHERE id = ?";
//    PreparedStatement checkStatement = dbb.conx.prepareStatement(checkQuery);
//    checkStatement.setInt(1, pst.getNumeroVent());
//    ResultSet checkResult = checkStatement.executeQuery();
//    checkResult.next();
//    int count = checkResult.getInt(1);
//
//    if (count > 0) {
//         If NumeroCommand already exists, show a message and return
//        JOptionPane.showMessageDialog(null, "NumeroCommand already exists.", "Duplicate NumeroCommand", JOptionPane.WARNING_MESSAGE);
//        return;
//    }

    // Check the current stock quantity
    String stockQuery = "SELECT QuantitéEnStock FROM Produit WHERE NumeroProduit = ?";
    PreparedStatement stockStatement = dbb.conx.prepareStatement(stockQuery);
    stockStatement.setInt(1, pst.getNumeroProduit());
    ResultSet stockResult = stockStatement.executeQuery();

    if (stockResult.next()) {
        int currentStock = stockResult.getInt("QuantitéEnStock");
        int orderedQuantity = Integer.parseInt(pst.getQuantite());

        if (currentStock < orderedQuantity) {
            JOptionPane.showMessageDialog(null, "Insufficient stock for the product.", "Stock Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Proceed with the insertion
        int nb = 0;
        try {
            String req = "INSERT INTO FactueVent (id, NumeroProduit, Prix, Quantité) VALUES (?, ?, ?, ?)";
            PreparedStatement ko = dbb.conx.prepareStatement(req);
            ko.setInt(1, pst.getNumeroVent());
            ko.setInt(2, pst.getNumeroProduit());
            ko.setString(3, pst.getQuantite());
            ko.setString(4, pst.getPrix());
            nb = ko.executeUpdate(); // Execute the INSERT statement

            // Update the stock quantity
            String updateStockQuery = "UPDATE Produit SET QuantitéEnStock = QuantitéEnStock - ? WHERE NumeroProduit = ?";
            PreparedStatement updateStockStatement = dbb.conx.prepareStatement(updateStockQuery);
            updateStockStatement.setInt(1, orderedQuantity);
            updateStockStatement.setInt(2, pst.getNumeroProduit());
            updateStockStatement.executeUpdate();

            JOptionPane.showMessageDialog(null, "Vente Facture ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            // Handle SQLException properly, e.g., print stack trace or log the error
            e.printStackTrace();
        }
    } else {
        JOptionPane.showMessageDialog(null, "Product not found.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

public void modifierDevis(VenteBean159 st) {
    dbb db = dbb.getCon();
    int nb = 0;
    try {
        String req = "UPDATE Vent SET NumClient=?, DateVent=? WHERE id=?";
        PreparedStatement pst = db.conx.prepareStatement(req);
        pst.setInt(1, st.getNumeroClient());
        pst.setString(2, st.getDateVent());
        pst.setInt(3, st.getNumeroVent());
        
        // Debug print statement to trace the query execution
        System.out.println("Executing update: " + pst.toString());
        
        nb = pst.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    if (nb == 1) {
        JOptionPane.showMessageDialog(null, "Devis bien modifiée", null, JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(null, "Échec de la modification du devis.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
public void modifierVentDetail(VenteBean159 st) {
    dbb db = dbb.getCon();
    int nb = 0;

    // Vérifier si NumeroProduit, Prix et Quantité sont vides
    if (st.getNumeroProduit() == 0 || st.getPrix().isEmpty() || st.getQuantite().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs.", "Champs obligatoires", JOptionPane.ERROR_MESSAGE);
        return; // Sortir de la méthode si l'un des champs est vide
    }

    // Vérifier si Prix est un entier
    try {
        Integer.parseInt(st.getPrix());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Le prix doit être un nombre entier.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
        return; // Sortir de la méthode si Prix n'est pas un entier
    }

    // Vérifier si Quantité est un entier
    try {
        Integer.parseInt(st.getQuantite());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "La quantité doit être un nombre entier.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
        return; // Sortir de la méthode si Quantité n'est pas un entier
    }

    try {
        String req = "UPDATE FactueVent SET Quantité=?, Prix=? WHERE id=? AND NumeroProduit=?";
        PreparedStatement pst = db.conx.prepareStatement(req);

        // Set Quantité
        pst.setInt(1, Integer.parseInt(st.getQuantite())); 

        // Set Prix
        pst.setInt(2, Integer.parseInt(st.getPrix())); 

        // Set id (NumeroVent)
        pst.setInt(3, st.getNumeroVent());

        // Set NumeroProduit
        pst.setInt(4, st.getNumeroProduit());

        nb = pst.executeUpdate();
    } catch (SQLException e) {
        // Gérer l'exception SQLException correctement, par exemple, imprimer la trace de la pile ou enregistrer l'erreur
        e.printStackTrace();
    }

    if (nb == 1) {
        JOptionPane.showMessageDialog(null, "Détail de la commande bien modifié.", "Succès", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(null, "Aucune modification effectuée. Vérifiez les critères de recherche.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}

     public void SupDetailVent(int IDCOMMAND) {
    int nb = 0;
    try {
        String req = "DELETE FROM FactueVent WHERE id = ?";
        PreparedStatement pst = dbb.conx.prepareStatement(req);
        pst.setInt(1, IDCOMMAND);

        if (JOptionPane.showConfirmDialog(null, "Confirmer la suppression", "Suppression", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            nb = pst.executeUpdate();
        }
    } catch (SQLException ex) {
        if (ex.getErrorCode() == (-1612)) {
            JOptionPane.showMessageDialog(null, "Impossible de supprimer l'enregistrement car il est déjà utilisé en réception ou en sortie.");
            System.out.println(ex.getErrorCode());
        } else {
            Logger.getLogger(VenteBean159.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    if (nb == 1) {
        JOptionPane.showMessageDialog(null, "Command supprimé avec succès.", null, JOptionPane.INFORMATION_MESSAGE);
    }
}

public void SupVent(int IDCOMMAND) {
    int nb = 0;
    try {
        String req = "DELETE FROM Vent WHERE id = ?";
        PreparedStatement pst = dbb.conx.prepareStatement(req);
        pst.setInt(1, IDCOMMAND);

        if (JOptionPane.showConfirmDialog(null, "Confirmer la suppression", "Suppression", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            nb = pst.executeUpdate();
        }
    } catch (SQLException ex) {
        Logger.getLogger(VenteBean159.class.getName()).log(Level.SEVERE, null, ex);
    }
    if (nb == 1) {
        JOptionPane.showMessageDialog(null, "Command supprimé avec succès.", null, JOptionPane.INFORMATION_MESSAGE);
    }
}

   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    }


