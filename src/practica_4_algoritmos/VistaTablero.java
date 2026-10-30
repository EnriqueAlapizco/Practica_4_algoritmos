package practica_4_algoritmos;

import java.util.Objects;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
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
    private ZonaJuego zonaSeleccionada;
    private int indiceSeleccionado = -1;
    private VistaCarta cartaSeleccionada;
    private final Label mensaje = new Label("Selecciona una carta libre y luego su destino.");

    public VistaTablero(JuegoEightOff juego) {
        this.juego = Objects.requireNonNull(juego);
        getStyleClass().add("tablero");
        mensaje.getStyleClass().add("mensaje");
        mensaje.setWrapText(true);
        setFocusTraversable(true);
        setOnKeyPressed(evento -> {
            if (evento.getCode() == KeyCode.ESCAPE) {
                cancelarSeleccion();
                informar("Seleccion cancelada.", false);
                evento.consume();
            }
        });
        actualizar();
    }

    /** Reconstruye la vista a partir de las listas del modelo. */
    public void actualizar() {
        cancelarSeleccion();
        double vertical = getCenter() instanceof ScrollPane
                ? ((ScrollPane) getCenter()).getVvalue() : 0;
        double horizontal = getCenter() instanceof ScrollPane
                ? ((ScrollPane) getCenter()).getHvalue() : 0;
        Label titulo = etiqueta("Eight Off", "titulo");
        Label movimientos = etiqueta("Movimientos: " + juego.getCantidadMovimientos(), "indicador");
        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);
        HBox cabecera = new HBox(12, titulo, espacio, movimientos);
        cabecera.setAlignment(Pos.CENTER_LEFT);
        VBox encabezado = new VBox(6, cabecera, mensaje);
        encabezado.getStyleClass().add("cabecera");
        setTop(encabezado);

        HBox reservas = new HBox(14);
        HBox columnas = new HBox(14);
        VBox bases = new VBox(12, etiqueta("BASES", "seccion"));
        for (int i = 0; i < JuegoEightOff.CANTIDAD_RESERVAS; i++) {
            reservas.getChildren().add(crearMonton("R" + (i + 1), juego.getReserva(i), false, "Libre", 1, ZonaJuego.RESERVA, i));
        }
        for (int i = 0; i < JuegoEightOff.CANTIDAD_COLUMNAS; i++) {
            columnas.getChildren().add(crearMonton("C" + (i + 1), juego.getColumna(i), true, "Rey", 1, ZonaJuego.COLUMNA, i));
        }
        for (Palo palo : Palo.values()) {
            ListaSimple<Carta> cartas = juego.getBase(palo.ordinal());
            bases.getChildren().add(crearMonton(palo.getNombre() + " · " + cartas.getTamanio() + "/13",
                    cartas, false, "A " + VistaCarta.simbolo(palo), 0.8, ZonaJuego.BASE, palo.ordinal()));
        }

        VBox mesa = new VBox(14, etiqueta("RESERVAS", "seccion"), reservas,
                etiqueta("COLUMNAS", "seccion"), columnas);
        HBox contenido = new HBox(30, mesa, bases);
        contenido.getStyleClass().add("contenido");
        ScrollPane desplazamiento = new ScrollPane(contenido);
        desplazamiento.setFitToWidth(true);
        desplazamiento.setPannable(false);
        desplazamiento.getStyleClass().add("desplazamiento");
        setCenter(desplazamiento);
        desplazamiento.setVvalue(vertical);
        desplazamiento.setHvalue(horizontal);

    }

    private VBox crearMonton(String nombre, ListaSimple<Carta> cartas,
            boolean desplegar, String textoVacio, double escala, ZonaJuego zona, int indice) {
        Pane pila = new Pane();
        pila.setId(zona.name().toLowerCase(java.util.Locale.ROOT) + "-" + indice);
        double alto = VistaCarta.ALTO + (desplegar ? Math.max(0, cartas.getTamanio() - 1) * SEPARACION_CARTAS : 0);
        pila.setMinSize(VistaCarta.ANCHO, alto);
        pila.setPrefSize(VistaCarta.ANCHO, alto);
        pila.setMaxSize(VistaCarta.ANCHO, alto);
        if (cartas.estaVacia()) {
            StackPane hueco = new StackPane(new Label(textoVacio));
            hueco.getStyleClass().add("hueco");
            hueco.setPrefSize(VistaCarta.ANCHO, VistaCarta.ALTO);
            pila.getChildren().add(hueco);
            conectarClic(hueco, zona, indice, null, false);
        } else if (desplegar) {
            int posicion = 0;
            for (Carta carta : cartas) {
                VistaCarta vista = new VistaCarta(carta);
                vista.setLayoutY(posicion++ * SEPARACION_CARTAS);
                conectarClic(vista, zona, indice, vista, posicion == cartas.getTamanio());
                pila.getChildren().add(vista);
            }
        } else {
            VistaCarta vista = new VistaCarta(cartas.obtenerFinal());
            conectarClic(vista, zona, indice, vista, true);
            pila.getChildren().add(vista);
        }
        // Group incorpora el tamano escalado al calculo del espacio de las bases.
        pila.getTransforms().add(new Scale(escala, escala));
        VBox monton = new VBox(7, etiqueta(nombre, "nombre-monton"), new Group(pila));
        monton.setAlignment(Pos.TOP_CENTER);
        monton.setMinWidth(90);
        monton.setPrefWidth(90);
        return monton;
    }

    private void conectarClic(Node nodo, ZonaJuego zona, int indice,
            VistaCarta vista, boolean libre) {
        nodo.setOnMouseClicked(evento -> {
            if (evento.getButton() == MouseButton.PRIMARY) {
                requestFocus();
                seleccionarOMover(zona, indice, vista, libre);
                evento.consume();
            }
        });
    }

    private void seleccionarOMover(ZonaJuego zona, int indice, VistaCarta vista, boolean libre) {
        if (zonaSeleccionada != null) {
            if (zona == zonaSeleccionada && indice == indiceSeleccionado) {
                cancelarSeleccion();
                informar("Seleccion cancelada.", false);
            } else if (juego.mover(zonaSeleccionada, indiceSeleccionado, zona, indice)) {
                actualizar();
                informar("Movimiento realizado. Selecciona otra carta.", false);
            } else {
                informar("Movimiento no permitido. Elige otro destino o pulsa Esc para cancelar.", true);
            }
            return;
        }
        if (zona == ZonaJuego.BASE) {
            informar("Las bases solo reciben cartas.", true);
        } else if (vista == null) {
            informar("Ese espacio esta vacio. Selecciona primero una carta.", true);
        } else if (!libre) {
            informar("Solo puedes seleccionar la ultima carta de la columna.", true);
        } else {
            zonaSeleccionada = zona;
            indiceSeleccionado = indice;
            cartaSeleccionada = vista;
            vista.getStyleClass().add("seleccionada");
            informar("Carta seleccionada. Elige un destino; otro clic en ella o Esc cancela.", false);
        }
    }

    private void cancelarSeleccion() {
        if (cartaSeleccionada != null) {
            cartaSeleccionada.getStyleClass().remove("seleccionada");
        }
        cartaSeleccionada = null;
        zonaSeleccionada = null;
        indiceSeleccionado = -1;
    }

    private void informar(String texto, boolean error) {
        mensaje.setText(texto);
        mensaje.getStyleClass().remove("error");
        if (error) { mensaje.getStyleClass().add("error"); }
    }

    private Label etiqueta(String texto, String estilo) {
        Label etiqueta = new Label(texto);
        etiqueta.getStyleClass().add(estilo);
        return etiqueta;
    }
}
