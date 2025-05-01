package clases;

import java.time.LocalDate;

public class Proveedor extends Administrador{
private int idProveedor;
private String nombre;
private String apellidos;
private LocalDate fecha_nac;
private Administrador administrador;

public Proveedor() {}
public Administrador getAdministrador() {
	return administrador;
}
public void setAdministrador(Administrador administrador) {
	this.administrador = administrador;
}
public Proveedor(int idAdministrador, String usuario, String contrasenya, int idProveedor, String nombre,
		String apellidos, LocalDate fecha_nac, Administrador administrador) {
	super(idAdministrador, usuario, contrasenya);
	this.idProveedor = idProveedor;
	this.nombre = nombre;
	this.apellidos = apellidos;
	this.fecha_nac = fecha_nac;
	this.administrador = administrador;
}
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


}
