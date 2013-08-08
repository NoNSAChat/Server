/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Intern.MySQL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pascal
 */
public class MySQLConnection {

    private String MySQLBenutzer = "";
    private String MySQLPasswort = "";
    private String MySQLUrl = "jdbc:mysql://localhost:3306";
    private String MySQLDriver = "com.mysql.jdbc.Driver";
    private Connection MySQLConnection;

    public MySQLConnection() {
        try {
            //User und Passwort einlesen
            FileReader fr = new FileReader(new File("src/RMIChatServer/Password/Password.pwd"));
            BufferedReader br = new BufferedReader(fr);
            MySQLBenutzer = br.readLine();
            MySQLPasswort = br.readLine();            
            //Mit MySQL verbinden
            Class.forName(this.MySQLDriver);
            this.MySQLConnection = DriverManager.getConnection(this.MySQLUrl, this.MySQLBenutzer, this.MySQLPasswort);
            //DB auswählen
            Statement selectDB = MySQLConnection.createStatement();
            selectDB.execute("USE chatter");
            System.out.println("Mit MySQL verbunden");
        } catch (SQLException ex) {
            Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
