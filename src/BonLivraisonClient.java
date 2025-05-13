
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
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author PC
 */
public class BonLivraisonClient extends javax.swing.JFrame {

    /**
     * Creates new form BonLivraisonClient
     */
    public BonLivraisonClient() {
        initComponents();

  //      updateJTable();
        loadRaisonSocaleIntoCodeClient();
        // Other initialization code
        fetchProduit();
        id();
        Prix1.setEditable(false);
     Prix1.setBackground(Color.LIGHT_GRAY);
            IDBON.setEditable(false);
    // Set the background color to grey
    IDBON.setBackground(Color.LIGHT_GRAY);
        fetchNextIDAndLoadIntoTextBox(IDBON);
        updateJTable();
    }
   private void updateJTable() {
    System.out.println("Updating JTable..."); // Add this line for debugging
    try {
        dbb db = dbb.getCon(); // Get the database connection
        if (db.conx != null && !db.conx.isClosed()) { // Check if connection is not null and not closed
            PreparedStatement pst = db.conx.prepareStatement(
                "SELECT dd.id, p.Désignation AS Produit, dd.Prix, dd.Quantité, c.RaisonSocial, d.DateBon " +
                "FROM DetailBonLC  dd " +
                "INNER JOIN Produit p ON dd.NumeroProduit = p.NumeroProduit " +
                "INNER JOIN BonLC d ON dd.id = d.id " +
                "INNER JOIN Client c ON d.NumClient = c.CodeClient;"
            );

            ResultSet resultSet = pst.executeQuery();

            DefaultTableModel df = (DefaultTableModel) jTable1.getModel();
            df.setRowCount(0);

            while (resultSet.next()) {
                Vector<Object> rowVector = new Vector<>();
                rowVector.add(resultSet.getString("RaisonSocial"));
                rowVector.add(resultSet.getString("DateBon"));
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
            String query = "SELECT DISTINCT ID FROM BonLC"; // Use DISTINCT to retrieve unique IDs directly from the database
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
    String sql = "SELECT MAX(id) AS max_id FROM BonLC";

    try (
        PreparedStatement stmt = db.conx.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

        if (rs.next()) {
            int maxId = rs.getInt("max_id");
            int nextId = maxId + 1;
            IDBON.setText(Integer.toString(nextId));
        } else {
            // Handle the case where no result is returned
            IDBON.setText(""); // Clear the text box
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        AjouterBonLivClient = new javax.swing.JButton();
        ModifierBonLivClient = new javax.swing.JButton();
        jTextField5 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        Qte = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        prod = new javax.swing.JComboBox<>();
        jButton9 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        AjouterDetailBonLivClient = new javax.swing.JButton();
        ModifierDetailBonLivClient = new javax.swing.JButton();
        SupprimerDetailBonLivClient = new javax.swing.JButton();
        ImprimerDetailBonLivClient = new javax.swing.JButton();
        Client = new javax.swing.JComboBox<>();
        Datee = new com.github.lgooddatepicker.components.DatePicker();
        Prix1 = new javax.swing.JTextField();
        IdA = new javax.swing.JComboBox<>();
        IDBON = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        datePicker1 = new com.github.lgooddatepicker.components.DatePicker();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1482, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Bon Livraison Client");
        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, -1, 30));

        jLabel2.setText("Prix Unitaire");
        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 180, -1, 10));

        jLabel3.setText("Id");
        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        jLabel4.setText("Nom Client");
        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        AjouterBonLivClient.setText("Ajouter");
        AjouterBonLivClient.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        AjouterBonLivClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AjouterBonLivClientActionPerformed(evt);
            }
        });
        jPanel2.add(AjouterBonLivClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 260, 119, -1));

        ModifierBonLivClient.setText("Modifier");
        ModifierBonLivClient.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        ModifierBonLivClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifierBonLivClientActionPerformed(evt);
            }
        });
        jPanel2.add(ModifierBonLivClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 260, 117, -1));

        jTextField5.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jPanel2.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 600, 145, -1));

        jLabel6.setText("Id");
        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 80, -1, -1));

        jLabel7.setText("Produit");
        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 130, -1, -1));

        Qte.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jPanel2.add(Qte, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 220, 170, 30));

        jLabel8.setText("Qte");
        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 230, -1, -1));

        prod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        prod.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
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
        jPanel2.add(prod, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 130, 170, 30));

        jButton9.setText("Rechercher");
        jButton9.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 600, -1, -1));

        jLabel5.setText("Detail Bon Livraison Client");
        jLabel5.setBackground(new java.awt.Color(0, 0, 0));
        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 20, -1, 30));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel2.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 40, -1, 560));

        AjouterDetailBonLivClient.setText("Ajouter");
        AjouterDetailBonLivClient.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        AjouterDetailBonLivClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AjouterDetailBonLivClientActionPerformed(evt);
            }
        });
        jPanel2.add(AjouterDetailBonLivClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 380, 119, -1));

        ModifierDetailBonLivClient.setText("Modifier");
        ModifierDetailBonLivClient.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        ModifierDetailBonLivClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifierDetailBonLivClientActionPerformed(evt);
            }
        });
        jPanel2.add(ModifierDetailBonLivClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 380, 117, -1));

        SupprimerDetailBonLivClient.setText("Supprimer");
        SupprimerDetailBonLivClient.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        SupprimerDetailBonLivClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SupprimerDetailBonLivClientActionPerformed(evt);
            }
        });
        jPanel2.add(SupprimerDetailBonLivClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 380, -1, -1));

        ImprimerDetailBonLivClient.setText("Imprimer");
        ImprimerDetailBonLivClient.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        ImprimerDetailBonLivClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImprimerDetailBonLivClientActionPerformed(evt);
            }
        });
        jPanel2.add(ImprimerDetailBonLivClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 380, 116, -1));

        Client.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClientActionPerformed(evt);
            }
        });
        jPanel2.add(Client, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 130, 200, 30));
        jPanel2.add(Datee, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 190, 200, 30));

        Prix1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel2.add(Prix1, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 170, 170, 30));

        IdA.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jPanel2.add(IdA, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 80, 170, 30));
        jPanel2.add(IDBON, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, 200, 30));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Date ");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Client", "Date", "Id", "Produit", "Prix", "Quantité"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 80, -1, 210));

        jButton1.setText("Rechercher Par Date");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 530, -1, -1));
        jPanel2.add(datePicker1, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 530, -1, -1));

        jButton5.setText("Retour");
        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 0, 0));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 320, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 704, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void prodItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_prodItemStateChanged

        // TODO add your handling code here:
    }//GEN-LAST:event_prodItemStateChanged

    private void ImprimerDetailBonLivClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImprimerDetailBonLivClientActionPerformed
   
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
        
        jasdi = JRXmlLoader.load("C:\\Users\\Administrator\\Desktop\\gestionCom - Copie\\src\\Livr.jrxml");
    } else {
        jasdi = JRXmlLoader.load("C:\\Users\\Administrator\\Desktop\\gestionCom - Copie\\src\\livlist.jrxml");
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
    }//GEN-LAST:event_ImprimerDetailBonLivClientActionPerformed

    private void ClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClientActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ClientActionPerformed

    private void prodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prodMouseClicked

        // TODO add your handling code here:
    }//GEN-LAST:event_prodMouseClicked

    private void prodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prodActionPerformed

