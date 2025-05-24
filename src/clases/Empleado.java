package clases;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.io.IOException;

import clases.Venta.MetodoEnvio;
import clases.Venta.MetodoPago;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utilidades.ConexionBD;

public class Empleado {
private int idEmpleado;
private String usuario;
private String contraseña;

public Empleado(int idEmpleado, String usuario, String contraseña) {
	this.idEmpleado = idEmpleado;
	this.usuario = usuario;
	this.contraseña = contraseña;
}
public Empleado(String usuario, String contraseña) {
	this.usuario = usuario;
	this.contraseña = contraseña;
}

public Empleado() {}
public int getIdEmpleado() {
	return idEmpleado;
}
public void setIdEmpleado(int idEmpleado) {
	this.idEmpleado = idEmpleado;
}
public String getUsuario() {
	return usuario;
}
public void setUsuario(String usuario) {
	this.usuario = usuario;
}

public String getContraseña() {
	return contraseña;
}
public void setContraseña(String contraseña) {
	this.contraseña = contraseña;
}
/**
 * Registra un nuevo empleado en la base de datos.
 *
 * @param user  El nombre de usuario del empleado.
 * @param contrasenya La contraseña del empleado.
 * @return El ID generado para el nuevo empleado si el registro es exitoso; -1 en caso de error.
 */
public int registrarNuevoEmpleado(String user, String contrasenya) {
	String q = "INSERT INTO empleado (usuario, contrasenya) VALUES (?, ?)";

	try (Connection c = ConexionBD.obtenerConexion();
			PreparedStatement pst = c.prepareStatement(q, Statement.RETURN_GENERATED_KEYS)) {

		pst.setString(1, user);
		pst.setString(2, contrasenya);
		

		pst.executeUpdate();

		ResultSet rs = pst.getGeneratedKeys();
		if (rs.next()) {
			this.idEmpleado = rs.getInt(1);
			this.usuario = user;
			this.contraseña = contrasenya;
			

			System.out.println("Empleado registrado con ID: " + this.idEmpleado);
			return this.idEmpleado;
		}
	} catch (SQLException ex) {
		ex.printStackTrace();
	}

	return -1;
}
/**
 * Muestra la interfaz para gestionar los clientes registrados, incluyendo búsqueda, registro y eliminación.
 *
 * @throws SQLException Si ocurre un error en la consulta o acceso a la base de datos.
 */
@SuppressWarnings("unchecked")
public void gestionarClientes() throws SQLException {
    Label lblTitulo = new Label("Gestión de Clientes");

    TextField txtBuscar = new TextField();
    txtBuscar.setPromptText("Buscar por DNI");

    Label estado = new Label();

    TableView<Cliente> tabla = new TableView<>();

    TableColumn<Cliente, String> colDni = new TableColumn<>("DNI");
    colDni.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDni()));

    TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
    colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));

    TableColumn<Cliente, String> colApellidos = new TableColumn<>("Apellidos");
    colApellidos.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getApellidos()));

    TableColumn<Cliente, String> colEmail = new TableColumn<>("Email");
    colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));

    tabla.getColumns().addAll(colDni, colNombre, colApellidos, colEmail);

    ObservableList<Cliente> listaClientes = FXCollections.observableArrayList(Cliente.obtenerClientesRegistrados());
    tabla.setItems(listaClientes);

    txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> {
        if (newVal == null || newVal.isEmpty()) {
            tabla.setItems(listaClientes);
        } else {
            ObservableList<Cliente> filtrados = FXCollections.observableArrayList();
            for (Cliente c : listaClientes) {
                if (c.getDni().toLowerCase().contains(newVal.toLowerCase())) {
                    filtrados.add(c);
                }
            }
            tabla.setItems(filtrados);
        }
    });

    Button btnRegistrar = new Button("Registrar Cliente");
    Button btnEliminar = new Button("Eliminar Cliente");

    btnRegistrar.setOnAction(e -> Cliente.mostrarRegistrarCliente(listaClientes, tabla));

    btnEliminar.setOnAction(e -> {
        Cliente seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Cliente.eliminarClienteBD(seleccionado.getDni());
            listaClientes.remove(seleccionado);
            tabla.setItems(listaClientes);
            tabla.refresh();
            estado.setText("Cliente eliminado correctamente.");
        } else {
            estado.setText("Selecciona un cliente para eliminar.");
        }
    });

    HBox botones = new HBox(10, btnRegistrar, btnEliminar);
    VBox layout = new VBox(10, lblTitulo, txtBuscar, tabla, botones, estado);
    layout.setPadding(new Insets(15));


    Stage stage = new Stage();
    stage.setScene(new Scene(layout, 500, 400));
    stage.setTitle("Gestionar Clientes");
    stage.show();
	
}
/**
 * Abre el formulario para registrar una nueva venta con selección de cliente, productos, métodos de envío y pago.
 */
