package clases;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import com.itextpdf.kernel.colors.Lab;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utilidades.ConexionBD;
/*
  Clase que representa a un proveedor en el sistema.
  Contiene la información personal del proveedor y funcionalidades
  para gestionar productos, pedidos y ventas asociados.
 */
public class Proveedor {
private int idProveedor;
private String nombre;
private String apellidos;
private LocalDate fecha_nac;
private String dni ;
private String telefono;
private String email;
private String direccionFiscal;
private String cuentaBancaria;

public Proveedor() {}
/**
 Constructor con ID y nombre.
 @param idProveedor ID del proveedor.
  @param nombre Nombre del proveedor.
 */
public Proveedor(int idProveedor, String nombre) {
	this.idProveedor = idProveedor;
	this.nombre = nombre;
}
/**
 Constructor con varios datos del proveedor.
 @param idAdministrador ID del administrador (no usado en la clase actual).
 @param usuario Usuario (no usado en la clase actual).
 @param contrasenya Contraseña (no usada en la clase actual).
  @param idProveedor ID del proveedor.
  @param nombre Nombre del proveedor.
  @param apellidos Apellidos del proveedor.
  @param fecha_nac Fecha de nacimiento del proveedor.
 */

public Proveedor(int idAdministrador, String usuario, String contrasenya, int idProveedor, String nombre,
		String apellidos, LocalDate fecha_nac) {
	
	this.idProveedor = idProveedor;
	this.nombre = nombre;
	this.apellidos = apellidos;
	this.fecha_nac = fecha_nac;
	
}
// === Métodos Getters y Setters ===

public int getIdProveedor() {
	return idProveedor;
}
public void setIdProveedor(int idProveedor) {
	this.idProveedor = idProveedor;
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
public LocalDate getFecha_nac() {
	return fecha_nac;
}
public void setFecha_nac(LocalDate fecha_nac) {
	this.fecha_nac = fecha_nac;
}

public String getDni() {
	return dni;
}

public void setDni(String dni) {
	this.dni = dni;
}

public String getTelefono() {
	return telefono;
}

public void setTelefono(String telefono) {
	this.telefono = telefono;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public String getDireccionFiscal() {
	return direccionFiscal;
}

public void setDireccionFiscal(String direccionFiscal) {
	this.direccionFiscal = direccionFiscal;
}

public String getCuentaBancaria() {
	return cuentaBancaria;
}

public void setCuentaBancaria(String cuentaBancaria) {
	this.cuentaBancaria = cuentaBancaria;
}
/**
 * Registra un nuevo proveedor en la base de datos.
 * @param dni DNI del proveedor.
 * @param nombre Nombre del proveedor.
 * @param apellidos Apellidos del proveedor.
 * @return ID generado del nuevo proveedor o -1 si falla.
 */
public int registrarNuevoProveedor(String dni, String nombre, String apellidos) {
	String q = "INSERT INTO proveedor (dni, nombre, apellidos) VALUES (?, ?, ?)";

    try (Connection c = ConexionBD.obtenerConexion();
         PreparedStatement pst = c.prepareStatement(q, Statement.RETURN_GENERATED_KEYS)) {

        pst.setString(1, dni);
        pst.setString(2, nombre);
        pst.setString(3, apellidos);

        pst.executeUpdate();

        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()) {
            this.idProveedor = rs.getInt(1); 
            this.dni = dni;
            this.nombre = nombre;
            this.apellidos = apellidos;
            
            System.out.println("Proveedor registrado con ID: " + this.idProveedor);
            return this.idProveedor;
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    return -1; 
}

/**
 * Abre una ventana de gestión de productos asociados al proveedor.
 * Permite agregar, modificar y eliminar productos.
 */
public void gestionarProductos() {
	
    Stage ventanaGestionProductos = new Stage();
    ventanaGestionProductos.setTitle("Gestión de Productos");

    // Crear tabla para mostrar los productos
    TableView<Producto> tablaProductos = new TableView<>();
    TableColumn<Producto, Integer> colIdProducto = new TableColumn<>("Id producto");
    colIdProducto.setCellValueFactory(new PropertyValueFactory<>("idProducto")); 

    TableColumn<Producto, String> colNombre = new TableColumn<>("Nombre");
    colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));


    TableColumn<Producto, Integer> colStock = new TableColumn<>("Stock");
    colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

    TableColumn<Producto, Double> colPrecio = new TableColumn<>("Precio");
    colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

    TableColumn<Producto, Boolean> colOnline = new TableColumn<>("Online");
    colOnline.setCellValueFactory(new PropertyValueFactory<>("esOnline"));
    TableColumn<Producto, LocalDate> colFecha = new TableColumn<>("Fecha");
    colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaEntrada"));
    // Añadir las columnas a la tabl
    tablaProductos.getColumns().addAll(colIdProducto,colNombre,colStock, colPrecio, colOnline, colFecha);

    // Botones para gestionar productos
    Button btnAgregarProducto = new Button("Agregar Producto");
    Button btnModificarProducto = new Button("Modificar Producto");
    Button btnEliminarProducto = new Button("Eliminar Producto");
    Button btnVolver = new Button("Volver");

    
    btnAgregarProducto.setOnAction(e -> {
        mostrarFormularioProducto(null, tablaProductos);
    });

    
    btnModificarProducto.setOnAction(e -> {
        Producto productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            mostrarFormularioProducto(productoSeleccionado, tablaProductos);
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Selecciona un producto para modificar.");
            alert.showAndWait();
        }
    });

    // Acción de eliminar producto
    btnEliminarProducto.setOnAction(e -> {
        Producto productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            eliminarProducto(productoSeleccionado);
            actualizarTablaProductos(tablaProductos);
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Selecciona un producto para eliminar.");
            alert.showAndWait();
        }
    });

