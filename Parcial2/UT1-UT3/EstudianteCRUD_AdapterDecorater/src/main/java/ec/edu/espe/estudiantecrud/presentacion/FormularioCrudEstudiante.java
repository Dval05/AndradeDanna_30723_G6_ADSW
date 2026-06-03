package ec.edu.espe.estudiantecrud.presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import ec.edu.espe.estudiantecrud.logica.ControlEstudiante;
import ec.edu.espe.estudiantecrud.logica.Estudiante;

public class FormularioCrudEstudiante extends JFrame {
    private final ControlEstudiante controlador;
    private JTextField txtId, txtNombre, txtEdad;
    private DefaultTableModel modeloTabla;
    private JTable tablaEstudiantes;
    private final Color colorFondo = new Color(245, 247, 250);
    private final Color colorPrimario = new Color(30, 35, 50);
    private final Color colorAcento = new Color(235, 140, 60);
    private final Color colorTexto = new Color(40, 45, 60);
    private final Font fuenteTitulo = new Font("Serif", Font.BOLD, 22);
    private final Font fuenteEtiqueta = new Font("SansSerif", Font.PLAIN, 14);
    private final Font fuenteBoton = new Font("SansSerif", Font.BOLD, 13);

    public FormularioCrudEstudiante() {
        this.controlador = new ControlEstudiante();
        configurarVentana();
        inicializarComponentes();
        configurarValidaciones();
    }