String selectedProduct = (String) prod.getSelectedItem();
        if (selectedProduct != null) {
            fetchPrix(selectedProduct);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_prodActionPerformed

    private void AjouterBonLivClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AjouterBonLivClientActionPerformed
try {
    dbb db = dbb.getCon();
    BonLivCBeans sb = new BonLivCBeans();

    // Check if NumF is selected
    if (Client.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(null, "Veuillez sélectionner un numéro de Bon.", "Champ requis", JOptionPane.WARNING_MESSAGE);
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
   sb.ajouteBonLC(sb);
    
    fetchNextIDAndLoadIntoTextBox(IDBON);
    id();
} catch (SQLException ex) {
    Logger.getLogger(Devis.class.getName()).log(Level.SEVERE, null, ex);
}

        // TODO add your handling code here:
    }//GEN-LAST:event_AjouterBonLivClientActionPerformed

    private void ModifierBonLivClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModifierBonLivClientActionPerformed

         dbb db = dbb.getCon(); // Ensure this method returns a valid connection
    BonLivCBeans sb = new BonLivCBeans();
    
    // Check if IDDEVIS is empty
    if (IDBON.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Veuillez entrer un numéro de commande.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }
    sb.setId(Integer.valueOf(IDBON.getText()));
    
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

    sb.modifierBonLiv(sb);
//    updateJTable();
    }//GEN-LAST:event_ModifierBonLivClientActionPerformed

    private void AjouterDetailBonLivClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AjouterDetailBonLivClientActionPerformed
