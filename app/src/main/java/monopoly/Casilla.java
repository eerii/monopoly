package monopoly;

import java.util.Set;
import java.util.HashSet;

// TODO: Mover funcionalidad a Solar cando se poida

public class Casilla {
    String nombre;
    TipoCasilla tipo;
    Set<Jugador> jugadores;

    // Propiedades de Solar
    float precio;
    Boolean hipotecado;
    Grupo grupo;

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
        SALIDA,
        NULL;
    }

    public Casilla(TipoCasilla tipo, String nombre) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.jugadores = new HashSet<Jugador>();
    }

    public Casilla(TipoCasilla tipo, String nombre, int precio, Grupo grupo) {
        this(tipo, nombre);
        this.precio = precio;
        this.hipotecado = false;
        this.grupo = grupo;
        grupo.add(this);
    } 

    public void add_jugador(Jugador jugador) {
        jugadores.add(jugador);
    }

    public void remove_jugador(Jugador jugador) {
        jugadores.remove(jugador);
    }

    public String get_nombre() {
        return nombre;
    }

    // String
    @Override
    public String toString() {
        String str_jugadores = new String("");
        for (Jugador j : jugadores) {
             str_jugadores += " " + j.toString();
        }

        return String.format("%s&%s", nombre, str_jugadores);
    }
}
