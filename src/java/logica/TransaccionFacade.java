/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import entities.Plato;
import entities.Transaccion;
import entities.Usuario;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author SALABD
 */
@Stateless
public class TransaccionFacade extends AbstractFacade<Transaccion> implements logica.TransaccionFacadeRemote {

    @Resource(mappedName = "jms/topicContabilidad")
    private Topic topicContabilidad;

    @Inject
    @JMSConnectionFactory("jms/topicContabilidadFactory")
    private JMSContext context;

    @PersistenceContext(unitName = "NegocioALIPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TransaccionFacade() {
        super(Transaccion.class);
    }
 
    public int pago(Usuario user, List<Plato> platos){
        int total = 0;
        Transaccion tx = new Transaccion();
        Date date = new Date();
        for (Plato plato : platos) {
            total += plato.getPrecio().intValue();
        }
        tx.setCedulaUsuario(user);
        tx.setFecha(date);
        tx.setPlatoList(platos);
        tx.setValor(BigInteger.valueOf(total));
        
        sendJMSMessageToTopicContabilidad(tx.toString());
        return 0;
    }

    private void sendJMSMessageToTopicContabilidad(String messageData) {
        context.createProducer().send(topicContabilidad, messageData);
    }
}
