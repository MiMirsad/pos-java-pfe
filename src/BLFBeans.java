
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
public class BLFBeans {

    public int getNumF() {
        return NumF;
    }

    public void setNumF(int NumF) {
        this.NumF = NumF;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatee() {
        return datee;
    }

    public void setDatee(String datee) {
        this.datee = datee;
    }

    public int getProduit() {
        return produit;
    }

    public void setProduit(int produit) {
        this.produit = produit;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getQte() {
        return qte;
    }

    public void setQte(String qte) {
        this.qte = qte;
    }
      public int NumF;
public int id;
    private String datee;
           private int produit;
         private String prix;
          private String qte;


public void ajouteBlf(BLFBeans pst) throws SQLException {
    int nb = 0;
    try {
        String req = "INSERT INTO BLFour (  DateBLf,Nfour) VALUES (?,?)";
        PreparedStatement ko = dbb.conx.prepareStatement(req);
       // ko.setString(1, pst.getId());
        ko.setString(1, pst.getDatee());
        ko.setInt(2, pst.getNumF());
       
        nb = ko.executeUpdate(); // Execute the INSERT statement
        
        if (nb > 0) {
            JOptionPane.showMessageDialog(null, "Bon Livraison Fournisseur ajouté avec succès.", null, JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du Bon Livraison .", null, JOptionPane.ERROR_MESSAGE);
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

public void modifierBONliv(BLFBeans st) {
    dbb db = dbb.getCon();
    int nb = 0;
    try {
        String req = "UPDATE BLFour SET DateBLf=?, Nfour=? WHERE ID=?";
        PreparedStatement pst = db.conx.prepareStatement(req);
        pst.setString(1, st.getDatee()); // Set the DateFacture
        pst.setInt(2, st.getNumF());     // Set the Nfour
        pst.setInt(3, st.getId());       // Set the ID
        nb = pst.executeUpdate();
    } catch (SQLException e) {
        // Handle the SQLException properly, e.g., print stack trace or log the error
        e.printStackTrace();
    }
    if (nb == 1) {
        JOptionPane.showMessageDialog(null, "Facture d'achat bien modifiée", null, JOptionPane.INFORMATION_MESSAGE);
    }
}
public void ajouteBONdetails(BLFBeans pst) throws SQLException {
    // Check if the ID already exists in the database
    try{
//    String checkQuery = "SELECT COUNT(*) FROM DetailBonLV WHERE ID = ?";
//    PreparedStatement checkStatement = dbb.conx.prepareStatement(checkQuery);
//    checkStatement.setInt(1, pst.getId());
//    ResultSet checkResult = checkStatement.executeQuery();
//    checkResult.next();
//    int count = checkResult.getInt(1);
//    
//    if (count > 0) {
//        // If the ID already exists, show a message
//        JOptionPane.showMessageDialog(null, "L'ID existe déjà.", "Duplicate ID", JOptionPane.WARNING_MESSAGE);
//        return;
//    }
    
    // Prompt the user for confirmation before insertion
    int option = JOptionPane.showConfirmDialog(null, "Voulez-vous insérer cet achat?", "Confirmation", JOptionPane.YES_NO_OPTION);
    if (option != JOptionPane.YES_OPTION) {
        return; // Exit the method if the user selects "No" or closes the dialog
    }
    
    // Check if PrixAchat is an integer
    try {
        Integer.parseInt(pst.getPrix());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Le prix d'achat doit être un nombre entier.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method if PrixAchat is not an integer
    }
    
    // Check if QteAchat is an integer
    try {
        Integer.parseInt(pst.getQte());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "La quantité d'achat doit être un nombre entier.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method if QteAchat is not an integer
    }
    
    // If all checks pass, proceed with the insertion
    String req = "INSERT INTO DetailBonLV (ID, NumPro, PrixAchat, QteAchat) VALUES (?, ?, ?, ?)";
    PreparedStatement ko = dbb.conx.prepareStatement(req);
    ko.setInt(1, pst.getId());
    ko.setInt(2, pst.getProduit());
    ko.setString(3, pst.getPrix());
    ko.setString(4, pst.getQte());
    int nb = ko.executeUpdate(); // Execute the INSERT statement
  if (nb > 0) {
            JOptionPane.showMessageDialog(null, "Produit ajouté avec succès.", null, JOptionPane.INFORMATION_MESSAGE);
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
public void modifierDetailBon(BLFBeans st) {
    dbb db = dbb.getCon();
    int nb = 0;

    // Validate that Prix and Quantité are valid integers
    try {
        Integer.parseInt(st.getPrix()); // Ensure PrixAchat is an integer
        Integer.parseInt(st.getQte());  // Ensure QteAchat is an integer
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Prix et Quantité doivent être des nombres entiers.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method if any of the fields are not integers
    }

    // Debug print statements
    System.out.println("Updating DetailAchat with ID: " + st.getId() + ", NumPro: " + st.getProduit());
    System.out.println("Prix: " + st.getPrix() + ", Quantité: " + st.getQte());

    // Prompt the user for confirmation before modification
    int option = JOptionPane.showConfirmDialog(null, "Voulez-vous modifier ce détail de bon?", "Confirmation", JOptionPane.YES_NO_OPTION);
    if (option != JOptionPane.YES_OPTION) {
        return; // Exit the method if the user selects "No" or closes the dialog
    }

    try {
        String req = "UPDATE DetailBonLV SET PrixAchat=?, QteAchat=? WHERE ID=? AND NumPro=?";
        PreparedStatement pst = db.conx.prepareStatement(req);

        pst.setString(1, st.getPrix());    // Set PrixAchat
        pst.setString(2, st.getQte());     // Set QteAchat
        pst.setInt(3, st.getId());         // Set ID
        pst.setInt(4, st.getProduit());    // Set NumPro

        // Execute the update and check the number of affected rows
        nb = pst.executeUpdate();
        System.out.println("Number of rows affected: " + nb);
    } catch (SQLException e) {
        // Handle SQL exceptions properly
        e.printStackTrace();
    }

    if (nb == 1) {
        JOptionPane.showMessageDialog(null, "Détail de la facture d'achat bien modifié.", "Succès", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(null, "Modification échouée. Vérifiez les valeurs fournies.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}


  public void SupBonDetail(int IDBN) {
    int nb = 0;
    try {
        String req = "DELETE FROM DetailBonLV WHERE ID = ?";
        PreparedStatement pst = dbb.conx.prepareStatement(req);
        pst.setInt(1, IDBN);
        
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
          public void SupBon(int IDBN) {
        try {
            int nb = 0;
            
            String req = "DELETE FROM BLFour WHERE ID = ?";
            PreparedStatement pst = dbb.conx.prepareStatement(req);
            pst.setInt(1, IDBN);
            
            if (nb == 1) {
                JOptionPane.showMessageDialog(null, "Bon supprimé avec succès.", null, JOptionPane.INFORMATION_MESSAGE);
            }   } catch (SQLException ex) {
            Logger.getLogger(BLFBeans.class.getName()).log(Level.SEVERE, null, ex);
        }
} 















}
