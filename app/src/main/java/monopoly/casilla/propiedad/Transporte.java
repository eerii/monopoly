package monopoly.casilla.propiedad;

import consola.Color;
import monopoly.Monopoly;
import monopoly.Jugador;

public class Transporte extends Propiedad {
    // ···········
    // Propiedades
    // ···········

    public Transporte(String nombre) {
        super(nombre);
    }

    @Override
    public String toString() {
        Jugador propietario = this.get_propietario();
        float media = Monopoly.get().get_tablero().precio_medio();
        String sn = String.format("%s%s%s%s", Color.AZUL_CLARITO, Color.BOLD, this.get_nombre(), Color.RESET);
        String st = String.format("%s%s%s%s", Color.VERDE, Color.BOLD, "transporte", Color.RESET);
        String sj = String.format("%s%s%s", Color.BOLD, this.lista_jugadores(), Color.RESET);
        String sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, this.get_precio(), Color.RESET);
        String sa = String.format("%s%s%.0f%s", Color.ROJO, Color.BOLD, (float) Math.floor(media * propietario.num_transportes() * 0.25), Color.RESET);
        String sjp = get_propietario() != null ? String.format("%s%s%s%s", Color.AZUL_CLARITO, Color.BOLD,
                this.get_propietario().get_nombre(), Color.RESET) : "";

        return String.format(
                "%s - tipo: %s - propietario: %s - valor: %s - a pagar: %s - jugadores: %s",
                sn, st, sjp, sp, sa, sj);
    }

}
