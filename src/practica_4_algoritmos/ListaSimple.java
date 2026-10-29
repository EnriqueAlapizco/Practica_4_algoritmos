package practica_4_algoritmos;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** Lista para las columnas, bases e historial del juego. */
public class ListaSimple<T> implements Iterable<T> {
    private NodoSimple<T> inicio;
    private NodoSimple<T> fin;
    private int tamanio;

    public boolean estaVacia() { return tamanio == 0; }
    public int getTamanio() { return tamanio; }

    public void insertarInicio(T dato) {
        NodoSimple<T> nuevo = new NodoSimple<>(dato);
        nuevo.setSiguiente(inicio);
        inicio = nuevo;
        if (fin == null) { fin = nuevo; }
        tamanio++;
    }

    public void insertarFinal(T dato) {
        NodoSimple<T> nuevo = new NodoSimple<>(dato);
        if (estaVacia()) { inicio = nuevo; }
        else { fin.setSiguiente(nuevo); }
        fin = nuevo;
        tamanio++;
    }

    public T eliminarInicio() {
        comprobarNoVacia();
        T dato = inicio.getDato();
        inicio = inicio.getSiguiente();
        tamanio--;
        if (estaVacia()) { fin = null; }
        return dato;
    }

    public T eliminarFinal() {
        comprobarNoVacia();
        if (tamanio == 1) { return eliminarInicio(); }
        NodoSimple<T> anterior = nodoEn(tamanio - 2);
        T dato = fin.getDato();
        anterior.setSiguiente(null);
        fin = anterior;
        tamanio--;
        return dato;
    }

    public T obtener(int indice) { return nodoEn(indice).getDato(); }
    public T obtenerFinal() {
        comprobarNoVacia();
        return fin.getDato();
    }

    private NodoSimple<T> nodoEn(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Indice fuera de la lista: " + indice);
        }
        NodoSimple<T> actual = inicio;
        for (int i = 0; i < indice; i++) { actual = actual.getSiguiente(); }
        return actual;
    }

    private void comprobarNoVacia() {
        if (estaVacia()) { throw new NoSuchElementException("La lista esta vacia"); }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private NodoSimple<T> actual = inicio;
            public boolean hasNext() { return actual != null; }
            public T next() {
                if (!hasNext()) { throw new NoSuchElementException(); }
                T dato = actual.getDato();
                actual = actual.getSiguiente();
                return dato;
            }
        };
    }
}
