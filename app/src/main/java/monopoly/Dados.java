package monopoly;

import java.util.Random;

public class Dados {
    // ···········
    // Propiedades
    // ···········

    private int a;
    private int b;
    private int dobles = -1;
    private Jugador jugador;

    // ·······
    // Getters
    // ·······

    public int get_a() {
        return a;
    }

    public int get_b() {
        return b;
    }

    public int get_dobles() {
        return dobles;
    }

    // ················
    // Interfaz pública
    // ················

    public boolean cambio_jugador(Jugador jugador) {
        if (this.jugador == jugador)
            return false;
        this.jugador = jugador;
        dobles = -1;
        return true;
    }

    public void tirar() {
        Random r = new Random();
        a = (r.nextInt(6) + 1);
        b = (r.nextInt(6) + 1);
        dobles = a == b ? Math.max(dobles, 0) + 1 : 0;
    }

    public void debug_set(int a, int b) {
        this.a = a;
        this.b = b;
        dobles = a == b ? Math.max(dobles, 0) + 1 : 0;
    }
}
