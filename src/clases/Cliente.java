package clases;
import java.sql.*;
import java.sql.Statement;
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
}
