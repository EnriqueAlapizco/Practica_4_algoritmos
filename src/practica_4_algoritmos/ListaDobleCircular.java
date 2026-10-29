package practica_4_algoritmos;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** El anterior del inicio es el fin y el siguiente del fin es el inicio. */
public class ListaDobleCircular<T> implements Iterable<T> {
    private NodoDoble<T> inicio;
    private int tamanio;

    public boolean estaVacia() { return tamanio == 0; }
    public int getTamanio() { return tamanio; }

    public void insertarFinal(T dato) {
        NodoDoble<T> nuevo = new NodoDoble<>(dato);
        if (estaVacia()) {
            nuevo.setAnterior(nuevo);
            nuevo.setSiguiente(nuevo);
            inicio = nuevo;
        } else {
            NodoDoble<T> fin = inicio.getAnterior();
            nuevo.setAnterior(fin);
            nuevo.setSiguiente(inicio);
            fin.setSiguiente(nuevo);
            inicio.setAnterior(nuevo);
        }
        tamanio++;
    }

    public void insertarInicio(T dato) {
        insertarFinal(dato);
        inicio = inicio.getAnterior();
    }

    public T eliminarInicio() { return eliminar(0); }
    public T eliminarFinal() { return eliminar(tamanio - 1); }

    public T eliminar(int indice) {
        NodoDoble<T> actual = nodoEn(indice);
        if (tamanio == 1) { inicio = null; }
        else {
            actual.getAnterior().setSiguiente(actual.getSiguiente());
            actual.getSiguiente().setAnterior(actual.getAnterior());
            if (actual == inicio) { inicio = actual.getSiguiente(); }
        }
        tamanio--;
        return actual.getDato();
    }

    public T obtener(int indice) { return nodoEn(indice).getDato(); }
    public void reemplazar(int indice, T dato) { nodoEn(indice).setDato(dato); }

    private NodoDoble<T> nodoEn(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Indice fuera de la lista: " + indice);
        }
        NodoDoble<T> actual;
        if (indice <= tamanio / 2) {
            actual = inicio;
            for (int i = 0; i < indice; i++) { actual = actual.getSiguiente(); }
        } else {
            actual = inicio.getAnterior();
            for (int i = tamanio - 1; i > indice; i--) { actual = actual.getAnterior(); }
        }
        return actual;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private NodoDoble<T> actual = inicio;
            private int restantes = tamanio;
            public boolean hasNext() { return restantes > 0; }
            public T next() {
                if (!hasNext()) { throw new NoSuchElementException(); }
                T dato = actual.getDato();
                actual = actual.getSiguiente();
                restantes--;
                return dato;
            }
        };
    }
}
