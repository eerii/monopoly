package monopoly.casilla;

import consola.Color;
import consola.IConsola;
import monopoly.Jugador;
import monopoly.Monopoly;

public class Impuesto extends Casilla {
    // ·············
    // Constructores
    // ·············

    public Impuesto(String nombre) {
        super(nombre);
    }

    // ·········
    // Overrides
    // ·········

    @Override
    public void add(Jugador jugador, boolean ignorar) {
        super.add(jugador, ignorar);

        if (!ignorar) {
            Monopoly m = Monopoly.get();
            IConsola cons = m.get_consola();
            jugador.add_fortuna(this.get_precio() * -1.f);
            m.get_banca().add_fortuna(this.get_precio());
            cons.imprimir("el jugador %s ha caído la casilla de impuestos, paga %.0f a la banca!\n",
                    jugador.get_nombre(), this.get_precio());
            m.get_stats().of(jugador)
                    .sumar_pago_tasa_impuestos(this.get_precio());
        }
    }

    @Override
    public Color get_color() {
        return Color.DIM;
    }

    @Override
    public String toString() {
        String sn = String.format("%s%s%s%s", Color.AZUL_CLARITO, Color.BOLD, this.get_nombre(), Color.RESET);
        String st = String.format("%s%s%s%s", Color.VERDE, Color.BOLD, "impuestos", Color.RESET);
        String sj = String.format("%s%s%s", Color.BOLD, this.lista_jugadores(), Color.RESET);
        String sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, this.get_precio(), Color.RESET);

        return String.format("%s - tipo: %s - a pagar: %s - jugadores: %s", sn, st, sp, sj);
    }

}
