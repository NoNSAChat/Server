/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Intern.Session;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class SessionControllerThread extends Thread {

    private ArrayList<Session> session;

    public SessionControllerThread(ArrayList<Session> session) {
        this.session = session;
    }

    @Override
    public void run() {
        long now;
        while (!interrupted()) {
            try {
                Thread.sleep(1000 * 60);
            } catch (InterruptedException ex) {
                Logger.getLogger(SessionControllerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            now = System.nanoTime() / 1000000000;
            for (Session currentSession : session) {
                if (currentSession.getEnd() < now) {
                    session.remove(currentSession);
                }
            }
        }
    }
}
