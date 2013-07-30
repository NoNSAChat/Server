/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Exception;

/**
 *
 * @author Pascal
 */
public class PasswordInvalidException extends Exception {

    /**
     * Creates a new instance of
     * <code>PasswordInvalidException</code> without detail message.
     */
    public PasswordInvalidException() {
    }

    /**
     * Constructs an instance of
     * <code>PasswordInvalidException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public PasswordInvalidException(String msg) {
        super(msg);
    }
}
