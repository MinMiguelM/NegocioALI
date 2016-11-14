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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author SALABD
 */
@Stateless
public class TransaccionFacade extends AbstractFacade<Transaccion> implements logica.TransaccionFacadeRemote {

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
        //llamar a mis pagos
        return 0;
    }
}
