
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author PC
 */
public class Produit extends javax.swing.JFrame {
 private ResultSet rs; // ResultSet to hold the records
    private List<ProduitBeans> records = new ArrayList<>(); // List to hold the records
    private int currentRow = 0; 
    private static Connection conx = null;
    /**
     * Creates new form Produit
     */
    public Produit() {
        initComponents();
NumeroProduit.setBackground(Color.GRAY);
NumeroProduit.setEditable(false);

        // Get the database connection
        afficherProd();
        afficherProd1();
    }


     public void afficherProd() {
        try {
            dbb db = new dbb(); // Create an instance of dbb
        db.getCon();
            PreparedStatement pst = dbb.conx.prepareStatement("SELECT * From Produit ORDER BY NumeroProduit ASC");
            rs = pst.executeQuery();
            while (rs.next()) {
                ProduitBeans pb = new ProduitBeans();
                pb.setNumeroProduit(rs.getString("NumeroProduit"));
                pb.setDésignation(rs.getString("Désignation"));
                pb.setQuantitéEnStock(rs.getString("QuantitéEnStock"));
                pb.setCategorie(rs.getString("Categorie"));
                pb.setPrix(rs.getString("Prix"));

                records.add(pb);
            }
            if (!records.isEmpty()) {
                displayRecord(); // Display the first record
            } else {
                // No records found
                JOptionPane.showMessageDialog(null, "Aucun fournisseur trouvé.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'affichage des fournisseurs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
     public void afficherProd1() {
        try {
         
            PreparedStatement pst = dbb.conx.prepareStatement("SELECT * FROM Produit ORDER BY NumeroProduit ASC");
            ResultSet rs = pst.executeQuery();

            DefaultTableModel df = (DefaultTableModel) jTable1.getModel();
        df.setRowCount(0);

        while (rs.next()) {
                Vector<Object> rowVector = new Vector<>();
                rowVector.add(rs.getString("NumeroProduit"));
                rowVector.add(rs.getString("Désignation"));
                rowVector.add(rs.getString("QuantitéEnStock"));
                rowVector.add(rs.getString("Categorie"));
                rowVector.add(rs.getString("Prix"));
                
                
            df.addRow(rowVector);
        }

            if (jTable1.getRowCount() == 0) {
                // No records found
                JOptionPane.showMessageDialog(null, "Aucun Client trouvé.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'affichage des Client.", "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void displayRecord() {
        ProduitBeans pb = records.get(currentRow);
        NumeroProduit.setText(pb.getNumeroProduit());
           Désignation.setText(pb.getDésignation());
           QuantitéEnStock.setText(pb.getQuantitéEnStock());
            Categorie.setText(pb.getCategorie());
           Prix.setText(pb.getPrix());
            
    }
    
    private void goToNextRecord() {
        if (currentRow < records.size() - 1) {
            currentRow++;
            displayRecord();
        } else {
            JOptionPane.showMessageDialog(null, "Fin des enregistrements.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void goToPreviousRecord() {
        if (currentRow > 0) {
            currentRow--;
            displayRecord();
        } else {
            JOptionPane.showMessageDialog(null, "Début des enregistrements.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void goToLastRecord() {
    if (!records.isEmpty()) {
        currentRow = records.size() - 1;
        displayRecord();
    } else {
        JOptionPane.showMessageDialog(null, "Aucun enregistrement trouvé.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}
    private void goToFirstRecord() {
    if (!records.isEmpty()) {
        currentRow = 0;
        displayRecord();
    } else {
        JOptionPane.showMessageDialog(null, "Aucun enregistrement trouvé.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        NumeroProduit = new java.awt.TextField();
        Désignation = new java.awt.TextField();
        QuantitéEnStock = new java.awt.TextField();
        Categorie = new java.awt.TextField();
        Prix = new java.awt.TextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        ajouterProduit = new javax.swing.JButton();
        SupprimerProduit = new javax.swing.JButton();
        ImprimerProduit = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        Effacer = new java.awt.Button();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Produit");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(281, 6, -1, 45));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Numero Produit");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 89, -1, -1));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Designation");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 133, -1, -1));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Quantite en Stock");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 177, -1, -1));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Categorie");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 228, -1, -1));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Prix");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 282, -1, -1));

        NumeroProduit.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jPanel1.add(NumeroProduit, new org.netbeans.lib.awtextra.AbsoluteConstraints(217, 89, 185, -1));

        Désignation.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jPanel1.add(Désignation, new org.netbeans.lib.awtextra.AbsoluteConstraints(217, 129, 185, -1));

        QuantitéEnStock.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jPanel1.add(QuantitéEnStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(217, 173, 185, -1));

        Categorie.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jPanel1.add(Categorie, new org.netbeans.lib.awtextra.AbsoluteConstraints(217, 228, 185, -1));

        Prix.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jPanel1.add(Prix, new org.netbeans.lib.awtextra.AbsoluteConstraints(217, 278, 185, -1));

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton1.setText("<< Premiere");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 420, -1, -1));

        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton2.setText("< Precedant");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 420, -1, -1));

        jButton3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton3.setText("Suivant >");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 420, 112, -1));

        jButton4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton4.setText("Derniere >>");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 420, -1, -1));

