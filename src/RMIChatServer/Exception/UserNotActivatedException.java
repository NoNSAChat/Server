/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Exception;

/**
 *
 * @author admin
 */
public class UserNotActivatedException extends Exception {

    /**
     * Creates a new instance of
     * <code>UserNotActivatedException</code> without detail message.
     */
    public UserNotActivatedException() {
    }

    /**
     * Constructs an instance of
     * <code>UserNotActivatedException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public UserNotActivatedException(String msg) {
        super(msg);
    }
}
