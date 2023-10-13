package monopoly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import consola.Color;

public class Tablero {
    List<Casilla> casillas; // TODO: Cambiar a set cuando los nombres no se repitan
    List<Grupo> grupos;

    float bote;

    public Tablero() {

        Jugador banca = new Jugador();
        bote = 0;
        grupos = new ArrayList<Grupo>(Arrays.asList(
            new Grupo(Color.AZUL, "Azul"),
            new Grupo(Color.AMARILLO, "Amarillo"),
            new Grupo(Color.CYAN, "Cyan"),
            new Grupo(Color.VERDE, "Verde"),
            new Grupo(Color.ROJO, "Rojo"),
            new Grupo(Color.ALT_ROJO, "Naranja"),
            new Grupo(Color.ALT_VERDE, "Verde claro"),
            new Grupo(Color.ALT_AZUL, "Azul oscuro"),
            new Grupo(Color.BOLD,""),
            new Grupo(Color.BOLD, "")
        ));

        casillas = new ArrayList<Casilla>(Arrays.asList(
            new Casilla(Casilla.TipoCasilla.SALIDA, "Salida"),
            new Casilla(Casilla.TipoCasilla.SOLAR, "Solar1",1000, grupos.get(0),true,banca),
            new Casilla(Casilla.TipoCasilla.COMUNIDAD, "Caja"),
            new Casilla(Casilla.TipoCasilla.SOLAR, "Solar2",1000, grupos.get(0),true,banca),
            new Casilla(Casilla.TipoCasilla.IMPUESTOS, "Imp1"),
            new Casilla(Casilla.TipoCasilla.TRANSPORTE, "Trans1",1000, grupos.get(8), true,banca),
            new Casilla(Casilla.TipoCasilla.SOLAR, "Solar3",1000, grupos.get(1),true,banca),
            new Casilla(Casilla.TipoCasilla.SUERTE, "Suerte"),
            new Casilla(Casilla.TipoCasilla.SOLAR, "Solar4",1000, grupos.get(1),true,banca),
            new Casilla(Casilla.TipoCasilla.SOLAR, "Solar5",1000, grupos.get(1),true,banca),
            new Casilla(Casilla.TipoCasilla.CARCEL, "Cárcel"),
            new Casilla(Casilla.TipoCasilla.SOLAR, "Solar6",1000, grupos.get(2),true,banca),
            new Casilla(Casilla.TipoCasilla.SERVICIOS, "Serv1", 1000, grupos.get(9),true,banca),
            new Casilla(Casilla.TipoCasilla.SOLAR, "Solar7",1000, grupos.get(2),true,banca),
            new Casilla(Casilla.TipoCasilla.SOLAR, "Solar8",1000, grupos.get(2),true,banca),
            new Casilla(Casilla.TipoCasilla.TRANSPORTE, "Trans2",1000, grupos.get(8), true,banca),
            new Casilla(Casilla.TipoCasilla.SOLAR, "Solar9",1000, grupos.get(3),true,banca),
            new Casilla(Casilla.TipoCasilla.COMUNIDAD, "Caja"),
            new Casilla(Casilla.TipoCasilla.SOLAR, "Solar10",1000, grupos.get(3),true,banca),
            new Casilla(Casilla.TipoCasilla.SOLAR, "Solar11",1000, grupos.get(3),true,banca),
            new Casilla(Casilla.TipoCasilla.PARKING, "Parking"),
            new Casilla(Casilla.TipoCasilla.SOLAR, "Solar12",1000, grupos.get(4),true,banca),
            new Casilla(Casilla.TipoCasilla.SUERTE, "Suerte"),
            new Casilla(Casilla.TipoCasilla.SOLAR, "Solar13",1000, grupos.get(4),true,banca),
            new Casilla(Casilla.TipoCasilla.SOLAR, "Solar14",1000, grupos.get(4),true,banca),
            new Casilla(Casilla.TipoCasilla.TRANSPORTE, "Trans3",1000, grupos.get(8), true,banca),
            new Casilla(Casilla.TipoCasilla.SOLAR, "Solar15",1000, grupos.get(5),true,banca),
            new Casilla(Casilla.TipoCasilla.SOLAR, "Solar16",1000, grupos.get(5),true,banca),
            new Casilla(Casilla.TipoCasilla.SERVICIOS, "Serv2",1000, grupos.get(9), true,banca),
            new Casilla(Casilla.TipoCasilla.SOLAR, "Solar17",1000, grupos.get(5),true,banca),
            new Casilla(Casilla.TipoCasilla.IR_A_CARCEL, "IrCarcel"),
            new Casilla(Casilla.TipoCasilla.SOLAR, "Solar18",1000, grupos.get(6),true,banca),
            new Casilla(Casilla.TipoCasilla.SOLAR, "Solar19",1000, grupos.get(6),true,banca),
            new Casilla(Casilla.TipoCasilla.COMUNIDAD, "Caja"),
            new Casilla(Casilla.TipoCasilla.SOLAR, "Solar20",1000, grupos.get(6),true,banca),
            new Casilla(Casilla.TipoCasilla.TRANSPORTE, "Trans4", 1000,grupos.get(8),true,banca),
            new Casilla(Casilla.TipoCasilla.SUERTE, "Suerte"),
            new Casilla(Casilla.TipoCasilla.SOLAR, "Solar21",1000, grupos.get(7),true,banca),
            new Casilla(Casilla.TipoCasilla.IMPUESTOS, "Imp2"),
            new Casilla(Casilla.TipoCasilla.SOLAR, "Solar22",1000, grupos.get(7),true,banca)
        ));
    }

    public List<Casilla> get_casillas() {
        return casillas;
    }

    public float get_bote() {
        return bote;
    }

    public Casilla buscar_casilla(String nombre) {
        for (Casilla c : casillas) {
            if (c.get_nombre().equals(nombre)) {
                return c;
            }
        }
        return null;
    }

    public Casilla buscar_jugador(Jugador jugador) {
        for (Casilla c : casillas) {
            if (c.jugadores.contains(jugador))
                return c;
        }
        return null;
    }

    // String
    String format_casilla(int i) {
        String fmt = new String();
        Casilla c = casillas.get(i);

        String[] partes = c.representar().split("&");
        fmt += String.format("%s%-12s%s", c.get_color(), partes[0], Color.RESET);
        
        return fmt;
    }

    String format_jugadores(int i) {
        String fmt = new String();
        Casilla c = casillas.get(i);

        String[] partes = c.representar().split("&");
        fmt += String.format("%s%s%12s%s", Color.UNDERLINE, c.get_color(), partes.length > 1 ? partes[1].replaceFirst(" ", "&") : "", Color.RESET);
        
        return fmt;
    }

    @Override
    public String toString() {
        // TODO: Cambiar los string formats de espacio en blanco por una constante
        String s = new String();
        String inf = new String();

        // Fila superior
        s += " " + String.format("%s%12s%s ", Color.UNDERLINE, " ", Color.RESET).repeat(11) + "\n";
        for (int i = 20; i <= 30; i++) {
            s += "|" + format_casilla(i);
            inf += "|" + format_jugadores(i);
        }
        s += "|\n|" + String.format("%12s|", "").repeat(11) + "\n" + inf + "|\n";
        inf = "";

        // Bordes laterales
        for (int i = 1; i < 10; i++) {
            final String espacio_interior = " ".repeat((12 + 1) * 9 - 1);
            final String barra_interior = String.format("%s%12s%s ", Color.UNDERLINE, " ", Color.RESET).repeat(9);
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
