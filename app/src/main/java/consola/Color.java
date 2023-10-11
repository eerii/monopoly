package consola;

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

    private Color(String ansi) {
        this.ansi = ansi;
    }

    @Override
    public String toString() {
        return ansi;
    }
}
