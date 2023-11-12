package monopoly;

import java.util.HashMap;
import java.util.List;

public class Edificio {
    TipoEdificio tipo;

    public enum TipoEdificio {
        CASA("casa"),
        HOTEL("hotel"),
        TERMAS("termas"),
        PABELLON("pabellon");

        public final String nombre;
        static final HashMap<String, TipoEdificio> by_str = new HashMap<>(); 
        static final HashMap<TipoEdificio, String> iconos = new HashMap<>();

        private TipoEdificio(String nombre) {
            this.nombre = nombre;
        }

        public String get_icono() {
            return iconos.get(this);
        }

        @Override
        public String toString() {
            return nombre;
        }

        public static TipoEdificio from_str(String edificio) {
            if (!by_str.containsKey(edificio)) {
                throw new IllegalArgumentException("edificio inválido");
            }
            return by_str.get(edificio);
        }

        static {
            for (TipoEdificio t: values()) {
                by_str.put(t.nombre, t);
            }
            iconos.put(CASA, "");
            iconos.put(HOTEL, "󰋡");
            iconos.put(TERMAS, "󰘆");
            iconos.put(PABELLON, "󰶠");
        }
    }

    public Edificio(TipoEdificio tipo) {
        this.tipo = tipo;
    }

    public TipoEdificio get_tipo() {
        return tipo;
    }

    public Casilla get_casilla(){
        List<Casilla> casillas = Monopoly.get().get_tablero().get_casillas();
        for(Casilla c: casillas){
            if(c.get_edificios().contains(this))
                return c;
        }
        return null;
    }

    @Override
    public String toString() {
        Casilla c = this.get_casilla();
        return String.format("%s - propietario: %s - casilla: %s - grupo: %s - coste: TODO\n", tipo, c.get_propietario().get_nombre(), c.get_nombre(), c.get_grupo().get_nombre());
    }

    public String representar() {
        Monopoly.Config c = Monopoly.get().get_config();
        if (c.get_iconos()) {
            return tipo.get_icono();
        }
        return tipo.toString().substring(0, 1).toUpperCase();
    }
}
