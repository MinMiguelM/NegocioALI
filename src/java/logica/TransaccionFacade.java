/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import com.sun.xml.wss.util.DateUtils;
import entities.Plato;
import entities.Transaccion;
import entities.Usuario;
import integracion.DTOTransaccion;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author SALABD
 */
@Stateless
public class TransaccionFacade extends AbstractFacade<Transaccion> implements logica.TransaccionFacadeRemote, Serializable{

    @Resource(mappedName = "queueMail")
    private Queue queueMail;

    @Resource(mappedName = "jms/queueMailFactory")
    private ConnectionFactory context;

    @Resource(mappedName = "jms/topicContabilidad")
    private Topic topicContabilidad;

    @Resource(mappedName = "jms/topicContabilidadFactory")
    private ConnectionFactory topicFactory;

    @PersistenceContext(unitName = "NegocioALIPU")
    private EntityManager em;
    
    @EJB
    private PlatoFacadeRemote platoFacade;
    
    @EJB
    private ProxyMisPagos proxy;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TransaccionFacade() {
        super(Transaccion.class);
    }
    
    public List<Transaccion> getTransaccionByUsuario(int numDocumento, String tipoDocumento){
        try{
            Query q = getEntityManager().createNamedQuery("Transaccion.findByUsuario");
            q.setParameter("numDocumento", numDocumento);
            q.setParameter("tipoDocumento", tipoDocumento);
            return q.getResultList();
        }catch(NoResultException ex){
            return new ArrayList<>();
        }
    }
 
    public int pago(Usuario user, List<Plato> platos){
        try {
            int total = 0;
            System.out.println(platos.size());
            Transaccion tx = new Transaccion();
            Date date = new Date();
            String desc = "";
            for (Plato plato : platos) {
                total += plato.getPrecio().intValue();
                desc += plato.getNombre()+" ";
            }
            tx.setUsuario(user);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            tx.setFecha(cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+
                    cal.get(Calendar.YEAR));
            tx.setPlatoList(platos);
            tx.setValor(BigInteger.valueOf(total));
            System.out.println(total + " " + desc);
            DTOTransaccion tr = proxy.pago(user.getUsuarioPK().getTipoDocumento(), user.getUsuarioPK().getNumDocumento().toString(), total, desc, "Pago");
            if(tr.getTransaccionId() == -1){
                return -1;
            }else
                tx.setNumTransaccion(BigDecimal.valueOf(tr.getTransaccionId()));
            getEntityManager().persist(tx);
            sendJMSMessageToQueueMail(tx,user);
            for (Plato plato : platos) {
                plato.getTransaccionList().add(tx);
                platoFacade.edit(plato);
            }
            sendJMSMessageToTopicContabilidad(tx);
            return tr.getTransaccionId();
        } catch (Exception ex) {
            Logger.getLogger(TransaccionFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    private void sendJMSMessageToTopicContabilidad(Transaccion messageData) throws JMSException{
        Connection connection = null;
        Session session = null;
        try{
            connection = topicFactory.createConnection();
            session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(topicContabilidad);
            MapMessage mm = session.createMapMessage();
            mm.setString("TipoDocumento", messageData.getUsuario().getUsuarioPK().getTipoDocumento());
            mm.setInt("NumDocumento", messageData.getUsuario().getUsuarioPK().getNumDocumento().intValue());
            mm.setInt("NumTransaccion", messageData.getNumTransaccion().intValue());
            mm.setString("Fecha",messageData.getFecha());
            mm.setDouble("Valor", messageData.getValor().doubleValue());
            messageProducer.send(mm);
        }finally{
            if(session != null){
                try {
                    session.close();
                } catch (JMSException ex) {
                    Logger.getLogger(TransaccionFacade.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(connection != null)
                connection.close();
        }
    }
    
    public List<Transaccion> getTransacciones(String date){
        try{
            Query q = em.createNamedQuery("Transaccion.findByFecha");
            q.setParameter("fecha", date);
            List<Transaccion> l = q.getResultList();
            return l;
        }catch(NoResultException e){
            return new ArrayList<>();
        }
    }

    private void sendJMSMessageToQueueMail(Transaccion tx,Usuario user) throws JMSException {
        Connection connection = null;
        Session session = null;
        try{
            connection = context.createConnection();
            session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(queueMail);
            MapMessage mm = session.createMapMessage();
            mm.setInt("NumTransaccion",tx.getNumTransaccion().intValue());
            mm.setString("Usuario", user.getCorreo());
            mm.setInt("Valor", tx.getValor().intValue());
            messageProducer.send(mm);
        }finally{
            if(session != null){
                try {
                    session.close();
                } catch (JMSException ex) {
                    Logger.getLogger(TransaccionFacade.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(connection != null)
                connection.close();
        }
    }
    
}
