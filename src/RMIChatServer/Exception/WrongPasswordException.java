/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Exception;

/**
 *
 * @author Pascal
 */
public class WrongPasswordException extends Exception {

    /**
     * Creates a new instance of
     * <code>PasswortFalsch</code> without detail message.
     */
    public WrongPasswordException() {
    }

    /**
     * Constructs an instance of
     * <code>PasswortFalsch</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public WrongPasswordException(String msg) {
        super(msg);
    }
}
