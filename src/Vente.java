
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
public class Vente extends javax.swing.JFrame {

    /**
     * Creates new form Client
     */
    public Vente() {
        initComponents();
//updateJTable();
        loadRaisonSocaleIntoCodeClient();
        // Other initialization code
        fetchProduit();
        id();
        IDVENT.setEditable(false);
    // Set the background color to grey
    Prix1.setEditable(false);
     Prix1.setBackground(Color.LIGHT_GRAY);
    IDVENT.setBackground(Color.LIGHT_GRAY);
        //Quantite*
        //updateJTable();
        id();
        fetchNextIDAndLoadIntoTextBox(IDVENT);
        updateJTable();
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
      public void fetchNextIDAndLoadIntoTextBox(JTextField id) {
    dbb db = dbb.getCon();  
    String sql = "SELECT MAX(id) AS max_id FROM Vent";

    try (
        PreparedStatement stmt = db.conx.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

        if (rs.next()) {
            int maxId = rs.getInt("max_id");
            int nextId = maxId + 1;
            IDVENT.setText(Integer.toString(nextId));
        } else {
            // Handle the case where no result is returned
            IDVENT.setText(""); // Clear the text box
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
            PreparedStatement pst = db.conx.prepareStatement(
                "SELECT dd.id, p.Désignation AS Produit, dd.Prix, dd.Quantité, c.RaisonSocial, d.DateVent " +
                "FROM FactueVent dd " +
                "INNER JOIN Produit p ON dd.NumeroProduit = p.NumeroProduit " +
                "INNER JOIN Vent d ON dd.id = d.id " +
                "INNER JOIN Client c ON d.NumClient = c.CodeClient;"
            );

            ResultSet resultSet = pst.executeQuery();

            DefaultTableModel df = (DefaultTableModel) jTable2.getModel();
            df.setRowCount(0);

            while (resultSet.next()) {
                Vector<Object> rowVector = new Vector<>();
                rowVector.add(resultSet.getString("RaisonSocial"));
                rowVector.add(resultSet.getString("DateVent"));
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


    public void id() {
        try {
            dbb db = dbb.getCon();
            if (db != null) {
                String query = "SELECT DISTINCT ID FROM Vent";
                PreparedStatement pst = db.conx.prepareStatement(query);
                ResultSet rs = pst.executeQuery();

                IdA.removeAllItems();

                while (rs.next()) {
                    String id = rs.getString("ID");
                    IdA.addItem(id);
                }
            } else {
                JOptionPane.showMessageDialog(null, "La connexion à la base de données n'est pas disponible.", "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des produits.", "Erreur SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fetchProduit() {
        try {
            dbb db = dbb.getCon();
            if (db.conx != null) {
                String query = "SELECT NumeroProduit FROM Produit";
                PreparedStatement pst = db.conx.prepareStatement(query);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    int productId = rs.getInt("NumeroProduit");
                    String productName = fetchProductNameForId(productId);
                    prod.addItem(productName);
                }
            } else {
                JOptionPane.showMessageDialog(null, "La connexion à la base de données n'est pas disponible.", "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des produits.", "Erreur SQL", JOptionPane.ERROR_MESSAGE);
        }
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

    private void fetchPrix(String selectedProductName) {
        System.out.println("Fetching price for product: " + selectedProductName);
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
                    System.out.println("Price fetched: " + price);
                } else {
                    System.out.println("No price found for product: " + selectedProductName);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private int matchIdForCodeClient(String numFrsName) {
        int id = 0;
        dbb db = dbb.getCon();
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

    private List<String> fetchCodeClient() {
        List<String> numFrsList = new ArrayList<>();
        dbb db = dbb.getCon();
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

    private String matchRaisonSocale(String numFrs) {
        String raisonSocale = null;
        dbb db = dbb.getCon();
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
        List<String> numFrsList = fetchCodeClient();

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
        Label3 = new javax.swing.JLabel();
        Label4 = new javax.swing.JLabel();
        ajouteFactureVente = new javax.swing.JButton();
        modifierFactureVente = new javax.swing.JButton();
        jTextField5 = new javax.swing.JTextField();
        rechercherVente = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        label1 = new java.awt.Label();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        label6 = new java.awt.Label();
        label7 = new java.awt.Label();
        label8 = new java.awt.Label();
        label9 = new java.awt.Label();
        prod = new javax.swing.JComboBox<>();
        Prix1 = new javax.swing.JTextField();
        Qte = new javax.swing.JTextField();
        button11 = new java.awt.Button();
        ModifierFacture = new java.awt.Button();
        SupprimmerFacture = new java.awt.Button();
        imprimerFacture = new java.awt.Button();
        IdA = new javax.swing.JComboBox<>();
        Datee = new com.github.lgooddatepicker.components.DatePicker();
        IDVENT = new javax.swing.JTextField();
        Client = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        datePicker1 = new com.github.lgooddatepicker.components.DatePicker();
        label10 = new java.awt.Label();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Facture Vente");
        jLabel1.setBackground(new java.awt.Color(204, 255, 204));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(216, 21, 325, 43));

        Label3.setText("Numero Client");
        Label3.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        Label3.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(Label3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, -1, -1));

        Label4.setText("Date Vente");
        Label4.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        Label4.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(Label4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, -1, -1));

        ajouteFactureVente.setText("Ajouter");
        ajouteFactureVente.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        ajouteFactureVente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajouteFactureVenteActionPerformed(evt);
            }
        });
        jPanel1.add(ajouteFactureVente, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 330, 116, -1));

        modifierFactureVente.setText("Modifer");
        modifierFactureVente.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        modifierFactureVente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifierFactureVenteActionPerformed(evt);
            }
        });
        jPanel1.add(modifierFactureVente, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 330, 124, -1));

        jTextField5.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jPanel1.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 330, 135, -1));

