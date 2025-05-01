package clases;

import java.time.LocalDate;

public class Producto {
	private int idProducto;
	private String nombreProducto;
	private LocalDate fechaEntrada;
	private int stock;
	private boolean esOnline;
private double precio;
	private Proveedor proveedor;
	private Administrador administrador;

	public Producto() {
	}

	public Producto(int idProducto, String nombreProducto, LocalDate fechaEntrada, int stock,double precio, boolean esOnline,
			Proveedor proveedor, Administrador administrador) {
		this.idProducto = idProducto;
		this.nombreProducto = nombreProducto;
		this.fechaEntrada = fechaEntrada;
		this.stock = stock;
		this.esOnline = esOnline;
		this.proveedor = proveedor;
		this.administrador = administrador;
		this.precio = precio;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(int idProducto) {
		this.idProducto = idProducto;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public LocalDate getFechaEntrada() {
		return fechaEntrada;
	}

	public void setFechaEntrada(LocalDate fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public boolean isEsOnline() {
		return esOnline;
	}

	public void setEsOnline(boolean esOnline) {
		this.esOnline = esOnline;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public Administrador getAdministrador() {
		return administrador;
	}

	public void setAdministrador(Administrador administrador) {
		this.administrador = administrador;
	}
	
	
}
