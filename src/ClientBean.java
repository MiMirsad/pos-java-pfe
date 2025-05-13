
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PC
 */
public class ClientBean {
      private String CodeClient;
    private String RaisonSociale;
    private String ICE;
    private String Adresse;
    private String Telephon;
    private String Email;
   

    public String getCodeClient() {
        return CodeClient;
    }

    public void setCodeClient(String CodeClient) {
        this.CodeClient=CodeClient;
    }

    public String getRaisonSociale() {
        return RaisonSociale;
    }
     public void setRaisonSociale(String RaisonSociale) {
        this.RaisonSociale = RaisonSociale;
    }

    public String getICE() {
        return ICE;
    }

    public void setICE(String ICE) {
        this.ICE = ICE;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String Adresse) {
        this.Adresse = Adresse;
    }

    public String getTelephon() {
        return Telephon;
    }

    public void setTelephon(String Telephon) {
        this.Telephon = Telephon;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }
    




}

   