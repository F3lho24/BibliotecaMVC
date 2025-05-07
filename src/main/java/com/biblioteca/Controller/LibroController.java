// Controlador: LibroController.java
package com.biblioteca.controller;

import com.biblioteca.model.Libro;
import com.biblioteca.model.dao.LibroDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibroController extends BibliotecaController<Libro> {
    private LibroDAO libroDAO;

    public LibroController() throws SQLException {
        super(new LibroDAO());
        this.libroDAO = (LibroDAO) this.dao;
    }

    public List<Libro> buscarPorTitulo(String titulo) {
        try {
            return libroDAO.buscarPorTitulo(titulo);
        } catch (SQLException e) {
            System.err.println("Error al buscar libros por título: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}