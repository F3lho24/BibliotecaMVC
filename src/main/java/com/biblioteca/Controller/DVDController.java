// Controlador: DVDController.java
package com.biblioteca.controller;

import com.biblioteca.model.DVD;
import com.biblioteca.model.dao.DVDDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DVDController extends BibliotecaController<DVD> {
    private DVDDAO dvdDAO;

    public DVDController() throws SQLException {
        super(new DVDDAO());
        this.dvdDAO = (DVDDAO) this.dao;
    }

    public List<DVD> buscarPorGenero(String genero) {
        try {
            return dvdDAO.buscarPorGenero(genero);
        } catch (SQLException e) {
            System.err.println("Error al buscar DVDs por género: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}