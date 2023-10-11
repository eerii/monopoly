package monopoly;

import java.util.ArrayList;
import java.util.List;
import consola.Color;

import consola.Color;

public class Tablero {
    List<Casilla> casillas; // TODO: Cambiar a set cuando los nombres no se repitan
    List<Grupo> grupos;
    public Tablero() {
        grupos = new ArrayList<Grupo>();
        grupos.add(new Grupo(Color.AZUL));
        grupos.add(new Grupo(Color.AMARILLO));
        grupos.add(new Grupo(Color.CYAN));
        grupos.add(new Grupo(Color.BLANCO));
        grupos.add(new Grupo(Color.ROJO));
        grupos.add(new Grupo(Color.VERDE));
        grupos.add(new Grupo(Color.VERDE));
        grupos.add(new Grupo(Color.ROJO));

        casillas = new ArrayList<Casilla>();
        casillas.add(new Casilla(Casilla.TipoCasilla.SALIDA, "Salida"));
        casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, "Solar1",1000, grupos.get(0)));
        casillas.add(new Casilla(Casilla.TipoCasilla.COMUNIDAD, "Caja"));
        casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, "Solar2",1000, grupos.get(0)));
        casillas.add(new Casilla(Casilla.TipoCasilla.IMPUESTOS, "Imp1"));
        casillas.add(new Casilla(Casilla.TipoCasilla.TRANSPORTE, "Trans1"));
        casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, "Solar3",1000, grupos.get(1)));
        casillas.add(new Casilla(Casilla.TipoCasilla.SUERTE, "Suerte"));
        casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, "Solar4",1000, grupos.get(1)));
        casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, "Solar5",1000, grupos.get(1)));
        casillas.add(new Casilla(Casilla.TipoCasilla.CARCEL, "Cárcel"));
        casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, "Solar6",1000, grupos.get(2)));
        casillas.add(new Casilla(Casilla.TipoCasilla.SERVICIOS, "Serv1"));
        casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, "Solar7",1000, grupos.get(2)));
        casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, "Solar8",1000, grupos.get(2)));
        casillas.add(new Casilla(Casilla.TipoCasilla.TRANSPORTE, "Trans2"));
        casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, "Solar9",1000, grupos.get(3)));
        casillas.add(new Casilla(Casilla.TipoCasilla.COMUNIDAD, "Caja"));
        casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, "Solar10",1000, grupos.get(3)));
        casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, "Solar11",1000, grupos.get(3)));
        casillas.add(new Casilla(Casilla.TipoCasilla.PARKING, "Parking"));
        casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, "Solar12",1000, grupos.get(4)));
        casillas.add(new Casilla(Casilla.TipoCasilla.SUERTE, "Suerte"));
        casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, "Solar13",1000, grupos.get(4)));
        casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, "Solar14",1000, grupos.get(4)));
        casillas.add(new Casilla(Casilla.TipoCasilla.TRANSPORTE, "Trans3"));
        casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, "Solar15",1000, grupos.get(5)));
        casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, "Solar16",1000, grupos.get(5)));
        casillas.add(new Casilla(Casilla.TipoCasilla.SERVICIOS, "Serv2"));
        casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, "Solar17",1000, grupos.get(5)));
        casillas.add(new Casilla(Casilla.TipoCasilla.IR_A_CARCEL, "IrCarcel"));
        casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, "Solar18",1000, grupos.get(6)));
        casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, "Solar19",1000, grupos.get(6)));
        casillas.add(new Casilla(Casilla.TipoCasilla.COMUNIDAD, "Caja"));
        casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, "Solar20",1000, grupos.get(6)));
        casillas.add(new Casilla(Casilla.TipoCasilla.TRANSPORTE, "Trans4"));
        casillas.add(new Casilla(Casilla.TipoCasilla.SUERTE, "Suerte"));
        casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, "Solar21",1000, grupos.get(7)));
        casillas.add(new Casilla(Casilla.TipoCasilla.IMPUESTOS, "Imp2"));
        casillas.add(new Casilla(Casilla.TipoCasilla.SOLAR, "Solar22",1000, grupos.get(7)));



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
        fmt += String.format("%s%12s%s", Color.UNDERLINE, partes.length > 1 ? partes[1].replaceFirst(" ", "&") : "", Color.RESET);
        
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
