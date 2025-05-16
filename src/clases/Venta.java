package clases;


import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import utilidades.ConexionBD;

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
    public static void descargarFacturaPdf(int idVenta) throws IOException, SQLException {
   	 String dest = "factura_" + idVenta + ".pdf";

        try (Connection con = ConexionBD.obtenerConexion();
             CallableStatement cs = con.prepareCall("{CALL facturacion(?)}")) {

            cs.setInt(1, idVenta);
            boolean hasResults = cs.execute();

            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            PdfFont font = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA);
            PdfFont fontBold = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);

            // Título de la factura
            Paragraph header = new Paragraph("Factura de Compra")
                    .setFont(fontBold)
                    .setFontSize(20)
                    .setFontColor(ColorConstants.BLUE)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(header);

            document.add(new Paragraph("\n"));

            // Extraer datos de la factura y cliente
            if (hasResults) {
                try (ResultSet rs = cs.getResultSet()) {
                    if (rs.next()) {
                        document.add(new Paragraph("Factura ID: " + idVenta).setFont(fontBold));
                        document.add(new Paragraph("Fecha: " + rs.getDate("fecha_emision").toLocalDate().format(DateTimeFormatter.ISO_DATE)).setFont(font));
                        document.add(new Paragraph("Cliente: " + rs.getString("cliente_nombre_apellidos")).setFont(font));
                        document.add(new Paragraph("Email: " + rs.getString("cliente_email")).setFont(font));
                        document.add(new Paragraph("Método de Pago: " + rs.getString("metodo_pago")).setFont(font));
                        document.add(new Paragraph("Método de Envío: " + rs.getString("metodo_envio")).setFont(font));
                        document.add(new Paragraph("\nProductos Comprados:").setFont(fontBold));
                    }
                }
            }

            // Suponiendo que el procedimiento facturacion devuelve también los productos en un siguiente ResultSet
            if (cs.getMoreResults()) {
                try (ResultSet rsProductos = cs.getResultSet()) {
                    Table table = new Table(UnitValue.createPercentArray(new float[]{70, 30}))
                            .useAllAvailableWidth();

                    table.addHeaderCell(new Cell().add(new Paragraph("Producto").setFont(fontBold).setFontColor(ColorConstants.WHITE))
                            .setBackgroundColor(ColorConstants.BLUE));
                    table.addHeaderCell(new Cell().add(new Paragraph("Cantidad").setFont(fontBold).setFontColor(ColorConstants.WHITE))
                            .setBackgroundColor(ColorConstants.BLUE));

                    while (rsProductos.next()) {
                        table.addCell(new Cell().add(new Paragraph(rsProductos.getString("producto")).setFont(font)));
                        table.addCell(new Cell().add(new Paragraph(String.valueOf(rsProductos.getInt("cantidad"))).setFont(font)));
                    }

                    document.add(table);
                }
            }

            document.close();
            System.out.println("Factura PDF creada: " + dest);
        }
    }
}
