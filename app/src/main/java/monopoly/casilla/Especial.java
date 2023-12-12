package monopoly.casilla;

import estadisticas.EstadisticasJugador;
import monopoly.Jugador;
import monopoly.Monopoly;

public class Especial extends Casilla {
    public enum Tipo {
        SALIDA, A_LA_CARCEL, CARCEL, PARKING
    }

    Tipo tipo;

    public Especial(String nombre, Tipo tipo) {
        super(nombre);
        this.tipo = tipo;
    }

    public Tipo get_tipo() {
        return tipo;
    }

    @Override
    public void add_jugador(Jugador jugador, boolean ignorar) {
        super.add_jugador(jugador, ignorar);
        if (!ignorar) {
            Monopoly m = Monopoly.get();
            EstadisticasJugador s = m.get_stats().of(jugador);

            switch (tipo) {
                case SALIDA:
                    float media = m.get_tablero().precio_medio();
                    jugador.add_fortuna(media);
                    System.out.format("el jugador %s ha caído en la salida, recibe %.0f extra!\n", jugador.get_nombre(),
                            media);
                    s.sumar_pasar_salida(media);
                    break;
                case A_LA_CARCEL:
                    jugador.ir_a_carcel();
                    s.sumar_veces_carcel();
                    break;
                case CARCEL:
                    if (!jugador.en_la_carcel())
                        System.out.println("no te preocupes, solo pasas de visita");
                    break;
                case PARKING:
                    float bote = m.get_banca().get_fortuna();
                    jugador.add_fortuna(bote);
                    m.get_banca().add_fortuna(bote * -1.f);
                    System.out.format("el jugador %s ha caído en el parking, recibe %.0f extra del bote!\n",
                            jugador.get_nombre(), bote);
                    s.sumar_premios(bote);
                    break;
                default:
            }
        }
    }

}
