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
    private com.biblioteca.view.PanelDVDs.LibroController controller;

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
        this.controller = new com.biblioteca.view.PanelDVDs.LibroController();

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