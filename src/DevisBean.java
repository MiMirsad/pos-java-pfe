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
public class DevisBean {
 
    
    
     private int NumeroDevis;
    private String DateDevis;
    private int NumeroClient;
    private String Prix;
    private String quantite;
    private int numeroProduit;

    public int getNumeroDevis() {
        return NumeroDevis;
    }

    public void setNumeroDevis(int NumeroDevis) {
        this.NumeroDevis = NumeroDevis;
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
    

    public String getDateDevis() {
        return DateDevis;
    }

    public void setDateDevis(String DateDevis) {
        this.DateDevis = DateDevis;
    }
public void ajouteDevis(DevisBean pst) throws SQLException {
    int nb = 0;
    try {
        String req = "INSERT INTO Devis (  NumClient,DateDevis) VALUES (?,?)";
        PreparedStatement ko = dbb.conx.prepareStatement(req);
       // ko.setString(1, pst.getId());
               ko.setInt(1, pst.getNumeroClient());
               ko.setString(2, pst.getDateDevis());
       
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
public void ajouteDeatilCom(DevisBean pst) throws SQLException {
    // Check if NumeroCommand is already inserted
//    String checkQuery = "SELECT COUNT(*) FROM DetailDevis WHERE id = ?";
//    PreparedStatement checkStatement = dbb.conx.prepareStatement(checkQuery);
//    checkStatement.setInt(1, pst.getNumeroDevis());
//    ResultSet checkResult = checkStatement.executeQuery();
//    checkResult.next();
//    int count = checkResult.getInt(1);
//    
//    if (count > 0) {
//        // If NumeroCommand already exists, show a message and return
//        JOptionPane.showMessageDialog(null, "NumeroCommand already exists.", "Duplicate NumeroCommand", JOptionPane.WARNING_MESSAGE);
//        return;
//    }

    // If NumeroCommand doesn't exist, proceed with the insertion
    int nb = 0;
    try {
        String req = "INSERT INTO DetailDevis (id, NumeroProduit, Quantité, Prix) VALUES (?, ?, ?, ?)";
        PreparedStatement ko = dbb.conx.prepareStatement(req);
        ko.setInt(1, pst.getNumeroDevis());
        ko.setInt(2, pst.getNumeroProduit());
        ko.setString(3, pst.getPrix());
        ko.setString(4, pst.getQuantite());
        nb = ko.executeUpdate(); // Execute the INSERT statement
          JOptionPane.showMessageDialog(null, "Details Command ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException e) {
        // Handle SQLException properly, e.g., print stack trace or log the error
        e.printStackTrace();
    }
}

public void modifierDevis(DevisBean st) {
    dbb db = dbb.getCon();
    int nb = 0;
    try {
        String req = "UPDATE Devis SET NumClient=?, DateDevis=? WHERE id=?";
        PreparedStatement pst = db.conx.prepareStatement(req);
        pst.setInt(1, st.getNumeroClient());
        pst.setString(2, st.getDateDevis());
        pst.setInt(3, st.getNumeroDevis());
        
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
     public void modifierDevisDetail(DevisBean st) {
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
        String req = "UPDATE DetailDevis SET NumeroProduit=?, Quantité=?, Prix=? WHERE id=?";
        PreparedStatement pst = db.conx.prepareStatement(req);
        pst.setInt(1, st.getNumeroProduit()); // Set NumeroProduit
        pst.setString(2, st.getQuantite()); 
        pst.setString(3, st.getPrix());       // Set Prix
          // Set Quantité
        pst.setInt(4, st.getNumeroDevis()); // Set NumeroCmd
        nb = pst.executeUpdate();
    } catch (SQLException e) {
        // Gérer l'exception SQLException correctement, par exemple, imprimer la trace de la pile ou enregistrer l'erreur
        e.printStackTrace();
    }

    if (nb == 1) {
        JOptionPane.showMessageDialog(null, "Détail de la commande bien modifié.", "Succès", JOptionPane.INFORMATION_MESSAGE);
    }
}
      public void SupDetailDevis(int IDCOMMAND) {
    int nb = 0;
    try {
        String req = "DELETE FROM DetailDevis WHERE id = ?";
        PreparedStatement pst = dbb.conx.prepareStatement(req);
        pst.setInt(1, IDCOMMAND);
        
        if (JOptionPane.showConfirmDialog(null, "Confirmer la suppression", "Suppression", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
            nb = pst.executeUpdate();
        }
    } catch (SQLException ex) {
        if (ex.getErrorCode() == (-1612)) {
            JOptionPane.showMessageDialog(null, "Impossible de supprimer l'enregistrement car il est déjà utilisé en réception ou en sortie.");
            System.out.println(ex.getErrorCode());
        }
    }
    if (nb == 1) {
        JOptionPane.showMessageDialog(null, "Command supprimé avec succès.", null, JOptionPane.INFORMATION_MESSAGE);
    }
} 
          public void SupDevis(int IDCOMMAND) {
        try {
            int nb = 0;
            
            String req = "DELETE FROM Devis WHERE id = ?";
            PreparedStatement pst = dbb.conx.prepareStatement(req);
            pst.setInt(1, IDCOMMAND);
            
            if (nb == 1) {
                JOptionPane.showMessageDialog(null, "Command supprimé avec succès.", null, JOptionPane.INFORMATION_MESSAGE);
            }   } catch (SQLException ex) {
            Logger.getLogger(DevisBean.class.getName()).log(Level.SEVERE, null, ex);
        }
} 














}
