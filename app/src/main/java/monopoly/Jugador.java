package monopoly;

import java.util.List;

public class Jugador {
    String nombre;
    Avatar avatar;
    int fortuna;
    List<Casilla> propiedades; // TODO: Cambiar con herencia
    
    // Constructor de un jugador normal
    public Jugador(String nombre, Avatar avatar) {
        this.nombre = nombre;
        this.avatar = avatar;
        this.fortuna = 1000; // TODO: Un tercio da suma total de todalas propiedades do tableiro
        this.propiedades = List.of();
    }

    // Constructor de la banca
    public Jugador() {
        this.nombre = "banca";
        this.avatar = null;
        this.fortuna = -1;
        this.propiedades = List.of(); // TODO: Debe tener todas las propiedades
    }
}
