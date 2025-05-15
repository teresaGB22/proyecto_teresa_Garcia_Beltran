package clases;

import java.time.LocalDate;

public class Promocion {
private int id_promocion;
private Proveedor proveedor;
private String descripcion;
private double descuento;
private LocalDate fecha_inicio;
private LocalDate fecha_fin;
public Promocion() {};
public Promocion(int id_promocion, Proveedor proveedor, String descripcion, double descuento, LocalDate fecha_inicio,
		LocalDate fecha_fin) {
	this.id_promocion = id_promocion;
	this.proveedor = proveedor;
	this.descripcion = descripcion;
	this.descuento = descuento;
	this.fecha_inicio = fecha_inicio;
	this.fecha_fin = fecha_fin;
}
public int getId_promocion() {
	return id_promocion;
}
public void setId_promocion(int id_promocion) {
	this.id_promocion = id_promocion;
}
public Proveedor getProveedor() {
	return proveedor;
}
public void setProveedor(Proveedor proveedor) {
	this.proveedor = proveedor;
}
public String getDescripcion() {
	return descripcion;
}
public void setDescripcion(String descripcion) {
	this.descripcion = descripcion;
}
public double getDescuento() {
	return descuento;
}
public void setDescuento(double descuento) {
	this.descuento = descuento;
}
public LocalDate getFecha_inicio() {
	return fecha_inicio;
}
public void setFecha_inicio(LocalDate fecha_inicio) {
	this.fecha_inicio = fecha_inicio;
}
public LocalDate getFecha_fin() {
	return fecha_fin;
}
public void setFecha_fin(LocalDate fecha_fin) {
	this.fecha_fin = fecha_fin;
}


}
