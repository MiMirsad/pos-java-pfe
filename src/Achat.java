
import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.String;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
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
public class Achat extends javax.swing.JFrame {

    private ResultSet rs; // ResultSet to hold the records
    private List<AchatBeans> records = new ArrayList<>(); // List to hold the records
    private int currentRow = 0;

    /**
     * Creates new form Achat
     */
    public Achat() throws SQLException {
        initComponents();
        //Four();
        fetchProduit();
//    afficherFournisseur();
   //     id();
   IDFACTUR.setEditable(false);
    // Set the background color to grey
    IDFACTUR.setBackground(Color.LIGHT_GRAY);
    fetchNextIDAndLoadIntoTextBox(IDFACTUR);
        updateJTable();
        loadRaisonSocaleIntoNumF();
            prix1.setEditable(false);
     prix1.setBackground(Color.LIGHT_GRAY);
      id();

        // Assuming idA is the component you want to add the listener to
     
    }
 
public void id() {
    try {
        // Assuming dbb.getCon().conx returns a valid database connection
        dbb db = dbb.getCon();
        if (db != null) {
            String query = "SELECT DISTINCT ID FROM FactureAchat"; // Use DISTINCT to retrieve unique IDs directly from the database
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

    private void updateJTable() {
        System.out.println("Updating JTable..."); // Add this line for debugging
        try {
            dbb db = dbb.getCon(); // Get the database connection
            if (db.conx != null && !db.conx.isClosed()) { // Check if connection is not null and not closed
                
                    PreparedStatement pst = db.conx.prepareStatement("SELECT da.ID, p.Désignation AS Produit, da.PrixAchat, da.QteAchat, f.RaisonSocale, fa.DateFacture\n" +
"FROM DetailAchat da\n" +
"INNER JOIN Produit p ON da.NumPro = p.NumeroProduit\n" +
"INNER JOIN FactureAchat fa ON da.ID = fa.ID\n" +
"INNER JOIN Fournisseur f ON fa.Nfour = f.NumFrs;");

                         //  pst.setInt(1,Integer.parseInt(idf.getText()));

                        ResultSet resultSet = pst.executeQuery();

                    DefaultTableModel df = (DefaultTableModel) dtFactAch.getModel();
                    df.setRowCount(0);

                    while (resultSet.next()) {
                        Vector<Object> rowVector = new Vector<>();
                           rowVector.add(resultSet.getString("RaisonSocale"));
                              rowVector.add(resultSet.getString("DateFacture"));
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
private void displayRecord() {
    if (!records.isEmpty() && currentRow >= 0 && currentRow < records.size()) {
        AchatBeans pb = records.get(currentRow);
        NumF.setSelectedItem(pb.getNumF());
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateeStr = formatter.format(datee);
    IDFACTUR.setText(Integer.toString(pb.getId()));
        datee.setText(dateeStr);
        IdA.setSelectedItem(pb.getId());
        prod.setSelectedIndex(pb.getProduit());
        prix1.setText(pb.getPrix());
        qte.setText(pb.getQte());
    } else {
        JOptionPane.showMessageDialog(null, "Aucun enregistrement trouvé.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}

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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator2 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        label1 = new java.awt.Label();
        label3 = new java.awt.Label();
        label4 = new java.awt.Label();
        AjouterFactureAchat = new java.awt.Button();
        ModifierFactureAchat = new java.awt.Button();
        textField5 = new java.awt.TextField();
        button9 = new java.awt.Button();
        jScrollPane1 = new javax.swing.JScrollPane();
        dtFactAch = new javax.swing.JTable();
        label6 = new java.awt.Label();
        label7 = new java.awt.Label();
        label8 = new java.awt.Label();
        IdA = new javax.swing.JComboBox<>();
        label9 = new java.awt.Label();
        prod = new javax.swing.JComboBox<>();
        IDFACTUR = new javax.swing.JTextField();
        qte = new javax.swing.JTextField();
        NumF = new javax.swing.JComboBox<>();
        AjouterDetailFactureAchat = new java.awt.Button();
        ModifierDetailFactureAchat = new java.awt.Button();
        SupprimerDetailFactureAchat = new java.awt.Button();
        jSeparator1 = new javax.swing.JSeparator();
        label2 = new java.awt.Label();
        datee = new com.github.lgooddatepicker.components.DatePicker();
        label10 = new java.awt.Label();
        prix1 = new javax.swing.JTextField();
        datePicker1 = new com.github.lgooddatepicker.components.DatePicker();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));
        jPanel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        label1.setText("Detail Facture Achat");
        jPanel1.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 10, 330, -1));

        label3.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        label3.setText("Nom Fournisseur");
        jPanel1.add(label3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 150, -1, -1));

        label4.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        label4.setText("Date D'achat");
        jPanel1.add(label4, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 230, -1, -1));

        AjouterFactureAchat.setBackground(new java.awt.Color(255, 255, 255));
        AjouterFactureAchat.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        AjouterFactureAchat.setLabel("Ajouter");
        AjouterFactureAchat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AjouterFactureAchatActionPerformed(evt);
            }
        });
        jPanel1.add(AjouterFactureAchat, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 330, 105, 30));

