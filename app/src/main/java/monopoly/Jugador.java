package monopoly;

import java.util.List;

public class Jugador {
    String nombre;
    Avatar avatar;
    float fortuna;
    List<Casilla> propiedades; // TODO: Cambiar con herencia
    
    // Constructor de un jugador normal
    public Jugador(String nombre, Avatar avatar) {
        this.nombre = nombre;
        this.avatar = avatar;
        this.fortuna = 1000.f; // TODO: Un tercio da suma total de todalas propiedades do tableiro
        this.propiedades = List.of();
    }

    // Constructor de la banca
    public Jugador() {
        this.nombre = "banca";
        this.avatar = null;
        this.fortuna = -1.f;
        this.propiedades = List.of(); // TODO: Debe tener todas las propiedades
    }

    // String
    @Override
    public String toString() {
        return avatar.toString();
    }

    // Comparación de jugadores
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Jugador lhs = (Jugador) obj;
        return this.nombre == lhs.nombre;
    }
}
