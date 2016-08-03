/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author root
 */
public class dbControl {
    private static dbControl db = null;
    Connection c;
    Statement stmt;
    PreparedStatement pstmt;
    String pathToDB;
    

    private dbControl(String dbPath) {
        pathToDB = dbPath;
    }

    public static dbControl getInstance(String dbPath) {
        if (db == null) {
            db = new dbControl(dbPath);
        }
        return db;
    }
    
   protected void packageClasstoDB(ArrayList<Package> pClassList){
       c = null;
       stmt = null;
       pstmt = null;
       PackageClasses pClasses = new PackageClasses();
       pClasses.luokkalista(pClassList);
       
       try {
           Class.forName("org.sqlite.JDBC");
           c = DriverManager.getConnection(pathToDB);
           c.setAutoCommit(false);
           System.out.println("Opened database at dbControl packageClasstoDB successfully");
           for (Package p : pClassList) {

               stmt = c.createStatement();
               //Tehdään kysely tietokantaan ja varmistetaan ettei tietoa yritetä lisätä kahteen kertaan
               ResultSet rsPackageClass = stmt.executeQuery(
                       "SELECT Luokka FROM Pakettiluokka WHERE Luokka='"
                       + p.getLuokka() + "';");

               //System.out.println(e.getElementsByTagName("postoffice").item(0).getTextContent());
               if (rsPackageClass.next() == false
                       || rsPackageClass.getInt("Luokka")!=p.getLuokka()) {
                   //Lisätään Pakettiluokka tauluun vaadittavat tiedot
                   pstmt = c.prepareStatement(
                           "INSERT INTO Pakettiluokka (Luokka,Tilavuus,Nopeus,Rikkoutuu,Kantama)VALUES( ?,  ?,  ?, ?, ?)");
                   pstmt.setInt(1, p.getLuokka());
                   pstmt.setInt(2, p.getTilavuus());
                   pstmt.setInt(3, p.getNopeus());
                   pstmt.setInt(4, p.getRikkoutuu());
                   pstmt.setInt(5, p.getKantama());
                   pstmt.executeUpdate();
                   pstmt.close();

               } else {
                   //sumthings happening
               }
               //Koska pakettiluokat eivät muutu, lisätään ne ArrayListiin mistä ne saatetaan
               //käyttäjän näkyviin
               
               
               rsPackageClass.close();
           }

           c.commit();
           c.close();
       } catch (ClassNotFoundException | SQLException ex) {
           System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
           try {
               c.close();
           } catch (SQLException ex1) {
               System.err.println("c.close error errorissa");;
           }
       }
   }
   protected void createObject(Obj o){
       c = null;
       pstmt = null;
       
       try {
           Class.forName("org.sqlite.JDBC");
           c = DriverManager.getConnection(pathToDB);
           c.setAutoCommit(false);
           System.out.println("Opened database at dbControl packageClasstoDB successfully");
           pstmt = c.prepareStatement(
                   "INSERT INTO Esine (Rikkoutuva,Nimi,Tilavuus,Paino)VALUES( ?,  ?,  ?, ?)");
           pstmt.setInt(1, o.getRikkoutuva());
           pstmt.setString(2, o.getName());
           pstmt.setInt(3, o.getTilavuus());
           pstmt.setInt(4, o.getPaino());
           pstmt.executeUpdate();
           pstmt.close();
           c.commit();
           c.close();
       } catch (ClassNotFoundException | SQLException ex) {
           System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
           try {
               c.close();
           } catch (SQLException ex1) {
               System.err.println("c.close error errorissa");;
           }
       }
   }
   
   protected int createPackage(ObjX o, Package p){
       c = null;
       pstmt = null;
       int fragile;
       if(o.getTilavuus()<=p.getTilavuus()){
           if(o.getRikkoutuva()==1 && p.getRikkoutuu()==1){
               fragile=1;
               
           }else if(o.getRikkoutuva()==1 && p.getRikkoutuu()==2){
               fragile=2;
           }else{
               fragile=0;
            }
           try {
               Class.forName("org.sqlite.JDBC");
               c = DriverManager.getConnection(pathToDB);
               c.setAutoCommit(false);
               pstmt = c.prepareStatement(
                       "INSERT INTO Paketti (EsineID,Luokka,VarastoID, Rikkoutuu)VALUES(?,  ?, ?,?)");
               pstmt.setInt(1, o.getPackageID());
               pstmt.setInt(2, p.getLuokka());
               pstmt.setInt(3, 1);
               pstmt.setInt(4, fragile);
               pstmt.executeUpdate();
               pstmt.close();
               c.commit();
               c.close();
           } catch (ClassNotFoundException | SQLException ex) {
               System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
               try {
                   c.close();
               } catch (SQLException ex1) {
                   System.err.println("c.close error errorissa");;
               }
           }
           return 1;
       }
       else{
           return 0;
       }
   }
   
