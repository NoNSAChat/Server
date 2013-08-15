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
import java.security.PublicKey;
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

    @Override
    public MyUser createUser(MyUser myUser, String password) throws UserAlreadyExsistsException, PasswordInvalidException, MailAlreadyInUseException, InternalServerErrorException {
        String sql = "SELECT COUNT(*) FROM 'chatter'.'user' WHERE 'username' = ? and 'mail' = ?";
        try {
            PreparedStatement countusername = MySQLConnection.prepareStatement(sql);
            countusername.setString(1, myUser.getUsername());
            ResultSet res = countusername.executeQuery();
            int count = res.getInt(1);

        if (count > 0) {
            throw new UserAlreadyExsistsException();
        } else if (false) {
            throw new PasswordInvalidException();
        } else if (false) {
            throw new MailAlreadyInUseException();
        }
        }
        catch (SQLException ex) {
            throw new InternalServerErrorException();
        }
        return null;
    }

    @Override
    public MyUser editUser(String sessionKey, MyUser myUser) throws SessionDeniedException, InternalServerErrorException {
        if (false) {
            throw new SessionDeniedException();
        } else if (false) {
            throw new InternalServerErrorException();
        }
        return null;
    }

    @Override
    public byte[] editPassword(String sessionKey, String oldPassword, String newPassword) throws SessionDeniedException, PasswordInvalidException, InternalServerErrorException {
        if (false) {
            throw new PasswordInvalidException();
        } else if (false) {
            throw new InternalServerErrorException();
        } else if (false) {
            throw new SessionDeniedException();
        }
        return null;
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
        if (false) {
            throw new UserNotFoundException();
        } else if (false) {
            throw new InternalServerErrorException();
        } else if (false) {
            throw new WrongPasswordException();
        }
        throw new WrongPasswordException("Du bist blöd");
        //return null;
    }

    @Override
    public void sendMessage(String sessionKey, Message message) throws SessionDeniedException, UserNotFoundException, InternalServerErrorException {
        //checke Session auf Gültigkeit
        //SessionHandler.checkSession(sessionKey);

        //int sender = SessionHandler.getUserID(sessionKey);
        //Für Tests
        int sender = 1;
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
        //SessionHandler.checkSession(sessionKey);

        Message[] messages = null;
        int sender = SessionHandler.getUserID(sessionKey);
        int reciever = user;
        String sql = "";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            //Hole Nachrichten aus DB
            if (id != 0) {
                //Mit id Berücksichtigung
                sql = "SELECT * FROM chatter.message WHERE ((sender = ? AND reciever = ?) OR (sender = ? AND reciever = ?)) AND id >= ? ORDER BY id DESC LIMIT ?;";
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
                while (rs.next()) {
                    countResults++;
                }
            }

            //Lade das Ergebnis in ein Message Array
            messages = new Message[countResults];
            rs.first();
            for (int i = 0; i < messages.length; i++){
                messages[i] = new Message(rs.getInt("id"), rs.getInt("reciever"), rs.getBytes("message"));
                rs.next();
            }

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
    public Message[] getMessagesSinceID(String sessionKey, int user, int lastID) throws SessionDeniedException, NoConversationFoundException, InternalServerErrorException {
        if (false) {
            throw new NoConversationFoundException();
        } else if (false) {
            throw new InternalServerErrorException();
        } else if (false) {
            throw new SessionDeniedException();
        }
        return null;
    }

    @Override
    public Friend[] getFriendlist(String sessionKey) throws InternalServerErrorException {
        if (false) {
            throw new InternalServerErrorException();
        }
        return null;
    }

    @Override
    public void addFriend(String sessionKey, int friendID) throws SessionDeniedException, InternalServerErrorException {
        if (false) {
            throw new InternalServerErrorException();
        } else if (false) {
            throw new SessionDeniedException();
        }
    }

    @Override
    public PublicKey getPublicKey(String sessionKey, int userID) throws SessionDeniedException, UserNotFoundException, InternalServerErrorException {
        if (false) {
            throw new SessionDeniedException();
        } else if (false) {
            throw new UserNotFoundException();
        } else if (false) {
            throw new InternalServerErrorException();
        }
        return null;
    }

    @Override
    public void logOff(String sessionKey) throws InternalServerErrorException {
        if (false) {
            throw new InternalServerErrorException();
        }
    }
}
