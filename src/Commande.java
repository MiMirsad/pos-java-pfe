
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
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
public class Commande extends javax.swing.JFrame {

    /**
     * Creates new form Commande
     */
    public Commande() {
        initComponents();
          loadRaisonSocaleIntoNumF();
          id();
          fetchProduit();
          updateJTable();
          fetchNextIDAndLoadIntoTextBox(IDCOMMAND);
             IDCOMMAND.setEditable(false);
    // Set the background color to grey
     prix.setEditable(false);
     prix.setBackground(Color.LIGHT_GRAY);
    IDCOMMAND.setBackground(Color.LIGHT_GRAY);
    }
    
    private void updateJTable() {
        System.out.println("Updating JTable..."); // Add this line for debugging
        try {
            dbb db = dbb.getCon(); // Get the database connection
            if (db.conx != null && !db.conx.isClosed()) { // Check if connection is not null and not closed
                
                    PreparedStatement pst = db.conx.prepareStatement("SELECT da.NumeroCmd, p.Désignation AS Produit, da.Prix, da.Quantité, f.RaisonSocale, fa.DateCommande\n" +
"FROM DetailCommand da\n" +
"INNER JOIN Produit p ON da.NumeroProduit = p.NumeroProduit\n" +
"INNER JOIN Command fa ON da.NumeroCmd = fa.NumeroCommande\n" +
"INNER JOIN Fournisseur f ON fa.NumFrs = f.NumFrs;");

                         //  pst.setInt(1,Integer.parseInt(idf.getText()));

                        ResultSet resultSet = pst.executeQuery();

                    DefaultTableModel df = (DefaultTableModel) jTable1.getModel();
                    df.setRowCount(0);

                    while (resultSet.next()) {
                        Vector<Object> rowVector = new Vector<>();
                        rowVector.add(resultSet.getString("RaisonSocale"));
                        rowVector.add(resultSet.getString("DateCommande"));
                        rowVector.add(resultSet.getString("NumeroCmd"));
                                                  
                        rowVector.add(resultSet.getString("Produit"));
                        rowVector.add(resultSet.getString("Prix"));
                        rowVector.add(resultSet.getString("Quantité"));

// Add RaisonSocale
                        df.addRow(rowVector);
                        System.out.println("Rows fetched: " + df.getRowCount());
                    }

                    // Notify JTable to refresh its view
                    df.fireTableDataChanged();
                }
             else {
                JOptionPane.showMessageDialog(null, "La connexion à la base de données est fermée.", "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Achat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      private void addItem(String item) {
        prod.addItem(item); // Use jComboBox2 directly instead of super
        if (item != null) {
            fetchPrix(item);
        }
    }
      
      private int prodd(String prod) {
        int id = 0; // Default value if no match is found
        dbb db = dbb.getCon(); // Get the database connection
        String query = "SELECT NumeroProduit FROM Produit WHERE Désignation = ?";
        try (PreparedStatement st = db.conx.prepareStatement(query)) {
            st.setString(1, prod);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                id = rs.getInt("NumeroProduit");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return id;
    }
private String fetchProductNameForId(int productId) {
        String productName = "";
        try {
            dbb db = dbb.getCon();
            if (db.conx != null) {
                String query = "SELECT Désignation FROM Produit WHERE NumeroProduit = ?";
                PreparedStatement pst = db.conx.prepareStatement(query);
                pst.setInt(1, productId);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    productName = rs.getString("Désignation");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return productName;
    }
    private void fetchProduit() {
        try {
            // Assuming DB.getCon().conx returns a valid database connection
            dbb db = dbb.getCon();
            if (db.conx != null) {
                String query = "SELECT NumeroProduit FROM Produit";
                PreparedStatement pst = db.conx.prepareStatement(query);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    int productId = rs.getInt("NumeroProduit");
                    String productName = fetchProductNameForId(productId);
                    prod.addItem(productName); // Add product designation to the combo box
                }
            } else {
                JOptionPane.showMessageDialog(null, "La connexion à la base de données n'est pas disponible.", "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des produits.", "Erreur SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String fetchProductName(String numeroProduit) {
        String productName = "";
        try {
            dbb db = dbb.getCon();
            if (db.conx != null) {
                String query = "SELECT Désignation FROM Produit WHERE NumeroProduit = ?";
                PreparedStatement pst = db.conx.prepareStatement(query);
                pst.setString(1, numeroProduit);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    productName = rs.getString("Désignation");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return productName;
    }

    private void fetchPrix(String selectedProductName) {
        System.out.println("Fetching price for product: " + selectedProductName); // Debug
        try {
            dbb db = dbb.getCon();
            if (db.conx != null) {
                String query = "SELECT Prix FROM Produit WHERE Désignation = ?";
                PreparedStatement pst = db.conx.prepareStatement(query);
                pst.setString(1, selectedProductName);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    String price = rs.getString("Prix");
                    prix.setText(price);
                    System.out.println("Price fetched: " + price); // Debug
                } else {
                    System.out.println("No price found for product: " + selectedProductName); // Debug
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
public void id() {
    try {
        // Assuming dbb.getCon().conx returns a valid database connection
        dbb db = dbb.getCon();
        if (db != null) {
            String query = "SELECT NumeroCommande FROM Command"; // Use DISTINCT to retrieve unique IDs directly from the database
            PreparedStatement pst = dbb.conx.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            IdA.removeAllItems(); // Clear the combobox before adding unique IDs

            while (rs.next()) {
                String numeroCommande = rs.getString("NumeroCommande");
                IdA.addItem(numeroCommande); // Add each unique ID to the combobox
            }
        } else {
            JOptionPane.showMessageDialog(null, "La connexion à la base de données n'est pas disponible.", "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des produits.", "Erreur SQL", JOptionPane.ERROR_MESSAGE);
    }
}

 private List<String> fetchNumFrs() {
        List<String> numFrsList = new ArrayList<>();
        dbb db = dbb.getCon(); // Get the database connection
        String query = "SELECT NumFrs FROM Fournisseur";
        try (PreparedStatement st = db.conx.prepareStatement(query); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                String numFrs = rs.getString("NumFrs");
                numFrsList.add(numFrs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return numFrsList;
    }
  private void loadRaisonSocaleIntoNumF() {
        // First, fetch NumFrs
        List<String> numFrsList = fetchNumFrs();

        // Then, fetch and match RaisonSocale for each NumFrs
        for (String numFrs : numFrsList) {
            String raisonSocale = matchRaisonSocale(numFrs);
            if (raisonSocale != null) {
                NumFrs.addItem(raisonSocale);
            }
        }
    }

// Method to match RaisonSocale for a given NumFrs
    private String matchRaisonSocale(String numFrs) {
        String raisonSocale = null;
        dbb db = dbb.getCon(); // Get the database connection
        String query = "SELECT RaisonSocale FROM Fournisseur WHERE NumFrs = ?";
        try (PreparedStatement st = db.conx.prepareStatement(query)) {
            st.setString(1, numFrs);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                raisonSocale = rs.getString("RaisonSocale");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return raisonSocale;
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
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        NumFrs = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        label1 = new java.awt.Label();
        label6 = new java.awt.Label();
        label7 = new java.awt.Label();
        label8 = new java.awt.Label();
        IdA = new javax.swing.JComboBox<>();
        label9 = new java.awt.Label();
        prod = new javax.swing.JComboBox<>();
        prix = new javax.swing.JTextField();
        qte = new javax.swing.JTextField();
        ajouterDetailCommande = new java.awt.Button();
        modifierDetailCommande = new java.awt.Button();
        supprimerDetailCommande = new java.awt.Button();
        imprrimerDetailCommande = new java.awt.Button();
        DateCommande = new com.github.lgooddatepicker.components.DatePicker();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        IDCOMMAND = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        datePicker1 = new com.github.lgooddatepicker.components.DatePicker();
        jButton7 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));
        jPanel1.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Commande");
        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(149, 21, -1, -1));

        jLabel3.setText("ID");
        jLabel3.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, -1, -1));

        jLabel4.setText("Date Commande");
        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 230, -1, -1));

        jButton5.setText("Ajouter");
        jButton5.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 350, -1, -1));

        jButton6.setText("Modifier");
        jButton6.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 350, 90, -1));

        jTextField1.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(343, 545, 154, -1));

        jButton9.setText("Rechercher");
        jButton9.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(225, 545, -1, -1));

        NumFrs.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        NumFrs.setToolTipText("");
        NumFrs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NumFrsActionPerformed(evt);
            }
        });
        jPanel1.add(NumFrs, new org.netbeans.lib.awtextra.AbsoluteConstraints(177, 182, 177, -1));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 0, 10, 570));

