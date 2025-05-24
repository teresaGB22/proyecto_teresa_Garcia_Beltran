package clases;
import java.sql.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utilidades.ConexionBD;

public class App extends Application {
	/**
	 * Muestra el menú principal para un cliente con varias opciones disponibles.
	 * @param c El objeto Cliente que representa al cliente actual.
	 */
	 private void mostrarMenuCliente(Cliente c) {
	    	
	    	
	    	Button btnComprarProductos = new Button("Comprar productos");
	    	Button btnVerCatalogo = new Button("Ver Catálogo de Productos");
	        Button btnBuscarProducto = new Button("Buscar Producto por Nombre");
	       
	        Button btnVerFacturas = new Button("Ver Mis Facturas");
	        Button btnParticiparSorteo = new Button("Participar en Sorteo");
	        Button btnConsultarCupones = new Button("Consultar Mis Cupones");
	        Button btnVolver = new Button("Volver");

	        btnComprarProductos.setOnAction(e ->{
	        	c.comprarProductos(c.getDni());
	        });
	        btnVerCatalogo.setOnAction(e -> {
				c.verCatalogoProductos();
			});
	        btnBuscarProducto.setOnAction(e -> {
				try {
					c.buscarProductoPorNombre();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
	        
	        btnVerFacturas.setOnAction(e -> {
				try {
					c.verFacturas(c.getDni());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
	        btnParticiparSorteo.setOnAction(e -> {
				try {
					c.participarEnSorteo();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
	        btnConsultarCupones.setOnAction(e -> {
				c.mostrarCuponesDisponibles();
			});
	        btnVolver.setOnAction(e -> {
	            
	            ((Stage) btnVolver.getScene().getWindow()).close();

	        });
	        VBox layout = new VBox(10);
	        layout.setStyle("-fx-padding: 20;");
	        layout.getChildren().addAll(
	            new Label("Menú Cliente:"),
	            btnComprarProductos,
	            btnVerCatalogo,
	            btnBuscarProducto,
	           
	            btnVerFacturas,
	            btnParticiparSorteo,
	            btnConsultarCupones,
	            btnVolver
	        );
	        Scene scene = new Scene(layout, 350, 400);
	        Stage stage = new Stage();
	        stage.setTitle("Menú Cliente");
	        stage.setScene(scene);
	        stage.show();
	    }
	
	 /**
	  * Comprueba si un cliente existe en la base de datos mediante su DNI.
	  * @param dni El DNI del cliente a verificar.
	  * @return true si el cliente existe, false en caso contrario o si ocurre un error.
	  */
	 private boolean clienteExiste(String dni) {
	    String q = "SELECT COUNT(*) FROM cliente WHERE dni = ?";
	    try (Connection c = ConexionBD.obtenerConexion();
	         PreparedStatement pst = c.prepareStatement(q)) {
	        pst.setString(1, dni);
	        ResultSet rs = pst.executeQuery();
	        return rs.next() && rs.getInt(1) > 0;
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        return false;
	    }
	}
	 /**
	  * Muestra la ventana de inicio de sesión para un cliente, permitiendo ingresar o registrarse.
	  * Al ingresar, si el DNI existe, se muestra el menú de cliente.
	  * Al registrarse, se crea un nuevo cliente en la base de datos.
	  */
	private void mostrarVentanaLoginCliente() {
	    Stage ventana = new Stage();
	    ventana.setTitle("Inicio de Sesión - Cliente");

	    VBox layout = new VBox(10);
	    layout.setStyle("-fx-padding: 14;");
	    
	    Label lblDni = new Label("Introduce tu DNI:");
	    TextField txtDni = new TextField();
	    Label lblNombre = new Label("Introduce tu nombre");
	    TextField txtNombre = new TextField();
	    Label lblApellidos = new Label("Introduce tu primer Apellidos");
	    TextField txtApellidos = new TextField();
	    Label lblEmail = new Label("Introduce tu correo");
	    TextField txtEmail = new TextField();
	    txtDni.setPromptText("DNI...");
	    txtNombre.setPromptText("Nombre...");
	    txtApellidos.setPromptText("Apellidos...");
	    txtEmail.setPromptText("correo...");
	    Label lblEstado = new Label();

	    Button btnIngresar = new Button("Ingresar");
	    Button btnRegistrar = new Button("Registrarse");

	    layout.getChildren().addAll(lblDni, txtDni, lblNombre,txtNombre,lblApellidos, txtApellidos, lblEmail, txtEmail, btnIngresar, btnRegistrar, lblEstado);
	    Scene escena = new Scene(layout, 300, 600);
	    ventana.setScene(escena);
	    ventana.show();

	    btnIngresar.setOnAction(e -> {
	        String dni = txtDni.getText().trim();
	        if (dni.isEmpty()) {
	            lblEstado.setText("Por favor, introduce un DNI.");
	            return;
	        }

	        String q = "SELECT * FROM cliente WHERE dni = ?";
	        try (Connection c = ConexionBD.obtenerConexion();
	             PreparedStatement pst = c.prepareStatement(q)) {
	            pst.setString(1, dni);
	            ResultSet rs = pst.executeQuery();
	            if (rs.next()) {
	                Cliente cliente = new Cliente();
	                cliente.setDni(rs.getString("dni"));
	                cliente.setNombre(rs.getString("nombre"));
	                cliente.setApellidos(rs.getString("apellidos"));
	                cliente.setEmail(rs.getString("email"));
	                lblEstado.setText("Bienvenido, " + cliente.getNombre());
	                ventana.close();
	                mostrarMenuCliente(cliente);
	            } else {
	                lblEstado.setText("DNI no registrado. Pulsa 'Registrarse'.");
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	            lblEstado.setText("Error al verificar el cliente.");
	        }
	    });

	    btnRegistrar.setOnAction(e -> {
	        String dni = txtDni.getText().trim();
	        String nombre = txtNombre.getText().trim();
	        String Apellidos = txtApellidos.getText().trim();
	        String email = txtEmail.getText();
	        if (dni.isEmpty() || nombre.isEmpty() || Apellidos.isEmpty() || email.isEmpty()) {
	            lblEstado.setText("Por favor, introduzca todos los datos");
	           
	        }else {
	        	if (clienteExiste(dni)) {
		            lblEstado.setText("Ese DNI ya está registrado.");
		        } else {
		        	Cliente c = new Cliente();
		            c.registrarNuevoCliente(dni, nombre, Apellidos, email);
		            lblEstado.setText("Cliente registrado. Ya puedes ingresar.");
		            
		            c.setDni(dni);
		            ventana.close();
		            mostrarMenuCliente(c);
		        }
	        }

	        
	    });
	}
   
	/**
	 * Muestra el menú principal para un empleado con opciones específicas para la gestión.
	 * @param empleado El objeto Empleado que representa al empleado actual.
	 */
    private void mostrarMenuEmpleado(Empleado empleado) {
    	Button btnGestionClientes = new Button("Gestionar Clientes");
    	Button btnGestionPedidosVentas = new Button("Gestion de pedidos o ventas");
    	Button btnPerfil = new Button("Actualizar el Perfil");
    	Button btnVolver = new Button("Volver");
    	
    	btnGestionClientes.setOnAction(e ->{
    		try {
				empleado.gestionarClientes();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	});
    	btnGestionPedidosVentas.setOnAction(e ->{
    		empleado.mostrarPedidos();    	});
btnVolver.setOnAction(e -> {
            
            ((Stage) btnVolver.getScene().getWindow()).close();

        });

VBox layout = new VBox(10);
layout.setStyle("-fx-padding: 20;");
layout.getChildren().addAll(btnGestionClientes,btnGestionPedidosVentas, btnVolver);
Scene scene = new Scene(layout, 350, 400);
Stage stage = new Stage();
stage.setTitle("Menú empleado");
stage.setScene(scene);
stage.show();
    	
    }
    /**
     * Muestra el menú principal para un proveedor con opciones específicas para gestión de productos, pedidos y más.
     * @param p El objeto Proveedor que representa al proveedor actual.
     */
    private void mostrarMenuProveedor(Proveedor p) {
        
        Button btnproductos = new Button("Gestionar Productos");
        Button btnPedidos = new Button("Ver pedidos");
        Button btnVentas = new Button("Gestion de ventas");
        Button btnPerfil = new Button("Actualizar el perfil");
        Button btnCupones = new Button("Promociones y cupones");
        Button btnSorteo = new Button("Crear sorteo");
        Button btnVolver = new Button("Volver");
        btnproductos.setOnAction(e ->{
        	p.gestionarProductos();
        }
        
        		);
        btnPedidos.setOnAction(e -> {
        p.verPedidos(); 
        }
        );
        btnVentas.setOnAction(e ->{
        	p.gestionVentas();
        });
        btnPerfil.setOnAction(e ->{
        	p.actualizarPerfil();
        });
        btnCupones.setOnAction(e ->{
        	p.gestionPromocionesCupones();
        });
        btnSorteo.setOnAction(e ->{
        	p.CrearSorteo();
        });
        btnVolver.setOnAction(e -> {
            
            ((Stage) btnVolver.getScene().getWindow()).close();

        });
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20;");
        layout.getChildren().addAll(
            new Label("Menú Proveedor:"),
            btnproductos,
            btnPedidos,
            btnVentas,
            btnPerfil,
            btnCupones,
            btnSorteo,
            btnVolver
        );
        Scene scene = new Scene(layout, 350, 400);
        Stage stage = new Stage();
        stage.setTitle("Menú Proveedor");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Comprueba si un proveedor existe en la base de datos mediante su DNI.
     * @param dni El DNI del proveedor a verificar.
     * @return true si el proveedor existe, false en caso contrario o si ocurre un error.
     */
    private boolean proveedorExiste(String dni) {
        String q = "SELECT COUNT(*) FROM proveedor WHERE dni = ?";
        try (Connection c = ConexionBD.obtenerConexion();
             PreparedStatement pst = c.prepareStatement(q)) {
            pst.setString(1, dni);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Comprueba si un empleado existe en la base de datos mediante su usuario.
     * @param user El nombre de usuario del empleado a verificar.
     * @return true si el empleado existe, false en caso contrario o si ocurre un error.
     */
    private boolean empleadoExiste(String user) {
        String q = "SELECT COUNT(*) FROM empleado WHERE usuario = ?";
        try (Connection c = ConexionBD.obtenerConexion();
             PreparedStatement pst = c.prepareStatement(q)) {
            pst.setString(1, user);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

/**
 * Muestra la ventana de inicio de sesión para un empleado, permitiendo ingresar o registrarse.
 * Al ingresar, si el usuario existe, se muestra el menú de empleado.
 * Al registrarse, se crea un nuevo empleado en la base de datos.
 */
    private void mostrarLoginEmpleado() {
    	Stage ventana = new Stage();
	    ventana.setTitle("Inicio de Sesión - Empleado");

	    VBox layout = new VBox(10);
	    layout.setStyle("-fx-padding: 14;");

	    
	    Label lblUsuario = new Label("Introduce tu usuario");
	    TextField txtUsuario = new TextField();
	    Label lblContrasenya = new Label("Introduce tu contraseña");
	    TextField txtContrasenya = new TextField();
	    Label lblEstado = new Label();

	    Button btnIngresar = new Button("Ingresar");
	    Button btnRegistrar = new Button("Registrar");

	    layout.getChildren().addAll( lblUsuario,txtUsuario,lblContrasenya,txtContrasenya, btnIngresar, btnRegistrar, lblEstado);
	    Scene escena = new Scene(layout, 300, 600);
	    ventana.setScene(escena);
	    ventana.show();

	    btnIngresar.setOnAction(e -> {
	        String user = txtUsuario.getText().trim();
	        String contrasenya = txtContrasenya.getText().trim();
	        if (user.isEmpty() || contrasenya.isEmpty()) {
	            lblEstado.setText("Por favor, introduce su usuario y contrasenya");
	            return;
	        }

	        String q = "SELECT * FROM empleado WHERE usuario = ?";
	        try (Connection c = ConexionBD.obtenerConexion();
	             PreparedStatement pst = c.prepareStatement(q)) {
	            pst.setString(1, user);

	            ResultSet rs = pst.executeQuery();
	            if (rs.next()) {
	                Empleado empleado = new Empleado();
	                empleado.setIdEmpleado(rs.getInt("id_empleado"));  
	                empleado.setContraseña(rs.getString("contrasenya"));
	                empleado.setUsuario(rs.getString("usuario"));
	                

	                lblEstado.setText("Bienvenido, " + empleado.getUsuario());
	                ventana.close();
	                mostrarMenuEmpleado(empleado); 
	            } else {
	                lblEstado.setText("Usuario no registrado. Pulsa para registrarse");
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	            lblEstado.setText("Error al verificar el empleado");
	        }
	    });

	    btnRegistrar.setOnAction(e -> {
	        String user = txtUsuario.getText().trim();
	        String contrasenya = txtContrasenya.getText().trim();

	        if (user.isEmpty() || contrasenya.isEmpty() ) {
	            lblEstado.setText("Por favor, introduzca todos los datos");
	        } else {
	            if (empleadoExiste(user)) {  
	                lblEstado.setText("Ese DNI ya está registrado.");
	            } else {
	                Empleado empleado = new Empleado();
	                int id = empleado.registrarNuevoEmpleado(user,contrasenya);
	                if (id != -1) {
	                    empleado.setIdEmpleado(id);  
	                    lblEstado.setText("Empleado registrado. Ya puedes ingresar.");

	                    ventana.close();
	                    mostrarMenuEmpleado(empleado);
	                } else {
	                    lblEstado.setText("Error al registrar empleado.");
	                }
	            }
	        }
	    });
    }
    /**
     * Muestra la ventana de inicio de sesión para un proveedor, permitiendo ingresar o registrarse.
     * Al ingresar, si el DNI existe, se muestra el menú de proveedor.
     * Al registrarse, se crea un nuevo proveedor en la base de datos.
     */
    private void mostrarLoginProveedor() {
    	 Stage ventana = new Stage();
    	    ventana.setTitle("Inicio de Sesión - Proveedor");

    	    VBox layout = new VBox(10);
    	    layout.setStyle("-fx-padding: 14;");

    	    Label lblDni = new Label("Introduce tu dni");
    	    TextField txtDni = new TextField();
    	    Label lblNombre = new Label("Introduce tu nombre");
    	    TextField txtNombre = new TextField();
    	    Label lblApellidos = new Label("Introduce tu primer Apellidos");
    	    TextField txtApellidos = new TextField();
    	    Label lblEstado = new Label();

    	    Button btnIngresar = new Button("Ingresar");
    	    Button btnRegistrar = new Button("Registrar");

    	    layout.getChildren().addAll(lblDni, txtDni, lblNombre, txtNombre, lblApellidos, txtApellidos, btnIngresar, btnRegistrar, lblEstado);
    	    Scene escena = new Scene(layout, 300, 600);
    	    ventana.setScene(escena);
    	    ventana.show();

    	    btnIngresar.setOnAction(e -> {
    	        String dni = txtDni.getText().trim();
    	        if (dni.isEmpty()) {
    	            lblEstado.setText("Por favor, introduce su dni");
    	            return;
    	        }

    	        String q = "SELECT * FROM proveedor WHERE dni = ?";
    	        try (Connection c = ConexionBD.obtenerConexion();
    	             PreparedStatement pst = c.prepareStatement(q)) {
    	            pst.setString(1, dni);

    	            ResultSet rs = pst.executeQuery();
    	            if (rs.next()) {
    	                Proveedor p = new Proveedor();
    	                p.setIdProveedor(rs.getInt("id_proveedor"));  // <-- importante
    	                p.setDni(rs.getString("dni"));
    	                p.setNombre(rs.getString("nombre"));
    	                p.setApellidos(rs.getString("apellidos"));

    	                lblEstado.setText("Bienvenido, " + p.getNombre());
    	                ventana.close();
    	                mostrarMenuProveedor(p);  // <-- pasamos el proveedor con ID
    	            } else {
    	                lblEstado.setText("DNI no registrado. Pulsa 'Registrarse'.");
    	            }
    	        } catch (SQLException ex) {
    	            ex.printStackTrace();
    	            lblEstado.setText("Error al verificar el proveedor");
    	        }
    	    });

    	    btnRegistrar.setOnAction(e -> {
    	        String dni = txtDni.getText().trim();
    	        String nombre = txtNombre.getText().trim();
    	        String apellidos = txtApellidos.getText().trim();

    	        if (dni.isEmpty() || nombre.isEmpty() || apellidos.isEmpty()) {
    	            lblEstado.setText("Por favor, introduzca todos los datos");
    	        } else {
    	            if (proveedorExiste(dni)) {  
    	                lblEstado.setText("Ese DNI ya está registrado.");
    	            } else {
    	                Proveedor p = new Proveedor();
    	                int id = p.registrarNuevoProveedor(dni, nombre, apellidos);
    	                if (id != -1) {
    	                    p.setIdProveedor(id);  // <-- asignar ID generado
    	                    lblEstado.setText("Proveedor registrado. Ya puedes ingresar.");

    	                    ventana.close();
    	                    mostrarMenuProveedor(p);
    	                } else {
    	                    lblEstado.setText("Error al registrar proveedor.");
    	                }
    	            }
    	        }
    	    });
	    }
    /**
     * Método principal que se ejecuta al iniciar la aplicación JavaFX.
     * Muestra la ventana principal con opciones para acceder como Cliente, Empleado, Proveedor o salir.
     *
     * @param stage La ventana principal (Stage) de la aplicación.
     */
    @Override
    public void start(Stage stage) {
        
        VBox vbox = new VBox(10); 
        vbox.setStyle("-fx-padding: 20;");

        Button btnCliente = new Button("Cliente");
        Button btnEmpleado = new Button("Empleado");
        Button btnAdministrador = new Button("Proveedor");
        Button btnSalir = new Button("Salir");

        btnCliente.setOnAction(e -> mostrarVentanaLoginCliente());

        btnEmpleado.setOnAction(e -> mostrarLoginEmpleado());

        btnAdministrador.setOnAction(e -> mostrarLoginProveedor());

        btnSalir.setOnAction(e -> stage.close());

        vbox.getChildren().addAll(btnCliente, btnEmpleado, btnAdministrador, btnSalir);

        Scene scene = new Scene(vbox, 300, 200);

        stage.setTitle("Menú Principal");
        stage.setScene(scene);
     
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