        rechercherVente.setText("Rechercher");
        rechercherVente.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        rechercherVente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rechercherVenteActionPerformed(evt);
            }
        });
        jPanel1.add(rechercherVente, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 330, -1, -1));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 40, 10, 540));

        label1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        label1.setText("Detail Facture Vente");
        jPanel1.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 10, -1, -1));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Client", "Date", "N achat", "Produit", "Prix", "Qte"
            }
        ));
        jTable2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 130, 400, 170));

        label6.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        label6.setText("ID");
        jPanel1.add(label6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, -1, -1));

        label7.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        label7.setText("Numero Produit");
        jPanel1.add(label7, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 180, -1, -1));

        label8.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        label8.setText("Quantité");
        jPanel1.add(label8, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 280, -1, -1));

        label9.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        label9.setText("Prix");
        jPanel1.add(label9, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 220, -1, -1));

        prod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prodActionPerformed(evt);
            }
        });
        jPanel1.add(prod, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 180, 124, -1));

        Prix1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Prix1ActionPerformed(evt);
            }
        });
        jPanel1.add(Prix1, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 230, 123, -1));
        jPanel1.add(Qte, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 280, 123, -1));

        button11.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        button11.setLabel("Ajouter");
        button11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button11ActionPerformed(evt);
            }
        });
        jPanel1.add(button11, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 340, 105, 30));

        ModifierFacture.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        ModifierFacture.setLabel("Modifier");
        ModifierFacture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifierFactureActionPerformed(evt);
            }
        });
        jPanel1.add(ModifierFacture, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 340, 95, -1));

        SupprimmerFacture.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        SupprimmerFacture.setLabel("Supprimer");
        SupprimmerFacture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SupprimmerFactureActionPerformed(evt);
            }
        });
        jPanel1.add(SupprimmerFacture, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 340, -1, -1));

        imprimerFacture.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        imprimerFacture.setLabel("Imprimer");
        imprimerFacture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imprimerFactureActionPerformed(evt);
            }
        });
        jPanel1.add(imprimerFacture, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 340, -1, -1));

        IdA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IdAActionPerformed(evt);
            }
        });
        jPanel1.add(IdA, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 130, 124, -1));
        jPanel1.add(Datee, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 240, -1, -1));
        jPanel1.add(IDVENT, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, 140, -1));

        jPanel1.add(Client, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 160, 146, -1));

        jButton1.setText("Rechercher Par Date");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 400, -1, -1));
        jPanel1.add(datePicker1, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 400, -1, -1));

        label10.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        label10.setText("ID");
        jPanel1.add(label10, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 130, -1, -1));

        jButton5.setText("Retour");
        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 0, 0));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 470, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1413, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void IdAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IdAActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IdAActionPerformed

    private void ModifierFactureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModifierFactureActionPerformed
