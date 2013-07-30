/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Message;

/**
 *
 * @author Pascal
 */
public class Message {
    private int id;
    private int user;
    private byte[] message;

    public Message(int reciever, byte[] message) {
        this.user = reciever;
        this.message = message;
    }

    public Message(int id, int user, byte[] message) {
        this.id = id;
        this.user = user;
        this.message = message;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
}
