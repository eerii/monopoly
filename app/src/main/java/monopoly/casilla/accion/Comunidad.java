package monopoly.casilla.accion;

import java.util.List;

import monopoly.Monopoly;
import monopoly.carta.Carta;

public class Comunidad extends Accion {
    public Comunidad(String nombre) {
        super(nombre);
    }

    @Override
    List<Carta> get_baraja() {
        return Monopoly.get().get_barajaComunidad();
    }
}
