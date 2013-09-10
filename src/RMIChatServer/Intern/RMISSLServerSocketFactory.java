package RMIChatServer.Intern;

import java.io.*;
import java.net.*;
import java.rmi.server.*;
import javax.net.ssl.*;
import java.security.KeyStore;
import javax.net.ssl.*;

public class RMISSLServerSocketFactory implements RMIServerSocketFactory {

    /*
     * Create one SSLServerSocketFactory, so we can reuse sessions
     * created by previous sessions of this SSLContext.
     */
    private SSLServerSocketFactory ssf = null;

    public RMISSLServerSocketFactory() throws Exception {
	try {
	    // set up key manager to do server authentication
	    SSLContext ctx;
	    KeyManagerFactory kmf;
	    KeyStore ks;
            
            BufferedReader br = new BufferedReader(new FileReader(new File("src/RMIChatServer/Password/ssl.pwd")));
	    char[] passphrase = br.readLine().toCharArray();
            br.close();
	    ks = KeyStore.getInstance("JKS");
	    ks.load(new FileInputStream("keystore"), passphrase);

	    kmf = KeyManagerFactory.getInstance("SunX509");
	    kmf.init(ks, passphrase);

	    ctx = SSLContext.getInstance("TLS");
	    ctx.init(kmf.getKeyManagers(), null, null);

	    ssf = ctx.getServerSocketFactory();
	} catch (Exception e) {
	    e.printStackTrace();
	    throw e;
	}
    }

    public ServerSocket createServerSocket(int port) throws IOException {
	    return ssf.createServerSocket(port);
    }

    public int hashCode() {
	return getClass().hashCode();
    }

    public boolean equals(Object obj) {
	if (obj == this) {
	    return true;
	} else if (obj == null || getClass() != obj.getClass()) {
	    return false;
	}
	return true;
    }
}
