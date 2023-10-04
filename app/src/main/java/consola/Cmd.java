package consola;

import java.util.List;
import java.util.HashMap;

public enum Cmd {
    CREAR("crear"),
    LISTAR("listar"),
    VER("ver"),
    COMPRAR("comprar"),
    LANZAR("lanzar"),
    XOGADOR("xogador"),
    REMATAR("rematar"),
    SAIR("sair"),
    DESCRIBIR("describir");
 
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

        if (arg == null || !args.get(value).contains(arg)) {
            throw new IllegalArgumentException("argumentos inválidos");
        }
        return value;
    }

    public String to_str() {
        return cmd;
    }

    static {
        // TAREA: Lista de cómandos válidos
        args.put(Cmd.CREAR, List.of("xogador"));
        args.put(Cmd.LISTAR, List.of("xogadores", "avatares"));
    }

    static {
        for (Cmd c: values()) {
            by_str.put(c.cmd, c);
        }
    }
}
