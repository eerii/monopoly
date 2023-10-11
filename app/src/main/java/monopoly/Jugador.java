package monopoly;

import java.util.List;

import consola.Color;

public class Jugador {
    String nombre;
    Avatar avatar;
    float fortuna;
    List<Casilla> propiedades; // TODO: Cambiar con herencia
    int vueltas;
    
    // Constructor de un jugador normal
    public Jugador(String nombre, Avatar avatar) {
        this.nombre = nombre;
        this.avatar = avatar;
        this.fortuna = 1000.f; // TODO: Un tercio da suma total de todalas propiedades do tableiro
        this.propiedades = List.of();
        this.vueltas = 0;
        
        Tablero t = Monopoly.get().get_tablero();
        Casilla salida = t.buscar_casilla("Salida");
        salida.add_jugador(this);
    }

    // Constructor de la banca
    public Jugador() {
        this.nombre = "banca";
        this.avatar = null;
        this.fortuna = -1.f;
        this.propiedades = List.of(); // TODO: Debe tener todas las propiedades
    }

    public String get_nombre() {
        return nombre;
    }

    public Avatar get_avatar() {
        return avatar;
    }

    public Casilla mover(Casilla inicial, int posiciones) {
        // TODO: Implementar mover
        return inicial;
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
