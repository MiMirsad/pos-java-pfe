
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
public class Devis extends javax.swing.JFrame {

    /**
     * Creates new form Devis
     */
    public Devis() {
        initComponents();
        updateJTable();
        loadRaisonSocaleIntoCodeClient();
        // Other initialization code
        fetchProduit();
        id();
            IDDEVIS.setEditable(false);
    // Set the background color to grey
        Prix1.setEditable(false);
     Prix1.setBackground(Color.LIGHT_GRAY);
    IDDEVIS.setBackground(Color.LIGHT_GRAY);
        fetchNextIDAndLoadIntoTextBox(IDDEVIS);
    }
   private void updateJTable() {
    System.out.println("Updating JTable..."); // Add this line for debugging
    try {
        dbb db = dbb.getCon(); // Get the database connection
        if (db.conx != null && !db.conx.isClosed()) { // Check if connection is not null and not closed
            PreparedStatement pst = db.conx.prepareStatement(
                "SELECT dd.id, p.Désignation AS Produit, dd.Prix, dd.Quantité, c.RaisonSocial, d.DateDevis " +
                "FROM DetailDevis dd " +
                "INNER JOIN Produit p ON dd.NumeroProduit = p.NumeroProduit " +
                "INNER JOIN Devis d ON dd.id = d.id " +
                "INNER JOIN Client c ON d.NumClient = c.CodeClient;"
            );

            ResultSet resultSet = pst.executeQuery();

            DefaultTableModel df = (DefaultTableModel) jTable1.getModel();
            df.setRowCount(0);

            while (resultSet.next()) {
                Vector<Object> rowVector = new Vector<>();
                rowVector.add(resultSet.getString("RaisonSocial"));
                rowVector.add(resultSet.getString("DateDevis"));
                rowVector.add(resultSet.getString("id"));
                rowVector.add(resultSet.getString("Produit"));
                rowVector.add(resultSet.getString("Prix"));
                rowVector.add(resultSet.getString("Quantité"));

                df.addRow(rowVector);
                System.out.println("Rows fetched: " + df.getRowCount());
            }

            // Notify JTable to refresh its view
            df.fireTableDataChanged();
        } else {
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
    public void id() {
    try {
        // Assuming dbb.getCon().conx returns a valid database connection
        dbb db = dbb.getCon();
        if (db != null) {
            String query = "SELECT DISTINCT ID FROM Devis"; // Use DISTINCT to retrieve unique IDs directly from the database
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
public void fetchNextIDAndLoadIntoTextBox(JTextField id) {
    dbb db = dbb.getCon();  
    String sql = "SELECT MAX(id) AS max_id FROM Devis";

    try (
        PreparedStatement stmt = db.conx.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

        if (rs.next()) {
            int maxId = rs.getInt("max_id");
            int nextId = maxId + 1;
            IDDEVIS.setText(Integer.toString(nextId));
        } else {
            // Handle the case where no result is returned
            IDDEVIS.setText(""); // Clear the text box
        }

    } catch (SQLException ex) {
        // Handle SQL exception
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
                    Prix1.setText(price);
                    System.out.println("Price fetched: " + price); // Debug
                } else {
                    System.out.println("No price found for product: " + selectedProductName); // Debug
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
      private int matchIdForCodeClient(String numFrsName) {
        int id = 0; // Default value if no match is found
        dbb db = dbb.getCon(); // Get the database connection
        String query = "SELECT CodeClient FROM Client WHERE RaisonSocial = ?";
        try (PreparedStatement st = db.conx.prepareStatement(query)) {
            st.setString(1, numFrsName);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                id = rs.getInt("CodeClient");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return id;
    }

// Method to fetch NumFrs
    private List<String> fetchCodeClient() {
        List<String> numFrsList = new ArrayList<>();
        dbb db = dbb.getCon(); // Get the database connection
        String query = "SELECT CodeClient FROM Client";
        try (PreparedStatement st = db.conx.prepareStatement(query); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                String numFrs = rs.getString("CodeClient");
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
        String query = "SELECT RaisonSocial FROM Client WHERE CodeClient = ?";
        try (PreparedStatement st = db.conx.prepareStatement(query)) {
            st.setString(1, numFrs);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                raisonSocale = rs.getString("RaisonSocial");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return raisonSocale;
    }
       private void loadRaisonSocaleIntoCodeClient() {
        // First, fetch NumFrs
        List<String> numFrsList = fetchCodeClient();

        // Then, fetch and match RaisonSocale for each NumFrs
        for (String numFrs : numFrsList) {
            String raisonSocale = matchRaisonSocale(numFrs);
            if (raisonSocale != null) {
                Client.addItem(raisonSocale);
            }
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
        AjouterDevis = new javax.swing.JButton();
        ModifierDevis = new javax.swing.JButton();
        jTextField5 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        Qte = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        prod = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        AjouterDetailDevis = new javax.swing.JButton();
        ModifierDetailDevis = new javax.swing.JButton();
        SupprimerDetailDevis = new javax.swing.JButton();
        ImprimerDetailDevis = new javax.swing.JButton();
        Client = new javax.swing.JComboBox<>();
        Datee = new com.github.lgooddatepicker.components.DatePicker();
        IdA = new javax.swing.JComboBox<>();
        Prix1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        IDDEVIS = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        datePicker1 = new com.github.lgooddatepicker.components.DatePicker();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Devis");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, -1, -1));

        jLabel2.setText("Prix Unitaire");
        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 200, -1, 20));

        jLabel3.setText("Date Devis");
        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, -1));

        jLabel4.setText("Nom Client");
        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));

        AjouterDevis.setText("Ajouter");
        AjouterDevis.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        AjouterDevis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AjouterDevisActionPerformed(evt);
            }
        });
        jPanel1.add(AjouterDevis, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 330, 119, -1));

        ModifierDevis.setText("Modifier");
        ModifierDevis.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        ModifierDevis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifierDevisActionPerformed(evt);
            }
        });
        jPanel1.add(ModifierDevis, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 330, 117, -1));

        jTextField5.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jPanel1.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 450, 145, -1));

        jLabel7.setText("Produit");
        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 150, -1, -1));

        Qte.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jPanel1.add(Qte, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 250, 170, -1));

        jLabel8.setText("Qte");
        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 250, -1, -1));

        prod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        prod.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        prod.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                prodItemStateChanged(evt);
            }
        });
        prod.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                prodMouseClicked(evt);
            }
        });
        prod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prodActionPerformed(evt);
            }
        });
        jPanel1.add(prod, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 150, 170, 30));

        jLabel9.setText("ID Devis");
        jLabel9.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 110, -1, 20));

        jButton9.setText("Rechercher");
        jButton9.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, -1, -1));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Detail Devis");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 40, -1, -1));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(553, 30, -1, 560));

        AjouterDetailDevis.setText("Ajouter");
        AjouterDetailDevis.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        AjouterDetailDevis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AjouterDetailDevisActionPerformed(evt);
            }
        });
        jPanel1.add(AjouterDetailDevis, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 330, 119, 30));

        ModifierDetailDevis.setText("Modifier");
        ModifierDetailDevis.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        ModifierDetailDevis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifierDetailDevisActionPerformed(evt);
            }
        });
        jPanel1.add(ModifierDetailDevis, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 330, 117, -1));

        SupprimerDetailDevis.setText("Supprimer");
        SupprimerDetailDevis.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        SupprimerDetailDevis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SupprimerDetailDevisActionPerformed(evt);
            }
        });
        jPanel1.add(SupprimerDetailDevis, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 330, -1, -1));

        ImprimerDetailDevis.setText("Imprimer");
        ImprimerDetailDevis.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        ImprimerDetailDevis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImprimerDetailDevisActionPerformed(evt);
            }
        });
        jPanel1.add(ImprimerDetailDevis, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 330, 116, -1));

        jPanel1.add(Client, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 180, 210, 30));
        jPanel1.add(Datee, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 260, 210, -1));

        jPanel1.add(IdA, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 110, 170, -1));
        jPanel1.add(Prix1, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 200, 170, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "RaisonSocial", "Date Devis", "id", "Produit", "Prix", "Quantité"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true
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

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 100, 490, 220));
        jPanel1.add(IDDEVIS, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 130, 200, -1));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("ID");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 40, -1));

        jButton1.setText("Rechercher Par Date");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 450, -1, -1));
        jPanel1.add(datePicker1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 450, -1, -1));

        jButton5.setText("Retour");
        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 0, 0));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 390, -1, -1));

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
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 704, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void prodItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_prodItemStateChanged



        // TODO add your handling code here:
    }//GEN-LAST:event_prodItemStateChanged

    private void ImprimerDetailDevisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImprimerDetailDevisActionPerformed
HashMap<String, Object> param = new HashMap<>();
int selectedRow = jTable1.getSelectedRow();

try {
    // Load Command.jrxml
    JasperDesign jasdi;
    JasperReport js;
    Connection con = ConnectioDB.getConnection(); // Replace ConnectioDB.getConnection() with your actual database connection method

    if (selectedRow != -1) { // Check if any row is selected
        String idvent = jTable1.getValueAt(selectedRow, 2).toString(); // Assuming ID is in the 3rd column (index 2)
        param.put("s", idvent);
        
        jasdi = JRXmlLoader.load("C:\\Users\\Administrator\\Desktop\\gestionCom - Copie\\src\\Devis2.jrxml");
    } else {
        jasdi = JRXmlLoader.load("C:\\Users\\Administrator\\Desktop\\gestionCom - Copie\\src\\Devis.jrxml");
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

    }//GEN-LAST:event_ImprimerDetailDevisActionPerformed

    private void prodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prodMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_prodMouseClicked

    private void prodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prodActionPerformed
       String selectedProduct = (String) prod.getSelectedItem();
        if (selectedProduct != null) {
            fetchPrix(selectedProduct);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_prodActionPerformed

    private void AjouterDevisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AjouterDevisActionPerformed
try {
    dbb db = dbb.getCon();
    DevisBean sb = new DevisBean();

    // Check if NumF is selected
    if (Client.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(null, "Veuillez sélectionner un numéro de Client.", "Champ requis", JOptionPane.WARNING_MESSAGE);
        return; // Exit method if NumF is not selected
    }

    // Get selected item from NumF combo box and cast it to int
    String selectedNumFrs = (String) Client.getSelectedItem();
    // Match the selected NumFrs name to get its corresponding Id
    int id = matchIdForCodeClient(selectedNumFrs);
    // Set the id to the AchatBeans object
    System.out.println(id);
    sb.setNumeroClient(id);

    // Check if datee is selected
    if (Datee.getDate() == null) {
        JOptionPane.showMessageDialog(null, "Veuillez sélectionner une date.", "Champ requis", JOptionPane.WARNING_MESSAGE);
        return; // Exit method if datee is not selected
    }

    // Get the selected date from Datee
    LocalDate selectedDate = Datee.getDate();
    // Format the date into string using DateTimeFormatter
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Modify the pattern as needed
    String dat = selectedDate.format(dateFormatter);
    // Set the formatted date to AchatBeans object
    sb.setDateDevis(dat);

    System.out.println(id);
   sb.ajouteDevis(sb);
    
    fetchNextIDAndLoadIntoTextBox(IDDEVIS);
    id();
} catch (SQLException ex) {
    Logger.getLogger(Devis.class.getName()).log(Level.SEVERE, null, ex);
}
        // TODO add your handling code here:
    }//GEN-LAST:event_AjouterDevisActionPerformed

    private void ModifierDevisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModifierDevisActionPerformed
  dbb db = dbb.getCon(); // Ensure this method returns a valid connection
    DevisBean sb = new DevisBean();
    
    // Check if IDDEVIS is empty
    if (IDDEVIS.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Veuillez entrer un numéro de commande.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }
    sb.setNumeroDevis(Integer.valueOf(IDDEVIS.getText()));
    
    // Check if Client is selected
    if (Client.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(null, "Veuillez sélectionner un fournisseur.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }
    String selectedNumFrs = (String) Client.getSelectedItem();
    int id = matchIdForCodeClient(selectedNumFrs);
    sb.setNumeroClient(id);
    
    // Check if Datee is selected
    if (Datee.getDate() == null) {
        JOptionPane.showMessageDialog(null, "Veuillez sélectionner une date de commande.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }
    LocalDate selectedDate = Datee.getDate();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String dat = selectedDate.format(dateFormatter);
    sb.setDateDevis(dat);

    sb.modifierDevis(sb);
    updateJTable();
    }//GEN-LAST:event_ModifierDevisActionPerformed

    private void AjouterDetailDevisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AjouterDetailDevisActionPerformed
try {
    DevisBean sb = new DevisBean();
    
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
    if (Prix1.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Veuillez entrer un prix.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method if the field is empty
    }

    // Check if qte is empty
    if (Qte.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Veuillez entrer une quantité.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method if the field is empty
    }

    // Check if prix is an integer
    try {
        Integer.parseInt(Prix1.getText());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Le prix doit être un nombre entier.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method if prix is not an integer
    }

    // Check if qte is an integer
    try {
        Integer.parseInt(Qte.getText());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "La quantité doit être un nombre entier.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method if qte is not an integer
    }

    int produitID = prodd(prod.getSelectedItem().toString());
    int id = Integer.parseInt((String) IdA.getSelectedItem());
    String prix1 = Prix1.getText();
    String qt = Qte.getText();

    sb.setNumeroDevis(id);
    sb.setNumeroProduit(produitID);
    sb.setPrix(prix1);
    sb.setQuantite(qt);

   // dbb db = dbb.getCon(); 
sb.ajouteDeatilCom(sb);

    updateJTable();
} catch (SQLException ex) {
    id();
    Logger.getLogger(Achat.class.getName()).log(Level.SEVERE, null, ex);
}


        // TODO add your handling code here:
    }//GEN-LAST:event_AjouterDetailDevisActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
  int selectedRow = jTable1.getSelectedRow();

// Check if a row is actually selected
if (selectedRow != -1) {
    // Assuming dtFactAch is a JTable
    // Set the selected item in IdA combo box
    Object idAValue = jTable1.getValueAt(selectedRow, 0);
    if (idAValue != null) {
        Client.setSelectedItem(idAValue.toString());
    }

Object prodValue = jTable1.getValueAt(selectedRow, 1);
Datee.setText((java.lang.String) prodValue);
    // Set the text of prix text field
    Object prixValue = jTable1.getValueAt(selectedRow, 2);
    if (prixValue != null) {
        IdA.setSelectedItem(prixValue);
      IDDEVIS.setText((prixValue).toString());
    }

    // Set the text of qte text field
    Object qteValue = jTable1.getValueAt(selectedRow, 3);
    if (qteValue != null) {
        prod.setSelectedItem(qteValue);
    }
Object price = jTable1.getValueAt(selectedRow, 4);
if (price != null) {
    Prix1.setText((price).toString()); // Use String.valueOf to convert the object to a string
}
Object qt = jTable1.getValueAt(selectedRow, 5);
if (qt != null) {
    Qte.setText((qt).toString()); // Use String.valueOf to convert the object to a string
}
    
    
    
} else {
    // No row selected, handle this case if needed
}

        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked

    private void ModifierDetailDevisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModifierDetailDevisActionPerformed
dbb b = dbb.getCon();
DevisBean sb = new DevisBean();
int produitID = prodd(prod.getSelectedItem().toString());

String qt = Qte.getText();
String prixText = Prix1.getText();

// Check if any of the required fields are empty
if (IdA.getSelectedItem() == null || qt.isEmpty() || prixText.isEmpty()) {
    JOptionPane.showMessageDialog(null, "Certains champs sont vides.", "Erreur", JOptionPane.ERROR_MESSAGE);
} else {
    int id = Integer.parseInt((String) IdA.getSelectedItem());

    sb.setNumeroDevis(id);
    sb.setNumeroProduit(produitID);
    sb.setPrix(prixText);
    sb.setQuantite(qt);
    sb.modifierDevisDetail(sb);
    updateJTable(); // TODO add your handling code here:
}        // TODO add your handling code here:
    }//GEN-LAST:event_ModifierDetailDevisActionPerformed

    private void SupprimerDetailDevisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SupprimerDetailDevisActionPerformed
 dbb d = dbb.getCon();
DevisBean sb = new DevisBean();

//sb.setId(IDFACTUR.getText());
   sb.setNumeroDevis(Integer.valueOf(IDDEVIS.getText()));

sb.SupDetailDevis(Integer.valueOf(IDDEVIS.getText()));
sb.SupDevis(Integer.valueOf(IDDEVIS.getText()));
   
    //
        // TODO add your handling code here:
                        
updateJTable();
  fetchNextIDAndLoadIntoTextBox(IDDEVIS);

        // TODO add your handling code here:
    }//GEN-LAST:event_SupprimerDetailDevisActionPerformed

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
updateJTable();        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1MouseClicked

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
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
    });     

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

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
            java.util.logging.Logger.getLogger(Devis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Devis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Devis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Devis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Devis().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AjouterDetailDevis;
    private javax.swing.JButton AjouterDevis;
    private javax.swing.JComboBox<String> Client;
    private com.github.lgooddatepicker.components.DatePicker Datee;
    private javax.swing.JTextField IDDEVIS;
    private javax.swing.JComboBox<String> IdA;
    private javax.swing.JButton ImprimerDetailDevis;
    private javax.swing.JButton ModifierDetailDevis;
    private javax.swing.JButton ModifierDevis;
    private javax.swing.JTextField Prix1;
    private javax.swing.JTextField Qte;
    private javax.swing.JButton SupprimerDetailDevis;
    private com.github.lgooddatepicker.components.DatePicker datePicker1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JComboBox<String> prod;
    // End of variables declaration//GEN-END:variables
}
