package monopoly;

import java.util.ArrayList;
import java.util.List;

import consola.Color;
import consola.Consola;
import estadisticas.Estadisticas;
import consola.excepciones.*;
import monopoly.casilla.*;
import monopoly.casilla.propiedad.*;
import monopoly.carta.*;

public class Monopoly {
    // TODO: Usar modificadores abstract y final en las jerarquías

    // TODO: Implementar métodos abstractos

    // TODO: Declarar constantes (en config por ejemplo)

    public class Config {
        boolean iconos = false;

        public void procesar(String[] args) {
            for (String arg : args) {
                switch (arg) {
                    case "iconos":
                        iconos = true;
                        break;
                }
            }
        }

        public boolean get_iconos() {
            return iconos;
        }
    }

    // ···········
    // Propiedades
    // ···········

    private Consola consola;
    private List<Jugador> jugadores;
    private Jugador banca;
    private Tablero tablero;
    private Dados dados;
    private final List<Suerte> barajaSuerte;
    private final List<Comunidad> barajaComunidad;
    private List<Trato> tratos;
    private Estadisticas stats;
    private Config config;
    private int turno = -1;
    private int vueltas_totales = 0;

    static Monopoly instance = null;

    // ·············
    // Constructores
    // ·············

    Monopoly() {
        consola = new Consola();
        jugadores = new ArrayList<Jugador>();
        tablero = new Tablero();
        dados = new Dados();
        barajaSuerte = Suerte.get_baraja();
        barajaComunidad = Comunidad.get_baraja();
        stats = new Estadisticas();
        tratos = new ArrayList<Trato>();

        banca = new Jugador();
        for (Casilla c : tablero.get_casillas())
            if (c instanceof Propiedad)
                try {
                    banca.add_propiedad((Propiedad) c, 0.f);
                } catch (ComprarCasillaException e) {
                    // No hacemos nada porque la banca no está comprando realmente las casillas
                    System.out.println("Aviso: " + e.getMessage());
                }

        config = new Config();
    }

    // ·········
    // Singleton
    // ·········

    public static Monopoly get() {
        if (instance == null)
            instance = new Monopoly();
        return instance;
    }

    // ·······
    // Getters
    // ·······

    public Tablero get_tablero() {
        return tablero;
    }

    public List<Jugador> get_jugadores() {
        return jugadores;
    }

    public Jugador get_banca() {
        return banca;
    }

    public Dados get_dados() {
        return dados;
    }

    public Estadisticas get_stats() {
        return stats;
    }

    public Config get_config() {
        return config;
    }

    public Jugador get_turno() {
        if (turno < 0)
            return null;
        return jugadores.get(turno % jugadores.size());
    }

    public Consola get_consola() {
        return consola;
    }

    public List<Carta> get_suerte() {
        List<Carta> baraja = new ArrayList<Carta>();
        for (Suerte s : barajaSuerte) {
            baraja.add(s);
        }
        return baraja;
    }

    public List<Carta> get_comunidad() {
        List<Carta> baraja = new ArrayList<Carta>();
        for (Comunidad c : barajaComunidad) {
            baraja.add(c);
        }
        return baraja;
    }

    // ················
    // Interfaz pública
    // ················

    public static void jugar(String[] args) {
        Monopoly m = Monopoly.get();
        m.consola.limpiar_pantalla();
        m.consola.limpiar_resultado();

        m.config.procesar(args);

        // FIX: Jugadores temporales para pruebas, borrar
        m.add_jugador(new Jugador("Jugador1", new Avatar(Avatar.TipoAvatar.ESFINGE)));
        m.add_jugador(new Jugador("Jugador2", new Avatar(Avatar.TipoAvatar.PELOTA)));

        boolean pausa = false;
        while (true) {
            m.consola.limpiar_pantalla();
            System.out.println(m.tablero.representar());
            if (pausa)
                m.consola.limpiar_resultado();
            pausa = m.consola.entrada();

            if (m.jugadores.size() == 1) {
                System.out.format("el jugador %s ha ganado!\n", m.jugadores.get(0).get_nombre());
                break;
            }
        }
    }

    // Adders

    public void add_jugador(Jugador jugador) {
        if (jugadores.size() >= 6) {
            throw new IllegalStateException("hay demasiados jugadores");
        }

        jugadores.add(jugador);
        if (jugadores.size() == 1) {
            turno = 0;
        }

        stats.add_jugador(jugador);
    }

    public void add_trato(Trato trato) {
        tratos.add(trato);
    }

    // Removers

    public void remove_jugador(Jugador jugador) {
        jugadores.remove(jugador);

        for (Casilla c : tablero.get_casillas())
            c.remove(jugador);
    }

    public void remove_trato(Trato trato) {
        tratos.remove(trato);
    }

    // Buscadores

    public Jugador buscar_jugador(String nombre) {
        for (Jugador j : jugadores) {
            if (j.get_nombre().equals(nombre)) {
                return j;
            }
        }
        return null;
    }

    public Jugador buscar_avatar(char caracter) {
        for (Jugador j : jugadores) {
            if (j.get_avatar().get_id() == caracter) {
                return j;
            }
        }
        return null;
    }

    public Trato buscar_trato(int id) {
        for (Trato t : tratos) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    // Otros

    public void listar_tratos() {
        int cont = 0;
        System.out.println("Tratos:");
        for (Trato t : tratos) {
            if (t.getJugadorRecibe().equals(get_turno().get_nombre())) {
                System.out.format(t.representar());
                cont = 1;
            }

        }
        if (cont == 0)
            System.out.println("No hay tratos disponibles\n");
    }

    public void siguiente_turno() {
        Jugador j = get_turno();
        j.turno();
        turno = (turno + 1) % jugadores.size();
        j = get_turno();
        System.out.format("el jugador actual es %s%s%s%s\n", Color.AZUL_OSCURO, Color.BOLD, j.get_nombre(),
                Color.RESET);
    }

    public void debug_set_turno(Jugador j) {
        turno = jugadores.indexOf(j);
        dados.cambio_jugador(null);
    }

    public void comprobar_vueltas() {
        int min = 10000;
        for (Jugador j : jugadores) {
            min = j.get_vueltas() < min ? j.get_vueltas() : min;
        }
        if (min > vueltas_totales) {
            vueltas_totales = min;
            System.out.format("todos los jugadores han completado %d vueltas!\n", vueltas_totales);
            if (vueltas_totales % 4 == 0) {
                tablero.get_casillas().stream()
                        .filter(c -> c instanceof Propiedad && ((Propiedad) c).en_venta())
                        .forEach(c -> ((Propiedad) c).incrementar_precio());
                System.out.println("el precio de todas las casillas se incrementa en un 5%");
            }
        }
    }

    public Carta sacar_carta(List<Carta> baraja) {

        int n = -1;
        java.util.Collections.shuffle(baraja); // Ya barajamos aquí
        while (n < 1 || n > 6) {
            System.out.print("elige una carta del 1 al 6: ");
            String respuesta = consola.get_raw().trim();
            try {
                n = Integer.parseInt(respuesta);
            } catch (NumberFormatException e) {
                System.out.println("debes introducir un número");
            }
        }

        if (n > baraja.size())
            throw new IllegalStateException("no hay tantas cartas en la baraja");

        return baraja.get(n - 1);
    }

}
