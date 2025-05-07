// Modelo: Revista.java
package com.biblioteca.model;

public class Revista extends ElementoBiblioteca {
    private int numeroEdicion;
    private String categoria;

    // Constructor completo
    public Revista(int id, String titulo, String autor, int anoPublicacion,
                   int numeroEdicion, String categoria) {
        super(id, titulo, autor, anoPublicacion, "REVISTA");
        this.numeroEdicion = numeroEdicion;
        this.categoria = categoria;
    }

    // Constructor sin ID (para nuevas revistas)
    public Revista(String titulo, String autor, int anoPublicacion,
                   int numeroEdicion, String categoria) {
        super(titulo, autor, anoPublicacion, "REVISTA");
        this.numeroEdicion = numeroEdicion;
        this.categoria = categoria;
    }

    // Getters y Setters
    public int getNumeroEdicion() {
        return numeroEdicion;
    }

    public void setNumeroEdicion(int numeroEdicion) {
        this.numeroEdicion = numeroEdicion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return super.toString() + " - Edición: " + numeroEdicion + " - " + categoria;
    }
}