package monopoly;

import java.util.List;

// TODO: Mover funcionalidad a Solar cando se poida

public class Casilla {
    List<Jugador> jugadores;
    TipoCasilla tipo;

    // Propiedades de Solar
    int precio;
    Boolean hipotecado;
    String grupo;

    public enum TipoCasilla {
        SOLAR,
        TRANSPORTE,
        SERVICIOS,
        SUERTE,
        COMUNIDAD,
        IMPUESTOS,
        CARCEL,
        IR_A_CARCEL,
        PARKING,
        SALIDA;
    }

    public Casilla(TipoCasilla tipo) {
        this.tipo = tipo;
        this.jugadores = List.of();
    }

    public Casilla(TipoCasilla tipo, int precio, String grupo) {
        this(tipo);
        this.precio = precio;
        this.hipotecado = false;
        this.grupo = grupo;
    }
}
