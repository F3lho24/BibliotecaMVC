// DAO: ElementoBibliotecaDAO.java
package com.biblioteca.model.dao;

import ElementoBiblioteca;
import com.biblioteca.model.Revista;
import com.biblioteca.view.PanelDVDs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class ElementoBibliotecaDAO<T extends ElementoBiblioteca> {

    protected Connection conexion;

    public ElementoBibliotecaDAO() throws SQLException {
        this.conexion = PanelDVDs.ConexionBD.getConnection();
    }

    // Método para insertar un elemento en la tabla base
    protected int insertarElementoBase(T elemento) throws SQLException {
        String query = "INSERT INTO ElementoBiblioteca (titulo, autor, ano_publicacion, tipo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, elemento.getTitulo());
            stmt.setString(2, elemento.getAutor());
            stmt.setInt(3, elemento.getAnoPublicacion());
            stmt.setString(4, elemento.getTipo());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException("La inserción falló, no se guardó ningún registro.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("La inserción falló, no se pudo obtener el ID generado.");
                }
            }
        }
    }

    // Método para actualizar un elemento en la tabla base
    protected void actualizarElementoBase(T elemento) throws SQLException {
        String query = "UPDATE ElementoBiblioteca SET titulo = ?, autor = ?, ano_publicacion = ? WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, elemento.getTitulo());
            stmt.setString(2, elemento.getAutor());
            stmt.setInt(3, elemento.getAnoPublicacion());
            stmt.setInt(4, elemento.getId());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException("La actualización falló, no se encontró el elemento con ID " + elemento.getId());
            }
        }
    }

    // Método para eliminar un elemento y todas sus referencias
    public boolean eliminar(int id) throws SQLException {
        String query = "DELETE FROM ElementoBiblioteca WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    // Métodos abstractos que deben implementar las clases hijas
    public abstract T obtenerPorId(int id) throws SQLException;
    public abstract List<T> obtenerTodos() throws SQLException;
    public abstract boolean insertar(T elemento) throws SQLException;
    public abstract boolean actualizar(T elemento) throws SQLException;
}


// DAO: LibroDAO.java
package com.biblioteca.model.dao;

import com.biblioteca.model.Libro;
import java.sql.*;
        import java.util.ArrayList;
import java.util.List;

public class LibroDAO extends ElementoBibliotecaDAO<Libro> {

    public LibroDAO() throws SQLException {
        super();
    }

