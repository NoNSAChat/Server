/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Benutzer;

/**
 *
 * @author Pascal
 */
public class Friend extends User {

    private Boolean newMessage;
    private Boolean isOnline;

    public Friend(Boolean newMessage, Boolean isOnline, int id, String username) {
        super(id, username);
        this.newMessage = newMessage;
        this.isOnline = isOnline;
    }

    public Boolean getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(Boolean newMessage) {
        this.newMessage = newMessage;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }
}
