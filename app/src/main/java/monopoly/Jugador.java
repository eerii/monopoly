package monopoly;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
       
        // Todos los jugadores empiezan en la salida
        Tablero t = Monopoly.get().get_tablero();
        Casilla salida = t.buscar_casilla("Salida");
        salida.add_jugador(this, true);

        // La fortuna inicial es la suma del precio de todas las casillas comprables entre 3 
        for (Casilla c: t.get_casillas())
            if(c.get_tipo() == Casilla.TipoCasilla.SOLAR)
                fortuna += c.get_precio();
        fortuna /= 3.f;

        // La redondeamos a las centenas para que quede más bonita
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
                Color.AZUL_OSCURO, Color.BOLD, nombre, Color.RESET,
                Color.ROJO, Color.BOLD, Color.RESET
            );
            System.out.println("ni hipotecar ni bancarrota están implementados todavía");
            Monopoly.get().remove_jugador(this);
        }
        return fortuna > 0;
        // TODO: Comprobar bancarrota
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

    public boolean tiene_grupo(Grupo grupo) {
        return propiedades.containsAll(grupo.get_casillas());
    }

    public int num_servicios() {
        return (int)propiedades.stream().filter(c -> c.get_tipo() == Casilla.TipoCasilla.SERVICIOS).count();
    }

    public int num_transportes() {
        return (int)propiedades.stream().filter(c -> c.get_tipo() == Casilla.TipoCasilla.TRANSPORTE).count();
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
        Casilla siguiente = avatar.siguiente_casilla(actual, movimiento);
        System.out.format("el avatar %s%s%s%s avanza %d posiciones, desde %s%s%s%s a %s%s%s%s\n",
            Color.AZUL_CLARITO, Color.BOLD, this.representar(), Color.RESET,
            movimiento,
            actual.get_color(), Color.BOLD, actual.get_nombre(), Color.RESET,
            siguiente.get_color(), Color.BOLD, siguiente.get_nombre(), Color.RESET);

        actual.remove_jugador(this);
        siguiente.add_jugador(this);
    }

    public void dar_vuelta() {
        vueltas += 1;
        float media = Monopoly.get().get_tablero().precio_medio();
        add_fortuna(media);
        System.out.format("el jugador %s ha pasado por la salida, recibe %.0f\n", get_nombre(), media);
    }

    public void paga_alquiler(Jugador propietario, Solar casilla) {
        float alquiler = casilla.get_alquiler();

        if (propietario.tiene_grupo(((Solar)casilla).get_grupo()))
            alquiler *= 2;
        // TODO: Comprobar edificios

        this.add_fortuna(alquiler * -1.f);
        if (propietario.add_fortuna(alquiler)) {
            System.out.format("%s%s%s%s paga %s%s%.0f%s de alquiler de %s%s%s%s a %s%s%s%s\n",
                Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
                casilla.get_color(), Color.BOLD, alquiler, Color.RESET,
                casilla.get_color(), Color.BOLD, casilla.get_nombre(), Color.RESET,
                Color.AZUL_CLARITO, Color.BOLD, propietario.get_nombre(), Color.RESET);
        }
    }

    public void paga_servicio_transporte(Jugador propietario, CasillaComprable casilla)
    {
        float apagar = 0;
        float media = Monopoly.get().get_tablero().precio_medio();
        float dados = Monopoly.get().get_dados().get_total();
        if (propietario.num_servicios() == 1)
            apagar = media/ 200 * 4 * dados ;
        else if(propietario.num_servicios() == 2)
            apagar = media/ 200 * 10 * dados;
        else if(propietario.num_transportes()==1)
            apagar = (float) Math.floor (media* 0.25f);
        else if(propietario.num_transportes()==2)
            apagar = (float) Math.floor (media * 0.50f);
        else if(propietario.num_transportes()==3)
            apagar = (float) Math.floor (media * 0.75f);
        else if(propietario.num_transportes()==4)
            apagar = media;

        this.add_fortuna(apagar * -1.f);
        if (propietario.add_fortuna(apagar)) {
            System.out.format("%s%s%s%s paga %s%s%.0f%s de por el uso de %s%s%s%s a %s%s%s%s\n",
                    Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
                    casilla.get_color(), Color.BOLD, apagar, Color.RESET,
                    casilla.get_color(), Color.BOLD, casilla.get_nombre(), Color.RESET,
                    Color.AZUL_CLARITO, Color.BOLD, propietario.get_nombre(), Color.RESET);
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
            Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
            Color.BOLD, representar(), Color.RESET,
            avatar.get_tipo(),
            Color.AMARILLO, Color.BOLD, fortuna, Color.RESET,
            propiedades.stream().map(p -> p.get_nombre()).collect(Collectors.toList())
            // TODO: imprimir hipotecas y edificios con colores en la lista de propiedades
        );
    }

    public String toStringMini() {
        return String.format("%s%s%s%s - avatar %s%s%s (%s)",
            Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
            Color.BOLD, representar(), Color.RESET,
            avatar.get_tipo()
        );
    }
}
