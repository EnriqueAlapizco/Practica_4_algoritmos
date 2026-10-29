package practica_4_algoritmos;

/** Datos inmutables de un movimiento individual, con indices desde cero. */
public final class Movimiento {
    private final ZonaJuego zonaOrigen;
    private final int indiceOrigen;
    private final ZonaJuego zonaDestino;
    private final int indiceDestino;

    Movimiento(ZonaJuego zonaOrigen, int indiceOrigen,
            ZonaJuego zonaDestino, int indiceDestino) {
        this.zonaOrigen = zonaOrigen;
        this.indiceOrigen = indiceOrigen;
        this.zonaDestino = zonaDestino;
        this.indiceDestino = indiceDestino;
    }

    public ZonaJuego getZonaOrigen() { return zonaOrigen; }
    public int getIndiceOrigen() { return indiceOrigen; }
    public ZonaJuego getZonaDestino() { return zonaDestino; }
    public int getIndiceDestino() { return indiceDestino; }
}