dbb b = dbb.getCon();
VenteBean159 sb = new VenteBean159();
int produitID = prodd(prod.getSelectedItem().toString());

String qt = Qte.getText();
String prixText = Prix1.getText();

// Check if any of the required fields are empty
if (IdA.getSelectedItem() == null || qt.isEmpty() || prixText.isEmpty()) {
    JOptionPane.showMessageDialog(null, "Certains champs sont vides.", "Erreur", JOptionPane.ERROR_MESSAGE);
} else {
    int id = Integer.parseInt((String) IdA.getSelectedItem());

    sb.setPrix(prixText);
    sb.setQuantite(qt);
    
    sb.setNumeroVent(id);
    sb.setNumeroProduit(produitID);
    sb.modifierVentDetail(sb);
    updateJTable(); // TODO add your handling code here:
}        // TODO add your handling code here:        // TODO add your handling code here:
    }//GEN-LAST:event_ModifierFactureActionPerformed

    private void button11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button11ActionPerformed
try {
    VenteBean159 sb = new VenteBean159();
    
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

    sb.setNumeroVent(id);
    sb.setNumeroProduit(produitID);
    sb.setPrix(prix1);
    sb.setQuantite(qt);

   // dbb db = dbb.getCon(); 
sb.ajouteDeatilCom(sb);

    updateJTable();
} catch (SQLException ex) {
    id();
            fetchNextIDAndLoadIntoTextBox(IDVENT);

    Logger.getLogger(Achat.class.getName()).log(Level.SEVERE, null, ex);
}

    }//GEN-LAST:event_button11ActionPerformed

    private void Prix1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Prix1ActionPerformed
        String selectedProduct = (String) prod.getSelectedItem();
        if (selectedProduct != null) {
            fetchPrix(selectedProduct);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_Prix1ActionPerformed

    private void prodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prodActionPerformed
        String selectedProduct = (String) prod.getSelectedItem();
        if (selectedProduct != null) {
            fetchPrix(selectedProduct);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_prodActionPerformed

    private void rechercherVenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rechercherVenteActionPerformed
    DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    jTable2.setRowSorter(sorter);

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
    });        // TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_rechercherVenteActionPerformed

    private void modifierFactureVenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifierFactureVenteActionPerformed
 dbb db = dbb.getCon(); // Ensure this method returns a valid connection
    VenteBean159 sb = new VenteBean159();
    
    // Check if IDDEVIS is empty
    if (IDVENT.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Veuillez entrer un numéro de commande.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }
    sb.setNumeroVent(Integer.valueOf(IDVENT.getText()));
    
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
    sb.setDateVent(dat);

    sb.modifierDevis(sb);
    updateJTable();

        // TODO add your handling code here:
    }//GEN-LAST:event_modifierFactureVenteActionPerformed

    private void ajouteFactureVenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajouteFactureVenteActionPerformed
       try {
    dbb db = dbb.getCon();
    VenteBean159 sb = new VenteBean159();

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
    sb.setDateVent(dat);

    System.out.println(id);
   sb.ajouteVent(sb);
    
  //  fetchNextIDAndLoadIntoTextBox(IDFACTUR);
    id();
            fetchNextIDAndLoadIntoTextBox(IDVENT);

} catch (SQLException ex) {
    Logger.getLogger(Vente.class.getName()).log(Level.SEVERE, null, ex);
}
    }//GEN-LAST:event_ajouteFactureVenteActionPerformed

    private void SupprimmerFactureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SupprimmerFactureActionPerformed
