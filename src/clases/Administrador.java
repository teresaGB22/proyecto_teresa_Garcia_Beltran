package clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utilidades.ConexionBD;

public class Administrador {
	@Id
	private int idAdministrador;
	private String usuario;
	private String contrasenya;

	public Administrador(int idAdministrador, String usuario, String contrasenya) {
		this.idAdministrador = idAdministrador;
		this.usuario = usuario;
		this.contrasenya = contrasenya;
	}

	public Administrador() {

	}

	public Administrador(int idAdministrador) {
		this.idAdministrador = idAdministrador;
	}

	public int getIdAdministrador() {
		return idAdministrador;
	}

	public void setIdAdministrador(int idAdministrador) {
		this.idAdministrador = idAdministrador;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContrasenya() {
		return contrasenya;
	}

	public void setContrasenya(String contrasenya) {
		this.contrasenya = contrasenya;
	}
	


	// Método para modificar un producto en la base de datos
	
	// Método para eliminar un producto de la base de datos
	private void eliminarProducto(Producto producto) {
	    String query = "DELETE FROM producto WHERE id_producto = ?";
	    
	    try (Connection conexion = ConexionBD.obtenerConexion();
	         PreparedStatement pst = conexion.prepareStatement(query)) {
	        
	        pst.setInt(1, producto.getIdProducto());
	        pst.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

}
