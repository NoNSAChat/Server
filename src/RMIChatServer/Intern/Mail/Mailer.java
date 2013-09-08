/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Intern.Mail;

import RMIChatServer.Exception.InternalServerErrorException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author admin
 */
public class Mailer {

    private String host = "localhost";
    private int port = 25;
    private Properties props;

    public Mailer() {
        props = new Properties();
        props.put("mail.smtp.host", host);
    }

    public void sendKey(String reciever, String key) throws InternalServerErrorException {
        try {
            Session session = Session.getDefaultInstance(props);
            Message msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress("NoNSAChat@spr19.dhbw-heidenheim.de");
            msg.setFrom(addressFrom);
            InternetAddress addressTo = new InternetAddress(reciever);
            msg.setRecipient(Message.RecipientType.TO, addressTo);
            msg.setSubject("Ihre Registrierung für NoNSAChat");
            String message = "Hallo,<br />dein Aktivierungscode lautet:<br />" + key + "<br />Danke f&uuml;r deine Registrierung!<br /><br />Mit freundlichen Gr&uuml;&szlig;en<br />Dein NoNSAChat-Team";
            msg.setContent(message, "text/html");
            Transport.send(msg);
        } catch (AddressException ex) {
            throw new InternalServerErrorException("Mail konnte nicht gesendet werden!");
        } catch (MessagingException ex) {
            Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException("MessagingException: " + ex.getMessage());
        }
    }
}
