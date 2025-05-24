package clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    
    public Producto(int idProducto, String nombreProducto, LocalDate fechaEntrada, int stock, double precio, boolean esOnline,
            Proveedor proveedor) {
        this.idProducto = idProducto;
        this.nombre = nombreProducto;
        this.fechaEntrada = fechaEntrada;
        this.stock = stock;
        this.esOnline = esOnline;
        this.proveedor = proveedor;
        this.precio = precio;
    }


    public Producto(int idProducto, String nombreProducto, LocalDate fechaEntrada, int stock, boolean esOnline,
            double precio, Proveedor proveedor) {
        this.idProducto = idProducto;
        this.nombre = nombreProducto;
        this.fechaEntrada = fechaEntrada;
        this.stock = stock;
        this.esOnline = esOnline;
        this.proveedor = proveedor;
        this.precio = precio;
    }

    public Producto(int int1, String string, double double1, int stock) {
        this.idProducto = int1;
        this.nombre = string;
        this.precio = double1;
        this.stock = stock;
    }


    public Producto() {
		// TODO Auto-generated constructor stub
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

    /**
     * Carga todos los productos disponibles desde la base de datos y los devuelve
     * como una lista  para su uso en interfaces JavaFX.
     * 
     * @return ObservableList con todos los productos.
     */
    public static ObservableList<Producto> cargarProductosDisponibles() {
        ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

        String sql = "SELECT * FROM producto";

        try (Connection con = ConexionBD.obtenerConexion();
                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int idProducto = rs.getInt("id_producto");
                String nombre = rs.getString("nombre");
                int stock = rs.getInt("stock");
                double precio = rs.getDouble("precio");
                boolean esOnline = rs.getBoolean("es_online");
                LocalDate fechaEntrada = rs.getDate("fecha_entrada").toLocalDate();
                int idProveedor = rs.getInt("proveedor_id");

                Proveedor proveedor = new Proveedor(idProveedor);

                Producto producto = new Producto(idProducto, nombre, fechaEntrada, stock, esOnline, precio, proveedor);

                listaProductos.add(producto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaProductos;
    }

 
    @Override
    public String toString() {
        return nombre;
    }
}
