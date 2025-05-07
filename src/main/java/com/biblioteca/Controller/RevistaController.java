// Controlador: RevistaController.java
package com.biblioteca.controller;

import com.biblioteca.model.Revista;
import com.biblioteca.model.dao.RevistaDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RevistaController extends BibliotecaController<Revista> {
    private RevistaDAO revistaDAO;

    public RevistaController() throws SQLException {
        super(new RevistaDAO());
        this.revistaDAO = (RevistaDAO) this.dao;
    }

    public List<Revista> buscarPorCategoria(String categoria) {
        try {
            return revistaDAO.buscarPorCategoria(categoria);
        } catch (SQLException e) {
            System.err.println("Error al buscar revistas por categoría: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}