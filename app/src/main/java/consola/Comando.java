package consola;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.util.Pair;

import monopoly.*;
import monopoly.Avatar.TipoAvatar;

public class Comando {
    public Comando(String cmd) {
        String[] args = cmd.split(" ");
        String arg_cmd = args[0];
        String arg_primeiro = args.length > 1 ? args[1] : "";
       
        try {
            this.cmd = Cmd.from_str(arg_cmd, arg_primeiro);
        } catch (IllegalArgumentException e) {
            throw e;
        }

        this.args = Arrays.stream(args, 1, args.length).collect(Collectors.toList());
    }

    Cmd cmd;
    List<String> args;

    // Gestionar todas las acciones
    // TAREA: Completar acciones
    public void run() {
        switch (cmd) {
            case CREAR:
                if (args.get(0).equals("jugador")) {
                    // TODO: mejorar gestión de errores
                    String nombre = args.get(1);
                    TipoAvatar tipo = TipoAvatar.from_str(args.get(2));
                    
                    Jugador j = new Jugador(nombre, new Avatar(tipo));
                    Monopoly.get().add_jugador(j);

                    return;
                }
                break;
            case VER:
                if (args.get(0).equals("tablero")) {
                    Tablero t = Monopoly.get().get_tablero();
                    System.out.println(t);
                    return;
                }
                break;
            case LISTAR:
                if (args.get(0).equals("jugadores")) {
                    List<Jugador> jugadores = Monopoly.get().get_jugadores();
                    for (int i = 0; i < jugadores.size(); i++) {
                        System.out.print(jugadores.get(i));
                        System.out.println(i == jugadores.size() - 1 ? "" : ",");
                    }
                    return;
                }
                break;
            case LANZAR:
                if (args.get(0).equals("dados")) {
                    Monopoly m = Monopoly.get();
                    Tablero t = m.get_tablero();
                    Jugador j = m.get_turno();
                    Dados d = m.get_dados();

                    Casilla inicial = t.buscar_jugador(j);
                   
                    d.tirar(j);
                    if (!d.primera_tirada() && !d.son_dobles()) {
                        System.out.println("No puedes volver a tirar");
                        return;
                    }
                    
                    int resultado = d.get_a() + d.get_b();
                    System.out.format("Los dados han sacado %d y %d\n", d.get_a(), d.get_b());
                    if (d.son_dobles())
                        System.out.println("Has sacado dobles! Tienes que volver a tirar"); // TODO: Gestionar esto en dados 

                    System.out.format("El avatar %s avanza %d posiciones, desde %s a %s\n", j.representar(), resultado, inicial.get_nombre(), "-");
                    return;
                }
                break;
            case DESCRIBIR:
                if (args.get(0).equals("jugador")) {
                    String nombre = args.get(1);
                    Jugador j = Monopoly.get().buscar_jugador(nombre);
                    if (j != null)
                        System.out.println(j);
                    return;
                }
                break;
            case JUGADOR:
                {
                    Jugador j = Monopoly.get().get_turno();
                    System.out.println(j.toStringMini());
                }
                break;
            case ACABAR:
                if (args.get(0).equals("turno")) {
                    Monopoly m = Monopoly.get();

                    if (m.get_dados().son_dobles()) {
                        System.out.println("Has sacado dobles, no puedes terminar el turno");
                        return;
                    }

                    m.siguiente_turno();
                    Jugador j = m.get_turno();
                    System.out.format("El jugador actual es %s\n", j.get_nombre());
                    return;
                }
                break;
            case SALIR:
                System.exit(0);
            default:
                System.out.println(cmd.to_str());
                System.out.println(args);
        }
    }
}
