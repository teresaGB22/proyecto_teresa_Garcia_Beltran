package clases;

import java.time.LocalDate;

public class Venta {
	private int idVenta;
    private LocalDate fecha_venta;
    private double total;
    private MetodoPago metodoPago;
    private MetodoEnvio metodoEnvio;
    private double costeEnvio;
    private Administrador administrador;  
    private Empleado empleado;            
    private Cliente cliente;             
    private final String nombre;


    public Venta(int idVenta, LocalDate fecha_venta, double total, MetodoPago metodoPago, 
                 MetodoEnvio metodoEnvio, double costeEnvio, Administrador administrador, 
                 Empleado empleado, Cliente cliente, String nombre) {
        this.idVenta = idVenta;
        this.fecha_venta = fecha_venta;
        this.total = total;
        this.metodoPago = metodoPago;
        this.metodoEnvio = metodoEnvio;
        this.costeEnvio = costeEnvio;
        this.administrador = administrador;
        this.empleado = empleado;
        this.cliente = cliente;
        this.nombre = nombre;
    }


    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public LocalDate getFecha_venta() {
        return fecha_venta;
    }

    public void setFecha_venta(LocalDate fecha_venta) {
        this.fecha_venta = fecha_venta;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public MetodoEnvio getMetodoEnvio() {
        return metodoEnvio;
    }

    public void setMetodoEnvio(MetodoEnvio metodoEnvio) {
        this.metodoEnvio = metodoEnvio;
    }

    public double getCosteEnvio() {
        return costeEnvio;
    }

    public void setCosteEnvio(double costeEnvio) {
        this.costeEnvio = costeEnvio;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getNombre() {
        return nombre;
    }


    @Override
    public String toString() {
        return "Venta{" +
                "idVenta=" + idVenta +
                ", fecha_venta=" + fecha_venta +
                ", total=" + total +
                ", metodoPago=" + metodoPago.getNombre() +
                ", metodoEnvio=" + metodoEnvio.getNombre() +
                ", costeEnvio=" + costeEnvio +
                ", nombre='" + nombre + '\'' +
                '}';
    }


    public enum MetodoPago {
        TARJETA("Tarjeta"),
        EFECTIVO("Efectivo"),
        DESPUES_DEL_ENVIO("Despues del envio");

        private final String nombre;

        // Constructor de MetodoPago
        MetodoPago(String nombre) {
            this.nombre = nombre;
        }

        // Getter para el nombre
        public String getNombre() {
            return nombre;
        }
    }


    public enum MetodoEnvio {
        VEINTICUATRO_HORAS("24H"),
        TRES_A_CINCO_DIAS("3 A 5 DIAS"),
        SIETE_A_DOCE_DIAS("7 A 12 DIAS");

        private final String nombre;

        MetodoEnvio(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }
    }
}
