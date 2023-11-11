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
        // TODO: Rellenar baraja y acciones de carta
        baraja = new ArrayList<Carta>(Arrays.asList(
            new Carta("Prueba", Carta.TipoCarta.COMUNIDAD, j -> System.out.println("hola")),
            new Carta("Hey", Carta.TipoCarta.COMUNIDAD, j -> System.out.println("adios"))
        ));
    }
}
