package consola;

import java.util.ArrayList;
import java.util.List;

import monopoly.*;
import monopoly.Avatar.TipoAvatar;

public class Comando {
    public Comando(String cmd) {
        args = new ArrayList<String>(List.of(cmd.split(" ")));
        if (args.size() == 1)
            args.add("");
       
        try {
            this.cmd = Cmd.from_str(args.get(0), args.get(1));
        } catch (IllegalArgumentException e) {
            throw e;
        }

        args.remove(0);
    }

    Cmd cmd;
    List<String> args;

    // Gestionar todas las acciones
    public void run() {
        switch (cmd) {
            case CREAR:
                switch (args.get(0)) {
                    case "jugador":
                        if (args.size() != 3)
                            throw new IllegalArgumentException("argumentos inválidos, uso: crear jugador [nombre] [avatar]");

                        String nombre = args.get(1);
                        TipoAvatar tipo = TipoAvatar.from_str(args.get(2));

                        for (Jugador j : Monopoly.get().get_jugadores())
                            if (j.get_nombre().equals(nombre))
                                throw new IllegalArgumentException("el nombre del jugador ya está cogido");

                        Jugador j = new Jugador(nombre, new Avatar(tipo));
                        Monopoly.get().add_jugador(j);
                        System.out.println(j.toStringMini());

                        break;
                    default:
                        throw new RuntimeException("unreachable");
                }
                break;

            case LISTAR:
                List<Jugador> jugadores = Monopoly.get().get_jugadores();

                switch (args.get(0)) {
                    case "jugadores":
                        for (Jugador j : jugadores) {
                            System.out.println(j);
                        }
                        break;
                    case "avatares":
                        for (Jugador j : jugadores) {
                            System.out.println(j.get_avatar());
                        }
                        break;
                    case "enventa":
                        throw new IllegalArgumentException("no implementado");
                    default:
                        throw new RuntimeException("unreachable");
                }
                break;

            case VER:
                switch (args.get(0)) {
                    case "tablero":
                        Tablero t = Monopoly.get().get_tablero();
                        System.out.println(t);
                        break;
                    default:
                        throw new RuntimeException("unreachable");
                }
                break;

            case COMPRAR:
                {
                    throw new IllegalArgumentException("no implementado");
                }
                //break;
            
            case LANZAR:
                switch (args.get(0)) {
                    case "dados":
                        Monopoly m = Monopoly.get();
                        Tablero t = m.get_tablero();
                        Jugador j = m.get_turno();
                        Dados d = m.get_dados();

                        Casilla actual = t.buscar_jugador(j);
                      
                        if (!d.cambio_jugador(j) && !d.son_dobles())
                            throw new RuntimeException("no puedes volver a tirar");
                        d.tirar();
                        
                        int movimiento = d.get_a() + d.get_b();
                        System.out.format("los dados han sacado %s%s%d%s y %s%s%d%s\n",
                            Color.ROJO, Color.BOLD, d.get_a(), Color.RESET,
                            Color.ROJO, Color.BOLD, d.get_b(), Color.RESET);
                        if (d.son_dobles())
                            System.out.format("has sacado %s%sdobles%s! tienes que volver a tirar\n",
                                Color.ALT_ROJO, Color.BOLD, Color.RESET);

                        j.mover(actual, movimiento);
                        
                        break;
                    default:
                        throw new RuntimeException("unreachable");
                }
                break;

            case JUGADOR:
                {
                    Jugador j = Monopoly.get().get_turno();
                    System.out.println(j.toStringMini());
                }
                break;
            
            case ACABAR:
                 switch (args.get(0)) {
                    case "turno":
                        Monopoly m = Monopoly.get();

                        if (m.get_dados().son_dobles())
                            throw new RuntimeException("has sacado dobles, no puedes terminar el turno");

                        m.siguiente_turno();
                        Jugador j = m.get_turno();
                        System.out.format("el jugador actual es %s%s%s%s\n", Color.AZUL, Color.BOLD, j.get_nombre(), Color.RESET);

                        break;
                    default:
                        throw new RuntimeException("unreachable");
                }
                break;

            case DESCRIBIR:
                switch (args.get(0)) {
                    case "jugador":
                        if (args.size() != 2)
                            throw new IllegalArgumentException("argumentos inválidos, uso: describir jugador [nombre]");

                        String nombre = args.get(1);
                        Jugador j = Monopoly.get().buscar_jugador(nombre);

                        if (j == null)
                            throw new RuntimeException(String.format("el jugador '%s' no existe", nombre));
                        System.out.println(j);

                        break;
                    case "avatar":
                        throw new IllegalArgumentException("no implementado");
                        //break;
                    default:
                        throw new IllegalArgumentException("no implementado");
                }
                break; 
 
            case SALIR:
                switch (args.get(0)) {
                    case "carcel":
                        throw new IllegalArgumentException("no implementado");
                        //break;
                    case "":
                        System.exit(0);
                        break;
                    default:
                        throw new RuntimeException("unreachable");
                }
        }
    }
}