        ajouterProduit.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        ajouterProduit.setText("Ajouter");
        ajouterProduit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajouterProduitActionPerformed(evt);
            }
        });
        jPanel1.add(ajouterProduit, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 480, 127, -1));

        SupprimerProduit.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        SupprimerProduit.setText("Supprimer");
        SupprimerProduit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SupprimerProduitActionPerformed(evt);
            }
        });
        jPanel1.add(SupprimerProduit, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 480, -1, -1));

        ImprimerProduit.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        ImprimerProduit.setText("Imprimer");
        ImprimerProduit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImprimerProduitActionPerformed(evt);
            }
        });
        jPanel1.add(ImprimerProduit, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 480, 123, -1));

        jButton9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton9.setText("Rechercher");
        jPanel1.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, -1, -1));

        jTextField1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 530, 178, -1));

        jTable1.setBackground(new java.awt.Color(255, 255, 255));
        jTable1.setForeground(new java.awt.Color(0, 0, 0));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Numero", "Desgnation", "Quantite", "Categorie", "Prix"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
        }

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 70, -1, 140));

        Effacer.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        Effacer.setLabel("Effacer");
        Effacer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EffacerActionPerformed(evt);
            }
        });
        jPanel1.add(Effacer, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 430, 126, -1));

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 0, 0));
        jButton5.setText("Retour");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 480, -1, -1));

        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton6.setText("Modifer");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 480, 120, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
goToPreviousRecord();         // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void ajouterProduitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajouterProduitActionPerformed


// Create an instance of dbb
dbb b = new dbb();
b.getCon();

// Create a ProduitBeans instance and set its properties
ProduitBeans cb = new ProduitBeans();
String designation = Désignation.getText();
String quantiteEnStock = QuantitéEnStock.getText();
String categorie = Categorie.getText();
String prixTexte = Prix.getText();

// Vérifier si tous les champs sont remplis
if (!designation.isEmpty() && !quantiteEnStock.isEmpty() && !categorie.isEmpty() && !prixTexte.isEmpty()) {
    // Convertir le texte du prix en entier
    try {
        int prix = Integer.parseInt(prixTexte);
        // Définir les propriétés seulement si la conversion réussit
        cb.setDésignation(designation);
        cb.setQuantitéEnStock(quantiteEnStock);
        cb.setCategorie(categorie);
  cb.setPrix(String.valueOf(prix));
        // Appeler la méthode pour ajouter un produit
        b.ajouterProduit(cb);
        JOptionPane.showMessageDialog(null, "Produit ajouté avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
    } catch (NumberFormatException e) {
        // Afficher une boîte de dialogue en cas d'erreur de format du prix
        JOptionPane.showMessageDialog(null, "Le prix doit être un entier !", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
} else {
    // Afficher une boîte de dialogue si un champ est vide
    JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs !", "Erreur", JOptionPane.ERROR_MESSAGE);
}

// Afficher les produits après l'ajout
afficherProd();
afficherProd1();

    }//GEN-LAST:event_ajouterProduitActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
goToLastRecord();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
      goToNextRecord();  // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
goToFirstRecord();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        //updateJTable2();
        int selectedrow=jTable1.getSelectedRow();
        NumeroProduit.setText((String)jTable1.getValueAt(selectedrow, 0));
       Désignation.setText((String) jTable1.getValueAt(selectedrow, 1));
        QuantitéEnStock.setText(String.valueOf(jTable1.getValueAt(selectedrow, 2)));
        Categorie.setText(String.valueOf(jTable1.getValueAt(selectedrow, 3)));
         Prix.setText(String.valueOf(jTable1.getValueAt(selectedrow, 4)));
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked

    private void EffacerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EffacerActionPerformed
        NumeroProduit.setText("");
        Désignation.setText("");
        QuantitéEnStock.setText("");
        Categorie.setText("");
        Prix.setText("");
        // TODO add your handling code here:
    }//GEN-LAST:event_EffacerActionPerformed

    private void SupprimerProduitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SupprimerProduitActionPerformed
        // TODO add your handling code here:
        dbb b = (dbb) dbb.getCon ();
        ProduitBeans sb = new ProduitBeans();
        sb.setNumeroProduit(NumeroProduit.getText());
        sb.setDésignation(Désignation.getText());
        sb.setQuantitéEnStock(QuantitéEnStock.getText());
        sb.setCategorie(Categorie.getText());
        sb.setPrix(Prix.getText());
        b.SupProd(NumeroProduit.getText());
        
                afficherProd();
        afficherProd1();
    }//GEN-LAST:event_SupprimerProduitActionPerformed

    private void ImprimerProduitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImprimerProduitActionPerformed
