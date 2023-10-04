package monopoly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tablero {
    List<Casilla> casillas; // TODO: Cambiar a set cuando los nombres no se repitan

    public Tablero() {
        casillas = new ArrayList<Casilla>();

        Random r = new Random();
        for (int i = 0; i < 40; i++) {
            casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, String.valueOf(i)));
        }
    }

    public Casilla buscar_casilla(String nombre) {
        for (Casilla c : casillas) {
            if (c.get_nombre().equals(nombre)) {
                return c;
            }
        }
        return null;
    }

    // String
    String format_casilla(int i) {
        String fmt = new String();

        String[] partes = casillas.get(i).toString().split("&");
        fmt += String.format("%-12s", partes[0]);
        
        return fmt;
    }

    String format_jugadores(int i) {
        String fmt = new String();

        String[] partes = casillas.get(i).toString().split("&");
        fmt += String.format("\u001b[4m%12s\u001b[0m", partes.length > 1 ? partes[1].replaceFirst(" ", "&") : "");
        
        return fmt;
    }

    @Override
    public String toString() {
        // TODO: Cambiar los string formats de espacio en blanco por una constante
        String s = new String();
        String inf = new String();

        // Fila superior
        s += " " + String.format("\u001b[4m%12s\u001b[0m ", " ").repeat(11) + "\n";
        for (int i = 20; i <= 30; i++) {
            s += "|" + format_casilla(i);
            inf += "|" + format_jugadores(i);
        }
        s += "|\n|" + String.format("%12s|", "").repeat(11) + "\n" + inf + "|\n";
        inf = "";

        // Bordes laterales
        for (int i = 1; i < 10; i++) {
            final String espacio_interior = " ".repeat((12 + 1) * 9 - 1);
            final String barra_interior = String.format("\u001b[4m%12s\u001b[0m ", " ").repeat(9);
            String interior = i < 9 ? espacio_interior : barra_interior.substring(0, barra_interior.length() - 1);

            s += "|" + format_casilla(20-i) + "|" + espacio_interior + "|" + format_casilla(30+i) + "|" + "\n";
            s += "|" + String.format("%12s|", "") + espacio_interior + "|" + String.format("%12s|", "")+ "\n";
            s += "|" + format_jugadores(20-i) + "|" + interior + "|" + format_jugadores(30+i) + "|" + "\n";
        }

        // Fila inferior
        for (int i = 10; i >= 0; i--) {
            s += "|" + format_casilla(i);
            inf += "|" + format_jugadores(i);
        }
        s += "|\n|" + String.format("%12s|", "").repeat(11) + "\n" + inf + "|\n";

        return s;
    }
}