        label1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        label1.setText("Detail Commande");
        jPanel1.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(673, 10, 330, -1));

        label6.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        label6.setText("Id");
        jPanel1.add(label6, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 110, -1, -1));

        label7.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        label7.setText("Produit\n");
        jPanel1.add(label7, new org.netbeans.lib.awtextra.AbsoluteConstraints(593, 171, -1, -1));

        label8.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        label8.setText("Quantité");
        jPanel1.add(label8, new org.netbeans.lib.awtextra.AbsoluteConstraints(593, 263, -1, -1));

        jPanel1.add(IdA, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 110, 124, -1));

        label9.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        label9.setText("Prix");
        jPanel1.add(label9, new org.netbeans.lib.awtextra.AbsoluteConstraints(593, 211, -1, -1));

        prod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prodActionPerformed(evt);
            }
        });
        jPanel1.add(prod, new org.netbeans.lib.awtextra.AbsoluteConstraints(738, 171, 124, -1));

        prix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prixActionPerformed(evt);
            }
        });
        jPanel1.add(prix, new org.netbeans.lib.awtextra.AbsoluteConstraints(738, 211, 123, -1));
        jPanel1.add(qte, new org.netbeans.lib.awtextra.AbsoluteConstraints(738, 263, 123, -1));

        ajouterDetailCommande.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        ajouterDetailCommande.setLabel("Ajouter");
        ajouterDetailCommande.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajouterDetailCommandeActionPerformed(evt);
            }
        });
        jPanel1.add(ajouterDetailCommande, new org.netbeans.lib.awtextra.AbsoluteConstraints(603, 455, 105, 30));

        modifierDetailCommande.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        modifierDetailCommande.setLabel("Modifier");
        modifierDetailCommande.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifierDetailCommandeActionPerformed(evt);
            }
        });
        jPanel1.add(modifierDetailCommande, new org.netbeans.lib.awtextra.AbsoluteConstraints(743, 455, 95, -1));

        supprimerDetailCommande.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        supprimerDetailCommande.setLabel("Supprimer");
        supprimerDetailCommande.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supprimerDetailCommandeActionPerformed(evt);
            }
        });
        jPanel1.add(supprimerDetailCommande, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 450, -1, -1));

        imprrimerDetailCommande.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        imprrimerDetailCommande.setLabel("Imprimer");
        imprrimerDetailCommande.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imprrimerDetailCommandeActionPerformed(evt);
            }
        });
        jPanel1.add(imprrimerDetailCommande, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 450, -1, -1));
        jPanel1.add(DateCommande, new org.netbeans.lib.awtextra.AbsoluteConstraints(177, 229, 177, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Nom Fournissour", "Date Command", "ID ", "Produit", "Prix", "Qte"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(913, 67, -1, 210));
        jPanel1.add(IDCOMMAND, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 130, 180, -1));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Fournisser");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 185, -1, -1));

        jButton1.setText("Rechercher Par Date");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 540, -1, -1));
        jPanel1.add(datePicker1, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 540, -1, -1));

        jButton7.setText("Retour");
        jButton7.setBackground(new java.awt.Color(255, 255, 255));
        jButton7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton7.setForeground(new java.awt.Color(0, 0, 0));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 420, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void NumFrsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NumFrsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NumFrsActionPerformed

    private void prodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prodActionPerformed
        String selectedProduct = (String) prod.getSelectedItem();
        if (selectedProduct != null) {
            fetchPrix(selectedProduct);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_prodActionPerformed

    private void prixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prixActionPerformed
        String selectedProduct = (String) prod.getSelectedItem();
        if (selectedProduct != null) {
            fetchPrix(selectedProduct);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_prixActionPerformed

    private void ajouterDetailCommandeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajouterDetailCommandeActionPerformed
try {
    CommandeBeans sb = new CommandeBeans();
    
    // Check if IdA is selected
    if (IdA.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(null, "Veuillez sélectionner un numéro de commande.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method if the field is empty
    }
    
    // Check if prod is selected
    if (prod.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(null, "Veuillez sélectionner un produit.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method if the field is empty
    }

    // Check if prix is empty
    if (prix.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Veuillez entrer un prix.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method if the field is empty
    }

    // Check if qte is empty
    if (qte.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Veuillez entrer une quantité.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method if the field is empty
    }

    // Check if prix is an integer
    try {
        Integer.parseInt(prix.getText());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Le prix doit être un nombre entier.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method if prix is not an integer
    }

    // Check if qte is an integer
    try {
        Integer.parseInt(qte.getText());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "La quantité doit être un nombre entier.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method if qte is not an integer
    }

    int produitID = prodd(prod.getSelectedItem().toString());
    int id = Integer.parseInt((String) IdA.getSelectedItem());
    String prix1 = prix.getText();
    String qt = qte.getText();

    sb.setNumeroCommande(id);
    sb.setNumeroProduit(produitID);
    sb.setPrix(prix1);
    sb.setQuantité(qt);

    dbb db = dbb.getCon(); 
    db.ajouteDeatilCom(sb);

  updateJTable();
} catch (SQLException ex) {
    id();
    Logger.getLogger(Achat.class.getName()).log(Level.SEVERE, null, ex);
}


//        updateJTable();
    }//GEN-LAST:event_ajouterDetailCommandeActionPerformed
private int matchIdForNumFrs(String numFrsName) {
        int id = 0; // Default value if no match is found
        dbb db = dbb.getCon(); // Get the database connection
        String query = "SELECT NumFrs FROM Fournisseur WHERE RaisonSocale = ?";
        try (PreparedStatement st = db.conx.prepareStatement(query)) {
            st.setString(1, numFrsName);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                id = rs.getInt("NumFrs");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return id;
    }
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
try {
    dbb db = dbb.getCon();
    CommandeBeans sb = new CommandeBeans();

    // Check if NumFrs is selected
    if (NumFrs.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(null, "Veuillez sélectionner un fournisseur.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method if the field is empty
    }

    // Get selected item from NumF combo box and cast it to int
    String selectedNumFrs = (String) NumFrs.getSelectedItem();
    // Match the selected NumFrs name to get its corresponding Id
    int id = matchIdForNumFrs(selectedNumFrs);
    // Set the id to the AchatBeans object
    sb.setNumFrs(id);

    // Check if DateCommande is selected
    if (DateCommande.getDate() == null) {
        JOptionPane.showMessageDialog(null, "Veuillez sélectionner une date de commande.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method if the field is empty
    }

          LocalDate selectedDate = DateCommande.getDate();
        // Format the date into string using DateTimeFormatter
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Modify the pattern as needed
        String dat = selectedDate.format(dateFormatter);
        // Set the formatted date to AchatBeans object
        sb.setDateCommande(dat);
    System.out.println(id);
    db.ajouteCommand(sb);
    id();
     fetchNextIDAndLoadIntoTextBox(IDCOMMAND);
} catch (SQLException ex) {
    Logger.getLogger(Achat.class.getName()).log(Level.SEVERE, null, ex);
}


        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        dbb db = dbb.getCon(); // TODO add your handling code here:
        CommandeBeans sb = new CommandeBeans();
        // Check if IDCMD is empty
        if (IDCOMMAND.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer un numéro de commande.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return; // Exit the method if the field is empty
        }
        sb.setNumeroCommande(Integer.valueOf(IDCOMMAND.getText()));
        // Check if NumFrs is selected
        if (NumFrs.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner un fournisseur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return; // Exit the method if the field is empty
        }
        // Get selected item from NumF combo box and cast it to int
        String selectedNumFrs = (String) NumFrs.getSelectedItem();
        // Match the selected NumFrs name to get its corresponding Id
        int id = matchIdForNumFrs(selectedNumFrs);
        // Set the id to the AchatBeans object
        sb.setNumFrs(id);
        // Check if DateCommande is selected
        if (DateCommande.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner une date de commande.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return; // Exit the method if the field is empty
        }
        // Get the selected date from Datee
        LocalDate selectedDate = DateCommande.getDate();
        // Format the date into string using DateTimeFormatter
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Modify the pattern as needed
        String dat = selectedDate.format(dateFormatter);
        // Set the formatted date to AchatBeans object
        sb.setDateCommande(dat);
        sb.modifierCommand(sb);
        updateJTable();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
  int selectedRow = jTable1.getSelectedRow();

// Check if a row is actually selected
if (selectedRow != -1) {
    // Assuming dtFactAch is a JTable
    // Set the selected item in IdA combo box
    Object idAValue = jTable1.getValueAt(selectedRow, 0);
    if (idAValue != null) {
        NumFrs.setSelectedItem(idAValue.toString());
    }

Object prodValue = jTable1.getValueAt(selectedRow, 1);
DateCommande.setText((java.lang.String) prodValue);
    // Set the text of prix text field
    Object prixValue = jTable1.getValueAt(selectedRow, 2);
    if (prixValue != null) {
        IdA.setSelectedItem(prixValue);
      IDCOMMAND.setText((prixValue).toString());
    }

    // Set the text of qte text field
    Object qteValue = jTable1.getValueAt(selectedRow, 3);
    if (qteValue != null) {
        prod.setSelectedItem(qteValue);
    }
Object price = jTable1.getValueAt(selectedRow, 4);
if (price != null) {
    prix.setText((price).toString()); // Use String.valueOf to convert the object to a string
}
Object qt = jTable1.getValueAt(selectedRow, 5);
if (qt != null) {
    qte.setText((qt).toString()); // Use String.valueOf to convert the object to a string
}
    
    
    
} else {
    // No row selected, handle this case if needed
}
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked
public void fetchNextIDAndLoadIntoTextBox(JTextField id) {
    dbb db = dbb.getCon();  
    String sql = "SELECT MAX(NumeroCommande) AS max_id FROM Command";

    try (
        PreparedStatement stmt = db.conx.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

        if (rs.next()) {
            int maxId = rs.getInt("max_id");
            int nextId = maxId + 1;
            IDCOMMAND.setText(Integer.toString(nextId));
        } else {
            // Handle the case where no result is returned
            IDCOMMAND.setText(""); // Clear the text box
        }

    } catch (SQLException ex) {
        // Handle SQL exception
        ex.printStackTrace();
    }
}
    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
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
        }
    });       
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void modifierDetailCommandeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifierDetailCommandeActionPerformed
dbb b = dbb.getCon();
CommandeBeans sb = new CommandeBeans();
int produitID = prodd(prod.getSelectedItem().toString());

String qt = qte.getText();
String prixText = prix.getText();

// Check if any of the required fields are empty
if (IdA.getSelectedItem() == null || qt.isEmpty() || prixText.isEmpty()) {
    JOptionPane.showMessageDialog(null, "Certains champs sont vides.", "Erreur", JOptionPane.ERROR_MESSAGE);
} else {
    int id = Integer.parseInt((String) IdA.getSelectedItem());

    sb.setNumeroCommande(id);
    sb.setNumeroProduit(produitID);
    sb.setPrix(prixText);
    sb.setQuantité(qt);
    sb.modifierCommandDetail(sb);
    updateJTable(); // TODO add your handling code here:
}

        // TODO add your handling code here:
    }//GEN-LAST:event_modifierDetailCommandeActionPerformed

    private void supprimerDetailCommandeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supprimerDetailCommandeActionPerformed
   dbb d = dbb.getCon();
CommandeBeans sb = new CommandeBeans();

//sb.setId(IDFACTUR.getText());
   sb.setNumeroCommande(Integer.valueOf(IDCOMMAND.getText()));

sb.SupComman(Integer.valueOf(IDCOMMAND.getText()));
sb.SupCommandDetail(Integer.valueOf(IDCOMMAND.getText()));
   
    //
        // TODO add your handling code here:
                        
updateJTable();
  fetchNextIDAndLoadIntoTextBox(IDCOMMAND);
        // TODO add your handling code here:
    }//GEN-LAST:event_supprimerDetailCommandeActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
 // Assuming datePicker1 is an instance of a date picker that returns LocalDate
    LocalDate localDate = datePicker1.getDate();
    Date selectedDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust the format as needed
    String formattedSelectedDate = dateFormat.format(selectedDate);

    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    jTable1.setRowSorter(sorter);

    sorter.setRowFilter(new RowFilter<DefaultTableModel, Object>() {
        @Override
        public boolean include(RowFilter.Entry<? extends DefaultTableModel, ? extends Object> entry) {
            for (int i = 0; i < entry.getValueCount(); i++) {
                String tableDateValue = entry.getStringValue(i);
                if (tableDateValue.equals(formattedSelectedDate)) {
                    return true;
                }
            }
            return false;
        }
    });
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void imprrimerDetailCommandeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imprrimerDetailCommandeActionPerformed
HashMap<String, Object> param = new HashMap<>();
int selectedRow = jTable1.getSelectedRow();

try {
    // Load Command.jrxml
    JasperDesign jasdi;
    JasperReport js;
    Connection con = ConnectioDB.getConnection(); // Replace ConnectioDB.getConnection() with your actual database connection method

    if (selectedRow != -1) { // Check if any row is selected
    int idvent = Integer.parseInt(jTable1.getValueAt(selectedRow, 2).toString());

        param.put("s", idvent);
        
        jasdi = JRXmlLoader.load("C:\\Users\\Administrator\\Desktop\\gestionCom - Copie\\src\\cmd0.jrxml");
    } else {
        jasdi = JRXmlLoader.load("C:\\Users\\Administrator\\Desktop\\GestionDeStock\\src\\command1.jrxml");
    }
    
    // Compile the JasperReport template
    js = JasperCompileManager.compileReport(jasdi);

    // Fill the JasperReport with data
    JasperPrint jd = JasperFillManager.fillReport(js, param, con);

    // Show the report in a JasperViewer
    JasperViewer.viewReport(jd, false);

} catch (JRException ex) {
    Logger.getLogger(Vente.class.getName()).log(Level.SEVERE, null, ex);
}
      
        // TODO add your handling code here:
    }//GEN-LAST:event_imprrimerDetailCommandeActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
jTable1.clearSelection();
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1MouseClicked

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
            java.util.logging.Logger.getLogger(Commande.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Commande.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Commande.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Commande.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Commande().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.github.lgooddatepicker.components.DatePicker DateCommande;
    private javax.swing.JTextField IDCOMMAND;
    private javax.swing.JComboBox<String> IdA;
    private javax.swing.JComboBox<String> NumFrs;
    private java.awt.Button ajouterDetailCommande;
    private com.github.lgooddatepicker.components.DatePicker datePicker1;
    private java.awt.Button imprrimerDetailCommande;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private java.awt.Label label1;
    private java.awt.Label label6;
    private java.awt.Label label7;
    private java.awt.Label label8;
    private java.awt.Label label9;
    private java.awt.Button modifierDetailCommande;
    private javax.swing.JTextField prix;
    private javax.swing.JComboBox<String> prod;
    private javax.swing.JTextField qte;
    private java.awt.Button supprimerDetailCommande;
    // End of variables declaration//GEN-END:variables

}