int[] selectedRows = jTable1.getSelectedRows();

try {
    // Load the JasperReport template
    JasperDesign jasdi;
    if (selectedRows.length == 1) {
        // If only one row is selected, use a different report template
        jasdi = JRXmlLoader.load("C:\\Users\\Administrator\\Desktop\\gestionCom - Copie\\src\\FicherProduit.jrxml");
    } else {
        // If no row is selected or multiple rows are selected, use the original report template
        jasdi = JRXmlLoader.load("C:\\Users\\Administrator\\Desktop\\gestionCom - Copie\\src\\ListProduit.jrxml");
    }

    // Construct the SQL query dynamically based on the selected rows
    StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Produit");

    if (selectedRows.length == 1) {
        // If only one row is selected, add a condition to select that row
        String id = jTable1.getValueAt(selectedRows[0], 0).toString(); // Assuming the first column contains the id
        sqlBuilder.append(" WHERE NumeroProduit = '").append(id).append("'");
    } else if (selectedRows.length > 1) {
        // If multiple rows are selected, add conditions to select those rows
        sqlBuilder.append(" WHERE NumeroProduit IN (");
        for (int i = 0; i < selectedRows.length; i++) {
            String id = jTable1.getValueAt(selectedRows[i], 0).toString(); // Assuming the first column contains the id
            sqlBuilder.append("'").append(id).append("'");
            if (i < selectedRows.length - 1) {
                sqlBuilder.append(",");
            }
        }
        sqlBuilder.append(")");
    }

    // Set the constructed SQL query
    String sql = sqlBuilder.toString();
    JRDesignQuery newQuery = new JRDesignQuery();
    newQuery.setText(sql);
    jasdi.setQuery(newQuery);

    // Compile the JasperReport template
    JasperReport js = JasperCompileManager.compileReport(jasdi);

    // Get the database connection
    Connection con = ConnectioDB.getConnection();
    if (con == null) {
        throw new SQLException("Failed to obtain a database connection.");
    }

    // Fill the JasperReport with data
    JasperPrint jd = JasperFillManager.fillReport(js, null, con);

    // Show the report in a JasperViewer
    JasperViewer.viewReport(jd, false);

} catch (JRException | SQLException ex) {
    Logger.getLogger(Produit.class.getName()).log(Level.SEVERE, null, ex);
}
    }//GEN-LAST:event_ImprimerProduitActionPerformed

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
jTable1.clearSelection();
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
  
     dbb b = (dbb) dbb.getCon(); // Get the database connection
    if (b != null) {
        ProduitBeans sb = new ProduitBeans();
        try {
            // Validate and set properties if the conversion is successful
            try {
                // Validate that Prix and QuantitéEnStock are integers
                int quantiteEnStock = Integer.parseInt(QuantitéEnStock.getText());
                int prix = Integer.parseInt(Prix.getText());
                
                // Set properties
                sb.setDésignation(Désignation.getText());
                sb.setQuantitéEnStock(String.valueOf(quantiteEnStock)); // Convert back to String
                sb.setCategorie(Categorie.getText());
                sb.setPrix(String.valueOf(prix)); // Convert back to String
                sb.setNumeroProduit(NumeroProduit.getText());
                
                // Call the modifierProduit method
                b.modifierProduit(sb);
            } catch (NumberFormatException e) {
                // Display error message if the format is incorrect
                JOptionPane.showMessageDialog(null, "La quantité en stock et le prix doivent être des entiers !", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la modification du produit", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(null, "La connexion à la base de données n'est pas disponible.", "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
    }
    afficherProd();
    afficherProd1();

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Produit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Produit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Produit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Produit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Produit().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.TextField Categorie;
    private java.awt.TextField Désignation;
    private java.awt.Button Effacer;
    private javax.swing.JButton ImprimerProduit;
    private java.awt.TextField NumeroProduit;
    private java.awt.TextField Prix;
    private java.awt.TextField QuantitéEnStock;
    private javax.swing.JButton SupprimerProduit;
    private javax.swing.JButton ajouterProduit;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
