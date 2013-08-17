/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Intern.Session;

import RMIChatServer.Exception.SessionDeniedException;
import java.util.ArrayList;

/**
 *
 * @author Pascal
 */
public class SessionHandler {

    private ArrayList session;

    /**
     * Erstellt einen neuen SessionHandler. Er ist für die Verwaltung von
     * Session zuständig.
     */
    public SessionHandler() {
        this.session = new ArrayList<Session>();
    }

    /**
     * Die Funktion überprüft einen Session Key auf Gültigkeit.
     *
     * @param sessionKey String, der die Session identifiziert
     * @return true, falls die Session gültig ist, ansonsten false
     */
    public void checkSession(String sessionKey) throws SessionDeniedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Erzeugt eine neue Session.
     *
     * @param userID Die Benutzer ID, für den die Session angelegt werden soll.
     * @return Einen String, der die Session identifiziert.
     */
    public String generateSession(int userID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Eine bestimmte Session wird gelöscht.
     *
     * @param sessionKey Identifikation für die Session, die gelöscht werden
     * soll.
     */
    public void destroySession(String sessionKey) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Löscht alle Session für einen Benutzer.
     *
     * @param userID ID des Benutzers, für den alle Session gelöscht werden
     * sollen.
     */
    public void destroyAllSessions(int userID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Gibt die ID des Benutzers der Session zurück.
     *
     * @param sessionKey SessionKey des Benutzers
     * @return ID des Benutzers
     * @throws SessionDeniedException
     */
    public int getUserID(String sessionKey) throws SessionDeniedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
