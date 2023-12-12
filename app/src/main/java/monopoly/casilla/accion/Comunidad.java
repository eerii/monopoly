package monopoly.casilla.accion;

import java.util.List;

import monopoly.Monopoly;
import monopoly.carta.Carta;

public class Comunidad extends Accion {
    // ·············
    // Constructores
    // ·············

    public Comunidad(String nombre) {
        super(nombre);
    }

    // ·········
    // Overrides
    // ·········

    @Override
    List<Carta> get_baraja() {
        return Monopoly.get().get_comunidad();
    }
}
