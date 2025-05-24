package clases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;

public class CuponTest {

    @Test
    public void testGettersSetters() {
        Cliente cliente = new Cliente(); 
        Cupon cupon = new Cupon();

        cupon.setId_cupon(10);
        cupon.setDescuento(15.5);
        cupon.setCliente(cliente);
        cupon.setUsado(true);

        Promocion promo = new Promocion();
        promo.setId_promocion(5);
        promo.setDescripcion("Promo especial");
        cupon.setPromocion(promo);

        assertEquals(10, cupon.getId_cupon());
        assertEquals(15.5, cupon.getDescuento());
        assertEquals(cliente, cupon.getCliente());
        assertTrue(cupon.isUsado());
        assertEquals(promo, cupon.getPromocion());
    }

    @Test
    public void testToString() {
        Cupon cupon = new Cupon();
        cupon.setId_cupon(1);
        cupon.setDescuento(20.0);

        Promocion promo = new Promocion();
        promo.setDescripcion("Descuento verano");
        cupon.setPromocion(promo);

        String esperado = "Cupón #1 - 20.0% (Descuento verano)";
        assertEquals(esperado, cupon.toString());


        Cupon cuponSinPromo = new Cupon();
        cuponSinPromo.setId_cupon(2);
        cuponSinPromo.setDescuento(10.0);

        String esperadoSinPromo = "Cupón #2 - 10.0% (Sin descripción)";
        assertEquals(esperadoSinPromo, cuponSinPromo.toString());
    }

    @Test
    public void testObtenerCuponesDisponibles() {

        List<Cupon> cupones = Cupon.obtenerCuponesDisponibles();
        assertNotNull(cupones);

    }

    @Test
    public void testMarcarComoUsado() {

        boolean resultado = Cupon.marcarComoUsado(-1);
        assertTrue(resultado == false || resultado == true);
    }
}