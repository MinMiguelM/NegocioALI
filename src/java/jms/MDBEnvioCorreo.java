/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jms;

import entities.Usuario;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import logica.EnvioCorreoBean;

/**
 *
 * @author SALABD
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "queueMail"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class MDBEnvioCorreo implements MessageListener {
    
    @EJB
    private EnvioCorreoBean correoBean;
    
    public MDBEnvioCorreo() {
    }
    
    @Override
    public void onMessage(Message message) {
        System.out.println("    hooooooooooola");
        try{
            if (message instanceof MapMessage){
                MapMessage mm = (MapMessage) message;
                correoBean.envioCorreo(mm.getString("Usuario"), mm.getInt("NumTransaccion"), mm.getInt("Valor"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
