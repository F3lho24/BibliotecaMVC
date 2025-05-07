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