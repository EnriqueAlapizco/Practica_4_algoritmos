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
    private ListaSimple<Movimiento> historial;

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
        historial = new ListaSimple<>();
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

    /**
     * Consulta un movimiento de una sola carta sin modificar la partida.
     * Los indices empiezan en cero. Las bases solo reciben cartas;
     * una carta cubierta no se puede seleccionar como origen.
     * Devuelve false tambien para zonas nulas o indices invalidos.
     */
    public boolean puedeMover(ZonaJuego zonaOrigen, int indiceOrigen,
            ZonaJuego zonaDestino, int indiceDestino) {
        ListaSimple<Carta> origen = obtenerEspacio(zonaOrigen, indiceOrigen);
        ListaSimple<Carta> destino = obtenerEspacio(zonaDestino, indiceDestino);
        if (origen == null || destino == null || origen == destino
                || origen.estaVacia() || zonaOrigen == ZonaJuego.BASE) {
            return false;
        }

        Carta carta = origen.obtenerFinal();
        switch (zonaDestino) {
            case RESERVA:
                return destino.estaVacia();
            case COLUMNA:
                if (destino.estaVacia()) {
                    return carta.getRango() == 13;
                }
                Carta superior = destino.obtenerFinal();
                return carta.getPalo() == superior.getPalo()
                        && carta.getRango() + 1 == superior.getRango();
            case BASE:
                return carta.getPalo().ordinal() == indiceDestino
                        && carta.getRango() == destino.getTamanio() + 1;
            default:
                return false;
        }
    }

    /** Mueve la carta libre del origen; si no es legal, conserva el estado. */
    public boolean mover(ZonaJuego zonaOrigen, int indiceOrigen,
            ZonaJuego zonaDestino, int indiceDestino) {
        if (!puedeMover(zonaOrigen, indiceOrigen, zonaDestino, indiceDestino)) {
            return false;
        }
        ListaSimple<Carta> origen = obtenerEspacio(zonaOrigen, indiceOrigen);
        ListaSimple<Carta> destino = obtenerEspacio(zonaDestino, indiceDestino);
        destino.insertarFinal(origen.eliminarFinal());
        // El inicio funciona como la cima de una pila: ultimo en entrar, primero en salir.
        historial.insertarInicio(new Movimiento(zonaOrigen, indiceOrigen,
                zonaDestino, indiceDestino));
        return true;
    }

    public boolean puedeDeshacer() {
        return !historial.estaVacia();
    }

    /** Cantidad de movimientos actuales que aun se pueden deshacer. */
    public int getCantidadMovimientos() {
        return historial.getTamanio();
    }

    /** Restaura el ultimo movimiento sin registrar otro en el historial. */
    public boolean deshacer() {
        if (!puedeDeshacer()) { return false; }
        Movimiento ultimo = historial.eliminarInicio();
        ListaSimple<Carta> origen = obtenerEspacio(ultimo.getZonaOrigen(), ultimo.getIndiceOrigen());
        ListaSimple<Carta> destino = obtenerEspacio(ultimo.getZonaDestino(), ultimo.getIndiceDestino());
        // Restaurar el estado previo no requiere validar las reglas de un movimiento nuevo.
        origen.insertarFinal(destino.eliminarFinal());
        return true;
    }

    private ListaSimple<Carta> obtenerEspacio(ZonaJuego zona, int indice) {
        if (zona == null || indice < 0) { return null; }
        ListaSimple<ListaSimple<Carta>> espacios;
        switch (zona) {
            case COLUMNA: espacios = columnas; break;
            case RESERVA: espacios = reservas; break;
            case BASE: espacios = bases; break;
            default: return null;
        }
        return indice < espacios.getTamanio() ? espacios.obtener(indice) : null;
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
