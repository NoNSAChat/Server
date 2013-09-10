/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Intern;

import RMIChatServer.CommonFunctions.RMISSLClientSocketFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.rmi.AlreadyBoundException;
import java.rmi.RMISecurityManager;
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
        // Create and install a security manager
	if (System.getSecurityManager() == null) {
	    System.setSecurityManager(new RMISecurityManager());
	}      
        
        try {
                
            BufferedReader br = new BufferedReader(new FileReader(new File("src/RMIChatServer/Password/ssl.pwd")));
            System.setProperty("javax.net.ssl.keyStore", "keystore"); 
            System.setProperty("javax.net.ssl.keyStorePassword",br.readLine());
            System.setProperty("javax.net.ssl.trustStore", "trustsore");
            System.setProperty("javax.net.ssl.trustStorePassword",br.readLine()); 
            
            br.close();
            // Create SSL-based registry
	    Registry reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT,
		new RMISSLClientSocketFactory(),
		new RMISSLServerSocketFactory());
            System.out.println("Registry gestartet");
            
            //RMIServer starten
            ChatServer cs = new ChatServer();
            reg.bind("rmichatserver", cs);
            System.out.println("Server bereit");
//            cs.Test();
        } catch (RemoteException ex) {
            Logger.getLogger(RMIChatServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AlreadyBoundException ex) {
            Logger.getLogger(RMIChatServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(RMIChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
