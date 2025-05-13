
import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author PC
 */
public class BonLivraisonFournisseur extends javax.swing.JFrame {

    /**
     * Creates new form BonLivraisonFournisseur
     */
    public BonLivraisonFournisseur() {
        initComponents();
           //Four();
        fetchProduit();
//    afficherFournisseur();
   //     id();
       prix1.setEditable(false);
     prix1.setBackground(Color.LIGHT_GRAY);
   IDBN.setEditable(false);
    // Set the background color to grey
    IDBN.setBackground(Color.LIGHT_GRAY);
  fetchNextIDAndLoadIntoTextBox(IDBN);
        updateJTable();
        loadRaisonSocaleIntoNumF();
        
      id();
    }
    public void fetchNextIDAndLoadIntoTextBox(JTextField id) {
    dbb db = dbb.getCon();  
    String sql = "SELECT MAX(ID) AS max_id FROM BLFour";

    try (
        PreparedStatement stmt = db.conx.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

        if (rs.next()) {
            int maxId = rs.getInt("max_id");
            int nextId = maxId + 1;
            IDBN.setText(Integer.toString(nextId));
        } else {
            // Handle the case where no result is returned
            IDBN.setText(""); // Clear the text box
        }

    } catch (SQLException ex) {
        // Handle SQL exception
        ex.printStackTrace();
    }
}

