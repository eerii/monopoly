package monopoly.casilla;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.List;

import consola.Color;
import monopoly.*;

public abstract class Casilla {
    private final String nombre;
    private Set<Jugador> jugadores;
    private float precio;
    private int veces_visitada = 0;

    public Casilla(String nombre) {
        this.nombre = nombre;
        this.jugadores = new HashSet<Jugador>();
        this.precio = 0.f;
    }

    public void add_jugador(Jugador jugador, boolean ignorar) {
        jugadores.add(jugador);
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

    public String lista_jugadores() {
        List<String> js = jugadores.stream().map(Jugador::get_nombre).collect(Collectors.toList());
        String s = String.join(", ", js);
        return "[ " + s + " ]";
    }

    @Override
    public String toString() {
        // TODO: To String en cada subclase de casilla
        /*
         * String sn = String.format("%s%s%s%s", Color.AZUL_CLARITO, Color.BOLD, nombre,
         * Color.RESET);
         * String st = String.format("%s%s%s%s", Color.VERDE, Color.BOLD,
         * String.valueOf(tipo), Color.RESET);
         * String sj = String.format("%s%s%s", Color.BOLD, lista_jugadores(),
         * Color.RESET);
         * String sp;
         * 
         * switch (tipo) {
         * case IMPUESTOS:
         * sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, precio,
         * Color.RESET);
         * return String.format("%s - tipo: %s - a pagar: %s - jugadores: %s", sn, st,
         * sp, sj);
         * case PARKING:
         * sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD,
         * Monopoly.get().get_banca().get_fortuna(),
         * Color.RESET);
         * return String.format("%s - tipo: %s - bote: %s - jugadores: %s", sn, st, sp,
         * sj);
         * case CARCEL:
         * sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, precio,
         * Color.RESET);
         * return String.format("%s - tipo: %s - salir: %s - jugadores: %s", sn, st, sp,
         * sj);
         * default:
         * return String.format("%s - tipo: %s - jugadores: %s", sn, st, sj);
         * }
         */
        return "TODO";
    }

    public String toStringMini() {
        return String.format("%s", nombre);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Casilla c))
            return false;

        return c.nombre.equals(nombre);
    }
}
