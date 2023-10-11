package monopoly;

import java.util.ArrayList;
import java.util.List;

public class Monopoly {
    List<Jugador> jugadores;
    Tablero tablero;
    static Monopoly instance = null;

    Monopoly() {
        jugadores = new ArrayList<Jugador>();
        tablero = new Tablero();
    }

    Monopoly get_instance() {
        if (instance == null)
            instance = new Monopoly();
        return instance;
    }

    public Tablero get_tablero() {
        return get_instance().tablero;
    }

    public List<Jugador> get_jugador() {
        return get_instance().jugadores;
    }
}
