package practica_4_algoritmos;

public enum Palo {
    TREBOLES("Treboles"), DIAMANTES("Diamantes"),
    CORAZONES("Corazones"), PICAS("Picas");

    private final String nombre;
    Palo(String nombre) { this.nombre = nombre; }
    public String getNombre() { return nombre; }
}
