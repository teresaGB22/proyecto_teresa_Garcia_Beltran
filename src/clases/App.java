package clases;

import java.sql.SQLException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    private void mostrarMenuCliente() {
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
				cliente.usarCuponDescuento();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        btnVerFacturas.setOnAction(e -> {
			try {
				cliente.verFacturas();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        btnParticiparSorteo.setOnAction(e -> {
			try {
				cliente.participarEnSorteo();
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

    private void mostrarMenuEmpleado() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Menú Empleado");
        alert.setHeaderText("Opciones para Empleado");
        alert.setContentText("1. Registrar nueva venta\n2. Buscar productos disponibles\n3. Ver historial de ventas registradas");
        alert.showAndWait();
    }

    private void mostrarMenuAdministrador() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Menú Administrador");
        alert.setHeaderText("Opciones para Administrador");
        alert.setContentText("1. Gestión de productos\n2. Gestión de proveedores\n3. Gestión de empleados");
        alert.showAndWait();
    }

    @Override
    public void start(Stage stage) {
        // Crear la interfaz gráfica
        VBox vbox = new VBox(10); // Espaciado entre los elementos
        vbox.setStyle("-fx-padding: 20;");

        // Crear los botones de cada opción
        Button btnCliente = new Button("Cliente");
        Button btnEmpleado = new Button("Empleado");
        Button btnAdministrador = new Button("Administrador");
        Button btnSalir = new Button("Salir");

        // Acción al hacer clic en Cliente
        btnCliente.setOnAction(e -> mostrarMenuCliente());

        // Acción al hacer clic en Empleado
        btnEmpleado.setOnAction(e -> mostrarMenuEmpleado());

        // Acción al hacer clic en Administrador
        btnAdministrador.setOnAction(e -> mostrarMenuAdministrador());

        // Acción al hacer clic en Salir
        btnSalir.setOnAction(e -> stage.close()); // Cierra la ventana

        // Agregar botones a la interfaz
        vbox.getChildren().addAll(btnCliente, btnEmpleado, btnAdministrador, btnSalir);

        // Crear la escena
        Scene scene = new Scene(vbox, 300, 200);

        // Configurar el escenario
        stage.setTitle("Menú Principal");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
