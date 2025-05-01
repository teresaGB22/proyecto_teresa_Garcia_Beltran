package clases;

public class Sorteo {
	private int idSorteo;
	private Resultado resultado;
	private String premio;
	private Cliente cliente;

	public Sorteo(int idSorteo, Resultado resultado, String premio, Cliente cliente) {
		this.idSorteo = idSorteo;
		this.resultado = resultado;
		this.premio = premio;
		this.cliente = cliente;
	}

	public int getIdSorteo() {
		return idSorteo;
	}

	public void setIdSorteo(int idSorteo) {
		this.idSorteo = idSorteo;
	}

	public Resultado getResultado() {
		return resultado;
	}

	public void setResultado(Resultado resultado) {
		this.resultado = resultado;
	}

	public String getPremio() {
		return premio;
	}

	public void setPremio(String premio) {
		this.premio = premio;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public enum Resultado {
		GANADOR("Ganador"), PERDEDOR("Perdedor"), NO_PARTICIPO("No participó");

		private final String descripcion;

		Resultado(String descripcion) {
			this.descripcion = descripcion;
		}

		public String getDescripcion() {
			return descripcion;
		}

		@Override
		public String toString() {
			return descripcion;
		}
	}
}
