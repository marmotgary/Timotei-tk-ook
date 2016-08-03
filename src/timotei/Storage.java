/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class Storage {

    
    public int addStorageDB(String name){
        String storageName = name;
        Connection c = null;
        PreparedStatement pstmt;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:/root/pakettiohjelma/pakettiohjelma/db.sqlite3");
            c.setAutoCommit(false);
            //Lisätään varasto-tauluun vaadittavat tiedot
            pstmt = c.prepareStatement(
                    "INSERT INTO Varasto (Nimi)VALUES( ?)");
            pstmt.setString(1, ""+storageName+"");
            pstmt.executeUpdate();
            pstmt.close();
            c.commit();
            System.out.println("storage added");
            c.close();
            return 1;
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.err.println("addStorage failed");
            try {
                c.close();
            } catch (SQLException ex1) {
                System.err.println("c.close error");;
            } return 0;
        }
        
    }
}
