// Modelo: Libro.java
package com.biblioteca.model;

public class Libro extends ElementoBiblioteca {
    private String isbn;
    private int numeroPaginas;
    private String genero;
    private String editorial;

    // Constructor completo
    public Libro(int id, String titulo, String autor, int anoPublicacion,
                 String isbn, int numeroPaginas, String genero, String editorial) {
        super(id, titulo, autor, anoPublicacion, "LIBRO");
        this.isbn = isbn;
        this.numeroPaginas = numeroPaginas;
        this.genero = genero;
        this.editorial = editorial;
    }

    // Constructor sin ID (para nuevos libros)
    public Libro(String titulo, String autor, int anoPublicacion,
                 String isbn, int numeroPaginas, String genero, String editorial) {
        super(titulo, autor, anoPublicacion, "LIBRO");
        this.isbn = isbn;
        this.numeroPaginas = numeroPaginas;
        this.genero = genero;
        this.editorial = editorial;
    }

    // Getters y Setters
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    public void setNumeroPaginas(int numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    @Override
    public String toString() {
        return super.toString() + " - " + editorial + " - ISBN: " + isbn;
    }
}