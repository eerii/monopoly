package monopoly.casilla;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.List;

import consola.Color;
import estadisticas.EstadisticasJugador;

import monopoly.*;

public class Casilla {
    String nombre;
    TipoCasilla tipo;
    Set<Jugador> jugadores;
    float precio;
    int veces_visitada = 0;

    // TODO: Jerarquía de casillas
    // Propiedad, Accion, Impuesto, Grupo, Especial
    // Propiedad: Solar, Transporte, Servicios

    public enum TipoCasilla {
        SOLAR,
        TRANSPORTE,
        SERVICIOS,
        SUERTE,
        COMUNIDAD,
        IMPUESTOS,
        CARCEL,
        A_LA_CARCEL,
        PARKING,
        SALIDA,
        NULL;
    }

    public Casilla(TipoCasilla tipo, String nombre) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.jugadores = new HashSet<Jugador>();
        this.precio = 0.f;
    }

    public void add_jugador(Jugador jugador) {
        add_jugador(jugador, false);
    }

    public void add_jugador(Jugador jugador, boolean ignorar) {
        jugadores.add(jugador);
        if (!ignorar) {
            Monopoly m = Monopoly.get();
            EstadisticasJugador s = m.get_stats().of(jugador);

            switch (tipo) {
                case SALIDA:
                    float media = m.get_tablero().precio_medio();
                    jugador.add_fortuna(media);
                    System.out.format("el jugador %s ha caído en la salida, recibe %.0f extra!\n", jugador.get_nombre(),
                            media);
                    s.summar_pasar_salida(media);
                    break;
                case IMPUESTOS:
                    jugador.add_fortuna(precio * -1.f);
                    m.get_banca().add_fortuna(precio);
                    System.out.format("el jugador %s ha caído la casilla de impuestos, paga %.0f a la banca!\n",
                            jugador.get_nombre(), precio);
                    s.sumar_pago_tasa_impuestos(precio);
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
                case SUERTE:
                case COMUNIDAD:
                    List<Carta> baraja = m
                            .barajar(tipo == TipoCasilla.SUERTE ? Carta.TipoCarta.SUERTE : Carta.TipoCarta.COMUNIDAD);
                    Carta c = m.sacar_carta(baraja);
                    System.out.format("el jugador %s ha caído en la casilla de %s, saca la carta: %s\n",
                            jugador.get_nombre(), tipo, c);
                    c.ejecutar(jugador);
                    break;
                default:
            }

            if (this instanceof Propiedad) {
                Propiedad p = (Propiedad) this;
                if (p.en_venta()) {
                    if (jugador.get_fortuna() < precio || !jugador.puede_comprar())
                        return;

                    System.out.format("¿Quieres comprar %s por %.0f? (s/N)\n", nombre, precio);
                    String respuesta = m.get_consola().get_raw().trim();
                    if (respuesta.equalsIgnoreCase("s")) {
                        p.comprar(jugador);
                        System.out.format(
                                "el jugador %s%s%s%s compra la casilla %s%s%s%s por %s%s%.0f%s. Su fortuna actual es de %s%s%.0f%s\n",
                                Color.ROJO, Color.BOLD, jugador.get_nombre(), Color.RESET,
                                Color.AZUL_OSCURO, Color.BOLD, nombre, Color.RESET,
                                Color.AMARILLO, Color.BOLD, precio, Color.RESET,
                                Color.ROSA, Color.BOLD, jugador.get_fortuna(), Color.RESET);
                        return;
                    }
                    return;
                }

                Jugador propietario = p.get_propietario();
                if (propietario != null && propietario != jugador) {
                    if (p instanceof Solar)
                        jugador.paga_alquiler(propietario, (Solar) p);
                    else
                        jugador.paga_servicio_transporte(propietario, p);
                }
            }
        }
    }

    public void remove_jugador(Jugador jugador) {
        jugadores.remove(jugador);
    }

    public String get_nombre() {
        return nombre;
    }

    public void set_precio(float precio) {
        this.precio = (float) Math.floor(precio);
    }

    public float get_precio() {
        return precio;
    }

    public TipoCasilla get_tipo() {
        return tipo;
    }

    public Set<Jugador> get_jugadores() {
        return jugadores;
    }

    public int get_veces_visitada() {
        return veces_visitada;
    }

    public void sumar_veces_visitada() {
        veces_visitada += 1;
    }

    public Color get_color() {
        return Color.NONE;
    }

    // String
    public String representar() {
        String str_jugadores = new String("");
        for (Jugador j : jugadores) {
            str_jugadores += " " + j.representar();
        }

        return String.format("%s&%s", nombre, str_jugadores);
    }

    String lista_jugadores() {
        List<String> js = jugadores.stream().map(Jugador::get_nombre).collect(Collectors.toList());
        String s = String.join(", ", js);
        return "[ " + s + " ]";
    }

    @Override
    public String toString() {
        String sn = String.format("%s%s%s%s", Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET);
        String st = String.format("%s%s%s%s", Color.VERDE, Color.BOLD, String.valueOf(tipo), Color.RESET);
        String sj = String.format("%s%s%s", Color.BOLD, lista_jugadores(), Color.RESET);
        String sp;

        switch (tipo) {
            case IMPUESTOS:
                sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, precio, Color.RESET);
                return String.format("%s - tipo: %s - a pagar: %s - jugadores: %s", sn, st, sp, sj);
            case PARKING:
                sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, Monopoly.get().get_banca().get_fortuna(),
                        Color.RESET);
                return String.format("%s - tipo: %s - bote: %s - jugadores: %s", sn, st, sp, sj);
            case CARCEL:
                sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, precio, Color.RESET);
                return String.format("%s - tipo: %s - salir: %s - jugadores: %s", sn, st, sp, sj);
            default:
                return String.format("%s - tipo: %s - jugadores: %s", sn, st, sj);
        }
    }

    public String toStringMini() {
        return String.format("%s", nombre);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Casilla))
            return false;

        Casilla c = (Casilla) o;
        return c.nombre.equals(nombre);
    }
}
