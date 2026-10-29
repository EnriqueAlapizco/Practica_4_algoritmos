package practica_4_algoritmos;

import java.util.NoSuchElementException;
import java.util.Random;

/** Baraja francesa de 52 cartas almacenada en una lista doble circular. */
public class Baraja {
    private final ListaDobleCircular<Carta> cartas = new ListaDobleCircular<>();

    public Baraja() {
        for (Palo palo : Palo.values()) {
            for (int rango = 1; rango <= 13; rango++) {
                cartas.insertarFinal(new Carta(rango, palo));
            }
        }
    }

    public int getCantidad() { return cartas.getTamanio(); }
    public boolean estaVacia() { return cartas.estaVacia(); }

    /** Fisher-Yates sobre la lista, sin convertirla en un arreglo. */
    public void barajar() {
        Random aleatorio = new Random();
        for (int i = getCantidad() - 1; i > 0; i--) {
            int j = aleatorio.nextInt(i + 1);
            Carta temporal = cartas.obtener(i);
            cartas.reemplazar(i, cartas.obtener(j));
            cartas.reemplazar(j, temporal);
        }
    }

    public Carta sacarCarta() {
        if (estaVacia()) { throw new NoSuchElementException("La baraja esta vacia"); }
        return cartas.eliminarInicio();
    }
}
