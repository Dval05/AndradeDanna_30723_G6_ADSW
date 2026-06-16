package ec.edu.espe.estudiantecrud.strategy;

public class ValidacionBasica implements EstrategiaValidacion {

    @Override
    public boolean validar(String id, String nombre, int edad) {
        return !id.trim().isEmpty() && !nombre.trim().isEmpty() && edad > 0;
    }
}
