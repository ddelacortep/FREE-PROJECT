import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/free_project";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Cd@69440";

    public static void insertarResultado(String iniciales, int puntos) {
        Connection con = null;
        PreparedStatement psUsuario = null;
        PreparedStatement psPuntuacion = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            // 1. Verificamos si el usuario ya existe
            String selectUser = "SELECT id_usuari FROM usuaris WHERE nom = ?";
            psUsuario = con.prepareStatement(selectUser);
            psUsuario.setString(1, iniciales);
            rs = psUsuario.executeQuery();

            int idUsuario;
            if (rs.next()) {
                // Ya existe
                idUsuario = rs.getInt("id_usuari");
            } else {
                // No existe, lo insertamos
                String insertUser = "INSERT INTO usuaris (nom) VALUES (?)";
                psUsuario = con.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS);
                psUsuario.setString(1, iniciales);
                psUsuario.executeUpdate();

                rs = psUsuario.getGeneratedKeys();
                rs.next();
                idUsuario = rs.getInt(1);
            }

            // 2. Insertar puntuación
            String insertScore = "INSERT INTO puntuacio (id_usuari, punts) VALUES (?, ?)";
            psPuntuacion = con.prepareStatement(insertScore);
            psPuntuacion.setInt(1, idUsuario);
            psPuntuacion.setInt(2, puntos);
            psPuntuacion.executeUpdate();

            System.out.println("Puntuación guardada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al guardar datos en la base de datos:");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (psUsuario != null) psUsuario.close();
                if (psPuntuacion != null) psPuntuacion.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}