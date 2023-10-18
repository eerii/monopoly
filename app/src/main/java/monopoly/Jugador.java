package monopoly;

import java.util.ArrayList;
import java.util.List;

import consola.Color;

public class Jugador {
    String nombre;
    Avatar avatar;
    float fortuna;
    List<Casilla> propiedades;
    int vueltas;
    int contador_carcel = 0;
    static final int turnos_carcel = 3;
    
    // Constructor de un jugador normal
    public Jugador(String nombre, Avatar avatar) {
        this.nombre = nombre;
        this.avatar = avatar;
        this.propiedades = new ArrayList<Casilla>();
        this.vueltas = 0;
        
        Tablero t = Monopoly.get().get_tablero();
        Casilla salida = t.buscar_casilla("Salida");
        salida.add_jugador(this, true);

        for (Casilla c: t.get_casillas())
            if(c.get_tipo()== Casilla.TipoCasilla.SOLAR)
                        fortuna += c.get_precio();
        fortuna /= 3.f;
        // Lo redondeamos en las centenas para que quede más bonito
        fortuna = (float)Math.ceil(fortuna / 100.f) * 100.f;
    }

    // Constructor de la banca
    public Jugador() {
        this.nombre = "banca";
        this.avatar = null;
        this.fortuna = 0.f;
        this.propiedades = new ArrayList<Casilla>();
    }

    public String get_nombre() {
        return nombre;
    }

    public Avatar get_avatar() {
        return avatar;
    }

    public float get_fortuna() {
        return fortuna;
    }
    
    public int get_vueltas() {
        return vueltas;
    }

    public boolean add_fortuna(float valor) {
        fortuna = fortuna + valor;
        if (fortuna < 0) {
            System.out.format("%s%s%s%s está en %s%sbancarrota%s! :(\n",
                Color.AZUL, Color.BOLD, nombre, Color.RESET,
                Color.ROJO, Color.BOLD, Color.RESET
            );
            System.out.println("ni hipotecar ni bancarrota están implementados todavía");
            Monopoly.get().remove_jugador(this);
        }
        return fortuna < 0;
    }

    public void add_propiedad(Casilla casilla, float precio) {
        propiedades.add(casilla);
        add_fortuna(-precio);
    }

    public void ir_a_carcel() {
        Tablero t = Monopoly.get().get_tablero();
        Casilla c = t.buscar_casilla("Cárcel");
        Casilla actual = t.buscar_jugador(this);

        contador_carcel = turnos_carcel;

        actual.remove_jugador(this);
        c.add_jugador(this);

        System.out.format("el jugador %s ha ido a la cárcel!\n", nombre);
        Monopoly.get().siguiente_turno();
    }

    public int turno_carcel() {
        contador_carcel = contador_carcel > 0 ? contador_carcel - 1 : 0;
        return contador_carcel;
    }

    public boolean en_la_carcel() {
        return contador_carcel > 0;
    }

    public void salir_carcel() {
        Tablero t = Monopoly.get().get_tablero();
        Casilla c = t.buscar_casilla("Cárcel");

        if (fortuna < c.get_precio())
            throw new RuntimeException(String.format("el jugador %s no puede salir de la cárcel porque no tiene %.0f, quedan %d turnos para salir gratis\n", nombre, c.get_precio(), contador_carcel));
        
        fortuna -= c.get_precio();
        contador_carcel = 0;
    }

    public boolean es_propietario(String nombre) {
        for (Casilla c : propiedades) {
            if (c.get_nombre().equals(nombre)) {
                return true;
            }
        }
        return false;
    }

    public void mover(Casilla actual, int movimiento) {
        Monopoly m = Monopoly.get();
        Casilla siguiente = avatar.siguiente_casilla(actual, movimiento);
        System.out.format("el avatar %s%s%s%s avanza %d posiciones, desde %s%s%s%s a %s%s%s%s\n",
            Color.AZUL, Color.BOLD, this.representar(), Color.RESET,
            movimiento,
            actual.get_color(), Color.BOLD, actual.get_nombre(), Color.RESET,
            siguiente.get_color(), Color.BOLD, siguiente.get_nombre(), Color.RESET);

        actual.remove_jugador(this);
        siguiente.add_jugador(this);
    }

    public void dar_vuelta() {
        vueltas += 1;
        float media = Monopoly.get().get_media();
        add_fortuna(media);
        System.out.format("el jugador %s ha pasado por la salida, recibe %.0f\n", get_nombre(), media);
    }

    public void paga_alquiler(Jugador propietario, Casilla casilla) {
        float alquiler = casilla.get_alquiler();
        this.add_fortuna(alquiler * -1.f);
        if (propietario.add_fortuna(alquiler)) {
            System.out.format("%s%s%s%s paga %s%s%.0f%s de alquiler de %s%s%s%s a %s%s%s%s\n",
                Color.AZUL, Color.BOLD, nombre, Color.RESET,
                casilla.get_color(), Color.BOLD, alquiler, Color.RESET,
                casilla.get_color(), Color.BOLD, casilla.get_nombre(), Color.RESET,
                Color.AZUL, Color.BOLD, propietario.get_nombre(), Color.RESET);
        }
    }

    // String
    public String representar() {
        Monopoly.Config c = Monopoly.get().get_config();
        if (c.get_iconos()) {
            Avatar.TipoAvatar t = avatar.get_tipo();
            return t.get_icono();
        }
        return String.valueOf(avatar.get_id());
    }

    @Override
    public String toString() {
        // NOTA: El formato es diferente al del ejemplo para hacerlo más compacto
        //       De esta manera podemos tener una terminal interactiva con el tablero y la salida de los comandos
        return String.format(
            "%s%s%s%s - avatar: %s%s%s (%s) - fortuna: %s%s%.0f%s\n" +
            "propiedades: %s",
            Color.AZUL, Color.BOLD, nombre, Color.RESET,
            Color.BOLD, representar(), Color.RESET,
            avatar.get_tipo(),
            Color.AMARILLO, Color.BOLD, fortuna, Color.RESET,
            propiedades // TODO: imprimir hipotecas y edificios con colores en la lista de propiedades
        );
    }

    public String toStringMini() {
        return String.format("%s%s%s%s - avatar %s%s%s (%s)",
            Color.AZUL, Color.BOLD, nombre, Color.RESET,
            Color.BOLD, representar(), Color.RESET,
            avatar.get_tipo()
        );
    }
}
