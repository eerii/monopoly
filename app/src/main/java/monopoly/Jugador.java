package monopoly;

import java.util.List;

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
        return String.format("{\n" +
            "\tnombre: %s,\n" +
            "\tavatar: %s (%s),\n" +
            "\tfortuna: %.0f,\n" +
            "\tpropiedades: %s,\n" +
            "\thipotecas: %s,\n" +
            "\tedificios: %s\n" +
            "}",
            nombre,
            String.valueOf(avatar.get_id()), avatar.get_tipo(),
            fortuna,
            propiedades,
            "-", // TODO: imprimir hipotecas y edificios
            "-"
        );
    }

    public String toStringMini() {
        return String.format("{\n" +
            "\tnombre: %s,\n" +
            "\tavatar: %s (%s)\n" +
            "}",
            nombre,
            String.valueOf(avatar.get_id()), avatar.get_tipo()
        );
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
