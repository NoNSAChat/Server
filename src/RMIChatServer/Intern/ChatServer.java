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
import RMIChatServer.Intern.MySQL.MySQLConnection;
import RMIChatServer.Server.ChatServerInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;
import java.sql.SQLFeatureNotSupportedException;

/**
 *
 * @author Pascal
 */
public class ChatServer extends UnicastRemoteObject implements ChatServerInterface {

    private MySQLConnection MySQLConnection;
    private CommonFunctions function;
    private SessionHandler SessionHandler;

    public ChatServer() throws RemoteException {
        function = new CommonFunctions();
        MySQLConnection = new MySQLConnection();
        SessionHandler = new SessionHandler();
    }

    
    
    /**
     * Liest einen Benutzer aus der DB.
     * Es wird keinne Session für ihn erzeugt.
     * @param userID Benutzer ID, der ausgelesen werden soll.
     * @return Ein MyBenutzer wird zurückgegeben, ohne einen "sessionKey".
     * @throws UserNotFoundException Wird geworfen, wenn der gesuchte Benutzer nicht existiert.
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     */
    private MyUser readMyUserFromDB(int userID) throws UserNotFoundException, InternalServerErrorException{
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MyUser createUser(MyUser myUser, String password) throws UserAlreadyExsistsException, PasswordInvalidException, MailAlreadyInUseException, InternalServerErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MyUser editUser(String sessionKey, MyUser myUser) throws SessionDeniedException, InternalServerErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] editPassword(String sessionKey, String oldPassword, String newPassword) throws PasswordInvalidException, InternalServerErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User[] searchFriend(String username, String forename, String lastname, String residence, String mail) throws InternalServerErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MyUser login(String username, String password) throws WrongPasswordException, UserNotFoundException, InternalServerErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendMessage(String sessionKey, Message message) throws SessionDeniedException, UserNotFoundException, InternalServerErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Message[] getLastMessages(String sessionKey, int user, int count, int id) throws SessionDeniedException, NoConversationFoundException, InternalServerErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Message[] getMessagesSinceID(String sessionKey, int user, int lastID) throws SessionDeniedException, NoConversationFoundException, InternalServerErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Friend[] getFriendlist(String sessionKey) throws InternalServerErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PublicKey getPublicKey(String sessionKey, int userID) throws UserNotFoundException, InternalServerErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

}