private void abrirFormularioNuevaVenta() {
    Stage stage = new Stage();
    VBox vbox = new VBox(10);
    vbox.setPadding(new Insets(15));

    Label lblClienteDni = new Label("Cliente DNI:");
    TextField txtClienteDni = new TextField();

    Label lblFecha = new Label("Fecha de Venta:");
    DatePicker dpFecha = new DatePicker(LocalDate.now());

    Label lblEstado = new Label("Estado Pedido:");
    ComboBox<Venta.EstadoPedido> cbEstado = new ComboBox<>();
    cbEstado.getItems().addAll(Venta.EstadoPedido.values());
    cbEstado.getSelectionModel().selectFirst();

    Label lblMetodoEnvio = new Label("Método de Envío:");
    ComboBox<MetodoEnvio> cbMetodoEnvio = new ComboBox<>();
    cbMetodoEnvio.getItems().addAll(MetodoEnvio.values());
    cbMetodoEnvio.setPromptText("Selecciona método de envío");
    cbMetodoEnvio.getSelectionModel().selectFirst();

    Label lblMetodoPago = new Label("Método de Pago:");
    ComboBox<MetodoPago> cbMetodoPago = new ComboBox<>();
    cbMetodoPago.getItems().addAll(MetodoPago.values());
    cbMetodoPago.setPromptText("Selecciona método de pago");
    cbMetodoPago.getSelectionModel().selectFirst();

    Label lblProductos = new Label("Agregar Productos:");
    Label estado = new Label();

    ObservableList<Producto> productosDisponibles = Producto.cargarProductosDisponibles();
    ComboBox<Producto> cbProducto = new ComboBox<>(productosDisponibles);
    cbProducto.setPromptText("Selecciona un producto");

    TextField txtCantidad = new TextField();
    txtCantidad.setPromptText("Cantidad");

    Button btnAgregarProducto = new Button("Agregar");

    TableView<Producto_En_Venta> tablaProductos = new TableView<>();
    TableColumn<Producto_En_Venta, String> colNombre = new TableColumn<>("Producto");
    colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProducto().getNombre()));

    TableColumn<Producto_En_Venta, String> colCantidad = new TableColumn<>("Cantidad");
    colCantidad.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getCantidad())));

    tablaProductos.getColumns().addAll(colNombre, colCantidad);
    ObservableList<Producto_En_Venta> listaProductosVenta = FXCollections.observableArrayList();
    tablaProductos.setItems(listaProductosVenta);

    btnAgregarProducto.setOnAction(e -> {
        Producto producto = cbProducto.getSelectionModel().getSelectedItem();
        if (producto == null) {
            estado.setText("Error: Selecciona un producto");
            return;
        }
        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText());
            if (cantidad <= 0) {
                estado.setText("Error: Cantidad debe ser mayor que cero");
                return;
            }
        } catch (NumberFormatException ex) {
            estado.setText("Error: Cantidad inválida");
            return;
        }
        listaProductosVenta.add(new Producto_En_Venta(producto, cantidad));
        cbProducto.getSelectionModel().clearSelection();
        txtCantidad.clear();
    });

    Button btnProcesar = new Button("Procesar Venta");
    btnProcesar.setOnAction(e -> {
        String dni = txtClienteDni.getText().trim();
        if (dni.isEmpty()) {
            estado.setText("Error: Debes ingresar DNI del cliente");
            return;
        }
        if (listaProductosVenta.isEmpty()) {
            estado.setText("Error: Agrega al menos un producto");
            return;
        }

        Cliente cliente = new Cliente(dni, "", "", "");
        LocalDate fecha = dpFecha.getValue();
        Venta.EstadoPedido estadoP = cbEstado.getSelectionModel().getSelectedItem();
        MetodoEnvio metodoEnvio = cbMetodoEnvio.getSelectionModel().getSelectedItem();
        MetodoPago metodoPago = cbMetodoPago.getSelectionModel().getSelectedItem();

        double total = 0;
        for (Producto_En_Venta p : listaProductosVenta) {
            total += p.getProducto().getPrecio() * p.getCantidad();
        }

        
        double costeEnvio = 0;
        switch (metodoEnvio) {
            case VEINTICUATRO_HORAS:
            	costeEnvio =10.0;
            	break;
            case TRES_A_CINCO_DIAS:
            	costeEnvio =5.0;
            	break;
            case SIETE_A_DOCE_DIAS:
            	costeEnvio =2.0;
            	break;
        };

        
        Venta nuevaVenta = new Venta(0, fecha, total, estadoP, cliente, metodoPago, metodoEnvio);
        nuevaVenta.setMetodoEnvio(metodoEnvio);
        nuevaVenta.setMetodoPago(metodoPago);
        nuevaVenta.setCosteEnvio(costeEnvio);

        try {
            
        	Empleado empleado = this;
        	int idEmpleado = empleado.getIdEmpleado(); // o como sea que tengas el empleado logueado
        	int idVenta = procesarVenta(nuevaVenta, new ArrayList<>(listaProductosVenta), idEmpleado);
            nuevaVenta.setIdVenta(idVenta);

            
            try (Connection con = ConexionBD.obtenerConexion();
                 PreparedStatement psFactura = con.prepareStatement(
                         "INSERT INTO factura (venta_id, fecha_emision, total) VALUES (?, ?, ?)")) {

                psFactura.setInt(1, idVenta);
                psFactura.setDate(2, Date.valueOf(LocalDate.now()));
                psFactura.setDouble(3, nuevaVenta.getTotal());
                psFactura.executeUpdate();
            }

            estado.setText("Éxito: Venta y factura creadas correctamente");
            stage.close();
        } catch (SQLException ex) {
            estado.setText("Error: Error al procesar venta o crear factura: " + ex.getMessage());
            ex.printStackTrace();
        }
    });

    HBox agregarProductoHBox = new HBox(10, cbProducto, txtCantidad, btnAgregarProducto);
    VBox.setMargin(agregarProductoHBox, new Insets(10, 0, 10, 0));

    vbox.getChildren().addAll(
        lblClienteDni, txtClienteDni,
        lblFecha, dpFecha,
        lblEstado, cbEstado,
        lblMetodoEnvio, cbMetodoEnvio,
        lblMetodoPago, cbMetodoPago,
        lblProductos,
        agregarProductoHBox,
        tablaProductos,
        estado,
        btnProcesar
    );

    stage.setScene(new Scene(vbox, 500, 600));
    stage.setTitle("Nueva Venta");
    stage.show();
}

