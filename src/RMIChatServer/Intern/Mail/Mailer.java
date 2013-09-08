/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMIChatServer.Intern.Mail;

import RMIChatServer.Exception.InternalServerErrorException;
import java.util.Properties;
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
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.host", "mail.java-tutor.com");
    }

    public void senKey(String reciever, String key) throws InternalServerErrorException {
        try {
            Session session = Session.getDefaultInstance(props);
            Message msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress("NoNSAChat");
            msg.setFrom(addressFrom);
            InternetAddress addressTo = new InternetAddress(reciever);
            msg.setRecipient(Message.RecipientType.TO, addressTo);
            msg.setSubject("Ihre Registrierung für NoNSAChat");
            String message = "Hallo,\n\ndein Aktivierungscode lautet:\n" + key + "\nDanke für deine Registrierung!\n\nMit freundlichen Grüßen\nDein NoNSAChat-Team";
            msg.setContent(message, "text/plain");
            Transport.send(msg);
        } catch (AddressException ex) {
            throw new InternalServerErrorException("Mail konnte nicht gesendet werden!");
        } catch (MessagingException ex) {
            throw new InternalServerErrorException("MessagingException: " + ex.getMessage());
        }
    }
}
