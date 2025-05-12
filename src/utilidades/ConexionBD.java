package utilidades;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String url = "jdbc:mysql://localhost:3306/creacionbd";
    private static final String usuario = "root";
    private static final String contrasenya = "2002";

    public static Connection obtenerConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error cargando el driver", e);
        }
        return DriverManager.getConnection(url, usuario, contrasenya);
    }
}