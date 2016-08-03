/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;

/**
 *
 * @author root
 */
public class Smartpost {
    private String postitoimisto;
    private String aukiolo;
    private String kaupunki;
    private String osoite;
    private String postinumero;
    private String latitude;
    private String longitude;
    
    Smartpost(String postitoimisto, String aukiolo, String kaupunki,
            String osoite, String postinumero, String latitude, String longitude) {
        this.aukiolo = aukiolo;
        this.kaupunki = kaupunki;
        this.latitude = latitude;
        this.longitude = longitude;
        this.osoite = osoite;
        this.postinumero = postinumero;
        this.postitoimisto = postitoimisto;
    }

    public String getPostitoimisto() {
        return postitoimisto;
    }

    public String getAukiolo() {
        return aukiolo;
    }

    public String getKaupunki() {
        return kaupunki;
    }

    public String getOsoite() {
        return osoite;
    }

    public String getPostinumero() {
        return postinumero;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
    
    
    @Override
    public String toString() {
        return this.postitoimisto;
    }
}
