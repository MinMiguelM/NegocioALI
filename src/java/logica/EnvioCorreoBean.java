/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import entities.Transaccion;
import entities.Usuario;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author sala-a
 */
@Stateless
@LocalBean
public class EnvioCorreoBean {
    
    private Properties mailServerProperties;
    private Session getMailSession;
    private MimeMessage generateMailMessage;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    public void envioCorreo(String user, int numTransaccion, int valor){
        try {
            mailServerProperties = System.getProperties();
            mailServerProperties.put("mail.transport.protocol", "smtp");
            mailServerProperties.put("mail.smtp.port", "587");
            mailServerProperties.put("mail.smtp.auth", "true");
            mailServerProperties.put("mail.smtp.starttls.enable", "true");
            mailServerProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            
            // Step2
            getMailSession = Session.getDefaultInstance(mailServerProperties, null);
            //getMailSession.setDebug(true);
            generateMailMessage = new MimeMessage(getMailSession);
            generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(user));
            generateMailMessage.setSubject("Pago en ALI");
            String emailBody = "Estimado cliente, le queremos notificar que el costo total de su cuenta es: " + valor +
                    "<br> Con numero de transaccion: " + numTransaccion +
                    "<br><br> Atentamente, <br> ALI Admin";
            generateMailMessage.setContent(emailBody, "text/html");
            
            // Step3
            Transport transport = getMailSession.getTransport("smtp");
            transport.connect("smtp.gmail.com", "serviciosali.contacto@gmail.com", "123456789Pinkfloyd");
            transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
            transport.close();
        } catch (Exception ex) {
            Logger.getLogger(EnvioCorreoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
