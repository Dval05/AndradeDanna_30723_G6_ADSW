package ec.edu.espe.estudiantecrud.logica.adapter;

import ec.edu.espe.estudiantecrud.logica.IControlEstudiante;

public class AdaptadorEntradaEstudiante {
    private final IControlEstudiante servicioCRUD;

    public AdaptadorEntradaEstudiante(IControlEstudiante servicioCRUD) {
        this.servicioCRUD = servicioCRUD;
    }

    public String registrarDesdeExterno(EstudianteExterno externo) {
        // Adaptar los datos del formato externo (codigo, nombreCompleto, anios)
        // al formato interno (id, nombre, edad) esperado por el servicio
        String idAdaptado = externo.getCodigo();
        String nombreAdaptado = externo.getNombreCompleto();
        int edadAdaptada = externo.getAnios();

        // Enviar al servicio
        return servicioCRUD.agregarEstudiante(idAdaptado, nombreAdaptado, edadAdaptada);
    }
}
