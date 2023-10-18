package monopoly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import consola.Color;

public class Tablero {
    List<Casilla> casillas;
    List<Grupo> grupos;

    public Tablero(List<Grupo> grupos, List<Casilla> casillas) {
        this.grupos = grupos;
        this.casillas = casillas;
    }

    public Tablero() {
        grupos = new ArrayList<Grupo>(Arrays.asList(
            new Grupo(Color.AZUL, "Azul"),
            new Grupo(Color.AMARILLO, "Amarillo"),
            new Grupo(Color.CYAN, "Cyan"),
            new Grupo(Color.VERDE, "Verde"),
            new Grupo(Color.ROJO, "Naranja"),
            new Grupo(Color.ALT_ROJO, "Rojo"),
            new Grupo(Color.ALT_VERDE, "Verde claro"),
            new Grupo(Color.ALT_AZUL, "Azul oscuro")
        ));

        casillas = new ArrayList<Casilla>(Arrays.asList(
                new Casilla(Casilla.TipoCasilla.SALIDA,     "Salida"),
                new Casilla(Casilla.TipoCasilla.SOLAR,      "Solar1",	1200, 	grupos.get(0)),
                new Casilla(Casilla.TipoCasilla.COMUNIDAD,  "Caja"),
                new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar2",	1200, 	grupos.get(0)),
                new Casilla(Casilla.TipoCasilla.IMPUESTOS,	"Imp1",     0,    null),
                new Casilla(Casilla.TipoCasilla.TRANSPORTE,	"Trans1",	0,	null),
                new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar3",	1500,	grupos.get(1)),
                new Casilla(Casilla.TipoCasilla.SUERTE,		"Suerte"),
                new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar4",	1500,	grupos.get(1)),
                new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar5",	1500,	grupos.get(1)),
                new Casilla(Casilla.TipoCasilla.CARCEL,		"Cárcel",   0,    null),
                new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar6",	2100,	grupos.get(2)),
                new Casilla(Casilla.TipoCasilla.SERVICIOS,	"Serv1",	0,	null),
                new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar7",	2100,	grupos.get(2)),
                new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar8",	2100,	grupos.get(2)),
                new Casilla(Casilla.TipoCasilla.TRANSPORTE,	"Trans2",	0,	null),
                new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar9",	2700,	grupos.get(3)),
                new Casilla(Casilla.TipoCasilla.COMUNIDAD,	"Caja"),
                new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar10",	2700,	grupos.get(3)),
                new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar11",	2700,	grupos.get(3)),
                new Casilla(Casilla.TipoCasilla.PARKING,	"Parking"),
                new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar12",	3500,	grupos.get(4)),
                new Casilla(Casilla.TipoCasilla.SUERTE,		"Suerte"),
                new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar13",	3500,	grupos.get(4)),
                new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar14",	3500,	grupos.get(4)),
                new Casilla(Casilla.TipoCasilla.TRANSPORTE,	"Trans3",	0,	null),
                new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar15",	4500,	grupos.get(5)),
                new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar16",	4500,	grupos.get(5)),
                new Casilla(Casilla.TipoCasilla.SERVICIOS,	"Serv2",	0,	null),
                new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar17",	4500,	grupos.get(5)),
                new Casilla(Casilla.TipoCasilla.A_LA_CARCEL,"IrCarcel"),
                new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar18",	5800,	grupos.get(6)),
                new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar19",	5800,	grupos.get(6)),
                new Casilla(Casilla.TipoCasilla.COMUNIDAD,	"Caja"),
                new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar20",	5800,	grupos.get(6)),
                new Casilla(Casilla.TipoCasilla.TRANSPORTE,	"Trans4",	0,	null),
                new Casilla(Casilla.TipoCasilla.SUERTE,		"Suerte"),
                new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar21",	7500,	grupos.get(7)),
                new Casilla(Casilla.TipoCasilla.IMPUESTOS,	"Imp2",     0,    null),
                new Casilla(Casilla.TipoCasilla.SOLAR,		"Solar22",	7500,	grupos.get(7))
                ));

        float media =  0;
        int i=0;
        for (Casilla c: casillas)
        {
            if(c.get_tipo()== Casilla.TipoCasilla.SOLAR)
            {
                media += c.get_precio();
                i+=1;
            }

        }
        media /= i;
        media = (float)Math.ceil(media / 10.f) * 10.f;
        for(Casilla c: casillas)
        {
            if(c.get_tipo()== Casilla.TipoCasilla.TRANSPORTE)
            {
                c.set_precio(media);
                c.set_alquiler(media);
            }
            if(c.get_tipo()== Casilla.TipoCasilla.SERVICIOS)
            {
                c.set_precio((float) Math.floor(0.75f * media));
                c.set_alquiler(0.005f * media);
            }
            if(c.get_tipo()== Casilla.TipoCasilla.CARCEL)
            {
                c.set_precio((float) Math.floor(0.25f * media));
            }
            if(c.get_tipo()== Casilla.TipoCasilla.IMPUESTOS)
            {
                if(c.get_nombre().equals("Imp1"))
                    c.set_precio(media);
                else if(c.get_nombre().equals("Imp2"))
                    c.set_precio((float) Math.floor (media * 0.5f));
            }
        }

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
