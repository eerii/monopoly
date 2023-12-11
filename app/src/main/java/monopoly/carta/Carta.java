package monopoly.carta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import monopoly.Jugador;
import monopoly.Monopoly;
import monopoly.casilla.*;

public class Carta {

    private final String texto;
    private Consumer<Jugador> accion;

    public Carta(String texto, Consumer<Jugador> accion) {
        this.texto = texto;
        this.accion = accion;
    }

    public void ejecutar(Jugador jugador) {
        accion.accept(jugador);
    }

    static void viaje(Jugador j, String destino, String msg) {
        Casilla actual = Monopoly.get().get_tablero().buscar_jugador(j);
        Casilla siguiente = Monopoly.get().get_tablero().buscar_casilla(destino);

        System.out.format(msg, destino);
        actual.remove_jugador(j);
        siguiente.add_jugador(j);
        siguiente.sumar_veces_visitada();
    }

    public String getTexto() {
        return texto;
    }
}
