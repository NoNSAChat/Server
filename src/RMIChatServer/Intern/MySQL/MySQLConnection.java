/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Intern.MySQL;

import RMIChatServer.CommonFunctions.CommonFunctions;
import RMIChatServer.Exception.InternalServerErrorException;
import RMIChatServer.Exception.UserNotFoundException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pascal
 */
public class MySQLConnection {

    private String MySQLUser = "";
    private String MySQLPassword = "";
    private String MySQLUrl = "jdbc:mysql://localhost:3306";
    private String MySQLDriver = "com.mysql.jdbc.Driver";
    private Connection MySQLConnection;
    private CommonFunctions functions = new CommonFunctions();

    public MySQLConnection() {
        try {
            //User und Passwort einlesen
            FileReader fr = new FileReader(new File("src/RMIChatServer/Password/Password.pwd"));
            BufferedReader br = new BufferedReader(fr);
            MySQLUser = br.readLine();
            MySQLPassword = br.readLine();
            //Mit MySQL verbinden
            Class.forName(this.MySQLDriver);
            this.MySQLConnection = DriverManager.getConnection(this.MySQLUrl, this.MySQLUser, this.MySQLPassword);
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

    //Test-Funktionen
    public void createTestUser() throws Exception {
        String sql = "INSERT INTO `chatter`.`user`(`username`, `forename`, `lastname`, `residence`, `mail`, `password`, `salt`, `publickey`, `privatekey`) "
                + "VALUES (?,?,?,?,?,?,?,?,?);";

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        KeyPair kp = kpg.generateKeyPair();
        byte[] pk = functions.AESEncrypt(kp.getPrivate().getEncoded(), functions.generateBenutzerAESKey("TestUser1", "111"));


        PreparedStatement statement = MySQLConnection.prepareStatement(sql);
        statement.setString(1, "TestUser1");
        statement.setString(2, "Forename1");
        statement.setString(3, "Lastname1");
        statement.setString(4, "Residence1");
        statement.setString(5, "Mail1");
        statement.setBytes(6, functions.HashPassword("111", "Salt"));
        statement.setString(7, "Salt");
        statement.setBytes(8, kp.getPublic().getEncoded());
        statement.setBytes(9, pk);

        statement.executeUpdate();

        kp = kpg.generateKeyPair();
        pk = functions.AESEncrypt(kp.getPrivate().getEncoded(), functions.generateBenutzerAESKey("TestUser1", "111"));


        statement = MySQLConnection.prepareStatement(sql);
        statement.setString(1, "TestUser2");
        statement.setString(2, "Forename2");
        statement.setString(3, "Lastname2");
        statement.setString(4, "Residence2");
        statement.setString(5, "Mail2");
        statement.setBytes(6, functions.HashPassword("222", "Salt"));
        statement.setString(7, "Salt");
        statement.setBytes(8, kp.getPublic().getEncoded());
        statement.setBytes(9, pk);

        statement.executeUpdate();

        statement.close();
    }

    /*
     * Schreibt eine Message in die Datenbank.
     */
    public void createMessage(byte[] message, int sender, int reciever) throws InternalServerErrorException, UserNotFoundException {
        try {
            //Suche nach den beiden Benutzern
            //Hole Anzahl aus DB
            String sql = "SELECT count(*) as count FROM chatter.user WHERE user.id = ? OR user.id = ?;";
            PreparedStatement statement = MySQLConnection.prepareStatement(sql);
            statement.setInt(1, sender);
            statement.setInt(2, reciever);
            ResultSet resultSet = statement.executeQuery();

            //Überprüfe die Anzahl
            resultSet.first();
            if (resultSet.getInt("count") < 2){
                throw new UserNotFoundException("Benutzer konnten nicht gefunden werden");
            }
            
            //Schreibe die Nachricht in die DB
            sql = "INSERT INTO `chatter`.`message` (`sender`, `reciever`, `message`, `seen`) VALUES (?, ?, ?, ?);";
            statement = MySQLConnection.prepareStatement(sql);
            statement.setInt(1, sender);
            statement.setInt(2, reciever);
            statement.setBytes(3, message);
            statement.setInt(4, 1);
            statement.executeUpdate();
            statement.close();
            
        } catch (SQLException ex) {
            throw new InternalServerErrorException(ex.getMessage());
        }
    }
}
