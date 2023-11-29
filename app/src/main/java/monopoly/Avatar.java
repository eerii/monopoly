package monopoly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import consola.Color;
import monopoly.casilla.*;

import  consola.excepciones.*;

// TODO: Jerarquía de avatares

public class Avatar {
    char id;
    boolean modo_avanzado = false;
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
            for (TipoAvatar t : values()) {
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
            id = (char) (r.nextInt(26) + 'A');
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
    }

    public boolean es_modo_avanzado() {
        return modo_avanzado;
    }

    public void cambiar_modo() {
        modo_avanzado = !modo_avanzado;
    }

    // Movimiento
    void siguiente_casilla(Casilla actual, int movimiento) throws ConsolaException{
        if (!modo_avanzado) {
            avanzar(actual, movimiento);
            return;
        }

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
                Jugador j = get_jugador();

                if (movimiento < 4) {
                    avanzar(actual, -movimiento);
                    j.add_penalizacion(2);
                    j.add_turno_extra(-1);
                    break;
                }

                j.add_turno_extra(3);

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
        System.out.format("el avatar %s%s%s%s %s %d posiciones, desde %s%s%s%s a %s%s%s%s\n",
                Color.AZUL_CLARITO, Color.BOLD, j.representar(), Color.RESET,
                movimiento < 0 ? "retrocede" : "avanza",
                Math.abs(movimiento),
                actual.get_color(), Color.BOLD, actual.get_nombre(), Color.RESET,
                siguiente.get_color(), Color.BOLD, siguiente.get_nombre(), Color.RESET);

        actual.remove_jugador(j);
        siguiente.add_jugador(j);
        siguiente.sumar_veces_visitada();

        return siguiente;
    }

    // String
    @Override
    public String toString() {
        try {
            Jugador j = get_jugador();
            Tablero t = Monopoly.get().get_tablero();
            Casilla c = t.buscar_jugador(j);
            return String.format(
                    "%s%s%s%s - tipo: %s%s%s - jugador: %s%s%s%s - casilla: %s%s%s%s",
                    Color.AZUL_OSCURO, Color.BOLD, String.valueOf(id), Color.RESET,
                    Color.BOLD, tipo, Color.RESET,
                    Color.AMARILLO, Color.BOLD, j.get_nombre(), Color.RESET,
                    Color.VERDE, Color.BOLD, c.get_nombre(), Color.RESET);

        }catch (ConsolaException e) {

            System.out.println(e.getMessage());
        }

        return "";

    }

    static {
        ocupados = new ArrayList<Character>();
    }
}