   protected int createHistoryEntry(long distance, int isBroken, Smartpost start, Smartpost dest, PackageX p){
       c = null;
       pstmt = null;
           try {
               Class.forName("org.sqlite.JDBC");
               c = DriverManager.getConnection(pathToDB);
               c.setAutoCommit(false);
               pstmt = c.prepareStatement(
                       "INSERT INTO Historia (Lähtöpaikka,Saapumispaikka,MatkaPituus, "
                               + "PakettiID, Rikkoutunut)VALUES(?,?,?,?,?)");
               pstmt.setString(1, start.getPostitoimisto());
               pstmt.setString(2, dest.getPostitoimisto());
               pstmt.setInt(3, (int) distance);
               pstmt.setInt(4, p.getPakettiID());
               pstmt.setInt(5, isBroken);
               pstmt.executeUpdate();
               pstmt.close();
               c.commit();
               c.close();
               return 1;
           } catch (ClassNotFoundException | SQLException ex) {
               System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
               try {
                   c.close();
               } catch (SQLException ex1) {
                   System.err.println("c.close error errorissa");;
               }
           }
           return 0;
   }
   //Palauttaa tietyssä paketissa olevan esineen painon
   protected int getObjectWeight(PackageX p){
       try {
           int weight = 0;
           c = null;
           stmt = null;
           Class.forName("org.sqlite.JDBC");
           c = DriverManager.getConnection(pathToDB);
           c.setAutoCommit(false);

           stmt = c.createStatement();
           //Haetaan kannasta tavara jonka paketti sisältää
           ResultSet rsObj = stmt.executeQuery(
                   "SELECT Paino FROM Esine\n"
                   + "WHERE esine.esineID="+p.getEsineID()+";");
           while (rsObj.next()) {
               ObjX o = new ObjX();
               weight = rsObj.getInt("Paino");
           }
           c.close();
           rsObj.close();
           return weight;
       } catch (ClassNotFoundException ex) {
           System.err.println("classnotfound");
       } catch (SQLException ex) {
           System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
       }
       return -1;
   }
   //Palauttaa tietyssä paketissa olevan esineen nimen
   protected String getObjectName(PackageX p){
       try {
           String name = null;
           c = null;
           stmt = null;
           Class.forName("org.sqlite.JDBC");
           c = DriverManager.getConnection(pathToDB);
           c.setAutoCommit(false);

           stmt = c.createStatement();
           //Haetaan kannasta tavara jonka paketti sisältää
           ResultSet rsObj = stmt.executeQuery(
                   "SELECT Nimi FROM Esine\n"
                   + "INNER JOIN \"Paketti\" ON Esine.EsineID=paketti.EsineID\n"
                   + "WHERE esine.esineID=paketti.esineID;");
           while (rsObj.next()) {
               ObjX o = new ObjX();
               name = rsObj.getString("Nimi");
           }
           c.close();
           rsObj.close();
           return name;
       } catch (ClassNotFoundException ex) {
           System.err.println("classnotfound");
       } catch (SQLException ex) {
           System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
       }
       return null;
   }
   