    private void configurarVentana() {
        setTitle("EstudianteCRUD - ESPE");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));
        setLocationRelativeTo(null);
        getContentPane().setBackground(colorFondo);
    }

    private void inicializarComponentes() {
        JPanel pnlNorte = new JPanel(new BorderLayout(10, 10));
        pnlNorte.setBorder(BorderFactory.createEmptyBorder(12, 12, 6, 12));
        pnlNorte.setBackground(colorFondo);

        JLabel titulo = new JLabel("Gestion de Estudiantes");
        titulo.setFont(fuenteTitulo);
        titulo.setForeground(colorPrimario);
        pnlNorte.add(titulo, BorderLayout.NORTH);

        JPanel pnlDatos = new JPanel(new GridBagLayout());
        pnlDatos.setBackground(colorFondo);
        pnlDatos.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 235)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        pnlDatos.add(crearEtiqueta("ID:"), gbc);
        gbc.gridx = 1;
        txtId = crearCampoTexto();
        pnlDatos.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pnlDatos.add(crearEtiqueta("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = crearCampoTexto();
        pnlDatos.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        pnlDatos.add(crearEtiqueta("Edad:"), gbc);
        gbc.gridx = 1;
        txtEdad = crearCampoTexto();
        pnlDatos.add(txtEdad, gbc);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlBotones.setBackground(colorFondo);

        JButton btnAdd = crearBotonPrimario("Agregar");
        btnAdd.addActionListener(e -> clickAgregar());
        JButton btnUpdate = crearBotonSecundario("Actualizar");
        btnUpdate.addActionListener(e -> clickActualizar());
        JButton btnDelete = crearBotonSecundario("Eliminar");
        btnDelete.addActionListener(e -> clickEliminar());
        JButton btnShow = crearBotonOutline("Mostrar Tabla");
        btnShow.addActionListener(e -> clickMostrarTodo());

        pnlBotones.add(btnAdd);
        pnlBotones.add(btnUpdate);
        pnlBotones.add(btnDelete);
        pnlBotones.add(btnShow);

        JPanel pnlCentro = new JPanel(new BorderLayout(10, 10));
        pnlCentro.setBackground(colorFondo);
        pnlCentro.add(pnlDatos, BorderLayout.CENTER);
        pnlCentro.add(pnlBotones, BorderLayout.SOUTH);

        pnlNorte.add(pnlCentro, BorderLayout.CENTER);
        add(pnlNorte, BorderLayout.NORTH);

        // CONFIGURACIÓN DE TABLA: No editable mediante isCellEditable
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Edad"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Desactiva la edición directa en la celda
            }
        };
        
        tablaEstudiantes = new JTable(modeloTabla);
        tablaEstudiantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEstudiantes.setRowHeight(24);
        tablaEstudiantes.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaEstudiantes.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tablaEstudiantes.getTableHeader().setBackground(new Color(230, 235, 245));
        tablaEstudiantes.getTableHeader().setForeground(colorTexto);
        
        // EVENTO DE SELECCIÓN: Autollenado y Bloqueo de ID
        tablaEstudiantes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { 
                int fila = tablaEstudiantes.getSelectedRow();
                if (fila != -1) {
                    txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtEdad.setText(modeloTabla.getValueAt(fila, 2).toString());
                    
                    txtId.setEditable(false); // Bloqueo de ID para integridad
                    txtNombre.requestFocus();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tablaEstudiantes);
        scroll.setBorder(BorderFactory.createEmptyBorder(6, 12, 12, 12));
        add(scroll, BorderLayout.CENTER);
    }

    public void clickAgregar() {
        try {
            int edad = parseEdad();
            if (edad < 0) {
                return;
            }
            String res = controlador.agregarEstudiante(txtId.getText(), txtNombre.getText(), edad);
            mostrarMensaje(res);
            limpiarCampos();
        } catch (Exception e) { mostrarMensaje("Error: Edad debe ser numérica"); }
    }


    public void clickActualizar() {
        try {
            int edad = parseEdad();
            if (edad < 0) {
                return;
            }
            String res = controlador.actualizarEstudiante(txtId.getText(), txtNombre.getText(), edad);
            mostrarMensaje(res);
            limpiarCampos();
        } catch (Exception e) { mostrarMensaje("Error en actualización"); }
    }

    public void clickEliminar() {
        String id = txtId.getText();
        if (id.isEmpty()) {
            mostrarMensaje("Por favor, seleccione un estudiante para eliminar.");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de que desea eliminar al estudiante con ID: " + id + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            String res = controlador.eliminarEstudiante(id);
            mostrarMensaje(res);
            limpiarCampos();
            clickMostrarTodo();
        }
    }

    public void clickMostrarTodo() {
        mostrarTabla(controlador.mostrarTodos());
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void mostrarTabla(List<Estudiante> estudiantes) {
        modeloTabla.setRowCount(0);
        for (Estudiante e : estudiantes) {
            modeloTabla.addRow(new Object[]{e.getId(), e.getNombre(), e.getEdad()});
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtEdad.setText("");
        txtId.setEditable(true); // Habilita ID para nuevos registros
        tablaEstudiantes.clearSelection();
    }

    private void configurarValidaciones() {
        ((AbstractDocument) txtId.getDocument()).setDocumentFilter(new SoloNumerosFilter(10));
        ((AbstractDocument) txtEdad.getDocument()).setDocumentFilter(new SoloNumerosFilter(3));
        ((AbstractDocument) txtNombre.getDocument()).setDocumentFilter(new SoloLetrasFilter(60));
    }

    private int parseEdad() {
        String texto = txtEdad.getText().trim();
        if (texto.isEmpty()) {
            mostrarMensaje("Error: Edad es obligatoria.");
            return -1;
        }
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException ex) {
            mostrarMensaje("Error: Edad debe ser numérica.");
            return -1;
        }
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(fuenteEtiqueta);
        etiqueta.setForeground(colorTexto);
        return etiqueta;
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(fuenteEtiqueta);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 225)),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
        return campo;
    }

    private JButton crearBotonPrimario(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(fuenteBoton);
        boton.setForeground(Color.WHITE);
        boton.setBackground(colorAcento);
        boton.setFocusPainted(false);
        return boton;
    }

    private JButton crearBotonSecundario(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(fuenteBoton);
        boton.setForeground(colorPrimario);
        boton.setBackground(new Color(235, 238, 245));
        boton.setFocusPainted(false);
        return boton;
    }

    private JButton crearBotonOutline(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(fuenteBoton);
        boton.setForeground(colorPrimario);
        boton.setBackground(Color.WHITE);
        boton.setBorder(BorderFactory.createLineBorder(new Color(200, 205, 215)));
        boton.setFocusPainted(false);
        return boton;
    }

    private static class SoloNumerosFilter extends DocumentFilter {
        private final int maxLen;

        private SoloNumerosFilter(int maxLen) {
            this.maxLen = maxLen;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string != null && esValido(fb.getDocument().getLength(), string)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text != null && esValido(fb.getDocument().getLength() - length, text)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        private boolean esValido(int longitudActual, String textoNuevo) {
            if (!textoNuevo.matches("\\d+")) {
                return false;
            }
            return longitudActual + textoNuevo.length() <= maxLen;
        }
    }

    private static class SoloLetrasFilter extends DocumentFilter {
        private final int maxLen;

        private SoloLetrasFilter(int maxLen) {
            this.maxLen = maxLen;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string != null && esValido(fb.getDocument().getLength(), string)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text != null && esValido(fb.getDocument().getLength() - length, text)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        private boolean esValido(int longitudActual, String textoNuevo) {
            if (!textoNuevo.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
                return false;
            }
            return longitudActual + textoNuevo.length() <= maxLen;
        }
    }
}
