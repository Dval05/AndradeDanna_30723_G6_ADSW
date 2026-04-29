package ec.edu.espe.estudiantecrud.view;

import ec.edu.espe.estudiantecrud.controller.ControlEstudiante;
import ec.edu.espe.estudiantecrud.model.Estudiante;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FormularioCrudEstudiante extends JFrame {
    private final ControlEstudiante controlador;
    private JTextField txtId, txtNombre, txtEdad;
    private DefaultTableModel modeloTabla;
    private JTable tablaEstudiantes;

    public FormularioCrudEstudiante() {
        this.controlador = new ControlEstudiante();
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("EstudianteCRUD - ESPE");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));
        setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        // Panel de Datos
        JPanel pnlDatos = new JPanel(new GridLayout(4, 2, 5, 5));
        pnlDatos.setBorder(BorderFactory.createTitledBorder("Entrada de Datos"));

        pnlDatos.add(new JLabel("ID:")); txtId = new JTextField(); pnlDatos.add(txtId);
        pnlDatos.add(new JLabel("Nombre:")); txtNombre = new JTextField(); pnlDatos.add(txtNombre);
        pnlDatos.add(new JLabel("Edad:")); txtEdad = new JTextField(); pnlDatos.add(txtEdad);

        JButton btnAdd = new JButton("Agregar");
        btnAdd.addActionListener(e -> clickAgregar());
        pnlDatos.add(btnAdd);

        JButton btnShow = new JButton("Mostrar Tabla");
        btnShow.addActionListener(e -> clickMostrarTodo());
        pnlDatos.add(btnShow);

        // Panel de Botones CRUD
        JPanel pnlExtra = new JPanel(new FlowLayout());
        JButton btnUpdate = new JButton("Actualizar");
        btnUpdate.addActionListener(e -> clickActualizar());
        JButton btnDelete = new JButton("Eliminar");
        btnDelete.addActionListener(e -> clickEliminar1());
       // JButton btnLimpiar = new JButton("Nuevo / Limpiar");
        //btnLimpiar.addActionListener(e -> limpiarCampos());
        
        pnlExtra.add(btnUpdate); pnlExtra.add(btnDelete); //pnlExtra.add(btnLimpiar);

        JPanel pnlNorte = new JPanel(new BorderLayout());
        pnlNorte.add(pnlDatos, BorderLayout.CENTER);
        pnlNorte.add(pnlExtra, BorderLayout.SOUTH);
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

        add(new JScrollPane(tablaEstudiantes), BorderLayout.CENTER);
    }

    public void clickAgregar() {
        try {
            String res = controlador.agregarEstudiante(txtId.getText(), txtNombre.getText(), Integer.parseInt(txtEdad.getText()));
            mostrarMensaje(res);
            limpiarCampos();
        } catch (Exception e) { mostrarMensaje("Error: Edad debe ser numérica"); }
    }


public void clickEliminar1() {
    // 1. Verificar que haya un ID seleccionado o escrito
    String id = txtId.getText();
    if (id.isEmpty()) {
        mostrarMensaje("Por favor, seleccione un estudiante para eliminar.");
        return;
    }

    // 2. Mostrar el Pop-up de confirmación
    int respuesta = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro de que desea eliminar al estudiante con ID: " + id + "?", 
            "Confirmar Eliminación", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE
    );

    // 3. Evaluar la respuesta (JOptionPane.YES_OPTION es 0)
    if (respuesta == JOptionPane.YES_OPTION) {
        String res = controlador.eliminarEstudiante(id);
        mostrarMensaje(res);
        limpiarCampos();
        clickMostrarTodo(); // Opcional: refresca la tabla automáticamente
    }
}

    public void clickActualizar() {
        try {
            String res = controlador.actualizarEstudiante(txtId.getText(), txtNombre.getText(), Integer.parseInt(txtEdad.getText()));
            mostrarMensaje(res);
            limpiarCampos();
        } catch (Exception e) { mostrarMensaje("Error en actualización"); }
    }

public void clickEliminar() {
    // 1. Verificar que haya un ID seleccionado o escrito
    String id = txtId.getText();
    if (id.isEmpty()) {
        mostrarMensaje("Por favor, seleccione un estudiante para eliminar.");
        return;
    }

    // 2. Mostrar el Pop-up de confirmación
    int respuesta = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro de que desea eliminar al estudiante con ID: " + id + "?", 
            "Confirmar Eliminación", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE
    );

    // 3. Evaluar la respuesta (JOptionPane.YES_OPTION es 0)
    if (respuesta == JOptionPane.YES_OPTION) {
        String res = controlador.eliminarEstudiante(id);
        mostrarMensaje(res);
        limpiarCampos();
        clickMostrarTodo(); // Opcional: refresca la tabla automáticamente
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
}