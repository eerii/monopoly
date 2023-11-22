package consola;

import java.util.HashMap;

public enum Color {
    NEGRO("\u001b[30m"),
    MORADO("\u001b[38;2;173;216;230m"), // Azul Clarito (Pastel)
    AZUL_CLARITO("\u001b[38;2;255;255;102m"), // Amarillo Claro (Pastel)
    ROSA("\u001b[38;2;152;255;152m"), // Verde Menta (Pastel)
    NARANJA("\u001b[38;2;255;182;193m"), // Rosa Suave (Pastel)
    ROJO("\u001b[38;2;200;160;255m"), // Naranja Claro (Pastel)
    AMARILLO("\u001b[38;2;200;162;200m"), // Morado Lavanda (Pastel)
    VERDE("\u001b[38;2;135;206;235m"), // Azul Cielo (Pastel)
    AZUL_OSCURO("\u001b[38;2;173;255;47m"), // Verde Lima (Pastel)

    BG_MORADO("\u001b[48;2;173;216;230m"), // Azul Clarito (Intenso)
    BG_AZUL_CLARITO("\u001b[48;2;255;255;102m"), // Amarillo Claro (Intenso)
    BG_ROSA("\u001b[48;2;152;255;152m"), // Verde Menta (Intenso)
    BG_NARANJA("\u001b[48;2;255;182;193m"), // Rosa Suave (Intenso)
    BG_ROJO("\u001b[48;2;200;160;255m"), // Naranja Claro (Intenso)
    BG_AMARILLO("\u001b[48;2;200;162;200m"), // Morado Lavanda (Intenso)
    BG_VERDE("\u001b[48;2;135;206;235m"), // Azul Cielo (Intenso)
    BG_AZUL_OSCURO("\u001b[48;2;173;255;47m"), // Verde Lima (Intenso)

    RESET("\u001b[0m"),
    BOLD("\u001b[1m"),
    DIM("\u001b[2m"),
    ITALIC("\u001b[3m"),
    UNDERLINE("\u001b[4m"),
    BLINK("\u001b[5m"),
    INVERSE("\u001b[7m"),
    HIDDEN("\u001b[8m"),
    STRIKE("\u001b[9m"),

    NONE("");

    public final String ansi;
    static final HashMap<Color, Color> bg = new HashMap<>();

    Color(String ansi) {
        this.ansi = ansi;
    }

    public Color to_bg() {
        return bg.get(this);
    }

    @Override
    public String toString() {
        return ansi;
    }

    static {
        bg.put(MORADO, BG_MORADO);
        bg.put(AZUL_CLARITO, BG_AZUL_CLARITO);
        bg.put(ROSA, BG_ROSA);
        bg.put(NARANJA, BG_NARANJA);
        bg.put(ROJO, BG_ROJO);
        bg.put(AMARILLO, BG_AMARILLO);
        bg.put(VERDE, BG_VERDE);
        bg.put(AZUL_OSCURO, BG_AZUL_OSCURO);
    }
}
