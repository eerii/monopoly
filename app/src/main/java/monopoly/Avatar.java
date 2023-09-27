package monopoly;

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

        private TipoAvatar(String nombre) {
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return nombre;
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
}
