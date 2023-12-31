package consola;

import java.util.List;
import java.util.HashMap;

public enum Cmd {
    CREAR("crear"),
    LISTAR("listar"),
    VER("ver"),
    COMPRAR("comprar"),
    EDIFICAR("edificar"),
    VENDER("vender"),
    HIPOTECAR("hipotecar"),
    DESHIPOTECAR("deshipotecar"),
    BANCARROTA("bancarrota"),
    LANZAR("lanzar"),
    JUGADOR("jugador"),
    ACABAR("acabar"),
    DESCRIBIR("describir"),
    DAR("dar"),
    ESTADISTICAS("estadisticas"),
    CAMBIAR("cambiar"),
    TRATO("trato"),
    ACEPTAR("aceptar"),
    ELIMINAR("eliminar"),
    SALIR("salir");

    private final String cmd;
    private static final HashMap<Cmd, List<String>> args = new HashMap<>();
    private static final HashMap<String, Cmd> by_str = new HashMap<>();

    Cmd(String cmd) {
        this.cmd = cmd;
    }

    public static Cmd from_str(String cmd, String arg) {
        if (!by_str.containsKey(cmd)) {
            throw new IllegalArgumentException("comando inválido");
        }
        Cmd value = by_str.get(cmd);

        if (!args.get(value).contains(arg) && !args.get(value).contains("*")) {
            throw new IllegalArgumentException("argumentos inválidos");
        }
        return value;
    }

    public String to_str() {
        return cmd;
    }

    static {
        // TAREA: Lista de cómandos válidos
        args.put(Cmd.CREAR, List.of("jugador"));
        args.put(Cmd.LISTAR, List.of("jugadores", "avatares", "enventa", "edificios","tratos"));
        args.put(Cmd.VER, List.of("tablero"));
        args.put(Cmd.COMPRAR, List.of("*"));
        args.put(Cmd.EDIFICAR, List.of("casa", "hotel", "termas", "pabellon"));
        args.put(Cmd.VENDER, List.of("casa", "hotel", "termas", "pabellon"));
        args.put(Cmd.HIPOTECAR, List.of("*"));
        args.put(Cmd.DESHIPOTECAR, List.of("*"));
        args.put(Cmd.BANCARROTA, List.of(""));
        args.put(Cmd.LANZAR, List.of("dados"));
        args.put(Cmd.JUGADOR, List.of("*"));
        args.put(Cmd.ACABAR, List.of("turno"));
        args.put(Cmd.DESCRIBIR, List.of("*", "jugador", "avatar"));
        args.put(Cmd.DAR, List.of("dinero"));
        args.put(Cmd.ESTADISTICAS, List.of("*", ""));
        args.put(Cmd.CAMBIAR, List.of("modo"));
        args.put(Cmd.SALIR, List.of("", "carcel"));
        args.put(Cmd.TRATO, List.of("*"));
        args.put(Cmd.ELIMINAR, List.of("*"));
        args.put(Cmd.ACEPTAR, List.of("*"));
    }

    static {
        for (Cmd c : values()) {
            by_str.put(c.cmd, c);
        }
    }
}
