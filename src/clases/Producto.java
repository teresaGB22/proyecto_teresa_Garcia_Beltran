package clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utilidades.ConexionBD;

public class Producto {
	private int idProducto;
	private String nombre;
	private LocalDate fechaEntrada;
	private int stock;
	private boolean esOnline;
private double precio;
	private Proveedor proveedor;
	

	public Producto(int i, String string, int j, double d, boolean b, int id, LocalDate localDate) {
		this.idProducto = i;
		this.nombre = string;
		this.stock = j;
		this.precio = d;
		this.esOnline = b;
		
		this.fechaEntrada = localDate;
	}

	public Producto(int idProducto, String nombreProducto, LocalDate fechaEntrada, int stock,double precio, boolean esOnline,
			Proveedor proveedor) {
		this.idProducto = idProducto;
		this.nombre = nombreProducto;
		this.fechaEntrada = fechaEntrada;
		this.stock = stock;
		this.esOnline = esOnline;
		this.proveedor = proveedor;
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombreProducto) {
		this.nombre = nombreProducto;
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

	

	
	
	
}
