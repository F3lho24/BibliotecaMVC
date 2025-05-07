/* Estructura de directorios:
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── biblioteca/
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── ElementoBiblioteca.java
│   │   │   │   │   │   ├── Libro.java
│   │   │   │   │   │   ├── Revista.java
│   │   │   │   │   │   ├── DVD.java
│   │   │   │   │   │   └── dao/
│   │   │   │   │   │       ├── ElementoBibliotecaDAO.java
│   │   │   │   │   │       ├── LibroDAO.java
│   │   │   │   │   │       ├── RevistaDAO.java
│   │   │   │   │   │       ├── DVDDAO.java
│   │   │   │   │   │       └── ConexionBD.java
│   │   │   │   │   ├── view/
│   │   │   │   │   │   ├── MainFrame.java
│   │   │   │   │   │   ├── PanelPrincipal.java
│   │   │   │   │   │   ├── PanelLibros.java
│   │   │   │   │   │   ├── PanelRevistas.java
│   │   │   │   │   │   ├── PanelDVDs.java
│   │   │   │   │   │   ├── DialogoAgregarElemento.java
│   │   │   │   │   │   └── DialogoDetallesElemento.java
│   │   │   │   │   └── controller/
│   │   │   │   │       ├── BibliotecaController.java
│   │   │   │   │       ├── LibroController.java
│   │   │   │   │       ├── RevistaController.java
│   │   │   │   │       └── DVDController.java
│   │   │   │   └── Main.java
*/

// ---------------------- MODELO ----------------------

















// ---------------------- CONTROLADOR ----------------------

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

// ---------------------- VISTA ----------------------





