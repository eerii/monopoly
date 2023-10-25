package monopoly;

import consola.Color;

public class Solar extends CasillaComprable {
    Grupo grupo = null;

    public Solar(TipoCasilla tipo, String nombre, float precio, Grupo grupo) {
        super(tipo, nombre);

        this.precio = precio;
        if (grupo != null) {
            this.grupo = grupo;
            grupo.add(this);
        }
    }

    @Override
    public Color get_color() {
        return grupo != null ? grupo.get_color() : Color.NONE;
    }

    public Grupo get_grupo() {
        return grupo;
    }

    @Override
    public String toString() {
        String sn = String.format("%s%s%s%s", Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET);
        String st = String.format("%s%s%s%s", Color.VERDE, Color.BOLD, String.valueOf(tipo), Color.RESET);
        String sj = String.format("%s%s%s", Color.BOLD, lista_jugadores(), Color.RESET);

        Jugador jp = get_propietario();
        String sg = String.format("%s%s%s%s", String.valueOf(grupo.get_color()), Color.BOLD, grupo.get_nombre(), Color.RESET);
        String sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, precio, Color.RESET);
        String sa = String.format("%s%s%.0f%s", Color.ROJO, Color.BOLD, alquiler, Color.RESET);
        String sjp = jp != null ? String.format("%s%s%s%s", Color.AZUL_CLARITO, Color.BOLD, this.get_propietario().get_nombre(), Color.RESET) : ""; 
        
        return String.format("%s - tipo: %s - propietario: %s - grupo: %s - valor: %s - alquiler: %s - jugadores: %s", sn, st, sjp, sg, sp, sa, sj);
    }
}
