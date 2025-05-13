
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author PC
 */
public class dbb {
 
    static Connection conx = null;
private static dbb base;
public static Statement st;
    public static dbb getCon(){
         //private static Connection base = null;
        if (base == null ) {
        try {
            
                // Load the JDBC driver
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
conx = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=Gestion;integratedSecurity=true;trustServerCertificate=true");
st = conx.createStatement();
           

           } catch (ClassNotFoundException ex) {
                Logger.getLogger(dbb.class.getName()).log(Level.SEVERE, null, ex);
           } catch (SQLException ex) {
                Logger.getLogger(dbb.class.getName()).log(Level.SEVERE, null, ex);
            }
        base = new dbb();
        
                    }
    return  base;
    }
public void ajouterProduit(ProduitBeans art) {
    int nb = 0; 
    try {
        String req = "INSERT INTO Produit (Désignation,QuantitéEnStock,Categorie,Prix) VALUES(?,?,?,?)";
        PreparedStatement pst = dbb.conx.prepareStatement(req);
       // pst.setString(1, art.getNumeroProduit());
        pst.setString(1, art.getDésignation());
        pst.setString(2, art.getQuantitéEnStock());
        pst.setString(3, art.getCategorie());
        pst.setString(4, art.getPrix());
     
        nb = pst.executeUpdate();
        if (nb > 0) {
            JOptionPane.showMessageDialog(null, "Produit ajouté avec succès.", null, JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du Produit.", null, JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        if (ex.getErrorCode() == -1605) {
            JOptionPane.showMessageDialog(null, "Code Produit déjà existant", null, JOptionPane.INFORMATION_MESSAGE);
            System.out.println(ex.getErrorCode() + " " + ex.getMessage());
        } else {
            JOptionPane.showMessageDialog(null, "Erreur SQL: " + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }
}
//public void ajouterClient(ClientBean art) {
//    int nb = 0; 
//    try {
//        String req = "INSERT INTO Produit (RaisonSocial,ICE,Adresse,Telephone,Email) VALUES(?,?,?,?,?)";
//        PreparedStatement pst = dbb.conx.prepareStatement(req);
//        pst.setString(1, art.getNumeroProduit());
//        pst.setString(1, art.getRaisonSociale());
//        pst.setString(2, art.getICE());
//        pst.setString(3, art.getAdresse());
//        pst.setString(4, art.getTelephon());
//        pst.setString(5, art.getEmail());
//     
//        nb = pst.executeUpdate();
//        if (nb > 0) {
//            JOptionPane.showMessageDialog(null, "Produit ajouté avec succès.", null, JOptionPane.INFORMATION_MESSAGE);
//        } else {
//            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du Produit.", null, JOptionPane.ERROR_MESSAGE);
//        }
//    } catch (SQLException ex) {
//        if (ex.getErrorCode() == -1605) {
//            JOptionPane.showMessageDialog(null, "Code Produit déjà existant", null, JOptionPane.INFORMATION_MESSAGE);
//            System.out.println(ex.getErrorCode() + " " + ex.getMessage());
//        } else {
//            JOptionPane.showMessageDialog(null, "Erreur SQL: " + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
//        }
//    }
//}
  public boolean fournisseurExists(String numFrs) {
    boolean exists = false;
    try {
        // Assume conx is your database connection
        String query = "SELECT COUNT(*) FROM Fournisseur WHERE NumFrs = ?";
        PreparedStatement pst = conx.prepareStatement(query);
        pst.setString(1, numFrs);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            int count = rs.getInt(1);
            exists = (count > 0);
        }
    } catch (SQLException ex) {
        // Handle SQL exception

    }
    return exists;
}
public boolean produitExists(String numeroProduit) {
    boolean exists = false;
    try {
        // Assume conx is your database connection
        String query = "SELECT COUNT(*) FROM Produit WHERE NumeroProduit = ?";
        PreparedStatement pst = conx.prepareStatement(query);
        pst.setString(1, numeroProduit);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            int count = rs.getInt(1);
            exists = (count > 0);
        }
    } catch (SQLException ex) {
        // Handle SQL exception

    }
    return exists;
}
public boolean clientExists(String codeClient) {
    boolean exists = false;
    try {
        // Assume conx is your database connection
        String query = "SELECT COUNT(*) FROM Client WHERE CodeClient = ?";
        PreparedStatement pst = conx.prepareStatement(query);
        pst.setString(1, codeClient);
        ResultSet rs = pst.executeQuery();
        if (!rs.next()) {
        } else {
            int count = rs.getInt(1);
            exists = (count > 0);
        }
    } catch (SQLException ex) {
        // Handle SQL exception

    }
    return exists;
}

 public void ajouteFournisseur(FournisseurBean art) {
    int nb = 0;
    try {
        String req;
        req = "INSERT INTO Fournisseur(RaisonSocale,ICE,Adresse,telephon,email,ville) VALUES(?,?,?,?,?,?)";
        PreparedStatement pst = dbb.conx.prepareStatement(req);
        pst.setString(1, art.getRaisonSocale());
        pst.setString(2, art.getICE());
        pst.setString(3, art.getAdresse());
        pst.setString(4, art.getTelephon());
        pst.setString(5, art.getEmail());
        pst.setString(6, art.getVille());
        nb = pst.executeUpdate();
        if (nb > 0) {
            JOptionPane.showMessageDialog(null, "Fournisseur ajouté avec succès.", null, JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du fournisseur.", null, JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        if (ex.getErrorCode() == -1605) {
            JOptionPane.showMessageDialog(null, "Code Fournisseur déjà existant", null, JOptionPane.INFORMATION_MESSAGE);
            System.out.println(ex.getMessage() + ex.getErrorCode() + " ");
        } else {
            JOptionPane.showMessageDialog(null, "Erreur SQL: " + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }
}

    /**
     *
     * @param art
     */
    public void ajouterClient(ClientBean art) {
    int nb = 0;
    try {
        String req = "INSERT INTO Client (RaisonSocial,ICE,Adresse,Telephone,Email) VALUES(?,?,?,?,?)";
        PreparedStatement pst = dbb.conx.prepareStatement(req);
       
        pst.setString(1, art.getRaisonSociale());
        pst.setString(2, art.getICE());
        pst.setString(3, art.getAdresse());
        pst.setString(4, art.getTelephon());
        pst.setString(5, art.getEmail());
        nb = pst.executeUpdate();
        if (nb > 0) {
            JOptionPane.showMessageDialog(null, "Client ajouté avec succès.", null, JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du client.", null, JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        if (ex.getErrorCode() == -1605) {
            JOptionPane.showMessageDialog(null, "Code Client déjà existant", null, JOptionPane.INFORMATION_MESSAGE);
            System.out.println(ex.getMessage() + ex.getErrorCode() + " ");
        } else {
            JOptionPane.showMessageDialog(null, "Erreur SQL: " + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }
}
  public void SupFournisseur(String NumFrs) {
    int nb = 0;
    try {
        String req = "DELETE FROM Fournisseur WHERE NumFrs = ?";
        PreparedStatement pst = dbb.conx.prepareStatement(req);
        pst.setString(1, NumFrs);
        
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
        JOptionPane.showMessageDialog(null, "Fournisseur supprimé avec succès.", null, JOptionPane.INFORMATION_MESSAGE);
    }
}
    public void SupProd(String NumeroProduit) {
    int nb = 0;
    try {
        String req = "DELETE FROM Produit WHERE NumeroProduit = ?";
        PreparedStatement pst = dbb.conx.prepareStatement(req);
        pst.setString(1, NumeroProduit);
        
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
        JOptionPane.showMessageDialog(null, "Fournisseur supprimé avec succès.", null, JOptionPane.INFORMATION_MESSAGE);
    }
}
  public void SupClient(String CodeClient) {
    int nb = 0;
    try {
        String req = "DELETE FROM Client WHERE CodeClient = ?";
        PreparedStatement pst = dbb.conx.prepareStatement(req);
        pst.setString(1, CodeClient);
        
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
  
public void modifierClient(ClientBean st) {
    int nb = 0;
    try {
        String req = "UPDATE Client SET RaisonSocial=?, ICE=?, Adresse=?, Telephone=?, Email=?  WHERE CodeClient=?";
        PreparedStatement pst = conx.prepareStatement(req);
        pst.setString(1, st.getRaisonSociale());
        pst.setString(2, st.getICE());
        pst.setString(3, st.getAdresse());
        pst.setString(4, st.getTelephon());
        pst.setString(5, st.getEmail());
        pst.setString(6, st.getCodeClient());
        nb = pst.executeUpdate();
    } catch (SQLException e) {
    }
    if (nb == 1) {
        JOptionPane.showMessageDialog(null, "Client bien modifié", null, JOptionPane.INFORMATION_MESSAGE);
    }
}

  
public void modifieFour(FournisseurBean st) {
    int nb = 0;
    try {
        String req = "UPDATE Fournisseur SET RaisonSocale=?, ICE=?, Adresse=?, Telephon=?, Email=?, Ville=? WHERE NumFrs=?";
        PreparedStatement pst = conx.prepareStatement(req);
        pst.setString(1, st.getRaisonSocale());
        pst.setString(2, st.getICE());
        pst.setString(3, st.getAdresse());
        pst.setString(4, st.getTelephon());
        pst.setString(5, st.getEmail());
        pst.setString(6, st.getVille());
        pst.setString(7, st.getNumFrs());
        nb = pst.executeUpdate();
    } catch (SQLException e) {
    }
    if (nb == 1) {
        JOptionPane.showMessageDialog(null, "Fournisseur bien modifié", null, JOptionPane.INFORMATION_MESSAGE);
    }
}

public void ajouteAchat(AchatBeans pst) throws SQLException {
    int nb = 0;
    try {
        String req = "INSERT INTO FactureAchat (  DateFacture,Nfour) VALUES (?,?)";
        PreparedStatement ko = dbb.conx.prepareStatement(req);
       // ko.setString(1, pst.getId());
        ko.setString(1, pst.getDatee());
        ko.setInt(2, pst.getNumF());
       
        nb = ko.executeUpdate(); // Execute the INSERT statement
        
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
public void ajouteCommand(CommandeBeans pst) throws SQLException {
    int nb = 0;
    try {
        String req = "INSERT INTO Command (  NumFrs,DateCommande) VALUES (?,?)";
        PreparedStatement ko = dbb.conx.prepareStatement(req);
       // ko.setString(1, pst.getId());
        ko.setInt(1, pst.getNumFrs());
        ko.setString(2, pst.getDateCommande());
       
        nb = ko.executeUpdate(); // Execute the INSERT statement
        
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

public void ajouteAchatfac(AchatBeans pst) throws SQLException {
// try{
//    String checkQuery = "SELECT COUNT(*) FROM DetailAchat WHERE ID = ?";
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
    String req = "INSERT INTO DetailAchat (ID, NumPro, PrixAchat, QteAchat) VALUES (?, ?, ?, ?)";
    PreparedStatement ko = dbb.conx.prepareStatement(req);
    ko.setInt(1, pst.getId());
    ko.setInt(2, pst.getProduit());
    ko.setString(3, pst.getPrix());
    ko.setString(4, pst.getQte());
    int nb = ko.executeUpdate(); 
      if (nb > 0) {
            JOptionPane.showMessageDialog(null, "Achat Facture ajouté avec succès.", null, JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du Achat Facture.", null, JOptionPane.ERROR_MESSAGE);
        }
//    } catch (SQLException ex) {
//        if (ex.getErrorCode() == -1605) {
//            JOptionPane.showMessageDialog(null, "ID FActure déjà existant", null, JOptionPane.INFORMATION_MESSAGE);
//            System.out.println(ex.getErrorCode() + " " + ex.getMessage());
//        } else {
//            JOptionPane.showMessageDialog(null, "Erreur SQL: " + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
//        }
    
}


//public void ajouterProduit(ProduitBeans art) {
//    int nb = 0; 
//    try {
//        String req = "INSERT INTO Produit (NumeroProduit,Désignation,QuantitéEnStock,Categorie,Prix) VALUES(?,?,?,?,?)";
//        PreparedStatement pst = DB.conx.prepareStatement(req);
//        pst.setString(1, art.getNumeroProduit());
//        pst.setString(2, art.getDésignation());
//        pst.setString(3, art.getQuantitéEnStock());
//        pst.setString(4, art.getCategorie());
//        pst.setString(5, art.getPrix());
//     
//        nb = pst.executeUpdate();
//        if (nb > 0) {
//            JOptionPane.showMessageDialog(null, "Produit ajouté avec succès.", null, JOptionPane.INFORMATION_MESSAGE);
//        } else {
//            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du Produit.", null, JOptionPane.ERROR_MESSAGE);
//        }
//    } catch (SQLException ex) {
//        if (ex.getErrorCode() == -1605) {
//            JOptionPane.showMessageDialog(null, "Code Produit déjà existant", null, JOptionPane.INFORMATION_MESSAGE);
//            System.out.println(ex.getErrorCode() + " " + ex.getMessage());
//        } else {
//            JOptionPane.showMessageDialog(null, "Erreur SQL: " + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
//        }
//    }
//}


public void modifierProduit(ProduitBeans st) {
    try {
        int nb = 0;
        
        // Assuming Prix is an integer in the database
        int prix = Integer.parseInt(st.getPrix()); // Convert Prix to int
        String req = "UPDATE Produit SET Désignation=?, QuantitéEnStock=?, Categorie=?, Prix=? WHERE NumeroProduit=?";
        PreparedStatement pst = dbb.conx.prepareStatement(req);
        pst.setString(1, st.getDésignation()); // Set Désignation
        pst.setString(2, st.getQuantitéEnStock()); // Set QuantitéEnStock
        pst.setString(3, st.getCategorie()); // Set Catégorie
        pst.setInt(4, prix); // Set Prix
        pst.setString(5, st.getNumeroProduit()); // Set ProduitID
        
        nb = pst.executeUpdate();
        
        if (nb == 1) {
            JOptionPane.showMessageDialog(null, "Produit bien modifié", null, JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Aucun produit modifié", null, JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (SQLException ex) {
        Logger.getLogger(dbb.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(null, "Erreur lors de la modification du produit", "Erreur", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Le prix doit être un entier !", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}

public void ajouteDeatilCom(CommandeBeans pst) throws SQLException {
    // Check if NumeroCommand is already inserted
//    String checkQuery = "SELECT COUNT(*) FROM DetailCommand WHERE NumeroCmd = ?";
//    PreparedStatement checkStatement = dbb.conx.prepareStatement(checkQuery);
//    checkStatement.setInt(1, pst.getNumeroCommande());
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
        String req = "INSERT INTO DetailCommand (NumeroCmd, NumeroProduit, Prix, Quantité) VALUES (?, ?, ?, ?)";
        PreparedStatement ko = dbb.conx.prepareStatement(req);
        ko.setInt(1, pst.getNumeroCommande());
        ko.setInt(2, pst.getNumeroProduit());
        ko.setString(3, pst.getPrix());
        ko.setString(4, pst.getQuantité());
        nb = ko.executeUpdate(); // Execute the INSERT statement
          JOptionPane.showMessageDialog(null, "Details Command ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException e) {
        // Handle SQLException properly, e.g., print stack trace or log the error
        e.printStackTrace();
    }
}

// public void modifierClient(ClientBean st) {
//    int nb = 0;
//    try {
//        String req = "UPDATE Client SET RaisonSocial=?, ICE=?, Adresse=?, Telephone=?, Email=?  WHERE CodeClient=?";
//        PreparedStatement pst = dbb.conx.prepareStatement(req);
//        pst.setString(1, st.getRaisonSociale());
//        pst.setString(2, st.getICE());
//        pst.setString(3, st.getAdresse());
//        pst.setString(4, st.getTelephon());
//        pst.setString(5, st.getEmail());
//        pst.setString(6, st.getCodeClient());
//        nb = pst.executeUpdate();
//    } catch (SQLException e) {
//    }
//    if (nb == 1) {
//        JOptionPane.showMessageDialog(null, "Client bien modifié", null, JOptionPane.INFORMATION_MESSAGE);
//    }
//}
    public static void closePreparedStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                // Handle exception, log, or rethrow as needed

            }
        }
    }


}

