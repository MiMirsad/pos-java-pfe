
import java.awt.Color;
import java.sql.Connection;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;
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
public class Client extends javax.swing.JFrame {
    private ResultSet rs; // ResultSet to hold the records
    private final List<ClientBean> recordsClientBeans = new ArrayList<>(); // List to hold the records
    private int currentRow = 0; // Current row index // Current row index 
  private JTable jTable11;
    private DefaultTableModel model;
    /**
     * Creates new form Client
     */
    public Client() {
        initComponents();
        //afficherClient();
        afficherClient();
         afficherClient1();
            CodeClient.setEditable(false);
  //      kkkk();
    // Set the background color to grey
    CodeClient.setBackground(Color.LIGHT_GRAY);
    }

 public void afficherClient1() {
      dbb.getCon();
     try {
         
            PreparedStatement pst = dbb.conx.prepareStatement("SELECT * FROM Client ORDER BY CodeClient ASC");
            ResultSet rs = pst.executeQuery();

            DefaultTableModel df = (DefaultTableModel) jTable1.getModel();
        df.setRowCount(0);

        while (rs.next()) {
                Vector<Object> rowVector = new Vector<>();
                rowVector.add(rs.getString("CodeClient"));
                rowVector.add(rs.getString("RaisonSocial"));
                rowVector.add(rs.getString("ICE"));
                rowVector.add(rs.getString("Adresse"));
                rowVector.add(rs.getString("Telephone"));
                rowVector.add(rs.getString("Email"));
                
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
 private void kkkk() {
  System.out.println("Updating JTable..."); // Add this line for debugging

  try {
    dbb db = dbb.getCon(); // Get the database connection
    if (db.conx != null && !db.conx.isClosed()) { // Check if connection is not null and not closed
 String sql = "SELECT cl.RaisonSocial AS Client, p.Désignation AS Produit, v.DateVent " +
             "FROM Client cl " +
             "INNER JOIN Vent v ON cl.CodeClient = v.NumClient " +
             "INNER JOIN FactueVent d ON v.id = d.ID " +
             "INNER JOIN Produit p ON d.NumeroProduit = p.NumeroProduit " +
             "WHERE cl.CodeClient = ?";

PreparedStatement pst = db.conx.prepareStatement(sql);
pst.setString(1, CodeClient.getText());

            ResultSet resultSet = pst.executeQuery();

            DefaultTableModel df = (DefaultTableModel) jTable2.getModel();
            df.setRowCount(0);

      while (resultSet.next()) {
        Vector<Object> rowVector = new Vector<>();
        rowVector.add(resultSet.getString("Client"));
        rowVector.add(resultSet.getString("Produit"));
        rowVector.add(resultSet.getString("DateVent"));

        df.addRow(rowVector);
        System.out.println("Rows fetched: " + df.getRowCount());
      }

      // Notify JTable to refresh its view
      df.fireTableDataChanged();
    } else {
      JOptionPane.showMessageDialog(null, "La connexion à la base de données est fermée.", "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
    }
  } catch (SQLException ex) {
    Logger.getLogger(Devis.class.getName()).log(Level.SEVERE, null, ex);
  }
}
     public void afficherClient() {
        try {
            dbb.getCon();
            PreparedStatement pst = dbb.conx.prepareStatement("SELECT * FROM Client ORDER BY CodeClient ASC");
            rs = pst.executeQuery();
            while (rs.next()) {
                ClientBean pb = new ClientBean();
                pb.setCodeClient(rs.getString("CodeClient"));
                pb.setRaisonSociale(rs.getString("RaisonSocial"));
                pb.setICE(rs.getString("ICE"));
                pb.setAdresse(rs.getString("Adresse"));
                pb.setTelephon(rs.getString("Telephone"));
                pb.setEmail(rs.getString("Email"));
                recordsClientBeans.add(pb);
                
                }
            if (!recordsClientBeans.isEmpty()) {
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
        ClientBean pb = recordsClientBeans.get(currentRow);
     //   CodeClient.setText(pb.getCodeClient());
            RaisonSocial.setText(pb.getRaisonSociale());
            ICE.setText(pb.getICE());
            Adresse.setText(pb.getAdresse());
            Telephone.setText(pb.getTelephon());
            Email.setText(pb.getEmail());
            
    }
    
    private void goToNextRecordVoid() {
        if (currentRow < recordsClientBeans.size() - 1) {
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
    if (!recordsClientBeans.isEmpty()) {
        currentRow = recordsClientBeans.size() - 1;
        displayRecord();
    } else {
        JOptionPane.showMessageDialog(null, "Aucun enregistrement trouvé.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}
    private void goToFirstRecord() {
    if (!recordsClientBeans.isEmpty()) {
        currentRow = 0;
        displayRecord();
    } else {
        JOptionPane.showMessageDialog(null, "Aucun enregistrement trouvé.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}
    
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
        jLabel7 = new javax.swing.JLabel();
        CodeClient = new javax.swing.JTextField();
        RaisonSocial = new javax.swing.JTextField();
        ICE = new javax.swing.JTextField();
        Adresse = new javax.swing.JTextField();
        Telephone = new javax.swing.JTextField();
        Email = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        ajouterClient = new javax.swing.JButton();
        ModifierClient = new javax.swing.JButton();
        SupprimerClient = new javax.swing.JButton();
        ImprimerClient = new javax.swing.JButton();
        RechercherClient = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Client");

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Numero Client");

        jLabel3.setBackground(new java.awt.Color(0, 0, 0));
        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Raison Social");

        jLabel4.setBackground(new java.awt.Color(0, 0, 0));
        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("ICE");

        jLabel5.setBackground(new java.awt.Color(0, 0, 0));
        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Adresse");

        jLabel6.setBackground(new java.awt.Color(0, 0, 0));
        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Telephone");

        jLabel7.setBackground(new java.awt.Color(0, 0, 0));
        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Email");

        CodeClient.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        CodeClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CodeClientActionPerformed(evt);
            }
        });

        RaisonSocial.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        ICE.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        Adresse.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        Telephone.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        Email.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setText("<<Premiere");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 0, 0));
        jButton2.setText("<Precedant");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 0, 0));
        jButton3.setText("Suivant>");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 0, 0));
        jButton4.setText("Derniere>>");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        ajouterClient.setBackground(new java.awt.Color(255, 255, 255));
        ajouterClient.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        ajouterClient.setForeground(new java.awt.Color(0, 0, 0));
        ajouterClient.setText("Ajouter");
        ajouterClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajouterClientActionPerformed(evt);
            }
        });

        ModifierClient.setBackground(new java.awt.Color(255, 255, 255));
        ModifierClient.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        ModifierClient.setForeground(new java.awt.Color(0, 0, 0));
        ModifierClient.setText("Modifier");
        ModifierClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifierClientActionPerformed(evt);
            }
        });

        SupprimerClient.setBackground(new java.awt.Color(255, 255, 255));
        SupprimerClient.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        SupprimerClient.setForeground(new java.awt.Color(0, 0, 0));
        SupprimerClient.setText("Supprimer");
        SupprimerClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SupprimerClientActionPerformed(evt);
            }
        });

        ImprimerClient.setBackground(new java.awt.Color(255, 255, 255));
        ImprimerClient.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        ImprimerClient.setForeground(new java.awt.Color(0, 0, 0));
        ImprimerClient.setText("Imprimer");
        ImprimerClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImprimerClientActionPerformed(evt);
            }
        });

        RechercherClient.setBackground(new java.awt.Color(255, 255, 255));
        RechercherClient.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        RechercherClient.setForeground(new java.awt.Color(0, 0, 0));
        RechercherClient.setText("Rechercher");
        RechercherClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RechercherClientActionPerformed(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        jTable1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jTable1.setForeground(new java.awt.Color(0, 0, 0));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Numero Client", "Raison Social", "ICE", "Adresse", "Telephone", "Email"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 0, 0));
        jButton5.setText("Retour");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Client", "Produit", "Date Vent"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jButton6.setBackground(new java.awt.Color(255, 255, 255));
        jButton6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(0, 0, 0));
        jButton6.setText("Imprimer");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(255, 255, 255));
        jButton7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton7.setForeground(new java.awt.Color(0, 0, 0));
        jButton7.setText("Effacer");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ajouterClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(77, 77, 77)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(ModifierClient, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(26, 26, 26)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(SupprimerClient)
                                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(31, 31, 31)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addComponent(jLabel1)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(CodeClient)
                                    .addComponent(RaisonSocial)
                                    .addComponent(ICE)
                                    .addComponent(Adresse)
                                    .addComponent(Telephone)
                                    .addComponent(Email, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(61, 61, 61)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(31, 31, 31)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(232, 232, 232)
                                        .addComponent(ImprimerClient, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(213, 213, 213)
                                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(125, 125, 125)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(326, 326, 326)
                        .addComponent(RechercherClient)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(103, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel2)
                                                .addComponent(CodeClient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(28, 28, 28)
                                            .addComponent(jLabel3))
                                        .addComponent(RaisonSocial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(31, 31, 31)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(ICE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(28, 28, 28)
                                    .addComponent(jLabel5))
                                .addComponent(Adresse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(38, 38, 38)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(Telephone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6))))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(Email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ImprimerClient)
                            .addComponent(jButton6))))
                .addGap(61, 61, 61)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addGap(55, 55, 55)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SupprimerClient)
                    .addComponent(ModifierClient)
                    .addComponent(ajouterClient)
                    .addComponent(jButton7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RechercherClient)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ajouterClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajouterClientActionPerformed
