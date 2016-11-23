/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import entities.Plato;
import entities.Transaccion;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author SALABD
 */
@Stateless
@LocalBean
public class ConsultaPedidoUsuarioBean {
    
    @EJB
    private PedidosUsuario pedidosUsuario;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    public void getTransaccionByUsuario(String cedula){
        try {
            Future<List<Transaccion>> l = pedidosUsuario.getTransaccionByUsuario(cedula);
            for (Transaccion transaccion : l.get()) {
                System.out.println(transaccion.getCedulaUsuario() + " " +transaccion.getNumTransaccion());
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ConsultaPedidoUsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(ConsultaPedidoUsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
