package ec.edu.espe.estudiantecrud.view;

import ec.edu.espe.estudiantecrud.command.ComandoActualizar;
import ec.edu.espe.estudiantecrud.command.ComandoAgregar;
import ec.edu.espe.estudiantecrud.command.ComandoEliminar;
import ec.edu.espe.estudiantecrud.command.InvocadorComando;
import ec.edu.espe.estudiantecrud.controller.ControlEstudiante;
import ec.edu.espe.estudiantecrud.model.Estudiante;
import ec.edu.espe.estudiantecrud.observer.ObservadorEstudiante;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FormularioCrudEstudiante extends JFrame implements ObservadorEstudiante {

    private final ControlEstudiante controlador;
    private final InvocadorComando invocador = new InvocadorComando();
    private JTextField txtId, txtNombre, txtEdad;
    private DefaultTableModel modeloTabla;
    private JTable tablaEstudiantes;

    public FormularioCrudEstudiante() {
        this.controlador = new ControlEstudiante();
        controlador.agregarObservador(this);
        configurarVentana();
        inicializarComponentes();
    }

    // ── Observer ──────────────────────────────────────────────────────────────
    @Override
    public void actualizar() {
        mostrarTabla(controlador.mostrarTodos());
    }

    private void configurarVentana() {
        setTitle("EstudianteCRUD - ESPE");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));
        setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
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

        JPanel pnlExtra = new JPanel(new FlowLayout());
        JButton btnUpdate = new JButton("Actualizar");
        btnUpdate.addActionListener(e -> clickActualizar());
        JButton btnDelete = new JButton("Eliminar");
        btnDelete.addActionListener(e -> clickEliminar1());
        JButton btnDeshacer = new JButton("Deshacer");
        btnDeshacer.addActionListener(e -> clickDeshacer());

        pnlExtra.add(btnUpdate);
        pnlExtra.add(btnDelete);
        pnlExtra.add(btnDeshacer);

        JPanel pnlNorte = new JPanel(new BorderLayout());
        pnlNorte.add(pnlDatos, BorderLayout.CENTER);
        pnlNorte.add(pnlExtra, BorderLayout.SOUTH);
        add(pnlNorte, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Edad"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaEstudiantes = new JTable(modeloTabla);
        tablaEstudiantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tablaEstudiantes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaEstudiantes.getSelectedRow();
                if (fila != -1) {
                    txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtEdad.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtId.setEditable(false);
                    txtNombre.requestFocus();
                }
            }
        });

        add(new JScrollPane(tablaEstudiantes), BorderLayout.CENTER);
    }

    // ── Command ───────────────────────────────────────────────────────────────
    public void clickAgregar() {
        try {
            ComandoAgregar cmd = new ComandoAgregar(
                    controlador, txtId.getText(), txtNombre.getText(),
                    Integer.parseInt(txtEdad.getText()));
            invocador.ejecutar(cmd);
            mostrarMensaje(cmd.getResultado());
            limpiarCampos();
        } catch (Exception e) {
            mostrarMensaje("Error: Edad debe ser numérica");
        }
    }

    public void clickActualizar() {
        try {
            ComandoActualizar cmd = new ComandoActualizar(
                    controlador, txtId.getText(), txtNombre.getText(),
                    Integer.parseInt(txtEdad.getText()));
            invocador.ejecutar(cmd);
            mostrarMensaje(cmd.getResultado());
            limpiarCampos();
        } catch (Exception e) {
            mostrarMensaje("Error en actualización");
        }
    }

    public void clickEliminar1() {
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
                JOptionPane.WARNING_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) {
            ComandoEliminar cmd = new ComandoEliminar(controlador, id);
            invocador.ejecutar(cmd);
            mostrarMensaje(cmd.getResultado());
            limpiarCampos();
        }
    }

    public void clickDeshacer() {
        if (!invocador.tieneHistorial()) {
            mostrarMensaje("No hay operaciones para deshacer.");
            return;
        }
        invocador.deshacer();
        mostrarMensaje("Operación deshecha.");
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
        txtId.setEditable(true);
        tablaEstudiantes.clearSelection();
    }
}
