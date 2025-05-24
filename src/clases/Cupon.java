package clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import utilidades.ConexionBD;


public class Cupon {
    private int id_cupon;
    private double descuento;
    private Cliente cliente;
    private Promocion promocion;
    private boolean usado;

 
    public Cupon(int id_cupon, double descuento, Cliente cliente) {
        this.id_cupon = id_cupon;
        this.descuento = descuento;
        this.cliente = cliente;
    }


    public Cupon() {
        // Constructor por defecto.
    }


    public int getId_cupon() {
        return id_cupon;
    }

 
    public void setId_cupon(int id_cupon) {
        this.id_cupon = id_cupon;
    }

    public double getDescuento() {
        return descuento;
    }

 
    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }


    public Cliente getCliente() {
        return cliente;
    }

 
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }


    public Promocion getPromocion() {
        return promocion;
    }


    public void setPromocion(Promocion promocion) {
        this.promocion = promocion;
    }

 
    public boolean isUsado() {
        return usado;
    }

  
    public void setUsado(boolean usado) {
        this.usado = usado;
    }

    /**
     * Lista de todos los cupones disponibles que tengan promociones activas.
     * 
     * @return Lista de cupones disponibles.
     */
    public static List<Cupon> obtenerCuponesDisponibles() {
        List<Cupon> cupones = new ArrayList<>();

        String q = "select c.*,p.id_promocion, p.descripcion from cupon as c inner join promocion as p "
                + "on c.promocion_id = p.id_promocion where p.activo = true";

        try (Connection c = ConexionBD.obtenerConexion();
                PreparedStatement pst = c.prepareStatement(q)) {

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Cupon cupon = new Cupon();
                    cupon.setId_cupon(rs.getInt("id_cupon"));
                    cupon.setDescuento(rs.getDouble("descuento"));
                    cupon.setUsado(false);
                    Promocion promo = new Promocion();
                    promo.setId_promocion(rs.getInt("id_promocion"));
                    promo.setDescripcion(rs.getString("descripcion"));
                    cupon.setPromocion(promo);

                    cupones.add(cupon);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cupones;
    }

    /**
     * Marca un cupón como usados.
     * 
     * @param idCupon Identificador del cupón a marcar como usado.
     * @return true si la operación fue exitosa, false si no.
     */
    public static boolean marcarComoUsado(int idCupon) {
        String sql = "UPDATE cupon SET usado = true WHERE id_cupon = ?";

        try (Connection c = ConexionBD.obtenerConexion();
                PreparedStatement pst = c.prepareStatement(sql)) {

            pst.setInt(1, idCupon);
            int filas = pst.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

 
    @Override
    public String toString() {
        return "Cupón #" + id_cupon + " - " + descuento + "% (" +
                (promocion != null ? promocion.getDescripcion() : "Sin descripción") + ")";
    }
}
