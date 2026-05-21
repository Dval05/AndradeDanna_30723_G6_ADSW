package ec.edu.espe.estudiantecrud.view;

import ec.edu.espe.estudiantecrud.controller.ControlEstudiante;
import ec.edu.espe.estudiantecrud.model.Estudiante;
import ec.edu.espe.estudiantecrud.observer.ViewStudentObserver;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Formulario CRUD refactorizado con patrones:
 * - Observer
 * - Strategy
 * - Command
 */
public class FormularioCrudEstudiante extends JFrame implements ViewStudentObserver {

    private final ControlEstudiante controlador;

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtEdad;
    private JTextField txtCarrera;
    private JTextField txtBusqueda;

    private JComboBox<String> comboBusqueda;

    private DefaultTableModel modeloTabla;
    private JTable tablaEstudiantes;

    private JLabel lblHistorial;

    public FormularioCrudEstudiante() {

        controlador = new ControlEstudiante();

        // Registrar observer
        controlador.getService().registerObserver(this);

        configurarVentana();
        inicializarComponentes();

        clickMostrarTodo();
    }

    private void configurarVentana() {

        setTitle("CRUD Estudiantes - Patrones de Diseño");

        setSize(850, 650);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes() {

        // =========================
        // PANEL DATOS
        // =========================

        JPanel pnlDatos = new JPanel(new GridLayout(5, 2, 5, 5));

        pnlDatos.setBorder(
                BorderFactory.createTitledBorder("Datos del Estudiante")
        );

        pnlDatos.add(new JLabel("ID:"));

        txtId = new JTextField();

        pnlDatos.add(txtId);

        pnlDatos.add(new JLabel("Nombre:"));

        txtNombre = new JTextField();

        pnlDatos.add(txtNombre);

        pnlDatos.add(new JLabel("Edad:"));

        txtEdad = new JTextField();

        pnlDatos.add(txtEdad);

        pnlDatos.add(new JLabel("Carrera:"));

        txtCarrera = new JTextField();

        pnlDatos.add(txtCarrera);

        JButton btnAgregar = new JButton("Agregar");

        btnAgregar.addActionListener(e -> clickAgregar());

        pnlDatos.add(btnAgregar);

        JButton btnMostrar = new JButton("Mostrar");

        btnMostrar.addActionListener(e -> clickMostrarTodo());

        pnlDatos.add(btnMostrar);

        // =========================
        // PANEL CRUD
        // =========================

        JPanel pnlCrud = new JPanel(new FlowLayout());

        pnlCrud.setBorder(
                BorderFactory.createTitledBorder("Operaciones CRUD")
        );

        JButton btnActualizar = new JButton("Actualizar");

        btnActualizar.addActionListener(e -> clickActualizar());

        pnlCrud.add(btnActualizar);

        JButton btnEliminar = new JButton("Eliminar");

        btnEliminar.addActionListener(e -> clickEliminar());

        pnlCrud.add(btnEliminar);

        JButton btnLimpiar = new JButton("Limpiar");

        btnLimpiar.addActionListener(e -> limpiarCampos());

        pnlCrud.add(btnLimpiar);

        pnlCrud.add(Box.createHorizontalStrut(20));

        JButton btnUndo = new JButton("Deshacer");

        btnUndo.addActionListener(e -> clickDeshacer());

        pnlCrud.add(btnUndo);

        JButton btnRedo = new JButton("Rehacer");

        btnRedo.addActionListener(e -> clickRehacer());

        pnlCrud.add(btnRedo);

        // =========================
        // PANEL BUSQUEDA
        // =========================

        JPanel pnlBusqueda = new JPanel(new FlowLayout());

        pnlBusqueda.setBorder(
                BorderFactory.createTitledBorder("Busqueda - Strategy")
        );

        pnlBusqueda.add(new JLabel("Buscar por:"));

        comboBusqueda = new JComboBox<>(
                new String[]{
                        "Por ID",
                        "Por Nombre",
                        "Por Carrera"
                }
        );

        pnlBusqueda.add(comboBusqueda);

        txtBusqueda = new JTextField(15);

        pnlBusqueda.add(txtBusqueda);

        JButton btnBuscar = new JButton("Buscar");

        btnBuscar.addActionListener(e -> clickBuscar());

        pnlBusqueda.add(btnBuscar);

        // =========================
        // PANEL SUPERIOR
        // =========================

        JPanel pnlSuperior = new JPanel(new BorderLayout());

        pnlSuperior.add(pnlBusqueda, BorderLayout.NORTH);

        pnlSuperior.add(pnlDatos, BorderLayout.CENTER);

        pnlSuperior.add(pnlCrud, BorderLayout.SOUTH);

        add(pnlSuperior, BorderLayout.NORTH);

        // =========================
        // TABLA
        // =========================

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Nombre",
                        "Edad",
                        "Carrera"
                },
                0
        ) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaEstudiantes = new JTable(modeloTabla);

        tablaEstudiantes.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaEstudiantes.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int fila = tablaEstudiantes.getSelectedRow();