/**
 * Muestra la interfaz para gestionar pedidos, permite buscar, actualizar, crear, editar y ver detalles de pedidos.
 */
public void mostrarPedidos() {
    Label lblTitulo = new Label("Gestión de Pedidos");
    TextField txtBuscar = new TextField();
    txtBuscar.setPromptText("Buscar por cliente DNI");
    Label estado = new Label();

    TableView<Venta> tabla = new TableView<>();

    TableColumn<Venta, String> colId = new TableColumn<>("ID Venta");
    colId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getIdVenta())));

    TableColumn<Venta, String> colFecha = new TableColumn<>("Fecha");
    colFecha.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFecha_venta().toString()));

    TableColumn<Venta, String> colTotal = new TableColumn<>("Total");
    colTotal.setCellValueFactory(data -> new SimpleStringProperty(String.format("%.2f", data.getValue().getTotal())));

    TableColumn<Venta, String> colEstado = new TableColumn<>("Estado");
    colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstadoPedido().name()));

    TableColumn<Venta, String> colCliente = new TableColumn<>("Cliente DNI");
    colCliente.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCliente().getDni()));

    tabla.getColumns().addAll(colId, colFecha, colTotal, colEstado, colCliente);

    try {
        List<Venta> ventas = revisarPedidos();
        System.out.println("Ventas obtenidas: " + ventas.size());
        ObservableList<Venta> listaVentas = FXCollections.observableArrayList(ventas);
        tabla.setItems(listaVentas);

        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                tabla.setItems(listaVentas);
            } else {
                ObservableList<Venta> filtrados = FXCollections.observableArrayList();
                for (Venta v : listaVentas) {
                    if (v.getCliente().getDni().toLowerCase().contains(newValue.toLowerCase())) {
                        filtrados.add(v);
                    }
                }
                tabla.setItems(filtrados);
            }
        });

    } catch (SQLException e) {
        estado.setText("Error al cargar pedidos: " + e.getMessage());
        e.printStackTrace();
    }

    Button btnRefrescar = new Button("Actualizar Lista");
    btnRefrescar.setOnAction(e -> {
        try {
        	System.out.println("ID Empleado en revisarPedidos: " + idEmpleado);
            List<Venta> ventasNueva = revisarPedidos();
            System.out.println("Ventas refrescadas: " + ventasNueva.size());
            ObservableList<Venta> listaVentas = FXCollections.observableArrayList(ventasNueva);
            tabla.setItems(listaVentas);
            estado.setText("Lista actualizada.");
        } catch (SQLException ex) {
            estado.setText("Error al actualizar lista: " + ex.getMessage());
            ex.printStackTrace();
        }
    });

    Button btnCrearPedido = new Button("Crear Pedido");
    btnCrearPedido.setOnAction(e -> {
        
        abrirFormularioNuevaVenta();
    });
  

    Button btnEditar = new Button("Editar Pedido");
    btnEditar.setOnAction(e -> {
        Venta ventaSeleccionada = tabla.getSelectionModel().getSelectedItem();
        if (ventaSeleccionada == null) {
            estado.setText("Selecciona un pedido para editar.");
            return;
        }

       
        if (ventaSeleccionada.getEstadoPedido() != Venta.EstadoPedido.PENDIENTE) {
            estado.setText("Solo se pueden editar pedidos con estado PENDIENTE.");
            return;
        }

        Stage stage = new Stage();
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        Label lblClienteDni = new Label("Cliente DNI:");
        TextField txtClienteDni = new TextField(ventaSeleccionada.getCliente().getDni());
        txtClienteDni.setDisable(true); 

        Label lblFecha = new Label("Fecha de Venta:");
        DatePicker dpFecha = new DatePicker(ventaSeleccionada.getFecha_venta());

        Label lblEstado = new Label("Estado Pedido:");
        ComboBox<Venta.EstadoPedido> cbEstado = new ComboBox<>();
        cbEstado.getItems().addAll(Venta.EstadoPedido.values());
        cbEstado.getSelectionModel().select(ventaSeleccionada.getEstadoPedido());

        Label lblMetodoEnvio = new Label("Método de Envío:");
        ComboBox<MetodoEnvio> cbMetodoEnvio = new ComboBox<>();
        cbMetodoEnvio.getItems().addAll(MetodoEnvio.values());
        cbMetodoEnvio.getSelectionModel().select(ventaSeleccionada.getMetodoEnvio());

        Label lblMetodoPago = new Label("Método de Pago:");
        ComboBox<MetodoPago> cbMetodoPago = new ComboBox<>();
        cbMetodoPago.getItems().addAll(MetodoPago.values());
        cbMetodoPago.getSelectionModel().select(ventaSeleccionada.getMetodoPago());

        Button btnGuardar = new Button("Guardar Cambios");
        Label lblEstadoEdicion = new Label();

        btnGuardar.setOnAction(ev -> {
            LocalDate nuevaFecha = dpFecha.getValue();
            Venta.EstadoPedido nuevoEstado = cbEstado.getSelectionModel().getSelectedItem();
            MetodoEnvio nuevoMetodoEnvio = cbMetodoEnvio.getSelectionModel().getSelectedItem();
            MetodoPago nuevoMetodoPago = cbMetodoPago.getSelectionModel().getSelectedItem();

            if (nuevaFecha == null || nuevoEstado == null || nuevoMetodoEnvio == null || nuevoMetodoPago == null) {
                lblEstadoEdicion.setText("Por favor, completa todos los campos.");
                return;
            }

            ventaSeleccionada.setFecha_venta(nuevaFecha);
            ventaSeleccionada.setEstadoPedido(nuevoEstado);
            ventaSeleccionada.setMetodoEnvio(nuevoMetodoEnvio);
            ventaSeleccionada.setMetodoPago(nuevoMetodoPago);

            try {
            	ventaSeleccionada.setEmpleado(this);
                ventaSeleccionada.actualizarVenta();
                lblEstadoEdicion.setText("Venta actualizada correctamente.");
                stage.close();

            } catch (SQLException ex) {
                lblEstadoEdicion.setText("Error al actualizar la venta: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        vbox.getChildren().addAll(
            lblClienteDni, txtClienteDni,
            lblFecha, dpFecha,
            lblEstado, cbEstado,
            lblMetodoEnvio, cbMetodoEnvio,
            lblMetodoPago, cbMetodoPago,
            lblEstadoEdicion,
            btnGuardar
        );

        stage.setScene(new Scene(vbox, 400, 400));
        stage.setTitle("Editar Pedido");
        stage.show();
    });

    Button btnVerDetalles = new Button("Ver Detalles");
    btnVerDetalles.setOnAction(e -> {
        Venta ventaSeleccionada = tabla.getSelectionModel().getSelectedItem();
        if (ventaSeleccionada == null) {
            estado.setText("Selecciona un pedido para ver detalles.");
            return;
        }

        Stage detallesStage = new Stage();
        detallesStage.initModality(Modality.APPLICATION_MODAL);
        detallesStage.setTitle("Detalles del Pedido ID: " + ventaSeleccionada.getIdVenta());

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        Label lblId = new Label("ID Venta: " + ventaSeleccionada.getIdVenta());
        Label lblFecha = new Label("Fecha: " + ventaSeleccionada.getFecha_venta());
        Label lblEstado = new Label("Estado: " + ventaSeleccionada.getEstadoPedido());
        String metodoPagoStr = (ventaSeleccionada.getMetodoPago() != null) 
                ? ventaSeleccionada.getMetodoPago().toString() 
                : "No definido";

Label lblMetodoPago = new Label("Método Pago: " + metodoPagoStr);
String metodoEnvioStr = (ventaSeleccionada.getMetodoEnvio() != null) 
? ventaSeleccionada.getMetodoEnvio().getCodigoBD() 
: "No definido";

Label lblMetodoEnvio = new Label("Método Envío: " + metodoEnvioStr);
        Label lblEstadoFactura = new Label(); 

        Button btnDescargarFactura = new Button("Descargar Factura");
        btnDescargarFactura.setOnAction(ev -> {
            try {
                Venta.descargarFacturaPdf(ventaSeleccionada.getIdVenta());
                lblEstadoFactura.setText("Factura PDF creada correctamente.");
                lblEstadoFactura.setTextFill(Color.GREEN);
            } catch (IOException | SQLException | java.io.IOException ex) {
                lblEstadoFactura.setText("Error al descargar la factura: " + ex.getMessage());
                lblEstadoFactura.setTextFill(Color.RED);
                ex.printStackTrace();
            }
        });

        root.getChildren().addAll(lblId, lblFecha, lblEstado, lblMetodoPago, lblMetodoEnvio, btnDescargarFactura, lblEstadoFactura);

        Scene scene = new Scene(root, 350, 270);
        detallesStage.setScene(scene);
        detallesStage.show();
    });

    HBox botones = new HBox(10, btnRefrescar,btnCrearPedido, btnEditar, btnVerDetalles);
    botones.setPadding(new Insets(10, 0, 10, 0));

    VBox layout = new VBox(10, lblTitulo, txtBuscar, tabla, botones, estado);
    layout.setPadding(new Insets(15));

    Stage stage = new Stage();
    stage.setScene(new Scene(layout, 700, 450));
    stage.setTitle("Gestión de Pedidos");
    stage.show();
}

/**
 * Obtiene la lista de pedidos asociados al empleado actual, usando un procedimiento almacenado en la base de datos.
 *
 * @return Lista de objetos {@link Venta} con los pedidos encontrados.
 * @throws SQLException Si ocurre un error en la consulta o acceso a la base de datos.
 */
public List<Venta> revisarPedidos() throws SQLException {
    List<Venta> ventas = new ArrayList<>();
    String sql = "{call revisarPedidos(?)}";
    System.out.println("Pedidos cargados en revisarPedidos(): " + ventas.size());
    try (Connection c = ConexionBD.obtenerConexion();
         PreparedStatement pst = c.prepareStatement(sql)) {
        pst.setInt(1, idEmpleado);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Cliente cliente = new Cliente(
                rs.getString("dni"),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getString("email")
            );

            Venta.EstadoPedido estado = Venta.EstadoPedido.valueOf(rs.getString("estado_pedido"));
            MetodoPago metodoPago = MetodoPago.desdeTexto(rs.getString("metodo_pago"));
            MetodoEnvio metodoEnvio = MetodoEnvio.fromCodigoBD(rs.getString("metodo_envio"));
            Venta venta = new Venta(
                rs.getInt("id_venta"),
                rs.getDate("fecha_venta").toLocalDate(),
                rs.getDouble("total"),
                estado,
                cliente,
                metodoPago,
                metodoEnvio
            );

            ventas.add(venta);
        }
    }
    return ventas;
}
/**
 * Procesa una venta insertándola en la base de datos con sus productos.
 *
 * @param venta       Objeto {@link Venta} con los datos de la venta a procesar.
 * @param productos   Lista de {@link Producto_En_Venta} con los productos y cantidades de la venta.
 * @param idEmpleado  ID del empleado que procesa la venta.
 * @return El ID generado para la venta en la base de datos, o -1 si hubo un error.
 * @throws SQLException Si ocurre un error en la inserción de datos en la base de datos.
 */
public static int procesarVenta(Venta venta, List<Producto_En_Venta> productos, int idEmpleado) throws SQLException {
    int idVentaGenerada = -1;
    String sqlVenta = "INSERT INTO venta (total, cliente_dni, metodo_pago, metodo_envio, empleado_id) VALUES (?, ?, ?, ?, ?)";

    try (Connection con = ConexionBD.obtenerConexion();
         PreparedStatement psVenta = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {

        psVenta.setDouble(1, venta.getTotal());
        psVenta.setString(2, venta.getCliente().getDni());
        psVenta.setString(3, venta.getMetodoPago().toString());
        psVenta.setString(4, venta.getMetodoEnvio().getCodigoBD());
        psVenta.setInt(5, idEmpleado);

        psVenta.executeUpdate();

        try (ResultSet rs = psVenta.getGeneratedKeys()) {
            if (rs.next()) {
                idVentaGenerada = rs.getInt(1);
                venta.setIdVenta(idVentaGenerada);
            }
        }

        String sqlDetalle = "INSERT INTO producto_en_venta (venta_id, producto_id, cantidad) VALUES (?, ?, ?)";
        try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle)) {
            for (Producto_En_Venta pe : productos) {
                psDetalle.setInt(1, idVentaGenerada);
                psDetalle.setInt(2, pe.getProducto().getIdProducto());
                psDetalle.setInt(3, pe.getCantidad());
                psDetalle.addBatch();
            }
            psDetalle.executeBatch();
        }
    }

    return idVentaGenerada;
}

}

