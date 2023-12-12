package monopoly.casilla.accion;

import java.util.List;

import consola.Color;
import monopoly.Jugador;
import monopoly.Monopoly;
import monopoly.carta.Carta;
import monopoly.casilla.*;

public abstract class Accion extends Casilla {
    // ·············
    // Constructores
    // ·············

    public Accion(String nombre) {
        super(nombre);
    }

    // ·········
    // Overrides
    // ·········

    @Override
    public void add(Jugador jugador, boolean ignorar) {
        super.add(jugador, ignorar);

        if (!ignorar) {
            List<Carta> baraja = get_baraja();
            Carta c = Monopoly.get().sacar_carta(baraja);
            System.out.format("el jugador %s saca la carta de %s\n",
                    jugador.get_nombre(), c);
            c.ejecutar(jugador);
        }
    }

    @Override
    public Color get_color() {
        return Color.ITALIC;
    }

    // ·················
    // Funciones propias
    // ·················

    abstract List<Carta> get_baraja();
}
