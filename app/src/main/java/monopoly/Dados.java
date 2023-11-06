package monopoly;

import java.util.Random;

public class Dados {
    int a;
    int b;
    int dobles = -1;
    boolean tirados = false;
    Jugador jugador;

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

    public int get_a() {
        return a;
    }

    public int get_b() {
        return b;
    }

    public int get_total() {return a+b;}

    public int get_dobles() {
        return dobles;
    }

    public void debug_set(int a, int b) {
        this.a = a;
        this.b = b;
        dobles = a == b ? Math.max(dobles, 0) + 1 : 0;
    }
}
