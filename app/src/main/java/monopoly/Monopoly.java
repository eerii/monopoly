package monopoly;

import java.util.ArrayList;
import java.util.List;

public class Monopoly {
    List<Jugador> jugadores;
    Tablero tablero;
    Dados dados;
    int turno = -1;

    static Monopoly instance = null;

    Monopoly() {
        jugadores = new ArrayList<Jugador>();
        tablero = new Tablero();
        dados = new Dados();
    }

    public static Monopoly get() {
        if (instance == null)
            instance = new Monopoly();
        return instance;
    }

    public Tablero get_tablero() {
        return tablero;
    }

    public List<Jugador> get_jugadores() {
        return jugadores;
    }

    public Dados get_dados() {
        return dados;
    }

    public void add_jugador(Jugador jugador) {
        if (jugadores.size() >= 6) {
            throw new IllegalStateException("hay demasiados jugadores");
        }
        jugadores.add(jugador);
        if (jugadores.size() == 1) {
            turno = 0;
        }
    }

    public Jugador buscar_jugador(String nombre) {
        for (Jugador j : jugadores) {
            if (j.get_nombre().equals(nombre)) {
                return j;
            }
        }
        return null;
    }

    public Jugador get_turno() {
        if (turno < 0)
            return null;
        return jugadores.get(turno);
    }

    public void siguiente_turno() {
        turno = (turno + 1) % jugadores.size();
    }
}
