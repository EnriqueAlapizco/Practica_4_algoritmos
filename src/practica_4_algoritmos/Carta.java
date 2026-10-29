package practica_4_algoritmos;

import java.util.Objects;

/** Carta inmutable; el as vale 1 y el rey 13. */
public class Carta implements Comparable<Carta> {
    private final int rango;
    private final Palo palo;

    public Carta(int rango, Palo palo) {
        if (rango < 1 || rango > 13) {
            throw new IllegalArgumentException("El rango debe estar entre 1 y 13");
        }
        this.rango = rango;
        this.palo = Objects.requireNonNull(palo, "La carta debe tener un palo");
    }

    public int getRango() { return rango; }
    public Palo getPalo() { return palo; }

    /** Ordena por palo y, dentro del mismo palo, por rango. */
    @Override
    public int compareTo(Carta otra) {
        int comparacion = palo.compareTo(otra.palo);
        return comparacion != 0 ? comparacion : Integer.compare(rango, otra.rango);
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) { return true; }
        if (!(objeto instanceof Carta)) { return false; }
        Carta otra = (Carta) objeto;
        return rango == otra.rango && palo == otra.palo;
    }

    @Override
    public int hashCode() { return Objects.hash(rango, palo); }

    @Override
    public String toString() {
        String valor;
        switch (rango) {
            case 1: valor = "A"; break;
            case 11: valor = "J"; break;
            case 12: valor = "Q"; break;
            case 13: valor = "K"; break;
            default: valor = Integer.toString(rango);
        }
        return valor + " de " + palo.getNombre();
    }
}
