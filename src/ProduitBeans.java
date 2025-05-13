/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author PC
 */
public class ProduitBeans {
   
   private String Categorie;
    private String        QuantitéEnStock;
        private String    Désignation;
                 private String   NumeroProduit;
               private String     Prix;

    public String getCategorie() {
        return Categorie;
    }

    public void setCategorie(String Categorie) {
        this.Categorie = Categorie;
    }

    public String getQuantitéEnStock() {
        return QuantitéEnStock;
    }

    public void setQuantitéEnStock(String QuantitéEnStock) {
        this.QuantitéEnStock = QuantitéEnStock;
    }

    public String getDésignation() {
        return Désignation;
    }

    public void setDésignation(String Désignation) {
        this.Désignation = Désignation;
    }

    public String getNumeroProduit() {
        return NumeroProduit;
    }

    public void setNumeroProduit(String NumeroProduit) {
        this.NumeroProduit = NumeroProduit;
    }

    public String getPrix() {
        return Prix;
    }

    public void setPrix(String Prix) {
        this.Prix = Prix;
    }
}
