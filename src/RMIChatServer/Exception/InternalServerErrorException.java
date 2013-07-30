/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Exception;

/**
 *
 * @author Pascal
 */
public class InternalServerErrorException extends Exception {

    /**
     * Creates a new instance of
     * <code>InternerServerFehlerException</code> without detail message.
     */
    public InternalServerErrorException() {
    }

    /**
     * Constructs an instance of
     * <code>InternerServerFehlerException</code> with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public InternalServerErrorException(String msg) {
        super(msg);
    }
}
