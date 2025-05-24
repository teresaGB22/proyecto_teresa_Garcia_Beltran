package clases;


import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.itextpdf.io.font.constants.StandardFonts;
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
	   /**
     * Estados posibles para un pedido.
     */
	public enum EstadoPedido {
        PENDIENTE,
        CONFIRMADO,
        RECHAZADO,
        ENVIADO,
        ENTREGADO,
        CANCELADO
    }

	private int idVenta;
    private LocalDate fecha_venta;
    private double total;
    private MetodoPago metodoPago;
    private MetodoEnvio metodoEnvio;
    private EstadoPedido estadoPedido;
    private double costeEnvio;

    private Empleado empleado;            
    private Cliente cliente;             
    private final String nombre;



    public Venta(int int1, LocalDate localDate, double double1, EstadoPedido estado, Cliente c,
            MetodoPago metodoPago, MetodoEnvio metodoEnvio) {
   this.nombre = "";
   this.idVenta = int1;
   this.fecha_venta = localDate;
   this.total = double1;
   this.estadoPedido = estado;
   this.cliente = c;
   this.metodoPago = metodoPago;
   this.metodoEnvio = metodoEnvio;
}

   

	public EstadoPedido getEstadoPedido() {
		return estadoPedido;
	}


	public void setEstadoPedido(EstadoPedido estadoPedido) {
		this.estadoPedido = estadoPedido;
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


    

    /**
     * Métodos de pago disponibles para una venta.
     */
    public enum MetodoPago {
        TARJETA("TARJETA"),
        EFECTIVO("EFECTIVO"),
        DESPUES_DE_ENTREGA("DESPUES DE ENTREGA"),
        PAYPAL("PAYPAL"),
        BIZUM("BIZUM");

        private final String texto;

        MetodoPago(String texto) {
            this.texto = texto;
        }
        /**
         * Obtiene el método de pago a partir de su representación en texto.
         * @param texto Texto del método de pago
         * @return MetodoPago correspondiente
         * @throws IllegalArgumentException si el texto no coincide con ningún método
         */
        public static MetodoPago desdeTexto(String texto) {
            for (MetodoPago m : MetodoPago.values()) {
                if (m.texto.equalsIgnoreCase(texto)) {
                    return m;
                }
            }
            throw new IllegalArgumentException("Método de pago inválido: " + texto);
        }

        @Override
        public String toString() {
            return texto;
        }
    }
    /**
     * Métodos de envío disponibles para una venta.
     */
    public enum MetodoEnvio {
        VEINTICUATRO_HORAS("24H"),
        TRES_A_CINCO_DIAS("3 A 5 DIAS"),
        SIETE_A_DOCE_DIAS("7 A 12 DIAS"),
        URGENTE("URGENTE"),
        RECOGIDA_EN_TIENDA("RECOGIDA EN TIENDA");

        private final String codigoBD;

        MetodoEnvio(String codigoBD) {
            this.codigoBD = codigoBD;
        }

        public String getCodigoBD() {
            return codigoBD;
        }
        /**
         * Obtiene el método de envío a partir de su código en base de datos.
         * @param codigo Código del método de envío en base de datos
         * @return MetodoEnvio correspondiente
         * @throws IllegalArgumentException si el código no es válido
         */
        public static MetodoEnvio fromCodigoBD(String codigo) {
            for (MetodoEnvio metodo : values()) {
                if (metodo.codigoBD.equals(codigo)) {
                    return metodo;
                }
            }
            throw new IllegalArgumentException("Código no válido para MetodoEnvio: " + codigo);
        }
    }

    /**
     * Descarga la factura a una venta en formato PDF.
     * El archivo se guarda con el nombre "factura_<idVenta>.pdf".
     *
     * @param idVenta ID de la venta para la cual se genera la factura
     * @throws IOException  Si ocurre un error al generar el archivo PDF
     * @throws SQLException Si ocurre un error en la consulta a la base de datos
     */
    public static void descargarFacturaPdf(int idVenta) throws IOException, SQLException {
        String dest = "factura_" + idVenta + ".pdf";

        try (Connection con = ConexionBD.obtenerConexion()) {
            // Verificar existencia de factura
            String sqlCheck = "SELECT existe_factura(?)";
            try (PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, idVenta);
                try (ResultSet rsCheck = psCheck.executeQuery()) {
                    if (rsCheck.next() && rsCheck.getInt(1) == 0) {
                        throw new SQLException("No existe factura para la venta con ID " + idVenta);
                    }
                }
            }

            try (CallableStatement cs = con.prepareCall("{CALL facturacion(?)}")) {
                cs.setInt(1, idVenta);
                boolean hasResults = cs.execute();

                if (!hasResults) {
                    System.out.println("No se encontraron datos para la venta con ID: " + idVenta);
                    return;
                }

                try (ResultSet rs = cs.getResultSet()) {
                    if (!rs.isBeforeFirst()) {
                        System.out.println("No hay datos en la factura.");
                        return;
                    }

                    PdfWriter writer = new PdfWriter(dest);
                    PdfDocument pdf = new PdfDocument(writer);
                    Document document = new Document(pdf);

                    PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
                    PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

                    document.add(new Paragraph("Factura de Compra")
                            .setFont(fontBold)
                            .setFontSize(20)
                            .setFontColor(ColorConstants.BLUE)
                            .setTextAlignment(TextAlignment.CENTER));
                    document.add(new Paragraph("\n"));

                    boolean datosFacturaMostrados = false;
                    boolean hayProductos = false;

                    Table table = new Table(UnitValue.createPercentArray(new float[]{70, 30}))
                            .useAllAvailableWidth();
                    table.addHeaderCell(new Cell().add(new Paragraph("producto").setFont(fontBold).setFontColor(ColorConstants.WHITE))
                            .setBackgroundColor(ColorConstants.BLUE));
                    table.addHeaderCell(new Cell().add(new Paragraph("cantidad").setFont(fontBold).setFontColor(ColorConstants.WHITE))
                            .setBackgroundColor(ColorConstants.BLUE));

                    // Variables para guardar totales fuera del while
                    double total = 0;
                    double totalConDescuento = 0;

                    while (rs.next()) {
                        if (!datosFacturaMostrados) {
                            document.add(new Paragraph("Factura asociada a la Venta ID: " + idVenta).setFont(fontBold));
                            document.add(new Paragraph("Fecha Emisión: " + rs.getDate("fecha_emision")).setFont(font));
                            document.add(new Paragraph("Cliente: " + rs.getString("cliente_nombre_apellidos")).setFont(font));
                            document.add(new Paragraph("Email Cliente: " + rs.getString("cliente_email")).setFont(font));
                            document.add(new Paragraph("Método de Pago: " + rs.getString("metodo_pago")).setFont(font));
                            document.add(new Paragraph("Método de Envío: " + rs.getString("metodo_envio")).setFont(font));
                            document.add(new Paragraph("Proveedor: " + rs.getString("proveedor_nombre")).setFont(font));
                            document.add(new Paragraph("Dirección Proveedor: " + rs.getString("proveedor_direccion")).setFont(font));
                            document.add(new Paragraph("Teléfono Proveedor: " + rs.getString("proveedor_telefono")).setFont(font));
                            document.add(new Paragraph("Email Proveedor: " + rs.getString("proveedor_email")).setFont(font));
                            document.add(new Paragraph("Cuenta Bancaria Proveedor: " + rs.getString("proveedor_cuenta_bancaria")).setFont(font));

                            // Guardamos totales para usarlos luego
                            total = rs.getDouble("total");
                            totalConDescuento = rs.getDouble("total_con_descuento");

                            document.add(new Paragraph("Total Venta sin descuento: €" + String.format("%.2f", total)).setFont(font));
                            document.add(new Paragraph("\nProductos Comprados:").setFont(fontBold));

                            datosFacturaMostrados = true;
                        }

                        String producto = rs.getString("producto");
                        int cantidad = rs.getInt("cantidad");

                        if (producto != null && !producto.isBlank()) {
                            table.addCell(new Cell().add(new Paragraph(producto).setFont(font)));
                            table.addCell(new Cell().add(new Paragraph(String.valueOf(cantidad)).setFont(font)));
                            hayProductos = true;
                        }
                    }

                    if (hayProductos) {
                        document.add(table);
                    } else {
                        document.add(new Paragraph("No se encontraron productos para esta factura.").setFont(font));
                    }

                    // Usamos las variables guardadas en vez de leer del ResultSet ya cerrado
                    document.add(new Paragraph("\nResumen de Totales:").setFont(fontBold));
                    document.add(new Paragraph("Total Venta sin descuento: €" + String.format("%.2f", total)).setFont(font));
                    document.add(new Paragraph("Total Venta con descuento: €" + String.format("%.2f", totalConDescuento)).setFont(font));

                    document.close();
                    System.out.println("Factura PDF creada: " + dest);
                }
            }
        }
    }
    /**
     * Actualiza la información de la venta en la base de datos.
     *
     * @throws SQLException si ocurre un error durante la actualización
     */
    public void actualizarVenta() throws SQLException {
        String sql = "UPDATE venta SET fecha_venta = ?, estado_pedido = ?, metodo_envio = ?, metodo_pago = ?, cliente_dni = ?, empleado_id = ? WHERE id_venta = ?";
        try (Connection c = ConexionBD.obtenerConexion();
             PreparedStatement pst = c.prepareStatement(sql)) {
            pst.setDate(1, Date.valueOf(this.fecha_venta));
            pst.setString(2, this.estadoPedido.name());
            pst.setString(3, this.metodoEnvio.getCodigoBD());
            pst.setString(4, this.metodoPago.toString());
            pst.setString(5, this.cliente.getDni());
            pst.setInt(6, this.empleado.getIdEmpleado());
            pst.setInt(7, this.idVenta);
            pst.executeUpdate();
        }
    }
}
