/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Intern.Session;

/**
 *
 * @author Pascal
 */
public class Session {
    private int user;
    private long end;
    private String sessionKey;

    public Session(int user, String sessionKey) {
        this.user = user;
        this.sessionKey = sessionKey;
        //Zeit in Nanosekunden / 1 Millarde = Zeit in Sekunden
        //+ 5 Minuten Sessiongültigkeit
        this.end = (System.nanoTime() / 1000000000) + (5 * 60);
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
    
    
}
