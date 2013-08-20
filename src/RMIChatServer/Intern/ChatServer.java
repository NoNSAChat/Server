/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Intern;

import RMIChatServer.Benutzer.Friend;
import RMIChatServer.Benutzer.MyUser;
import RMIChatServer.Benutzer.User;
import RMIChatServer.CommonFunctions.CommonFunctions;
import RMIChatServer.Exception.InternalServerErrorException;
import RMIChatServer.Exception.MailAlreadyInUseException;
import RMIChatServer.Exception.NoConversationFoundException;
import RMIChatServer.Exception.PasswordInvalidException;
import RMIChatServer.Exception.SessionDeniedException;
import RMIChatServer.Exception.UserAlreadyExsistsException;
import RMIChatServer.Exception.UserAreAlreadyFriendsException;
import RMIChatServer.Exception.UserNotFoundException;
import RMIChatServer.Exception.WrongPasswordException;
import RMIChatServer.Intern.Session.SessionHandler;
import RMIChatServer.Message.Message;
import RMIChatServer.Server.ChatServerInterface;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Pascal
 */
public class ChatServer extends UnicastRemoteObject implements ChatServerInterface {

    private CommonFunctions function;
    private SessionHandler SessionHandler;
    private String MySQLUser = "";
    private String MySQLPassword = "";
    private String MySQLUrl = "jdbc:mysql://localhost:3306";
    private String MySQLDriver = "com.mysql.jdbc.Driver";
    private Connection MySQLConnection;

    public ChatServer() throws RemoteException, Exception {
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
        selectDB.close();
        System.out.println("Mit MySQL verbunden");
        function = new CommonFunctions();
        SessionHandler = new SessionHandler();
    }
    
    private MyUser getMyUser(String username) throws InternalServerErrorException {
        MyUser myUser = null;
        try {
            String sql = "SELECT * FROM chatter.user WHERE username = ?;";
            PreparedStatement statement = MySQLConnection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            rs.first();

            myUser = new MyUser(
                    rs.getBytes("privatekey"),
                    rs.getString("forename"),
                    rs.getString("lastname"),
                    rs.getString("residence"),
                    rs.getString("mail"),
                    SessionHandler.generateSession(rs.getInt("id")),
                    rs.getInt("id"),
                    rs.getString("username"));

        } catch (SQLException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException();
        }
        return myUser;
    }
    
    @Override
    public MyUser createUser(MyUser myUser, String password) throws UserAlreadyExsistsException, PasswordInvalidException, MailAlreadyInUseException, InternalServerErrorException {
        if (function.checkPassword(password) == false) {
            throw new PasswordInvalidException();
        }
        if (function.checkUserDetails(myUser) == false) {
            throw new InternalServerErrorException();
        }
        try {
            String sql = "SELECT COUNT(*) FROM chatter.user WHERE username = ?;";
            PreparedStatement statement = MySQLConnection.prepareStatement(sql);
            statement.setString(1, myUser.getUsername());
            ResultSet res = statement.executeQuery();
            res.first();
            int count = res.getInt(1);
            if (count > 0) {
                throw new UserAlreadyExsistsException();
            }
            sql = "SELECT COUNT(*) FROM chatter.user WHERE mail = ?;";
            statement = MySQLConnection.prepareStatement(sql);
            statement.setString(1, myUser.getMail());
            res = statement.executeQuery();
            res.first();
            count = res.getInt(1);
            if (count > 0) {
                throw new MailAlreadyInUseException();
            }

            sql = "INSERT INTO chatter.user "
                    + "(username, forename, lastname, residence, mail, password, salt, publickey, privatekey) "
                    + "VALUES (?,?,?,?,?,?,?,?,?);";
            statement = MySQLConnection.prepareStatement(sql);
            statement.setString(1, myUser.getUsername());
            statement.setString(2, myUser.getForename());
            statement.setString(3, myUser.getLastname());
            statement.setString(4, myUser.getResidence());
            statement.setString(5, myUser.getMail());

            SecureRandom random = new SecureRandom();
            byte seed[] = new byte[64];
            random.nextBytes(seed);

            statement.setBytes(6, function.HashPassword(password, seed));
            statement.setBytes(7, seed);

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048, random);
            KeyPair pair = keyGen.generateKeyPair();
            statement.setBytes(8, pair.getPublic().getEncoded());

            statement.setBytes(9, generatePrivateKey(password, pair));
            statement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException();
        }

