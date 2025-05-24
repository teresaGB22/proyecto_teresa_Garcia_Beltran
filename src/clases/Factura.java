package clases;

import java.time.LocalDate;

public class Factura {
	  private int idFactura;
	    private LocalDate fechaEmision;
	    private double total;
	    private LocalDate fechaVenta;
	    private String metodoPago;
	    private double total_con_descuento;

	    public Factura(int idFactura, LocalDate fechaEmision, double total,double totalDescuento, LocalDate fechaVenta, String metodoPago) {
	        this.idFactura = idFactura;
	        this.fechaEmision = fechaEmision;
	        this.total = total;
	        this.total_con_descuento = totalDescuento;
	        this.fechaVenta = fechaVenta;
	        this.metodoPago = metodoPago;
	    }

	    public int getIdFactura() {
	        return idFactura;
	    }

	    public void setIdFactura(int idFactura) {
	        this.idFactura = idFactura;
	    }

	    public LocalDate getFechaEmision() {
	        return fechaEmision;
	    }

	    public void setFechaEmision(LocalDate fechaEmision) {
	        this.fechaEmision = fechaEmision;
	    }

	    public double getTotal() {
	        return total;
	    }

	    public void setTotal(double total) {
	        this.total = total;
	    }

	    public LocalDate getFechaVenta() {
	        return fechaVenta;
	    }

	    public void setFechaVenta(LocalDate fechaVenta) {
	        this.fechaVenta = fechaVenta;
	    }

	    public String getMetodoPago() {
	        return metodoPago;
	    }

	    public void setMetodoPago(String metodoPago) {
	        this.metodoPago = metodoPago;
	    }

		public double getTotal_con_descuento() {
			return total_con_descuento;
		}

		public void setTotal_con_descuento(double total_con_descuento) {
			this.total_con_descuento = total_con_descuento;
		}
	
	
}
