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
    PLAIN("\u001b[0m");

    public final String ansi;

    private Color(String ansi) {
        this.ansi = ansi;
    }

    @Override
    public String toString() {
        return ansi;
    }
}
