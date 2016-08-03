/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;

import java.util.ArrayList;

/**
 *
 * @author root
 */
public abstract class Package {
    protected int luokka;
    protected int tilavuus;
    protected int nopeus;
    protected int rikkoutuu;
    protected int kantama;
    protected int esineID;
    protected int pakettiID;
    protected String nimi;
    

    protected int getLuokka() {
        return luokka;
    }

    protected int getTilavuus() {
        return tilavuus;
    }

    protected int getNopeus() {
        return nopeus;
    }

    protected int getRikkoutuu() {
        return rikkoutuu;
    }

    protected int getKantama() {
        return kantama;
    }
    
    @Override
    public String toString(){
        return ("Luokka "+luokka);
    }
}

class PackageX extends Package {

    public int getEsineID() {
        return esineID;
    }

    public int getPakettiID() {
        return pakettiID;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String Nimi) {
        this.nimi = Nimi;
    }

    public void setPakettiID(int pakettiID) {
        this.pakettiID = pakettiID;
    }

    public void setLuokka(int luokka) {
        this.luokka = luokka;
    }

    public void setTilavuus(int tilavuus) {
        this.tilavuus = tilavuus;
    }

    public void setNopeus(int nopeus) {
        this.nopeus = nopeus;
    }

    public void setRikkoutuu(int rikkoutuu) {
        this.rikkoutuu = rikkoutuu;
    }

    public void setKantama(int kantama) {
        this.kantama = kantama;
    }

    public void setEsineID(int esineID) {
        this.esineID = esineID;
    }
    @Override
    public String toString(){
        return (nimi+" "+luokka);
    }
}

class PackageClasses extends Package {

    public void luokkalista(ArrayList list) {
        Package p1 = new Package1();
        list.add(p1);
        
        Package p2 = new Package2();
        list.add(p2);
        
        Package p3 = new Package3();
        list.add(p3);
        

    }
    
}

