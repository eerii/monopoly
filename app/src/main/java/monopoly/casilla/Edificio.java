package monopoly.casilla;

import java.util.HashMap;

import monopoly.Monopoly;

public class Edificio {
    private final TipoEdificio tipo;
    private Solar casilla;

    // TODO: Cambiar a jerarquía de edificios

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
            for (TipoEdificio t : values()) {
                by_str.put(t.nombre, t);
            }
            iconos.put(CASA, "");
            iconos.put(HOTEL, "󰋡");
            iconos.put(TERMAS, "󰘆");
            iconos.put(PABELLON, "󰶠");
        }
    }

    public Edificio(TipoEdificio tipo, Solar casilla) {
        this.tipo = tipo;
        this.casilla = casilla;
    }

    public TipoEdificio get_tipo() {
        return tipo;
    }

    public float coste() {
        float base = (float) Math.floor(casilla.get_precio() * 0.2f);
        switch (tipo) {
            case CASA:
                return base;
            case HOTEL:
                return base * 2;
            case TERMAS:
                return base * 3;
            case PABELLON:
                return base * 4;
            default:
                return 0.0f;
        }
    }

    @Override
    public String toString() {
        return String.format("%s - propietario: %s - casilla: %s - grupo: %s - coste: %.2f\n",
                tipo, casilla.get_propietario().get_nombre(), casilla.get_nombre(), casilla.get_grupo().get_nombre(),
                coste());
    }

    public String representar() {
        Monopoly.Config c = Monopoly.get().get_config();
        if (c.get_iconos()) {
            return tipo.get_icono();
        }
        return tipo.toString().substring(0, 1).toUpperCase();
    }
}
