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




// Vista: PanelRevistas.java
package com.biblioteca.view;

import com.biblioteca.controller.RevistaController;
import com.biblioteca.model.Revista;
import javax.swing.*;
        import javax.swing.table.DefaultTableModel;
import java.awt.*;
        import java.sql.SQLException;
import java.util.List;

public class PanelRevistas extends JPanel {
    private MainFrame parent;
    private RevistaController controller;

    private JTable tablaRevistas;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnActualizar;

    public PanelRevistas(MainFrame parent) throws SQLException {
        this.parent = parent;
        this.controller = new RevistaController();

        setLayout(new BorderLayout());
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Panel de búsqueda y acciones
        JPanel panelSuperior = new JPanel(new BorderLayout());

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBuscar = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarRevistas());

        panelBusqueda.add(new JLabel("Buscar por categoría: "));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);

        // Panel de acciones
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnActualizar = new JButton("Actualizar");

        btnAgregar.addActionListener(e -> agregarRevista());
        btnEditar.addActionListener(e -> editarRevista());
        btnEliminar.addActionListener(e -> eliminarRevista());
        btnActualizar.addActionListener(e -> actualizarTabla());

        panelAcciones.add(btnAgregar);
        panelAcciones.add(btnEditar);
        panelAcciones.add(btnEliminar);
        panelAcciones.add(btnActualizar);

        panelSuperior.add(panelBusqueda, BorderLayout.WEST);
        panelSuperior.add(panelAcciones, BorderLayout.EAST);

        // Tabla de revistas
        modeloTabla = new DefaultTableModel(
                new Object[][] {},
                new String[] {"ID", "Título", "Autor", "Año", "Edición", "Categoría"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaRevistas = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaRevistas);
        tablaRevistas.setFillsViewportHeight(true);

        // Configuración de la tabla
        tablaRevistas.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
        tablaRevistas.getColumnModel().getColumn(1).setPreferredWidth(200); // Título
        tablaRevistas.getColumnModel().getColumn(2).setPreferredWidth(150); // Autor
        tablaRevistas.getColumnModel().getColumn(3).setPreferredWidth(60);  // Año
        tablaRevistas.getColumnModel().getColumn(4).setPreferredWidth(60);  // Edición
        tablaRevistas.getColumnModel().getColumn(5).setPreferredWidth(150); // Categoría

        // Panel principal
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Cargar datos iniciales
        actualizarTabla();
    }

    public void actualizarTabla() {
        // Limpiar tabla
        modeloTabla.setRowCount(0);

        // Obtener revistas y agregarlos a la tabla
        List<Revista> revistas = controller.obtenerTodos();
        for (Revista revista : revistas) {
            modeloTabla.addRow(new Object[] {
                    revista.getId(),
                    revista.getTitulo(),
                    revista.getAutor(),
                    revista.getAnoPublicacion(),
                    revista.getNumeroEdicion(),
                    revista.getCategoria()
            });
        }
    }

    private void buscarRevistas() {
        String terminoBusqueda = txtBuscar.getText().trim();

        if (terminoBusqueda.isEmpty()) {
            actualizarTabla();
            return;
        }

        // Limpiar tabla
        modeloTabla.setRowCount(0);

        // Buscar revistas y agregarlos a la tabla
        List<Revista> revistas = controller.buscarPorCategoria(terminoBusqueda);
        for (Revista revista : revistas) {
            modeloTabla.addRow(new Object[] {
                    revista.getId(),
                    revista.getTitulo(),
                    revista.getAutor(),
                    revista.getAnoPublicacion(),
                    revista.getNumeroEdicion(),
                    revista.getCategoria()
            });
        }
    }

    private void agregarRevista() {
        DialogoAgregarElemento dialogo = new DialogoAgregarElemento(parent, "Agregar Revista", "REVISTA");
        Revista revista = (Revista) dialogo.mostrar();

        if (revista != null) {
            if (controller.guardar(revista)) {
                JOptionPane.showMessageDialog(this,
                        "Revista agregada correctamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                actualizarTabla();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al agregar la revista",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarRevista() {
        int filaSeleccionada = tablaRevistas.getSelectedRow();

        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar una revista para editar",
                    "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tablaRevistas.getValueAt(filaSeleccionada, 0);
        Revista revista = controller.obtenerPorId(id);

        if (revista != null) {
            DialogoAgregarElemento dialogo = new DialogoAgregarElemento(parent, "Editar Revista", "REVISTA", revista);
            Revista revistaEditada = (Revista) dialogo.mostrar();

            if (revistaEditada != null) {
                if (controller.guardar(revistaEditada)) {
                    JOptionPane.showMessageDialog(this,
                            "Revista actualizada correctamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    actualizarTabla();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error al actualizar la revista",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void eliminarRevista() {
        int filaSeleccionada = tablaRevistas.getSelectedRow();

        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar una revista para eliminar",
                    "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tablaRevistas.getValueAt(filaSeleccionada, 0);
        String titulo = (String) tablaRevistas.getValueAt(filaSeleccionada, 1);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar la revista \"" + titulo + "\"?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (controller.eliminar(id)) {
                JOptionPane.showMessageDialog(this,
                        "Revista eliminada correctamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                actualizarTabla();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar la revista",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

// Vista: PanelDVDs.java
package com.biblioteca.view;

import com.biblioteca.controller.DVDController;
import com.biblioteca.model.DVD;
import javax.swing.*;
        import javax.swing.table.DefaultTableModel;
import java.awt.*;
        import java.sql.SQLException;
import java.util.List;

public class PanelDVDs extends JPanel {
    private MainFrame parent;
    private DVDController controller;

    private JTable tablaDVDs;
    private DefaultTableModel modeloTabla










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

// Vista: MainFrame.java
package com.biblioteca.view;

import javax.swing.*;
import java.awt.*;

    public class MainFrame extends JFrame {
        private PanelPrincipal panelPrincipal;

        public MainFrame() {
            configurarVentana();
            inicializarComponentes();
        }

        private void configurarVentana() {
            setTitle("Sistema de Biblioteca");
            setSize(900, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
        }

        private void inicializarComponentes() {
            // Panel principal con menú de navegación
            panelPrincipal = new PanelPrincipal(this);

            // Menú superior
            JMenuBar menuBar = crearMenuBar();
            setJMenuBar(menuBar);

            // Añadir panel principal al frame
            getContentPane().add(panelPrincipal, BorderLayout.CENTER);
        }

        private JMenuBar crearMenuBar() {
            JMenuBar menuBar = new JMenuBar();

            // Menú Archivo
            JMenu menuArchivo = new JMenu("Archivo");
            JMenuItem itemSalir = new JMenuItem("Salir");
            itemSalir.addActionListener(e -> System.exit(0));
            menuArchivo.add(itemSalir);

            // Menú Catálogo
            JMenu menuCatalogo = new JMenu("Catálogo");
            JMenuItem itemLibros = new JMenuItem("Libros");
            JMenuItem itemRevistas = new JMenuItem("Revistas");
            JMenuItem itemDVDs = new JMenuItem("DVDs");

            itemLibros.addActionListener(e -> panelPrincipal.mostrarPanelLibros());
            itemRevistas.addActionListener(e -> panelPrincipal.mostrarPanelRevistas());
            itemDVDs.addActionListener(e -> panelPrincipal.mostrarPanelDVDs());

            menuCatalogo.add(itemLibros);
            menuCatalogo.add(itemRevistas);
            menuCatalogo.add(itemDVDs);

            // Menú Ayuda
            JMenu menuAyuda = new JMenu("Ayuda");
            JMenuItem itemAcercaDe = new JMenuItem("Acerca de");
            itemAcercaDe.addActionListener(e ->
                    JOptionPane.showMessageDialog(this,
                            "Sistema de Biblioteca v1.0\nDesarrollado con Java y Swing",
                            "Acerca de", JOptionPane.INFORMATION_MESSAGE)
            );
            menuAyuda.add(itemAcercaDe);

            // Añadir menús a la barra
            menuBar.add(menuArchivo);
            menuBar.add(menuCatalogo);
            menuBar.add(menuAyuda);

            return menuBar;
        }

        public static void main(String[] args) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            SwingUtilities.invokeLater(() -> {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            });
        }
    }

// Vista: PanelPrincipal.java
package com.biblioteca.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

    public class PanelPrincipal extends JPanel {
        private MainFrame parent;
        private CardLayout cardLayout;
        private JPanel contenedor;

        private PanelLibros panelLibros;
        private PanelRevistas panelRevistas;
        private PanelDVDs panelDVDs;

        private JToolBar toolBar;

        public PanelPrincipal(MainFrame parent) {
            this.parent = parent;
            setLayout(new BorderLayout());

            inicializarToolBar();
            inicializarPaneles();
        }

        private void inicializarToolBar() {
            toolBar = new JToolBar();
            toolBar.setFloatable(false);

            JButton btnLibros = new JButton("Libros");
            JButton btnRevistas = new JButton("Revistas");
            JButton btnDVDs = new JButton("DVDs");

            btnLibros.addActionListener(e -> mostrarPanelLibros());
            btnRevistas.addActionListener(e -> mostrarPanelRevistas());
            btnDVDs.addActionListener(e -> mostrarPanelDVDs());

            toolBar.add(btnLibros);
            toolBar.add(new JToolBar.Separator());
            toolBar.add(btnRevistas);
            toolBar.add(new JToolBar.Separator());
            toolBar.add(btnDVDs);

            add(toolBar, BorderLayout.NORTH);
        }

        private void inicializarPaneles() {
            cardLayout = new CardLayout();
            contenedor = new JPanel(cardLayout);

            try {
                panelLibros = new PanelLibros(parent);
                panelRevistas = new PanelRevistas(parent);
                panelDVDs = new PanelDVDs(parent);

                contenedor.add(panelLibros, "LIBROS");
                contenedor.add(panelRevistas, "REVISTAS");
                contenedor.add(panelDVDs, "DVDS");

                // Panel de bienvenida
                JPanel panelBienvenida = new JPanel(new BorderLayout());
                JLabel lblBienvenida = new JLabel("Bienvenido al Sistema de Biblioteca", JLabel.CENTER);
                lblBienvenida.setFont(new Font("Arial", Font.BOLD, 24));

                JLabel lblInstrucciones = new JLabel("<html><center>Seleccione una opción en la barra de herramientas<br>para gestionar los elementos de la biblioteca.</center></html>", JLabel.CENTER);

                panelBienvenida.add(lblBienvenida, BorderLayout.CENTER);
                panelBienvenida.add(lblInstrucciones, BorderLayout.SOUTH);

                contenedor.add(panelBienvenida, "BIENVENIDA");

                add(contenedor, BorderLayout.CENTER);
                cardLayout.show(contenedor, "BIENVENIDA");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(parent,
                        "Error al inicializar los paneles: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        public void mostrarPanelLibros() {
            panelLibros.actualizarTabla();
            cardLayout.show(contenedor, "LIBROS");
        }

        public void mostrarPanelRevistas() {
            panelRevistas.actualizarTabla();
            cardLayout.show(contenedor, "REVISTAS");
        }

        public void mostrarPanelDVDs() {
            panelDVDs.actualizarTabla();
            cardLayout.show(contenedor, "DVDS");
        }
    }

// Vista: PanelLibros.java
package com.biblioteca.view;

import com.biblioteca.controller.LibroController;
import com.biblioteca.model.Libro;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

    public class PanelLibros extends JPanel {
        private MainFrame parent;
        private LibroController controller;

        private JTable tablaLibros;
        private DefaultTableModel modeloTabla;
        private JTextField txtBuscar;
        private JButton btnBuscar;
        private JButton btnAgregar;
        private JButton btnEditar;
        private JButton btnEliminar;
        private JButton btnActualizar;

        public PanelLibros(MainFrame parent) throws SQLException {
            this.parent = parent;
            this.controller = new LibroController();

            setLayout(new BorderLayout());
            inicializarComponentes();
        }

        private void inicializarComponentes() {
            // Panel de búsqueda y acciones
            JPanel panelSuperior = new JPanel(new BorderLayout());

            // Panel de búsqueda
            JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
            txtBuscar = new JTextField(20);
            btnBuscar = new JButton("Buscar");
            btnBuscar.addActionListener(e -> buscarLibros());

            panelBusqueda.add(new JLabel("Buscar por título: "));
            panelBusqueda.add(txtBuscar);
            panelBusqueda.add(btnBuscar);

            // Panel de acciones
            JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btnAgregar = new JButton("Agregar");
            btnEditar = new JButton("Editar");
            btnEliminar = new JButton("Eliminar");
            btnActualizar = new JButton("Actualizar");

            btnAgregar.addActionListener(e -> agregarLibro());
            btnEditar.addActionListener(e -> editarLibro());
            btnEliminar.addActionListener(e -> eliminarLibro());
            btnActualizar.addActionListener(e -> actualizarTabla());

            panelAcciones.add(btnAgregar);
            panelAcciones.add(btnEditar);
            panelAcciones.add(btnEliminar);
            panelAcciones.add(btnActualizar);

            panelSuperior.add(panelBusqueda, BorderLayout.WEST);
            panelSuperior.add(panelAcciones, BorderLayout.EAST);

            // Tabla de libros
            modeloTabla = new DefaultTableModel(
                    new Object[][] {},
                    new String[] {"ID", "Título", "Autor", "Año", "ISBN", "Páginas", "Género", "Editorial"}
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            tablaLibros = new JTable(modeloTabla);
            JScrollPane scrollPane = new JScrollPane(tablaLibros);
            tablaLibros.setFillsViewportHeight(true);

            // Configuración de la tabla
            tablaLibros.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
            tablaLibros.getColumnModel().getColumn(1).setPreferredWidth(200); // Título
            tablaLibros.getColumnModel().getColumn(2).setPreferredWidth(150); // Autor
            tablaLibros.getColumnModel().getColumn(3).setPreferredWidth(60);  // Año
            tablaLibros.getColumnModel().getColumn(4).setPreferredWidth(100); // ISBN
            tablaLibros.getColumnModel().getColumn(5).setPreferredWidth(60);  // Páginas
            tablaLibros.getColumnModel().getColumn(6).setPreferredWidth(100); // Género
            tablaLibros.getColumnModel().getColumn(7).setPreferredWidth(150); // Editorial

            // Panel principal
            add(panelSuperior, BorderLayout.NORTH);
            add(scrollPane, BorderLayout.CENTER);

            // Cargar datos iniciales
            actualizarTabla();
        }

        public void actualizarTabla() {
            // Limpiar tabla
            modeloTabla.setRowCount(0);

            // Obtener libros y agregarlos a la tabla
            List<Libro> libros = controller.obtenerTodos();
            for (Libro libro : libros) {
                modeloTabla.addRow(new Object[] {
                        libro.getId(),
                        libro.getTitulo(),
                        libro.getAutor(),
                        libro.getAnoPublicacion(),
                        libro.getIsbn(),
                        libro.getNumeroPaginas(),
                        libro.getGenero(),
                        libro.getEditorial()
                });
            }
        }

        private void buscarLibros() {
            String terminoBusqueda = txtBuscar.getText().trim();

            if (terminoBusqueda.isEmpty()) {
                actualizarTabla();
                return;
            }

            // Limpiar tabla
            modeloTabla.setRowCount(0);

            // Buscar libros y agregarlos a la tabla
            List<Libro> libros = controller.buscarPorTitulo(terminoBusqueda);
            for (Libro libro : libros) {
                modeloTabla.addRow(new Object[] {
                        libro.getId(),
                        libro.getTitulo(),
                        libro.getAutor(),
                        libro.getAnoPublicacion(),
                        libro.getIsbn(),
                        libro.getNumeroPaginas(),
                        libro.getGenero(),
                        libro.getEditorial()
                });
            }
        }

        private void agregarLibro() {
            DialogoAgregarElemento dialogo = new DialogoAgregarElemento(parent, "Agregar Libro", "LIBRO");
            Libro libro = (Libro) dialogo.mostrar();

            if (libro != null) {
                if (controller.guardar(libro)) {
                    JOptionPane.showMessageDialog(this,
                            "Libro agregado correctamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    actualizarTabla();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error al agregar el libro",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private void editarLibro() {
            int filaSeleccionada = tablaLibros.getSelectedRow();

            if (filaSeleccionada < 0) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar un libro para editar",
                        "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = (int) tablaLibros.getValueAt(filaSeleccionada, 0);
            Libro libro = controller.obtenerPorId(id);

            if (libro != null) {
                DialogoAgregarElemento dialogo = new DialogoAgregarElemento(parent, "Editar Libro", "LIBRO", libro);
                Libro libroEditado = (Libro) dialogo.mostrar();

                if (libroEditado != null) {
                    if (controller.guardar(libroEditado)) {
                        JOptionPane.showMessageDialog(this,
                                "Libro actualizado correctamente",
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        actualizarTabla();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Error al actualizar el libro",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }

        private void eliminarLibro() {
            int filaSeleccionada = tablaLibros.getSelectedRow();

            if (filaSeleccionada < 0) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar un libro para eliminar",
                        "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = (int) tablaLibros.getValueAt(filaSeleccionada, 0);
            String titulo = (String) tablaLibros.getValueAt(filaSeleccionada, 1);

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar el libro \"" + titulo + "\"?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                if (controller.eliminar(id)) {
                    JOptionPane.showMessageDialog(this,
                            "Libro eliminado correctamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    actualizarTabla();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error al eliminar el libro",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }