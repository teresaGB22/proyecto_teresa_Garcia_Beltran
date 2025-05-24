package clases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.stage.Stage;

class ProveedorTest {

	 @Test
	    public void testRegistrarNuevoProveedor() {
	        Proveedor proveedor = new Proveedor();

	       
	        String dni = "98765432Z";
	        String nombre = "Test";
	        String apellidos = "Proveedor";

	        int idGenerado = proveedor.registrarNuevoProveedor(dni, nombre, apellidos);

	        assertTrue(idGenerado > 0, "El ID generado debe ser mayor que 0");

	        assertEquals(dni, proveedor.getDni());
	        assertEquals(nombre, proveedor.getNombre());
	        assertEquals(apellidos, proveedor.getApellidos());
	    }

	    private Proveedor proveedor;

	    public void start(Stage stage) {
	        proveedor = new Proveedor();
	        
	        proveedor.gestionarProductos();
	    }

	   


}
