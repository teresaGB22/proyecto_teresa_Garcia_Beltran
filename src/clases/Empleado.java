package clases;

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

}

