/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Exception;

/**
 *
 * @author Pascal
 */
public class UserAlreadyExsistsException extends Exception {

    /**
     * Creates a new instance of
     * <code>UserAlreadyExsistsException</code> without detail message.
     */
    public UserAlreadyExsistsException() {
    }

    /**
     * Constructs an instance of
     * <code>UserAlreadyExsistsException</code> with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public UserAlreadyExsistsException(String msg) {
        super(msg);
    }
}
