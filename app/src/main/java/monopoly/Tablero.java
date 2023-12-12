package monopoly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import consola.Color;
import monopoly.casilla.Casilla;
import monopoly.casilla.Especial;
import monopoly.casilla.Impuesto;
import monopoly.casilla.propiedad.*;
import monopoly.casilla.accion.*;

public class Tablero {
    // ···········
    // Propiedades
    // ···········

    private List<Casilla> casillas;
    private List<Grupo> grupos;

    // ·············
    // Constructores
    // ·············

    public Tablero(List<Grupo> grupos, List<Casilla> casillas) {
        this.grupos = grupos;
        this.casillas = casillas;
    }

    public Tablero() {
        grupos = new ArrayList<Grupo>(Arrays.asList(
                new Grupo(Color.MORADO, "morado"),
                new Grupo(Color.AZUL_CLARITO, "azul clarito"),
                new Grupo(Color.ROSA, "rosa"),
                new Grupo(Color.NARANJA, "naranja"),
                new Grupo(Color.ROJO, "rojo"),
                new Grupo(Color.AMARILLO, "amarillo"),
                new Grupo(Color.VERDE, "verde"),
                new Grupo(Color.AZUL_OSCURO, "azul oscuro")));

        casillas = new ArrayList<Casilla>(Arrays.asList(
                new Especial("Salida", Especial.Tipo.SALIDA),
                new Solar("Ferrol", 1200.f, grupos.get(0)),
                new Comunidad("Caja"),
                new Solar("Monforte", 1200.f, grupos.get(0)),
                new Impuesto("Matrícula"),
                new Transporte("Puerto ext."),
                new Solar("Viveiro", 1500.f, grupos.get(1)),
                new Suerte("Suerte"),
                new Solar("Tui", 1500.f, grupos.get(1)),
                new Solar("Porriño", 1500.f, grupos.get(1)),
                new Especial("Cárcel", Especial.Tipo.CARCEL),
                new Solar("Marin", 2100.f, grupos.get(2)),
                new Servicio("Iberdrola"),
                new Solar("O Grove", 2100.f, grupos.get(2)),
                new Solar("Cambre", 2100.f, grupos.get(2)),
                new Transporte("AP-9"),
                new Solar("Cangas", 2700.f, grupos.get(3)),
                new Comunidad("Caja"),
                new Solar("Ribeira", 2700.f, grupos.get(3)),
                new Solar("Redondela", 2700.f, grupos.get(3)),
                new Especial("Parking", Especial.Tipo.PARKING),
                new Solar("Culleredo", 3500.f, grupos.get(4)),
                new Suerte("Suerte"),
                new Solar("Carballo", 3500.f, grupos.get(4)),
                new Solar("Ames", 3500.f, grupos.get(4)),
                new Transporte("Urzaiz"),
                new Solar("Arteixo", 4500.f, grupos.get(5)),
                new Solar("Oleiros", 4500.f, grupos.get(5)),
                new Servicio("Fenosa"),
                new Solar("Narón", 4500.f, grupos.get(5)),
                new Especial("IrCarcel", Especial.Tipo.A_LA_CARCEL),
                new Solar("Pontevedra", 5800.f, grupos.get(6)),
                new Solar("Ourense", 5800.f, grupos.get(6)),
                new Comunidad("Caja"),
                new Solar("Santiago", 5800.f, grupos.get(6)),
                new Transporte("Lavacolla"),
                new Suerte("Suerte"),
                new Solar("A Coruña", 7500.f, grupos.get(7)),
                new Impuesto("Diezmo"),
                new Solar("Vigo", 7500.f, grupos.get(7))));

        float media = this.precio_medio();
        for (Casilla c : casillas) {
            if (c instanceof Transporte)
                c.set_precio(media);
            else if (c instanceof Servicio)
                c.set_precio(media * 0.75f);
            else if (c instanceof Especial && ((Especial) c).get_tipo() == Especial.Tipo.CARCEL)
                c.set_precio(media * 0.25f);
            else if (c instanceof Impuesto)
                c.set_precio(c.get_nombre().equals("Matrícula") ? media * 0.5f : media);
        }
    }

    // ·······
    // Getters
    // ·······

    public List<Casilla> get_casillas() {
        return casillas;
    }

    public List<Grupo> get_grupos() {
        return grupos;
    }

    public Grupo get_grupo(String nombre) {
        for (Grupo g : grupos) {
            if (g.get_nombre().equals(nombre))
                return g;
        }
        return null;
    }

    // ················
    // Interfaz pública
    // ················

    // Buscadores

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
            if (c.get_jugadores().contains(jugador))
                return c;
        }
        return null;
    }

    public float precio_medio() {
        List<Casilla> solares = casillas.stream()
                .filter(c -> c instanceof Solar)
                .collect(Collectors.toList());

        float media = 0.f;
        for (Casilla c : solares)
            media += c.get_precio();

        media /= solares.size();
        media = (float) Math.ceil(media / 10.f) * 10.f;

        return media;
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
        Casilla c = casillas.get(i);
        float precio = c.get_precio();

        if (c instanceof Solar) {
            Solar s = (Solar) c;

            if (s.get_propietario() != null) {
                String l = s.lista_edificios();
                int len = l.codePointCount(0, l.length());
                return String.format("%-12s%s", l, " ".repeat(l.length() - len));
            }
        }

        if (!(c instanceof Propiedad) || !((Propiedad) c).en_venta())
            return String.format("%12s", "");

        return String.format("%sM%-11.0f%s", c.get_color(), precio, Color.RESET);
    }

    String format_jugadores(int i) {
        String fmt = new String();
        Casilla c = casillas.get(i);

        String[] partes = c.representar().split("&");

        // Arreglar unicode
        String sj = partes.length > 1 ? partes[1].substring(1) : "";
        int len = sj.codePointCount(0, sj.length());

        fmt += String.format("%s%s%s%12s%s", Color.UNDERLINE, c.get_color(), " ".repeat(sj.length() - len), sj,
                Color.RESET);

        return fmt;
    }

    public String representar() {
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
        inf = "";
        mid = "";

        // Bordes laterales
        for (int i = 1; i < 10; i++) {
            final String espacio_interior = " ".repeat((12 + 1) * 9 - 1);
            final String barra_interior = String.format("%s%12s%s ", Color.UNDERLINE, " ", Color.RESET).repeat(9);
            String interior = i < 9 ? espacio_interior : barra_interior.substring(0, barra_interior.length() - 1);

            s += "|" + format_casilla(20 - i) + "|" + espacio_interior + "|" + format_casilla(30 + i) + "|" + "\n";
            s += "|" + format_propiedad(20 - i) + "|" + espacio_interior + "|" + format_propiedad(30 + i) + "|" + "\n";
            s += "|" + format_jugadores(20 - i) + "|" + interior + "|" + format_jugadores(30 + i) + "|" + "\n";
        }

        // Fila inferior
        for (int i = 10; i >= 0; i--) {
            s += "|" + format_casilla(i);
            mid += "|" + format_propiedad(i);
            inf += "|" + format_jugadores(i);
        }
        s += "|\n" + mid + "|\n" + inf + "|\n";
        inf = "";
        mid = "";

        return s;
    }
}
