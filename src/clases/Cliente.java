package clases;


import utilidades.ConexionBD;


import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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
     * Muestra una ventana con el catálogo de productos disponibles para la venta,
     * mostrando nombre, precio y stock.
     */
    public void verCatalogoProductos() {
        ListView<String> listaProductos = new ListView<>();

        String q = "select pev.cantidad, p.id_producto, p.nombre, p.precio " +
                   "from producto_en_venta pev " +
                   "join producto p on pev.producto_id = p.id_producto";

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

            String q = "select p.id_producto, p.nombre, p.precio, sum(pev.cantidad) as total_cantidad " +
                       "from producto p join producto_en_venta pev on p.id_producto = pev.producto_id " +
                       "where p.nombre LIKE ? group by p.id_producto, p.nombre, p.precio";

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
     * Permite al cliente aplicar un cupón de descuento a una compra,
     * verificando su validez y actualizando el estado del cupón.
     * 
     * @param dniCliente DNI del cliente que usa el cupón.
     * @throws SQLException en caso de error en la base de datos.
     */
    public void usarCuponDescuento(String dniCliente) throws SQLException {
    	Stage ventana = new Stage();
        ventana.setTitle("Usar Cupón de Descuento");

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 15;");

        Label lblCodigo = new Label("Código del cupón:");
        TextField inputCodigo = new TextField();

        Label lblMonto = new Label("Monto total de la compra:");
        TextField inputMonto = new TextField();

        Button btnAplicar = new Button("Aplicar Descuento");
        TextArea resultado = new TextArea();
        resultado.setEditable(false);

        btnAplicar.setOnAction(e -> {
            String codigoStr = inputCodigo.getText().trim();
            String montoStr = inputMonto.getText().trim();

            if (codigoStr.isEmpty() || montoStr.isEmpty()) {
                resultado.setText("Debes introducir el código del cupón y el monto de la compra.");
                return;
            }

            try {
                int idCupon = Integer.parseInt(codigoStr);
                double montoCompra = Double.parseDouble(montoStr);

                String q = "select descuento from cupon where id_cupon = ? and cliente_dni = ? and usado = false";

                try (Connection c = ConexionBD.obtenerConexion();
                     PreparedStatement pst = c.prepareStatement(q)) {

                    pst.setInt(1, idCupon);
                    pst.setString(2, dniCliente);

                    try (ResultSet rs = pst.executeQuery()) {
                        if (rs.next()) {
                            double descuento = rs.getDouble("descuento");
                            double montoFinal = montoCompra - (montoCompra * (descuento / 100));

                            StringBuilder sb = new StringBuilder();
                            sb.append("Cupón válido.\n")
                              .append("Descuento aplicado: ").append(descuento).append("%\n")
                              .append("Monto final: ").append(String.format("%.2f", montoFinal)).append("\n");

                            
                            String update = "update cupon set usado = true where id_cupon = ?";
                            try (PreparedStatement pstUpdate = c.prepareStatement(update)) {
                                pstUpdate.setInt(1, idCupon);
                                pstUpdate.executeUpdate();
                                sb.append("Cupón marcado como usado.");
                            }

                            resultado.setText(sb.toString());
                        } else {
                            resultado.setText("Cupón no válido o ya fue usado.");
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                resultado.setText("Código o monto no válidos.");
            } catch (SQLException ex) {
                resultado.setText("Error al acceder a la base de datos:\n" + ex.getMessage());
                ex.printStackTrace();
            }
        });

        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setOnAction(e2 -> ventana.close());

        layout.getChildren().addAll(lblCodigo, inputCodigo, lblMonto, inputMonto, btnAplicar, resultado, btnCerrar);

        Scene escena = new Scene(layout, 400, 350);
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
    	Stage ventana = new Stage();
        ventana.setTitle("Mis Facturas");

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 15;");

        TextArea areaFacturas = new TextArea();
        areaFacturas.setEditable(false);

        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setOnAction(e -> ventana.close());

        layout.getChildren().addAll(new Label("Facturas registradas:"), areaFacturas, btnCerrar);

        Scene escena = new Scene(layout, 500, 400);
        ventana.setScene(escena);
        ventana.show();

        String q = "select f.id_factura, f.venta_id, f.fecha_emision, f.total, " +
                   "v.fecha_venta, v.metodo_pago, v.metodo_envio, v.coste_envio " +
                   "from factura f join venta v on f.venta_id = v.id_venta " +
                   "where f.dni_cliente = ?";

        try (Connection c = ConexionBD.obtenerConexion();
             PreparedStatement pst = c.prepareStatement(q)) {

            pst.setString(1, dniCliente);

            try (ResultSet rs = pst.executeQuery()) {
                StringBuilder sb = new StringBuilder();
                boolean encontrado = false;

                while (rs.next()) {
                    encontrado = true;
                    sb.append("Factura ID: ").append(rs.getInt("id_factura")).append("\n");
                    sb.append("Fecha emisión: ").append(rs.getDate("fecha_emision")).append("\n");
                    sb.append("Total: ").append(rs.getBigDecimal("total")).append(" €\n");
                    sb.append("Venta ID: ").append(rs.getInt("venta_id")).append("\n");
                    sb.append("Fecha venta: ").append(rs.getDate("fecha_venta")).append("\n");
                    sb.append("Método pago: ").append(rs.getString("metodo_pago")).append("\n");
                    sb.append("Método envío: ").append(rs.getString("metodo_envio")).append("\n");
                    sb.append("Coste envío: ").append(rs.getBigDecimal("coste_envio")).append(" €\n");
                    sb.append("---------------------------------------------------\n");
                }

                if (!encontrado) {
                    areaFacturas.setText("No se encontraron facturas para este cliente.");
                } else {
                    areaFacturas.setText(sb.toString());
                }
            }

        } catch (SQLException ex) {
            areaFacturas.setText("Error al cargar facturas:\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }
    /**
     * Permite al cliente participar en un sorteo,
     * asignando un resultado (ganador o perdedor) y un premio si corresponde.
     * 
     * @param dniCliente DNI del cliente participante.
     * @throws SQLException en caso de error en la base de datos.
     */
    public void participarEnSorteo(String dniCliente) throws SQLException {
        if (dniCliente == null || dniCliente.isBlank()) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "El DNI no puede ser nulo ni estar vacío.");
            alerta.show();
            return;
        }

        List<String> premios = Arrays.asList(
            "vale de 10€", "producto gratis", "cupón de envío gratuito",
            "descuento del 50%", "acceso a evento vip", "vale de 25€"
        );

        String sqlSelect = "SELECT resultado FROM sorteo WHERE cliente_dni = ?";
        String sqlUpdate = "UPDATE sorteo SET resultado = ?, premio = ? WHERE cliente_dni = ?";
        String sqlInsert = "INSERT INTO sorteo (resultado, premio, cliente_dni) VALUES (?, ?, ?)";

        try (Connection c = ConexionBD.obtenerConexion();
             PreparedStatement pstSelect = c.prepareStatement(sqlSelect)) {

            pstSelect.setString(1, dniCliente);
            ResultSet rs = pstSelect.executeQuery();

            String resultado;
            String premio;
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Resultado del sorteo");

            if (rs.next()) {
                String resultadoAnterior = rs.getString("resultado");
                if ("NO PARTICIPÓ".equalsIgnoreCase(resultadoAnterior)) {
                    resultado = Math.random() < 0.3 ? "GANADOR" : "PERDEDOR";
                    premio = resultado.equals("GANADOR") ? premios.get(new Random().nextInt(premios.size())) : null;

                    try (PreparedStatement pstUpdate = c.prepareStatement(sqlUpdate)) {
                        pstUpdate.setString(1, resultado);
                        pstUpdate.setString(2, premio);
                        pstUpdate.setString(3, dniCliente);
                        pstUpdate.executeUpdate();
                    }

                    alerta.setHeaderText("¡Participación actualizada!");
                } else {
                    resultado = resultadoAnterior;
                    premio = obtenerPremioExistente(dniCliente, c);
                    alerta.setHeaderText("Ya has participado en el sorteo");
                }
            } else {
                resultado = Math.random() < 0.3 ? "GANADOR" : "PERDEDOR";
                premio = resultado.equals("GANADOR") ? premios.get(new Random().nextInt(premios.size())) : null;

                try (PreparedStatement pstInsert = c.prepareStatement(sqlInsert)) {
                    pstInsert.setString(1, resultado);
                    pstInsert.setString(2, premio);
                    pstInsert.setString(3, dniCliente);
                    pstInsert.executeUpdate();
                }

                alerta.setHeaderText("¡Participación registrada!");
            }

            alerta.setContentText("Resultado: " + resultado + "\nPremio: " + (premio != null ? premio : "Ninguno"));
            alerta.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR, "Error al participar en el sorteo.");
            error.show();
        }
    }
    /**
     * Consulta el premio ya asignado al cliente en el sorteo.
     * 
     * @param dni DNI del cliente.
     * @param c   Conexión abierta a la base de datos.
     * @return el premio asignado o "Ninguno" si no tiene premio.
     * @throws SQLException en caso de error en la base de datos.
     */
    private String obtenerPremioExistente(String dni, Connection c) throws SQLException {
        String query = "SELECT premio FROM sorteo WHERE cliente_dni = ?";
        try (PreparedStatement pst = c.prepareStatement(query)) {
            pst.setString(1, dni);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("premio") != null ? rs.getString("premio") : "Ninguno";
            }
        }
        return "Ninguno";
    }
    /**
     * Consulta y muestra los cupones de descuento disponibles para el cliente.
     * 
     * @throws SQLException en caso de error en la consulta a la base de datos.
     */
    public void consultarCuponesDisponibles() throws SQLException {
        String q = "SELECT id_cupon, descuento FROM cupon WHERE cliente_dni = ? AND usado = false";

        try (Connection c = ConexionBD.obtenerConexion();
             PreparedStatement pst = c.prepareStatement(q)) {

            pst.setString(1, dni);
            ResultSet rs = pst.executeQuery();

            StringBuilder mensaje = new StringBuilder();
            boolean hayCupones = false;

            while (rs.next()) {
                hayCupones = true;
                int id = rs.getInt("id_cupon");
                double descuento = rs.getDouble("descuento");
                mensaje.append("- Cupón #").append(id)
                       .append(" | Descuento: ").append(descuento).append("%\n");
            }

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Cupones disponibles");

            if (hayCupones) {
                alerta.setHeaderText("Cupones activos:");
                alerta.setContentText(mensaje.toString());
            } else {
                alerta.setHeaderText("No tiene cupones disponibles actualmente.");
                alerta.setContentText(null);
            }

            alerta.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR, "Error al consultar los cupones.");
            error.show();
        }
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
    	    Map<String, Integer> productoMap = new HashMap<>();
    	    Map<String, Double> precioMap = new HashMap<>();

    	    try (Connection c = ConexionBD.obtenerConexion();
    	         Statement stmt = c.createStatement();
    	         ResultSet rs = stmt.executeQuery("SELECT id_producto, nombre, precio FROM producto")) {

    	        while (rs.next()) {
    	            int id = rs.getInt("id_producto");
    	            String nombre = rs.getString("nombre");
    	            double precio = rs.getDouble("precio");
    	            String item = nombre + " " + precio + " - €";
    	            listaProductos.getItems().add(item);
    	            productoMap.put(item, id);
    	            precioMap.put(item, precio);
    	        }
    	    } catch (SQLException ex) {
    	        ex.printStackTrace();
    	    }

    	    ListView<String> cesta = new ListView<>();
    	    Label labelCesta = new Label("Cesta de productos:");
    	    Button btnAgregar = new Button("Agregar a cesta");

    	    btnAgregar.setOnAction(e -> {
    	        String seleccionado = listaProductos.getSelectionModel().getSelectedItem();
    	        if (seleccionado != null) {
    	            cesta.getItems().add(seleccionado);
    	        }
    	    });

    	    CheckBox usarCupon = new CheckBox("Usar cupón de descuento");
    	    Label estado = new Label();

    	    Button btnConfirmar = new Button("Confirmar compra");
    	    btnConfirmar.setOnAction(e -> {
    	        List<String> seleccionados = cesta.getItems();
    	        if (seleccionados.isEmpty()) {
    	            estado.setText("La cesta está vacía.");
    	            return;
    	        }

    	        try (Connection c = ConexionBD.obtenerConexion()) {
    	            double total = 0.0;
    	            for (String item : seleccionados) {
    	                total += precioMap.get(item);
    	            }

    	            double descuento = 0.0;
    	            int cuponId = -1;

    	            if (usarCupon.isSelected()) {
    	                PreparedStatement pstCupon = c.prepareStatement(
    	                        "SELECT id_cupon, descuento FROM cupon WHERE cliente_dni = ? AND usado = false LIMIT 1");
    	                pstCupon.setString(1, dni);
    	                ResultSet rsCupon = pstCupon.executeQuery();

    	                if (rsCupon.next()) {
    	                    descuento = rsCupon.getDouble("descuento") / 100.0;
    	                    cuponId = rsCupon.getInt("id_cupon");
    	                } else {
    	                    estado.setText("No tienes cupones disponibles.");
    	                    return;
    	                }
    	            }

    	            double totalFinal = total * (1 - descuento);

    	            // Insertar en tabla venta
    	            PreparedStatement pstVenta = c.prepareStatement(
    	                    "INSERT INTO venta (fecha_venta, total, metodo_pago, metodo_envio, coste_envio, cliente_dni) " +
    	                    "VALUES (CURDATE(), ?, 'TARJETA', '3 A 5 DIAS', 0.00, ?)", Statement.RETURN_GENERATED_KEYS);
    	            pstVenta.setDouble(1, totalFinal);
    	            pstVenta.setString(2, dni);
    	            int filasVenta = pstVenta.executeUpdate();

    	            if (filasVenta == 0) {
    	                estado.setText("Error al registrar la venta.");
    	                return;
    	            }

    	            ResultSet rsVenta = pstVenta.getGeneratedKeys();
    	            int idVenta = -1;
    	            if (rsVenta.next()) {
    	                idVenta = rsVenta.getInt(1);
    	            } else {
    	                estado.setText("Error al obtener el ID de la venta.");
    	                return;
    	            }

    	            // Agrupar productos por id y contar cantidades para evitar duplicados
    	            Map<Integer, Integer> cantidades = new HashMap<>();
    	            for (String item : seleccionados) {
    	                int idProducto = productoMap.get(item);
    	                cantidades.put(idProducto, cantidades.getOrDefault(idProducto, 0) + 1);
    	            }

    	            // Insertar productos en producto_en_venta
    	            for (Map.Entry<Integer, Integer> entry : cantidades.entrySet()) {
    	                int idProducto = entry.getKey();
    	                int cantidad = entry.getValue();

    	                PreparedStatement pstProdVenta = c.prepareStatement(
    	                        "INSERT INTO producto_en_venta (venta_id, producto_id, cantidad) VALUES (?, ?, ?)");
    	                pstProdVenta.setInt(1, idVenta);
    	                pstProdVenta.setInt(2, idProducto);
    	                pstProdVenta.setInt(3, cantidad);
    	                pstProdVenta.executeUpdate();
    	            }

    	            // Actualizar cupón si se usó
    	            if (cuponId != -1) {
    	                PreparedStatement pstUpdateCupon = c.prepareStatement(
    	                        "UPDATE cupon SET usado = true, fecha_uso = CURDATE(), venta_id = ? WHERE id_cupon = ?");
    	                pstUpdateCupon.setInt(1, idVenta);
    	                pstUpdateCupon.setInt(2, cuponId);
    	                pstUpdateCupon.executeUpdate();
    	            }

    	            estado.setText(String.format("Compra realizada con éxito. Venta #%d - Total: %.2f €", idVenta, totalFinal));

    	        } catch (SQLException ex) {
    	            ex.printStackTrace();
    	            estado.setText("Error al registrar la venta.");
    	        }
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
   
    
    
}