    // Acción de volver
    btnVolver.setOnAction(e -> ventanaGestionProductos.close());

    // Layout para la ventana
    VBox vbox = new VBox(10);
    vbox.setStyle("-fx-padding: 20;");
    vbox.getChildren().addAll(
        new Label("Gestión de Productos:"),
        tablaProductos,
        btnAgregarProducto,
        btnModificarProducto,
        btnEliminarProducto,
        btnVolver
    );

    
    actualizarTablaProductos(tablaProductos);

    // Crear y mostrar la escena
    Scene escena = new Scene(vbox, 500, 400);
    ventanaGestionProductos.setScene(escena);
    ventanaGestionProductos.show();
}

/**
 * Carga todos los productos asociados al proveedor desde la base de datos.
 * @return Lista observable de productos.
 */
private ObservableList<Producto> cargarProductos() {
	ObservableList<Producto> lista = FXCollections.observableArrayList();

    String sql = "SELECT * FROM producto WHERE proveedor_id = ?";

    try (Connection con = ConexionBD.obtenerConexion();
         PreparedStatement pst = con.prepareStatement(sql)) {

       
        System.out.println("Cargando productos para proveedor: " + this.idProveedor);
        pst.setInt(1, this.idProveedor);

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            Producto p = new Producto(
                rs.getInt("id_producto"),
                rs.getString("nombre"),
                 rs.getInt("stock"),
                rs.getDouble("precio"),
                rs.getBoolean("es_online"), this.idProveedor, rs.getDate("fecha_entrada").toLocalDate()
            );
            lista.add(p);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return lista;
}
/**
 * Modifica los datos de un producto existente.
 * @param idProducto ID del producto.
 * @param nombre Nombre nuevo.
 * @param stock Stock nuevo.
 * @param precio Precio nuevo.
 * @param esOnline Indica si está disponible online.
 */

protected void modificarProducto(int idProducto, String nombre, int stock, double precio, boolean esOnline) {
    String query = "UPDATE producto SET nombre = ?, stock = ?, precio = ?, es_online = ? WHERE id_producto = ?";
    
    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement pst = conexion.prepareStatement(query)) {
        
        pst.setString(1, nombre);
        pst.setInt(2, stock);
        pst.setDouble(3, precio);
        pst.setBoolean(4, esOnline);
        pst.setInt(5, idProducto);
        
        pst.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

/**
 * Actualiza la tabla con los productos más recientes del proveedor.
 * @param tablaProductos Tabla de productos a actualizar.
 */
private void actualizarTablaProductos(TableView<Producto> tablaProductos) {
ObservableList<Producto> listaProductos = cargarProductos(); 
tablaProductos.setItems(listaProductos);
}
   
/**
 * Muestra el formulario para agregar o modificar un producto.
 * @param producto Producto existente o null si se va a crear uno nuevo.
 * @param tablaProductos Tabla de productos a actualizar tras la acción.
 */
private void mostrarFormularioProducto(Producto producto, TableView<Producto> tablaProductos) {
    Stage ventanaFormulario = new Stage();
    ventanaFormulario.setTitle(producto == null ? "Agregar Producto" : "Modificar Producto");

    VBox vbox = new VBox(10);
    vbox.setStyle("-fx-padding: 20;");

    Label lblNombre = new Label("Nombre:");
    TextField txtNombre = new TextField(producto == null ? "" : producto.getNombre());

    Label lblStock = new Label("Stock:");
    TextField txtStock = new TextField(producto == null ? "" : String.valueOf(producto.getStock()));

    Label lblPrecio = new Label("Precio:");
    TextField txtPrecio = new TextField(producto == null ? "" : String.valueOf(producto.getPrecio()));

    Label lblEsOnline = new Label("Es Online:");
    ComboBox<String> cmbEsOnline = new ComboBox<>(FXCollections.observableArrayList("Sí", "No"));
    cmbEsOnline.getSelectionModel().select(producto == null || producto.isEsOnline() ? "Sí" : "No");

    // Aquí podrías añadir más campos para el proveedor, si lo necesitas.

    Button btnGuardar = new Button("Guardar");
    Button btnCancelar = new Button("Cancelar");

    btnGuardar.setOnAction(e -> {
        String nombre = txtNombre.getText().trim();
        int stock = Integer.parseInt(txtStock.getText().trim());
        double precio = Double.parseDouble(txtPrecio.getText().trim());
        boolean esOnline = cmbEsOnline.getSelectionModel().getSelectedItem().equals("Sí");
       
        if (producto == null) {
            agregarProducto(nombre, stock, precio, esOnline);
        } else {
            modificarProducto(producto.getIdProducto(), nombre, stock, precio, esOnline);
        }

        actualizarTablaProductos(tablaProductos);
        ventanaFormulario.close();
    });

    btnCancelar.setOnAction(e -> ventanaFormulario.close());

    vbox.getChildren().addAll(lblNombre, txtNombre, lblStock, txtStock, lblPrecio, txtPrecio, lblEsOnline, cmbEsOnline, btnGuardar, btnCancelar);

    Scene escena = new Scene(vbox, 300, 400);
    ventanaFormulario.setScene(escena);
    ventanaFormulario.show();
}

/**
 * Inserta un nuevo producto en la base de datos.
 * @param nombre Nombre del producto.
 * @param stock Stock disponible.
 * @param precio Precio del producto.
 * @param esOnline Indica si está disponible online.
 */
private void agregarProducto( String nombre, int stock, double precio, boolean esOnline) {
	if (this.idProveedor <= 0) {
        System.out.println("Error: El proveedor aún no ha sido registrado en la base de datos.");
        return;
    }

    String query = "INSERT INTO producto (nombre, stock, precio, es_online, proveedor_id, fecha_entrada) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement pst = conexion.prepareStatement(query)) {

        pst.setString(1, nombre);
        pst.setInt(2, stock);
        pst.setDouble(3, precio);
        pst.setBoolean(4, esOnline);
        pst.setInt(5, this.idProveedor); 
        pst.setDate(6, java.sql.Date.valueOf(LocalDate.now()));

        pst.executeUpdate();
        System.out.println("Producto agregado correctamente.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
/**
 * Elimina un producto de la base de datos.
 * @param producto Producto a eliminar.
 */
private void eliminarProducto(Producto producto) {
    String query = "DELETE FROM producto WHERE id_producto = ?";
    
    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement pst = conexion.prepareStatement(query)) {
        
        pst.setInt(1, producto.getIdProducto());
        pst.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
/**
 * Muestra los pedidos asociados a los productos del proveedor.
 * Permite aplicar filtros y actualizar el estado de los pedidos.
 */
public void verPedidos() {
	 Stage ventana = new Stage();
	    ventana.setTitle("Pedidos de mis productos");

	    TableView<ObservableList<String>> tabla = new TableView<>();
	    ObservableList<ObservableList<String>> datos = FXCollections.observableArrayList();

	    // Filtros
	    DatePicker dpFecha = new DatePicker();
	    ComboBox<String> cbEstadoFiltro = new ComboBox<>();
	    cbEstadoFiltro.getItems().addAll("TODOS", "PENDIENTE", "CONFIRMADO", "RECHAZADO", "ENVIADO", "ENTREGADO", "CANCELADO");
	    cbEstadoFiltro.setValue("TODOS");
	    Button btnFiltrar = new Button("Filtrar");

	    // ComboBox para cambiar estado
	    ComboBox<String> cmbEstados = new ComboBox<>();
	    cmbEstados.getItems().addAll("CONFIRMADO", "RECHAZADO", "ENVIADO", "ENTREGADO", "CANCELADO");
	    cmbEstados.setPromptText("Selecciona nuevo estado");

	    Button btnActualizar = new Button("Actualizar estado");
	    Label lblInfo = new Label();

	    Runnable cargarPedidos = () -> {
	        tabla.getItems().clear();
	        tabla.getColumns().clear();
	        datos.clear();

	        String sql = "{CALL verPedidos(?)}";

	        try (Connection conn = ConexionBD.obtenerConexion();
	             CallableStatement cs = conn.prepareCall(sql)) {

	            cs.setInt(1, this.idProveedor);
	            ResultSet rs = cs.executeQuery();

	            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
	                final int colIndex = i;
	                TableColumn<ObservableList<String>, String> col = new TableColumn<>(rs.getMetaData().getColumnName(i + 1));
	                col.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().get(colIndex)));
	                tabla.getColumns().add(col);
	            }

	            while (rs.next()) {
	                ObservableList<String> fila = FXCollections.observableArrayList();
	                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
	                    fila.add(rs.getString(i));
	                }

	                // Filtro por fecha
	                if (dpFecha.getValue() != null) {
	                    LocalDate fechaPedido = LocalDate.parse(fila.get(1)); // columna 1 = fecha_venta
	                    if (!fechaPedido.equals(dpFecha.getValue())) continue;
	                }

	                // Filtro por estado
	                if (!cbEstadoFiltro.getValue().equals("TODOS")) {
	                    String estadoPedido = fila.get(3); // columna 3 = estado_pedido (ajusta si cambia)
	                    if (!cbEstadoFiltro.getValue().equals(estadoPedido)) continue;
	                }

	                datos.add(fila);
	            }

	            tabla.setItems(datos);

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    };

	    // Botón filtrar
	    btnFiltrar.setOnAction(e -> cargarPedidos.run());

	    // Botón actualizar estado
	    btnActualizar.setOnAction(e -> {
	        ObservableList<String> filaSeleccionada = tabla.getSelectionModel().getSelectedItem();
	        String nuevoEstado = cmbEstados.getValue();

	        if (filaSeleccionada == null || nuevoEstado == null) {
	            lblInfo.setText("Selecciona un pedido y un estado.");
	            return;
	        }

	        int idVenta = Integer.parseInt(filaSeleccionada.get(0)); // columna id_venta

	        String update = "UPDATE venta SET estado_pedido = ? WHERE id_venta = ?";
	        try (Connection con = ConexionBD.obtenerConexion();
	             PreparedStatement pst = con.prepareStatement(update)) {

	            pst.setString(1, nuevoEstado);
	            pst.setInt(2, idVenta);
	            pst.executeUpdate();

	            lblInfo.setText("Estado actualizado.");
	            cargarPedidos.run(); 

	        } catch (SQLException ex) {
	            ex.printStackTrace();
	            lblInfo.setText("Error al actualizar.");
	        }
	    });

	    // Layouts
	    HBox filtros = new HBox(10, new Label("Filtrar por fecha:"), dpFecha,
	                                  new Label("Estado:"), cbEstadoFiltro, btnFiltrar);
	    filtros.setPadding(new Insets(10));

	    VBox vbox = new VBox(10);
	    vbox.setPadding(new Insets(15));
	    vbox.getChildren().addAll(
	        filtros,
	        tabla,
	        new Label("Actualizar estado del pedido:"),
	        cmbEstados,
	        btnActualizar,
	        lblInfo
	    );

	    Scene scene = new Scene(vbox, 800, 550);
	    ventana.setScene(scene);
	    ventana.show();

	    cargarPedidos.run();
	}
/**
 * Muestra el historial de ventas de los productos del proveedor.
 * También muestra estadísticas y permite descargar facturas.
 */
public void gestionVentas() {
    Stage ventana = new Stage();
    ventana.setTitle("Gestión de Ventas");

    TableView<ObservableList<String>> tabla = new TableView<>();
    ObservableList<ObservableList<String>> datos = FXCollections.observableArrayList();

    Label lblTitulo = new Label("Historial de Ventas de tus productos:");
    Label lblEstadisticas = new Label("Estadísticas de Ventas:");
    Label lblTotalVentas = new Label("Total de ventas: ");
    Label lblIngresos = new Label("Ingresos generados: ");
    Label lblProductoTop = new Label("Producto más vendido: ");
    Label lblInfo = new Label();

    Button btnDescargarFactura = new Button("Descargar Factura");

    // Cargar ventas usando procedimiento almacenado
    String sqlVentas = "{CALL ventasProveedor(?)}";

    try (Connection con = ConexionBD.obtenerConexion();
         CallableStatement cs = con.prepareCall(sqlVentas)) {

        cs.setInt(1, this.idProveedor);
        ResultSet rs = cs.executeQuery();

        // Crear columnas dinámicamente
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            final int colIndex = i;
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(rs.getMetaData().getColumnName(i + 1));
            col.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().get(colIndex)));
            tabla.getColumns().add(col);
        }

        // Rellenar tabla
        while (rs.next()) {
            ObservableList<String> fila = FXCollections.observableArrayList();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                fila.add(rs.getString(i));
            }
            datos.add(fila);
        }
        tabla.setItems(datos);

    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Cargar estadísticas con procedimiento almacenado
    String procEstadisticas = "{CALL estadisticasProveedor(?)}";
    try (Connection con = ConexionBD.obtenerConexion();
         CallableStatement cs = con.prepareCall(procEstadisticas)) {

        cs.setInt(1, this.idProveedor);
        ResultSet rs = cs.executeQuery();

        if (rs.next()) {
            lblTotalVentas.setText("Total de ventas: " + rs.getInt("total_ventas"));
            lblIngresos.setText(String.format("Ingresos generados: €%.2f", rs.getDouble("ingresos")));
            lblProductoTop.setText("Producto más vendido: " + rs.getString("producto_mas_vendido"));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Boton descargar factura
    btnDescargarFactura.setOnAction(e -> {
        ObservableList<String> fila = tabla.getSelectionModel().getSelectedItem();
        if (fila == null) {
            lblInfo.setText("Selecciona una fila.");
            return;
        }

        int idVenta = Integer.parseInt(fila.get(0)); // id_venta en primera columna

        try {
            Venta.descargarFacturaPdf(idVenta); // Método que usa el procedimiento 'facturacion'
            lblInfo.setText("Factura descargada como factura_" + idVenta + ".pdf");
        } catch (IOException | SQLException ex) {
            ex.printStackTrace();
            lblInfo.setText("Error al descargar la factura.");
        }
    });

    VBox vbox = new VBox(10);
    vbox.setPadding(new Insets(20));
    vbox.getChildren().addAll(
        lblTitulo,
        tabla,
        lblEstadisticas,
        lblTotalVentas,
        lblIngresos,
        lblProductoTop,
        btnDescargarFactura,
        lblInfo
    );

    Scene scene = new Scene(vbox, 800, 600);
    ventana.setScene(scene);
    ventana.show();
}
/**
 * Permite al proveedor actualizar su perfil mostrando una ventana con los campos editables.
 * Carga los datos actuales desde la base de datos, permite modificar campos y cambiar la imagen.
 * Al guardar, actualiza la base de datos con los nuevos datos y la imagen seleccionada.
 */
public void actualizarPerfil() {
	try {
		cargarDatosDesdeBD();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    Stage ventana = new Stage();
    ventana.setTitle("Actualizar Perfil - Proveedor");

    Label lblNombre = new Label("Nombre:");
    TextField txtNombre = new TextField(this.nombre);

    Label lblApellidos = new Label("Apellidos:");
    TextField txtApellidos = new TextField(this.apellidos);

    Label lblTelefono = new Label("Teléfono:");
    TextField txtTelefono = new TextField(this.telefono);

    Label lblEmail = new Label("Email:");
    TextField txtEmail = new TextField(this.email);

    Label lblDireccionFiscal = new Label("Dirección Fiscal:");
    TextField txtDireccionFiscal = new TextField(this.direccionFiscal);

    Label lblCuentaBancaria = new Label("Cuenta Bancaria:");
    TextField txtCuentaBancaria = new TextField(this.cuentaBancaria);

    Label lblEstado = new Label();

    ImageView verImagen = new ImageView();
    verImagen.setFitWidth(150);
    verImagen.setFitHeight(150);
    verImagen.setPreserveRatio(true);

    try {
        Image imagenActual = cargarImagenProveedor(this.idProveedor);
        if (imagenActual != null) {
            verImagen.setImage(imagenActual);
        } else {
            URL defaultImgUrl = Proveedor.class.getResource("/img/persona.jpeg");
            if (defaultImgUrl != null) {
                verImagen.setImage(new Image(defaultImgUrl.toExternalForm()));
            } else {
                System.err.println("❌ Imagen por defecto no encontrada: /img/persona.jpeg");
            }
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    Button btnCargarImagen = new Button("Cambiar Imagen");
    final File[] archivoImagenSeleccionada = new File[1];

    btnCargarImagen.setOnAction(e -> {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona imagen del proveedor");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File seleccionado = fileChooser.showOpenDialog(ventana);
        if (seleccionado != null) {
            archivoImagenSeleccionada[0] = seleccionado;
            Image nuevaImagen = new Image(seleccionado.toURI().toString());
            verImagen.setImage(nuevaImagen);
        }
    });

    Button btnGuardar = new Button("Guardar cambios");
    btnGuardar.setOnAction(e -> {
        String nombre = txtNombre.getText();
        String email = txtEmail.getText();
        String telefono = txtTelefono.getText();
        String apellidos = txtApellidos.getText();
        String direccionFiscal = txtDireccionFiscal.getText();
        String cuentaBancaria = txtCuentaBancaria.getText();

        if (nombre == null || nombre.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            lblEstado.setText("Nombre y Email son obligatorios.");
            return;
        }

        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.email = email;
        this.direccionFiscal = direccionFiscal;
        this.cuentaBancaria = cuentaBancaria;
        
        String sqlUpdate = "UPDATE proveedor SET nombre = ?, apellidos = ?, telefono = ?, email = ?, direccion_fiscal = ?, cuenta_bancaria = ? WHERE id_proveedor = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement pst = con.prepareStatement(sqlUpdate)) {

            pst.setString(1, this.nombre);
            pst.setString(2, this.apellidos);
            pst.setString(3, this.telefono);
            pst.setString(4, this.email);
            pst.setString(5, this.direccionFiscal);
            pst.setString(6, this.cuentaBancaria);
            pst.setInt(7, this.idProveedor);

            int filas = pst.executeUpdate();

            if (filas > 0) {
            	lblEstado.setText("Perfil actualizado");
            	if (archivoImagenSeleccionada[0] != null) {
                    try (FileInputStream fis = new FileInputStream(archivoImagenSeleccionada[0]);
                         Connection conImg = ConexionBD.obtenerConexion();
                         PreparedStatement pstImg = conImg.prepareStatement("UPDATE proveedor SET imagen = ? WHERE id_proveedor = ?")) {
                        
                        pstImg.setBinaryStream(1, fis, (int) archivoImagenSeleccionada[0].length());
                        pstImg.setInt(2, this.idProveedor);
                        pstImg.executeUpdate();
                        lblEstado.setText(lblEstado.getText() + " Imagen actualizada.");
                    } catch (IOException | SQLException ex) {
                        ex.printStackTrace();
                        lblEstado.setText("Error al actualizar la imagen.");
                    }
                }

            } else {
                lblEstado.setText("No se pudo actualizar el perfil.");
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
            lblEstado.setText("Error al actualizar el perfil.");
        }
    });

    Button btnVolver = new Button("Volver");
    btnVolver.setOnAction(e -> ventana.close());

    VBox layout = new VBox(10,
        lblNombre, txtNombre,
        lblApellidos, txtApellidos,
        lblTelefono, txtTelefono,
        lblEmail, txtEmail,
        lblDireccionFiscal, txtDireccionFiscal,
        lblCuentaBancaria, txtCuentaBancaria,
        verImagen,
        btnCargarImagen,
        btnGuardar, lblEstado,
        btnVolver
    );
    layout.setPadding(new Insets(15));

    Scene scene = new Scene(layout, 400, 600);
    ventana.setScene(scene);
    ventana.show();
}
/**
 * Actualiza la imagen del proveedor en la base de datos.
 * 
 * @param idProveedor ID del proveedor cuyo imagen se actualizará.
 * @param archivoImagen Archivo de imagen que se desea guardar.
 * @throws SQLException Si ocurre un error durante la operación con la base de datos.
 * @throws IOException Si ocurre un error al leer el archivo de imagen.
 */
public void actualizarImagenProveedor(int idProveedor, File archivoImagen) throws SQLException, IOException {
    String sql = "UPDATE proveedor SET imagen = ? WHERE id_proveedor = ?";
    try (Connection con = ConexionBD.obtenerConexion();
         PreparedStatement pst = con.prepareStatement(sql);
         FileInputStream fis = new FileInputStream(archivoImagen)) {
        pst.setBinaryStream(1, fis, (int) archivoImagen.length());
        pst.setInt(2, idProveedor);
        pst.executeUpdate();
    }
    
}
/**
 * Carga la imagen del proveedor desde la base de datos.
 * 
 * @param idProveedor ID del proveedor del cual se desea obtener la imagen.
 * @return Imagen del proveedor como objeto {@link Image}, o null si no existe imagen.
 * @throws SQLException Si ocurre un error en la consulta a la base de datos.
 * @throws IOException Si ocurre un error al leer el flujo de datos de la imagen.
 */
public Image cargarImagenProveedor(int idProveedor) throws SQLException, IOException {
    String sql = "SELECT imagen FROM proveedor WHERE id_proveedor = ?";
    try (Connection con = ConexionBD.obtenerConexion();
         PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setInt(1, idProveedor);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                InputStream is = rs.getBinaryStream("imagen");
                if (is != null) {
                    return new Image(is);
                }
            }
        }
    }
    return null; 
}
/**
 * Carga los datos del proveedor desde la base de datos y actualiza
 * los atributos de la instancia actual.
 * 
 * @throws SQLException Si ocurre un error al consultar la base de datos.
 */
private void cargarDatosDesdeBD() throws SQLException {
    String sql = "SELECT nombre, apellidos, telefono, email, direccion_fiscal, cuenta_bancaria FROM proveedor WHERE id_proveedor = ?";
    try (Connection con = ConexionBD.obtenerConexion();
         PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setInt(1, this.idProveedor);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                this.nombre = rs.getString("nombre");
                this.apellidos = rs.getString("apellidos");
                this.telefono = rs.getString("telefono");
                this.email = rs.getString("email");
                this.direccionFiscal = rs.getString("direccion_fiscal");
                this.cuentaBancaria = rs.getString("cuenta_bancaria");
            }
        }
    }
}
/**
 * Método que permite gestionar promociones y cupones.
 */
