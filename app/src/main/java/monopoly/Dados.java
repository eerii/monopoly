package monopoly;

import java.util.Random;

public class Dados {
    int a;
    int b;
    int dobles = 0;
    Jugador jugador;

    public boolean cambio_jugador(Jugador jugador) {
        if (this.jugador == jugador)
            return false;
        this.jugador = jugador;

        dobles = 0; 
        return true;
    }

    public void tirar() {
        Random r = new Random();
        a = (r.nextInt(6) + 1);
        b = (r.nextInt(6) + 1);
        dobles = a == b ? dobles + 1 : 0;
    }

    public int get_a() {
        return a;
    }

    public int get_b() {
        return b;
    }

    public boolean son_dobles() {
        return dobles > 0;
    }

    public void debug_set(int a, int b) {
        this.a = a;
        this.b = b;
        dobles = a == b ? dobles + 1 : 0;
    }
}
