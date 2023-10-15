package consola;

import java.util.HashMap;

public enum Color {
    NEGRO("\u001b[30m"),
    ROJO("\u001b[31m"),
    VERDE("\u001b[32m"),
    AMARILLO("\u001b[33m"),
    AZUL("\u001b[34m"),
    VIOLETA("\u001b[35m"),
    CYAN("\u001b[36m"),
    BLANCO("\u001b[37m"),

    ALT_NEGRO("\u001b[90m"),
    ALT_ROJO("\u001b[91m"),
    ALT_VERDE("\u001b[92m"),
    ALT_AMARILLO("\u001b[93m"),
    ALT_AZUL("\u001b[94m"),
    ALT_VIOLETA("\u001b[95m"),
    ALT_CYAN("\u001b[96m"),
    ALT_BLANCO("\u001b[97m"),

    BG_NEGRO("\u001b[40m"),
    BG_ROJO("\u001b[41m"),
    BG_VERDE("\u001b[42m"),
    BG_AMARILLO("\u001b[43m"),
    BG_AZUL("\u001b[44m"),
    BG_VIOLETA("\u001b[45m"),
    BG_CYAN("\u001b[46m"),
    BG_BLANCO("\u001b[47m"),

    BG_ALT_NEGRO("\u001b[100m"),
    BG_ALT_ROJO("\u001b[101m"),
    BG_ALT_VERDE("\u001b[102m"),
    BG_ALT_AMARILLO("\u001b[103m"),
    BG_ALT_AZUL("\u001b[104m"),
    BG_ALT_VIOLETA("\u001b[105m"),
    BG_ALT_CYAN("\u001b[106m"),
    BG_ALT_BLANCO("\u001b[107m"),

    RESET("\u001b[0m"),
    BOLD("\u001b[1m"),
    DIM("\u001b[2m"),
    ITALIC("\u001b[3m"),
    UNDERLINE("\u001b[4m"),
    BLINK("\u001b[5m"),
    INVERSE("\u001b[7m"),
    HIDDEN("\u001b[8m"),
    STRIKE("\u001b[9m");

    public final String ansi;
    static final HashMap<Color, Color> bg = new HashMap<>(); 

    private Color(String ansi) {
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
        bg.put(NEGRO, BG_NEGRO);
        bg.put(ROJO, BG_ROJO);
        bg.put(VERDE, BG_VERDE);
        bg.put(AMARILLO, BG_AMARILLO);
        bg.put(AZUL, BG_AZUL);
        bg.put(VIOLETA, BG_VIOLETA);
        bg.put(CYAN, BG_CYAN);
        bg.put(BLANCO, BG_BLANCO);

        bg.put(ALT_NEGRO, BG_ALT_NEGRO);
        bg.put(ALT_ROJO, BG_ALT_ROJO);
        bg.put(ALT_VERDE, BG_ALT_VERDE);
        bg.put(ALT_AMARILLO, BG_ALT_AMARILLO);
        bg.put(ALT_AZUL, BG_ALT_AZUL);
        bg.put(ALT_VIOLETA, BG_ALT_VIOLETA);
        bg.put(ALT_CYAN, BG_ALT_CYAN);
        bg.put(ALT_BLANCO, BG_ALT_BLANCO);
    }
}
