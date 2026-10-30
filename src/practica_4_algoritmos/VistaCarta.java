package practica_4_algoritmos;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/** Representacion visual de una carta inmutable del modelo. */
public class VistaCarta extends StackPane {
    public static final double ANCHO = 86;
    public static final double ALTO = 118;

    public VistaCarta(Carta carta) {
        setMinSize(ANCHO, ALTO);
        setPrefSize(ANCHO, ALTO);
        setMaxSize(ANCHO, ALTO);
        getStyleClass().add("carta");
        boolean roja = carta.getPalo() == Palo.CORAZONES || carta.getPalo() == Palo.DIAMANTES;
        getStyleClass().add(roja ? "roja" : "negra");

        String esquina = rango(carta.getRango()) + " " + simbolo(carta.getPalo());
        Label superior = new Label(esquina);
        superior.getStyleClass().add("esquina");
        StackPane.setAlignment(superior, Pos.TOP_LEFT);
        Label centro = new Label(simbolo(carta.getPalo()));
        centro.getStyleClass().add("palo-central");
        Label inferior = new Label(esquina);
        inferior.getStyleClass().add("esquina");
        inferior.setRotate(180);
        StackPane.setAlignment(inferior, Pos.BOTTOM_RIGHT);
        getChildren().addAll(superior, centro, inferior);
        setAccessibleText(carta.toString());
    }

    public static String simbolo(Palo palo) {
        switch (palo) {
            case TREBOLES: return "\u2663";
            case DIAMANTES: return "\u2666";
            case CORAZONES: return "\u2665";
            case PICAS: return "\u2660";
            default: throw new IllegalArgumentException("Palo desconocido");
        }
    }

    private String rango(int rango) {
        switch (rango) {
            case 1: return "A";
            case 11: return "J";
            case 12: return "Q";
            case 13: return "K";
            default: return Integer.toString(rango);
        }
    }
}
