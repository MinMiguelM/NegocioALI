/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import entities.Restaurante;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author SALABD
 */
@Stateless
public class RestauranteFacade extends AbstractFacade<Restaurante> implements logica.RestauranteFacadeRemote {

    @PersistenceContext(unitName = "NegocioALIPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RestauranteFacade() {
        super(Restaurante.class);
    }
    
    public List<Restaurante> getRestaurantes(String busqueda){
        Query q = em.createNamedQuery("Restaurante.findSimilar");
        q.setParameter("nombre","%"+busqueda+"%");
        return q.getResultList();
        
    }
    
}
