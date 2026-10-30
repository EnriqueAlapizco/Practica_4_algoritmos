package practica_4_algoritmos;

import java.util.Objects;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;

/** Consulta el modelo y dibuja el tablero, sin implementar reglas de juego. */
public class VistaTablero extends BorderPane {
    private final JuegoEightOff juego;
    private static final double SEPARACION_CARTAS = 30;

    public VistaTablero(JuegoEightOff juego) {
        this.juego = Objects.requireNonNull(juego);
        getStyleClass().add("tablero");
        actualizar();
    }

    /** Reconstruye la vista a partir de las listas del modelo. */
    public void actualizar() {
        Label titulo = etiqueta("Eight Off", "titulo");
        Label movimientos = etiqueta("Movimientos: " + juego.getCantidadMovimientos(), "indicador");
        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);
        HBox cabecera = new HBox(12, titulo, espacio, movimientos);
        cabecera.setAlignment(Pos.CENTER_LEFT);
        cabecera.getStyleClass().add("cabecera");
        setTop(cabecera);

        HBox reservas = new HBox(14);
        HBox columnas = new HBox(14);
        VBox bases = new VBox(12, etiqueta("BASES", "seccion"));
        for (int i = 0; i < JuegoEightOff.CANTIDAD_RESERVAS; i++) {
            reservas.getChildren().add(crearMonton("R" + (i + 1), juego.getReserva(i), false, "Libre", 1));
        }
        for (int i = 0; i < JuegoEightOff.CANTIDAD_COLUMNAS; i++) {
            columnas.getChildren().add(crearMonton("C" + (i + 1), juego.getColumna(i), true, "Rey", 1));
        }
        for (Palo palo : Palo.values()) {
            ListaSimple<Carta> cartas = juego.getBase(palo.ordinal());
            bases.getChildren().add(crearMonton(palo.getNombre() + " · " + cartas.getTamanio() + "/13",
                    cartas, false, "A " + VistaCarta.simbolo(palo), 0.8));
        }

        VBox mesa = new VBox(14, etiqueta("RESERVAS", "seccion"), reservas,
                etiqueta("COLUMNAS", "seccion"), columnas);
        HBox contenido = new HBox(30, mesa, bases);
        contenido.getStyleClass().add("contenido");
        ScrollPane desplazamiento = new ScrollPane(contenido);
        desplazamiento.setFitToWidth(true);
        desplazamiento.setPannable(true);
        desplazamiento.getStyleClass().add("desplazamiento");
        setCenter(desplazamiento);

    }

    private VBox crearMonton(String nombre, ListaSimple<Carta> cartas,
            boolean desplegar, String textoVacio, double escala) {
        Pane pila = new Pane();
        double alto = VistaCarta.ALTO + (desplegar ? Math.max(0, cartas.getTamanio() - 1) * SEPARACION_CARTAS : 0);
        pila.setMinSize(VistaCarta.ANCHO, alto);
        pila.setPrefSize(VistaCarta.ANCHO, alto);
        pila.setMaxSize(VistaCarta.ANCHO, alto);
        if (cartas.estaVacia()) {
            StackPane hueco = new StackPane(new Label(textoVacio));
            hueco.getStyleClass().add("hueco");
            hueco.setPrefSize(VistaCarta.ANCHO, VistaCarta.ALTO);
            pila.getChildren().add(hueco);
        } else if (desplegar) {
            int posicion = 0;
            for (Carta carta : cartas) {
                VistaCarta vista = new VistaCarta(carta);
                vista.setLayoutY(posicion++ * SEPARACION_CARTAS);
                pila.getChildren().add(vista);
            }
        } else {
            pila.getChildren().add(new VistaCarta(cartas.obtenerFinal()));
        }
        // Group incorpora el tamano escalado al calculo del espacio de las bases.
        pila.getTransforms().add(new Scale(escala, escala));
        VBox monton = new VBox(7, etiqueta(nombre, "nombre-monton"), new Group(pila));
        monton.setAlignment(Pos.TOP_CENTER);
        monton.setMinWidth(90);
        monton.setPrefWidth(90);
        return monton;
    }

    private Label etiqueta(String texto, String estilo) {
        Label etiqueta = new Label(texto);
        etiqueta.getStyleClass().add(estilo);
        return etiqueta;
    }
}
