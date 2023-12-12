package monopoly.casilla.propiedad;

import java.util.ArrayList;
import java.util.List;
import consola.Color;

public class Grupo {
    // ···········
    // Propiedades
    // ···········

    private final Color color;
    private final String nombre;
    private final List<Solar> casillas;

    // ·············
    // Constructores
    // ·············

    public Grupo(Color color, String nombre) {
        this.color = color;
        this.nombre = nombre;
        this.casillas = new ArrayList<>();
    }

    // ·······
    // Getters
    // ·······

    public Color get_color() {
        return color;
    }

    public String get_nombre() {
        return nombre;
    }

    public List<Solar> get_casillas() {
        return casillas;
    }

    // ················
    // Interfaz pública
    // ················

    public void add(Solar casilla) {
        casillas.add(casilla);
    }
}
