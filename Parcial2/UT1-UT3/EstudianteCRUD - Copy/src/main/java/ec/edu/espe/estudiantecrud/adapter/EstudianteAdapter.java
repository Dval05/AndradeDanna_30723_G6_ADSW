package ec.edu.espe.estudiantecrud.adapter;

import ec.edu.espe.estudiantecrud.model.Estudiante;

public class EstudianteAdapter {
    public Estudiante adaptar(EstudianteExterno externo) {
        return new Estudiante(externo.getCodigo(), externo.getNombreCompleto(), externo.getAnios());
    }
}
