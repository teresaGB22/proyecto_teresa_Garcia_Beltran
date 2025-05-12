package clases;


import utilidades.ConexionBD;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Cliente {
    private String dni;
    private String nombre;
    private String apellidos;
    private String email;

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

    public void verCatalogoProductos() throws SQLException {
        System.out.println("Mostrando catálogo de productos...");
        String q = "select pev.cantidad, p.id_producto, p.nombre, p.precio " +
                   "from producto_en_venta pev " +
                   "join producto p on pev.producto_id = p.id_producto";

        try (Connection c = ConexionBD.obtenerConexion();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(q)) {

            while (rs.next()) {
                System.out.println("Producto: " + rs.getString("nombre") +
                                   ", precio: " + rs.getDouble("precio") +
                                   ", stock disponible: " + rs.getInt("cantidad"));
            }
        }
    }

    public void buscarProductoPorNombre() throws SQLException {
        Scanner t = new Scanner(System.in);
        System.out.println("Introduce el nombre del producto que quiere buscar:");
        String nombreBuscar = t.nextLine();

        String q = "select p.id_producto, p.nombre, p.precio, sum(pev.cantidad) as total_cantidad " +
                   "from producto p join producto_en_venta pev on p.id_producto = pev.producto_id " +
                   "where p.nombre like ? group by p.id_producto, p.nombre, p.precio";

        try (Connection c = ConexionBD.obtenerConexion();
             PreparedStatement pst = c.prepareStatement(q)) {

            pst.setString(1, "%" + nombreBuscar + "%");
            ResultSet rs = pst.executeQuery();
            boolean encontrado = false;

            while (rs.next()) {
                encontrado = true;
                System.out.println("Id del producto: " + rs.getInt("id_producto") +
                                   ", Nombre: " + rs.getString("nombre") +
                                   ", Precio: " + rs.getDouble("precio") +
                                   ", Stock: " + rs.getInt("total_cantidad"));
            }

            if (!encontrado) {
                System.out.println("No se encontraron productos con ese nombre.");
            }
        }
    }

    public void usarCuponDescuento() throws SQLException {
        Scanner t = new Scanner(System.in);
        System.out.println("Ingrese el código del cupón: ");
        int idCupon = t.nextInt();

        String q = "select descuento from cupon where id_cupon = ? and cliente_dni = ? and usado = false";

        try (Connection c = ConexionBD.obtenerConexion();
             PreparedStatement pst = c.prepareStatement(q)) {

            pst.setInt(1, idCupon);
            pst.setString(2, dni);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    double descuento = rs.getDouble("descuento");
                    System.out.println("Cupón válido, descuento aplicado: " + descuento + "%");

                    System.out.print("Ingrese el monto total de la compra: ");
                    double montoCompra = t.nextDouble();
                    double montoFinal = montoCompra - (montoCompra * (descuento / 100));
                    System.out.printf("Monto con descuento aplicado: %.2f%n", montoFinal);

                    String update = "update cupon set usado = true where id_cupon = ?";
                    try (PreparedStatement pstUpdate = c.prepareStatement(update)) {
                        pstUpdate.setInt(1, idCupon);
                        pstUpdate.executeUpdate();
                        System.out.println("Cupón marcado como usado.");
                    }
                } else {
                    System.out.println("Cupón no válido o ya fue usado.");
                }
            }
        }
    }

    public void verFacturas() throws SQLException {
        if (dni == null) {
            System.out.println("DNI no establecido en el objeto cliente.");
            return;
        }

        String q = "select f.id_factura, f.venta_id, f.fecha_emision, f.total, " +
                   "v.fecha_venta, v.metodo_pago, v.metodo_envio, v.coste_envio " +
                   "from factura f join venta v on f.venta_id = v.id_venta " +
                   "where f.dni_cliente = ?";

        try (Connection c = ConexionBD.obtenerConexion();
             PreparedStatement pst = c.prepareStatement(q)) {

            pst.setString(1, dni);

            try (ResultSet rs = pst.executeQuery()) {
                boolean encontrado = false;
                while (rs.next()) {
                    encontrado = true;
                    System.out.println("Factura ID: " + rs.getInt("id_factura"));
                    System.out.println("Fecha emisión: " + rs.getDate("fecha_emision"));
                    System.out.println("Total: " + rs.getBigDecimal("total"));
                    System.out.println("Venta ID: " + rs.getInt("venta_id"));
                    System.out.println("Fecha venta: " + rs.getDate("fecha_venta"));
                    System.out.println("Método pago: " + rs.getString("metodo_pago"));
                    System.out.println("Método envío: " + rs.getString("metodo_envio"));
                    System.out.println("Coste envío: " + rs.getBigDecimal("coste_envio"));
                    System.out.println("-------------------------------------------------");
                }
                if (!encontrado) {
                    System.out.println("No se encontraron facturas para este cliente.");
                }
            }
        }
    }

    public void participarEnSorteo() throws SQLException {
        String comprobacion = "select id_sorteo, resultado from sorteo where cliente_dni = ?";
        String actualizacion = "update sorteo set resultado = ?, premio = ? where cliente_dni = ?";
        String insertar = "insert into sorteo (resultado, premio, cliente_dni) values (?, ?, ?)";

        List<String> premios = Arrays.asList("vale de 10€", "producto gratis", "cupón de envío gratuito", 
                                             "descuento del 50%", "acceso a evento vip", "vale de 25€");

        try (Connection c = ConexionBD.obtenerConexion();
             PreparedStatement pst = c.prepareStatement(comprobacion)) {

            pst.setString(1, dni);
            ResultSet rs = pst.executeQuery();

            String resultado = Math.random() < 0.3 ? "GANADOR" : "PERDEDOR";
            String premio = resultado.equals("GANADOR") ? premios.get(new Random().nextInt(premios.size())) : null;

            if (rs.next()) {
                String resultadoAnterior = rs.getString("resultado");

                if ("NO PARTICIPÓ".equals(resultadoAnterior)) {
                    try (PreparedStatement updateStmt = c.prepareStatement(actualizacion)) {
                        updateStmt.setString(1, resultado);
                        updateStmt.setString(2, premio);
                        updateStmt.setString(3, dni);
                        updateStmt.executeUpdate();
                        System.out.println("Participación actualizada. Resultado: " + resultado +
                                           ", Premio: " + (premio != null ? premio : "ninguno"));
                    }
                } else {
                    System.out.println("Ya participó. Resultado: " + resultadoAnterior);
                }

            } else {
                try (PreparedStatement insertStmt = c.prepareStatement(insertar)) {
                    insertStmt.setString(1, resultado);
                    insertStmt.setString(2, premio);
                    insertStmt.setString(3, dni);
                    insertStmt.executeUpdate();
                    System.out.println("Participación registrada. Resultado: " + resultado +
                                       ", Premio: " + (premio != null ? premio : "ninguno"));
                }
            }
        }
    }

    public void consultarCuponesDisponibles() throws SQLException {
        String q = "select id_cupon, descuento from cupon where cliente_dni = ? and usado = false";

        try (Connection c = ConexionBD.obtenerConexion();
             PreparedStatement pst = c.prepareStatement(q)) {

            pst.setString(1, dni);
            ResultSet rs = pst.executeQuery();

            boolean hayCupones = false;
            System.out.println("Cupones disponibles:");

            while (rs.next()) {
                hayCupones = true;
                System.out.println("- Cupón #" + rs.getInt("id_cupon") + " | Descuento: " + rs.getDouble("descuento") + "%");
            }

            if (!hayCupones) {
                System.out.println("No tiene cupones disponibles actualmente.");
            }
        }
    }
}
