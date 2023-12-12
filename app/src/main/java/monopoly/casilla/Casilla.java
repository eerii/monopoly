package monopoly.casilla;

import java.util.Set;
import java.util.stream.Collectors;

import consola.Color;

import java.util.HashSet;
import java.util.List;

import monopoly.*;

public abstract class Casilla {
    // ···········
    // Propiedades
    // ···········

    private final String nombre;
    private Set<Jugador> jugadores = new HashSet<Jugador>();
    private float precio = 0.f;
    private int veces_visitada = 0;

    // ·············
    // Constructores
    // ·············

    public Casilla(String nombre) {
        this.nombre = nombre;
    }

    // ·········
    // Overrides
    // ·········

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Casilla c))
            return false;

        return c.nombre.equals(nombre);
    }

    // ·······
    // Getters
    // ·······

    public String get_nombre() {
        return nombre;
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

    // ·······
    // Setters
    // ·······

    public void set_precio(float precio) {
        this.precio = (float) Math.floor(precio);
    }

    // ················
    // Interfaz pública
    // ················

    public void add(Jugador jugador, boolean ignorar) {
        jugadores.add(jugador);
        veces_visitada++;
    }

    public void remove(Jugador jugador) {
        jugadores.remove(jugador);
    }

    public String representar() {
        String str_jugadores = new String("");
        for (Jugador j : jugadores) {
            str_jugadores += " " + j.representar();
        }

        return String.format("%s&%s", nombre, str_jugadores);
    }

    abstract public Color get_color();

    // ·················
    // Funciones propias
    // ·················

    protected String lista_jugadores() {
        List<String> js = jugadores.stream().map(Jugador::get_nombre).collect(Collectors.toList());
        String s = String.join(", ", js);
        return "[ " + s + " ]";
    }

    // TODO: To String en cada subclase de casilla
    /*
     * @Override
     * public String toString() {
     * 
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
     * 
     * }
     */
}