                if (fila != -1) {

                    txtId.setText(
                            modeloTabla.getValueAt(fila, 0).toString()
                    );

                    txtNombre.setText(
                            modeloTabla.getValueAt(fila, 1).toString()
                    );

                    txtEdad.setText(
                            modeloTabla.getValueAt(fila, 2).toString()
                    );

                    txtCarrera.setText(
                            modeloTabla.getValueAt(fila, 3).toString()
                    );

                    txtId.setEditable(false);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tablaEstudiantes);

        add(scroll, BorderLayout.CENTER);

        // =========================
        // PANEL INFERIOR
        // =========================

        JPanel pnlInferior = new JPanel(new BorderLayout());

        lblHistorial = new JLabel("Historial: 0 operaciones");

        pnlInferior.add(lblHistorial, BorderLayout.WEST);

        add(pnlInferior, BorderLayout.SOUTH);
    }

    // =========================================
    // ACCIONES
    // =========================================

    private void clickAgregar() {

        try {

            String id = txtId.getText();

            String nombre = txtNombre.getText();

            int edad = Integer.parseInt(txtEdad.getText());

            String carrera = txtCarrera.getText();

            String resultado =
                    controlador.agregarEstudiante(
                            id,
                            nombre,
                            edad,
                            carrera
                    );

            mostrarMensaje(resultado);

            limpiarCampos();

            actualizarHistorial();

        } catch (NumberFormatException e) {

            mostrarMensaje("Edad invalida");

        } catch (Exception e) {

            mostrarMensaje(e.getMessage());
        }
    }

    private void clickActualizar() {

        try {

            String id = txtId.getText();

            if (id.isEmpty()) {

                mostrarMensaje("Seleccione un estudiante");

                return;
            }

            String nombre = txtNombre.getText();

            int edad = Integer.parseInt(txtEdad.getText());

            String carrera = txtCarrera.getText();

            String resultado =
                    controlador.actualizarEstudiante(
                            id,
                            nombre,
                            edad,
                            carrera
                    );

            mostrarMensaje(resultado);

            limpiarCampos();

            actualizarHistorial();

        } catch (NumberFormatException e) {

            mostrarMensaje("Edad invalida");

        } catch (Exception e) {

            mostrarMensaje(e.getMessage());
        }
    }

    private void clickEliminar() {

        String id = txtId.getText();

        if (id.isEmpty()) {

            mostrarMensaje("Seleccione un estudiante");

            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "Eliminar estudiante?",
                "Confirmacion",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta == JOptionPane.YES_OPTION) {

            String resultado =
                    controlador.eliminarEstudiante(id);

            mostrarMensaje(resultado);

            limpiarCampos();

            actualizarHistorial();
        }
    }

    private void clickBuscar() {

        String criterio = txtBusqueda.getText();

        if (criterio.isEmpty()) {

            mostrarMensaje("Ingrese criterio");

            return;
        }

        List<Estudiante> resultados;

        switch (comboBusqueda.getSelectedIndex()) {

            case 0:

                resultados =
                        controlador.buscarPorId(criterio);

                break;

            case 1:

                resultados =
                        controlador.buscarPorNombre(criterio);

                break;

            case 2:

                resultados =
                        controlador.buscarPorCarrera(criterio);

                break;

            default:

                resultados = List.of();
        }

        mostrarTabla(resultados);
    }

    private void clickMostrarTodo() {

        mostrarTabla(controlador.mostrarTodos());
    }

    private void clickDeshacer() {

        String resultado = controlador.deshacer();

        mostrarMensaje(resultado);

        clickMostrarTodo();

        actualizarHistorial();
    }

    private void clickRehacer() {

        String resultado = controlador.rehacer();

        mostrarMensaje(resultado);

        clickMostrarTodo();

        actualizarHistorial();
    }

    // =========================================
    // UTILIDADES
    // =========================================

    private void mostrarMensaje(String mensaje) {

        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void mostrarTabla(List<Estudiante> estudiantes) {

        modeloTabla.setRowCount(0);

        for (Estudiante e : estudiantes) {

            modeloTabla.addRow(
                    new Object[]{
                            e.getId(),
                            e.getNombre(),
                            e.getEdad(),
                            e.getCarrera()
                    }
            );
        }
    }

    private void limpiarCampos() {

        txtId.setText("");

        txtNombre.setText("");

        txtEdad.setText("");

        txtCarrera.setText("");

        txtBusqueda.setText("");

        txtId.setEditable(true);

        tablaEstudiantes.clearSelection();
    }

    private void actualizarHistorial() {

        int tamaño =
                controlador.getService().getHistorySize();

        lblHistorial.setText(
                "Historial: " + tamaño + " operaciones"
        );
    }

    // =========================================
    // OBSERVER
    // =========================================

    @Override
    public void onStudentAdded(Estudiante estudiante) {

        System.out.println(
                "[Observer] Agregado: "
                        + estudiante.getNombre()
        );

        clickMostrarTodo();
    }

    @Override
    public void onStudentUpdated(Estudiante estudiante) {

        System.out.println(
                "[Observer] Actualizado: "
                        + estudiante.getNombre()
        );

        clickMostrarTodo();
    }

    @Override
    public void onStudentDeleted(String id) {

        System.out.println(
                "[Observer] Eliminado: "
                        + id
        );

        clickMostrarTodo();
    }

    @Override
    public void refreshTable() {

        clickMostrarTodo();
    }
}