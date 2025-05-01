package clases;

public class Producto_En_Venta {
private int cantidad;
private Venta venta;
private Producto producto;
public Producto_En_Venta() {
	
}
public Producto_En_Venta(int cantidad, Venta venta, Producto producto) {
	this.cantidad = cantidad;
	this.venta = venta;
	this.producto = producto;
}

public int getCantidad() {
	return cantidad;
}

public void setCantidad(int cantidad) {
	this.cantidad = cantidad;
}

public Venta getVenta() {
	return venta;
}

public void setVenta(Venta venta) {
	this.venta = venta;
}

public Producto getProducto() {
	return producto;
}

public void setProducto(Producto producto) {
	this.producto = producto;
}

}
