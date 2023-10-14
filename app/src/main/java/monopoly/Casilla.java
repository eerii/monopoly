package monopoly;

import java.util.Set;
import java.util.stream.Collectors;

import consola.Color;

import java.util.HashSet;
import java.util.List;

// TODO: Mover funcionalidad a Solar cuando se pueda

public class Casilla {
    String nombre;
    TipoCasilla tipo;
    Set<Jugador> jugadores;

    // Propiedades de Solar
    float precio;
    Boolean en_venta;

    Boolean hipotecado;
    Grupo grupo = null;

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
    }

    public Casilla(TipoCasilla tipo, String nombre, int precio, Grupo grupo) {
        this(tipo, nombre);
        this.precio = precio;
        this.hipotecado = false;
        this.grupo = grupo;
        this.en_venta = true;
        grupo.add(this);
    }

    public void add_jugador(Jugador jugador) {
        add_jugador(jugador, false);
    }

    public void add_jugador(Jugador jugador, boolean ignorar) {
        jugadores.add(jugador);
        if (!ignorar) {
            Monopoly m = Monopoly.get();
            Tablero t = m.get_tablero();

            switch (tipo) {
                case A_LA_CARCEL: 
                    Casilla c = t.buscar_casilla("Cárcel");
                    jugador.ir_a_carcel();

                    this.remove_jugador(jugador);
                    c.add_jugador(jugador);
                    System.out.format("el jugador %s ha ido a la cárcel!\n", jugador.get_nombre());
                    
                    m.siguiente_turno();
                    break;
                case CARCEL:
                    if (!jugador.en_la_carcel())
                        System.out.println("no te preocupes, solo pasas de visita");
                    break;
                default:
                    System.out.println("no implementado: acción de casilla");
            }
        }
    }

    public void remove_jugador(Jugador jugador) {
        jugadores.remove(jugador);
    }

    public boolean get_en_venta() {
        return (tipo == TipoCasilla.SOLAR || tipo == TipoCasilla.TRANSPORTE || tipo == TipoCasilla.SERVICIOS) && en_venta;
    }

    public void comprar(Jugador jugador) {
        if (!en_venta)
            throw new RuntimeException(String.format("la casilla %s no está en venta", nombre));

        if (!jugadores.contains(jugador))
            throw new RuntimeException(String.format("el jugador %s no está en la casilla %s por lo que no puede comprarla", jugador.get_nombre(), nombre));
        
        if (jugador.get_fortuna() < precio)
            throw new RuntimeException(String.format("el jugador %s no puede permitirse comprar la casilla %s por %.0f", jugador.get_nombre(), nombre, precio));

        jugador.add_propiedad(this, precio);
        en_venta=false;
    }

    public String get_nombre() {
        return nombre;
    }

    public float get_precio() {
        return precio;
    }

    public TipoCasilla get_tipo() {
        return tipo;
    }

    public Color get_color() {
        return grupo != null ? grupo.get_color() : Color.BLANCO;
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
        List<String> js = jugadores.stream().map( Jugador::get_nombre ).collect( Collectors.toList() );
        String s = String.join(", ", js);
        return "[ " + s + " ]";
    }

    @Override
    public String toString() {
        Monopoly m = Monopoly.get();
        String sn = String.format("%s%s%s%s", Color.AZUL, Color.BOLD, nombre, Color.RESET);
        String st = String.format("%s%s%s%s", Color.VERDE, Color.BOLD, String.valueOf(tipo), Color.RESET);
        String sj = String.format("%s%s%s", Color.BOLD, lista_jugadores(), Color.RESET);
        String sp, sg;

        switch (tipo) {
            case SOLAR:
                sg = String.format("%s%s%s%s", String.valueOf(grupo.get_color()), Color.BOLD, grupo.get_nombre(), Color.RESET);
                sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, precio, Color.RESET);
                return String.format("%s - tipo: %s - grupo: %s - valor: %s - jugadores: %s", sn, st, sg, sp, sj);
            case TRANSPORTE:
            case SERVICIOS:
                sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, precio, Color.RESET);
                return String.format("%s - tipo: %s - valor: %s - jugadores: %s", sn, st, sp, sj);
            case IMPUESTOS:
                sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, precio, Color.RESET);
                return String.format("%s - tipo: %s - a pagar: %s - jugadores: %s", sn, st, sp, sj);
            case PARKING:
                sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, m.get_banca().get_fortuna(), Color.RESET);
                return String.format("%s - tipo: %s - bote: %s - jugadores: %s", sn, st, sp, sj);
            default:
                return String.format("%s - tipo: %s - jugadores: %s", sn, st, sj);
        }
    }
}