try {
    BonLivCBeans sb = new BonLivCBeans();
    
    // Check if IdA is selected
    if (IdA.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(null, "Veuillez sélectionner un numéro de Bon.", "Erreur", JOptionPane.ERROR_MESSAGE);
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

    sb.setId(id);
    sb.setNumeroProduit(produitID);
    sb.setPrix(prix1);
    sb.setQuantite(qt);

   // dbb db = dbb.getCon(); 
sb.ajouteDetailBonLC(sb);

  updateJTable();
} catch (SQLException ex) {
    id();
    Logger.getLogger(Achat.class.getName()).log(Level.SEVERE, null, ex);
}


        // TODO add your handling code here:
    }//GEN-LAST:event_AjouterDetailBonLivClientActionPerformed

    private void ModifierDetailBonLivClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModifierDetailBonLivClientActionPerformed
dbb b = dbb.getCon();
BonLivCBeans sb = new BonLivCBeans();
int produitID = prodd(prod.getSelectedItem().toString());

String qt = Qte.getText();
String prixText = Prix1.getText();

// Check if any of the required fields are empty
if (IdA.getSelectedItem() == null || qt.isEmpty() || prixText.isEmpty()) {
    JOptionPane.showMessageDialog(null, "Certains champs sont vides.", "Erreur", JOptionPane.ERROR_MESSAGE);
} else {
    int id = Integer.parseInt((String) IdA.getSelectedItem());

    sb.setId(id);
    sb.setNumeroProduit(produitID);
    sb.setPrix(prixText);
    sb.setQuantite(qt);
    sb.modifierDetailBon(sb);
   updateJTable(); // TODO add your handling code here:
}   

        // TODO add your handling code here:
    }//GEN-LAST:event_ModifierDetailBonLivClientActionPerformed

    private void SupprimerDetailBonLivClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SupprimerDetailBonLivClientActionPerformed
dbb d = dbb.getCon();
BonLivCBeans sb = new BonLivCBeans();

//sb.setId(IDFACTUR.getText());
   sb.setId(Integer.valueOf(IDBON.getText()));

sb.SupBonl(Integer.valueOf(IDBON.getText()));
sb.SupDetailBon(Integer.valueOf(IDBON.getText()));
   
    //
        // TODO add your handling code here:
                        
//updateJTable();
  fetchNextIDAndLoadIntoTextBox(IDBON);
        // TODO add your handling code here:
    }//GEN-LAST:event_SupprimerDetailBonLivClientActionPerformed

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
      IDBON.setText((prixValue).toString());
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
//            id();
        }
    }); 
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

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
            java.util.logging.Logger.getLogger(BonLivraisonClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BonLivraisonClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BonLivraisonClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BonLivraisonClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BonLivraisonClient().setVisible(true);
            }
        });
    }

     // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AjouterBonLivClient;
    private javax.swing.JButton AjouterDetailBonLivClient;
    private javax.swing.JComboBox<String> Client;
    private javax.swing.JTextField IDBON;
    private javax.swing.JComboBox<String> IdA;
    private javax.swing.JButton ImprimerDetailBonLivClient;
    private javax.swing.JButton ModifierBonLivClient;
    private javax.swing.JButton ModifierDetailBonLivClient;
    private javax.swing.JTextField Prix1;
    private javax.swing.JTextField Qte;
    private javax.swing.JButton SupprimerDetailBonLivClient;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JComboBox<String> prod;
    // End of variables declaration   // Variables declaration - do not modify                     
    private javax.swing.JButton AjouterBonLivClient;
    private javax.swing.JButton AjouterDetailBonLivClient;
    private javax.swing.JComboBox<String> Client;
    private com.github.lgooddatepicker.components.DatePicker Datee;
    private javax.swing.JTextField IDBON;
    private javax.swing.JComboBox<String> IdA;
    private javax.swing.JButton ImprimerDetailBonLivClient;
    private javax.swing.JButton ModifierBonLivClient;
    private javax.swing.JButton ModifierDetailBonLivClient;
    private javax.swing.JTextField Prix1;
    private javax.swing.JTextField Qte;
    private javax.swing.JButton SupprimerDetailBonLivClient;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JComboBox<String> prod;
    // End of variables declaration//GEN-END:variables
}
