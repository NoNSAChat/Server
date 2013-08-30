/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Benutzer;

public class MyUser extends User {

    private byte[] encyptedPrivateKey;
    private String forename;
    private String lastname;
    private String residence;
    private String mail;
    private String sessionKey;

    public MyUser(byte[] encyptedPrivateKey, String forename, String lastname, String residence, String mail, String sessionKey, int id, String username) {
        super(id, username);
        this.encyptedPrivateKey = encyptedPrivateKey;
        this.forename = forename;
        this.lastname = lastname;
        this.residence = residence;
        this.mail = mail;
        this.sessionKey = sessionKey;
    }

    public MyUser(String forename, String lastname, String residence, String mail, String sessionKey, String username) {
        super(username);
        this.forename = forename;
        this.lastname = lastname;
        this.residence = residence;
        this.mail = mail;
        this.sessionKey = sessionKey;
    }

    public MyUser(byte[] encyptedPrivateKey, String forename, String residence, String mail, int id, String username) {
        super(id, username);
        this.encyptedPrivateKey = encyptedPrivateKey;
        this.forename = forename;
        this.residence = residence;
        this.mail = mail;
    }

    public MyUser(String forename, String lastname, String residence, String mail, String username) {
        super(username);
        this.forename = forename;
        this.lastname = lastname;
        this.residence = residence;
        this.mail = mail;
    }
    
    

    public byte[] getEncyptedPrivateKey() {
        return encyptedPrivateKey;
    }

    public void setEncyptedPrivateKey(byte[] encyptedPrivateKey) {
        this.encyptedPrivateKey = encyptedPrivateKey;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    
}