    @Override
    public Libro obtenerPorId(int id) throws SQLException {
        String query = "SELECT e.id, e.titulo, e.autor, e.ano_publicacion, " +
                "l.isbn, l.numero_paginas, l.genero, l.editorial " +
                "FROM ElementoBiblioteca e " +
                "JOIN Libro l ON e.id = l.id " +
                "WHERE e.id = ? AND e.tipo = 'LIBRO'";

        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Libro(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getInt("ano_publicacion"),
                            rs.getString("isbn"),
                            rs.getInt("numero_paginas"),
                            rs.getString("genero"),
                            rs.getString("editorial")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Libro> obtenerTodos() throws SQLException {
        List<Libro> libros = new ArrayList<>();
        String query = "SELECT e.id, e.titulo, e.autor, e.ano_publicacion, " +
                "l.isbn, l.numero_paginas, l.genero, l.editorial " +
                "FROM ElementoBiblioteca e " +
                "JOIN Libro l ON e.id = l.id " +
                "WHERE e.tipo = 'LIBRO'";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                libros.add(new Libro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("ano_publicacion"),
                        rs.getString("isbn"),
                        rs.getInt("numero_paginas"),
                        rs.getString("genero"),
                        rs.getString("editorial")
                ));
            }
        }
        return libros;
    }

    @Override
    public boolean insertar(Libro libro) throws SQLException {
        conexion.setAutoCommit(false);
        try {
            // Inserta en la tabla base y obtiene el ID generado
            int id = insertarElementoBase(libro);
            libro.setId(id);

            // Inserta en la tabla Libro
            String query = "INSERT INTO Libro (id, isbn, numero_paginas, genero, editorial) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conexion.prepareStatement(query)) {
                stmt.setInt(1, id);
                stmt.setString(2, libro.getIsbn());
                stmt.setInt(3, libro.getNumeroPaginas());
                stmt.setString(4, libro.getGenero());
                stmt.setString(5, libro.getEditorial());

                int filasAfectadas = stmt.executeUpdate();
                if (filasAfectadas > 0) {
                    conexion.commit();
                    return true;
                } else {
                    conexion.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    @Override
    public boolean actualizar(Libro libro) throws SQLException {
        conexion.setAutoCommit(false);
        try {
            // Actualiza la tabla base
            actualizarElementoBase(libro);

            // Actualiza la tabla Libro
            String query = "UPDATE Libro SET isbn = ?, numero_paginas = ?, genero = ?, editorial = ? WHERE id = ?";
            try (PreparedStatement stmt = conexion.prepareStatement(query)) {
                stmt.setString(1, libro.getIsbn());
                stmt.setInt(2, libro.getNumeroPaginas());
                stmt.setString(3, libro.getGenero());
                stmt.setString(4, libro.getEditorial());
                stmt.setInt(5, libro.getId());

                int filasAfectadas = stmt.executeUpdate();
                if (filasAfectadas > 0) {
                    conexion.commit();
                    return true;
                } else {
                    conexion.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    public List<Libro> buscarPorTitulo(String titulo) throws SQLException {
        List<Libro> libros = new ArrayList<>();
        String query = "SELECT e.id, e.titulo, e.autor, e.ano_publicacion, " +
                "l.isbn, l.numero_paginas, l.genero, l.editorial " +
                "FROM ElementoBiblioteca e " +
                "JOIN Libro l ON e.id = l.id " +
                "WHERE e.tipo = 'LIBRO' AND e.titulo LIKE ?";

        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, "%" + titulo + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    libros.add(new Libro(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getInt("ano_publicacion"),
                            rs.getString("isbn"),
                            rs.getInt("numero_paginas"),
                            rs.getString("genero"),
                            rs.getString("editorial")
                    ));
                }
            }
        }
        return libros;
    }
}

// DAO: RevistaDAO.java
package com.biblioteca.model.dao;

import com.biblioteca.model.Revista;
import java.sql.*;
        import java.util.ArrayList;
import java.util.List;

public class RevistaDAO extends ElementoBibliotecaDAO<Revista> {

    public RevistaDAO() throws SQLException {
        super();
    }

    @Override
    public Revista obtenerPorId(int id) throws SQLException {
        String query = "SELECT e.id, e.titulo, e.autor, e.ano_publicacion, " +
                "r.numero_edicion, r.categoria " +
                "FROM ElementoBiblioteca e " +
                "JOIN Revista r ON e.id = r.id " +
                "WHERE e.id = ? AND e.tipo = 'REVISTA'";

        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Revista(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getInt("ano_publicacion"),
                            rs.getInt("numero_edicion"),
                            rs.getString("categoria")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Revista> obtenerTodos() throws SQLException {
        List<Revista> revistas = new ArrayList<>();
        String query = "SELECT e.id, e.titulo, e.autor, e.ano_publicacion, " +
                "r.numero_edicion, r.categoria " +
                "FROM ElementoBiblioteca e " +
                "JOIN Revista r ON e.id = r.id " +
                "WHERE e.tipo = 'REVISTA'";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                revistas.add(new Revista(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("ano_publicacion"),
                        rs.getInt("numero_edicion"),
                        rs.getString("categoria")
                ));
            }
        }
        return revistas;
    }

    @Override
    public boolean insertar(Revista revista) throws SQLException {
        conexion.setAutoCommit(false);
        try {
            // Inserta en la tabla base y obtiene el ID generado
            int id = insertarElementoBase(revista);
            revista.setId(id);

            // Inserta en la tabla Revista
            String query = "INSERT INTO Revista (id, numero_edicion, categoria) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conexion.prepareStatement(query)) {
                stmt.setInt(1, id);
                stmt.setInt(2, revista.getNumeroEdicion());
                stmt.setString(3, revista.getCategoria());

                int filasAfectadas = stmt.executeUpdate();
                if (filasAfectadas > 0) {
                    conexion.commit();
                    return true;
                } else {
                    conexion.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    @Override
    public boolean actualizar(Revista revista) throws SQLException {
        conexion.setAutoCommit(false);
        try {
            // Actualiza la tabla base
            actualizarElementoBase(revista);

            // Actualiza la tabla Revista
            String query = "UPDATE Revista SET numero_edicion = ?, categoria = ? WHERE id = ?";
            try (PreparedStatement stmt = conexion.prepareStatement(query)) {
                stmt.setInt(1, revista.getNumeroEdicion());
                stmt.setString(2, revista.getCategoria());
                stmt.setInt(3, revista.getId());

                int filasAfectadas = stmt.executeUpdate();
                if (filasAfectadas > 0) {
                    conexion.commit();
                    return true;
                } else {
                    conexion.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    public List<Revista> buscarPorCategoria(String categoria) throws SQLException {
        List<Revista> revistas = new ArrayList<>();
        String query = "SELECT e.id, e.titulo, e.autor, e.ano_publicacion, " +
                "r.numero_edicion, r.categoria " +
                "FROM ElementoBiblioteca e " +
                "JOIN Revista r ON e.id = r.id " +
                "WHERE e.tipo = 'REVISTA' AND r.categoria LIKE ?";

        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, "%" + categoria + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    revistas.add(new Revista(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getInt("ano_publicacion"),
                            rs.getInt("numero_edicion"),
                            rs.getString("categoria")
                    ));
                }
            }
        }
        return revistas;
    }
}

// DAO: DVDDAO.java
package com.biblioteca.model.dao;

import com.biblioteca.model.DVD;
import java.sql.*;
        import java.util.ArrayList;
import java.util.List;

public class DVDDAO extends ElementoBibliotecaDAO<DVD> {

    public DVDDAO() throws SQLException {
        super();
    }

    @Override
    public DVD obtenerPorId(int id) throws SQLException {
        String query = "SELECT e.id, e.titulo, e.autor, e.ano_publicacion, " +
                "d.duracion, d.genero " +
                "FROM ElementoBiblioteca e " +
                "JOIN DVD d ON e.id = d.id " +
                "WHERE e.id = ? AND e.tipo = 'DVD'";

        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new DVD(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getInt("ano_publicacion"),
                            rs.getInt("duracion"),
                            rs.getString("genero")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<DVD> obtenerTodos() throws SQLException {
        List<DVD> dvds = new ArrayList<>();
        String query = "SELECT e.id, e.titulo, e.autor, e.ano_publicacion, " +
                "d.duracion, d.genero " +
                "FROM ElementoBiblioteca e " +
                "JOIN DVD d ON e.id = d.id " +
                "WHERE e.tipo = 'DVD'";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                dvds.add(new DVD(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("ano_publicacion"),
                        rs.getInt("duracion"),
                        rs.getString("genero")
                ));
            }
        }
        return dvds;
    }

    @Override
    public boolean insertar(DVD dvd) throws SQLException {
        conexion.setAutoCommit(false);
        try {
            // Inserta en la tabla base y obtiene el ID generado
            int id = insertarElementoBase(dvd);
            dvd.setId(id);

            // Inserta en la tabla DVD
            String query = "INSERT INTO DVD (id, duracion, genero) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conexion.prepareStatement(query)) {
                stmt.setInt(1, id);
                stmt.setInt(2, dvd.getDuracion());
                stmt.setString(3, dvd.getGenero());

                int filasAfectadas = stmt.executeUpdate();
                if (filasAfectadas > 0) {
                    conexion.commit();
                    return true;
                } else {
                    conexion.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    @Override
    public boolean actualizar(DVD dvd) throws SQLException {
        conexion.setAutoCommit(false);
        try {
            // Actualiza la tabla base
            actualizarElementoBase(dvd);

            // Actualiza la tabla DVD
            String query = "UPDATE DVD SET duracion = ?, genero = ? WHERE id = ?";
            try (PreparedStatement stmt = conexion.prepareStatement(query)) {
                stmt.setInt(1, dvd.getDuracion());
                stmt.setString(2, dvd.getGenero());
                stmt.setInt(3, dvd.getId());

                int filasAfectadas = stmt.executeUpdate();
                if (filasAfectadas > 0) {
                    conexion.commit();
                    return true;
                } else {
                    conexion.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    public List<DVD> buscarPorGenero(String genero) throws SQLException {
        List<DVD> dvds = new ArrayList<>();
        String query = "SELECT e.id, e.titulo, e.autor, e.ano_publicacion, " +
                "d.duracion, d.genero " +
                "FROM ElementoBiblioteca e " +
                "JOIN DVD d ON e.id = d.id " +
                "WHERE e.tipo = 'DVD' AND d.genero LIKE ?";

        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, "%" + genero + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dvds.add(new DVD(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getInt("ano_publicacion"),
                            rs.getInt("duracion"),
                            rs.getString("genero")
                    ));
                }
            }
        }
        return dvds;
    }
}
