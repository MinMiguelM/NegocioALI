/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import entities.Plato;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author SALABD
 */
@Stateless
public class PlatoFacade extends AbstractFacade<Plato> implements PlatoFacadeRemote {

    @PersistenceContext(unitName = "NegocioALIPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PlatoFacade() {
        super(Plato.class);
    }
    
    public List<Plato> getPlatos(String busqueda){
        try{
            Query q = em.createNamedQuery("Plato.findSimilar");
            q.setParameter("nombre","%"+busqueda+"%");
            List<Plato> l = q.getResultList();
            for (Plato plato : l) {
                plato.getTransaccionList().size();
            }
            return l;
        }catch(NoResultException e){
            return new ArrayList<>();
        }
    }
    
    public Plato getPlatoByName(String busqueda){
        try{
            Query q = em.createNamedQuery("Plato.findByNombre");
            q.setParameter("nombre",busqueda);
            Plato p =(Plato)q.getSingleResult();
            p.getTransaccionList().size();
            return p;
        }catch(NoResultException e){
            return null;
        }
    }
}
