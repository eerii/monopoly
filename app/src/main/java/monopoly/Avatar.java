package monopoly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import consola.Color;

// TODO: Tipo de avatar se implementará como subclases

/* Coche: si el valor de los dados es mayor que 4, avanza tantas casillas como dicho valor y puede
seguir lanzando los dados tres veces más mientras el valor sea mayor que 4. Durante el turno solo
se puede realizar una sola compra de propiedades, servicios o transportes, aunque se podría hacer
en cualesquiera de los 4 intentos posibles. Sin embargo, se puede edificar cualquier tipo de edificio
en cualquier intento. Si el valor de los dados es menor que 4, el avatar retrocederá el número de
casillas correspondientes y además no puede volver a lanzar los dados en los siguientes dos turnos. */

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
        static final HashMap<String, TipoAvatar> by_str = new HashMap<>(); 
        static final HashMap<TipoAvatar, String> iconos = new HashMap<>();

        private TipoAvatar(String nombre) {
            this.nombre = nombre;
        }

        public String get_icono() {
            return iconos.get(this);
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
            iconos.put(ESFINGE, "󰄛");
            iconos.put(SOMBRERO, "󰮤");
            iconos.put(PELOTA, "");
            iconos.put(COCHE, "");
        }
    }

    public Avatar() {
        this(TipoAvatar.SOMBRERO);
    }

    public Avatar(TipoAvatar tipo) {
        Random r = new Random();
        do {
            id = (char)(r.nextInt(26) + 'A');
        } while (ocupados.contains(id));
        ocupados.add(id);
        this.tipo = tipo;
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

    // Movimiento
    void siguiente_casilla(Casilla actual, int movimiento) {
        // TODO: Cambiar movimientos

        switch (tipo) {
            case PELOTA:
                if (movimiento < 4) {
                    avanzar(actual, -movimiento);
                    break;
                }

                int restantes = movimiento;

                for (int i = 5; i <= movimiento; i += 2) {
                    int avanzar = movimiento - i;
                    actual = avanzar(actual, restantes - avanzar);
                    restantes = avanzar;
                    if (actual.get_nombre().equals("Ir a Cárcel"))
                        break;
                }

                if (restantes > 0)
                    avanzar(actual, restantes);

                break;
            case COCHE:
                if (movimiento < 4) {
                    avanzar(actual, -movimiento);
                    break;
                }

                // TODO: Implementar el resto del movimiento de coche

                avanzar(actual, movimiento);
                break;
            default:
                avanzar(actual, movimiento);
        }
    }

    Casilla avanzar(Casilla actual, int movimiento) {
        Monopoly m = Monopoly.get();
        List<Casilla> casillas = m.get_tablero().get_casillas();
        Jugador j = get_jugador();

        int ini = casillas.indexOf(actual);
        int sig = ini + movimiento;
        int vueltas = sig / casillas.size();
        j.dar_vueltas(vueltas);

        sig %= casillas.size();
        if (sig < 0)
            sig += casillas.size();

        Casilla siguiente = casillas.get(sig);

        System.out.format("el avatar %s%s%s%s avanza %d posiciones, desde %s%s%s%s a %s%s%s%s\n",
            Color.AZUL_CLARITO, Color.BOLD, j.representar(), Color.RESET,
            movimiento,
            actual.get_color(), Color.BOLD, actual.get_nombre(), Color.RESET,
            siguiente.get_color(), Color.BOLD, siguiente.get_nombre(), Color.RESET);

        actual.remove_jugador(j);
        siguiente.add_jugador(j);

        // TODO: Parar para preguntar si quiere comprar la casilla

        return siguiente;
    }

    // String
    @Override
    public String toString() {
        Jugador j = get_jugador();
        Tablero t = Monopoly.get().get_tablero();
        Casilla c = t.buscar_jugador(j);

        return String.format(
            "%s%s%s%s - tipo: %s%s%s - jugador: %s%s%s%s - casilla: %s%s%s%s",
            Color.AZUL_OSCURO, Color.BOLD, String.valueOf(id), Color.RESET,
            Color.BOLD, tipo, Color.RESET,
            Color.AMARILLO, Color.BOLD, j.get_nombre(), Color.RESET,
            Color.VERDE, Color.BOLD, c.get_nombre(), Color.RESET
        );
    }

    static {
        ocupados = new ArrayList<Character>();
    }
}
