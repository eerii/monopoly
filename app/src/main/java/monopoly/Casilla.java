package monopoly;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import consola.Color;
import estadisticas.EstadisticasJugador;
import monopoly.Edificio.TipoEdificio;

import consola.excepciones.*;
public class Casilla {
    String nombre;
    TipoCasilla tipo;
    Set<Jugador> jugadores;
    float precio;
    float rentabilidad = 0;
    int vecesVisitada = 0;

    // TODO: Jerarquía de casillas
    // Propiedad, Accion, Impuesto, Grupo, Especial
    // Propiedad: Solar, Transporte, Servicios

    // CasillaComprable

    // Solar
    float alquiler;
    Grupo grupo;
    List<Edificio> edificios;

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
        this.precio = 0.f;
        this.edificios = new ArrayList<>();
    }

    // Solar
    public Casilla(TipoCasilla tipo, String nombre, float precio, Grupo grupo) {
        this(tipo, nombre);

        this.precio = precio;
        this.alquiler = (float) Math.floor(0.1f * precio);
        this.grupo = grupo;
        grupo.add(this);

    }

    public void add_jugador(Jugador jugador) throws ConsolaException{
        add_jugador(jugador, false);
    }

    public void add_jugador(Jugador jugador, boolean ignorar) throws ConsolaException{
        jugadores.add(jugador);
        if (!ignorar) {
            Monopoly m = Monopoly.get();
            EstadisticasJugador s = m.get_stats().of(jugador);

            switch (tipo) {
                case SALIDA:
                    float media = m.get_tablero().precio_medio();
                    jugador.add_fortuna(media);
                    System.out.format("el jugador %s ha caído en la salida, recibe %.0f extra!\n", jugador.get_nombre(),
                            media);
                    s.summar_pasar_salida(media);
                    break;
                case IMPUESTOS:
                    jugador.add_fortuna(precio * -1.f);
                    m.get_banca().add_fortuna(precio);
                    System.out.format("el jugador %s ha caído la casilla de impuestos, paga %.0f a la banca!\n",
                            jugador.get_nombre(), precio);
                    s.sumar_pago_tasa_impuestos(precio);
                    break;
                case A_LA_CARCEL:
                    jugador.ir_a_carcel();
                    s.sumar_veces_carcel();
                    break;
                case CARCEL:
                    if (!jugador.en_la_carcel())
                        System.out.println("no te preocupes, solo pasas de visita");
                    break;
                case PARKING:
                    float bote = m.get_banca().get_fortuna();
                    jugador.add_fortuna(bote);
                    m.get_banca().add_fortuna(bote * -1.f);
                    System.out.format("el jugador %s ha caído en el parking, recibe %.0f extra del bote!\n",
                            jugador.get_nombre(), bote);
                    s.sumar_premios(bote);
                    break;
                case SUERTE:
                case COMUNIDAD:
                    List<Carta> baraja = m
                            .barajar(tipo == TipoCasilla.SUERTE ? Carta.TipoCarta.SUERTE : Carta.TipoCarta.COMUNIDAD);
                    Carta c = m.sacar_carta(baraja);
                    System.out.format("el jugador %s ha caído en la casilla de %s, saca la carta: %s\n",
                            jugador.get_nombre(), tipo, c);
                    c.ejecutar(jugador);
                    break;
                default:
            }

            // CasillaComprable
            if (es_comprable() && en_venta()) {
                if (jugador.get_fortuna() < precio || !jugador.puede_comprar())
                    return;

                System.out.format("¿Quieres comprar %s por %.0f? (s/N)\n", nombre, precio);
                String respuesta = m.get_consola().get_raw().trim();
                if (respuesta.equalsIgnoreCase("s")) {
                    comprar(jugador);
                    System.out.format(
                            "el jugador %s%s%s%s compra la casilla %s%s%s%s por %s%s%.0f%s. Su fortuna actual es de %s%s%.0f%s\n",
                            Color.ROJO, Color.BOLD, jugador.get_nombre(), Color.RESET,
                            Color.AZUL_OSCURO, Color.BOLD, nombre, Color.RESET,
                            Color.AMARILLO, Color.BOLD, precio, Color.RESET,
                            Color.ROSA, Color.BOLD, jugador.get_fortuna(), Color.RESET);
                    return;
                }
                return;
            }

            Jugador propietario = this.get_propietario();
            if (propietario != null && propietario != jugador) {
                if (es_solar())
                    jugador.paga_alquiler(propietario, this);
                else
                    jugador.paga_servicio_transporte(propietario, this);
            }
        }
    }

    public void remove_jugador(Jugador jugador) {
        jugadores.remove(jugador);
    }

    public String get_nombre() {
        return nombre;
    }

    public void set_precio(float precio) {
        this.precio = (float) Math.floor(precio);
        // Solar
        this.alquiler = (float) Math.floor(0.1f * precio);
    }

    public float get_precio() {
        return precio;
    }

    public TipoCasilla get_tipo() {
        return tipo;
    }

    public List<Edificio> get_edificios() {
        return edificios;
    }

    public void sumar_rentabilidad(float valor) {
        rentabilidad += valor;
    }

    public void sumar_vecesVisitada() {
        vecesVisitada += 1;
    }

    public Color get_color() {
        return grupo != null ? grupo.get_color() : Color.NONE;
    }

    public boolean es_comprable() {
        return tipo == TipoCasilla.SOLAR || tipo == TipoCasilla.TRANSPORTE || tipo == TipoCasilla.SERVICIOS;
    }

    // CasillaComprable

    public boolean en_venta()throws ConsolaException {
        return Monopoly.get().get_banca().es_propietario(this);
    }

    public float get_hipoteca() {
        return (float) Math.floor(0.5f * precio);
    }

    public Jugador get_propietario() throws ConsolaException{
        Monopoly m = Monopoly.get();
        for (Jugador j : m.get_jugadores()) {
            if (j.es_propietario(this))
                return j;
        }
        return null;
    }

    public Jugador get_hipotecario() throws ConsolaException{
        Monopoly m = Monopoly.get();
        for (Jugador j : m.get_jugadores()) {
            if (j.es_hipotecario(this))
                return j;
        }
        return null;
    }

    public void comprar(Jugador jugador) throws ConsolaException{
        Monopoly m = Monopoly.get();

        if (!en_venta())
            throw new ComprarCasillaException(String.format("la casilla %s no está en venta", nombre));

        if (!jugadores.contains(jugador))
            throw new ComprarCasillaException(
                    String.format("el jugador %s no está en la casilla %s por lo que no puede comprarla",
                            jugador.get_nombre(), nombre));

        if (jugador.get_fortuna() < precio)
            throw new ComprarCasillaException(String.format("el jugador %s no puede permitirse comprar la casilla %s por %.0f",
                    jugador.get_nombre(), nombre, precio));

        if (!jugador.puede_comprar())
            throw new ComprarCasillaException(
                    String.format("el jugador %s no puede comprar la casilla %s, ya lo ha hecho este turno",
                            jugador.get_nombre(), nombre));

        m.get_banca().remove_propiedad(this);
        jugador.add_propiedad(this, precio);
        m.get_stats().of(jugador).sumar_dinero_invertido(precio);
    }

    public void hipotecar(Jugador jugador) throws ConsolaException{
        if (jugador.esta_hipotecada(this))
            throw new HipotecaException(
                    String.format("%s no puede hipotecar %s, ya la tiene hipotecada", jugador.get_nombre(), nombre));

        if (!jugador.es_propietario(this))
            throw new PropiedadException(
                    String.format("%s no puede hipotecar %s, no le pertenece", jugador.get_nombre(), nombre));

        jugador.remove_propiedad(this);
        jugador.add_hipoteca(this, this.get_hipoteca());
    }

    public void deshipotecar(Jugador jugador) throws ConsolaException {
        if (!jugador.esta_hipotecada(this)) {
            if (!jugador.es_propietario(this))
                throw new PropiedadException(
                        String.format("%s no puede deshipotecar %s, no le pertenece", jugador.get_nombre(), nombre));
            throw new HipotecaException(
                    String.format("%s no puede deshipotecar %s, no la tiene hipotecada", jugador.get_nombre(), nombre));
        }

        jugador.remove_hipoteca(this);
        jugador.add_propiedad(this, this.get_hipoteca() * 1.1f);
    }

    public void incrementar_precio() {
        precio = (float) Math.floor(precio * 1.05f);
    }

    // Solar

    public float get_alquiler() {
        final float alquileres[] = { 5, 15, 35, 50 };
        float alquiler = this.alquiler;
        int casas = 0;

        for (Edificio e : edificios) {
            switch (e.get_tipo()) {
                case CASA:
                    alquiler += this.alquiler * alquileres[casas++];
                    break;
                case HOTEL:
                    alquiler += this.alquiler * 70;
                    break;
                case TERMAS:
                case PABELLON:
                    alquiler += this.alquiler * 25;
                    break;
            }
        }

        return alquiler;
    }

    public float get_rentabilidad() {
        return rentabilidad;
    }

    public int get_vecesVisitada() {
        return vecesVisitada;
    }

    public Grupo get_grupo() {
        return grupo;
    }

    public int numero_edificios(TipoEdificio tipo) {
        return (int) edificios.stream().filter(e -> e.get_tipo() == tipo).count();
    }

    public int numero_edificiosGrupo(TipoEdificio tipo) {
        int total = 0;
        List<Casilla> casillas = this.get_grupo().get_casillas();
        for (Casilla c : casillas) {
            for (Edificio e : c.get_edificios()) {
                if (e.get_tipo() == tipo)
                    total += 1;
            }
        }
        return total;
    }

    public void comprar_edificio(Jugador jugador, TipoEdificio tipo) throws ConsolaException {
        int num = this.get_grupo().get_casillas().size();
        switch (tipo) {
            case CASA:
                if (numero_edificiosGrupo(TipoEdificio.CASA) == num && numero_edificiosGrupo(TipoEdificio.HOTEL) == num)
                    throw new ComprarEdificioException("ya tienes el numero máximo de casas y hoteles permitidos en el grupo");
                if (numero_edificios(TipoEdificio.CASA) == 4)
                    throw new ComprarEdificioException("no se pueden comprar más casas, ya tienes 4");
                break;

            case HOTEL:
                if (numero_edificiosGrupo(TipoEdificio.HOTEL) == num)
                    throw new ComprarEdificioException("ya tienes el maximo numero de hoteles permitidos en el grupo");
                if (numero_edificios(TipoEdificio.CASA) != 4)
                    throw new ComprarEdificioException("no se puede hacer un hotel, no tienes 4 casas");

                edificios = edificios.stream().filter(e -> e.get_tipo() != TipoEdificio.CASA)
                        .collect(Collectors.toList());
                break;

            case TERMAS:
                if (numero_edificiosGrupo(TipoEdificio.TERMAS) == num)
                    throw new ComprarEdificioException("ya tienes el maximo numero de termas permitidos en el grupo");
                if (numero_edificios(TipoEdificio.HOTEL) < 1 && numero_edificios(TipoEdificio.CASA) < 2)
                    throw new ComprarEdificioException(
                            "no se puede comprar unas termas, necesitas al menos 2 casas y un hotel");
                break;

            case PABELLON:
                if (numero_edificiosGrupo(TipoEdificio.PABELLON) == num)
                    throw new ComprarEdificioException("ya tienes el maximo numero de pabellones permitidos en el grupo");
                if (numero_edificios(TipoEdificio.HOTEL) < 2)
                    throw new ComprarEdificioException("no se puede comprar un pabellón, necesitas al menos 2 hoteles");
                break;
        }

        Edificio e = new Edificio(tipo, this);
        float coste = e.coste();

        if (jugador.get_fortuna() < coste)
            throw new ComprarEdificioException(String.format("no se puede comprar un%s %s por %.0f",
                    tipo == TipoEdificio.CASA ? "a" : "", tipo, coste));

        jugador.add_fortuna(coste * -1.f);
        edificios.add(e);

        System.out.format(
                "el jugador %s%s%s%s compra un%s %s en la casilla %s%s%s%s por %s. Su fortuna actual es de %s%s%.0f%s\n",
                Color.ROJO, Color.BOLD, jugador.get_nombre(), Color.RESET,
                tipo == TipoEdificio.CASA ? "a" : "", tipo,
                Color.AZUL_OSCURO, Color.BOLD, this.get_nombre(), Color.RESET, coste,
                Color.ROSA, Color.BOLD, jugador.get_fortuna(), Color.RESET);
    }

    public void vender_edificio(Jugador jugador, TipoEdificio tipo) throws PropiedadException {
        Edificio e = edificios.stream().filter(ed -> ed.get_tipo() == tipo).findFirst().orElse(null);
        if (e == null)
            throw new PropiedadException(
                    String.format("no se puede vender un edificio de tipo %s, no tienes ninguno", tipo));

        float coste = (float) Math.floor(e.coste() * 0.8f);
        edificios.remove(e);
        jugador.add_fortuna(coste);
        System.out.format(
                "el jugador %s%s%s%s vende un%s %s en la casilla %s%s%s%s por %s. Su fortuna actual es de %s%s%.0f%s\n",
                Color.ROJO, Color.BOLD, jugador.get_nombre(), Color.RESET,
                tipo == TipoEdificio.CASA ? "a" : "", tipo,
                Color.AZUL_OSCURO, Color.BOLD, this.get_nombre(), Color.RESET,
                coste,
                Color.ROSA, Color.BOLD, jugador.get_fortuna(), Color.RESET);
    }

    public boolean es_solar() {
        return tipo == TipoCasilla.SOLAR;
    }

    // String
    public String representar() throws ConsolaException{
        String str_jugadores = new String("");
        for (Jugador j : jugadores) {
            str_jugadores += " " + j.representar();
        }

        return String.format("%s&%s", nombre, str_jugadores);
    }

    String lista_jugadores() {
        List<String> js = jugadores.stream().map(Jugador::get_nombre).collect(Collectors.toList());
        String s = String.join(", ", js);
        return "[ " + s + " ]";
    }

    public String lista_edificios() {
        List<String> l = edificios.stream().map(e -> e.representar()).collect(Collectors.toList());
        return String.join("", l);
    }

    @Override
    public String toString() {

        try {

            String sn = String.format("%s%s%s%s", Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET);
            String st = String.format("%s%s%s%s", Color.VERDE, Color.BOLD, String.valueOf(tipo), Color.RESET);
            String sj = String.format("%s%s%s", Color.BOLD, lista_jugadores(), Color.RESET);
            String sp;

            switch (tipo) {
                case IMPUESTOS:
                    sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, precio, Color.RESET);
                    return String.format("%s - tipo: %s - a pagar: %s - jugadores: %s", sn, st, sp, sj);
                case PARKING:
                    sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, Monopoly.get().get_banca().get_fortuna(),
                            Color.RESET);
                    return String.format("%s - tipo: %s - bote: %s - jugadores: %s", sn, st, sp, sj);
                case CARCEL:
                    sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, precio, Color.RESET);
                    return String.format("%s - tipo: %s - salir: %s - jugadores: %s", sn, st, sp, sj);
                // CasillaComprable
                case TRANSPORTE:
                case SERVICIOS:
                    sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, precio, Color.RESET);
                    return String.format("%s - tipo: %s - valor: %s - jugadores: %s", sn, st, sp, sj);
                // Solar
                case SOLAR:
                    sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, precio, Color.RESET);
                    String sg = String.format("%s%s%s%s", String.valueOf(grupo.get_color()), Color.BOLD, grupo.get_nombre(),
                            Color.RESET);
                    String sa = String.format("%s%s%.0f%s", Color.ROJO, Color.BOLD, get_alquiler(), Color.RESET);
                    String sjp = get_propietario() != null ? String.format("%s%s%s%s", Color.AZUL_CLARITO, Color.BOLD,
                            this.get_propietario().get_nombre(), Color.RESET) : "";
                    String shp = get_hipotecario() != null ? String.format("%s%s%s%s", Color.AZUL_CLARITO, Color.BOLD,
                            this.get_hipotecario().get_nombre(), Color.RESET) : "";
                    return String.format(
                            "%s - tipo: %s - propietario: %s - edificios: %s - hipotecado: %s - grupo: %s - valor: %s - alquiler: %s - jugadores: %s",
                            sn, st, sjp, lista_edificios(), shp, sg, sp, sa, sj);
                default:
                    return String.format("%s - tipo: %s - jugadores: %s", sn, st, sj);
            }

        }catch (ConsolaException e) {

            System.out.println(e.getMessage());
        }

        return "";

    }

    public String toStringMini() {
        return String.format("%s %s", nombre, lista_edificios());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Casilla))
            return false;

        Casilla c = (Casilla) o;
        return c.nombre.equals(nombre);
    }
}
