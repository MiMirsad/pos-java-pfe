
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Administrator
 */
public class BonLivCBeans {

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getDateDevis() {
        return DateDevis;
    }

    public void setDateDevis(String DateDevis) {
        this.DateDevis = DateDevis;
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
    
    
     private int Id;
    private String DateDevis;
    private int NumeroClient;
    private String Prix;
    private String quantite;
    private int numeroProduit;
    
    public void ajouteBonLC(BonLivCBeans pst) throws SQLException {
    int nb = 0;
    try {
        String req = "INSERT INTO BonLC (  DateBon,NumClient) VALUES (?,?)";
        PreparedStatement ko = dbb.conx.prepareStatement(req);
       // ko.setString(1, pst.getId());
        ko.setString(1, pst.getDateDevis());
               ko.setInt(2, pst.getNumeroClient());
              
       
        nb = ko.executeUpdate(); // Execute the INSERT statement
        
        if (nb > 0) {
            JOptionPane.showMessageDialog(null, "Bon ajouté avec succès.", null, JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du Bon.", null, JOptionPane.ERROR_MESSAGE);
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
public void ajouteDetailBonLC(BonLivCBeans pst) throws SQLException {
       // Check if NumeroCommand is already inserted
//    String checkQuery = "SELECT COUNT(*) FROM DetailBonLC WHERE id = ?";
//    PreparedStatement checkStatement = dbb.conx.prepareStatement(checkQuery);
//    checkStatement.setInt(1, pst.getId());
//    ResultSet checkResult = checkStatement.executeQuery();
//    checkResult.next();
//    int count = checkResult.getInt(1);
//
//    if (count > 0) {
//        // If NumeroCommand already exists, show a message and return
//        JOptionPane.showMessageDialog(null, "Numero de Bon already exists.", "Duplicate NumeroCommand", JOptionPane.WARNING_MESSAGE);
//        return;
//    }

    // Ask for confirmation before proceeding with the insertion
    int confirm = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment ajouter ces détails de bon ?", "Confirmer l'insertion", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) {
        return; // If the user does not confirm, do not proceed
    }

    // If NumeroCommand doesn't exist and user confirms, proceed with the insertion
    int nb = 0;
    try {
        String req = "INSERT INTO DetailBonLC (id, NumeroProduit, Prix, Quantité) VALUES (?, ?, ?, ?)";
        PreparedStatement ko = dbb.conx.prepareStatement(req);
        ko.setInt(1, pst.getId());
        ko.setInt(2, pst.getNumeroProduit());
        ko.setString(3, pst.getPrix());
        ko.setString(4, pst.getQuantite());
        nb = ko.executeUpdate(); // Execute the INSERT statement
        JOptionPane.showMessageDialog(null, "Details De Bon ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException e) {
        // Handle SQLException properly, e.g., print stack trace or log the error
        e.printStackTrace();
    }
}

public void modifierBonLiv(BonLivCBeans st) {
    dbb db = dbb.getCon();
    int nb = 0;
    try {
        String req = "UPDATE BonLC SET NumClient=?, DateBon=? WHERE id=?";
        PreparedStatement pst = db.conx.prepareStatement(req);
        pst.setInt(1, st.getNumeroClient());
        pst.setString(2, st.getDateDevis());
        pst.setInt(3, st.getId());
        
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
     public void modifierDetailBon(BonLivCBeans st) {
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
        // Ensure that the SQL query matches the expected behavior
        String req = "UPDATE DetailBonLC SET Prix=?, Quantité=? WHERE id=? AND NumeroProduit=?";
        PreparedStatement pst = db.conx.prepareStatement(req);
        pst.setString(1, st.getPrix());    // Set Prix
        pst.setString(2, st.getQuantite()); // Set Quantité
        pst.setInt(3, st.getId());          // Set id
        pst.setInt(4, st.getNumeroProduit()); // Set NumeroProduit

        // Execute the update and check the number of affected rows
        nb = pst.executeUpdate();
    } catch (SQLException e) {
        // Gérer l'exception SQLException correctement, par exemple, imprimer la trace de la pile ou enregistrer l'erreur
        e.printStackTrace();
    }

    if (nb == 1) {
        JOptionPane.showMessageDialog(null, "Détail de la commande bien modifié.", "Succès", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(null, "Modification échouée. Vérifiez les valeurs fournies.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}

      public void SupDetailBon(int IDBON) {
    int nb = 0;
    try {
        String req = "DELETE FROM DetailBonLC WHERE id = ?";
        PreparedStatement pst = dbb.conx.prepareStatement(req);
        pst.setInt(1, IDBON);
        
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
        JOptionPane.showMessageDialog(null, "Bon supprimé avec succès.", null, JOptionPane.INFORMATION_MESSAGE);
    }
} 
          public void SupBonl(int IDBON) {
        try {
            int nb = 0;
            
            String req = "DELETE FROM BonLC WHERE id = ?";
            PreparedStatement pst = dbb.conx.prepareStatement(req);
            pst.setInt(1, IDBON);
            
            if (nb == 1) {
                JOptionPane.showMessageDialog(null, "Bon supprimé avec succès.", null, JOptionPane.INFORMATION_MESSAGE);
            }   } catch (SQLException ex) {
            Logger.getLogger(DevisBean.class.getName()).log(Level.SEVERE, null, ex);
        }
} 


    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
