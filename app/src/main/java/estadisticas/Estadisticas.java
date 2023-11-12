package estadisticas;

import java.util.HashMap;
import java.util.Map;

import monopoly.Jugador;

public class Estadisticas {
    Map<Jugador, EstadisticasJugador> jugador;

    public Estadisticas() {
        jugador = new HashMap<Jugador, EstadisticasJugador>();
    }

    public void add_jugador(Jugador j) {
        jugador.put(j, new EstadisticasJugador());
    }

    public EstadisticasJugador of(Jugador j) {
        return jugador.get(j);
    }
}
