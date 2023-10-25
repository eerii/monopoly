package consola;

import java.util.HashMap;

public enum Color {
    NEGRO("\u001b[30m"),
    MORADO("\u001b[38;5;141m"),
    AZUL_CLARITO("\u001b[38;5;153m"),
    ROSA("\u001b[38;5;212m"),
    NARANJA("\u001b[38;5;216m"),
    ROJO("\u001b[38;5;203m"),
    AMARILLO("\u001b[38;5;220m"),
    VERDE("\u001b[38;5;42m"),
    AZUL_OSCURO("\u001b[38;5;39m"),

    BG_MORADO("\u001b[48;5;141m"),
    BG_AZUL_CLARITO("\u001b[48;5;153m"),
    BG_ROSA("\u001b[48;5;212m"),
    BG_NARANJA("\u001b[48;5;216m"),
    BG_ROJO("\u001b[48;5;203m"),
    BG_AMARILLO("\u001b[48;5;220m"),
    BG_VERDE("\u001b[48;5;42m"),
    BG_AZUL_OSCURO("\u001b[48;5;39m"),

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
