/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Exception;

/**
 *
 * @author admin
 */
public class UserAreAlreadyFriendsException extends Exception {

    /**
     * Creates a new instance of
     * <code>UserAreAlreadyFriendsException</code> without detail message.
     */
    public UserAreAlreadyFriendsException() {
    }

    /**
     * Constructs an instance of
     * <code>UserAreAlreadyFriendsException</code> with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public UserAreAlreadyFriendsException(String msg) {
        super(msg);
    }
}
