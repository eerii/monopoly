package monopoly.carta;

import monopoly.Jugador;
import monopoly.Monopoly;
import monopoly.casilla.Casilla;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.function.Consumer;

import consola.IConsola;

public class Comunidad extends Carta {
    // ···········
    // Propiedades
    // ···········

    private static final List<Comunidad> baraja;

    // ·············
    // Constructores
    // ·············

    public Comunidad(String texto, Consumer<Jugador> accion) {
        super(texto, accion);
    }

    // ·········
    // Overrides
    // ·········

    @Override
    public String toString() {
        return String.format("comunidad: '%s'", this.get_texto());
    }

    // ·······
    // Getters
    // ·······

    public static List<Comunidad> get_baraja() {
        return baraja;
    }

    static {
        baraja = new ArrayList<Comunidad>(Arrays.asList(
                new Comunidad("Viaje",
                        j -> viaje(j, "Ferrol", "viajas hasta %s para comprar antigüedades exóticas!\n")),

                new Comunidad("Balneario", j -> {
                    IConsola cons = Monopoly.get().get_consola();
                    cons.imprimir("paga 50000 por un fin de semana en un balneario!\n");
                    j.add_fortuna(-50000);
                }),

                new Comunidad("Fraude", j -> {
                    IConsola cons = Monopoly.get().get_consola();
                    cons.imprimir("te investigan por fraude, vas a la carcel!\n");
                    j.ir_a_carcel();
                }),

                new Comunidad("Salida", j -> {
                    IConsola cons = Monopoly.get().get_consola();
                    cons.imprimir("ve a la salida para cobrar!\n");
                    Casilla actual = Monopoly.get().get_tablero().buscar_jugador(j);
                    actual.remove(j);
                    Casilla c = Monopoly.get().get_tablero().buscar_casilla("Salida");
                    c.add(j, false);
                }),

                new Comunidad("Hacienda", j -> {
                    IConsola cons = Monopoly.get().get_consola();
                    cons.imprimir("recibes una devolucion de hacienda de 50000!\n");
                    j.add_fortuna(50000);
                }),

                new Comunidad("Beneficio", j -> {
                    IConsola cons = Monopoly.get().get_consola();
                    cons.imprimir("tu compañia de internet obtiene beneficios, recibes 20000!\n");
                    j.add_fortuna(20000);
                })));
    }
}
