
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
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
public class Fournisseur extends javax.swing.JFrame {
 private final List<FournisseurBean> recordsBeans = new ArrayList<>(); // List to hold the record
    private ResultSet rs; // ResultSet to hold the records
    private List<FournisseurBean> records = new ArrayList<>(); // List to hold the records
    private int currentRow = 0; // Current row index // Current row index    
private DefaultTableModel tableModel = new DefaultTableModel();

    /**
     * Creates new form Fournisseur
     */
    public Fournisseur() {
        initComponents();
     // createTableModel(); // Create the table model
    afficherFRS1(); // Retrieve data from the database
   // displayRecord(); // Display the first record
        afficherFour();
        afficherFour();
          NumFrs.setEditable(false);
    // Set the background color to grey

    NumFrs.setBackground(Color.LIGHT_GRAY);
   
    }
private void populateJTable2() {
    String fournisseurNumFrs = NumFrs.getText();
    dbb db = dbb.getCon();

    String query = "SELECT " +
                   "    f.RaisonSocale, " +
                   "    c.NumeroCommande, " +
                   "    c.DateCommande, " +
                   "    bl.Bon_ID, " +
                   "    bl.DateBLf, " +
                   "    fa.Facture_ID, " +
                   "    fa.DateFacture " +
                   "FROM " +
                   "    [Gestion].[dbo].[Fournisseur] AS f " +
                   "OUTER APPLY " +
                   "    (SELECT TOP 1 " +
                   "         NumFrs, " +
                   "         NumeroCommande, " +
                   "         DateCommande " +
                   "     FROM " +
                   "         [Gestion].[dbo].[Command] AS c " +
                   "     WHERE " +
                   "         c.NumFrs = f.NumFrs " +
                   "     ORDER BY " +
                   "         DateCommande DESC " +
                   "    ) AS c " +
                   "OUTER APPLY " +
                   "    (SELECT TOP 1 " +
                   "         Nfour AS NumFrs, " +
                   "         ID AS Bon_ID, " +
                   "         DateBLf " +
                   "     FROM " +
                   "         [Gestion].[dbo].[BLFour] AS bl " +
                   "     WHERE " +
                   "         bl.Nfour = f.NumFrs " +
                   "     ORDER BY " +
                   "         DateBLf DESC " +
                   "    ) AS bl " +
                   "OUTER APPLY " +
                   "    (SELECT TOP 1 " +
                   "         Nfour AS NumFrs, " +
                   "         ID AS Facture_ID, " +
                   "         DateFacture " +
                   "     FROM " +
                   "         [Gestion].[dbo].[FactureAchat] AS fa " +
                   "     WHERE " +
                   "         fa.Nfour = f.NumFrs " +
                   "     ORDER BY " +
                   "         DateFacture DESC " +
                   "    ) AS fa " +
                   "WHERE " +
                   "    f.NumFrs = ?";

    try (PreparedStatement pst = db.conx.prepareStatement(query)) {
        pst.setString(1, fournisseurNumFrs);
        ResultSet rs = pst.executeQuery();

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Clear existing rows

        while (rs.next()) {
            String raisonSocale = rs.getString("RaisonSocale");
            String numeroCommande = rs.getString("NumeroCommande");
            String dateCommande = rs.getString("DateCommande");
            int bonID = rs.getInt("Bon_ID");
            String dateBLf = rs.getString("DateBLf");
            int factureID = rs.getInt("Facture_ID");
            String dateFacture = rs.getString("DateFacture");

            Object[] row = {raisonSocale, numeroCommande, dateCommande, bonID, dateBLf, factureID, dateFacture};
            model.addRow(row);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



 public void afficherFRS1() {
      dbb.getCon();
     try {
         
            PreparedStatement pst = dbb.conx.prepareStatement("SELECT * FROM Fournisseur ORDER BY NumFrs ASC");
            ResultSet rs = pst.executeQuery();

            DefaultTableModel df = (DefaultTableModel) jTable2.getModel();
        df.setRowCount(0);

        while (rs.next()) {
                Vector<Object> rowVector = new Vector<>();
                rowVector.add(rs.getString("NumFrs"));
                rowVector.add(rs.getString("RaisonSocale"));
                rowVector.add(rs.getString("ICE"));
                rowVector.add(rs.getString("Adresse"));
                rowVector.add(rs.getString("Telephon"));
                rowVector.add(rs.getString("Email"));
                rowVector.add(rs.getString("Ville"));
                
            df.addRow(rowVector);
        }

            if (jTable2.getRowCount() == 0) {
                // No records found
                JOptionPane.showMessageDialog(null, "Aucun Fournissuer trouvé.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'affichage des Client.", "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
//    private void createTableModel() {
//        // Define column names
//        String[] columnNames = {"NumFrs", "RaisonSociale", "ICE", "Adresse", "Telephone", "Email", "Ville"};
//        // Create the table model with the column names
//        tableModel = new DefaultTableModel(columnNames, 0);
//        // Set the model to JTable1
//        jTable2.setModel(tableModel);
//    }

//    private void populateTable() {
//        // Clear existing data in the table
//        tableModel.setRowCount(0);
//        // Populate the table with data from the records list
//        for (FournisseurBean pb : records) {
//            tableModel.addRow(new Object[]{pb.getNumFrs(), pb.getRaisonSocale(), pb.getICE(), pb.getAdresse(), pb.getTelephon(), pb.getEmail(), pb.getVille()});
//        }
//    }
////
public void afficherFour() {
    try {
        dbb.getCon();
        PreparedStatement pst = dbb.conx.prepareStatement("SELECT * FROM Fournisseur ORDER BY NumFrs ASC");
        rs = pst.executeQuery();
        
        // Clear the records list before populating it again
        records.clear();
        
        while (rs.next()) {
            FournisseurBean pb = new FournisseurBean();
            pb.setNumFrs(rs.getString("NumFrs"));
            pb.setRaisonSocale(rs.getString("RaisonSocale"));
            pb.setICE(rs.getString("ICE"));
            pb.setAdresse(rs.getString("Adresse"));
            pb.setTelephon(rs.getString("Telephon"));
            pb.setEmail(rs.getString("Email"));
            pb.setVille(rs.getString("Ville"));
            recordsBeans.add(pb);
            
            // Add each record to the records list
            records.add(pb);
         //   populateJTable2();
        }
        
        if (!recordsBeans.isEmpty()) {
            displayRecord(); // Display the first record
        } else {
            // No records found
            JOptionPane.showMessageDialog(null, "Aucun Client trouvé.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Erreur lors de l'affichage des Client.", "Erreur", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
    private void displayRecord() {
    if (records.isEmpty() || currentRow < 0 || currentRow >= records.size()) {
        // Gérer le cas où la liste est vide ou currentRow est hors des limites
        // Par exemple, effacer les champs texte
        NumFrs.setText("");
        RaisonSocale.setText("");
        ICE.setText("");
        Adresse.setText("");
        Telephon.setText("");
        Email.setText("");
        ville.setText("");
        return;
    }

    FournisseurBean pb = records.get(currentRow);
    NumFrs.setText(pb.getNumFrs());
    RaisonSocale.setText(pb.getRaisonSocale());
    ICE.setText(pb.getICE());
    Adresse.setText(pb.getAdresse());
    Telephon.setText(pb.getTelephon());
    Email.setText(pb.getEmail());
    ville.setText(pb.getVille());
}

//
    public void goToNextRecord() {
        if (currentRow < records.size() - 1) {
            currentRow++;
            displayRecord();
        } else {
            JOptionPane.showMessageDialog(null, "Fin des enregistrements.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

 

    public void goToPreviousRecord() {
        if (currentRow > 0) {
            currentRow--;
            displayRecord();
        } else {
            JOptionPane.showMessageDialog(null, "Début des enregistrements.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void goToLastRecord() {
        if (!records.isEmpty()) {
            currentRow = records.size() - 1;
            displayRecord();
        } else {
            JOptionPane.showMessageDialog(null, "Aucun enregistrement trouvé.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void goToFirstRecord() {
        if (!records.isEmpty()) {
            currentRow = 0;
            displayRecord();
        } else {
            JOptionPane.showMessageDialog(null, "Aucun enregistrement trouvé.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        button5 = new java.awt.Button();
        Label5 = new java.awt.Label();
        ajouterFournisseur = new java.awt.Button();
        Telephon = new java.awt.TextField();
        textField8 = new java.awt.TextField();
        Label3 = new java.awt.Label();
        RaisonSocale = new java.awt.TextField();
        Adresse = new java.awt.TextField();
        button3 = new java.awt.Button();
        NumFrs = new java.awt.TextField();
        Label6 = new java.awt.Label();
        button2 = new java.awt.Button();
        Label8 = new java.awt.Label();
        Label7 = new java.awt.Label();
        button9 = new java.awt.Button();
        Label2 = new java.awt.Label();
        Label4 = new java.awt.Label();
        ville = new java.awt.TextField();
        supprimerFournisseur = new java.awt.Button();
        ICE = new java.awt.TextField();
        button1 = new java.awt.Button();
        button10 = new java.awt.Button();
        Effacer = new java.awt.Button();
        modifierFournisseur = new java.awt.Button();
        label1 = new java.awt.Label();
        Email = new java.awt.TextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
        });
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        button5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        button5.setLabel("Demiere>>");
        button5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button5ActionPerformed(evt);
            }
        });
        jPanel2.add(button5, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 570, 126, 30));

        Label5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        Label5.setText("Adresse Fournisseur");
        jPanel2.add(Label5, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 201, -1, -1));

        ajouterFournisseur.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        ajouterFournisseur.setLabel("Ajouter");
        ajouterFournisseur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajouterFournisseurActionPerformed(evt);
            }
        });
        jPanel2.add(ajouterFournisseur, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 660, 105, 30));

        Telephon.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jPanel2.add(Telephon, new org.netbeans.lib.awtextra.AbsoluteConstraints(317, 237, 145, -1));
        jPanel2.add(textField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 660, 160, 40));

        Label3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        Label3.setText("Raison Sociale Fournisseur");
        jPanel2.add(Label3, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 129, -1, -1));

        RaisonSocale.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jPanel2.add(RaisonSocale, new org.netbeans.lib.awtextra.AbsoluteConstraints(317, 129, 145, -1));

        Adresse.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jPanel2.add(Adresse, new org.netbeans.lib.awtextra.AbsoluteConstraints(317, 201, 145, -1));

        button3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        button3.setLabel("Suivant>");
        button3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button3ActionPerformed(evt);
            }
        });
        jPanel2.add(button3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 570, 103, 30));

        NumFrs.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jPanel2.add(NumFrs, new org.netbeans.lib.awtextra.AbsoluteConstraints(317, 89, 145, -1));

        Label6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        Label6.setText("Telephone Fournisseur");
        jPanel2.add(Label6, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 237, -1, -1));

        button2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        button2.setLabel("<Precedant");
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });
        jPanel2.add(button2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 570, -1, 30));

