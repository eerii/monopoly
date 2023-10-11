package monopoly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import consola.Color;

// TODO: Tipo de avatar se implementará como subclases

public class Avatar {
    char id;
    static List<Character> ocupados;
    TipoAvatar tipo;

    public enum TipoAvatar {
        ESFINGE("esfinge"),
        SOMBRERO("sombrero"),
        PELOTA("pelota"),
        COCHE("coche");

        public final String nombre;
        private static final HashMap<String, TipoAvatar> by_str = new HashMap<>(); 

        private TipoAvatar(String nombre) {
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return nombre;
        }

        public static TipoAvatar from_str(String avatar) {
            if (!by_str.containsKey(avatar)) {
                throw new IllegalArgumentException("avatar inválido");
            }
            return by_str.get(avatar);
        }

        static {
            for (TipoAvatar t: values()) {
                by_str.put(t.nombre, t);
            }
        }
    }

    public Avatar(TipoAvatar tipo) {
        Random r = new Random();
        do {
            id = (char)(r.nextInt(26) + 'A');
        } while (ocupados.contains(id));
        ocupados.add(id);
        this.tipo = tipo;
    }

    Casilla siguiente_casilla(Tablero tablero) {
        throw null;
    }

    public char get_id() {
        return id;
    }

    public TipoAvatar get_tipo() {
        return tipo;
    }

    Jugador get_jugador() {
        List<Jugador> jugadores = Monopoly.get().get_jugadores();
        for (Jugador j : jugadores) {
            if (j.get_avatar() == this)
                return j;
        }
        throw new RuntimeException("unreachable");
    }

    // String
    @Override
    public String toString() {
        Jugador j = get_jugador();
        Tablero t = Monopoly.get().get_tablero();
        Casilla c = t.buscar_jugador(j);

        return String.format(
            "%s%s%s%s - tipo: %s%s%s - jugador: %s%s%s%s - casilla: %s%s%s%s",
            Color.AZUL, Color.BOLD, String.valueOf(id), Color.RESET,
            Color.BOLD, tipo, Color.RESET,
            Color.AMARILLO, Color.BOLD, j.get_nombre(), Color.RESET,
            Color.VERDE, Color.BOLD, c.get_nombre(), Color.RESET
        );
    }

    static {
        ocupados = new ArrayList<Character>();
    }
}
