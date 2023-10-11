package monopoly;

import java.util.HashMap;
import java.util.Random;

// TODO: Tipo de avatar se implementará como subclases

public class Avatar {
    char id;
    TipoAvatar tipo;

    public enum TipoAvatar {
        ESFINGE("esfinge"),
        SOMBRERO("sombrero"),
        PELOTA("pelota"),
        COCHE("coche");

        public final String nombre;
        private static final HashMap<String, TipoAvatar> by_str = new HashMap<>(); 

        private TipoAvatar(String nombre) {
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return nombre;
        }

        public static TipoAvatar from_str(String avatar) {
            if (!by_str.containsKey(avatar)) {
                throw new IllegalArgumentException("avatar inválido");
            }
            return by_str.get(avatar);
        }

        static {
            for (TipoAvatar t: values()) {
                by_str.put(t.nombre, t);
            }
        }
    }

    public Avatar(TipoAvatar tipo) {
        Random r = new Random();
        this.id = (char)(r.nextInt(26) + 'A');
        this.tipo = tipo;
    }

    Casilla siguiente_casilla(Tablero tablero) {
        throw null;
    }

    public char get_id() {
        return id;
    }

    public TipoAvatar get_tipo() {
        return tipo;
    }

    // String
    @Override
    public String toString() {
        return "";
    }
}