        Label8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        Label8.setText("Ville Fournisseur");
        jPanel2.add(Label8, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 317, -1, -1));

        Label7.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        Label7.setText("Email Fournisseur");
        jPanel2.add(Label7, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 281, -1, -1));

        button9.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        button9.setLabel("Rechercher");
        button9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button9ActionPerformed(evt);
            }
        });
        jPanel2.add(button9, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 660, -1, 30));

        Label2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        Label2.setText("Numero Fournisseur");
        jPanel2.add(Label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 99, -1, 20));

        Label4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        Label4.setText("ICE Fournisseur");
        jPanel2.add(Label4, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 165, -1, -1));

        ville.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jPanel2.add(ville, new org.netbeans.lib.awtextra.AbsoluteConstraints(317, 317, 145, -1));

        supprimerFournisseur.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        supprimerFournisseur.setLabel("Supprimer");
        supprimerFournisseur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supprimerFournisseurActionPerformed(evt);
            }
        });
        jPanel2.add(supprimerFournisseur, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 660, 103, 30));

        ICE.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jPanel2.add(ICE, new org.netbeans.lib.awtextra.AbsoluteConstraints(317, 165, 145, -1));

        button1.setActionCommand("Premiere");
        button1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        button1.setLabel("<<Premiere");
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });
        jPanel2.add(button1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 570, -1, 30));

        button10.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        button10.setLabel("Imprimer");
        button10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button10ActionPerformed(evt);
            }
        });
        jPanel2.add(button10, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 660, 126, 30));

        Effacer.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        Effacer.setLabel("Effacer");
        Effacer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EffacerActionPerformed(evt);
            }
        });
        jPanel2.add(Effacer, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 570, 126, 30));

        modifierFournisseur.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        modifierFournisseur.setLabel("Modifier");
        modifierFournisseur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifierFournisseurActionPerformed(evt);
            }
        });
        jPanel2.add(modifierFournisseur, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 660, 103, 30));

        label1.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        label1.setText("Fournisseur");
        jPanel2.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(255, 10, -1, -1));

        Email.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jPanel2.add(Email, new org.netbeans.lib.awtextra.AbsoluteConstraints(317, 281, 145, -1));

        jTable2.setBackground(new java.awt.Color(255, 255, 255));
        jTable2.setForeground(new java.awt.Color(0, 0, 0));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Numero", "Raison Sociale", "ICE", "Adresse", "Telephon", "Email", "Ville"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 80, -1, 200));

        jTable1.setBackground(new java.awt.Color(255, 255, 255));
        jTable1.setForeground(new java.awt.Color(0, 0, 0));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Raison social", "numero Commande ", "date Command", "bon ID", "date Bon", "facture ID", "date Facture"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 290, -1, 200));

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 0, 0));
        jButton5.setText("Retour");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 570, -1, -1));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setText("Imprimer");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 500, -1, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 990, 710));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button3ActionPerformed
        goToNextRecord();
        // TODO add your handling code here:
    }//GEN-LAST:event_button3ActionPerformed

    private void button5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button5ActionPerformed

        goToLastRecord();
        // TODO add your handling code here:
    }//GEN-LAST:event_button5ActionPerformed

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
        goToPreviousRecord();        // TODO add your handling code here:
    }//GEN-LAST:event_button2ActionPerformed

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        goToFirstRecord();
        // TODO add your handling code here:
    }//GEN-LAST:event_button1ActionPerformed

    private void button9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button9ActionPerformed
     DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    jTable2.setRowSorter(sorter);

    String searchText = textField8.getText().toLowerCase(); // Convert to lowercase for case-insensitive search

    sorter.setRowFilter(new RowFilter<DefaultTableModel, Object>() {
        @Override
        public boolean include(RowFilter.Entry<? extends DefaultTableModel, ? extends Object> entry) {
            for (int i = 0; i < entry.getValueCount(); i++) {
                // Check if any column value contains the search text
                if (entry.getStringValue(i).toLowerCase().contains(searchText)) {
                    return true;
                }
            }
            return false;
//            id();
        }
    }); 
        // TODO add your handling code here:
    }//GEN-LAST:event_button9ActionPerformed

    private void EffacerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EffacerActionPerformed
        NumFrs.setText("");
        RaisonSocale.setText("");
        ICE.setText("");
        Adresse.setText("");
        Telephon.setText("");
        Email.setText("");
        ville.setText("");

        // TODO add your handling code here:
    }//GEN-LAST:event_EffacerActionPerformed

    private void ajouterFournisseurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajouterFournisseurActionPerformed
        dbb d = dbb.getCon();
        FournisseurBean sb = new FournisseurBean();

