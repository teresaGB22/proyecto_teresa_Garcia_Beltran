package clases;
import clases.*;
import clases.Venta.EstadoPedido;
import clases.Venta.MetodoEnvio;
import clases.Venta.MetodoPago;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.sql.*;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.*;


class VentaTest {
	  private Venta venta;
	    private Cliente cliente;
	    private Empleado empleado;
	    @BeforeEach
	    public void setUp() {
	        cliente = new Cliente("12345678A", "Juan ", "Perez Garcia","juan@example.com");
	        empleado = new Empleado(1, "Ana", "García");
	        venta = new Venta(
	                1001,
	                LocalDate.of(2025, 5, 24),
	                250.75,
	                EstadoPedido.PENDIENTE,
	                cliente,
	                MetodoPago.TARJETA,
	                MetodoEnvio.VEINTICUATRO_HORAS
	        );
	        venta.setEmpleado(empleado);
	        venta.setCosteEnvio(15.00);
	    }
    @Test
    public void testFuncionamientoGetSet() {
        assertEquals(1001, venta.getIdVenta());
        assertEquals(LocalDate.of(2025, 5, 24), venta.getFecha_venta());
        assertEquals(250.75, venta.getTotal());
        assertEquals(EstadoPedido.PENDIENTE, venta.getEstadoPedido());
        assertEquals(cliente, venta.getCliente());
        assertEquals(empleado, venta.getEmpleado());
        assertEquals(MetodoPago.TARJETA, venta.getMetodoPago());
        assertEquals(MetodoEnvio.VEINTICUATRO_HORAS, venta.getMetodoEnvio());
        assertEquals(15.00, venta.getCosteEnvio());

        venta.setEstadoPedido(EstadoPedido.CONFIRMADO);
        assertEquals(EstadoPedido.CONFIRMADO, venta.getEstadoPedido());
    }
    public void testActualizarVenta() {
        try {
            venta.setEstadoPedido(EstadoPedido.CONFIRMADO);
            venta.setTotal(300.50);
            venta.actualizarVenta();  

            
        } catch (SQLException e) {
            fail("Error al actualizar venta en la base de datos: " + e.getMessage());
        }
    }
}
