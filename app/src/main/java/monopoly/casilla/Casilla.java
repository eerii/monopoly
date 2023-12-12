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

}