// Check if any textbox is empty
        if ( RaisonSocale.getText().trim().isEmpty() || ICE.getText().trim().isEmpty()
                || Adresse.getText().trim().isEmpty() || Telephon.getText().trim().isEmpty() || Email.getText().trim().isEmpty()
                || ville.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs.", "Champs vides", JOptionPane.ERROR_MESSAGE);
        } else {
           {
                // All fields are filled and NumFrs doesn't exist, proceed to set properties and add to the database
                //sb.setNumFrs(NumFrs.getText());
                sb.setRaisonSocale(RaisonSocale.getText());
                sb.setICE(ICE.getText());
                sb.setAdresse(Adresse.getText());
                sb.setTelephon(Telephon.getText());
                sb.setEmail(Email.getText());
                sb.setVille(ville.getText());

                if (d != null) {
                    d.ajouteFournisseur(sb);
                } else {
                    JOptionPane.showMessageDialog(null, "La connexion à la base de données n'est pas disponible.", "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
               
                }
           afficherFRS1();
              
           }
        }


    }//GEN-LAST:event_ajouterFournisseurActionPerformed

    private void modifierFournisseurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifierFournisseurActionPerformed
        // TODO add your handling code here:
                dbb d = dbb.getCon();
          if ( RaisonSocale.getText().trim().isEmpty() || ICE.getText().trim().isEmpty()
                || Adresse.getText().trim().isEmpty() || Telephon.getText().trim().isEmpty() || Email.getText().trim().isEmpty()
                || ville.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs.", "Champs vides", JOptionPane.ERROR_MESSAGE);
        } else {
        dbb b = dbb.getCon();
        FournisseurBean sb = new FournisseurBean();
        sb.setNumFrs(NumFrs.getText());
        sb.setRaisonSocale(RaisonSocale.getText());
        sb.setICE(ICE.getText());
        sb.setAdresse(Adresse.getText());
        sb.setTelephon(Telephon.getText());
        sb.setEmail(Email.getText());
        sb.setVille(ville.getText());
            if (d != null) {
        b.modifieFour(sb);
 } else {
                    JOptionPane.showMessageDialog(null, "La connexion à la base de données n'est pas disponible.", "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
               
                }
           afficherFRS1();
              
           }
        
    }//GEN-LAST:event_modifierFournisseurActionPerformed

    private void supprimerFournisseurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supprimerFournisseurActionPerformed
        // TODO add your handling code here:
        dbb b = dbb.getCon();
        FournisseurBean sb = new FournisseurBean();
        sb.setNumFrs(NumFrs.getText());
        sb.setRaisonSocale(RaisonSocale.getText());
        sb.setICE(ICE.getText());
        sb.setAdresse(Adresse.getText());
        sb.setTelephon(Telephon.getText());
        sb.setEmail(Email.getText());
        sb.setVille(ville.getText());
        b.SupFournisseur(NumFrs.getText());


    }//GEN-LAST:event_supprimerFournisseurActionPerformed

    private void button10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button10ActionPerformed
