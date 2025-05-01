package clases;

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

}
