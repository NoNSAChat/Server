/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Exception;

/**
 *
 * @author Pascal
 */
public class UserNotFoundException extends Exception {

    /**
     * Creates a new instance of
     * <code>UserNotFoundException</code> without detail message.
     */
    public UserNotFoundException() {
    }

    /**
     * Constructs an instance of
     * <code>UserNotFoundException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public UserNotFoundException(String msg) {
        super(msg);
    }
}
