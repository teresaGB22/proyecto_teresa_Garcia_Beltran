package clases;

import java.time.LocalDate;

public class Factura {
private int id_fatura;
private LocalDate fecha_emision;
private String email_cliente;
private Venta venta;

public Factura(int id_fatura, LocalDate fecha_emision, String email_cliente, Venta venta) {
	this.id_fatura = id_fatura;
	this.fecha_emision = fecha_emision;
	this.email_cliente = email_cliente;
	this.venta = venta;
}
public int getId_fatura() {
	return id_fatura;
}
public void setId_fatura(int id_fatura) {
	this.id_fatura = id_fatura;
}
public LocalDate getFecha_emision() {
	return fecha_emision;
}
public void setFecha_emision(LocalDate fecha_emision) {
	this.fecha_emision = fecha_emision;
}
public String getEmail_cliente() {
	return email_cliente;
}
public void setEmail_cliente(String email_cliente) {
	this.email_cliente = email_cliente;
}
public Venta getVenta() {
	return venta;
}
public void setVenta(Venta venta) {
	this.venta = venta;
}

	
}
