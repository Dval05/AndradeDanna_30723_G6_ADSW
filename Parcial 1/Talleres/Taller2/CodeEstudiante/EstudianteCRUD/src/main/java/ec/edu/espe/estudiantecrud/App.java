package ec.edu.espe.estudiantecrud;

import ec.edu.espe.estudiantecrud.view.FormularioCrudEstudiante;
import javax.swing.SwingUtilities;

/**
 * Punto de entrada del sistema.
 */
public class App {
    public static void main(String[] args) {
        // Iniciar la interfaz gráfica de forma segura
        SwingUtilities.invokeLater(() -> {
            FormularioCrudEstudiante ventana = new FormularioCrudEstudiante();
            ventana.setVisible(true);
        });
    }
}