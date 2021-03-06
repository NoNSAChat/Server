/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Server;

import RMIChatServer.Benutzer.Friend;
import RMIChatServer.Benutzer.MyUser;
import RMIChatServer.Benutzer.User;
import RMIChatServer.Exception.ActivationKeyNotFoundException;
import RMIChatServer.Exception.InternalServerErrorException;
import RMIChatServer.Exception.MailAlreadyInUseException;
import RMIChatServer.Exception.NoConversationFoundException;
import RMIChatServer.Exception.PasswordInvalidException;
import RMIChatServer.Exception.SessionDeniedException;
import RMIChatServer.Exception.UserAlreadyExsistsException;
import RMIChatServer.Exception.UserAreAlreadyFriendsException;
import RMIChatServer.Exception.UserNotActivatedException;
import RMIChatServer.Exception.UserNotFoundException;
import RMIChatServer.Exception.WrongPasswordException;
import RMIChatServer.Message.Message;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Pascal
 */
public interface ChatServerInterface extends Remote {
    
    /**
     * Legt einen komplett neuen Benutzer an.
     * Die Keys für den Benutzer werden auf dem Server generiert.
     * @param myUser Vollständig generierter MyUser ohne ID und encyrptedPrivyteKey
     * @return Ein MyUser wird zurückgegeben
     * @throws UserAlreadyExsistsException Wird geworfen, wenn ein Benutzer mit diesem Benutzernamen schon existiert.
     * @throws PasswordInvalidException Wird geworfen, wenn das Passwort nicht mit den Rcihtlinien übereinstimmt.
     * @throws MailAlreadyInUseException Wird geworfen, wenn ein Benutzer mit dieser E-Mail Adresse schon existiert.
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglic
     */
    public MyUser createUser(MyUser myUser, String password) throws UserAlreadyExsistsException, PasswordInvalidException, MailAlreadyInUseException, InternalServerErrorException, RemoteException;
    
    /**
     * Editiert einen vorhandenen Benutzer.
     * @param sessionKey SessionKey um sich zu authentifizieren.
     * @param myUser Ein generiert MyUser, die Attribute "id", "username", "encyptedPrivateKey", "sessionKey" werden ignoert und somit nicht editiert.
     * @return Gibt einen vollständig generierten MyUser zurück. Alle Attribute enthalten aktuelle Werte.
     * @throws SessionDeniedException Wird geworfen, wenn die genannte Session nicht gültig ist.
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     * @throws UserAlreadyExsistsException Wird geworfen, wenn ein Benutzer mit diesem Benutzernamen schon existiert.
     * @throws MailAlreadyInUseException Wird geworfen, wenn ein Benutzer mit dieser E-Mail Adresse schon existiert.
     */
    public MyUser editUser(String sessionKey, MyUser myUser) throws SessionDeniedException, InternalServerErrorException, UserAlreadyExsistsException, MailAlreadyInUseException, RemoteException;
    
    /**
     * Ändert das Passwort eines Benutzers.
     * Damit ändert sich in der DB auch der verschlüsselte PrivateKey.
     * @param sessionKey SessionKey um sich zu authentifizieren.
     * @param oldPassword Das alte Passwort.
     * @param newPassword Das neue Passwort.
     * @return Es wird der neu verschlüsselte PrivateKey zurück gegeben.
     * @throws WrongPasswordException Wird geworfen, falls das Passwort nicht übereinstimmt.
     * @throws PasswordInvalidException Wird geworfen, wenn das Passwort nicht mit den Rcihtlinien übereinstimmt.
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     */
    public byte[] editPassword (String sessionKey, String oldPassword, String newPassword) throws SessionDeniedException, PasswordInvalidException, WrongPasswordException, InternalServerErrorException, RemoteException;
    
    /**
     * Sucht nach neuen Freunden.
     * @param username Attribut nach dem gesucht wird, kann null sein.
     * @param forename Attribut nach dem gesucht wird, kann null sein.
     * @param lastname Attribut nach dem gesucht wird, kann null sein.
     * @param residence Attribut nach dem gesucht wird, kann null sein.
     * @param mail Attribut nach dem gesucht wird, kann null sein.
     * @return Gibt ein User Array zurück.
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     */
    public User[] searchFriend (String username, String forename, String lastname, String residence, String mail) throws InternalServerErrorException, RemoteException;
    
    /**
     * Erzeugt eine Freundschaft und legt die dazu passenden Keys an.
     * @param sessionKey SessionKey um sich zu authentifizieren.
     * @param friendID ID des Freundes-Benutzers.
     * @throws UserAreAlreadyFriendsException Wird geworfen, wenn die Benutzer schon Freunde sind.
     * @throws UserNotFoundException Wird geworfen, wenn ein Benutzer nicht gefunden werden kann.
     * @throws SessionDeniedException Wird geworfen, wenn die genannte Session nicht gültig ist.
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     */
    public void addFriend (String sessionKey, int friendID) throws UserAreAlreadyFriendsException, UserNotFoundException, SessionDeniedException, InternalServerErrorException, RemoteException;
    
    
    /**
     * Generiert eine Session auf dem Server, um den Zugriff auf den Server zu ermöglichen.
     * @param username Benutzername des Benutzers
     * @param password Passwort des Benutzers
     * @return Ein MyUser wird zurückgegeben.
     * @throws WrongPasswordException Wird geworfen, falls das Passwort nicht übereinstimmt.
     * @throws UserNotFoundException Wird geworfen, falls ein Benutzer nicht gefunden wurde.
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     */
    public MyUser login(String username, String password) throws UserNotActivatedException, WrongPasswordException, UserNotFoundException, InternalServerErrorException, RemoteException;
    
