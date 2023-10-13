package monopoly;

import java.util.Set;

import consola.Color;

import java.util.HashSet;

// TODO: Mover funcionalidad a Solar cuando se pueda

public class Casilla {
    String nombre;
    TipoCasilla tipo;
    Set<Jugador> jugadores;

    // Propiedades de Solar
    float precio;
    Boolean enVenta;

    Jugador propietario;
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
        IR_A_CARCEL,
        PARKING,
        SALIDA,
        NULL;
    }


    public Casilla(TipoCasilla tipo, String nombre) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.jugadores = new HashSet<Jugador>();
        this.propietario = null;
    }

    public Casilla(TipoCasilla tipo, String nombre, int precio, Grupo grupo, boolean enVenta, Jugador propietario) {
        this(tipo, nombre);
        this.precio = precio;
        this.hipotecado = false;
        this.grupo = grupo;
        this.enVenta = enVenta;
        this.propietario = propietario;
        grupo.add(this);
    }

    public void add_jugador(Jugador jugador) {
        jugadores.add(jugador);
        System.out.println("no implementado: acción de casilla");
    }

    public boolean enVenta () {
        if(tipo == TipoCasilla.SOLAR || tipo == TipoCasilla.TRANSPORTE || tipo == TipoCasilla.SERVICIOS)
        {
            return enVenta;
        }
        return false;
    }

    public int comprar(Jugador jugador) {

        if(jugador.get_fortuna() >= precio) {
            jugador.add_propiedades(this, precio);
            enVenta=false;
            propietario=jugador;
            return 0;
        }
        return -1;
    }

    public void remove_jugador(Jugador jugador) {
        jugadores.remove(jugador);
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

    @Override
    public String toString() {

        if(tipo==TipoCasilla.SOLAR)
        {
            return String.format(
                    "tipo: %s%s%s%s - grupo: %s%s%s - precio: %s%.0f%s",
                    Color.AZUL, Color.BOLD, String.valueOf(tipo), Color.RESET,
                    Color.BOLD, String.valueOf(grupo.get_color()), Color.RESET,
                    Color.BOLD, precio, Color.RESET
            );
        }

        return String.format(
                "tipo: %s%s%s%s - precio: %s%.0f%s",
                Color.AZUL, Color.BOLD, String.valueOf(tipo), Color.RESET,
                Color.BOLD, precio, Color.RESET
                );
    }

    public String describir()
    {
        if(tipo==TipoCasilla.SOLAR)
        {
            return String.format(
                    "tipo: %s%s%s%s - grupo: %s%s%s%s - propietario: %s%s%s - valor: %s%.0f%s",
                    Color.AZUL, Color.BOLD, String.valueOf(tipo), Color.RESET,
                    grupo.get_color(), Color.BOLD, String.valueOf(grupo.get_nombre()), Color.RESET,
                    Color.BOLD, propietario.get_nombre(), Color.RESET,
                    Color.BOLD, precio, Color.RESET
            );
        }

        if(tipo==TipoCasilla.TRANSPORTE || tipo==TipoCasilla.SERVICIOS)
        {
            return String.format(
                    "tipo: %s%s%s%s - propietario: %s%s%s - valor: %s%.0f%s",
                    Color.AZUL, Color.BOLD, String.valueOf(tipo), Color.RESET,
                    Color.BOLD, propietario.get_nombre(), Color.RESET,
                    Color.BOLD, precio, Color.RESET
            );
        }

        if(tipo==TipoCasilla.IMPUESTOS)
        {
            return String.format(
                    "tipo: %s%s%s%s - a pagar: %s%.0f%s",
                    Color.AZUL, Color.BOLD, String.valueOf(tipo), Color.RESET,
                    Color.BOLD, precio, Color.RESET
            );
        }

        if(tipo==TipoCasilla.PARKING)
        {
            float bote = Monopoly.get().get_tablero().get_bote();
            return String.format(
                    "bote: %s%s%f%s - jugadores: %s%s%s",
                    Color.AZUL, Color.BOLD, bote, Color.RESET,
                    Color.BOLD, jugadores, Color.RESET
            );
        }

        if(tipo==TipoCasilla.CARCEL)
        {

            return String.format(
                    "salir: %s%s%f%s - jugadores: %s%s%s",
                    Color.AZUL, Color.BOLD, precio, Color.RESET,
                    Color.BOLD, jugadores, Color.RESET
            );
        }
        return String.format("La casilla no se puede describir");
    }
}