        ModifierFactureAchat.setBackground(new java.awt.Color(255, 255, 255));
        ModifierFactureAchat.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        ModifierFactureAchat.setLabel("Modifier");
        ModifierFactureAchat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifierFactureAchatActionPerformed(evt);
            }
        });
        jPanel1.add(ModifierFactureAchat, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 330, 95, -1));

        textField5.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        textField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textField5ActionPerformed(evt);
            }
        });
        jPanel1.add(textField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 540, 139, -1));

        button9.setBackground(new java.awt.Color(255, 255, 255));
        button9.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        button9.setLabel("Rechercher");
        button9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button9ActionPerformed(evt);
            }
        });
        jPanel1.add(button9, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 540, -1, -1));

        dtFactAch.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Nom Fournisseur", "Date D'achat", "Id detail", "Produit", "Prix", "Quantité"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dtFactAch.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        dtFactAch.setSelectionBackground(new java.awt.Color(0, 0, 0));
        dtFactAch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dtFactAchMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(dtFactAch);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 130, 438, 140));

        label6.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        label6.setText("Id");
        jPanel1.add(label6, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 90, -1, -1));

        label7.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        label7.setText("Produit\n");
        jPanel1.add(label7, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 140, -1, -1));

        label8.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        label8.setText("Quantité");
        jPanel1.add(label8, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 230, -1, -1));

        IdA.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                IdAMouseClicked(evt);
            }
        });
        IdA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IdAActionPerformed(evt);
            }
        });
        jPanel1.add(IdA, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 90, 124, -1));

        label9.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        label9.setText("Prix");
        jPanel1.add(label9, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 180, -1, -1));

        prod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prodActionPerformed(evt);
            }
        });
        jPanel1.add(prod, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 140, 124, -1));

        IDFACTUR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDFACTURActionPerformed(evt);
            }
        });
        jPanel1.add(IDFACTUR, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 90, 123, -1));
        jPanel1.add(qte, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 230, 123, -1));

        jPanel1.add(NumF, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 150, 144, -1));

        AjouterDetailFactureAchat.setBackground(new java.awt.Color(255, 255, 255));
        AjouterDetailFactureAchat.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        AjouterDetailFactureAchat.setLabel("Ajouter");
        AjouterDetailFactureAchat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AjouterDetailFactureAchatActionPerformed(evt);
            }
        });
        jPanel1.add(AjouterDetailFactureAchat, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 330, 105, 30));

        ModifierDetailFactureAchat.setBackground(new java.awt.Color(255, 255, 255));
        ModifierDetailFactureAchat.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        ModifierDetailFactureAchat.setLabel("Modifier");
        ModifierDetailFactureAchat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifierDetailFactureAchatActionPerformed(evt);
            }
        });
        jPanel1.add(ModifierDetailFactureAchat, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 330, 95, -1));

        SupprimerDetailFactureAchat.setBackground(new java.awt.Color(255, 255, 255));
        SupprimerDetailFactureAchat.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        SupprimerDetailFactureAchat.setLabel("Supprimer");
        SupprimerDetailFactureAchat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SupprimerDetailFactureAchatActionPerformed(evt);
            }
        });
        jPanel1.add(SupprimerDetailFactureAchat, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 330, -1, -1));

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 40, 10, 560));

        label2.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        label2.setText("Facture Achat");
        jPanel1.add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 0, 310, -1));
        jPanel1.add(datee, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 230, -1, -1));

        label10.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        label10.setText("Id");
        jPanel1.add(label10, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 90, -1, -1));

        prix1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prix1ActionPerformed(evt);
            }
        });
        jPanel1.add(prix1, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 180, 123, -1));
        jPanel1.add(datePicker1, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 540, -1, -1));

        jButton1.setText("Rechercher Par Date");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 540, -1, -1));

        jButton5.setText("Retour");
        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 0, 0));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 390, 143, -1));

        jButton2.setText("Imprimer");
        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 0, 0));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 330, 140, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1397, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 686, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void textField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textField5ActionPerformed

    private void IDFACTURActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDFACTURActionPerformed
        String selectedProduct = (String) prod.getSelectedItem();
        if (selectedProduct != null) {
            fetchPrix(selectedProduct);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_IDFACTURActionPerformed

    private void prodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prodActionPerformed
        String selectedProduct = (String) prod.getSelectedItem();
        if (selectedProduct != null) {
            fetchPrix(selectedProduct);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_prodActionPerformed

    private void AjouterFactureAchatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AjouterFactureAchatActionPerformed
try {
    dbb db = dbb.getCon();
    AchatBeans sb = new AchatBeans();

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

    System.out.println(id);
    db.ajouteAchat(sb);
    
    fetchNextIDAndLoadIntoTextBox(IDFACTUR);
    id();
} catch (SQLException ex) {
    Logger.getLogger(Achat.class.getName()).log(Level.SEVERE, null, ex);
}



    }//GEN-LAST:event_AjouterFactureAchatActionPerformed

    private void AjouterDetailFactureAchatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AjouterDetailFactureAchatActionPerformed
  try {
    int produitID = prodd(prod.getSelectedItem().toString());
    int id = Integer.parseInt((String) IdA.getSelectedItem());
    String qt = qte.getText();

    // Create AchatBeans object
    AchatBeans sb = new AchatBeans(); // Create AchatBeans object here
    sb.setId(id);
    sb.setProduit(produitID);
    sb.setPrix(prix1.getText());
    sb.setQte(qt);

    // Perform database operation
    dbb db = dbb.getCon();
    db.ajouteAchatfac(sb);

    // Show confirmation message in French
//    JOptionPane.showMessageDialog(this, "Achat ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);

} catch (SQLException ex) {
    Logger.getLogger(Achat.class.getName()).log(Level.SEVERE, null, ex);
}
updateJTable();

         // id();
    }//GEN-LAST:event_AjouterDetailFactureAchatActionPerformed

    private void dtFactAchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dtFactAchMouseClicked
      int selectedRow = dtFactAch.getSelectedRow();

