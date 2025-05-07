// Vista: MainFrame.java
package com.biblioteca.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private com.biblioteca.view.PanelDVDs.PanelPrincipal panelPrincipal;

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
        panelPrincipal = new com.biblioteca.view.PanelDVDs.PanelPrincipal(this);

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
