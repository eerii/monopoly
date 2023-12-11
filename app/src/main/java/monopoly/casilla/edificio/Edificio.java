package monopoly.casilla.edificio;

import java.util.HashMap;

import monopoly.Monopoly;
import monopoly.casilla.Solar;

public class Edificio {
    private Solar casilla;
    private String nombre;
    private String icono;
    public String get_icono() {
            return icono;
        }
                

    public Edificio(Solar casilla, String nombre, String icono) {
        this.casilla = casilla;
        this.nombre = nombre;
        this.icono = icono;
    }

    public float coste() {
        return (float) Math.floor(casilla.get_precio() * 0.2f);}

    public Solar getCasilla() {
        return casilla;
    }

    public String representar() {
        Monopoly.Config c = Monopoly.get().get_config();
        if (c.get_iconos()) {
            return icono;
        }
        return nombre.substring(0, 1).toUpperCase();
    }
}
