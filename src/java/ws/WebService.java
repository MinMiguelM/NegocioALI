/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import entities.Plato;
import entities.Transaccion;
import entities.Usuario;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import logica.TransaccionFacadeRemote;

/**
 *
 * @author sala-a
 */
@javax.jws.WebService(serviceName = "WebService")
@Stateless()
public class WebService {

    @EJB
    private TransaccionFacadeRemote ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Web Service Operation")

    @WebMethod(operationName = "create")
    @Oneway
    public void create(@WebParam(name = "transaccion") Transaccion transaccion) {
        ejbRef.create(transaccion);
    }

    @WebMethod(operationName = "find")
    public Transaccion find(@WebParam(name = "id") Object id) {
        return ejbRef.find(id);
    }

    @WebMethod(operationName = "findAll")
    public List<Transaccion> findAll() {
        return ejbRef.findAll();
    }

    @WebMethod(operationName = "findRange")
    public List<Transaccion> findRange(@WebParam(name = "range") int[] range) {
        return ejbRef.findRange(range);
    }

    @WebMethod(operationName = "count")
    public int count() {
        return ejbRef.count();
    }
    
    @WebMethod(operationName = "getTransacciones")
    public List<Transaccion> getTransacciones(@WebParam(name = "date") String date){
        return ejbRef.getTransacciones(date);
    }
    
}
