package clases;


import utilidades.ConexionBD;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * Clase que representa un cliente del sistema.
 * Proporciona funcionalidades para consultar productos, aplicar cupones,
 * ver facturas y participar en sorteos mediante interfaces gráficas.
 * 
 * @author Teresa
 * @version 1.0
 */
public class Cliente {
    private String dni;
    private String nombre;
    private String apellidos;
    private String email;
    /**
     * Constructor completo de Cliente.
     * 
     * @param dni       Documento Nacional de Identidad del cliente.
     * @param nombre    Nombre del cliente.
     * @param apellidos Apellidos del cliente.
     * @param email     Correo electrónico del cliente.
     */
    public Cliente(String dni, String nombre, String apellidos, String email) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
    }

    public Cliente() {
    }
    
    public Cliente(String dni) {
    	this.dni = dni;
		// TODO Auto-generated constructor stub
	}

	public String getDni() {
        return dni;
    }
   
    public void setDni(String dni) {
        this.dni = dni;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
/**
 * se registra un nuevo cliente en la base de datos
 * @param dni del cliente String
 * @param nombre del cliente String
 * @param apellidos del cliente String
 * @param email del cliente String*/
    public void registrarNuevoCliente(String dni, String nombre, String apellidos, String email) {
	    String q = "INSERT INTO cliente (dni, nombre, apellidos, email) VALUES (?, ?, ?, ?)";
	    try (Connection c = ConexionBD.obtenerConexion();
	         PreparedStatement pst = c.prepareStatement(q, Statement.RETURN_GENERATED_KEYS)) {
	        pst.setString(1, dni);
	        pst.setString(2, nombre);
	        pst.setString(3, apellidos);
	        pst.setString(4, email);
	        pst.executeUpdate();
	        ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				
				this.dni = dni;
				this.nombre = nombre;
				this.apellidos = apellidos;

			}
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}
    /**
     * Muestra una ventana para registrar un nuevo cliente con campos para DNI, nombre,
     * apellidos y correo electrónico. Incluye un botón para guardar el cliente.
     * Al guardar correctamente, actualiza la lista y la tabla de clientes mostrada.
     *
     * @param lista ObservableList de clientes que se actualizará con el nuevo cliente registrado.
     * @param tabla TableView que mostrará la lista actualizada de clientes.
     */
    public static void mostrarRegistrarCliente(ObservableList<Cliente> lista, TableView<Cliente> tabla) {
    	
	    TextField txtDni = new TextField();
	   
	    TextField txtNombre = new TextField();
	    
	    TextField txtApellidos = new TextField();
	  
	    TextField txtEmail = new TextField();
	    txtDni.setPromptText("DNI...");
	    txtNombre.setPromptText("Nombre...");
	    txtApellidos.setPromptText("Apellidos...");
	    txtEmail.setPromptText("correo...");
Label estado = new Label();
        Button btnGuardar = new Button("Guardar");

        btnGuardar.setOnAction(e -> {
        	String dni = txtDni.getText().trim();
	        String nombre = txtNombre.getText().trim();
	        String Apellidos = txtApellidos.getText().trim();
	        String email = txtEmail.getText();
	        
            if (dni.isEmpty() || nombre.isEmpty() || Apellidos.isEmpty() || email.isEmpty()) {
                
                estado.setText("Completa todos los campos");

                
            } else {
            	Cliente nuevo = new Cliente();
                
                nuevo.registrarNuevoCliente(dni, nombre, Apellidos, email);
                try {
                	 List<Cliente> listaActualizada = Cliente.obtenerClientesRegistrados();
                	 lista.setAll(listaActualizada);
                	 tabla.refresh();
                }catch(SQLException e1) {
                	e1.printStackTrace();
                }
                
                ((Stage) btnGuardar.getScene().getWindow()).close();
            }
        });

        VBox layout = new VBox(10, new Label("Registrar nuevo cliente"), txtDni, txtNombre,txtApellidos,txtEmail, btnGuardar);
        layout.setPadding(new Insets(15));
        Stage ventana = new Stage();
        ventana.setTitle("Registrar Cliente");
        ventana.setScene(new Scene(layout, 300, 200));
        ventana.show();
    }
    /**
     * Obtiene la lista de clientes ya registrados en la base de datos.
     *
     * @return Lista con todos los clientes registrados.
     * @throws SQLException Si ocurre un error al realizar la consulta a la base de datos.
     */
    public static List<Cliente> obtenerClientesRegistrados() throws SQLException {
	    List<Cliente> historial = new ArrayList<>();
	    String sql = "select * from cliente";
	    try (Connection c = ConexionBD.obtenerConexion();
	         PreparedStatement pst = c.prepareStatement(sql)) {
	        
	        ResultSet rs = pst.executeQuery();

	        while (rs.next()) {
	           String dni = rs.getString("dni");
	           String nombre = rs.getString("nombre");
	           String apellidos = rs.getString("apellidos");
	           String email = rs.getString("email");
	           historial.add(new Cliente(dni, nombre, apellidos, email));
	        }
	    }

	    return historial;
	}
    /**
     * Muestra una ventana con el catálogo de productos disponibles para la venta,
     * mostrando nombre, precio y stock.
     */
    public void verCatalogoProductos() {
        ListView<String> listaProductos = new ListView<>();

        String q = "select cantidad, id_producto, nombre, precio from vista_productos_en_venta";

        try (Connection c = ConexionBD.obtenerConexion();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(q)) {

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                int cantidad = rs.getInt("cantidad");

                String productoInfo = "Producto: " + nombre +
                                      ", Precio: " + precio +
                                      ", Stock: " + cantidad;
                listaProductos.getItems().add(productoInfo);
            }

        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText("No se pudo cargar el catálogo");
            error.setContentText(e.getMessage());
            error.showAndWait();
            return;
        }

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20;");
        layout.getChildren().addAll(new Label("Catálogo de Productos:"), listaProductos);

        Scene scene = new Scene(layout, 400, 300);
        Stage stage = new Stage();
        stage.setTitle("Catálogo de Productos");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Muestra una ventana para buscar productos por nombre en el catálogo.
     * Se realiza la búsqueda con consulta SQL usando LIKE.
     * 
     * @throws SQLException en caso de error en la consulta a la base de datos.
     */
    public void buscarProductoPorNombre() throws SQLException {
    	Stage ventana = new Stage();
        ventana.setTitle("Buscar Producto por Nombre");

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 15;");

        Label lblInstruccion = new Label("Introduce el nombre del producto:");
        TextField inputNombre = new TextField();
        Button btnBuscar = new Button("Buscar");
        TextArea resultados = new TextArea();
        resultados.setEditable(false);

        btnBuscar.setOnAction(e -> {
            String nombreBuscar = inputNombre.getText().trim();
            if (nombreBuscar.isEmpty()) {
                resultados.setText("Por favor introduce un nombre.");
                return;
            }

            String q = "select id_producto, nombre, precio, total_cantidad from vista_productos_cantidad where nombre like ?";

            try (Connection c = ConexionBD.obtenerConexion();
                 PreparedStatement pst = c.prepareStatement(q)) {

                pst.setString(1, "%" + nombreBuscar + "%");
                ResultSet rs = pst.executeQuery();
                StringBuilder sb = new StringBuilder();
                boolean encontrado = false;

                while (rs.next()) {
                    encontrado = true;
                    sb.append("ID: ").append(rs.getInt("id_producto"))
                      .append(", Nombre: ").append(rs.getString("nombre"))
                      .append(", Precio: ").append(rs.getDouble("precio"))
                      .append(", Stock: ").append(rs.getInt("total_cantidad"))
                      .append("\n");
                }

                if (!encontrado) {
                    resultados.setText("No se encontraron productos con ese nombre.");
                } else {
                    resultados.setText(sb.toString());
                }

            } catch (SQLException ex) {
                resultados.setText("Error al buscar productos:\n" + ex.getMessage());
                ex.printStackTrace();
            }
        });

        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setOnAction(e -> ventana.close());

        layout.getChildren().addAll(lblInstruccion, inputNombre, btnBuscar, resultados, btnCerrar);
        Scene escena = new Scene(layout, 400, 300);
        ventana.setScene(escena);
        ventana.show();
    }
    
    /**
     * Muestra las facturas asociadas al cliente.
     * 
     * @param dniCliente DNI del cliente cuyas facturas se consultan.
     * @throws SQLException en caso de error en la base de datos.
     */
    public void verFacturas(String dniCliente) throws SQLException {
    	 
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        Label titulo = new Label("Facturas del cliente DNI: " + dniCliente);

        TableView<Factura> tabla = new TableView<>();

        TableColumn<Factura, Integer> colId = new TableColumn<>("ID Factura");
        colId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getIdFactura()));

        TableColumn<Factura, LocalDate> colFechaEmision = new TableColumn<>("Fecha Emisión");
        colFechaEmision.setCellValueFactory(new PropertyValueFactory<>("fechaEmision"));

        TableColumn<Factura, Double> colTotal = new TableColumn<>("Total (€)");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        TableColumn<Factura, Double> colTotalDescuento = new TableColumn<>("Total con descuento (€)");
        colTotalDescuento.setCellValueFactory(new PropertyValueFactory<>("total_con_descuento"));
        

        tabla.getColumns().addAll(colId, colFechaEmision, colTotal, colTotalDescuento);

        ObservableList<Factura> facturas = FXCollections.observableArrayList();
        Button btnFactura = new Button("Descargar factura");
        Label estado = new Label();
        btnFactura.setOnAction(e -> {
            Factura facturaSeleccionada = tabla.getSelectionModel().getSelectedItem();

            if (facturaSeleccionada == null) {
                
                estado.setText("Por favor, selecciona una factura.");
                return;
            }

            int idVenta = facturaSeleccionada.getIdFactura(); // Obtenemos el id de la factura

            try {
                Venta.descargarFacturaPdf(idVenta); // Método que usa el procedimiento 'facturacion'
                estado.setText("Factura descargada como factura_" + idVenta + ".pdf");
            } catch (IOException | SQLException ex) {
                ex.printStackTrace();
                estado.setText("Error al descargar la factura.");
            }
        });
        String sql = "select id_factura, fecha_emision, total, total_con_descuento from factura where dni_cliente = ?";

        try {
            
            Connection con = ConexionBD.obtenerConexion();
            PreparedStatement pst = con.prepareStatement(sql);

            
            pst.setString(1, dniCliente);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Factura factura = new Factura(
                    rs.getInt("id_factura"),
                    rs.getDate("fecha_emision").toLocalDate(),
                    rs.getDouble("total"), rs.getDouble("total_con_descuento"),null, sql
                );
                facturas.add(factura);
            }

            rs.close();
            pst.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        tabla.setItems(facturas);

        root.getChildren().addAll(titulo, tabla, btnFactura);

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Facturas del cliente");
        stage.show();
    }
    /**
     * Permite al cliente participar en un sorteo,
     * asignando un resultado (ganador o perdedor) y un premio si corresponde.
     * 
     * @param dniCliente DNI del cliente participante.
     * @throws SQLException en caso de error en la base de datos.
     */
   public void participarEnSorteo() throws SQLException {
	   Stage stage = new Stage();
	   VBox v = new VBox(10);
	   v.setPadding(new Insets(15));
	   
	   Label titulo = new Label("Sorteos disponibles para" + this.nombre);
	   ;
	   ListView<Sorteo> listSorteo = new ListView<Sorteo>();
	   Button btnParticipar = new Button("Participar");
	   Label estado = new Label();
	   Label lblHistorial = new Label("Historial de sorteos:");
	    ListView<String> listHistorial = new ListView<>();
	   listSorteo.getItems().addAll(Sorteo.obtenerSorteosDisponibles(dni))	;
	   
	   listHistorial.getItems().addAll(Sorteo.obtenerHistorialParticipacion(dni));
	   btnParticipar.setOnAction(e ->{
		   Sorteo seleccion = listSorteo.getSelectionModel().getSelectedItem();
		   if(seleccion == null) {
			   estado.setText("Selecciona un sorteo primero");
			   
		   }else {
		   String resultado = Math.random()< 0.1 ? "GANADOR":"PERDEDOR";
		   String sql = "update sorteo set resultado = ? where id_sorteo = ?";
		   try(Connection c = ConexionBD.obtenerConexion();
				   PreparedStatement pst = c.prepareStatement(sql)){
			   pst.setString(1, resultado);
			   pst.setInt(2, seleccion.getIdSorteo());
			   int filas =pst.executeUpdate();
			   if (filas > 0) {
				   estado.setText("Participaste como " + resultado.toLowerCase() + " en el sorteo " + seleccion.getIdSorteo());
				   listSorteo.getItems().remove(seleccion);
				   listHistorial.getItems().clear();
	                listHistorial.getItems().addAll(Sorteo.obtenerHistorialParticipacion(dni));
			   }else {
				   estado.setText("No se pudo actualizar el sorteo. ¿Ya participaste?");
			   }
		   } catch (SQLException e1) {
			// TODO Auto-generated catch block
			estado.setText("Error al participar: " + e1.getMessage());
		}
	   }});
	   v.getChildren().addAll(titulo, listSorteo, btnParticipar, estado, lblHistorial, listHistorial);
	   stage.setScene(new Scene(v, 400, 300));
	   stage.setTitle("Participar en sorteos");
	   stage.show();
	   
   }
   
    
   /**
     * Consulta y muestra los cupones de descuento disponibles para el cliente.
     * 
     * @throws SQLException en caso de error en la consulta a la base de datos.
     */
   public void mostrarCuponesDisponibles() {
	    Stage ventana = new Stage();
	    ventana.setTitle("Cupones Disponibles");

	    VBox layout = new VBox(10);
	    layout.setPadding(new Insets(10));

	    ListView<Cupon> listView = new ListView<>();
	    List<Cupon> cupones = Cupon.obtenerCuponesDisponibles();
	    listView.getItems().addAll(cupones);


	    Label lblEstado = new Label();

	    

	    layout.getChildren().addAll(new Label("Seleccione un cupón:"), listView,  lblEstado);
	    Scene escena = new Scene(layout, 400, 300);
	    ventana.setScene(escena);
	    ventana.show();
	}
    /*Comprar productos insertandolo en la cesta y comprar (se puede insertar el cupon )
    * @param dni El DNI del cliente que está realizando la compra.
       */
 
   public void comprarProductos(String dni) {
	    Stage stage = new Stage();
	    VBox v = new VBox(10);
	    v.setStyle("-fx-padding: 15;");

	    Label titulo = new Label("Selecciona productos:");
	    ListView<String> listaProductos = new ListView<>();
	    Map<String, Integer> productoMap = new HashMap<>(); // item string -> idProducto
	    Map<Integer, String> idProductoANombre = new HashMap<>();
	    Map<Integer, Double> idProductoAPrecio = new HashMap<>();

	    // Cargar productos con consulta SELECT normal, no con procedimiento
	    try (Connection c = ConexionBD.obtenerConexion();
	         PreparedStatement pst = c.prepareStatement(
	            "select id_producto, nombre, coalesce(precio_descuento, precio) as precio_final from producto");
	         ResultSet rs = pst.executeQuery()) {

	        while (rs.next()) {
	            int id = rs.getInt("id_producto");
	            String nombre = rs.getString("nombre");
	            double precio = rs.getDouble("precio_final");
	            String item = nombre + " " + String.format("%.2f", precio) + " €";
	            listaProductos.getItems().add(item);
	            productoMap.put(item, id);
	            idProductoANombre.put(id, nombre);
	            idProductoAPrecio.put(id, precio);
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }

	    ListView<String> cesta = new ListView<>();
	    Label labelCesta = new Label("Cesta de productos:");
	    Button btnAgregar = new Button("Agregar a cesta");

	    // Mapa para agrupar productos y cantidades
	    Map<Integer, Integer> cestaMap = new HashMap<>();

	    btnAgregar.setOnAction(e -> {
	        String seleccionado = listaProductos.getSelectionModel().getSelectedItem();
	        if (seleccionado != null) {
	            Stage cantidadStage = new Stage();
	            cantidadStage.initModality(Modality.APPLICATION_MODAL);
	            cantidadStage.setTitle("Seleccionar cantidad");

	            VBox cantidadLayout = new VBox(10);
	            cantidadLayout.setPadding(new Insets(15));

	            Label lbl = new Label("¿Cuántas unidades deseas?");
	            Spinner<Integer> spinner = new Spinner<>(1, 100, 1);

	            Button confirmar = new Button("Agregar");
	            confirmar.setOnAction(ev -> {
	                int cantidad = spinner.getValue();

	                Integer idProducto = productoMap.get(seleccionado);
	                if (idProducto == null) {
	                    cantidadStage.close();
	                    return;
	                }

	              
	                cestaMap.put(idProducto, cestaMap.getOrDefault(idProducto, 0) + cantidad);

	                
	                cesta.getItems().clear();
	                for (Map.Entry<Integer, Integer> entry : cestaMap.entrySet()) {
	                    String nombreProducto = idProductoANombre.get(entry.getKey());
	                    double precioProducto = idProductoAPrecio.get(entry.getKey());
	                    String itemTexto = nombreProducto + " " + String.format("%.2f", precioProducto) + " € x" + entry.getValue();
	                    cesta.getItems().add(itemTexto);
	                }

	                cantidadStage.close();
	            });

	            cantidadLayout.getChildren().addAll(lbl, spinner, confirmar);
	            Scene scene = new Scene(cantidadLayout, 250, 120);
	            cantidadStage.setScene(scene);
	            cantidadStage.showAndWait();
	        }
	    });

	    CheckBox usarCupon = new CheckBox("Usar cupón de descuento");
	    Label estado = new Label();

	    Button btnConfirmar = new Button("Confirmar compra");
	    btnConfirmar.setOnAction(e -> {
	        if (cestaMap.isEmpty()) {
	            estado.setText("La cesta está vacía.");
	            return;
	        }

	        Stage dialog = new Stage();
	        dialog.initModality(Modality.APPLICATION_MODAL);
	        dialog.setTitle("Método de Pago y Envío");

	        VBox layout = new VBox(10);
	        layout.setPadding(new Insets(15));

	        Label lblPago = new Label("Método de pago:");
	        ComboBox<String> comboPago = new ComboBox<>();
	        comboPago.getItems().addAll("TARJETA", "EFECTIVO", "DESPUES DE ENTREGA", "PAYPAL", "BIZUM");
	        comboPago.getSelectionModel().selectFirst();

	        Label lblEnvio = new Label("Método de envío:");
	        ComboBox<String> comboEnvio = new ComboBox<>();
	        comboEnvio.getItems().addAll("24H", "3 A 5 DIAS", "7 A 12 DIAS", "URGENTE", "RECOGIDA EN TIENDA");
	        comboEnvio.getSelectionModel().selectFirst();

	        Button btnFinalizar = new Button("Finalizar compra");
	        btnFinalizar.setOnAction(ev -> {
	            dialog.close();
	            String metodoPago = comboPago.getValue();
	            String metodoEnvio = comboEnvio.getValue();

	            try (Connection c2 = ConexionBD.obtenerConexion()) {
	                int cuponId = -1;
	                boolean usar = usarCupon.isSelected();

	                if (usar) {
	                    String sqlCupon = "Select obtener_cupon_disponible()";
	                    try (PreparedStatement pstCupon = c2.prepareStatement(sqlCupon);
	                         ResultSet rsCupon = pstCupon.executeQuery()) {
	                        if (rsCupon.next()) {
	                        	cuponId = rsCupon.getInt(1); 
	                        } else {
	                            estado.setText("No tienes cupones activos disponibles.");
	                            return;
	                        }
	                    }
	                }

	                String insertVenta = "INSERT INTO venta (cliente_dni, fecha_venta, total, metodo_pago, metodo_envio, coste_envio) " +
	                        "VALUES (?, CURDATE(), 0, ?, ?, 0.00)";
	                int idVenta;
	                try (PreparedStatement pstVenta = c2.prepareStatement(insertVenta, Statement.RETURN_GENERATED_KEYS)) {
	                    pstVenta.setString(1, dni);
	                    pstVenta.setString(2, metodoPago);
	                    pstVenta.setString(3, metodoEnvio);
	                    pstVenta.executeUpdate();

	                    try (ResultSet rsVenta = pstVenta.getGeneratedKeys()) {
	                        if (rsVenta.next()) {
	                            idVenta = rsVenta.getInt(1);
	                        } else {
	                            estado.setText("No se pudo obtener el ID de venta.");
	                            return;
	                        }
	                    }
	                }

	                String insertProd = "INSERT INTO producto_en_venta (venta_id, producto_id, cantidad) VALUES (?, ?, ?)";
	                try (PreparedStatement pstProd = c2.prepareStatement(insertProd)) {
	                    for (Map.Entry<Integer, Integer> entry : cestaMap.entrySet()) {
	                        pstProd.setInt(1, idVenta);
	                        pstProd.setInt(2, entry.getKey());
	                        pstProd.setInt(3, entry.getValue());
	                        pstProd.executeUpdate();
	                    }
	                }

	    
	                String getIdSql = "SELECT dni FROM cliente WHERE dni = ?";
	                
	                try (PreparedStatement pst = c2.prepareStatement(getIdSql)) {
	                    pst.setString(1, this.dni);
	                    try (ResultSet rs = pst.executeQuery()) {
	                        if (rs.next()) {
	                             rs.getString("dni");
	                        } else {
	                            estado.setText("Cliente no encontrado.");
	                            return;
	                        }
	                    }
	                }

	                String call = "{CALL finalizar_venta(?, ?, ?)}";
	                try (CallableStatement cs = c2.prepareCall(call)) {
	                    cs.setInt(1, idVenta); 
	                    if (cuponId != -1) {
	                        cs.setInt(2, cuponId);
	                        cs.setBoolean(3, true);
	                    } else {
	                        cs.setNull(2, Types.INTEGER);
	                        cs.setBoolean(3, false);
	                    }

	                    try (ResultSet rs = cs.executeQuery()) {
	                        if (rs.next()) {
	                            int id = rs.getInt("id_venta");
	                            double total = rs.getDouble("total");
	                            double totalConDescuento = rs.getDouble("total_con_descuento");

	                            estado.setText(String.format("Venta #%d registrada. Total: %.2f€, Final: %.2f€", id, total, totalConDescuento));
	                        }
	                    }
	                }


	                cestaMap.clear();
	                cesta.getItems().clear();

	            } catch (SQLException ex) {
	                ex.printStackTrace();
	                estado.setText("Error al realizar la compra.");
	            }
	        });

	        layout.getChildren().addAll(lblPago, comboPago, lblEnvio, comboEnvio, btnFinalizar);
	        Scene scene = new Scene(layout, 300, 200);
	        dialog.setScene(scene);
	        dialog.showAndWait();
	    });

	    Button btnCerrar = new Button("Cerrar");
	    btnCerrar.setOnAction(e -> stage.close());

	    v.getChildren().addAll(
	            titulo,
	            listaProductos,
	            btnAgregar,
	            labelCesta,
	            cesta,
	            usarCupon,
	            btnConfirmar,
	            estado,
	            btnCerrar
	    );

	    Scene scene = new Scene(v, 400, 550);
	    stage.setScene(scene);
	    stage.setTitle("Compra de Productos");
	    stage.show();
	}
   /**
    * Elimina un cliente de la base de datos según su DNI.
    *
    * @param dni DNI del cliente que se desea eliminar.
    */
    public static void eliminarClienteBD(String dni) {
        String sql = "delete from cliente where dni = ?";
        try (Connection c = ConexionBD.obtenerConexion();
             PreparedStatement pst = c.prepareStatement(sql)) {
            pst.setString(1, dni);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
   
    
    
    
}