dbb d = dbb.getCon();
ClientBean sb = new ClientBean();

// Check for null or empty text in each JTextField
//String codeClient = CodeClient.getText().trim();
String raisonSociale = RaisonSocial.getText().trim();
String ice = ICE.getText().trim();
String adresse = Adresse.getText().trim();
String telephone = Telephone.getText().trim();
String email = Email.getText().trim();

// Check if any field is empty
if ( raisonSociale.isEmpty() || ice.isEmpty() || adresse.isEmpty() || telephone.isEmpty() || email.isEmpty()) {
    JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs.", "Champs vides", JOptionPane.ERROR_MESSAGE);
} else {
    // Check if codeClient already exists in the database
   
        // All fields are filled, codeClient doesn't exist in the database, proceed to set properties
        //
        //sb.setCodeClient(codeClient);
        sb.setRaisonSociale(raisonSociale);
        sb.setICE(ice);
        sb.setAdresse(adresse);
        sb.setTelephon(telephone);
        sb.setEmail(email);

        // Check if DB connection is valid before using it
        if (d != null) {
            d.ajouterClient(sb);
        } else {
            JOptionPane.showMessageDialog(null, "La connexion à la base de données n'est pas disponible.", "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
        }
    afficherClient();
         afficherClient1();
}
 
    
     // TODO add your handling code here:
      
    }//GEN-LAST:event_ajouterClientActionPerformed

    private void SupprimerClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SupprimerClientActionPerformed
    dbb d = dbb.getCon();
