package monopoly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import consola.Color;

public class Tablero {
    List<Casilla> casillas; // TODO: Cambiar a set cuando los nombres no se repitan
    List<Grupo> grupos;

    public Tablero() {
        grupos = new ArrayList<Grupo>(Arrays.asList(
            new Grupo(Color.AZUL, "Azul"),
            new Grupo(Color.AMARILLO, "Amarillo"),
            new Grupo(Color.CYAN, "Cyan"),
            new Grupo(Color.VERDE, "Verde"),
            new Grupo(Color.ROJO, "Naranja"),
            new Grupo(Color.ALT_ROJO, "Rojo"),
            new Grupo(Color.ALT_VERDE, "Verde claro"),
            new Grupo(Color.ALT_AZUL, "Azul oscuro"),
            new Grupo(Color.NONE, "Transporte"),
            new Grupo(Color.NONE, "Servicios")
        ));

        casillas = new ArrayList<Casilla>(Arrays.asList(
            new Casilla(Casilla.TipoCasilla.SALIDA,     "Salida"),
            new Casilla(Casilla.TipoCasilla.SOLAR,      "Solar1",	60, 	grupos.get(0)),
            new Casilla(Casilla.TipoCasilla.COMUNIDAD,  "Caja"),
            new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar2",	60, 	grupos.get(0)),
            new Casilla(Casilla.TipoCasilla.IMPUESTOS,	"Imp1",     200,    null),
            new Casilla(Casilla.TipoCasilla.TRANSPORTE,	"Trans1",	200,	grupos.get(8)),
            new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar3",	100,	grupos.get(1)),
            new Casilla(Casilla.TipoCasilla.SUERTE,		"Suerte"),
            new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar4",	100,	grupos.get(1)),
            new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar5",	120,	grupos.get(1)),
            new Casilla(Casilla.TipoCasilla.CARCEL,		"Cárcel",   300,    null),
            new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar6",	140,	grupos.get(2)),
            new Casilla(Casilla.TipoCasilla.SERVICIOS,	"Serv1",	150,	grupos.get(9)),
            new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar7",	140,	grupos.get(2)),
            new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar8",	160,	grupos.get(2)),
            new Casilla(Casilla.TipoCasilla.TRANSPORTE,	"Trans2",	200,	grupos.get(8)),
            new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar9",	180,	grupos.get(3)),
            new Casilla(Casilla.TipoCasilla.COMUNIDAD,	"Caja"),
            new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar10",	180,	grupos.get(3)),
            new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar11",	200,	grupos.get(3)),
            new Casilla(Casilla.TipoCasilla.PARKING,	"Parking"),
            new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar12",	220,	grupos.get(4)),
            new Casilla(Casilla.TipoCasilla.SUERTE,		"Suerte"),
            new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar13",	220,	grupos.get(4)),
            new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar14",	240,	grupos.get(4)),
            new Casilla(Casilla.TipoCasilla.TRANSPORTE,	"Trans3",	200,	grupos.get(8)),
            new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar15",	260,	grupos.get(5)),
            new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar16",	260,	grupos.get(5)),
            new Casilla(Casilla.TipoCasilla.SERVICIOS,	"Serv2",	150,	grupos.get(9)),
            new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar17",	280,	grupos.get(5)),
            new Casilla(Casilla.TipoCasilla.A_LA_CARCEL,"IrCarcel"),
            new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar18",	300,	grupos.get(6)),
            new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar19",	300,	grupos.get(6)),
            new Casilla(Casilla.TipoCasilla.COMUNIDAD,	"Caja"),
            new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar20",	100,	grupos.get(6)),
            new Casilla(Casilla.TipoCasilla.TRANSPORTE,	"Trans4",	320,	grupos.get(8)),
            new Casilla(Casilla.TipoCasilla.SUERTE,		"Suerte"),
            new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar21",	350,	grupos.get(7)),
            new Casilla(Casilla.TipoCasilla.IMPUESTOS,	"Imp2",     200,    null),
            new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar22",	400,	grupos.get(7))
        )); 
    }

    public List<Casilla> get_casillas() {
        return casillas;
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
        Color bg_col = c.get_color().to_bg();
        String bg = bg_col == null ? "" : bg_col.toString();
        String fg = bg_col == null ? "" : Color.NEGRO.toString();

        fmt += String.format("%s%s%s%-12s%s", Color.BOLD, fg, bg, partes[0], Color.RESET);
        
        return fmt;
    }

    String format_propiedad(int i) {
        String fmt = new String();
        Casilla c = casillas.get(i);

        float precio = c.get_precio();
        if (precio == 0.f || !c.get_en_venta())
            return String.format("%12s", "");

        fmt += String.format("%sM%-11.0f%s", c.get_color(), precio, Color.RESET);
        
        return fmt;
    }

    String format_jugadores(int i) {
        String fmt = new String();
        Casilla c = casillas.get(i);

        String[] partes = c.representar().split("&");

        // Arreglar unicode
        String sj = partes.length > 1 ? partes[1].substring(1) : "";
        int len = sj.codePointCount(0, sj.length());

        fmt += String.format("%s%s%s%12s%s", Color.UNDERLINE, c.get_color(), " ".repeat(sj.length() - len), sj, Color.RESET);
        
        return fmt;
    }

    @Override
    public String toString() {
        // TODO: Cambiar los string formats de espacio en blanco por una constante
        String s = new String();
        String mid = new String();
        String inf = new String();

        // Fila superior
        s += " " + String.format("%s%12s%s ", Color.UNDERLINE, " ", Color.RESET).repeat(11) + "\n";
        for (int i = 20; i <= 30; i++) {
            s += "|" + format_casilla(i);
            mid += "|" + format_propiedad(i);
            inf += "|" + format_jugadores(i);
        }
        s += "|\n" + mid + "|\n" + inf + "|\n";
        inf = ""; mid = "";

        // Bordes laterales
        for (int i = 1; i < 10; i++) {
            final String espacio_interior = " ".repeat((12 + 1) * 9 - 1);
            final String barra_interior = String.format("%s%12s%s ", Color.UNDERLINE, " ", Color.RESET).repeat(9);
            String interior = i < 9 ? espacio_interior : barra_interior.substring(0, barra_interior.length() - 1);

            s += "|" + format_casilla(20-i) + "|" + espacio_interior + "|" + format_casilla(30+i) + "|" + "\n";
            s += "|" + format_propiedad(20-i) + "|" + espacio_interior + "|" + format_propiedad(30+i) + "|" + "\n";
            s += "|" + format_jugadores(20-i) + "|" + interior + "|" + format_jugadores(30+i) + "|" + "\n";
        }

        // Fila inferior
        for (int i = 10; i >= 0; i--) {
            s += "|" + format_casilla(i);
            mid += "|" + format_propiedad(i);
            inf += "|" + format_jugadores(i);
        }
        s += "|\n" + mid + "|\n" + inf + "|\n";
        inf = ""; mid = "";

        return s;
    }
}
