/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Exception;

/**
 *
 * @author Pascal
 */
public class NoConversationFoundException extends Exception {

    /**
     * Creates a new instance of
     * <code>NoConversationFoundException</code> without detail message.
     */
    public NoConversationFoundException() {
    }

    /**
     * Constructs an instance of
     * <code>NoConversationFoundException</code> with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public NoConversationFoundException(String msg) {
        super(msg);
    }
}
