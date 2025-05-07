// Controlador: BibliotecaController.java
package com.biblioteca.controller;

import ElementoBiblioteca;
import com.biblioteca.model.dao.ElementoBibliotecaDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BibliotecaController<T extends ElementoBiblioteca> {
    protected ElementoBibliotecaDAO<T> dao;

    public BibliotecaController(ElementoBibliotecaDAO<T> dao) {
        this.dao = dao;
    }

    public T obtenerPorId(int id) {
        try {
            return dao.obtenerPorId(id);
        } catch (SQLException e) {
            System.err.println("Error al obtener el elemento: " + e.getMessage());
            return null;
        }
    }

    public List<T> obtenerTodos() {
        try {
            return dao.obtenerTodos();
        } catch (SQLException e) {
            System.err.println("Error al obtener los elementos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean guardar(T elemento) {
        try {
            if (elemento.getId() < 0) {
                return dao.insertar(elemento);
            } else {
                return dao.actualizar(elemento);
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar el elemento: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        try {
            return dao.eliminar(id);
        } catch (SQLException e) {
            System.err.println("Error al eliminar el elemento: " + e.getMessage());
            return false;
        }
    }
}