// Check if a row is actually selected
if (selectedRow != -1) {
    // Assuming dtFactAch is a JTable
    // Set the selected item in IdA combo box
    Object idAValue = dtFactAch.getValueAt(selectedRow, 0);
    if (idAValue != null) {
        NumF.setSelectedItem(idAValue.toString());
    }

Object prodValue = dtFactAch.getValueAt(selectedRow, 1);
datee.setText((java.lang.String) prodValue);
    // Set the text of prix text field
    Object prixValue = dtFactAch.getValueAt(selectedRow, 2);
    if (prixValue != null) {
        IdA.setSelectedItem(prixValue);
      IDFACTUR.setText((prixValue).toString());
    }

    // Set the text of qte text field
    Object qteValue = dtFactAch.getValueAt(selectedRow, 3);
    if (qteValue != null) {
        prod.setSelectedItem(qteValue);
    }
Object price = dtFactAch.getValueAt(selectedRow, 4);
if (price != null) {
    prix1.setText((price).toString()); // Use String.valueOf to convert the object to a string
}
Object qt = dtFactAch.getValueAt(selectedRow, 5);
if (qt != null) {
    qte.setText((qt).toString()); // Use String.valueOf to convert the object to a string
}
    
    
    
} else {
    // No row selected, handle this case if needed
}

      // TODO add your handling code here:
    }//GEN-LAST:event_dtFactAchMouseClicked

    private void IdAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IdAActionPerformed
//id();        // TODO add your handling code here:
    }//GEN-LAST:event_IdAActionPerformed

    private void ModifierFactureAchatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModifierFactureAchatActionPerformed
        // Check if IDFACTUR is not empty
        if (IDFACTUR.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner un ID.", "Champ requis", JOptionPane.WARNING_MESSAGE);
            return; // Exit method if IDFACTUR is empty
        }
        dbb db = dbb.getCon();
        AchatBeans sb = new AchatBeans();
        // Set the ID to the AchatBeans object
        sb.setId(Integer.valueOf(IDFACTUR.getText()));
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
        sb.modifierAchat(sb);
        updateJTable();
        fetchNextIDAndLoadIntoTextBox(IDFACTUR);
    }//GEN-LAST:event_ModifierFactureAchatActionPerformed

    private void IdAMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IdAMouseClicked
      //  id();  // TODO add your handling code here:
    }//GEN-LAST:event_IdAMouseClicked
