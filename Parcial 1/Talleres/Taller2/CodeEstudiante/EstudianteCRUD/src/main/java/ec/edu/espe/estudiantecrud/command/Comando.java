package ec.edu.espe.estudiantecrud.command;

public interface Comando {
    void ejecutar();
    void deshacer();
    String getResultado();
}
