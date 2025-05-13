
import java.sql.PreparedStatement;
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
public class AchatBeans {
    public int NumF;
public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumF() {
        return NumF;
    }

    public void setNumF(int NumF) {
        this.NumF = NumF;
    }

   
     private String datee;

    public String getDatee() {
        return datee;
    }

    public void setDatee(String datee) {
        this.datee = datee;
    }
      private int numero;

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
        private int produit;
         private String prix;
          private String qte;

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

public void modifierAchat(AchatBeans st) {
    dbb db = dbb.getCon();
    int nb = 0;
    try {
        String req = "UPDATE FactureAchat SET DateFacture=?, Nfour=? WHERE ID=?";
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

public void modifierDetailAchat(AchatBeans st) {
    dbb db = dbb.getCon();
    int nb = 0;

    // Check if NumPro, PrixAchat, and QteAchat are empty or not integers
    try {
        Integer.parseInt(st.getPrix());    // Check if PrixAchat is an integer
        Integer.parseInt(st.getQte());     // Check if QteAchat is an integer
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "NumPro, PrixAchat et QteAchat doivent être des entiers.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method if any of the fields are not integers
    }

    // Prompt the user for confirmation before modification
    int option = JOptionPane.showConfirmDialog(null, "Voulez-vous modifier ce détail d'achat?", "Confirmation", JOptionPane.YES_NO_OPTION);
    if (option != JOptionPane.YES_OPTION) {
        return; // Exit the method if the user selects "No" or closes the dialog
    }

    try {
        String req = "UPDATE DetailAchat SET PrixAchat=?, QteAchat=? WHERE ID=? AND NumPro=?";
        PreparedStatement pst = db.conx.prepareStatement(req);
        pst.setString(1, st.getPrix());    // Set the PrixAchat
        pst.setString(2, st.getQte());     // Set the QteAchat
        pst.setInt(3, st.getId());         // Set the ID
        pst.setInt(4, st.getProduit());    // Set the NumPro
        nb = pst.executeUpdate();
    } catch (SQLException e) {
        // Handle the SQLException properly, e.g., print stack trace or log the error
        e.printStackTrace();
    }

    if (nb == 1) {
        JOptionPane.showMessageDialog(null, "Détail de la facture d'achat bien modifié.", "Succès", JOptionPane.INFORMATION_MESSAGE);
    }
}


        public void SupAChat(int IDFACTUR) {
    int nb = 0;
    try {
        String req = "DELETE FROM DetailAchat WHERE id = ?";
        PreparedStatement pst = dbb.conx.prepareStatement(req);
        pst.setInt(1, IDFACTUR);
        
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
        JOptionPane.showMessageDialog(null, "Client supprimé avec succès.", null, JOptionPane.INFORMATION_MESSAGE);
    }
} 
          public void SupAChatFacture(int IDFACTUR) {
        try {
            int nb = 0;
            
            String req = "DELETE FROM FactureAchat WHERE id = ?";
            PreparedStatement pst = dbb.conx.prepareStatement(req);
            pst.setInt(1, IDFACTUR);
            
            if (nb == 1) {
                JOptionPane.showMessageDialog(null, "Client supprimé avec succès.", null, JOptionPane.INFORMATION_MESSAGE);
            }   } catch (SQLException ex) {
            Logger.getLogger(AchatBeans.class.getName()).log(Level.SEVERE, null, ex);
        }
} 
}
          
