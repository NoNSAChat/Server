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

/**
 *
 * @author Pascal
 */
public class ChatServer extends UnicastRemoteObject implements ChatServerInterface {

    private MySQLConnection MySQLConnection;
    private CommonFunctions function;
    private SessionHandler SessionHandler;

    public ChatServer() throws RemoteException, Exception {
        function = new CommonFunctions();
        MySQLConnection = new MySQLConnection();
        SessionHandler = new SessionHandler();
        //MySQLConnection.createTestUser();
    }

    @Override
    public MyUser createUser(MyUser myUser, String password) throws UserAlreadyExsistsException, PasswordInvalidException, MailAlreadyInUseException, InternalServerErrorException {
        if (false) {
            throw new UserAlreadyExsistsException();
        } else if (false) {
            throw new InternalServerErrorException();
        } else if (false) {
            throw new PasswordInvalidException();
        } else if (false) {
            throw new MailAlreadyInUseException();
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
        return null;
    }

    @Override
    public void sendMessage(String sessionKey, Message message) throws SessionDeniedException, UserNotFoundException, InternalServerErrorException {
        //checke Session auf Gültigkeit
        //SessionHandler.checkSession(sessionKey);
        
        if (message.getUser() == SessionHandler.getUserID(sessionKey)){
            throw new InternalServerErrorException("Ein Benutzer kann sich selber keine Nachricht senden");
        }
        
        //Schreibe die Message in die Datenbank
        //MySQLConnection.createMessage(message.getMessage(), SessionHandler.getUserID(sessionKey), message.getUser());
        //Für Tests
        MySQLConnection.createMessage(message.getMessage(), 1, message.getUser());
        
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
        if (false){
            throw new InternalServerErrorException();
        }
    }
}