    /**
     * Sendet eine Nachricht an einen Benutzer.
     * @param message Eine komplett generierte Message ohne ausgefüllte ID.
     * @throws SessionDeniedException Wird geworfen, wenn die genannte Session nicht gültig ist.
     * @throws UserNotFoundException Wird geworfen, falls ein Benutzer nicht gefunden wurde.
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     */
    public void sendMessage(String sessionKey, Message message) throws SessionDeniedException, UserNotFoundException, InternalServerErrorException, RemoteException;
    
    /**
     * Gibt alle gesendete und empfangene Nachrichten einer Konversation seit der angegebenen ID zurück.
     * Die erste Nachricht im Array hat die höchste und somit neuste ID.
     * Es werden je nach count definiert Nachrichten zurück gegeben.
     * @param sessionKey SessionKey um sich zu authentifizieren.
     * @param user Benutzer ID, dessen Konversation geladen werden soll.
     * @param count Anzahl der Nachrichten
     * @param id Nachrichten dürfen keine KLEINERE ID besitzen, Wert "0" wenn die letzten Nachrichten geladen werden sollen.
     * @return Gibt ein Array an Messages zurück. Hierbei ist bei der Message das Attribut "user" entweder die eigene ID oder die ID des Konversationspartners.
     * @throws SessionDeniedException Wird geworfen, wenn die genannte Session nicht gültig ist.
     * @throws NoConversationFoundException Wird geworfen, wenn keine Konversation mit einem Benutzer gefunden wurde (dürfte eigentlich nicht auftreten).
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     */
    public Message[] getLastMessages(String sessionKey, int user, int count, int id) throws SessionDeniedException, NoConversationFoundException, InternalServerErrorException, RemoteException;
    
    /**
     * Gibt alle Nachrichten zurück, die empfangen oder gesendet wurden bis zu der angegeben ID.
     * Die erste Nachricht in dem Array hat die höchste id.
     * @param sessionKey SessionKey um sich zu authentifizieren.
     * @param user Benutzer ID, dessen Konversation geladen werden soll.
     * @param lastID Nachrichten dürfen keine HÖHERE ID besitzen
     * @param count Anzahl der Nachrichten
     * @return Gibt ein Array an Messages zurück. Hierbei ist bei der Message das Attribut "user" entweder die eigene ID oder die ID des Konversationspartners.
     * @throws SessionDeniedException Wird geworfen, wenn die genannte Session nicht gültig ist.
     * @throws NoConversationFoundException Wird geworfen, wenn keine Konversation mit einem Benutzer gefunden wurde (dürfte eigentlich nicht auftreten).
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     */
    public Message[] getMessagesSinceID (String sessionKey, int user, int lastID, int count) throws SessionDeniedException, NoConversationFoundException, InternalServerErrorException, RemoteException;
    
    /**
     * Gibt alle Freunde eines Benutzer zurück.
     * @param sessionKey SessionKey um sich zu authentifizieren.
     * @return Gibt alle Freunde eines Benutzer zurück.
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     */
    public Friend[] getFriendlist (String sessionKey) throws SessionDeniedException, InternalServerErrorException, RemoteException;
    
    /**
     * Gibt einen verschlüsselten Key für eine Unterhaltung zurück.
     * Mit dem private Key des Benutzers kann der Key entschlüsselt werden.
     * @param sessionKey SessionKey um sich zu authentifizieren.
     * @param userID Benutzer ID, für die Freundschaft, nach der der Key gesucht werden soll.
     * @return Gibt einen verschlüsselten Key für eine Unterhaltung zurück.
     * @throws SessionDeniedException Wird geworfen, wenn die genannte Session nicht gültig ist.
     * @throws NoConversationFoundException Wird geworfen, wenn die Benutzer keine Freunde sind.
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     */
    public byte[] getConversationKey (String sessionKey, int userID) throws SessionDeniedException, NoConversationFoundException, InternalServerErrorException, RemoteException;
    
    /**
     * Zerstört die angegebene Session.
     * @param sessionKey SessionKey um sich zu authentifizieren.
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     */
    public void logOff (String sessionKey) throws InternalServerErrorException, RemoteException;
    
    /**
     * Löscht einen Benutzer, seine Freunde und Nachrichten.
     * @param sessionKey Session KEy der Anmeldung
     * @param password Passwort des Benutzers
     * @throws WrongPasswordException Wird bei einem falschen Passwort geworfen
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     * @throws SessionDeniedException Wird geworfen, wenn eine Session abgelehnt wird.
     */
    public void deleteUser (String sessionKey, String password) throws UserNotFoundException, WrongPasswordException, InternalServerErrorException, SessionDeniedException, RemoteException;
    
    /**
     * Löscht eine Freundschaft und deren Nachrichten.
     * @param sessionKey Session Key des Benutzers.
     * @param user User ID des Freundes
     * @throws SessionDeniedException
     * @throws InternalServerErrorException
     * @throws RemoteException
     */
    public void deleteFriendship(String sessionKey, int user) throws SessionDeniedException, InternalServerErrorException, RemoteException;
    
    /**
     * Aktiviert einen bestehenden Benutzer.
     * @param key Key, der mit der E-Mail versandt wurde.
     * @throws ActivationKeyNotFoundException Wird geworfen, wenn der angegebene Key nicht gefunden werden kann.
     * @throws InternalServerErrorException
     * @throws RemoteException
     */
    public void activateUser(String key) throws ActivationKeyNotFoundException, InternalServerErrorException, RemoteException;
}