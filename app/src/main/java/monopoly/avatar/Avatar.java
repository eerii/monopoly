package monopoly.avatar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import consola.Color;
import consola.IConsola;
import monopoly.Jugador;
import monopoly.Monopoly;
import monopoly.casilla.Casilla;

public abstract class Avatar {
    // ···········
    // Propiedades
    // ···········

    private char id;
    protected boolean modo_avanzado = false;
    private static List<Character> ocupados;

    // ·············
    // Constructores
    // ·············

    public Avatar() {
        Random r = new Random();
        do {
            id = (char) (r.nextInt(26) + 'A');
        } while (ocupados.contains(id));
        ocupados.add(id);
    }

    // ·······
    // Getters
    // ·······

    public char get_id() {
        return id;
    }

    Jugador get_jugador() {
        List<Jugador> jugadores = Monopoly.get().get_jugadores();
        for (Jugador j : jugadores) {
            if (j.get_avatar() == this)
                return j;
        }
        return null;
    }

    public abstract String get_nombre();

    public abstract String get_icono();

    public abstract String representar();

    // ················
    // Interfaz pública
    // ················

    public boolean es_modo_avanzado() {
        return modo_avanzado;
    }

    public void cambiar_modo() {
        modo_avanzado = !modo_avanzado;
    }

    public void siguiente_casilla(Casilla actual, int movimiento) {
            avanzar(actual, movimiento);

    }

    public Casilla avanzar(Casilla actual, int movimiento) {
        Monopoly m = Monopoly.get();
        IConsola cons = m.get_consola();
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
        cons.imprimir("el avatar %s%s%s%s %s %d posiciones, desde %s%s%s%s a %s%s%s%s\n",
                Color.AZUL_CLARITO, Color.BOLD, j.representar(), Color.RESET,
                movimiento < 0 ? "retrocede" : "avanza",
                Math.abs(movimiento),
                actual.get_color(), Color.BOLD, actual.get_nombre(), Color.RESET,
                siguiente.get_color(), Color.BOLD, siguiente.get_nombre(), Color.RESET);

        actual.remove(j);
        siguiente.add(j, false);

        return siguiente;
    }

    static {
        ocupados = new ArrayList<Character>();
    }
}
