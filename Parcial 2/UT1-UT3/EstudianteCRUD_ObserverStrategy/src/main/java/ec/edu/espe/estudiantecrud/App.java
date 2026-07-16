package ec.edu.espe.estudiantecrud;

import javax.swing.SwingUtilities;

import ec.edu.espe.estudiantecrud.presentacion.FormularioCrudEstudiante;

/**
 * Punto de entrada del sistema.
 */
public final class App {

    private App() {
        // Evita instanciación
    }

    public static void main(String[] args) {
        // Iniciar la interfaz gráfica de forma segura
        SwingUtilities.invokeLater(() -> {
            FormularioCrudEstudiante ventana = new FormularioCrudEstudiante();
            ventana.setVisible(true);
        });
    }
}