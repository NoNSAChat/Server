/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Intern.Session;

import RMIChatServer.Exception.SessionDeniedException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pascal
 */
public class SessionHandler {

    private ArrayList<Session> session;

    /**
     * Erstellt einen neuen SessionHandler. Er ist für die Verwaltung von
     * Session zuständig.
     */
    public SessionHandler() {
        this.session = new ArrayList<Session>();
        Thread sessionController = new SessionControllerThread(session);
        sessionController.start();
        //Für Tests
        session.add(new Session(12, "user12"));
        session.add(new Session(13, "user13"));
    }

    /**
     * Die Funktion überprüft einen Session Key auf Gültigkeit.
     *
     * @param sessionKey String, der die Session identifiziert
     * @return true, falls die Session gültig ist, ansonsten false
     */
    public void checkSession(String sessionKey) throws SessionDeniedException {
        if (containsSession(sessionKey)) {
        } else {
            Session currentSession = getSession(sessionKey);
            checkSessionTime(currentSession);
            resetTime(currentSession);
        }
    }

    /**
     * Erzeugt eine neue Session.
     *
     * @param userID Die Benutzer ID, für den die Session angelegt werden soll.
     * @return Einen String, der die Session identifiziert.
     */
    public String generateSession(int userID) {
        SecureRandom random = new SecureRandom();
        String sessionKey = new BigInteger(130, random).toString(32);
        session.add(new Session(userID, sessionKey));
        return sessionKey;
    }

    /**
     * Eine bestimmte Session wird gelöscht.
     *
     * @param sessionKey Identifikation für die Session, die gelöscht werden
     * soll.
     */
    public void destroySession(String sessionKey) {
        try {
            session.remove(getSession(sessionKey));
        } catch (SessionDeniedException ex) {
            Logger.getLogger(SessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Löscht alle Session für einen Benutzer.
     *
     * @param userID ID des Benutzers, für den alle Session gelöscht werden
     * sollen.
     */
    public void destroyAllSessions(int userID) {
        for (Session currentSession : session) {
            if (currentSession.getUser() == userID) {
                try {
                    session.remove(getSession(currentSession.getSessionKey()));
                } catch (SessionDeniedException ex) {
                    Logger.getLogger(SessionHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Gibt die ID des Benutzers der Session zurück.
     *
     * @param sessionKey SessionKey des Benutzers
     * @return ID des Benutzers
     * @throws SessionDeniedException
     */
    public int getUserID(String sessionKey) throws SessionDeniedException {
        Session currentSession = getSession(sessionKey);
        checkSessionTime(currentSession);
        resetTime(currentSession);
        return currentSession.getUser();
    }

    /**
     * Untersucht, ob der entsprechende User eine Session hat.
     *
     * @param userID ID des Benutzers
     * @return true, wenn eine Session vorhanden ist, ansonsten false
     */
    public Boolean hasSession(int userID) {
        for (Session currentSession : session) {
            if (currentSession.getUser() == userID) {
                try {
                    checkSessionTime(currentSession);
                } catch (SessionDeniedException ex) {
                    Logger.getLogger(SessionHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                resetTime(currentSession);
                return true;
            }
        }
        return false;
    }

    private Boolean containsSession(String sessionKey) {
        for (Session currentSession : session) {
            if (currentSession.getSessionKey().equals(sessionKey)) {
                return true;
            }
        }
        return false;
    }

    private Session getSession(String sessionKey) throws SessionDeniedException {
        for (Session currentSession : session) {
            if (currentSession.getSessionKey().equals(sessionKey)) {
                return currentSession;
            }
        }
        throw new SessionDeniedException("Session konnte nicht gefunden werden!");
    }

    private void resetTime(Session sessionToReset) {
        sessionToReset.setEnd((System.nanoTime() / 1000000000) + (60 * 5));
    }

    private void resetTime(String sessionKey) {
        for (Session currentSession : session) {
            if (currentSession.getSessionKey().equals(sessionKey)) {
                currentSession.setEnd((System.nanoTime() / 1000000000) + (60 * 5));
            }
        }
    }

    private void checkSessionTime(Session currentSession) throws SessionDeniedException {
        if (currentSession.getEnd() < (System.nanoTime() / 1000000000)) {
            session.remove(currentSession);
            throw new SessionDeniedException("Die Session ist abgelaufen");
        }
    }
}
