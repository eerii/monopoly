package monopoly;

import java.util.Set;
import java.util.stream.Collectors;

import consola.Color;

import java.util.HashSet;
import java.util.List;

// TODO: Mover funcionalidad a Solar cuando se pueda

public class Casilla {
    String nombre;
    TipoCasilla tipo;
    Set<Jugador> jugadores;

    // Propiedades de Solar
    float precio = 0.f;

    float alquiler = 0.f;
    Boolean en_venta = false;

    Boolean hipotecado;
    Grupo grupo = null;

    public enum TipoCasilla {
        SOLAR,
        TRANSPORTE,
        SERVICIOS,
        SUERTE,
        COMUNIDAD,
        IMPUESTOS,
        CARCEL,
        A_LA_CARCEL,
        PARKING,
        SALIDA,
        NULL;
    }

    public Casilla(TipoCasilla tipo, String nombre) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.jugadores = new HashSet<Jugador>();
    }

    public Casilla(TipoCasilla tipo, String nombre, int precio, Grupo grupo) {
        this(tipo, nombre);
        this.precio = precio;
        this.alquiler = (float) (0.1 * precio);
        this.hipotecado = false;
        this.grupo = grupo;
        this.en_venta = true;
        grupo.add(this);
    }

    public void add_jugador(Jugador jugador) {
        add_jugador(jugador, false);
    }

    public void add_jugador(Jugador jugador, boolean ignorar) {
        jugadores.add(jugador);
        if (!ignorar) {
            Monopoly m = Monopoly.get();

            switch (tipo) {
                case SALIDA:
                    float media = m.get_media(); 
                    jugador.add_fortuna(media);
                    System.out.format("el jugador %s ha caído en la salida, recibe %.0f extra!\n", jugador.get_nombre(), media);
                    break;
                case IMPUESTOS:
                    float total = m.get_banca().get_fortuna();
                    jugador.add_fortuna((-1*precio));
                    m.get_banca().add_fortuna(precio);
                    System.out.format("el jugador %s ha caído la casilla de impuestos, paga %.0f a la banca!\n", jugador.get_nombre(), precio);
                    break;
                case A_LA_CARCEL: 
                    jugador.ir_a_carcel();
                    break;
                case CARCEL:
                    if (!jugador.en_la_carcel())
                        System.out.println("no te preocupes, solo pasas de visita");
                    break;
                case PARKING:
                    float bote = m.get_banca().get_fortuna();
                    jugador.add_fortuna(bote);
                    System.out.format("el jugador %s ha caído en el parking, recibe %.0f extra del bote!\n", jugador.get_nombre(), bote);
                default:
                    if(!this.en_venta)
                    {
                        Jugador j = this.get_dueno();
                            if(j!=null)
                            {
                                jugador.paga_alquiler(j,this);
                                System.out.format("%s%s%s%s paga %s%s%.0f%s de alquiler de %s%s%s%s a %s%s%s%s\n",
                                        Color.AZUL, Color.BOLD, jugador.get_nombre(), Color.RESET,
                                        this.get_color(), Color.BOLD, this.get_alquiler(), Color.RESET,
                                        this.get_color(), Color.BOLD, this.get_nombre(), Color.RESET,
                                        Color.AZUL, Color.BOLD, j.get_nombre(), Color.RESET);
                            }
                    }
            }
        }
    }

    public void remove_jugador(Jugador jugador) {
        jugadores.remove(jugador);
    }

    public boolean get_en_venta() {
        return (tipo == TipoCasilla.SOLAR || tipo == TipoCasilla.TRANSPORTE || tipo == TipoCasilla.SERVICIOS) && en_venta;
    }

    public void comprar(Jugador jugador) {
        if (!en_venta)
            throw new RuntimeException(String.format("la casilla %s no está en venta", nombre));

        if (!jugadores.contains(jugador))
            throw new RuntimeException(String.format("el jugador %s no está en la casilla %s por lo que no puede comprarla", jugador.get_nombre(), nombre));
        
        if (jugador.get_fortuna() < precio)
            throw new RuntimeException(String.format("el jugador %s no puede permitirse comprar la casilla %s por %.0f", jugador.get_nombre(), nombre, precio));

        jugador.add_propiedad(this, precio);
        en_venta=false;
    }

    public String get_nombre() {
        return nombre;
    }

    public float get_precio() {
        return precio;
    }
    public float get_alquiler() {
        return alquiler;
    }

    public Jugador get_dueno() {
        Monopoly m = Monopoly.get();
        List<Jugador> jugadores = m.get_jugadores();
        for(Jugador j : jugadores)
        {
            if(j.esDueno(this.nombre))
                return j;
        }
        return null;
    }

    public void incrementar_precio() {
        precio = (float)Math.floor(precio * 1.05f);
    }

    public TipoCasilla get_tipo() {
        return tipo;
    }

    public Color get_color() {
        return grupo != null ? grupo.get_color() : Color.BLANCO;
    }

    // String
    public String representar() {
        String str_jugadores = new String("");
        for (Jugador j : jugadores) {
            str_jugadores += " " + j.representar();
        }

        return String.format("%s&%s", nombre, str_jugadores);
    }

    String lista_jugadores() {
        List<String> js = jugadores.stream().map( Jugador::get_nombre ).collect( Collectors.toList() );
        String s = String.join(", ", js);
        return "[ " + s + " ]";
    }

    @Override
    public String toString() {
        Monopoly m = Monopoly.get();
        String sn = String.format("%s%s%s%s", Color.AZUL, Color.BOLD, nombre, Color.RESET);
        String st = String.format("%s%s%s%s", Color.VERDE, Color.BOLD, String.valueOf(tipo), Color.RESET);
        String sj = String.format("%s%s%s", Color.BOLD, lista_jugadores(), Color.RESET);
        String sp, sg, s;

        switch (tipo) {
            case SOLAR:
                Jugador j =this.get_dueno();
                if(j!=null)
                s = String.format("%s%s%s%s", Color.AMARILLO, Color.BOLD, this.get_dueno().get_nombre(), Color.RESET);
                else
                    s = " no hay ";
                sg = String.format("%s%s%s%s", String.valueOf(grupo.get_color()), Color.BOLD, grupo.get_nombre(), Color.RESET);
                sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, precio, Color.RESET);
                return String.format("%s - tipo: %s - propietario: %s - grupo: %s - valor: %s - jugadores: %s", sn, st, s, sg, sp, sj);
            case TRANSPORTE:
            case SERVICIOS:
                sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, precio, Color.RESET);
                return String.format("%s - tipo: %s - valor: %s - jugadores: %s", sn, st, sp, sj);
            case IMPUESTOS:
                sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, precio, Color.RESET);
                return String.format("%s - tipo: %s - a pagar: %s - jugadores: %s", sn, st, sp, sj);
            case PARKING:
                sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, m.get_banca().get_fortuna(), Color.RESET);
                return String.format("%s - tipo: %s - bote: %s - jugadores: %s", sn, st, sp, sj);
            case CARCEL:
                sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, precio, Color.RESET);
                return String.format("%s - tipo: %s - salir: %s - jugadores: %s", sn, st, sp, sj);
            default:
                return String.format("%s - tipo: %s - jugadores: %s", sn, st, sj);
        }
    }
}
