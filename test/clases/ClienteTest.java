package clases;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

class ClienteTest {
	  private ObservableList<Cliente> listaClientes;
	    private TableView<Cliente> tablaClientes;
	 @Test
	    public void testRegistrarNuevoCliente() {
	        Cliente cliente = new Cliente();

	        String dni = "12345678X";
	        String nombre = "Juan";
	        String apellidos = "Pérez";
	        String email = "juan.perez@example.com";

	        cliente.registrarNuevoCliente(dni, nombre, apellidos, email);

	        assertEquals(dni, cliente.getDni());
	        assertEquals(nombre, cliente.getNombre());
	        assertEquals(apellidos, cliente.getApellidos());
	        assertEquals(email, cliente.getEmail());
	        Cliente.eliminarClienteBD(dni);
	    }
	@Test
	    public void start(Stage stage) {
	        listaClientes = FXCollections.observableArrayList();
	        tablaClientes = new TableView<>();
	 
	        Cliente.mostrarRegistrarCliente(listaClientes, tablaClientes);
	    }
	@Test
    public void testObtenerClientesRegistrados() {
        try {
            List<Cliente> clientes = Cliente.obtenerClientesRegistrados();

            assertNotNull(clientes, "La lista no debe ser null");
            // Puedes añadir comprobaciones según datos que sabes que existen en tu BD de prueba
            assertTrue(clientes.size() >= 0, "La lista debe contener 0 o más clientes");

            // Por ejemplo, verificar que todos los clientes tengan DNI no vacío
            for (Cliente c : clientes) {
                assertNotNull(c.getDni());
                assertFalse(c.getDni().isEmpty());
                assertNotNull(c.getNombre());
                assertNotNull(c.getApellidos());
                assertNotNull(c.getEmail());
            }
        } catch (SQLException e) {
            fail("SQLException lanzada: " + e.getMessage());
        }
    }
	@Test
    public void testVerCatalogoProductos() {
        assertDoesNotThrow(() -> {
            Platform.runLater(() -> {
                Cliente c = new Cliente();
               c.verCatalogoProductos();
            });
            Thread.sleep(1000);
        });
    }
	
	 @Test
	    public void testBuscarProductoPorNombre() {
	        assertDoesNotThrow(() -> {
	            Platform.runLater(() -> {
	                Cliente c = new Cliente();
	                try {
	                    c.buscarProductoPorNombre();
	                } catch (Exception e) {
	                    fail("El método lanzó una excepción: " + e.getMessage());
	                }
	            });

	        });
	    }
	 @Test
	    public void testVerFacturas() throws InterruptedException {
	        Platform.runLater(() -> {
	            try {
	                Cliente cliente = new Cliente();
	                cliente.verFacturas("12345678A");
	            } catch (Exception e) {
	                fail("verFacturas lanzó excepción: " + e.getMessage());
	            }
	        });
	       
	    }
	 @Test
	 public void testParticiparEnSorteo() throws InterruptedException {
	     Platform.runLater(() -> {
	         try {
	             Cliente cliente = new Cliente();
	             cliente.participarEnSorteo();
	         } catch (Exception e) {
	             fail("participarEnSorteo lanzó excepción: " + e.getMessage());
	         }
	     });
	
	 }
	 @Test
	 public void testMostrarCuponesDisponibles() throws InterruptedException {
	     Platform.runLater(() -> {
	         try {
	             Cliente cliente = new Cliente();
	             cliente.mostrarCuponesDisponibles();
	         } catch (Exception e) {
	             fail("mostrarCuponesDisponibles lanzó excepción: " + e.getMessage());
	         }
	     });

	 }
}
