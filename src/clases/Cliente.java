package clases;
import java.sql.*;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

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
private Connection conectar() throws SQLException{
	String url ="jdbc:mysql://localhost:3306/creacionbd";
	String usuario = "root";
	String contrasenya = "2002";
	try {
		Class.forName("com.mysql.cj.jdbc.Driver");
	}catch(ClassNotFoundException e) {
		e.printStackTrace();
	}
	return DriverManager.getConnection(url, usuario, contrasenya);
	
}
public void verCatalogoProductos() throws SQLException {
	System.out.println("Mostrando catalogo de productos...");
	String q = "SELECT pev.cantidad, p.id_producto, p.nombre, p.precio\r\n"
			+ "                   FROM producto_en_venta pev \r\n"
			+ "                   JOIN producto p ON pev.producto_id = p.id_producto;";
	try(Connection c = conectar();
			Statement st = c.createStatement();
			ResultSet rs = st.executeQuery(q)){
		
		while(rs.next()) {
			Producto producto = new Producto();
			producto.setIdProducto(rs.getInt("id_producto"));
			producto.setNombreProducto(rs.getString("nombre"));
			producto.setPrecio(rs.getDouble("precio"));
			
			Producto_En_Venta productoventa = new Producto_En_Venta();
			productoventa.setProducto(producto);
			productoventa.setCantidad(rs.getInt("cantidad"));
			
			System.out.println("Producto: " + producto.getNombreProducto() + ", precio: " + producto.getPrecio() + ", stock disponible: " + productoventa.getCantidad());
		}
	}catch(SQLException e) {
		e.printStackTrace();
	}
	
}
public void buscarProductoPorNombre() throws SQLException {
	Scanner t = new Scanner(System.in);
	System.out.println("Introduce el nombre del producto que quiere buscar:\n");
	String nombreBuscar = t.nextLine();
	String q = "select p.id_producto, p.nombre, p.precio, sum(pev.cantidad) as total_cantidad from producto p join producto_en_venta pev on p.id_producto = pev.producto_id where p.nombre like ? GROUP BY p.id_producto, p.nombre, p.precio";
	
	try (Connection c = conectar();
			PreparedStatement pst = c.prepareStatement(q)){
		pst.setString(1,"%"+ nombreBuscar + "%");
		ResultSet rs = pst.executeQuery();
		boolean encontrado = false;
		
		while(rs.next()) {
			encontrado = true;
			int id = rs.getInt("id_producto");
			String nombre = rs.getString("nombre");
			double precio = rs.getDouble("precio");
			int stock = rs.getInt("total_cantidad");
			System.out.println("Id del producto: " + id + "Nombre: " +  nombre + " Precio: " + precio + " Stock: " + stock + "\n");
		}
		if(!encontrado) {
			System.out.println("No se encontraron productos con ese nombre");
		}
	}catch(SQLException e) {
		System.err.println("Error al buscar el producto" + e.getMessage());
	}
}
public void usarCuponDescuento() throws SQLException {
	Scanner t = new Scanner(System.in);
	System.out.println("Ingrese su dni: ");
	String dni_cliente = t.nextLine();
	System.out.println("Ingrese el codigo del cupon: ");
	int idCupon= t.nextInt();
	
	String q = "select descuento from cupon where id_cupon = ? and cliente_dni = ?";
	
	try (Connection c = conectar();
			PreparedStatement pst = c.prepareStatement(q)){
		pst.setInt(1, idCupon);
		pst.setString(2, dni_cliente);
		
		try(ResultSet rs = pst.executeQuery()) {
			if(rs.next()) {
				double descuento = rs.getDouble("descuento");
				System.out.println("Cupón valido, descuento aplicado: " + descuento + "%");
			
				 System.out.print("Ingrese el monto total de la compra: ");
	                double montoCompra = t.nextDouble();
	                t.nextLine();
	                double montoFinal = montoCompra - (montoCompra * (descuento / 100));
	                System.out.printf("Monto con descuento aplicado: %.2f%n", montoFinal);
	                
	               // marcar el cupón como usado 
	                String update = "UPDATE cupon SET usado = TRUE WHERE id_cupon = ?";
	                try (PreparedStatement pstUpdate = c.prepareStatement(update)) {
	                    pstUpdate.setInt(1, idCupon);
	                    pstUpdate.executeUpdate();
	                    System.out.println("Cupón marcado como usado.");
	                }
			}else {
				System.out.println("Cupon no valido o no pertenece al dni introducido");
			}
		}
		
	}catch(SQLException e) {
		System.err.println("Error al usar el cupo, " + e.getMessage());
	}
	
}
public void verFacturas()throws SQLException{
	Scanner t = new Scanner(System.in);
	System.out.println("Ingrese su dni por favor:");
	String dniCliente = t.nextLine();
	
	String q = "SELECT f.id_factura,f.venta_id, f.fecha_emision, f.total, v.fecha_venta, v.metodo_pago, v.metodo_envio, v.coste_envio "
            + "FROM factura f "
            + "JOIN venta v ON f.venta_id = v.id_venta "
            + "WHERE f.dni_cliente = ?";
	try(Connection c = conectar(); PreparedStatement pst = c.prepareStatement(q)){
		pst.setString(1, dniCliente);
		try(ResultSet rs = pst.executeQuery()){
			if(!rs.next()) {
				System.out.println("No se encontraron facturas con dni " + dniCliente);
			}else {
				do {
					System.out.println("Factura ID: " + rs.getInt("id_factura"));
                    System.out.println("Fecha Emisión: " + rs.getDate("fecha_emision"));
                    System.out.println("Total: " + rs.getBigDecimal("total"));
                    System.out.println("Venta ID: " + rs.getInt("venta_id"));
                    System.out.println("Fecha Venta: " + rs.getDate("fecha_venta"));
                    System.out.println("Método Pago: " + rs.getString("metodo_pago"));
                    System.out.println("Método Envío: " + rs.getString("metodo_envio"));
                    System.out.println("Coste Envío: " + rs.getBigDecimal("coste_envio"));
                    System.out.println("-------------------------------------------------");
				}while(rs.next());
			}
		}
	}catch(SQLException e) {
		System.err.println("Error al mostrar las facturas" + e.getMessage());
	}

	
}
public void participarEnSorteo() throws SQLException{
	Scanner t = new Scanner(System.in);
	System.out.println("Ingrese su dni por favor:");
	String dniCliente = t.nextLine();
	 String comprobacion = "SELECT id_sorteo, resultado FROM sorteo WHERE cliente_dni = ?";
	    String actualizacion = "UPDATE sorteo SET resultado = ?, premio = ? WHERE cliente_dni = ?";
	    String insertar = "INSERT INTO sorteo (resultado, premio, cliente_dni) VALUES (?, ?, ?)";

	    List<String> premios = Arrays.asList(
	        "Vale de 10€", "Producto gratis", "Cupón de envío gratuito", 
	        "Descuento del 50%", "Acceso a evento VIP", "Vale de 25€"
	    );

	    try (Connection c = conectar();
	         PreparedStatement pst = c.prepareStatement(comprobacion)) {

	        pst.setString(1, dniCliente);
	        ResultSet rs = pst.executeQuery();

	        String resultado = Math.random() < 0.3 ? "GANADOR" : "PERDEDOR";
	        String premio = resultado.equals("GANADOR") ? premios.get(new Random().nextInt(premios.size())) : null;

	        if (rs.next()) {
	            String resultadoAnterior = rs.getString("resultado");

	            if ("NO PARTICIPÓ".equals(resultadoAnterior)) {
	                try (PreparedStatement updateStmt = c.prepareStatement(actualizacion)) {
	                    updateStmt.setString(1, resultado);
	                    updateStmt.setString(2, premio);
	                    updateStmt.setString(3, dniCliente);
	                    updateStmt.executeUpdate();
	                    System.out.println("Participación actualizada.");
	                    System.out.println("Resultado: " + resultado);
	                    System.out.println("Premio: " + (premio != null ? premio : "Ninguno"));
	                }
	            } else {
	                System.out.println("Ya participó. Resultado: " + resultadoAnterior);
	            }

	        } else {
	            try (PreparedStatement insertStmt = c.prepareStatement(insertar)) {
	                insertStmt.setString(1, resultado);
	                insertStmt.setString(2, premio);
	                insertStmt.setString(3, dniCliente);
	                insertStmt.executeUpdate();
	                System.out.println("Participación registrada.");
	                System.out.println("Resultado: " + resultado);
	                System.out.println("Premio: " + (premio != null ? premio : "Ninguno"));
	            }
	        }

	    } catch (SQLException e) {
	        System.err.println("Error al participar en sorteo: " + e.getMessage());
	    }
}
}
