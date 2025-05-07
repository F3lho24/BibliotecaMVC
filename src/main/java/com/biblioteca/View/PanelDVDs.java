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