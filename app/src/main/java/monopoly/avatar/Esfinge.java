package monopoly.avatar;

import consola.Color;
import monopoly.Jugador;
import monopoly.Monopoly;
import monopoly.Tablero;
import monopoly.casilla.Casilla;

public class Esfinge extends Avatar{


    public final String nombre;
    public final String icono;
    // ·············
    // Constructores
    // ·············

    public Esfinge() {
        super();
        nombre = "Esfinge";
        icono = "󰄛";
    }

    // ·········
    // Overrides
    // ·········

    @Override
    public String toString() {
        Jugador j = get_jugador();
        Tablero t = Monopoly.get().get_tablero();
        Casilla c = t.buscar_jugador(j);
        return String.format(
                "%s%s%s%s - tipo: %s%s%s - jugador: %s%s%s%s - casilla: %s%s%s%s",
                Color.AZUL_OSCURO, Color.BOLD, String.valueOf(this.get_id()), Color.RESET,
                Color.BOLD, "esfinge", Color.RESET,
                Color.AMARILLO, Color.BOLD, j.get_nombre(), Color.RESET,
                Color.VERDE, Color.BOLD, c.get_nombre(), Color.RESET);

    }

    public String get_icono() {
        return icono;
    }

    public String representar() {
        Monopoly.Config c = Monopoly.get().get_config();
        if (c.get_iconos()) {
            return this.get_icono();
        }
        return String.valueOf(this.get_id());
    }
}
