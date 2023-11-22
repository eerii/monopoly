package monopoly;

import java.util.ArrayList;
import java.util.List;
import consola.Color;

public class Grupo {
    Color color;
    String nombre;
    List<Casilla> casillas;

    public Grupo(Color color, String nombre) {
        this.color = color;
        this.nombre = nombre;
        this.casillas = new ArrayList<Casilla>();
    }

    public void add(Casilla casilla) {
        casillas.add(casilla);
    }

    public Color get_color() {
        return color;
    }

    public String get_nombre() {
        return nombre;
    }

    public List<Casilla> get_casillas() {
        return casillas;
    }
}