ClientBean sb = new ClientBean();

sb.setCodeClient(CodeClient.getText());
sb.setRaisonSociale(RaisonSocial.getText());
sb.setICE(ICE.getText());
sb.setAdresse(Adresse.getText());
sb.setTelephon(Telephone.getText());
sb.setEmail(Email.getText());
d.SupClient(CodeClient.getText());
   
    //
        // TODO add your handling code here:
                        
afficherClient();
         afficherClient1();
   
    }//GEN-LAST:event_SupprimerClientActionPerformed

    private void ModifierClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModifierClientActionPerformed
   dbb b = dbb.getCon();
ClientBean sb = new ClientBean();
if (CodeClient.getText().isEmpty() || RaisonSocial.getText().isEmpty() || ICE.getText().isEmpty() ||
    Adresse.getText().isEmpty() || Telephone.getText().isEmpty() || Email.getText().isEmpty()) {
    JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs.", "Champs obligatoires", JOptionPane.ERROR_MESSAGE);
    return; // Sortir de la méthode si l'un des champs est vide
}
sb.setCodeClient(CodeClient.getText());
sb.setRaisonSociale(RaisonSocial.getText());
sb.setICE(ICE.getText());
sb.setAdresse(Adresse.getText());
sb.setTelephon(Telephone.getText());
sb.setEmail(Email.getText());
b.modifierClient(sb);
afficherClient();
         afficherClient1();
        // TODO add your handling code here:
                           
    }//GEN-LAST:event_ModifierClientActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        goToNextRecordVoid();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
