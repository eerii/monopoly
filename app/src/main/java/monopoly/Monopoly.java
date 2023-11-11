package monopoly;

import java.util.ArrayList;
import java.util.List;

import consola.Color;
import consola.Consola;
import monopoly.Carta.TipoCarta;

public class Monopoly {
    Consola consola;
    List<Jugador> jugadores;
    Jugador banca;
    Tablero tablero;
    Dados dados;
    List<Carta> baraja;
    Config config;
    int turno = -1;
    int vueltas_totales = 0;

    // TODO: Hacer estadísiticas del juego (probablemente una clase en consola)

    static Monopoly instance = null;

    Monopoly() {
        consola = new Consola();
        jugadores = new ArrayList<Jugador>();
        tablero = new Tablero();
        dados = new Dados();
        baraja = Carta.baraja;

        banca = new Jugador();
        for (Casilla c: tablero.get_casillas())
            banca.add_propiedad(c, 0.f);

        config = new Config();
    }

    public static Monopoly get() {
        if (instance == null)
            instance = new Monopoly();
        return instance;
    }

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

    public static void jugar(String[] args) {
        Monopoly m = Monopoly.get();
        m.consola.limpiar_pantalla();
        m.consola.limpiar_resultado();

        m.config.procesar(args);

        // FIX: Jugadores temporales para pruebas, borrar
        m.add_jugador(new Jugador("Jugador1", new Avatar(Avatar.TipoAvatar.ESFINGE)));
        m.add_jugador(new Jugador("Jugador2", new Avatar(Avatar.TipoAvatar.COCHE)));
        List<Casilla> c = m.get_tablero().get_casillas();
        Jugador banca = Monopoly.get().get_banca();
        Jugador j = m.get_jugadores().get(0);
        banca.remove_propiedad(c.get(6));
        j.add_propiedad(c.get(6), 0);
        banca.remove_propiedad(c.get(8));
        j.add_propiedad(c.get(8), 0);
        banca.remove_propiedad(c.get(9));
        j.add_propiedad(c.get(9), 0);

        boolean pausa = false;
        while(true) {
            m.consola.limpiar_pantalla();
            System.out.println(m.tablero);
            if (pausa) m.consola.limpiar_resultado();
            pausa = m.consola.entrada();

            if (m.jugadores.size() == 1) {
                System.out.format("el jugador %s ha ganado!\n", m.jugadores.get(0).get_nombre());
                break;
            }
        }
    }

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

    public Config get_config() {
        return config;
    }

    public void add_jugador(Jugador jugador) {
        if (jugadores.size() >= 6) {
            throw new IllegalStateException("hay demasiados jugadores");
        }
        jugadores.add(jugador);
        if (jugadores.size() == 1) {
            turno = 0;
        }
    }

    public void remove_jugador(Jugador jugador) {
        jugadores.remove(jugador);

        for (Casilla c : tablero.get_casillas())
            c.remove_jugador(jugador); 
    }

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

    public Jugador get_turno() {
        if (turno < 0)
            return null;
        return jugadores.get(turno);
    }

    public void siguiente_turno() {
        Jugador j = get_turno();
        int tc = j.turno_carcel();
        if (tc > 0)
            System.out.format("al jugador %s le quedan %d turnos en la cárcel\n", j.get_nombre(), tc);            

        turno = (turno + 1) % jugadores.size();
        j = get_turno();
        System.out.format("el jugador actual es %s%s%s%s\n", Color.AZUL_OSCURO, Color.BOLD, j.get_nombre(), Color.RESET);
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
                    .filter(c -> c.es_comprable() && c.en_venta())
                    .forEach(c -> c.incrementar_precio());
                System.out.println("el precio de todas las casillas se incrementa en un 5%");
            }
        }
    }

    public Consola get_consola() {
        return consola;
    }

    public List<Carta> barajar(TipoCarta tipo) {
        List<Carta> baraja = new ArrayList<Carta>(this.baraja);
        baraja.removeIf(c -> c.get_tipo() != tipo);
        java.util.Collections.shuffle(baraja);
        return baraja;
    }

    public Carta sacar_carta(List<Carta> baraja) {
        int n = -1;
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
