package monopoly;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import consola.Color;

import consola.excepciones.*;

public class Jugador {
    // TODO: Tratos con otros jugadores
    // Cambiar combinaciones de propiedades, dinero y turnos sin pagar

    // TODO: Aceptar trato

    // TODO: Listar tratos

    // TODO: Eliminar trato

    // TODO: Limpiar mucho los getters y setters de esta clase (son un desastre)

    String nombre;
    Avatar avatar;
    float fortuna;
    List<Casilla> propiedades;
    List<Casilla> hipotecas;
    int vueltas;
    int tiradas;
    float fortuna_total;
    int contador_carcel = 0;
    int turnos_extra = 0;
    int turnos_penalizacion = 0;
    boolean ha_comprado = false;

    static final int turnos_carcel = 3;

    // Constructor de un jugador normal
    public Jugador(String nombre, Avatar avatar) throws ConsolaException{
        this.nombre = nombre;
        this.avatar = avatar;
        this.propiedades = new ArrayList<Casilla>();
        this.hipotecas = new ArrayList<Casilla>();
        this.vueltas = 0;
        this.tiradas = 0;

        // Todos los jugadores empiezan en la salida
        Tablero t = Monopoly.get().get_tablero();
        Casilla salida = t.buscar_casilla("Salida");
        salida.add_jugador(this, true);

        // La fortuna inicial es la suma del precio de todas las casillas comprables
        // entre 3
        for (Casilla c : t.get_casillas())
            if (c.es_solar())
                fortuna += c.get_precio();
        fortuna /= 3.f;

        // La redondeamos a las centenas para que quede más bonita
        fortuna = (float) Math.ceil(fortuna / 100.f) * 100.f;
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

    public float get_fortunaTotal() {
        float total = fortuna;
        for (Casilla c : propiedades)
            total += c.get_precio();
        return total;
    }

    public int get_vueltas() {
        return vueltas;
    }

    public int get_tiradas() {
        return tiradas;
    }

    public void add_fortuna(float valor) {
        fortuna = fortuna + valor;
    }

    public void sumar_tirada() {
        tiradas += 1;
    }

    public void add_propiedad(Casilla casilla, float precio) throws ComprarException {
        if (avatar != null && avatar.get_tipo() == Avatar.TipoAvatar.COCHE && avatar.es_modo_avanzado() && ha_comprado)
            throw new ComprarException(
                    String.format("el jugador %s ya ha comprado una propiedad en este turno\n", nombre));

        ha_comprado = true;

        propiedades.add(casilla);
        add_fortuna(-precio);
    }

    public void add_hipoteca(Casilla casilla, float hipoteca) {
        hipotecas.add(casilla);
        add_fortuna(hipoteca);
    }

    public void remove_propiedad(Casilla casilla) {
        propiedades.remove(casilla);
    }

    public void remove_hipoteca(Casilla casilla) {
        hipotecas.remove(casilla);
    }

    public void ir_a_carcel() throws ConsolaException{
        Tablero t = Monopoly.get().get_tablero();
        Casilla c = t.buscar_casilla("Cárcel");
        Casilla actual = t.buscar_jugador(this);

        contador_carcel = turnos_carcel;

        actual.remove_jugador(this);
        c.add_jugador(this);

        System.out.format("el jugador %s ha ido a la cárcel!\n", nombre);
        Monopoly.get().siguiente_turno();
    }

    public void bancarrota() throws ConsolaException{
        Monopoly m = Monopoly.get();
        Casilla caida = m.get_tablero().buscar_jugador(this);

        Jugador otro = fortuna > 0 || caida.get_propietario() == null ? m.get_banca() : caida.get_propietario();
        if (otro != m.get_banca()) {
            otro.add_fortuna(fortuna);
        }

        for (Casilla c : propiedades)
            otro.add_propiedad(c, 0);

        for (Casilla h : hipotecas)
            otro.add_propiedad(h, 0);

        if (fortuna > 0 || caida.get_propietario() == null) {
            System.out.format("el jugador %s se ha declarado en bancarrota, todas sus propiedades pasan a la banca!\n",
                    nombre);
        } else {
            System.out.format(
                    "el jugador %s se ha declarado en bancarrota, todas sus propiedades pasan al jugador %s!\n", nombre,
                    otro.get_nombre());
            otro.add_fortuna(this.get_fortuna()); // Esto resta el dinero que no le pudo pagar el deudor al otro jugador
            caida.sumar_rentabilidad(this.get_fortuna());
        }
        m.remove_jugador(this);
    }

    public void turno() {
        contador_carcel = contador_carcel > 0 ? contador_carcel - 1 : 0;
        turnos_penalizacion = turnos_penalizacion > 0 ? turnos_penalizacion - 1 : 0;
        ha_comprado = false;
    }

    public boolean en_la_carcel() {
        return contador_carcel > 0;
    }

    public void salir_carcel() throws ConsolaException{
        Tablero t = Monopoly.get().get_tablero();
        Casilla c = t.buscar_casilla("Cárcel");

        if (fortuna < c.get_precio())
            throw new CarcelException(String.format(
                    "el jugador %s no puede salir de la cárcel porque no tiene %.0f, quedan %d turnos para salir gratis\n",
                    nombre, c.get_precio(), contador_carcel));

        fortuna -= c.get_precio();
        contador_carcel = 0;
    }

    public void add_penalizacion(int penalizacion) {
        this.turnos_penalizacion = penalizacion;
    }

    public boolean puede_tirar() {
        return contador_carcel == 0 && turnos_penalizacion == 0;
    }

    public boolean puede_comprar() {
        return !ha_comprado;
    }

    public void add_turno_extra(int extra) {
        turnos_extra = extra < 0 ? 0 : turnos_extra + extra;
    }

    public boolean tiene_turno_extra() {
        return turnos_extra > 0;
    }

    public boolean tiene_grupo(Grupo grupo) {
        return propiedades.containsAll(grupo.get_casillas());
    }

    public int num_servicios() {
        return (int) propiedades.stream().filter(c -> c.get_tipo() == Casilla.TipoCasilla.SERVICIOS).count();
    }

    public int num_transportes() {
        return (int) propiedades.stream().filter(c -> c.get_tipo() == Casilla.TipoCasilla.TRANSPORTE).count();
    }

    public boolean es_propietario(Casilla casilla) {
        return propiedades.contains(casilla);
    }

    public boolean es_hipotecario(Casilla casilla) {
        return hipotecas.contains(casilla);
    }

    public boolean esta_hipotecada(Casilla casilla) {
        return hipotecas.contains(casilla);
    }

    public void mover(Casilla actual, int movimiento) throws ConsolaException{
        avatar.siguiente_casilla(actual, movimiento);
        turnos_extra = turnos_extra > 0 ? turnos_extra - 1 : 0;
    }

    public void dar_vueltas(int num) throws ConsolaException{
        Monopoly m = Monopoly.get();

        vueltas += num;
        float media = m.get_tablero().precio_medio();
        for (int i = 0; i < num; i++) {
            add_fortuna(media);
            m.comprobar_vueltas();
            System.out.format("el jugador %s ha pasado por la salida, recibe %.0f\n", get_nombre(), media);
            m.get_stats().of(this).summar_pasar_salida(media);
        }
    }

    public List<Edificio> get_edificios() throws ConsolaException{
        List<Edificio> edificios = new ArrayList<>();
        Monopoly m = Monopoly.get();
        List<Casilla> casillas = m.get_tablero().get_casillas();

        for (Casilla c : casillas) {
            if (c.get_propietario() == this) {
                edificios.addAll(c.get_edificios());
            }
        }
        return edificios;
    }

    public void paga_alquiler(Jugador propietario, Casilla casilla) throws ConsolaException{
        Monopoly m = Monopoly.get();
        float alquiler = casilla.get_alquiler();
        float alquiler_casas = 0;
        float alquiler_hoteles = 0;
        float alquiler_termas = 0;
        float alquiler_pabellones = 0;
        if (propietario.tiene_grupo(casilla.get_grupo()))
            alquiler *= 2;
        int num = casilla.numero_edificios(Edificio.TipoEdificio.CASA);
        switch (num) {
            case 1:
                alquiler_casas = alquiler * 5;
                break;
            case 2:
                alquiler_casas = alquiler * 15;
                break;
            case 3:
                alquiler_casas = alquiler * 35;
                break;
            case 4:
                alquiler_casas = alquiler * 50;
                break;
        }
        num = casilla.numero_edificios(Edificio.TipoEdificio.HOTEL);
        alquiler_hoteles = alquiler * 70 * num;

        num = casilla.numero_edificios(Edificio.TipoEdificio.TERMAS);
        alquiler_termas = alquiler * 25 * num;

        num = casilla.numero_edificios(Edificio.TipoEdificio.PABELLON);
        alquiler_pabellones = alquiler * 25 * num;

        alquiler += alquiler_hoteles + alquiler_casas + alquiler_termas + alquiler_pabellones;

        add_fortuna(alquiler * -1.f);
        propietario.add_fortuna(alquiler);

        if (fortuna >= 0) {
            System.out.format("%s%s%s%s paga %s%s%.0f%s de alquiler de %s%s%s%s a %s%s%s%s\n",
                    Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
                    casilla.get_color(), Color.BOLD, alquiler, Color.RESET,
                    casilla.get_color(), Color.BOLD, casilla.get_nombre(), Color.RESET,
                    Color.AZUL_CLARITO, Color.BOLD, propietario.get_nombre(), Color.RESET);
        } else {
            System.out.format("%s%s%s%s no puede permitirse pagar %s%s%.0f%s de alquiler de %s%s%s%s a %s%s%s%s\n",
                    Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
                    casilla.get_color(), Color.BOLD, alquiler, Color.RESET,
                    casilla.get_color(), Color.BOLD, casilla.get_nombre(), Color.RESET,
                    Color.AZUL_CLARITO, Color.BOLD, propietario.get_nombre(), Color.RESET);
        }

        m.get_stats().of(this).sumar_pago_alquileres(alquiler);
        m.get_stats().of(propietario).sumar_cobro_alquileres(alquiler);
        casilla.sumar_rentabilidad(alquiler);

    }

    public void paga_servicio_transporte(Jugador propietario, Casilla casilla) throws ConsolaException{
        Monopoly m = Monopoly.get();
        Dados d = m.get_dados();
        float media = m.get_tablero().precio_medio();
        float coste = 0;

        switch (casilla.get_tipo()) {
            case SERVICIOS:
                coste = (float) Math
                        .floor(media / 200 * (propietario.num_servicios() == 1 ? 4 : 10) * (d.get_a() + d.get_b()));
                break;
            case TRANSPORTE:
                coste = (float) Math.floor(media * propietario.num_transportes() * 0.25);
                break;
            default:
        }
        this.add_fortuna(coste * -1.f);
        propietario.add_fortuna(coste);

        if (fortuna >= 0) {
            System.out.format("%s%s%s%s paga %s%s%.0f%s por el uso de %s%s%s%s a %s%s%s%s\n",
                    Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
                    casilla.get_color(), Color.BOLD, coste, Color.RESET,
                    casilla.get_color(), Color.BOLD, casilla.get_nombre(), Color.RESET,
                    Color.AZUL_CLARITO, Color.BOLD, propietario.get_nombre(), Color.RESET);
        } else {
            System.out.format("%s%s%s%s no se puede permitir el coste de %s%s%.0f%s de uso de %s%s%s%s a %s%s%s%s\n",
                    Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
                    casilla.get_color(), Color.BOLD, coste, Color.RESET,
                    casilla.get_color(), Color.BOLD, casilla.get_nombre(), Color.RESET,
                    Color.AZUL_CLARITO, Color.BOLD, propietario.get_nombre(), Color.RESET);
        }

        m.get_stats().of(this).sumar_pago_alquileres(coste);
        m.get_stats().of(propietario).sumar_cobro_alquileres(coste);
        casilla.sumar_rentabilidad(coste);
    }

    // String
    public String representar() throws ConsolaException{
        Monopoly.Config c = Monopoly.get().get_config();
        if (c.get_iconos()) {
            Avatar.TipoAvatar t = avatar.get_tipo();
            return t.get_icono();
        }
        return String.valueOf(avatar.get_id());
    }

    @Override
    public String toString() {

        try {
            return String.format(
                    "%s%s%s%s - avatar: %s%s%s (%s) - fortuna: %s%s%.0f%s\n" +
                            "propiedades: %s\nhipotecas: %s",
                    Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
                    Color.BOLD, representar(), Color.RESET,
                    avatar.get_tipo(),
                    Color.AMARILLO, Color.BOLD, fortuna, Color.RESET,
                    propiedades.stream().map(p -> p.toStringMini()).collect(Collectors.toList()),
                    hipotecas.stream().map(h -> h.get_nombre()).collect(Collectors.toList()));

        }catch (ConsolaException e) {

            System.out.println(e.getMessage());
        }

        return "";
    }

    public String toStringMini() throws ConsolaException{
        return String.format("%s%s%s%s - avatar %s%s%s (%s)",
                Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
                Color.BOLD, representar(), Color.RESET,
                avatar.get_tipo());
    }
}