VenteBean159 sb = new VenteBean159();
sb.SupDetailVent(Integer.valueOf(IDVENT.getText()));
sb.SupVent(Integer.valueOf(IDVENT.getText()));
updateJTable();
fetchNextIDAndLoadIntoTextBox(IDVENT);

    }//GEN-LAST:event_SupprimmerFactureActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Assuming datePicker1 is an instance of a date picker that returns LocalDate
        LocalDate localDate = datePicker1.getDate();
        Date selectedDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust the format as needed
        String formattedSelectedDate = dateFormat.format(selectedDate);

        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        jTable2.setRowSorter(sorter);

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

    private void imprimerFactureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imprimerFactureActionPerformed

 //       JasperDesign jasdi = JRXmlLoader.load("C:\\Users\\Administrator\\Desktop\\gestionCom - Copie\\src\\Vent.jrxml");

    
HashMap<String, Object> param = new HashMap<>();
int selectedRow = jTable2.getSelectedRow();

try {
    // Load Command.jrxml
    JasperDesign jasdi;
    JasperReport js;
    Connection con = ConnectioDB.getConnection(); // Replace ConnectioDB.getConnection() with your actual database connection method

    if (selectedRow != -1) { // Check if any row is selected
    int idvent = Integer.parseInt(jTable2.getValueAt(selectedRow, 2).toString());

        param.put("s", idvent);
        
        jasdi = JRXmlLoader.load("C:\\\\Users\\\\Administrator\\\\Desktop\\\\gestionCom - Copie\\\\src\\\\Ventort1.jrxml");
    } else {
        jasdi = JRXmlLoader.load("C:\\\\Users\\\\Administrator\\\\Desktop\\\\gestionCom - Copie\\\\src\\\\Vent.jrxml");
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
    }//GEN-LAST:event_imprimerFactureActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
  int selectedRow = jTable2.getSelectedRow();

// Check if a row is actually selected
if (selectedRow != -1) {
    // Assuming dtFactAch is a JTable
    // Set the selected item in IdA combo box
    Object idAValue = jTable2.getValueAt(selectedRow, 0);
    if (idAValue != null) {
        Client.setSelectedItem(idAValue.toString());
    }

Object prodValue = jTable2.getValueAt(selectedRow, 1);
Datee.setText((java.lang.String) prodValue);
    // Set the text of prix text field
    Object prixValue = jTable2.getValueAt(selectedRow, 2);
    if (prixValue != null) {
        IdA.setSelectedItem(prixValue);
      IDVENT.setText((prixValue).toString());
    }

    // Set the text of qte text field
    Object qteValue = jTable2.getValueAt(selectedRow, 3);
    if (qteValue != null) {
        prod.setSelectedItem(qteValue);
    }
Object price = jTable2.getValueAt(selectedRow, 4);
if (price != null) {
    Prix1.setText((price).toString()); // Use String.valueOf to convert the object to a string
}
Object qt = jTable2.getValueAt(selectedRow, 5);
if (qt != null) {
    Qte.setText((qt).toString()); // Use String.valueOf to convert the object to a string
}
    
    
    
} else {
    // No row selected, handle this case if needed
}
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable2MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
jTable2.clearSelection();
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
            java.util.logging.Logger.getLogger(Vente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Vente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Vente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Vente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Vente().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Client;
    private com.github.lgooddatepicker.components.DatePicker Datee;
    private javax.swing.JTextField IDVENT;
    private javax.swing.JComboBox<String> IdA;
    private javax.swing.JLabel Label3;
    private javax.swing.JLabel Label4;
    private java.awt.Button ModifierFacture;
    private javax.swing.JTextField Prix1;
    private javax.swing.JTextField Qte;
    private java.awt.Button SupprimmerFacture;
    private javax.swing.JButton ajouteFactureVente;
    private java.awt.Button button11;
    private com.github.lgooddatepicker.components.DatePicker datePicker1;
    private java.awt.Button imprimerFacture;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField5;
    private java.awt.Label label1;
    private java.awt.Label label10;
    private java.awt.Label label6;
    private java.awt.Label label7;
    private java.awt.Label label8;
    private java.awt.Label label9;
    private javax.swing.JButton modifierFactureVente;
    private javax.swing.JComboBox<String> prod;
    private javax.swing.JButton rechercherVente;
    // End of variables declaration//GEN-END:variables
}
