package monopoly.casilla;

import monopoly.Jugador;
import monopoly.Monopoly;

public class Impuesto extends Casilla {
    public Impuesto(String nombre) {
        super(nombre);
    }

    @Override
    public void add_jugador(Jugador jugador, boolean ignorar) {
        super.add_jugador(jugador, ignorar);

        if (!ignorar) {
            Monopoly m = Monopoly.get();
            jugador.add_fortuna(this.get_precio() * -1.f);
            m.get_banca().add_fortuna(this.get_precio());
            System.out.format("el jugador %s ha caído la casilla de impuestos, paga %.0f a la banca!\n",
                    jugador.get_nombre(), this.get_precio());
            m.get_stats().of(jugador)
                    .sumar_pago_tasa_impuestos(this.get_precio());
        }
    }
}
