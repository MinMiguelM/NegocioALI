/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 *
 * @author SALABD
 */
@Singleton
public class TimerConsulta implements TimerConsultaLocal {
    
    @EJB
    private ConsultaPedidoUsuarioBean consultaPedidoUsuarioBean;
    
    @Schedule(minute = "*/5",hour = "*")
    @Override
    public void myTimer() {
        System.out.println("Timer event: " + new Date());
        consultaPedidoUsuarioBean.getTransaccionByUsuario("123");
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
