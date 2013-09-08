/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Exception;

/**
 *
 * @author admin
 */
public class ActivationKeyNotFoundException extends Exception {

    /**
     * Creates a new instance of
     * <code>ActivationKeyNotFoundException</code> without detail message.
     */
    public ActivationKeyNotFoundException() {
    }

    /**
     * Constructs an instance of
     * <code>ActivationKeyNotFoundException</code> with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public ActivationKeyNotFoundException(String msg) {
        super(msg);
    }
}
