/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Exception;

/**
 *
 * @author Pascal
 */
public class MailAlreadyInUseException extends Exception {

    /**
     * Creates a new instance of
     * <code>MailAlreadyInUseException</code> without detail message.
     */
    public MailAlreadyInUseException() {
    }

    /**
     * Constructs an instance of
     * <code>MailAlreadyInUseException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public MailAlreadyInUseException(String msg) {
        super(msg);
    }
}
