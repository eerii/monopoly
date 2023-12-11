package consola;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import consola.excepciones.*;

import monopoly.*;
import monopoly.Avatar.TipoAvatar;
import monopoly.casilla.*;
import monopoly.casilla.Edificio.TipoEdificio;

public class Comando {
    // TODO: Cambiar un poco esto por una interfaz comando

    public Comando(String cmd) {
        args = new ArrayList<String>(List.of(cmd.split(" ")));
        if (args.size() == 1)
            args.add("");

        try {
            this.cmd = Cmd.from_str(args.get(0).toLowerCase(), args.get(1));
        } catch (IllegalArgumentException e) {
            throw e;
        }

        args.remove(0);
    }

    private Cmd cmd;
    private List<String> args;

    // Gestionar todas las acciones
    public void run() throws ConsolaException {
        switch (cmd) {
            case CREAR:
                switch (args.get(0)) {
                    case "jugador":
                        if (args.size() != 3)
                            throw new IllegalArgumentException(
                                    "argumentos inválidos, uso: crear jugador [nombre] [avatar]");

                        String nombre = args.get(1).toLowerCase();
                        TipoAvatar tipo = TipoAvatar.from_str(args.get(2));

                        for (Jugador j : Monopoly.get().get_jugadores())
                            if (j.get_nombre().equals(nombre))
                                throw new IllegalArgumentException("el nombre del jugador ya está cogido");

                        Jugador j = new Jugador(nombre, new Avatar(tipo));
                        Monopoly.get().add_jugador(j);
                        System.out.println(j.toStringMini());

                        break;
                    default:
                }
                break;

            case LISTAR:
                List<Jugador> jugadores = Monopoly.get().get_jugadores();
                List<Casilla> casillas = Monopoly.get().get_tablero().get_casillas();

                switch (args.get(0).toLowerCase()) {
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
                        for (Casilla c : casillas) {
                            if (c instanceof Propiedad && ((Propiedad) c).en_venta()) {
                                System.out.println(c);
                            }
                        }
                        break;
                    case "tratos":
                        Monopoly.get().listar_tratos();
                        break;
                    case "edificios":
                        int vacios = 0;
                        if (args.size() == 1) {
                            for (Casilla c : casillas) {
                                if (c instanceof Solar) {
                                    Solar s = (Solar) c;
                                    for (Edificio e : s.get_edificios())
                                        System.out.println(e);
                                    if (s.get_edificios().isEmpty())
                                        vacios += 1;
                                }
                            }
                            if (vacios == 22)
                                throw new NoSuchElementException("no existen edificios construidos en el tablero!");
                            return;
                        }

                        Grupo g = Monopoly.get().get_tablero().get_grupo(args.get(1));
                        if (g == null)
                            throw new NoSuchElementException(String.format("el grupo '%s' no existe\n", args.get(1)));
                        List<Solar> casillas_grupo = g.get_casillas();

                        for (Solar s : casillas_grupo) {
                            for (Edificio e : s.get_edificios())
                                System.out.println(e);
                            if (s.get_edificios().isEmpty())
                                vacios += 1;
                        }
                        if (vacios == casillas_grupo.size())
                            throw new NoSuchElementException("no existen edificios construidos en el grupo!");

                        break;

                    default:
                }
                break;

            case VER:
                switch (args.get(0).toLowerCase()) {
                    case "tablero":
                        Tablero t = Monopoly.get().get_tablero();
                        System.out.println(t);
                        break;
                    default:
                }
                break;

            case COMPRAR: {
                Monopoly m = Monopoly.get();
                Tablero t = m.get_tablero();
                Jugador j = m.get_turno();
                Casilla c = t.buscar_casilla(args.get(0));

                if (c == null)
                    throw new NoSuchElementException(String.format("la casilla '%s' no existe\n", args.get(0)));

                if (!(c instanceof Propiedad))
                    throw new ComprarCasillaException(
                            String.format("la casilla '%s' no se puede comprar\n", args.get(0)));

                ((Propiedad) c).comprar(j);
                System.out.format(
                        "el jugador %s%s%s%s compra la casilla %s%s%s%s por %s%s%.0f%s. Su fortuna actual es de %s%s%.0f%s\n",
                        Color.ROJO, Color.BOLD, j.get_nombre(), Color.RESET,
                        Color.AZUL_OSCURO, Color.BOLD, c.get_nombre(), Color.RESET,
                        Color.AMARILLO, Color.BOLD, c.get_precio(), Color.RESET,
                        Color.ROSA, Color.BOLD, j.get_fortuna(), Color.RESET);
            }
                break;

            case EDIFICAR: {
                Monopoly m = Monopoly.get();
                Tablero t = m.get_tablero();
                Jugador j = m.get_turno();
                Casilla c = t.buscar_jugador(j);

                if (!(c instanceof Solar))
                    throw new PropiedadException(String.format("la casilla '%s' no es un solar\n", args.get(0)));
                Solar s = (Solar) c;

                if (!j.es_propietario(s))
                    throw new PropiedadException(
                            String.format("no eres el propietario de la casilla '%s'\n", c.get_nombre()));

                Grupo g = s.get_grupo();
                if (!j.tiene_grupo(g))
                    throw new PropiedadException(String
                            .format("no tienes en propiedad todas las casillas del grupo '%s'\n", g.get_nombre()));

                TipoEdificio tipo = TipoEdificio.from_str(args.get(0).toLowerCase());
                s.comprar_edificio(j, tipo);

            }
                break;

            case VENDER: {
                Monopoly m = Monopoly.get();
                Tablero t = m.get_tablero();
                Jugador j = m.get_turno();
                Casilla c = t.buscar_jugador(j);

                if (!(c instanceof Solar))
                    throw new PropiedadException(String.format("la casilla '%s' no es un solar\n", args.get(0)));
                Solar s = (Solar) c;

                if (!j.es_propietario(s))
                    throw new PropiedadException(
                            String.format("no eres el propietario de la casilla '%s'\n", c.get_nombre()));

                TipoEdificio tipo = TipoEdificio.from_str(args.get(0));
                s.vender_edificio(j, tipo);

            }
                break;

            case HIPOTECAR: {
                Monopoly m = Monopoly.get();
                Tablero t = m.get_tablero();
                Jugador j = m.get_turno();
                Casilla c = t.buscar_casilla(args.get(0));

                if (c == null)
                    throw new NoSuchElementException(String.format("la casilla '%s' no existe\n", args.get(0)));

                if (!(c instanceof Propiedad))
                    throw new HipotecaException(String.format("la casilla '%s' no se puede hipotecar\n", args.get(0)));

                Propiedad p = ((Propiedad) c);
                p.hipotecar(j);
                System.out.format(
                        "el jugador %s%s%s%s hipoteca la casilla %s%s%s%s y recibe %s%s%.0f%s\n",
                        Color.ROJO, Color.BOLD, j.get_nombre(), Color.RESET,
                        Color.AZUL_OSCURO, Color.BOLD, p.get_nombre(), Color.RESET,
                        Color.AMARILLO, Color.BOLD, p.get_hipoteca(), Color.RESET);

                if (p instanceof Solar) {
                    System.out.format(
                            "no puede recibir alquileres ni hipotecar en el grupo %s%s%s%s\n",
                            Color.ROSA, Color.BOLD, ((Solar) p).get_grupo().get_nombre(), Color.RESET);
                }
            }
                break;

            case DESHIPOTECAR: {
                Monopoly m = Monopoly.get();
                Tablero t = m.get_tablero();
                Jugador j = m.get_turno();
                Casilla c = t.buscar_casilla(args.get(0));

                if (c == null)
                    throw new NoSuchElementException(String.format("la casilla '%s' no existe\n", args.get(0)));

                if (!(c instanceof Propiedad))
                    throw new HipotecaException(
                            String.format("la casilla '%s' no se puede deshipotecar\n", args.get(0)));

                Propiedad p = ((Propiedad) c);
                p.deshipotecar(j);
                System.out.format(
                        "el jugador %s%s%s%s deshipoteca la casilla %s%s%s%s y paga %s%s%.0f%s\n",
                        Color.ROJO, Color.BOLD, j.get_nombre(), Color.RESET,
                        Color.AZUL_OSCURO, Color.BOLD, p.get_nombre(), Color.RESET,
                        Color.AMARILLO, Color.BOLD, p.get_hipoteca() * 1.1f, Color.RESET);
            }
                break;

            case BANCARROTA: {
                Monopoly m = Monopoly.get();
                Jugador j = m.get_turno();
                Jugador actual;
                j.bancarrota();
                actual = m.get_turno();
                System.out.format("el jugador actual es %s%s%s%s\n", Color.AZUL_OSCURO, Color.BOLD, actual.get_nombre(),
                        Color.RESET);
            }
                break;

            case LANZAR:
                switch (args.get(0).toLowerCase()) {
                    case "dados":
                        Monopoly m = Monopoly.get();
                        Tablero t = m.get_tablero();
                        Jugador j = m.get_turno();
                        Dados d = m.get_dados();

                        Casilla actual = t.buscar_jugador(j);

                        if (!j.puede_tirar())
                            throw new LanzarDadosException("no puedes tirar");
                        if (!j.tiene_turno_extra() && !d.cambio_jugador(j) && d.get_dobles() == 0)
                            throw new LanzarDadosException("no puedes volver a tirar");
                        if (j.get_fortuna() < 0)
                            throw new LanzarDadosException("salda tus deudas antes de volver a tirar");

                        if (args.size() == 3)
                            d.debug_set(Integer.parseInt(args.get(1)), Integer.parseInt(args.get(2)));
                        else
                            d.tirar();

                        int movimiento = d.get_a() + d.get_b();
                        System.out.format("los dados han sacado %s%s%d%s y %s%s%d%s\n",
                                Color.ROJO, Color.BOLD, d.get_a(), Color.RESET,
                                Color.ROJO, Color.BOLD, d.get_b(), Color.RESET);

                        if (d.get_dobles() == 3) {
                            System.out.println("te has pasado de velocidad, vas a la cárcel");
                            j.ir_a_carcel();
                            break;
                        }

                        if (d.get_dobles() > 0) {
                            System.out.format("has sacado %s%sdobles%s! tienes que volver a tirar\n", Color.ROJO,
                                    Color.BOLD, Color.RESET);
                        }
                        j.sumar_tirada();
                        j.mover(actual, movimiento);

                        break;
                    default:
                }
                break;

            case JUGADOR: {
                Monopoly m = Monopoly.get();
                Jugador j = m.get_turno();

                if (!args.get(0).equals("")) {
                    j = m.buscar_jugador(args.get(0));
                    m.debug_set_turno(j);
                }

                System.out.println(j.toStringMini());
            }
                break;

            case ACABAR:
                switch (args.get(0).toLowerCase()) {
                    case "turno":
                        Monopoly m = Monopoly.get();
                        Jugador j = m.get_turno();
                        Dados d = m.get_dados();
                        d.cambio_jugador(j);

                        if (j.tiene_turno_extra())
                            throw new LanzarDadosException("aún tienes un turno extra, no puedes terminar el turno");

                        if (d.get_dobles() > 0 && j.puede_tirar())
                            throw new LanzarDadosException("has sacado dobles, no puedes terminar el turno");

                        if (d.get_dobles() < 0 && j.puede_tirar())
                            throw new LanzarDadosException("aún no has tirado, no puedes terminar el turno");

                        if (j.get_fortuna() < 0)
                            throw new LanzarDadosException("salda tus deudas antes de acabar tu turno!");

                        m.siguiente_turno();
                        break;
                    default:
                }
                break;

            case DESCRIBIR:
                switch (args.get(0).toLowerCase()) {
                    case "jugador":
                        if (args.size() != 2)
                            throw new IllegalArgumentException("argumentos inválidos, uso: describir jugador [nombre]");

                        String nombre = args.get(1);
                        Jugador j = Monopoly.get().buscar_jugador(nombre);

                        if (j == null)
                            throw new NoSuchElementException(String.format("el jugador '%s' no existe", nombre));
                        System.out.println(j);

                        break;
                    case "avatar":
                        if (args.size() != 2)
                            throw new IllegalArgumentException(
                                    "argumentos inválidos, uso: describir avatar [caracter]");

                        char caracter = args.get(1).charAt(0);
                        Jugador jugador = Monopoly.get().buscar_avatar(caracter);

                        if (jugador == null)
                            throw new NoSuchElementException(String.format("el jugador '%c' no existe", caracter));

                        Avatar a = jugador.get_avatar();
                        System.out.println(a);

                        break;
                    default:
                        if (args.size() != 1)
                            throw new IllegalArgumentException("argumentos inválidos, uso: describir [casilla]");

                        String n = args.get(0);
                        Casilla casilla = Monopoly.get().get_tablero().buscar_casilla(n);

                        if (casilla == null)
                            throw new NoSuchElementException(String.format("la casilla '%s' no existe", n));

                        System.out.println(casilla);
                }
                break;

            case ESTADISTICAS: {
                if (args.size() > 1)
                    throw new IllegalArgumentException("argumentos inválidos, uso: estadisticas [jugador]");
                if (args.get(0).equals("")) {
                    System.out.println(Monopoly.get().get_stats());
                } else {
                    String n = args.get(0);
                    Monopoly m = Monopoly.get();
                    Jugador j = m.buscar_jugador(n);

                    if (j == null)
                        throw new NoSuchElementException(String.format("el jugador '%s' no existe", n));

                    System.out.println(m.get_stats().of(j));
                }

            }
                break;

            case DAR:
                switch (args.get(0).toLowerCase()) {
                    case "dinero":
                        if (args.size() != 3)
                            throw new IllegalArgumentException(
                                    "argumentos inválidos, uso: dar dinero [nombre] [cantidad]");

                        String nombre = args.get(1);
                        float dinero = Float.parseFloat(args.get(2));
                        Jugador j = Monopoly.get().buscar_jugador(nombre);

                        if (j == null)
                            throw new NoSuchElementException(String.format("el jugador '%s' no existe", nombre));
                        j.add_fortuna(dinero);

                        break;
                    default:
                }
                break;

            case CAMBIAR:
                switch (args.get(0).toLowerCase()) {
                    case "modo":
                        Jugador j = Monopoly.get().get_turno();
                        Avatar a = j.get_avatar();
                        a.cambiar_modo();
                        System.out.format("el jugador %s%s%s%s cambia al modo %s%s%s%s\n",
                                Color.ROJO, Color.BOLD, j.get_nombre(), Color.RESET,
                                Color.AZUL_OSCURO, Color.BOLD, a.es_modo_avanzado() ? "avanzado" : "normal",
                                Color.RESET);

                        break;
                    default:
                }
                break;
            case TRATO:
                Trato t = new Trato(String.join(" ", args), Monopoly.get().get_turno().get_nombre());
                System.out.println(t);
                Monopoly.get().add_trato(t);
                break;
            case ACEPTAR: {
                if (args.size() != 1)
                    throw new IllegalArgumentException("argumentos inválidos, uso: aceptar [id]");
                String aux = args.get(0).toLowerCase().replace("trato", "");
                int id = Integer.parseInt(aux);
                Trato trato = Monopoly.get().buscar_trato(id);
                if (trato == null)
                    throw new NoSuchElementException(String.format("el trato '%d' no existe!", id));
                if (!trato.getJugadorRecibe().equals(Monopoly.get().get_turno().get_nombre()))
                    throw new TratoException(String.format("el trato '%d' no es para ti!", id));
                trato.aceptar_trato();
                Monopoly.get().remove_trato(trato);
            }
                break;
            case ELIMINAR: {
                if (args.size() != 1)
                    throw new IllegalArgumentException("argumentos inválidos, uso: eliminar trato[id]");
                String aux = args.get(0).toLowerCase().replace("trato", "");
                if (aux.isEmpty())
                    throw new IllegalArgumentException("no has introducido un id");
                int id = Integer.parseInt(aux);
                Trato trato = Monopoly.get().buscar_trato(id);
                if (trato == null)
                    throw new NoSuchElementException(String.format("el trato '%d' no existe!", id));
                Monopoly.get().remove_trato(trato);
                System.out.printf("el trato %s%d%s ha sido eliminado\n", Color.ROJO, id, Color.RESET);
            }

                break;
            case SALIR:
                switch (args.get(0).toLowerCase()) {
                    case "carcel":
                        if (args.size() != 1)
                            throw new IllegalArgumentException("argumentos inválidos, uso: salir carcel");

                        Jugador j = Monopoly.get().get_turno();
                        j.salir_carcel();

                        break;
                    case "":
                        System.out.print("hasta la próxima\n");
                        System.exit(0);
                        break;
                    default:
                }
                break;
        }
    }
}
