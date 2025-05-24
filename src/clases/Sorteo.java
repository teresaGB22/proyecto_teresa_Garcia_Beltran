package clases;

import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.stage.Stage;
import utilidades.ConexionBD;

/**
 * Clase que representa un sorteo asociado a un cliente, un premio y un resultado.
 */
public class Sorteo {
    private int idSorteo;
    private Resultado resultado;
    private String premio;
    private Cliente cliente;


    public Sorteo(int idSorteo, Resultado resultado, String premio, Cliente cliente) {
        this.idSorteo = idSorteo;
        this.resultado = resultado;
        this.premio = premio;
        this.cliente = cliente;
    }


    public Sorteo(int id, String premio) {
        this.idSorteo = id;
        this.premio = premio;
    }


    public int getIdSorteo() {
        return idSorteo;
    }


    public void setIdSorteo(int idSorteo) {
        this.idSorteo = idSorteo;
    }


    public Resultado getResultado() {
        return resultado;
    }

    public void setResultado(Resultado resultado) {
        this.resultado = resultado;
    }


    public String getPremio() {
        return premio;
    }

 
    public void setPremio(String premio) {
        this.premio = premio;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Enumeración que representa el resultado de un sorteo.
     */
    public enum Resultado {
        GANADOR("Ganador"),
        PERDEDOR("Perdedor"),
        NO_PARTICIPO("No participó");

        private final String descripcion;

        /**
         * Constructor de la enumeración Resultado.
         *
         * @param descripcion Descripción legible del resultado.
         */
        Resultado(String descripcion) {
            this.descripcion = descripcion;
        }

        
        public String getDescripcion() {
            return descripcion;
        }

        @Override
        public String toString() {
            return descripcion;
        }
    }

    /**
     * Método privado, realiza la consulta para crear sorteos
     * para todos los clientes asociados a un proveedor.
     *
     * @param idProveedor Identificador del proveedor.
     * @param premio Premio a asignar a cada sorteo.
     * @param sql Consulta SQL para obtener clientes asociados.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    private static void consultaCrearSorteo(int idProveedor, String premio, String sql) throws SQLException {
        String insertar = "insert into sorteo (resultado, premio, cliente_dni, proveedor_id) values ('NO PARTICIPÓ', ?, ?, ?)";
        try (Connection c = ConexionBD.obtenerConexion();
             PreparedStatement pstClientes = c.prepareStatement(sql);
             PreparedStatement pstInsertar = c.prepareStatement(insertar)) {

            pstClientes.setInt(1, idProveedor);
            ResultSet rs = pstClientes.executeQuery();
            System.out.println("IdProveedor: " + idProveedor + ", Premio: " + premio);
            while (rs.next()) {
                String dniCliente = rs.getString("cliente_dni");
                if (dniCliente == null) {
                    System.out.println("Cliente con dni nulo, se omite inserción.");
                    continue;
                }
                System.out.println("Insertando sorteo para cliente_dni: " + dniCliente);
                pstInsertar.setString(1, premio);
                pstInsertar.setString(2, dniCliente);
                pstInsertar.setInt(3, idProveedor);
                int filas = pstInsertar.executeUpdate();
                System.out.println("Filas insertadas: " + filas);
            }

        }
    }

    /**
     * Crea sorteos paralos clientes que han comprado productos
     * de un proveedor específico.
     *
     * @param idProveedor Identificador del proveedor.
     * @param premios Premio que se asignará en el sorteo.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    private static void crearSorteo(int idProveedor, String premios) throws SQLException {
        String sql = "select cliente_dni from clientes_proveedor where proveedor_id = ? ";
        consultaCrearSorteo(idProveedor, premios, sql);
    }

    /**
     * Ventana gráfica para crear un sorteo para un proveedor.
     *
     * @param p Proveedor para el cual se va a crear el sorteo.
     */
    public static void mostrarCrearSorteo(Proveedor p) {
        Stage stage = new Stage();
        VBox v = new VBox(10);
        v.setPadding(new Insets(15));

        Label titulo = new Label("Crear sorteo para proveedor: " + p.getNombre());

        Label lblPremio = new Label("Premio: ");
        TextField txtPremio = new TextField();

        Label clientes = new Label("Para clientes ");
        CheckBox cbClientes = new CheckBox();
        cbClientes.setSelected(true);

        Button btnCrear = new Button("Crear sorteo");
        Label estado = new Label();

        btnCrear.setOnAction(e -> {
            String DarPremio = txtPremio.getText().trim();
            if (DarPremio.isEmpty()) {
                estado.setText("El premio no puede ser vacío");
                return;
            }
            try {
                crearSorteo(p.getIdProveedor(), DarPremio);
                estado.setText("Sorteo creado");
            } catch (SQLException e1) {
                estado.setText("Error al crear sorteo: " + e1.getMessage());
            }
        });

        v.getChildren().addAll(titulo, lblPremio, txtPremio, clientes, cbClientes, btnCrear, estado);

        Scene scene = new Scene(v, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Crear Sorteo");
        stage.show();
    }

    /**
     * Lista de sorteos disponibles para un cliente,
     *  aquellos donde el cliente no ha participado aún.
     *
     * @param dniCliente DNI del cliente.
     * @return Lista de objetos Sorteo disponibles.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public static List<Sorteo> obtenerSorteosDisponibles(String dniCliente) throws SQLException {
        List<Sorteo> lista = new ArrayList<>();
        String sql = "select id_sorteo, premio from sorteo where cliente_dni = ? and "
                + "resultado = 'NO PARTICIPÓ'";
        try (Connection c = ConexionBD.obtenerConexion();
             PreparedStatement pst = c.prepareStatement(sql)) {
            pst.setString(1, dniCliente);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_sorteo");
                String premio = rs.getString("premio");
                lista.add(new Sorteo(id, premio));
            }
        }
        return lista;
    }

    /**
     * historial de participación en sorteos de un cliente,
     * incluyendo resultados y premios.
     *
     * @param dni DNI del cliente.
     * @return Lista de cadenas con el historial de participación.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public static List<String> obtenerHistorialParticipacion(String dni) throws SQLException {
        List<String> historial = new ArrayList<>();
      

        String sql = "select id_sorteo, resultado, premio from sorteo where cliente_dni = ? and resultado is not null";

        try (Connection c = ConexionBD.obtenerConexion();
             PreparedStatement pst = c.prepareStatement(sql)) {
            pst.setString(1, dni);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_sorteo");
                String resultado = rs.getString("resultado");
                String premio = rs.getString("premio");
                historial.add("Sorteo " + id + " - " + resultado + " - Premio: " + premio);
            }
        }

        return historial;
    }

    @Override
    public String toString() {
        return "ID: " + idSorteo + " | Premio: " + premio;
    }
}