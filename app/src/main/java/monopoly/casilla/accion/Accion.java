package monopoly.casilla.accion;

import java.util.List;

import monopoly.Jugador;
import monopoly.Monopoly;
import monopoly.carta.Carta;
import monopoly.casilla.*;

public abstract class Accion extends Casilla {
    public Accion(String nombre) {
        super(nombre);
    }

    abstract List<Carta> get_baraja();

    @Override
    public void add_jugador(Jugador jugador, boolean ignorar) {
        super.add_jugador(jugador, ignorar);

        if (!ignorar) {
            List<Carta> baraja = get_baraja();
            Carta c = Monopoly.get().sacar_carta(baraja);
            System.out.format("el jugador %s saca la carta de %s\n",
                    jugador.get_nombre(), c);
            c.ejecutar(jugador);
        }
    }
}