public void gestionPromocionesCupones()
{
	
	ListView<String> listCupones = new ListView<String>();
	cargarCuponesActivos(listCupones);
	Stage v = new Stage();
	v.setTitle("Gestión de promociones y cupones.");
	TabPane tb = new TabPane();
	Tab tp = new Tab("Promociones");
	VBox vp = new VBox(10);
	vp.setPadding(new Insets(10));
	
	Label lblCrearPromo = new Label("Crear nueva Promocion");
	TextField txtProducto = new TextField();
	txtProducto.setPromptText("Nombre del producto");
	TextField txtDescuento = new TextField();
	txtDescuento.setPromptText("Descuento (%)");
	Button btnCrearPromo = new Button("Crear promocion");
	Label lblEstado = new Label();
	
	btnCrearPromo.setOnAction(e -> {
		String producto = txtProducto.getText().trim();
		String descuento = txtDescuento.getText().trim();
		
		if(producto.isEmpty() || descuento.isEmpty()) {
			lblEstado.setText("Debe ingresar producto y descuento.");
			return;
		}
		try {
			double descuentoD = Double.parseDouble(descuento);
			if(descuentoD <= 0 || descuentoD > 100) {
				lblEstado.setText("Descuento debe ser entre 0 100");
				return;
			}
			
			String query = "INSERT INTO promocion (proveedor_id, descripcion, descuento, fecha_inicio, fecha_fin, activo) VALUES (?, ?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY), true)";
			try (Connection conexion = ConexionBD.obtenerConexion();
			     PreparedStatement pst = conexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

			    pst.setInt(1, this.idProveedor);
			    pst.setString(2, "promocion para producto " + producto);
			    pst.setDouble(3, descuentoD);
			    int filas = pst.executeUpdate();

			    if (filas > 0) {
			        ResultSet rs = pst.getGeneratedKeys();
			        if (rs.next()) {
			            int idPromocion = rs.getInt(1);

			            
			            String queryCupon = "INSERT INTO cupon (promocion_id, descuento, usado) VALUES (?, ?, false)";
			            try (PreparedStatement pstCupon = conexion.prepareStatement(queryCupon)) {
			                pstCupon.setInt(1, idPromocion);
			                pstCupon.setDouble(2, descuentoD);
			                pstCupon.executeUpdate();
			            }
			        }

			        lblEstado.setText("Promoción creada: " + producto + " con " + descuento + " %");
			        txtProducto.clear();
			        txtDescuento.clear();
			        cargarCuponesActivos(listCupones);
			    } else {
			        lblEstado.setText("Error de creación de promoción");
			    }
			
		        
		    } catch (SQLException e1) {
		        e1.printStackTrace();
		    }
			
			
		}catch(NumberFormatException ex) {
			lblEstado.setText("Descuento debe ser un numero valido");
		}
	});
	Tab tc = new Tab("Cupones Activos");
	VBox vc = new VBox(10);
	
	vc.setPadding(new Insets(10));
	
	cargarCuponesActivos(listCupones);
	vc.getChildren()
.addAll(new Label("Cupones activos: "), listCupones);
	tc.setContent(vc);
	
	vp.getChildren()
.addAll(lblCrearPromo,txtProducto,txtDescuento,btnCrearPromo,lblEstado);
	tp.setContent(vp);
	 tb.getTabs().addAll(tp, tc);

	    Scene scene = new Scene(tb, 500, 400);
	    v.setScene(scene);
	    v.show();
}

private void cargarCuponesActivos(ListView<String> listCupones) {
	listCupones.getItems().clear();
	String sql = "{call cargar_cupones_existentes(?)}";
	try(Connection c = ConexionBD.obtenerConexion();
			PreparedStatement pst = c.prepareStatement(sql)){
		pst.setInt(1, this.idProveedor);
		try(ResultSet rs = pst.executeQuery()) {
		    boolean hayCupones = false;
		    while (rs.next()) {
		        int idCupon = rs.getInt("id_cupon");
		        double descuento = rs.getDouble("descuento");
		        String descripcionP = rs.getString("descripcion");
		        String item = String.format("Cupón %d - %.2f%% descuento (%s)", idCupon, descuento, descripcionP);
		        listCupones.getItems().add(item);
		        hayCupones = true;
		    }
		    if (!hayCupones) {
		        listCupones.getItems().add("No hay cupones activos.");
		    }
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
        listCupones.getItems().add("Error al cargar cupones: " + e.getMessage());

		e.printStackTrace();
	}
}
}


