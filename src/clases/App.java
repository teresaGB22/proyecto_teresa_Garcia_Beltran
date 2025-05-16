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
	 private void mostrarMenuCliente(Cliente c) {
	    	
	    	Cliente cliente = new Cliente();
	    	Button btnVerCatalogo = new Button("Ver Catálogo de Productos");
	        Button btnBuscarProducto = new Button("Buscar Producto por Nombre");
	        Button btnUsarCupon = new Button("Usar Cupón de Descuento");
	        Button btnVerFacturas = new Button("Ver Mis Facturas");
	        Button btnParticiparSorteo = new Button("Participar en Sorteo");
	        Button btnConsultarCupones = new Button("Consultar Mis Cupones");
	        Button btnVolver = new Button("Volver");

	        btnVerCatalogo.setOnAction(e -> {
				cliente.verCatalogoProductos();
			});
	        btnBuscarProducto.setOnAction(e -> {
				try {
					cliente.buscarProductoPorNombre();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
	        btnUsarCupon.setOnAction(e -> {
				try {
					cliente.usarCuponDescuento(c.getDni());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
	        btnVerFacturas.setOnAction(e -> {
				try {
					cliente.verFacturas(c.getDni());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
	        btnParticiparSorteo.setOnAction(e -> {
				try {
					cliente.participarEnSorteo(c.getDni());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
	        btnConsultarCupones.setOnAction(e -> {
				try {
					cliente.consultarCuponesDisponibles();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
	        btnVolver.setOnAction(e -> {
	            
	            ((Stage) btnVolver.getScene().getWindow()).close();

	        });
	        VBox layout = new VBox(10);
	        layout.setStyle("-fx-padding: 20;");
	        layout.getChildren().addAll(
	            new Label("Menú Cliente:"),
	            btnVerCatalogo,
	            btnBuscarProducto,
	            btnUsarCupon,
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
	private void registrarNuevoCliente(String dni, String nombre, String apellidos, String email) {
	    String q = "INSERT INTO cliente (dni, nombre, apellidos, email) VALUES (?, ?, ?, ?)";
	    try (Connection c = ConexionBD.obtenerConexion();
	         PreparedStatement pst = c.prepareStatement(q)) {
	        pst.setString(1, dni);
	        pst.setString(2, nombre);
	        pst.setString(3, apellidos);
	        pst.setString(4, email);
	        pst.executeUpdate();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}
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
		            registrarNuevoCliente(dni, nombre, Apellidos, email);
		            lblEstado.setText("Cliente registrado. Ya puedes ingresar.");
		            Cliente cliente = new Cliente();
		            cliente.setDni(dni);
		            ventana.close();
		            mostrarMenuCliente(cliente);
		        }
	        }

	        
	    });
	}
   

    private void mostrarMenuEmpleado() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Menú Empleado");
        alert.setHeaderText("Opciones para Empleado");
        alert.setContentText("1. Registrar nueva venta\n2. Buscar productos disponibles\n3. Ver historial de ventas registradas");
        alert.showAndWait();
    }

    private void mostrarMenuProveedor(Proveedor p) {
        
        Button btnproductos = new Button("Gestionar Productos");
        Button btnPedidos = new Button("Ver pedidos");
        Button btnVentas = new Button("Gestion de ventas");
        Button btnPerfil = new Button("Actualizar el perfil");
        Button btnCupones = new Button("Promociones y cupones");
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
            btnVolver
        );
        Scene scene = new Scene(layout, 350, 400);
        Stage stage = new Stage();
        stage.setTitle("Menú Proveedor");
        stage.setScene(scene);
        stage.show();
    }
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

    @Override
    public void start(Stage stage) {
        
        VBox vbox = new VBox(10); 
        vbox.setStyle("-fx-padding: 20;");

        Button btnCliente = new Button("Cliente");
        Button btnEmpleado = new Button("Empleado");
        Button btnAdministrador = new Button("Proveedor");
        Button btnSalir = new Button("Salir");

        btnCliente.setOnAction(e -> mostrarVentanaLoginCliente());

        btnEmpleado.setOnAction(e -> mostrarMenuEmpleado());

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
