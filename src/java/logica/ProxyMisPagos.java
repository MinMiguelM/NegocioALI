/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import integracion.DTOTransaccion;
import integracion.WebService;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author SALABD
 */
@Stateless
@LocalBean
public class ProxyMisPagos {

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/10.192.10.15/WS/WebService.asmx.wsdl")
    private WebService service;

    /*public DTOTransaccion pago(java.lang.String tipo, java.lang.String doc, int valor, java.lang.String desc, java.lang.String concepto) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        integracion.WebServiceSoap port = service.getWebServiceSoap12();
        return port.pago(tipo, doc, valor, desc, concepto);
    }*/

    public DTOTransaccion pago(java.lang.String tipo, java.lang.String doc, int valor, java.lang.String desc, java.lang.String concepto) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        integracion.WebServiceSoap port = service.getWebServiceSoap();
        return port.pago(tipo, doc, valor, desc, concepto);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    
    
    
}