public void fetchNextIDAndLoadIntoTextBox(JTextField id) {
    dbb db = dbb.getCon();  
    String sql = "SELECT MAX(ID) AS max_id FROM FactureAchat";

    try (
        PreparedStatement stmt = db.conx.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

        if (rs.next()) {
            int maxId = rs.getInt("max_id");
            int nextId = maxId + 1;
            IDFACTUR.setText(Integer.toString(nextId));
        } else {
            // Handle the case where no result is returned
            IDFACTUR.setText(""); // Clear the text box
        }

    } catch (SQLException ex) {
        // Handle SQL exception
        ex.printStackTrace();
    }
}

    private void prix1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prix1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_prix1ActionPerformed

    private void ModifierDetailFactureAchatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModifierDetailFactureAchatActionPerformed
 dbb b = dbb.getCon();
    AchatBeans sb = new AchatBeans();
      int produitID = prodd(prod.getSelectedItem().toString());
            // String selectedprod = (String) prod.getSelectedItem();
            // Match the selected NumFrs name to get its corresponding Id
            // int numero1 = prodd(selectedprod);
            int id = Integer.parseInt((String) IdA.getSelectedItem());

          //  String prix1 = id.getText();
            String qt = qte.getText();

            sb.setId(id);
            sb.setProduit(produitID);
            sb.setPrix(prix1.getText());
            sb.setQte(qt);
            sb.modifierDetailAchat(sb);
    updateJTable();        // TODO add your handling code here:
    }//GEN-LAST:event_ModifierDetailFactureAchatActionPerformed

    private void button9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button9ActionPerformed
      DefaultTableModel model = (DefaultTableModel) dtFactAch.getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    dtFactAch.setRowSorter(sorter);

    String searchText = textField5.getText().toLowerCase(); // Convert to lowercase for case-insensitive search

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
    }//GEN-LAST:event_button9ActionPerformed

    private void SupprimerDetailFactureAchatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SupprimerDetailFactureAchatActionPerformed
   dbb d = dbb.getCon();
AchatBeans sb = new AchatBeans();

//sb.setId(IDFACTUR.getText());
   sb.setId(Integer.valueOf(IDFACTUR.getText()));

sb.SupAChat(Integer.valueOf(IDFACTUR.getText()));
sb.SupAChatFacture(Integer.valueOf(IDFACTUR.getText()));
   
    //
        // TODO add your handling code here:
                        