        MyUser newUser = getMyUser(myUser.getUsername());
        if (newUser.getUsername().equals("")) {
            System.out.println("Benutzer nicht korrekt angelegt");
            throw new InternalServerErrorException();
        }
        return newUser;
    }
    
    private byte[] generatePrivateKey(String password, KeyPair pair) throws NoSuchAlgorithmException, InternalServerErrorException {
        MessageDigest MD5 = MessageDigest.getInstance("MD5");
        SecretKey secKey = new SecretKeySpec(MD5.digest(function.StringToByte(password)), "AES");
        return function.AESEncrypt(pair.getPublic().getEncoded(), secKey);
    }

    @Override
    public MyUser editUser(String sessionKey, MyUser editUser) throws SessionDeniedException, InternalServerErrorException, UserAlreadyExsistsException, MailAlreadyInUseException {

        SessionHandler.checkSession(sessionKey);
        int oldUserID = SessionHandler.getUserID(sessionKey);

        if (function.checkUserDetails(editUser) == false) {
            throw new InternalServerErrorException();
        }
        try {
            String sql = "SELECT COUNT(*) FROM chatter.user WHERE username = ?;";
            PreparedStatement statement = MySQLConnection.prepareStatement(sql);
            statement.setString(1, editUser.getUsername());
            ResultSet res = statement.executeQuery();
            res.first();
            int count = res.getInt(1);
            if (count > 0) {
                throw new UserAlreadyExsistsException();
            }
            sql = "SELECT COUNT(*) FROM chatter.user WHERE mail = ?;";
            statement = MySQLConnection.prepareStatement(sql);
            statement.setString(1, editUser.getMail());
            res = statement.executeQuery();
            res.first();
            count = res.getInt(1);
            if (count > 0) {
                throw new MailAlreadyInUseException();
            }

            sql = "UPDATE chatter.user SET username = ?, forename = ?, lastname = ?, residence = ?, mail = ? WHERE id = ?;";
            statement = MySQLConnection.prepareStatement(sql);
            statement.setString(1, editUser.getUsername());
            statement.setString(2, editUser.getForename());
            statement.setString(3, editUser.getLastname());
            statement.setString(4, editUser.getResidence());
            statement.setString(5, editUser.getMail());
            statement.setInt(6, oldUserID);
            statement.executeUpdate();

            MyUser newUser = getMyUser(editUser.getUsername());
            if (newUser.getUsername().equals("")) {
                System.out.println("Benutzer nicht korrekt geändert");
                throw new InternalServerErrorException();
            }
            return newUser;
        } catch (SQLException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException();
        }
    }

    @Override
    public byte[] editPassword(String sessionKey, String oldPassword, String newPassword) throws SessionDeniedException, PasswordInvalidException, WrongPasswordException, InternalServerErrorException {
       SessionHandler.checkSession(sessionKey);
       int userID = SessionHandler.getUserID(sessionKey);
       
       try {
            if (function.checkPassword(newPassword) == false) {
                throw new PasswordInvalidException();
            }
            String sql = "SELECT * FROM chatter.user WHERE id = ?;";
            PreparedStatement statement = MySQLConnection.prepareStatement(sql);
            statement.setInt(1, userID);
            ResultSet res = statement.executeQuery();
         
            res.first();
            if (!Arrays.equals(res.getBytes("password"),function.HashPassword(oldPassword, res.getBytes("salt")))) {
                throw new WrongPasswordException();
            }
            
            sql = "UPDATE chatter.user SET password = ?, salt = ?, publickey = ?, privatekey = ?;";
            statement = MySQLConnection.prepareStatement(sql);
            statement.setInt(1, userID);
            
            SecureRandom random = new SecureRandom();
            byte seed[] = new byte[64];
            random.nextBytes(seed);

            statement.setBytes(1, function.HashPassword(newPassword, seed));
            statement.setBytes(2, seed);

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048, random);
            KeyPair pair = keyGen.generateKeyPair();
            statement.setBytes(3, pair.getPublic().getEncoded());
            statement.setBytes(4, generatePrivateKey(newPassword, pair));
            statement.executeUpdate();
            
            return generatePrivateKey(newPassword, pair);
        
        } catch (SQLException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException();
        }
    }

    @Override
    public User[] searchFriend(String username, String forename, String lastname, String residence, String mail) throws InternalServerErrorException {
        if (false) {
            throw new InternalServerErrorException();
        }
        return null;
    }

    @Override
    public MyUser login(String username, String password) throws WrongPasswordException, UserNotFoundException, InternalServerErrorException {
        try {
            String sql = "SELECT * FROM chatter.user WHERE username = ?;";
            PreparedStatement statement;
            statement = MySQLConnection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet res = statement.executeQuery();
            res.last();

            if (res.getRow() == 0) {
                throw new UserNotFoundException();
            }
            res.first();
            if (!Arrays.equals(res.getBytes("password"),function.HashPassword(password, res.getBytes("salt")))) {
                throw new WrongPasswordException();
            }
            MyUser newUser = getMyUser(username);
            if (newUser.getUsername().equals("")) {
                System.out.println("Benutzer nicht gefunden");
                throw new InternalServerErrorException();
            }
            return newUser;
        } catch (SQLException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException();
        }
    }

    @Override
    public void sendMessage(String sessionKey, Message message) throws SessionDeniedException, UserNotFoundException, InternalServerErrorException {
        //checke Session auf Gültigkeit
        SessionHandler.checkSession(sessionKey);

        int sender = SessionHandler.getUserID(sessionKey);
        //Für Tests
        //int sender = 1;
        int reciever = message.getUser();

        if (message.getUser() == sender) {
            throw new InternalServerErrorException("Ein Benutzer kann sich selber keine Nachricht senden");
        }

        //Schreibe die Message in die Datenbank
        //Suche nach den beiden Benutzern
        //Hole Anzahl aus DB
        try {
            String sql = "SELECT count(*) as count FROM chatter.user WHERE user.id = ? OR user.id = ?;";
            PreparedStatement statement = MySQLConnection.prepareStatement(sql);
            statement.setInt(1, sender);
            statement.setInt(2, reciever);
            ResultSet resultSet = statement.executeQuery();

            //Überprüfe die Anzahl
            resultSet.first();
            if (resultSet.getInt("count") < 2) {
                throw new UserNotFoundException("Benutzer konnten nicht gefunden werden");
            }

            //Schreibe die Nachricht in die DB
            sql = "INSERT INTO `chatter`.`message` (`sender`, `reciever`, `message`, `seen`) VALUES (?, ?, ?, ?);";
            statement = MySQLConnection.prepareStatement(sql);
            statement.setInt(1, sender);
            statement.setInt(2, reciever);
            statement.setBytes(3, message.getMessage());
            statement.setInt(4, 0);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            throw new InternalServerErrorException("SQLException");
        }

        if (false) {
            throw new UserNotFoundException();
        } else if (false) {
            throw new InternalServerErrorException();
        } else if (false) {
            throw new SessionDeniedException();
        }
    }

    @Override
    public Message[] getLastMessages(String sessionKey, int user, int count, int id) throws SessionDeniedException, NoConversationFoundException, InternalServerErrorException {

        //checke Session auf Gültigkeit
        SessionHandler.checkSession(sessionKey);

        Message[] messages = null;
        int sender = SessionHandler.getUserID(sessionKey);
        //Für Tests
        //int sender = 1;
        int reciever = user;
        String sql = "";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            //Hole Nachrichten aus DB
            if (id != 0) {
                //Mit id Berücksichtigung
                sql = "SELECT * FROM chatter.message WHERE ((sender = ? AND reciever = ?) OR (sender = ? AND reciever = ?)) AND id > ? ORDER BY id DESC LIMIT ?;";
                statement = MySQLConnection.prepareStatement(sql);
                statement.setInt(1, sender);
                statement.setInt(2, reciever);
                statement.setInt(3, reciever);
                statement.setInt(4, sender);
                statement.setInt(5, id);
                statement.setInt(6, count);
                rs = statement.executeQuery();
            } else {
                //Lade die letzten Nachrichten
                sql = "SELECT * FROM chatter.message WHERE ((sender = ? AND reciever = ?) OR (sender = ? AND reciever = ?)) ORDER BY id DESC LIMIT ?;";
                statement = MySQLConnection.prepareStatement(sql);
                statement.setInt(1, sender);
                statement.setInt(2, reciever);
                statement.setInt(3, reciever);
                statement.setInt(4, sender);
                statement.setInt(5, count);
                rs = statement.executeQuery();
            }

            //Anzahl der Ergebnisse
            int countResults = 0;
            if (rs.first()) {
                countResults++;
                while (rs.next()) {
                    countResults++;
                }
            }

            //Lade das Ergebnis in ein Message Array
            messages = new Message[countResults];
            rs.first();
            for (int i = 0; i < messages.length; i++) {
                messages[i] = new Message(rs.getInt("id"), rs.getInt("reciever"), rs.getBytes("message"));
                rs.next();
            }
            statement.close();
        } catch (SQLException ex) {
            throw new InternalServerErrorException(ex.getMessage());
        }
        if (false) {
            throw new NoConversationFoundException();
        } else if (false) {
            throw new InternalServerErrorException();
        } else if (false) {
            throw new SessionDeniedException();
        }
        return messages;
    }

    @Override
    public Message[] getMessagesSinceID(String sessionKey, int user, int lastID, int count) throws SessionDeniedException, NoConversationFoundException, InternalServerErrorException {
        //checke Session auf Gültigkeit
        SessionHandler.checkSession(sessionKey);

        Message[] messages = null;
        try {
            //Für Tests
            //int sender = 1;
            int sender = SessionHandler.getUserID(sessionKey);
            int reciever = user;

            //Lade Nachrichten aus DB
            String sql = "SELECT * FROM chatter.message WHERE ((sender = ? AND reciever = ?) OR (sender = ? AND reciever = ?)) AND id < ? ORDER BY id DESC LIMIT ?;";
            PreparedStatement statement = MySQLConnection.prepareStatement(sql);
            statement.setInt(1, sender);
            statement.setInt(2, reciever);
            statement.setInt(3, reciever);
            statement.setInt(4, sender);
            statement.setInt(5, lastID);
            statement.setInt(6, count);

            ResultSet rs = statement.executeQuery();

            //Anzahl der Ergebnisse
            int countResults = 0;
            if (rs.first()) {
                countResults++;
                while (rs.next()) {
                    countResults++;
                }
            }

            //Lade das Ergebnis in ein Message Array
            messages = new Message[countResults];
            rs.first();
            for (int i = 0; i < messages.length; i++) {
                messages[i] = new Message(rs.getInt("id"), rs.getInt("reciever"), rs.getBytes("message"));
                rs.next();
            }
            statement.close();
        } catch (SQLException ex) {
            throw new InternalServerErrorException(ex.getMessage());
        }
        if (false) {
            throw new NoConversationFoundException();
        } else if (false) {
            throw new InternalServerErrorException();
        } else if (false) {
            throw new SessionDeniedException();
        }
        return messages;
    }

    @Override
    public Friend[] getFriendlist(String sessionKey) throws InternalServerErrorException, SessionDeniedException {
        Friend[] friends = null;
        //Checke Session auf Gültigkeit!
        SessionHandler.checkSession(sessionKey);

        String sql;
        PreparedStatement statement;
        ResultSet rs;
        int user = SessionHandler.getUserID(sessionKey);
        //Für Tests
        //int user = 1;

        try {
            //Hole Daten aus DB
            sql = "SELECT user.id, user.username, max(message.seen) as seen "
                    + "FROM chatter.friend friend, chatter.user user, chatter.message message "
                    + "WHERE friend.user = ? "
                    + "AND friend.friend = (SELECT friend.friend FROM chatter.friend friend WHERE friend.user = ?) "
                    + "AND friend.friend = user.id "
                    + "AND message.reciever = friend.user AND message.sender = friend.friend;";
            statement = MySQLConnection.prepareStatement(sql);
            statement.setInt(1, user);
            statement.setInt(2, user);

            rs = statement.executeQuery();

            //Schreibe Ergebnis um
            int count = 0;
            if (rs.first()) {
                count++;
                while (rs.next()) {
                    count++;
                }
            }
            friends = new Friend[count];
            rs.first();
            int friendID;
            String username;
            Boolean newMessage;
            Boolean isOnline = null;
            for (int i = 0; i < count; i++) {
                friendID = rs.getInt("id");
                username = rs.getString("username");
                if (rs.getInt("seen") == 0) {
                    newMessage = false;
                } else {
                    newMessage = true;
                }
                //isOnline = SessionHandler.hasSession(user);
                friends[i] = new Friend(newMessage, isOnline, friendID, username);
            }
        } catch (SQLException ex) {
            throw new InternalServerErrorException("SQLException: " + ex.getMessage());
        }

        if (false) {
            throw new InternalServerErrorException();
        }
        return friends;
    }


    @Override
    public void addFriend(String sessionKey, int friendID) throws UserAreAlreadyFriendsException, UserNotFoundException, SessionDeniedException, InternalServerErrorException {
        //Checke Session auf Gültigkeit!
        SessionHandler.checkSession(sessionKey);

        String sql;
        PreparedStatement statement;
        ResultSet rs;
        int user = SessionHandler.getUserID(sessionKey);
        //Für Tests
        //int user = 1;
        int friend = friendID;
        try {
            //Überprüfe ob Benutzer vorhanden
            checkUserExists(user, friend);

            //Überprüfe, ob Freunde schon vorhanden.
            checkFriendsExists(user, friend);

            //Lege Freunde an.
            //Lege Key an.
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128);
            Key key = kgen.generateKey();
            byte[] keyBytes = key.getEncoded();

            //verschlüssele für user
            sql = "SELECT publicKey FROM chatter.user WHERE id = ?;";
            statement = MySQLConnection.prepareStatement(sql);
            statement.setInt(1, user);
            rs = statement.executeQuery();
            rs.first();
            byte[] encryptedUserKey = function.RSAEncrypt(keyBytes, function.byteToPublicKey(rs.getBytes("publicKey")));

            //Verschlüssle für friend
            sql = "SELECT publicKey FROM chatter.user WHERE id = ?;";
            statement = MySQLConnection.prepareStatement(sql);
            statement.setInt(1, friend);
            rs = statement.executeQuery();
            rs.first();
            byte[] encryptedFriendKey = function.RSAEncrypt(keyBytes, function.byteToPublicKey(rs.getBytes("publicKey")));

            //lege Datensätze an für user
            sql = "INSERT INTO `chatter`.`friend` (`user`, `friend`, `key`) VALUES (?, ?, ?);";
            statement = MySQLConnection.prepareStatement(sql);
            statement.setInt(1, user);
            statement.setInt(2, friend);
            statement.setBytes(3, encryptedUserKey);
            statement.executeUpdate();

            //lege Datensätze an für user
            sql = "INSERT INTO `chatter`.`friend` (`user`, `friend`, `key`) VALUES (?, ?, ?);";
            statement = MySQLConnection.prepareStatement(sql);
            statement.setInt(1, friend);
            statement.setInt(2, user);
            statement.setBytes(3, encryptedFriendKey);
            statement.executeUpdate();

        } catch (SQLException ex) {
            throw new InternalServerErrorException("SQLException: " + ex.getMessage());
        } catch (NoSuchAlgorithmException ex) {
            throw new InternalServerErrorException("NoSuchAlgorithmException: " + ex.getMessage());
        }
        if (false) {
            throw new InternalServerErrorException();
        } else if (false) {
            throw new SessionDeniedException();
        }
    }

    @Override
    public byte[] getConversationKey(String sessionKey, int userID) throws SessionDeniedException, NoConversationFoundException, InternalServerErrorException {
        //Checke Session auf Gültigkeit!
        SessionHandler.checkSession(sessionKey);

        int user = SessionHandler.getUserID(sessionKey);
        //Für Tests
        //int user = 1;
        int friend = userID;

        String sql;
        PreparedStatement statement;
        ResultSet rs;

        byte[] key = null;

        //Überprüfe, ob Unterhaltung vorhanden ist
        checkConversationExsists(user, friend);

        try {
            sql = "SELECT f.key FROM chatter.friend f WHERE user = ? AND friend = ?;";
            statement = MySQLConnection.prepareStatement(sql);
            statement.setInt(1, user);
            statement.setInt(2, friend);
            rs = statement.executeQuery();
            rs.first();
            key = rs.getBytes("key");

        } catch (SQLException ex) {
            throw new InternalServerErrorException("SQLException: " + ex.getMessage());
        }

        if (false) {
            throw new SessionDeniedException();
        } else if (false) {
            throw new NoConversationFoundException();
        }
        return key;
    }

    @Override
    public void logOff(String sessionKey) throws InternalServerErrorException {
        SessionHandler.destroySession(sessionKey);
        if (false) {
            throw new InternalServerErrorException();
        }
    }

    private void checkUserExists(int user, int friend) throws UserNotFoundException, InternalServerErrorException {
        try {
            String sql = "SELECT count(*) as count FROM chatter.user WHERE id = ? OR id = ?;";
            PreparedStatement statement = MySQLConnection.prepareStatement(sql);
            statement.setInt(1, user);
            statement.setInt(2, friend);
            ResultSet rs = statement.executeQuery();
            rs.first();
            if (rs.getInt("count") != 2) {
                throw new UserNotFoundException("Benutzer konnten nicht gefunden weden!");
            }
        } catch (SQLException ex) {
            throw new InternalServerErrorException("SQLException: " + ex.getMessage());
        }
    }

    private void checkConversationExsists(int user, int friend) throws InternalServerErrorException, NoConversationFoundException {
        try {
            String sql = "SELECT count(*) as count FROM chatter.friend WHERE (user = ? AND friend = ?) OR (user = ? AND friend = ?);";
            PreparedStatement statement = MySQLConnection.prepareStatement(sql);
            statement.setInt(1, user);
            statement.setInt(2, friend);
            statement.setInt(3, friend);
            statement.setInt(4, user);
            ResultSet rs = statement.executeQuery();
            rs.first();
            if (rs.getInt("count") != 2) {
                throw new NoConversationFoundException("Die Benutzer sind keine Freunde!");
            }
        } catch (SQLException ex) {
            throw new InternalServerErrorException("SQLException: " + ex.getMessage());
        }
    }

    private void checkFriendsExists(int user, int friend) throws UserAreAlreadyFriendsException, InternalServerErrorException {
        try {
            String sql = "SELECT count(*) as count FROM chatter.friend WHERE (user = ? AND friend = ?) OR (user = ? AND friend = ?);";
            PreparedStatement statement = MySQLConnection.prepareStatement(sql);
            statement.setInt(1, user);
            statement.setInt(2, friend);
            statement.setInt(3, friend);
            statement.setInt(4, user);
            ResultSet rs = statement.executeQuery();
            rs.first();
            if (rs.getInt("count") > 0) {
                throw new UserAreAlreadyFriendsException("Die Benutzer sind schon Freunde!");
            }
        } catch (SQLException ex) {
            throw new InternalServerErrorException("SQLException: " + ex.getMessage());
        }
    }
    
    public void Test(){
        System.out.println(SessionHandler.hasSession(1));
        String sess = SessionHandler.generateSession(1);
        System.out.println(sess);
        try {
            System.out.println(SessionHandler.getUserID(sess));
        } catch (SessionDeniedException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            SessionHandler.checkSession(sess);
        } catch (SessionDeniedException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            SessionHandler.checkSession(sess + "123");
        } catch (SessionDeniedException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        SessionHandler.destroySession(sess);
        try {
            SessionHandler.checkSession(sess);
        } catch (SessionDeniedException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        SessionHandler.destroyAllSessions(1);
        System.out.println(SessionHandler.hasSession(1));
    }
}
