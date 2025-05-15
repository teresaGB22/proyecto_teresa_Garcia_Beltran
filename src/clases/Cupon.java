package clases;

public class Cupon {
private int id_cupon;
private double descuento;
private Cliente cliente;
private Promocion promocion;

public Cupon(int id_cupon, double descuento, Cliente cliente) {
	this.id_cupon = id_cupon;
	this.descuento = descuento;
	this.cliente = cliente;
}
public int getId_cupon() {
	return id_cupon;
}
public void setId_cupon(int id_cupon) {
	this.id_cupon = id_cupon;
}
public double getDescuento() {
	return descuento;
}
public void setDescuento(double descuento) {
	this.descuento = descuento;
}
public Cliente getCliente() {
	return cliente;
}
public void setCliente(Cliente cliente) {
	this.cliente = cliente;
}
public Promocion getPromocion() {
	return promocion;
}
public void setPromocion(Promocion promocion) {
	this.promocion = promocion;
}

}
