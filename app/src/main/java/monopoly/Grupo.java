package monopoly;

import java.util.ArrayList;
import java.util.List;
import consola.Color;

public class Grupo{
    Color color;
    List<Casilla> casillas;

    public Grupo(Color color) {
        this.color = color;
        this.casillas = new ArrayList<Casilla>();
    }

    public void add(Casilla casilla) {
        casillas.add(casilla);
    }

    public Color get_color() {
        return color;
    }
}