goToPreviousRecord();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
goToFirstRecord();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
goToLastRecord();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        kkkk();
        int selectedrow=jTable1.getSelectedRow();
       CodeClient.setText((String)jTable1.getValueAt(selectedrow, 0));
        RaisonSocial.setText((String) jTable1.getValueAt(selectedrow, 1));
        ICE.setText(String.valueOf(jTable1.getValueAt(selectedrow, 2)));
        Adresse.setText(String.valueOf(jTable1.getValueAt(selectedrow, 3)));
        Telephone.setText(String.valueOf(jTable1.getValueAt(selectedrow, 4)));
        Email.setText(String.valueOf(jTable1.getValueAt(selectedrow, 5)));
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked

    private void CodeClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CodeClientActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CodeClientActionPerformed

    private void RechercherClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RechercherClientActionPerformed
      DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    jTable1.setRowSorter(sorter);

    String searchText = jTextField1.getText().toLowerCase(); // Convert to lowercase for case-insensitive search

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
    }//GEN-LAST:event_RechercherClientActionPerformed

    private void ImprimerClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImprimerClientActionPerformed
int[] selectedRows = jTable1.getSelectedRows();

try {
    // Load the JasperReport template
    JasperDesign jasdi;
    if (selectedRows.length == 1) {
        // If only one row is selected, use a different report template
        jasdi = JRXmlLoader.load("C:\\Users\\Administrator\\Desktop\\gestionCom - Copie\\src\\FicherClient.jrxml");
    } else {
        // If no row is selected or multiple rows are selected, use the original report template
        jasdi = JRXmlLoader.load("C:\\Users\\Administrator\\Desktop\\gestionCom - Copie\\src\\Client.jrxml");
    }

    // Construct the SQL query dynamically based on the selected rows
    StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Client");

    if (selectedRows.length == 1) {
        // If only one row is selected, add a condition to select that row
        String id = jTable1.getValueAt(selectedRows[0], 0).toString(); // Assuming the first column contains the id
        sqlBuilder.append(" WHERE CodeClient = '").append(id).append("'");
    } else if (selectedRows.length > 1) {
        // If multiple rows are selected, add conditions to select those rows
        sqlBuilder.append(" WHERE CodeClient IN (");
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
        // TODO add your handling code here:
    }//GEN-LAST:event_ImprimerClientActionPerformed

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
jTable1.clearSelection();
jTable2.clearSelection();
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
DefaultTableModel tbl = (DefaultTableModel) jTable1.getModel();
int row = jTable1.getSelectedRow();

try {
    HashMap<String, Object> param = new HashMap<>();

    if (row >= 0) {
        // Assuming 'GareName' is in the second column (index 1) of the table
        String gareName = String.valueOf(tbl.getValueAt(row, 0));
        param.put("s", gareName);

        JasperDesign jasdi = JRXmlLoader.load("C:\\Users\\Administrator\\Desktop\\gestionCom - Copie\\src\\clientbought.jrxml");

        // Compile the JasperReport template
        JasperReport js = JasperCompileManager.compileReport(jasdi);

        // Provide a database connection (replace 'con' with your actual Connection object)
        Connection con = ConnectioDB.getConnection();

        // Fill the JasperReport with data
        JasperPrint jd = JasperFillManager.fillReport(js, param, con);

        // Show the report in a JasperViewer
        JasperViewer.viewReport(jd, false);

    } else {
        JOptionPane.showMessageDialog(null, "Sélectionnez une ligne dans le tableau");
    }
} catch (JRException ex) {
    // Handle JasperReports exceptions
    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
    JOptionPane.showMessageDialog(null, "Erreur lors de la génération du rapport : " + ex.getMessage());
} catch (Exception ex) {
    // Handle any other exceptions
    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
    JOptionPane.showMessageDialog(null, "Une erreur est survenue : " + ex.getMessage());
}


        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
   ICE.setText("");
        RaisonSocial.setText("");
        Adresse.setText("");
        Telephone.setText("");
        Email.setText("");
      CodeClient.setText("");
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed
                                                     
     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Adresse;
    private javax.swing.JTextField CodeClient;
    private javax.swing.JTextField Email;
    private javax.swing.JTextField ICE;
    private javax.swing.JButton ImprimerClient;
    private javax.swing.JButton ModifierClient;
    private javax.swing.JTextField RaisonSocial;
    private javax.swing.JButton RechercherClient;
    private javax.swing.JButton SupprimerClient;
    private javax.swing.JTextField Telephone;
    private javax.swing.JButton ajouterClient;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

  

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
            java.util.logging.Logger.getLogger(Fournisseur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Fournisseur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Fournisseur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Fournisseur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Client().setVisible(true);
            }
        });
    }
   

   
   
}