      private void updateJTable() {
        System.out.println("Updating JTable..."); // Add this line for debugging
        try {
            dbb db = dbb.getCon(); // Get the database connection
            if (db.conx != null && !db.conx.isClosed()) { // Check if connection is not null and not closed
                
                    PreparedStatement pst = db.conx.prepareStatement("SELECT da.ID, p.Désignation AS Produit, da.PrixAchat, da.QteAchat, f.RaisonSocale, fa.DateBLf\n" +
"FROM DetailBonLV da\n" +
"INNER JOIN Produit p ON da.NumPro = p.NumeroProduit\n" +
"INNER JOIN BLFour fa ON da.ID = fa.ID\n" +
"INNER JOIN Fournisseur f ON fa.Nfour = f.NumFrs;");

                         //  pst.setInt(1,Integer.parseInt(idf.getText()));

                        ResultSet resultSet = pst.executeQuery();

                    DefaultTableModel df = (DefaultTableModel) jTable1.getModel();
                    df.setRowCount(0);

                    while (resultSet.next()) {
                        Vector<Object> rowVector = new Vector<>();
                           rowVector.add(resultSet.getString("RaisonSocale"));
                              rowVector.add(resultSet.getString("DateBLf"));
                                                rowVector.add(resultSet.getString("ID"));
                                                  
                        rowVector.add(resultSet.getString("Produit"));
                        rowVector.add(resultSet.getString("PrixAchat"));
                        rowVector.add(resultSet.getString("QteAchat"));

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
     private void loadRaisonSocaleIntoNumF() {
        // First, fetch NumFrs
        List<String> numFrsList = fetchNumFrs();

        // Then, fetch and match RaisonSocale for each NumFrs
        for (String numFrs : numFrsList) {
            String raisonSocale = matchRaisonSocale(numFrs);
            if (raisonSocale != null) {
                NumF.addItem(raisonSocale);
            }
        }
    }
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
 
public void id() {
    try {
        // Assuming dbb.getCon().conx returns a valid database connection
        dbb db = dbb.getCon();
        if (db != null) {
            String query = "SELECT DISTINCT ID FROM BLFour"; // Use DISTINCT to retrieve unique IDs directly from the database
            PreparedStatement pst = dbb.conx.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            IdA.removeAllItems(); // Clear the combobox before adding unique IDs

            while (rs.next()) {
                String id = rs.getString("ID");
                IdA.addItem(id); // Add each unique ID to the combobox
            }
        } else {
            JOptionPane.showMessageDialog(null, "La connexion à la base de données n'est pas disponible.", "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des produits.", "Erreur SQL", JOptionPane.ERROR_MESSAGE);
    }
}
// Method to fetch NumFrs
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
                    prix1.setText(price);
                    System.out.println("Price fetched: " + price); // Debug
                } else {
                    System.out.println("No price found for product: " + selectedProductName); // Debug
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        AjouterBonLivFou = new javax.swing.JButton();
        ModifierBonLivFou = new javax.swing.JButton();
        jTextField5 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        prix1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        prod = new javax.swing.JComboBox<>();
        jButton9 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        AjouterDetailBonLivFour = new javax.swing.JButton();
        ModifierDetailBonLivFour = new javax.swing.JButton();
        SupprimerDetailBonLivFour = new javax.swing.JButton();
        NumF = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        IdA = new javax.swing.JComboBox<>();
        datee = new com.github.lgooddatepicker.components.DatePicker();
        qte = new javax.swing.JTextField();
        IDBN = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        datePicker1 = new com.github.lgooddatepicker.components.DatePicker();
        jButton11 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setText("Bon Livraison Fournisseur");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, -1, 30));

        jLabel2.setText("Prix Unitaire");
        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 190, -1, 10));

        jLabel3.setText("Date ");
        jLabel3.setBackground(new java.awt.Color(0, 0, 0));
        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, -1, -1));

        jLabel4.setText("Nom Fournisseur");
        jLabel4.setBackground(new java.awt.Color(0, 0, 0));
        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, 30));

        AjouterBonLivFou.setText("Ajouter");
        AjouterBonLivFou.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        AjouterBonLivFou.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AjouterBonLivFouActionPerformed(evt);
            }
        });
        jPanel2.add(AjouterBonLivFou, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 340, 119, -1));

        ModifierBonLivFou.setText("Modifier");
        ModifierBonLivFou.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        ModifierBonLivFou.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifierBonLivFouActionPerformed(evt);
            }
        });
        jPanel2.add(ModifierBonLivFou, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 340, 117, -1));

        jTextField5.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jPanel2.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 610, 145, -1));

        jLabel7.setText("Produit");
        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 130, -1, -1));

        prix1.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        prix1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prix1ActionPerformed(evt);
            }
        });
        jPanel2.add(prix1, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 180, 170, 30));

        jLabel8.setText("Qte");
        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 240, -1, -1));

        prod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        prod.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        prod.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                prodItemStateChanged(evt);
            }
        });
        prod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prodActionPerformed(evt);
            }
        });
        jPanel2.add(prod, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 120, 170, 30));

        jButton9.setText("Rechercher");
        jButton9.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 610, -1, -1));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel5.setText("Detail Bon Livraison Fournisseur");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 20, -1, 30));

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel2.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 30, -1, 560));

        AjouterDetailBonLivFour.setText("Ajouter");
        AjouterDetailBonLivFour.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        AjouterDetailBonLivFour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AjouterDetailBonLivFourActionPerformed(evt);
            }
        });
        jPanel2.add(AjouterDetailBonLivFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 350, 119, -1));

        ModifierDetailBonLivFour.setText("Modifier");
        ModifierDetailBonLivFour.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        ModifierDetailBonLivFour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifierDetailBonLivFourActionPerformed(evt);
            }
        });
        jPanel2.add(ModifierDetailBonLivFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 350, 117, -1));

        SupprimerDetailBonLivFour.setText("Supprimer");
        SupprimerDetailBonLivFour.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        SupprimerDetailBonLivFour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SupprimerDetailBonLivFourActionPerformed(evt);
            }
        });
        jPanel2.add(SupprimerDetailBonLivFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 350, -1, -1));

        jPanel2.add(NumF, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 160, 210, -1));

        jLabel9.setText("Id");
        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, -1));

        IdA.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        IdA.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        IdA.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                IdAItemStateChanged(evt);
            }
        });
        IdA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IdAActionPerformed(evt);
            }
        });
        jPanel2.add(IdA, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 70, 170, 30));
        jPanel2.add(datee, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 230, 210, -1));
        jPanel2.add(qte, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 240, 170, -1));
        jPanel2.add(IDBN, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 110, 210, -1));

        jLabel10.setText("Id");
        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 70, -1, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Raison Socale", "Date", "ID", "Produit", "Prix Achat", "Qte Achat"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 70, -1, 230));
        jPanel2.add(datePicker1, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 610, -1, -1));

        jButton11.setText("Rechercher");
        jButton11.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 610, -1, -1));

        jButton5.setText("Retour");
        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 0, 0));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 340, -1, -1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1661, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 704, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        int selectedRow = jTable1.getSelectedRow();

        // Check if a row is actually selected
        if (selectedRow != -1) {
            // Assuming dtFactAch is a JTable
            // Set the selected item in IdA combo box
            Object idAValue = jTable1.getValueAt(selectedRow, 0);
            if (idAValue != null) {
                NumF.setSelectedItem(idAValue.toString());
            }

            Object prodValue = jTable1.getValueAt(selectedRow, 1);
            datee.setText((java.lang.String) prodValue);
            // Set the text of prix text field
            Object prixValue = jTable1.getValueAt(selectedRow, 2);
            if (prixValue != null) {
                IdA.setSelectedItem(prixValue);
                IDBN.setText((prixValue).toString());
            }

            // Set the text of qte text field
            Object qteValue = jTable1.getValueAt(selectedRow, 3);
            if (qteValue != null) {
                prod.setSelectedItem(qteValue);
            }
            Object price = jTable1.getValueAt(selectedRow, 4);
            if (price != null) {
                prix1.setText((price).toString()); // Use String.valueOf to convert the object to a string
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

    private void IdAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IdAActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IdAActionPerformed

    private void IdAItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_IdAItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_IdAItemStateChanged

    private void SupprimerDetailBonLivFourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SupprimerDetailBonLivFourActionPerformed

        dbb d = dbb.getCon();
        BLFBeans sb = new BLFBeans();

        //sb.setId(IDFACTUR.getText());
        sb.setId(Integer.valueOf(IDBN.getText()));

        sb.SupBon(Integer.valueOf(IDBN.getText()));
        sb.SupBonDetail(Integer.valueOf(IDBN.getText()));

        //
        // TODO add your handling code here:

        updateJTable();
        fetchNextIDAndLoadIntoTextBox(IDBN);
        // TODO add your handling code here:
    }//GEN-LAST:event_SupprimerDetailBonLivFourActionPerformed

    private void ModifierDetailBonLivFourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModifierDetailBonLivFourActionPerformed
      dbb b = dbb.getCon();
    BLFBeans sb = new BLFBeans();
    int produitID = prodd(prod.getSelectedItem().toString()); // Retrieve product ID
    int id = Integer.parseInt((String) IdA.getSelectedItem()); // Retrieve selected ID
    String qt = qte.getText(); // Retrieve quantity from the text field

    sb.setId(id);
    sb.setPrix(prix1.getText()); // Retrieve price from the text field
    sb.setQte(qt);
    sb.setProduit(produitID);

    // Call the method to modify the detail
    sb.modifierDetailBon(sb);

    // Update the table and fetch the next ID
    updateJTable();
    fetchNextIDAndLoadIntoTextBox(IDBN);
        // TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_ModifierDetailBonLivFourActionPerformed

    private void AjouterDetailBonLivFourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AjouterDetailBonLivFourActionPerformed
        try {
            int produitID = prodd(prod.getSelectedItem().toString());
            int id = Integer.parseInt((String) IdA.getSelectedItem());
            String qt = qte.getText();

            // Create AchatBeans object
            BLFBeans sb = new BLFBeans(); // Create AchatBeans object here
            sb.setId(id);
            sb.setProduit(produitID);
            sb.setPrix(prix1.getText());
            sb.setQte(qt);

            sb.ajouteBONdetails(sb);
            fetchNextIDAndLoadIntoTextBox(IDBN);
            updateJTable();
            // Show confirmation message in French
            //    JOptionPane.showMessageDialog(this, "Achat ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            Logger.getLogger(Achat.class.getName()).log(Level.SEVERE, null, ex);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_AjouterDetailBonLivFourActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
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
    }//GEN-LAST:event_jButton9ActionPerformed

    private void prodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prodActionPerformed
        String selectedProduct = (String) prod.getSelectedItem();
        if (selectedProduct != null) {
            fetchPrix(selectedProduct);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_prodActionPerformed

    private void prodItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_prodItemStateChanged

        // TODO add your handling code here:
    }//GEN-LAST:event_prodItemStateChanged

    private void prix1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prix1ActionPerformed
        String selectedProduct = (String) prod.getSelectedItem();
        if (selectedProduct != null) {
            fetchPrix(selectedProduct);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_prix1ActionPerformed

    private void ModifierBonLivFouActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModifierBonLivFouActionPerformed
        // Check if IDFACTUR is not empty
        if (IDBN.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner un ID.", "Champ requis", JOptionPane.WARNING_MESSAGE);
            return; // Exit method if IDFACTUR is empty
        }
        //dbb db = dbb.getCon();
        BLFBeans sb = new BLFBeans();
        // Set the ID to the AchatBeans object
        sb.setId(Integer.valueOf(IDBN.getText()));
        // Check if NumF is selected
        if (NumF.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner un numéro de fournisseur.", "Champ requis", JOptionPane.WARNING_MESSAGE);
            return; // Exit method if NumF is not selected
        }
        // Get selected item from NumF combo box and cast it to int
        String selectedNumFrs = (String) NumF.getSelectedItem();
        // Match the selected NumFrs name to get its corresponding Id
        int id = matchIdForNumFrs(selectedNumFrs);
        // Set the id to the AchatBeans object
        sb.setNumF(id);
        // Check if datee is selected
        if (datee.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner une date.", "Champ requis", JOptionPane.WARNING_MESSAGE);
            return; // Exit method if datee is not selected
        }
        // Get the selected date from Datee
        LocalDate selectedDate = datee.getDate();
        // Format the date into string using DateTimeFormatter
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Modify the pattern as needed
        String dat = selectedDate.format(dateFormatter);
        // Set the formatted date to AchatBeans object
        sb.setDatee(dat);
        sb.modifierBONliv(sb);
        fetchNextIDAndLoadIntoTextBox(IDBN);
        updateJTable();
        // TODO add your handling code here:
    }//GEN-LAST:event_ModifierBonLivFouActionPerformed

    private void AjouterBonLivFouActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AjouterBonLivFouActionPerformed
        try {
            dbb db = dbb.getCon();
            BLFBeans sb = new BLFBeans();

            // Check if NumF is selected
            if (NumF.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Veuillez sélectionner un numéro de fournisseur.", "Champ requis", JOptionPane.WARNING_MESSAGE);
                return; // Exit method if NumF is not selected
            }

            // Get selected item from NumF combo box and cast it to int
            String selectedNumFrs = (String) NumF.getSelectedItem();
            // Match the selected NumFrs name to get its corresponding Id
            int id = matchIdForNumFrs(selectedNumFrs);
            // Set the id to the AchatBeans object
            sb.setNumF(id);

            // Check if datee is selected
            if (datee.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Veuillez sélectionner une date.", "Champ requis", JOptionPane.WARNING_MESSAGE);
                return; // Exit method if datee is not selected
            }

            // Get the selected date from Datee
            LocalDate selectedDate = datee.getDate();
            // Format the date into string using DateTimeFormatter
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
            String dat = selectedDate.format(dateFormatter);
            // Set the formatted date to AchatBeans object
            sb.setDatee(dat);

            System.out.println(id);
            sb.ajouteBlf(sb);

            id();
            fetchNextIDAndLoadIntoTextBox(IDBN);
        } catch (SQLException ex) {
            Logger.getLogger(Achat.class.getName()).log(Level.SEVERE, null, ex);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_AjouterBonLivFouActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        jTable1.setRowSorter(sorter);

        String searchText = jTextField5.getText().toLowerCase(); // Convert to lowercase for case-insensitive search

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
        });        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

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
            java.util.logging.Logger.getLogger(BonLivraisonFournisseur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BonLivraisonFournisseur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BonLivraisonFournisseur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BonLivraisonFournisseur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BonLivraisonFournisseur().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AjouterBonLivFou;
    private javax.swing.JButton AjouterDetailBonLivFour;
    private javax.swing.JTextField IDBN;
    private javax.swing.JComboBox<String> IdA;
    private javax.swing.JButton ModifierBonLivFou;
    private javax.swing.JButton ModifierDetailBonLivFour;
    private javax.swing.JComboBox<String> NumF;
    private javax.swing.JButton SupprimerDetailBonLivFour;
    private com.github.lgooddatepicker.components.DatePicker datePicker1;
    private com.github.lgooddatepicker.components.DatePicker datee;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField prix1;
    private javax.swing.JComboBox<String> prod;
    private javax.swing.JTextField qte;
    // End of variables declaration//GEN-END:variables
}
