
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
public class CommandeBeans {
    private int NumeroCommande;
    private int NumFrs;
    private String DateCommande;
    private int NumeroProduit;
    private String Prix;
    private String Quantité;

    public int getNumeroProduit() {
        return NumeroProduit;
    }

    public void setNumeroProduit(int NumeroProduit) {
        this.NumeroProduit = NumeroProduit;
    }

    public String getPrix() {
        return Prix;
    }

    public void setPrix(String Prix) {
        this.Prix = Prix;
    }

    public String getQuantité() {
        return Quantité;
    }

    public void setQuantité(String Quantité) {
        this.Quantité = Quantité;
    }
    public int getNumeroCommande() {
        return NumeroCommande;
    }

    public void setNumeroCommande(int NumeroCommande) {
        this.NumeroCommande = NumeroCommande;
    }

    public int getNumFrs() {
        return NumFrs;
    }

    public void setNumFrs(int NumFrs) {
        this.NumFrs = NumFrs;
    }

    public String getDateCommande() {
        return DateCommande;
    }

    public void setDateCommande(String DateCommande) {
        this.DateCommande = DateCommande;
    }
    
 public void modifierCommand(CommandeBeans st) {
    dbb db = dbb.getCon();
    int nb = 0;
    try {
        String req = "UPDATE Command SET DateCommande=?, NumFrs=? WHERE NumeroCommande=?";
        PreparedStatement pst = db.conx.prepareStatement(req);
        pst.setString(1, st.getDateCommande()); // Set the DateFacture
        pst.setInt(2, st.getNumFrs());     // Set the Nfour
        pst.setInt(3, st.getNumeroCommande()); // Set the ID
        nb = pst.executeUpdate();
    } catch (SQLException e) {
        // Handle the SQLException properly, e.g., print stack trace or log the error
        e.printStackTrace();
    }
    if (nb == 1) {
        JOptionPane.showMessageDialog(null, "Command bien modifiée", null, JOptionPane.INFORMATION_MESSAGE);
    }
}

public void modifierCommandDetail(CommandeBeans st) {
    dbb db = dbb.getCon();
    int nb = 0;

    // Verify if NumeroProduit, Prix and Quantité are empty
    if (st.getNumeroProduit() == 0 || st.getPrix().isEmpty() || st.getQuantité().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs.", "Champs obligatoires", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method if any field is empty
    }

    // Verify if Prix is an integer
    try {
        Integer.parseInt(st.getPrix());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Le prix doit être un nombre entier.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method if Prix is not an integer
    }

    // Verify if Quantité is an integer
    try {
        Integer.parseInt(st.getQuantité());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "La quantité doit être un nombre entier.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method if Quantité is not an integer
    }

    try {
        // Update detail where both NumeroCmd and NumeroProduit match
        String req = "UPDATE DetailCommand SET Prix=?, Quantité=? WHERE NumeroCmd=? AND NumeroProduit=?";
        PreparedStatement pst = db.conx.prepareStatement(req);
        pst.setString(1, st.getPrix());       // Set Prix
        pst.setString(2, st.getQuantité());   // Set Quantité
        pst.setInt(3, st.getNumeroCommande()); // Set NumeroCmd
        pst.setInt(4, st.getNumeroProduit());  // Set NumeroProduit
        nb = pst.executeUpdate();
    } catch (SQLException e) {
        // Handle the SQLException properly, e.g., print stack trace or log the error
        e.printStackTrace();
    }

    if (nb == 1) {
        JOptionPane.showMessageDialog(null, "Détail de la commande bien modifié.", "Succès", JOptionPane.INFORMATION_MESSAGE);
    }
}


             public void SupComman(int IDCOMMAND) {
    int nb = 0;
    try {
        String req = "DELETE FROM DetailCommand WHERE NumeroCmd = ?";
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
          public void SupCommandDetail(int IDCOMMAND) {
        try {
            int nb = 0;
            
            String req = "DELETE FROM Command WHERE NumeroCommande = ?";
            PreparedStatement pst = dbb.conx.prepareStatement(req);
            pst.setInt(1, IDCOMMAND);
            
            if (nb == 1) {
                JOptionPane.showMessageDialog(null, "Command supprimé avec succès.", null, JOptionPane.INFORMATION_MESSAGE);
            }   } catch (SQLException ex) {
            Logger.getLogger(CommandeBeans.class.getName()).log(Level.SEVERE, null, ex);
        }
} 
}
