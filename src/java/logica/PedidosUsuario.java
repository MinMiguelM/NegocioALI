/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import entities.Plato;
import entities.Transaccion;
import java.util.List;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author SALABD
 */
@Stateless
@LocalBean
public class PedidosUsuario {
    
    @EJB
    private TransaccionFacadeRemote transaccionFacade;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Asynchronous
    public Future<List<Transaccion>> getTransaccionByUsuario(String cedula){
        return new AsyncResult<List<Transaccion>>(transaccionFacade.getTransaccionByUsuario(cedula));
    }
}
