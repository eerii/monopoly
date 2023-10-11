package monopoly;

import java.util.Random;

public class Dados {
    int a;
    int b;
    int dobles = 0;
    Jugador jugador;
    boolean primera_tirada;

    public void tirar(Jugador jugador) {
        Random r = new Random();
        a = (r.nextInt(6) + 1);
        b = (r.nextInt(6) + 1);
        if (a == b)
            dobles += 1;

        primera_tirada = this.jugador != jugador;
        this.jugador = jugador;
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
    
    public boolean primera_tirada() {
        return primera_tirada;
    }
}
