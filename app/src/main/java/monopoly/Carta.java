package monopoly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Carta {
    enum TipoCarta {
        COMUNIDAD,
        SUERTE;

        @Override
        public String toString() {
            return this == COMUNIDAD ? "Comunidad" : "Suerte";
        }
    }

    String texto;
    TipoCarta tipo;
    Consumer<Jugador> accion;
    static List<Carta> baraja;

    public Carta(String texto, TipoCarta tipo, Consumer<Jugador> accion) {
        this.texto = texto;
        this.tipo = tipo;
        this.accion = accion;
    }

    public void ejecutar(Jugador jugador) {
        accion.accept(jugador);
    }

    public TipoCarta get_tipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return String.format("%s: '%s'", tipo, texto);
    }

    static {

        baraja = new ArrayList<Carta>(Arrays.asList(
            new Carta("Viaje", Carta.TipoCarta.SUERTE, j -> {
                Casilla actual = Monopoly.get().get_tablero().buscar_jugador(j);
                System.out.println("decides hacer un viaje de placer hasta Vigo!\n");
                actual.remove_jugador(j);
                Casilla c = Monopoly.get().get_tablero().buscar_casilla("Vigo");
                c.add_jugador(j);
            }),
            new Carta("Loteria", Carta.TipoCarta.SUERTE, j -> {
                System.out.println("has ganado la loteria!, recibes 100000!\n");
                j.add_fortuna(100000);
            }),
            new Carta("Carcel", Carta.TipoCarta.SUERTE, j -> {
                System.out.println("te persiguen por impago, vas a la carcel!\n");
                j.ir_a_carcel();
            }),
            new Carta("Trafico", Carta.TipoCarta.SUERTE, j -> {
                System.out.println("hora punta de trafico, retrocedes tres casillas!\n");
                Casilla actual = Monopoly.get().get_tablero().buscar_jugador(j);
                j.get_avatar().avanzar(actual,-3);
            }),

            new Carta("Multa", Carta.TipoCarta.SUERTE, j -> {
                System.out.println("has recibido una multa por usar el movil conduciendo, pagas 15000!\n");
                j.add_fortuna(-15000);
            }),

            new Carta("Beneficio", Carta.TipoCarta.SUERTE, j -> {
                System.out.println("has recibido un ingreso por venta de acciones, recibes 150000!\n");
                j.add_fortuna(150000);
            }),

            new Carta("Balneario", Carta.TipoCarta.COMUNIDAD, j -> {
                System.out.println("paga 50000 por un fin de semana en un balneario!\n");
                j.add_fortuna(-50000);
            }),

            new Carta("Fraude", Carta.TipoCarta.COMUNIDAD, j -> {
                System.out.println("te investigan por fraude, vas a la carcel!\n");
                j.ir_a_carcel();
            }),

            new Carta("Salida", Carta.TipoCarta.COMUNIDAD, j -> {
                System.out.println("ve a la salida para cobrar!\n");
                Casilla actual = Monopoly.get().get_tablero().buscar_jugador(j);
                actual.remove_jugador(j);
                Casilla c = Monopoly.get().get_tablero().buscar_casilla("Salida");
                c.add_jugador(j);
            }),

            new Carta("Hacienda", Carta.TipoCarta.COMUNIDAD, j -> {
                System.out.println("recibes una devolucion de hacienda de 50000!\n");
                j.add_fortuna(50000);
            }),

            new Carta("Beneficio", Carta.TipoCarta.COMUNIDAD, j -> {
                System.out.println("tu compañia de internet obtiene beneficios, recibes 20000!\n");
                j.add_fortuna(20000);
            }),

            new Carta("Viaje", Carta.TipoCarta.COMUNIDAD, j -> {
                Casilla actual = Monopoly.get().get_tablero().buscar_jugador(j);
                System.out.println("viajas hasta Ferrol para comprar antigüedades exóticas!\n");
                actual.remove_jugador(j);
                Casilla c = Monopoly.get().get_tablero().buscar_casilla("Ferrol");
                c.add_jugador(j);
            })
        ));
    }
}