int[] selectedRows = jTable2.getSelectedRows();

try {
    // Load the JasperReport template
    JasperDesign jasdi;
    if (selectedRows.length == 1) {
        // If only one row is selected, use a different report template
    jasdi = JRXmlLoader.load("C:\\Users\\Administrator\\Desktop\\gestionCom - Copie\\src\\FicherFour.jrxml");
    } else {
        // If no row is selected or multiple rows are selected, use the original report template
jasdi = JRXmlLoader.load("C:\\Users\\Administrator\\Desktop\\gestionCom - Copie\\src\\ListFourn.jrxml");
    }

    // Construct the SQL query dynamically based on the selected rows
    StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Fournisseur");

    if (selectedRows.length == 1) {
        // If only one row is selected, add a condition to select that row
        String id = jTable2.getValueAt(selectedRows[0], 0).toString(); // Assuming the first column contains the id
        sqlBuilder.append(" WHERE NumFrs = '").append(id).append("'");
    } else if (selectedRows.length > 1) {
        // If multiple rows are selected, add conditions to select those rows
        sqlBuilder.append(" WHERE NumFrs IN (");
        for (int i = 0; i < selectedRows.length; i++) {
            String id = jTable2.getValueAt(selectedRows[i], 0).toString(); // Assuming the first column contains the id
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
        // TODO add your handling code here:
    }//GEN-LAST:event_button10ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        populateJTable2();
        int selectedrow=jTable2.getSelectedRow();
       NumFrs.setText((String)jTable2.getValueAt(selectedrow, 0));
        RaisonSocale.setText((String) jTable2.getValueAt(selectedrow, 1));
        ICE.setText(String.valueOf(jTable2.getValueAt(selectedrow, 2)));
        Adresse.setText(String.valueOf(jTable2.getValueAt(selectedrow, 3)));
        Telephon.setText(String.valueOf(jTable2.getValueAt(selectedrow, 4)));
        Email.setText(String.valueOf(jTable2.getValueAt(selectedrow, 5)));
       ville.setText(String.valueOf(jTable2.getValueAt(selectedrow, 6)));
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable2MouseClicked

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked

jTable2.clearSelection();        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel2MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
try {
        HashMap<String, Object> param = new HashMap<>();

        DefaultTableModel tbl = (DefaultTableModel) jTable1.getModel();
        int row = jTable1.getSelectedRow();

        if (row >= 0) {
            // Assuming 'GareName' is in the second column (index 1) of the table
            Object value = tbl.getValueAt(row, 1);
            if (value != null && !value.toString().isEmpty()) {
                String gareName = value.toString();
                param.put("S", gareName);

                JasperDesign jasdi = JRXmlLoader.load("C:\\Users\\Administrator\\Desktop\\gestionCom - Copie\\src\\fourprod.jrxml");

                // Compile the JasperReport template
                JasperReport js = JasperCompileManager.compileReport(jasdi);

                // Provide a database connection (replace 'con' with your actual Connection object)
                Connection con = ConnectioDB.getConnection();

                // Fill the JasperReport with data
                JasperPrint jd = JasperFillManager.fillReport(js, param, con);

                // Show the report in a JasperViewer
                JasperViewer.viewReport(jd, false);
            } else {
                JOptionPane.showMessageDialog(null, "Le champ sélectionné est vide ou nul. Sélectionnez une valeur valide.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Sélectionnez une ligne dans le tableau.");
        }
    } catch (JRException ex) {
        Logger.getLogger(Fournisseur.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(null, "Erreur lors du remplissage du rapport: " + ex.getMessage());
    }

 
    // TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed
    private void searchFournisseur(String searchText) {
        List<FournisseurBean> searchResults = new ArrayList<>();
        records.stream().filter((fournisseur) -> (fournisseurMatchesSearch(fournisseur, searchText))).forEachOrdered((fournisseur) -> {
            searchResults.add(fournisseur);
        });
        if (!searchResults.isEmpty()) {
            records = searchResults;
            currentRow = 0;
       //     displayRecord();
            JOptionPane.showMessageDialog(this, "Fournisseur trouvé.", "Rechercher Fournisseur", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Aucun fournisseur trouvé pour ce critère de recherche.", "Rechercher Fournisseur", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean fournisseurMatchesSearch(FournisseurBean fournisseur, String searchText) {
        String lowerCaseSearchText = searchText.toLowerCase();
        return fournisseur.getNumFrs().toLowerCase().contains(lowerCaseSearchText)
                || fournisseur.getRaisonSocale().toLowerCase().contains(lowerCaseSearchText)
                || fournisseur.getICE().toLowerCase().contains(lowerCaseSearchText)
                || fournisseur.getAdresse().toLowerCase().contains(lowerCaseSearchText)
                || fournisseur.getTelephon().toLowerCase().contains(lowerCaseSearchText)
                || fournisseur.getEmail().toLowerCase().contains(lowerCaseSearchText)
                || fournisseur.getVille().toLowerCase().contains(lowerCaseSearchText);
    }

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Fournisseur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Fournisseur().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.TextField Adresse;
    private java.awt.Button Effacer;
    private java.awt.TextField Email;
    private java.awt.TextField ICE;
    private java.awt.Label Label2;
    private java.awt.Label Label3;
    private java.awt.Label Label4;
    private java.awt.Label Label5;
    private java.awt.Label Label6;
    private java.awt.Label Label7;
    private java.awt.Label Label8;
    private java.awt.TextField NumFrs;
    private java.awt.TextField RaisonSocale;
    private java.awt.TextField Telephon;
    private java.awt.Button ajouterFournisseur;
    private java.awt.Button button1;
    private java.awt.Button button10;
    private java.awt.Button button2;
    private java.awt.Button button3;
    private java.awt.Button button5;
    private java.awt.Button button9;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private java.awt.Label label1;
    private java.awt.Button modifierFournisseur;
    private java.awt.Button supprimerFournisseur;
    private java.awt.TextField textField8;
    private java.awt.TextField ville;
    // End of variables declaration//GEN-END:variables
}
