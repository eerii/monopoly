package estadisticas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import consola.Color;
import monopoly.Jugador;
import monopoly.Monopoly;
import monopoly.casilla.*;

public class Estadisticas {
    private Map<Jugador, EstadisticasJugador> jugador;

    private String casillaMasRentable;
    private String grupoMasRentable;
    private String casillaMasFrecuentada;
    private String jugadorMasVueltas;
    private String jugadorMasVecesDados;
    private String jugadorEnCabeza;

    public Estadisticas() {
        jugador = new HashMap<Jugador, EstadisticasJugador>();

    }

    public void add_jugador(Jugador j) {
        jugador.put(j, new EstadisticasJugador());
    }

    public EstadisticasJugador of(Jugador j) {
        return jugador.get(j);
    }

    public void casilla_mas_rentable() {
        List<Casilla> casillas = Monopoly.get().get_tablero().get_casillas();
        float max = 0;
        for (Casilla c : casillas) {
            if (c instanceof Propiedad) {
                Propiedad p = (Propiedad) c;
                if (p.get_rentabilidad() > max) {
                    max = p.get_rentabilidad();
                    casillaMasRentable = c.get_nombre();
                }
            }
        }
    }

    public void grupo_mas_rentable() {
        List<Grupo> grupos = Monopoly.get().get_tablero().get_grupos();
        float max = 0;
        for (Grupo g : grupos) {
            List<Solar> casillas = g.get_casillas();
            float total = 0;
            for (Solar c : casillas) {
                total += c.get_rentabilidad();
            }
            if (total > max) {
                max = total;
                grupoMasRentable = g.get_nombre();
            }

        }
    }

    public void casilla_mas_visitada() {
        List<Casilla> casillas = Monopoly.get().get_tablero().get_casillas();
        float max = 0;
        for (Casilla c : casillas) {
            if (c.get_veces_visitada() > max) {
                max = c.get_veces_visitada();
                casillaMasFrecuentada = c.get_nombre();
            }
        }
    }

    public void jugador_mas_vueltas() {
        List<Jugador> jugadores = Monopoly.get().get_jugadores();
        float max = 0;
        for (Jugador j : jugadores) {
            if (j.get_vueltas() > max) {
                max = j.get_vueltas();
                jugadorMasVueltas = j.get_nombre();
            }
        }
    }

    public void jugador_mas_tiradas() {
        List<Jugador> jugadores = Monopoly.get().get_jugadores();
        float max = 0;
        for (Jugador j : jugadores) {
            if (j.get_tiradas() > max) {
                max = j.get_tiradas();
                jugadorMasVecesDados = j.get_nombre();
            }
        }
    }

    public void jugador_en_cabeza() {
        List<Jugador> jugadores = Monopoly.get().get_jugadores();
        float max = 0;
        for (Jugador j : jugadores) {
            if (j.get_fortunaTotal() > max) {
                max = j.get_fortunaTotal();
                jugadorEnCabeza = j.get_nombre();
            }
        }
    }

    @Override
    public String toString() {
        jugador_mas_vueltas();
        jugador_en_cabeza();
        grupo_mas_rentable();
        casilla_mas_visitada();
        casilla_mas_rentable();
        jugador_mas_tiradas();
        String n = casillaMasRentable;
        if (n == null)
            n = " - ";
        String s1 = String.format("%s%scasilla mas rentable%s: %s%s%s", Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, n, Color.RESET);
        n = grupoMasRentable;
        if (n == null)
            n = " - ";
        String s2 = String.format("%s%sgrupo mas rentable%s: %s%s%s", Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, n, Color.RESET);

        n = casillaMasFrecuentada;
        if (n == null)
            n = " - ";
        String s3 = String.format("%s%scasilla mas visitada%s: %s%s%s", Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, n, Color.RESET);

        n = jugadorMasVueltas;
        if (n == null)
            n = " - ";
        String s4 = String.format("%s%sjugador con mas vueltas dadas%s: %s%s%s", Color.AZUL_CLARITO, Color.BOLD,
                Color.RESET,
                Color.BOLD, n, Color.RESET);

        n = jugadorMasVecesDados;
        if (n == null)
            n = " - ";
        String s5 = String.format("%s%sjugador con mas tiradas%s: %s%s%s", Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, n, Color.RESET);

        n = jugadorEnCabeza;
        if (n == null)
            n = " - ";
        String s6 = String.format("%s%sjugador en cabeza%s: %s%s%s", Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, n, Color.RESET);

        return String.format("%s,\n%s,\n%s,\n%s,\n%s,\n%s\n", s1, s2, s3, s4, s5, s6);
    }
}
