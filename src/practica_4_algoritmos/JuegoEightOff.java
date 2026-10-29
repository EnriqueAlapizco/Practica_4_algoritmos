package practica_4_algoritmos;

/** Estado y logica del juego, independientes de la interfaz grafica. */
public class JuegoEightOff {
    public static final int CANTIDAD_COLUMNAS = 8;
    public static final int CANTIDAD_RESERVAS = 8;
    public static final int CANTIDAD_BASES = 4;
    public static final int CARTAS_POR_COLUMNA = 6;
    public static final int RESERVAS_INICIALES_OCUPADAS = 4;

    private ListaSimple<ListaSimple<Carta>> columnas;
    private ListaSimple<ListaSimple<Carta>> reservas;
    private ListaSimple<ListaSimple<Carta>> bases;

    public JuegoEightOff() {
        nuevaPartida();
    }

    /** Baraja y reparte las 52 cartas: 48 en columnas y cuatro en reservas. */
    public void nuevaPartida() {
        Baraja baraja = new Baraja();
        baraja.barajar();
        ListaSimple<ListaSimple<Carta>> nuevasColumnas = crearEspacios(CANTIDAD_COLUMNAS);
        ListaSimple<ListaSimple<Carta>> nuevasReservas = crearEspacios(CANTIDAD_RESERVAS);
        ListaSimple<ListaSimple<Carta>> nuevasBases = crearEspacios(CANTIDAD_BASES);

        // Reparto por filas, de izquierda a derecha; el final es la carta libre.
        for (int fila = 0; fila < CARTAS_POR_COLUMNA; fila++) {
            for (ListaSimple<Carta> columna : nuevasColumnas) {
                columna.insertarFinal(baraja.sacarCarta());
            }
        }
        for (int i = 0; i < RESERVAS_INICIALES_OCUPADAS; i++) {
            nuevasReservas.obtener(i).insertarFinal(baraja.sacarCarta());
        }

        columnas = nuevasColumnas;
        reservas = nuevasReservas;
        bases = nuevasBases;
    }

    /** Devuelve las cartas en orden; la ultima es la disponible para mover. */
    public ListaSimple<Carta> getColumna(int indice) {
        return copiar(columnas.obtener(indice));
    }

    /** Cada reserva contiene cero o una carta. */
    public ListaSimple<Carta> getReserva(int indice) {
        return copiar(reservas.obtener(indice));
    }

    /** Las bases se identifican en el mismo orden que Palo.values(). */
    public ListaSimple<Carta> getBase(int indice) {
        return copiar(bases.obtener(indice));
    }

    private ListaSimple<ListaSimple<Carta>> crearEspacios(int cantidad) {
        ListaSimple<ListaSimple<Carta>> espacios = new ListaSimple<>();
        for (int i = 0; i < cantidad; i++) {
            espacios.insertarFinal(new ListaSimple<>());
        }
        return espacios;
    }

    // Se copian los nodos para que la GUI no pueda modificar el estado del juego.
    // Carta es inmutable, por lo que sus instancias se pueden compartir.
    private ListaSimple<Carta> copiar(ListaSimple<Carta> origen) {
        ListaSimple<Carta> copia = new ListaSimple<>();
        for (Carta carta : origen) {
            copia.insertarFinal(carta);
        }
        return copia;
    }
}
