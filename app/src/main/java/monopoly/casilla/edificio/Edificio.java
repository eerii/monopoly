package monopoly.casilla.edificio;

import monopoly.Monopoly;
import monopoly.casilla.propiedad.Solar;

public class Edificio {
    // ···········
    // Propiedades
    // ···········

    private Solar casilla;
    private String nombre;
    private String icono;

    // ·············
    // Constructores
    // ·············

    public Edificio(Solar casilla, String nombre, String icono) {
        this.casilla = casilla;
        this.nombre = nombre;
        this.icono = icono;
    }

    // ·······
    // Getters
    // ·······

    public Solar get_casilla() {
        return casilla;
    }

    public String get_icono() {
        return icono;
    }

    // ················
    // Interfaz pública
    // ················

    public float coste() {
        return (float) Math.floor(casilla.get_precio() * 0.2f);
    }

    public String representar() {
        Monopoly.Config c = Monopoly.get().get_config();
        if (c.get_iconos()) {
            return icono;
        }
        return nombre.substring(0, 1).toUpperCase();
    }
}
