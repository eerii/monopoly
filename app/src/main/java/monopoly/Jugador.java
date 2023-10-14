package monopoly;

import java.util.ArrayList;
import java.util.List;

import consola.Color;

public class Jugador {
    String nombre;
    Avatar avatar;
    float fortuna;
    List<Casilla> propiedades; // TODO: Cambiar con herencia
    int vueltas;
    int contador_carcel = 0;
    static final float precio_carcel = 200.f;
    static final int turnos_carcel = 3;
    
    // Constructor de un jugador normal
    public Jugador(String nombre, Avatar avatar) {
        this.nombre = nombre;
        this.avatar = avatar;
        this.fortuna = 1000.f; // TODO: Un tercio da suma total de todalas propiedades do tableiro
        this.propiedades = new ArrayList<Casilla>();
        this.vueltas = 0;
        
        Tablero t = Monopoly.get().get_tablero();
        Casilla salida = t.buscar_casilla("Salida");
        salida.add_jugador(this, true);
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

    public void add_fortuna(float valor) {
        fortuna = fortuna + valor;
        if (fortuna < 0) {
            System.out.format("%s%s%s%s está en %s%sbancarrota%s! :(\n",
                Color.AZUL, Color.BOLD, nombre, Color.RESET,
                Color.ROJO, Color.BOLD, Color.RESET
            );
        }
    }

    public void add_propiedad(Casilla casilla, float precio) {
        propiedades.add(casilla);
        add_fortuna(-precio);
    }

    public void ir_a_carcel() {
        contador_carcel = turnos_carcel;
    }

    public int turno_carcel() {
        contador_carcel = contador_carcel > 0 ? contador_carcel - 1 : 0;
        return contador_carcel;
    }

    public boolean en_la_carcel() {
        return contador_carcel > 0;
    }

    public void salir_carcel() {
        if (fortuna < precio_carcel)
            throw new RuntimeException(String.format("el jugador %s no puede salir de la cárcel porque no tiene %.0f, quedan %d turnos para salir gratis\n", nombre, precio_carcel, contador_carcel));
        
        fortuna -= precio_carcel;
        contador_carcel = 0;
    }

    public void mover(Casilla actual, int movimiento) {
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
    }

    // String
    public String representar() {
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
            Color.BOLD, String.valueOf(avatar.get_id()), Color.RESET,
            avatar.get_tipo(),
            Color.AMARILLO, Color.BOLD, fortuna, Color.RESET,
            propiedades
            // TODO: imprimir hipotecas y edificios con colores en la lista de propiedades
        );
    }

    public String toStringMini() {
        return String.format("%s%s%s%s - avatar %s%s%s (%s)",
            Color.AZUL, Color.BOLD, nombre, Color.RESET,
            Color.BOLD, String.valueOf(avatar.get_id()), Color.RESET,
            avatar.get_tipo()
        );
    }
}
