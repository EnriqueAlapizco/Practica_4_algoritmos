package practica_4_algoritmos;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Inicio de la interfaz; las reglas permanecen en JuegoEightOff. */
public class VentanaEightOff extends Application {
    @Override
    public void start(Stage ventana) {
        VistaTablero tablero = new VistaTablero(new JuegoEightOff());
        Scene escena = new Scene(tablero, 1120, 780);
        escena.getStylesheets().add(getClass().getResource("tablero.css").toExternalForm());
        ventana.setTitle("Eight Off");
        ventana.setScene(escena);
        ventana.setMinWidth(800);
        ventana.setMinHeight(600);
        ventana.show();
    }
}
