/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author root
 */
public class LoadXML {
    private String content;
    private Document doc;
    private ArrayList<Smartpost> smartList;
    
    
    public int loadXML(){
        
        try {
            URL url = new URL("http://smartpost.ee/fi_apt.xml");
            BufferedReader br;
            br = new BufferedReader(new InputStreamReader(url.openStream()));

            content = "";
            String line = null;
            while ((line = br.readLine()) != null) {
                content += line + "\n";
            }
        } catch (MalformedURLException ex) {
            System.err.println("URL error");
            return -1;
        } catch (IOException ex) {
            //Java antaa IOExceptionin mikäli internetyhteyttä annettuun osoitteeseen ei voida muodostaa.
            System.err.println("IO Er, internet bad?");
            return -2;
        }
        try {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        dBuilder = dbFactory.newDocumentBuilder();
            
            doc = dBuilder.parse(new InputSource(new StringReader(content)));
        } catch (IOException ex) {
            System.err.println("I/O error doc");
        } catch (SAXException | ParserConfigurationException ex) {
            Logger.getLogger(LoadXML.class.getName()).log(Level.SEVERE, null, ex);
        }

        doc.getDocumentElement().normalize();
        //list = new ArrayList();
        return 1;
        
    }
    
    public void parseCurrentData(ArrayList list, String pathToDB){
        this.smartList = list;
        NodeList nodes = doc.getElementsByTagName("place");
        Connection c = null;
        Statement stmt = null;
        PreparedStatement pstmt;
        
        
        
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(pathToDB);
            c.setAutoCommit(false);
            System.out.println("Opened database successfullyyyyyyy");
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                Element e = (Element) node;

                //System.out.println(i);
                stmt = c.createStatement();
                //Tehdään kysely tietokantaan ja varmistetaan ettei tietoa yritetä lisätä kahteen kertaan
                ResultSet rsSmartpost = stmt.executeQuery(
                "SELECT Postitoimisto FROM Smartpost WHERE Postitoimisto='"
                +e.getElementsByTagName("postoffice").item(0).getTextContent()+"';");
                    
                //System.out.println(e.getElementsByTagName("postoffice").item(0).getTextContent());
                if(rsSmartpost.next()==false || 
                !rsSmartpost.getString("Postitoimisto").equals(e.getElementsByTagName("postoffice").item(0).getTextContent())){
                    //Lisätään Smartpost tauluun vaadittavat tiedot
                    pstmt = c.prepareStatement(
                            "INSERT INTO Smartpost (Postitoimisto,Aukiolo,VarastoID)VALUES( ?,  ?,  ?)");
                    pstmt.setString(1, e.getElementsByTagName("postoffice").item(0).getTextContent());
                    pstmt.setString(2, e.getElementsByTagName("availability").item(0).getTextContent());
                    pstmt.setInt(3, 1);
                    pstmt.executeUpdate();
                    //Lisätään Sijainti-tauluun vaadittavat tiedot
                    //SijaintiID kasvaa itsestään oikein kun tietokantaan lisätään
                    //jokainen smartpost ja sen sijaintitiedot samalla commitilla.
                    pstmt = c.prepareStatement(
                            "INSERT INTO Sijainti (Postitoimisto,Kaupunki,Osoite)VALUES( ?,  ?,  ?)");
                    pstmt.setString(1, e.getElementsByTagName("postoffice").item(0).getTextContent());
                    pstmt.setString(2, e.getElementsByTagName("city").item(0).getTextContent());
                    pstmt.setString(3, e.getElementsByTagName("address").item(0).getTextContent());
                    pstmt.executeUpdate();
                    //Lisätään Postinumero tauluun vaadittavat tiedot
                    pstmt = c.prepareStatement(
                            "INSERT INTO Postinumero (Postinumero)VALUES( ?)");
                    pstmt.setString(1, e.getElementsByTagName("code").item(0).getTextContent());
                    pstmt.executeUpdate();
                    
                    pstmt = c.prepareStatement(
                            "INSERT INTO Koordinaatit (Latitude,Longitude)VALUES( ?,  ?)");
                    pstmt.setString(1, e.getElementsByTagName("lat").item(0).getTextContent());
                    pstmt.setString(2, e.getElementsByTagName("lng").item(0).getTextContent());
                    pstmt.executeUpdate();
                    pstmt.close();
                    
                } else {
                    //sumthings happening
                }
                //Luodaan samalla vaivalla smartpost oliot
                Smartpost newSp = new Smartpost(e.getElementsByTagName("postoffice").item(0).getTextContent(),
                        e.getElementsByTagName("availability").item(0).getTextContent(),
                        e.getElementsByTagName("city").item(0).getTextContent(),
                        e.getElementsByTagName("address").item(0).getTextContent(),
                        e.getElementsByTagName("code").item(0).getTextContent(),
                        e.getElementsByTagName("lat").item(0).getTextContent(),
                        e.getElementsByTagName("lng").item(0).getTextContent());
                smartList.add(newSp);
                rsSmartpost.close();
            }
            
            //list.add(newTheatre);
            //list.add(e.getElementsByTagName("Name").item(0).getTextContent());
            c.commit();
            c.close();
        }
        catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.err.println("XML luku tietokantaan epäonnistui, ");
            try {
                c.close();
            } catch (SQLException ex1) {
                System.err.println("c.close error errorissa");;
            }
        }
        
    }
}
