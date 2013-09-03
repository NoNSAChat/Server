/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Message;

import java.io.Serializable;
import java.sql.Time;

/**
 *
 * @author Pascal
 */
public class Message implements Serializable {

    private int id;
    private int user;
    private byte[] message;
    private Time time;

    public Message(int reciever, byte[] message) {
        this.user = reciever;
        this.message = message;
    }

    public Message(int id, int user, byte[] message, Time time) {
        this.id = id;
        this.user = user;
        this.message = message;
        this.time = time;
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

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
    
}
