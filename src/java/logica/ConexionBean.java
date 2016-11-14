/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import entities.Plato;
import entities.Usuario;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author SALABD
 */
@Stateless
public class ConexionBean implements ConexionBeanRemote {

    @EJB
    private PlatoFacadeRemote platoFacade;
    @EJB
    private TransaccionFacadeRemote transaccionFacade;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public List<Plato> getPlatos(String busqueda) {
        return platoFacade.getPlatos(busqueda);
    }
    
    @Override
    public int pago(Usuario user, List<Plato> platos){
        return transaccionFacade.pago(user, platos);
    }

}
