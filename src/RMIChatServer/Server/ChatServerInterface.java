/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Server;

import RMIChatServer.Benutzer.Friend;
import RMIChatServer.Benutzer.MyUser;
import RMIChatServer.Benutzer.User;
import RMIChatServer.Exception.InternalServerErrorException;
import RMIChatServer.Exception.MailAlreadyInUseException;
import RMIChatServer.Exception.NoConversationFoundException;
import RMIChatServer.Exception.PasswordInvalidException;
import RMIChatServer.Exception.SessionDeniedException;
import RMIChatServer.Exception.UserAlreadyExsistsException;
import RMIChatServer.Exception.UserNotFoundException;
import RMIChatServer.Exception.WrongPasswordException;
import RMIChatServer.Message.Message;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;

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
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     */
    public MyUser createUser(MyUser myUser, String password) throws UserAlreadyExsistsException, PasswordInvalidException, MailAlreadyInUseException, InternalServerErrorException, RemoteException;
    
    /**
     * Editiert einen vorhandenen Benutzer.
     * @param sessionKey SessionKey um sich zu authentifizieren.
     * @param myUser Ein generiert MyUser, die Attribute "id", "username", "encyptedPrivateKey", "sessionKey" werden ignoert und somit nicht editiert.
     * @return Gibt einen vollständig generierten MyUser zurück. Alle Attribute enthalten aktuelle Werte.
     * @throws Wird geworfen, wenn die genannte Session nicht gültig ist.
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     */
    public MyUser editUser(String sessionKey, MyUser myUser) throws SessionDeniedException, InternalServerErrorException, RemoteException;
    
    /**
     * Ändert das Passwort eines Benutzers.
     * Damit ändert sich in der DB auch der verschlüsselte PrivateKey.
     * @param sessionKey SessionKey um sich zu authentifizieren.
     * @param oldPassword Das alte Passwort.
     * @param newPassword Das neue Passwort.
     * @return Es wird der neu verschlüsselte PrivateKey zurück gegeben.
     * @throws PasswordInvalidException Wird geworfen, wenn das Passwort nicht mit den Rcihtlinien übereinstimmt.
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     */
    public byte[] editPassword (String sessionKey, String oldPassword, String newPassword) throws SessionDeniedException, PasswordInvalidException, InternalServerErrorException, RemoteException;
    
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
     * @throws SessionDeniedException Wird geworfen, wenn die genannte Session nicht gültig ist.
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     */
    public void addFriend (String sessionKey, int friendID) throws SessionDeniedException, InternalServerErrorException, RemoteException;
    
    
    /**
     * Generiert eine Session auf dem Server, um den Zugriff auf den Server zu ermöglichen.
     * @param username Benutzername des Benutzers
     * @param password Passwort des Benutzers
     * @return Ein MyUser wird zurückgegeben.
     * @throws WrongPasswordException Wird geworfen, falls das Passwort nicht übereinstimmt.
     * @throws UserNotFoundException Wird geworfen, falls ein Benutzer nicht gefunden wurde.
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     */
    public MyUser login(String username, String password) throws WrongPasswordException, UserNotFoundException, InternalServerErrorException, RemoteException;
    
    /**
     * Sendet eine Nachricht an einen Benutzer.
     * @param message Eine komplett generierte Message ohne ausgefüllte ID.
     * @throws SessionDeniedException Wird geworfen, wenn die genannte Session nicht gültig ist.
     * @throws UserNotFoundException Wird geworfen, falls ein Benutzer nicht gefunden wurde.
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     */
    public void sendMessage(String sessionKey, Message message) throws SessionDeniedException, UserNotFoundException, InternalServerErrorException, RemoteException;
    
    /**
     * Gibt alle gesendete und empfangene Nachrichten einer Konversation zurück.
     * Es werden je nach count definiert Nachrichten zurück gegeben.
     * @param sessionKey SessionKey um sich zu authentifizieren.
     * @param user Benutzer ID, dessen Konversation geladen werden soll.
     * @param count Anzahl der Nachrichten
     * @param id Nachrichten dürfen keine HÖHERE ID besitzen, null wenn die letzten Nachrichten geladen werden sollen.
     * @return Gibt ein Array an Messages zurück. Hierbei ist bei der Message das Attribut "user" entweder die eigene ID oder die ID des Konversationspartners.
     * @throws SessionDeniedException Wird geworfen, wenn die genannte Session nicht gültig ist.
     * @throws NoConversationFoundException Wird geworfen, wenn keine Konversation mit einem Benutzer gefunden wurde (dürfte eigentlich nicht auftreten).
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     */
    public Message[] getLastMessages(String sessionKey, int user, int count, int id) throws SessionDeniedException, NoConversationFoundException, InternalServerErrorException, RemoteException;
    
    /**
     * Gibt alle Nachrichten zurück, die empfangen oder gesendet wurden seit der angegeben ID.
     * @param sessionKey SessionKey um sich zu authentifizieren.
     * @param user Benutzer ID, dessen Konversation geladen werden soll.
     * @param lastID Nachrichten dürfen keine KLEINERE ID besitzen
     * @return Gibt ein Array an Messages zurück. Hierbei ist bei der Message das Attribut "user" entweder die eigene ID oder die ID des Konversationspartners.
     * @throws SessionDeniedException Wird geworfen, wenn die genannte Session nicht gültig ist.
     * @throws NoConversationFoundException Wird geworfen, wenn keine Konversation mit einem Benutzer gefunden wurde (dürfte eigentlich nicht auftreten).
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     */
    public Message[] getMessagesSinceID (String sessionKey, int user, int lastID) throws SessionDeniedException, NoConversationFoundException, InternalServerErrorException, RemoteException;
    
    /**
     * Gibt alle Freunde eines Benutzer zurück.
     * @param sessionKey SessionKey um sich zu authentifizieren.
     * @return Gibt alle Freunde eines Benutzer zurück.
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     */
    public Friend[] getFriendlist (String sessionKey) throws SessionDeniedException, InternalServerErrorException, RemoteException;
    
    /**
     * Gibt einen PublicKey für einen Benutzer zurück.
     * @param sessionKey SessionKey um sich zu authentifizieren.
     * @param userID Benutzer ID, für den der PublicKey gesucht werden soll.
     * @return Gibt einen PublicKey für einen Benutzer zurück.
     * @throws UserNotFoundException Gibt alle Freunde eines Benutzer zurück.
     * @throws InternalServerErrorException Wird geworfen, wenn ein Fehler auftritt, der nicht auftreten dürfte. Keine Fehlerbehandlung clientseitig möglich.
     */
    public PublicKey getPublicKey (String sessionKey, int userID) throws SessionDeniedException, UserNotFoundException, InternalServerErrorException, RemoteException;
 }