   //Täyttää listan tietokannasta löytyvillä esineillä.
   protected void noPackageObj(ArrayList<ObjX> list){
        try {
            c = null;
            stmt = null;
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(pathToDB);
            c.setAutoCommit(false);
            
            stmt = c.createStatement();
            //Haetaan kannasta ne tavarat joita ei ole paketoitu
            ResultSet rsObj = stmt.executeQuery(
                    "SELECT Esine.EsineID, Rikkoutuva, Nimi, Tilavuus, Paino FROM Esine\n" +
                    "LEFT JOIN \"Paketti\" ON Esine.EsineID=paketti.EsineID\n" +
                    "WHERE paketti.esineID IS NULL;");
            while(rsObj.next()){
                ObjX o = new ObjX();
                o.setEsineID(rsObj.getInt("EsineID"));
                o.setNimi(rsObj.getString("Nimi"));
                o.setPaino(rsObj.getInt("Paino"));
                o.setRikkoutuva(rsObj.getInt("Rikkoutuva"));
                o.setTilavuus(rsObj.getInt("Tilavuus"));
                list.add(o);
            }
            c.close();
            rsObj.close();
        } catch (ClassNotFoundException ex) {
            System.err.println("classnotfound");
        } catch (SQLException ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
        }
   }
   //Lisää lähetettävissä olevat paketit listaan
   protected void packageList(ArrayList<PackageX> list){
       try {
           c = null;
           stmt = null;
           Class.forName("org.sqlite.JDBC");
           c = DriverManager.getConnection(pathToDB);
           c.setAutoCommit(false);

           stmt = c.createStatement();
           //Haetaan kannasta ne tavarat joita ei ole paketoitu
           ResultSet rsPackage = stmt.executeQuery(
                   "SELECT Paketti.PakettiID, Paketti.EsineID, luokka, nimi, rikkoutuu FROM Paketti\n"
                           + "LEFT OUTER JOIN Esine ON Paketti.EsineID=Esine.EsineID\n"
                           + "LEFT JOIN Historia ON paketti.PakettiID=Historia.PakettiID\n"
                           + "WHERE Historia.PakettiID IS NULL;");
           while (rsPackage.next()) {
               PackageX p = new PackageX();
               p.setEsineID(rsPackage.getInt("EsineID"));
               p.setPakettiID(rsPackage.getInt("PakettiID"));
               p.setLuokka(rsPackage.getInt("Luokka"));
               p.setNimi(rsPackage.getString("Nimi"));
               p.setRikkoutuu(rsPackage.getInt("Rikkoutuu"));
               list.add(p);
           }
           c.close();
           rsPackage.close();
       } catch (ClassNotFoundException ex) {
           System.err.println("classnotfound");
       } catch (SQLException ex) {
           System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
       }
   }
   
   protected void historyList(ArrayList<History> list){
       try {
           c = null;
           stmt = null;
           Class.forName("org.sqlite.JDBC");
           c = DriverManager.getConnection(pathToDB);
           c.setAutoCommit(false);

           stmt = c.createStatement();
           //Haetaan kannasta historiatiedot
           ResultSet rs = stmt.executeQuery("SELECT * FROM HistoriaNäkymä;");
           while (rs.next()) {
               History h = new History();
               h.setStart(rs.getString("Lähtöpaikka"));
               h.setEnd(rs.getString("Saapumispaikka"));
               h.setDistance(rs.getInt("Matkapituus"));
               h.setObjName(rs.getString("Nimi"));
               h.setHistoryID(rs.getInt("HistoriaID"));
               h.setIsBroken(rs.getInt("Rikkoutunut"));
               h.setTimestamp(rs.getString("Aikaleima"));
               list.add(h);
           }
           c.close();
           rs.close();
       } catch (ClassNotFoundException ex) {
           System.err.println("classnotfound");
       } catch (SQLException ex) {
           System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
       }
   }
   
    protected int packageCount() {
        try {
            c = null;
            stmt = null;
            int count;
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(pathToDB);
            c.setAutoCommit(false);

            stmt = c.createStatement();
            //Haetaan kannasta historiatiedot
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Paketti;");
            
            rs.next();
            count = rs.getInt("total");

            c.close();
            rs.close();
            return count;
        } catch (ClassNotFoundException ex) {
            System.err.println("classnotfound");
        } catch (SQLException ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
        }
        return -1;
    }
   protected void deleteObject(ObjX o){
       c = null;
       pstmt = null;

       try {
           Class.forName("org.sqlite.JDBC");
           c = DriverManager.getConnection(pathToDB);
           c.setAutoCommit(false);
           pstmt = c.prepareStatement(
                   "DELETE FROM Esine WHERE EsineID = ?");
           pstmt.setInt(1, o.getPackageID());
           pstmt.executeUpdate();
           pstmt.close();
           c.commit();
           c.close();
       } catch (ClassNotFoundException | SQLException ex) {
           System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
           try {
               c.close();
           } catch (SQLException ex1) {
               System.err.println("c.close error errorissa");;
           }
       }
   }
   protected void deletePackage(PackageX p){
       c = null;
       pstmt = null;

       try {
           Class.forName("org.sqlite.JDBC");
           c = DriverManager.getConnection(pathToDB);
           c.setAutoCommit(false);
           pstmt = c.prepareStatement(
                   "DELETE FROM Paketti WHERE PakettiID = ?");
           pstmt.setInt(1, p.getPakettiID());
           pstmt.executeUpdate();
           pstmt.close();
           c.commit();
           c.close();
       } catch (ClassNotFoundException | SQLException ex) {
           System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
           try {
               c.close();
           } catch (SQLException ex1) {
               System.err.println("c.close error errorissa");;
           }
       }
   }
}
