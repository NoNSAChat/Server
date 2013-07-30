/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Exception;

/**
 *
 * @author Pascal
 */
public class SessionDeniedException extends Exception {

    /**
     * Creates a new instance of
     * <code>SessionDeniedException</code> without detail message.
     */
    public SessionDeniedException() {
    }

    /**
     * Constructs an instance of
     * <code>SessionDeniedException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public SessionDeniedException(String msg) {
        super(msg);
    }
}
