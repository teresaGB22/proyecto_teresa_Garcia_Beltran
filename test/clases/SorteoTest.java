package clases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SorteoTest {

    @Test
    public void testConstructorYGetters() {
        Cliente cliente = new Cliente("12345678A"); // Suponiendo que Cliente tiene constructor con DNI
        Sorteo.Resultado resultado = Sorteo.Resultado.GANADOR;
        Sorteo sorteo = new Sorteo(1, resultado, "Bicicleta", cliente);

        assertEquals(1, sorteo.getIdSorteo());
        assertEquals(resultado, sorteo.getResultado());
        assertEquals("Bicicleta", sorteo.getPremio());
        assertEquals(cliente, sorteo.getCliente());
    }

    @Test
    public void testSetters() {
        Sorteo sorteo = new Sorteo(0, "Premio inicial");

        sorteo.setIdSorteo(5);
        assertEquals(5, sorteo.getIdSorteo());

        sorteo.setPremio("Nuevo Premio");
        assertEquals("Nuevo Premio", sorteo.getPremio());

        Sorteo.Resultado resultado = Sorteo.Resultado.PERDEDOR;
        sorteo.setResultado(resultado);
        assertEquals(resultado, sorteo.getResultado());

        Cliente cliente = new Cliente("87654321B");
        sorteo.setCliente(cliente);
        assertEquals(cliente, sorteo.getCliente());
    }

    @Test
    public void testToString() {
        Sorteo sorteo = new Sorteo(10, "Tablet");
        String esperado = "ID: 10 | Premio: Tablet";
        assertEquals(esperado, sorteo.toString());
    }
}