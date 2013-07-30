/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Benutzer;

/**
 *
 * @author Pascal
 */
public class DetailedFriend extends Friend {
    private String forename;
    private String lastname;
    private String residence;

    public DetailedFriend(String forename, String lastname, String residence, Boolean newMessage, Boolean isOnline, int id, String username) {
        super(newMessage, isOnline, id, username);
        this.forename = forename;
        this.lastname = lastname;
        this.residence = residence;
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
    
    
}
