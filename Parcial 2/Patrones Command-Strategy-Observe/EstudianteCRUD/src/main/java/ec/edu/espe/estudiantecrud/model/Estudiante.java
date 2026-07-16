package ec.edu.espe.estudiantecrud.model;

/**
 * Modelo de dominio que representa un estudiante.
 * Encapsula los datos básicos de un estudiante de la ESPE.
 */
public class Estudiante {
    private String id;
    private String nombre;
    private int edad;
    private String carrera;

    public Estudiante(String id, String nombre, int edad, String carrera) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.carrera = carrera;
    }

    // Constructor para compatibilidad con código anterior
    public Estudiante(String id, String nombre, int edad) {
        this(id, nombre, edad, "Sin carrera");
    }

    // Getters
    public String getId() { 
        return id; 
    }

    public String getNombre() { 
        return nombre; 
    }

    public int getEdad() { 
        return edad; 
    }

    public String getCarrera() { 
        return carrera; 
    }

    // Setters para permitir actualización
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", edad=" + edad +
                ", carrera='" + carrera + '\'' +
                '}';
    }
}