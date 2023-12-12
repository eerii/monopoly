package monopoly.casilla.accion;

import java.util.List;

import monopoly.Monopoly;
import monopoly.carta.Carta;

public class Suerte extends Accion {
    public Suerte(String nombre) {
        super(nombre);
    }

    @Override
    List<Carta> get_baraja() {
        return Monopoly.get().get_barajaSuerte();
    }
}
