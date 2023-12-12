package monopoly.casilla.accion;

import java.util.List;

import monopoly.Monopoly;
import monopoly.carta.Carta;

public class Suerte extends Accion {
    // ·············
    // Constructores
    // ·············

    public Suerte(String nombre) {
        super(nombre);
    }

    // ·········
    // Overrides
    // ·········

    @Override
    List<Carta> get_baraja() {
        return Monopoly.get().get_suerte();
    }
}
