package monopoly.casilla.propiedad;

import consola.Color;
import monopoly.Dados;
import monopoly.Jugador;
import monopoly.Monopoly;

public class Servicio extends Propiedad {
    // ···········
    // Propiedades
    // ···········

    public Servicio(String nombre) {
        super(nombre);
    }

    @Override
    public String toString() {
        Jugador propietario = this.get_propietario();
        float media = Monopoly.get().get_tablero().precio_medio();
        Dados d = Monopoly.get().get_dados();
        String sn = String.format("%s%s%s%s", Color.AZUL_CLARITO, Color.BOLD, this.get_nombre(), Color.RESET);
        String st = String.format("%s%s%s%s", Color.VERDE, Color.BOLD, "servicio", Color.RESET);
        String sj = String.format("%s%s%s", Color.BOLD, this.lista_jugadores(), Color.RESET);
        String sa = String.format("%s%s%.0f%s", Color.ROJO, Color.BOLD, (float) Math
                .floor(media / 200 * (propietario.num_servicios() == 1 ? 4 : 10) * (d.get_a() + d.get_b())), Color.RESET);
        String sjp = get_propietario() != null ? String.format("%s%s%s%s", Color.AZUL_CLARITO, Color.BOLD,
                this.get_propietario().get_nombre(), Color.RESET) : "";

        return String.format(
                "%s - tipo: %s - propietario: %s - a pagar: %s - jugadores: %s",
                sn, st, sjp, sa, sj);
    }
}
