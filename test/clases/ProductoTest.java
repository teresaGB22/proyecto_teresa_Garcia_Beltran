package clases;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import clases.Producto;
import clases.Proveedor;

public class ProductoTest {

    @Test
    public void testCargarProductosDisponibles_NoVacio() {
        var productos = Producto.cargarProductosDisponibles();
        assertNotNull(productos, "La lista no debe ser nula");
        assertTrue(productos.size() > 0, "Debe cargar al menos un producto");
    }

    @Test
    public void testGettersSetters() {
        Proveedor proveedor = new Proveedor(1); 
        LocalDate fecha = LocalDate.of(2023, 1, 1);
        Producto producto = new Producto(10, "Microfono", fecha, 100, true, 50.0, proveedor);

        assertEquals(10, producto.getIdProducto());
        assertEquals("ProductoTest", producto.getNombre());
        assertEquals(fecha, producto.getFechaEntrada());
        assertEquals(100, producto.getStock());
        assertTrue(producto.isEsOnline());
        assertEquals(50.0, producto.getPrecio());
        assertEquals(proveedor, producto.getProveedor());

  
        producto.setNombre("Microfono");
        producto.setStock(200);
        producto.setEsOnline(false);
        producto.setPrecio(75.5);

        assertEquals("Microfono", producto.getNombre());
        assertEquals(200, producto.getStock());
        assertFalse(producto.isEsOnline());
        assertEquals(75.5, producto.getPrecio());
    }

    @Test
    public void testToString() {
        Producto producto = new Producto(1, "Microfono", LocalDate.now(), 10, true, 25.0, new Proveedor(1));
        assertEquals("Microfono", producto.toString());
    }
}