updateJTable();
  fetchNextIDAndLoadIntoTextBox(IDFACTUR);
     //    afficherClient1();
        // TODO add your handling code here:
    }//GEN-LAST:event_SupprimerDetailFactureAchatActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
 // Assuming datePicker1 is an instance of a date picker that returns LocalDate
    LocalDate localDate = datePicker1.getDate();
    Date selectedDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust the format as needed
    String formattedSelectedDate = dateFormat.format(selectedDate);

    DefaultTableModel model = (DefaultTableModel) dtFactAch.getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    dtFactAch.setRowSorter(sorter);

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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
  int[] selectedRows = dtFactAch.getSelectedRows();

    try {
        // Load the appropriate JasperReport template based on the number of selected rows
        JasperDesign jasdi;
        if (selectedRows.length == 1) {
            jasdi = JRXmlLoader.load("C:\\Users\\Administrator\\Desktop\\gestionCom - Copie\\src\\AchatPar.jrxml");
        } else {
            jasdi = JRXmlLoader.load("C:\\Users\\Administrator\\Desktop\\gestionCom - Copie\\src\\Achat.jrxml");
        }

        // Construct SQL query dynamically based on selected rows
     StringBuilder sqlBuilder = new StringBuilder(
        "SELECT da.ID, \n" +
        "       p.Désignation AS Produit, \n" +
        "       CAST(da.PrixAchat AS INTEGER) AS PrixAchat, \n" +
        "       CAST(da.QteAchat AS INTEGER) AS QteAchat, \n" +
        "       f.RaisonSocale, \n" +
        "       fa.DateFacture, \n" +
        "       CONCAT(CAST(ROUND(CAST(da.PrixAchat AS DECIMAL(10, 2)) * CAST(da.QteAchat AS DECIMAL(10, 2)), 0) AS INTEGER), ' DH') AS Total, \n" +
        "       CONCAT(CAST(ROUND(SUM(CAST(da.PrixAchat AS DECIMAL(10, 2)) * CAST(da.QteAchat AS DECIMAL(10, 2))) OVER (), 0) AS INTEGER), ' DH') AS TotalPrice \n" +
        "FROM DetailAchat da \n" +
        "INNER JOIN Produit p ON da.NumPro = p.NumeroProduit \n" +
        "INNER JOIN FactureAchat fa ON da.ID = fa.ID \n" +
        "INNER JOIN Fournisseur f ON fa.Nfour = f.NumFrs"
    );


        if (selectedRows.length > 0) {
            sqlBuilder.append(" WHERE fa.ID IN (");
            for (int i = 0; i < selectedRows.length; i++) {
                String id = dtFactAch.getValueAt(selectedRows[i], 2).toString(); // Assuming the first column contains the id
                sqlBuilder.append("'").append(id).append("'");
                if (i < selectedRows.length - 1) {
                    sqlBuilder.append(",");
                }
            }
            sqlBuilder.append(")");
        }

        String sql = sqlBuilder.toString();
        System.out.println("Generated SQL Query: " + sql); // Debugging

        // Set the constructed SQL query
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

        // Check if the report has pages
        if (jd.getPages().isEmpty()) {
            System.out.println("No pages in the report. Check your data and query.");
        } else {
            // Show the report in a JasperViewer
            JasperViewer.viewReport(jd, false);
        }

    } catch (JRException | SQLException ex) {
        Logger.getLogger(Produit.class.getName()).log(Level.SEVERE, null, ex);
    }

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
dtFactAch.clearSelection();
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1MouseClicked
    private void addItem(String item) {
        prod.addItem(item); // Use jComboBox2 directly instead of super
        if (item != null) {
            fetchPrix(item);
        }
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Achat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Achat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Achat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Achat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Achat().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(Achat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

     // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button AjouterDetailFactureAchat;
    private java.awt.Button AjouterFactureAchat;
    private javax.swing.JTextField IDFACTUR;
    private javax.swing.JComboBox<String> IdA;
    private java.awt.Button ModifierDetailFactureAchat;
    private java.awt.Button ModifierFactureAchat;
    private javax.swing.JComboBox<String> NumF;
    private java.awt.Button SupprimerDetailFactureAchat;
    private java.awt.Button button9;
    private javax.swing.JTable dtFactAch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private java.awt.Label label1;
    private java.awt.Label label10;
    private java.awt.Label label2;
    private java.awt.Label label3;
    private java.awt.Label label4;
    private java.awt.Label label6;
    private java.awt.Label label7;
    private java.awt.Label label8;
    private java.awt.Label label9;
    private javax.swing.JTextField prix1;
    private javax.swing.JComboBox<String> prod;
    private javax.swing.JTextField qte;
    private java.awt.TextField textField5;
    // End of variables declaration   // Variables declaration - do not modify                     
    private java.awt.Button AjouterDetailFactureAchat;
    private java.awt.Button AjouterFactureAchat;
    private javax.swing.JTextField IDFACTUR;
    private javax.swing.JComboBox<String> IdA;
    private java.awt.Button ModifierDetailFactureAchat;
    private java.awt.Button ModifierFactureAchat;
    private javax.swing.JComboBox<String> NumF;
    private java.awt.Button SupprimerDetailFactureAchat;
    private java.awt.Button button9;
    private com.github.lgooddatepicker.components.DatePicker datePicker1;
    private com.github.lgooddatepicker.components.DatePicker datee;
    private javax.swing.JTable dtFactAch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private java.awt.Label label1;
    private java.awt.Label label10;
    private java.awt.Label label2;
    private java.awt.Label label3;
    private java.awt.Label label4;
    private java.awt.Label label6;
    private java.awt.Label label7;
    private java.awt.Label label8;
    private java.awt.Label label9;
    private javax.swing.JTextField prix1;
    private javax.swing.JComboBox<String> prod;
    private javax.swing.JTextField qte;
    private java.awt.TextField textField5;
    // End of variables declaration//GEN-END:variables

}
