/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Intern;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pascal
 */
public class RMIChatServer {

    public static void main(String[] args) {
        try {
            //Registry starten
            Registry reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            System.out.println("Registry gestartet");
            
            //RMIServer starten
            reg.bind("rmichatserver", new ChatServer());
            System.out.println("Server bereit");
        } catch (RemoteException ex) {
            Logger.getLogger(RMIChatServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AlreadyBoundException ex) {
            Logger.getLogger(RMIChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
