package monopoly;

import java.util.ArrayList;
import java.util.List;

public class Monopoly {
    List<Jugador> jugadores;
    Tablero tablero;

    public Monopoly() {
        jugadores = new ArrayList<Jugador>();
        tablero = new Tablero();
    }

    public Tablero get_tablero() {
        return tablero;
    }

    public List<Jugador> get_jugador() {
        return jugadores;
    }
}
