package monopoly.carta;

import monopoly.Jugador;
import monopoly.Monopoly;
import monopoly.casilla.Casilla;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import consola.IConsola;

public class Suerte extends Carta {
    // ···········
    // Propiedades
    // ···········

    private static final List<Suerte> baraja;

    // ·············
    // Constructores
    // ·············

    public Suerte(String texto, Consumer<Jugador> accion) {
        super(texto, accion);
    }

    // ·········
    // Overrides
    // ·········

    @Override
    public String toString() {
        return String.format("suerte: '%s'", this.get_texto());
    }

    // ·······
    // Getters
    // ·······

    public static List<Suerte> get_baraja() {
        return baraja;
    }

    static {
        baraja = new ArrayList<Suerte>(Arrays.asList(
                new Suerte("Viaje", j -> viaje(j, "Vigo", "decides hacer un viaje por placer hasta %s!\n")),

                new Suerte("Loteria", j -> {
                    IConsola cons = Monopoly.get().get_consola();
                    cons.imprimir("has ganado la loteria!, recibes 100000!\n");
                    j.add_fortuna(100000);
                }),
                new Suerte("Carcel", j -> {
                    IConsola cons = Monopoly.get().get_consola();
                    cons.imprimir("te persiguen por impago, vas a la carcel!\n");
                    j.ir_a_carcel();
                }),
                new Suerte("Trafico", j -> {
                    IConsola cons = Monopoly.get().get_consola();
                    cons.imprimir("hora punta de trafico, retrocedes tres casillas!\n");
                    Casilla actual = Monopoly.get().get_tablero().buscar_jugador(j);
                    j.get_avatar().avanzar(actual, -3);
                }),

                new Suerte("Multa", j -> {
                    IConsola cons = Monopoly.get().get_consola();
                    cons.imprimir("has recibido una multa por usar el movil conduciendo, pagas 15000!\n");
                    j.add_fortuna(-15000);
                }),

                new Suerte("Beneficio", j -> {
                    IConsola cons = Monopoly.get().get_consola();
                    cons.imprimir("has recibido un ingreso por venta de acciones, recibes 150000!\n");
                    j.add_fortuna(150000);
                })));
    }
}
