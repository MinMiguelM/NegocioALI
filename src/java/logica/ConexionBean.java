/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import entities.Plato;
import entities.Restaurante;
import entities.Usuario;
import entities.UsuarioPK;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

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
    @EJB
    private RestauranteFacadeRemote restauranteFacade;
    @EJB
    private UsuarioFacadeRemote usuarioFacade;

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
    
    @Override
    public boolean eliminarPlato(Plato p){
        platoFacade.remove(p);
        return true;
    }
    
    @Override
    public Plato getPlatoByName (String busq){
       return platoFacade.getPlatoByName(busq);
    }
    
    @Override
    public void editarPlato(Plato p){
        platoFacade.edit(p);
    }
    
    @Override
    public void agregarPlato(Plato p, Restaurante r){
        System.out.println(r.getNombre());
        platoFacade.create(p);
    }
    
    @Override
    public void editarRestaurante(Restaurante r){
        restauranteFacade.edit(r);
    }
    
    @Override
    public List<Restaurante> getRestaurantes(String busqueda){
        return restauranteFacade.getRestaurantes(busqueda);
    }
    
    @Override
    public void agregarRestaurante(Restaurante r){
        restauranteFacade.create(r);
    }
    
    @Override
    public boolean eliminarRest(Restaurante r){
        restauranteFacade.remove(r);
        return true;
    }
    
    @Override
    public Restaurante getRestauranteByName(String busq){
        return restauranteFacade.getByName(busq);
    }
    
    @Override
    public Usuario findUsuario(String tipo, int numero){
        try{
            UsuarioPK pk = new UsuarioPK(BigInteger.valueOf(numero), tipo);
            return usuarioFacade.find(pk);
        }catch(NoResultException e){
            return null;
        }
    }

}
