package monopoly.carta;

import java.util.function.Consumer;

import monopoly.Jugador;
import monopoly.Monopoly;
import monopoly.casilla.*;

public class Carta {
    // ···········
    // Propiedades
    // ···········

    private final String texto;
    private Consumer<Jugador> accion;

    // ·············
    // Constructores
    // ·············

    public Carta(String texto, Consumer<Jugador> accion) {
        this.texto = texto;
        this.accion = accion;
    }

    // ·······
    // Getters
    // ·······

    public String get_texto() {
        return texto;
    }

    // ················
    // Interfaz pública
    // ················

    public void ejecutar(Jugador jugador) {
        accion.accept(jugador);
    }

    // ·················
    // Funciones propias
    // ·················

    static void viaje(Jugador j, String destino, String msg) {
        Casilla actual = Monopoly.get().get_tablero().buscar_jugador(j);
        Casilla siguiente = Monopoly.get().get_tablero().buscar_casilla(destino);

        System.out.format(msg, destino);
        actual.remove(j);
        siguiente.add(j, false);
    }
}
