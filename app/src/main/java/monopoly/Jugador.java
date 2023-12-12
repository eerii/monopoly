package monopoly;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import consola.Color;
import consola.IConsola;
import consola.excepciones.*;

import monopoly.avatar.*;
import monopoly.casilla.Casilla;
import monopoly.casilla.edificio.*;
import monopoly.casilla.propiedad.*;

public class Jugador {
    // ···········
    // Propiedades
    // ···········

    private final String nombre;
    private final Avatar avatar;
    private float fortuna;
    private List<Propiedad> propiedades;
    private List<Propiedad> hipotecas;
    private int vueltas;
    private int tiradas;
    private int contador_carcel = 0;
    private int turnos_extra = 0;
    private int turnos_penalizacion = 0;
    private boolean ha_comprado = false;

    // ·············
    // Constructores
    // ·············

    public Jugador(String nombre, Avatar avatar) {
        this.nombre = nombre;
        this.avatar = avatar;
        this.propiedades = new ArrayList<>();
        this.hipotecas = new ArrayList<>();
        this.vueltas = 0;
        this.tiradas = 0;

        // Todos los jugadores empiezan en la salida
        Tablero t = Monopoly.get().get_tablero();
        Casilla salida = t.buscar_casilla("Salida");
        salida.add(this, true);

        // La fortuna inicial es la suma del precio de todas las propiedades entre 3
        for (Casilla c : t.get_casillas())
            if (c instanceof Solar)
                fortuna += ((Solar) c).get_precio();
        fortuna /= 3.f;

        // La redondeamos a las centenas para que quede más bonita
        fortuna = (float) Math.ceil(fortuna / 100.f) * 100.f;
    }

    // Constructor de la banca
    public Jugador() {
        this.nombre = "banca";
        this.avatar = null;
        this.fortuna = 0.f;
        this.propiedades = new ArrayList<>();
    }

    // ·········
    // Overrides
    // ·········

    @Override
    public String toString() {
        if (avatar instanceof Coche coche)
            return String.format(
                    "%s%s%s%s - avatar: %s%s%s (coche) - fortuna: %s%s%.0f%s\n" +
                            "propiedades: %s\nhipotecas: %s",
                    Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
                    Color.BOLD, representar(), Color.RESET,
                    Color.AMARILLO, Color.BOLD, fortuna, Color.RESET,
                    propiedades.stream().map(p -> p.get_nombre()).collect(Collectors.toList()),
                    hipotecas.stream().map(h -> h.get_nombre()).collect(Collectors.toList()));
        else if (avatar instanceof Sombrero sombrero)
            return String.format(
                    "%s%s%s%s - avatar: %s%s%s (sombrero) - fortuna: %s%s%.0f%s\n" +
                            "propiedades: %s\nhipotecas: %s",
                    Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
                    Color.BOLD, representar(), Color.RESET,
                    Color.AMARILLO, Color.BOLD, fortuna, Color.RESET,
                    propiedades.stream().map(p -> p.get_nombre()).collect(Collectors.toList()),
                    hipotecas.stream().map(h -> h.get_nombre()).collect(Collectors.toList()));
        else if (avatar instanceof Esfinge esfinge)
            return String.format(
                    "%s%s%s%s - avatar: %s%s%s (esfinge) - fortuna: %s%s%.0f%s\n" +
                            "propiedades: %s\nhipotecas: %s",
                    Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
                    Color.BOLD, representar(), Color.RESET,
                    Color.AMARILLO, Color.BOLD, fortuna, Color.RESET,
                    propiedades.stream().map(p -> p.get_nombre()).collect(Collectors.toList()),
                    hipotecas.stream().map(h -> h.get_nombre()).collect(Collectors.toList()));
        return String.format(
                "%s%s%s%s - avatar: %s%s%s (pelota) - fortuna: %s%s%.0f%s\n" +
                        "propiedades: %s\nhipotecas: %s",
                Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
                Color.BOLD, representar(), Color.RESET,
                Color.AMARILLO, Color.BOLD, fortuna, Color.RESET,
                propiedades.stream().map(p -> p.get_nombre()).collect(Collectors.toList()),
                hipotecas.stream().map(h -> h.get_nombre()).collect(Collectors.toList()));

    }

    // ·······
    // Getters
    // ·······

    public String get_nombre() {
        return nombre;
    }

    public Avatar get_avatar() {
        return avatar;
    }

    public float get_fortuna() {
        return fortuna;
    }

    public float get_fortuna_total() {
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

    public List<Edificio> get_edificios() {
        List<Edificio> edificios = new ArrayList<>();
        Monopoly m = Monopoly.get();
        List<Casilla> casillas = m.get_tablero().get_casillas();

        for (Casilla c : casillas) {
            if (c instanceof Solar) {
                Solar s = (Solar) c;
                if (s.get_propietario() == this) {
                    edificios.addAll(s.get_edificios());
                }
            }
        }
        return edificios;
    }

    // ················
    // Interfaz pública
    // ················

    // Adders

    public void add_fortuna(float valor) {
        fortuna = fortuna + valor;
    }

    public void add_propiedad(Casilla casilla, float precio) throws ComprarCasillaException {
        if (avatar != null && avatar instanceof Coche && avatar.es_modo_avanzado() && ha_comprado)
            throw new ComprarCasillaException(
                    String.format("el jugador %s ya ha comprado una propiedad en este turno\n", nombre));

        ha_comprado = true;

        if (!(casilla instanceof Propiedad))
            throw new ComprarCasillaException(String.format("la casilla %s no es comprable\n", casilla.get_nombre()));
        propiedades.add((Propiedad) casilla);
        add_fortuna(-precio);
    }

    public void add_hipoteca(Propiedad casilla, float hipoteca) {
        hipotecas.add(casilla);
        add_fortuna(hipoteca);
    }

    public void add_penalizacion(int penalizacion) {
        this.turnos_penalizacion = penalizacion;
    }

    public void add_turno_extra(int extra) {
        turnos_extra = extra < 0 ? 0 : turnos_extra + extra;
    }

    public void add_tirada() {
        tiradas += 1;
    }

    // Removers

    public void remove_propiedad(Propiedad casilla) {

        propiedades.remove(casilla);
        if (casilla instanceof Solar solar) {
            solar.clean_noAlquiler();
        }
        ;
    }

    public void remove_hipoteca(Propiedad casilla) {
        hipotecas.remove(casilla);
    }

    // Cárcel

    public void ir_a_carcel() {
        Tablero t = Monopoly.get().get_tablero();
        IConsola cons = Monopoly.get().get_consola();
        Casilla c = t.buscar_casilla("Cárcel");
        Casilla actual = t.buscar_jugador(this);

        contador_carcel = Monopoly.Config.turnos_carcel;

        actual.remove(this);
        c.add(this, false);

        cons.imprimir("el jugador %s ha ido a la cárcel!\n", nombre);
        Monopoly.get().siguiente_turno();
    }

    public boolean en_la_carcel() {
        return contador_carcel > 0;
    }

    public void salir_carcel() throws CarcelException {
        Tablero t = Monopoly.get().get_tablero();
        Casilla c = t.buscar_casilla("Cárcel");

        if (fortuna < c.get_precio())
            throw new CarcelException(String.format(
                    "el jugador %s no puede salir de la cárcel porque no tiene %.0f, quedan %d turnos para salir gratis\n",
                    nombre, c.get_precio(), contador_carcel));

        fortuna -= c.get_precio();
        contador_carcel = 0;
    }

    // Bancarrota

    public void bancarrota() throws ComprarCasillaException {
        Monopoly m = Monopoly.get();
        IConsola cons = m.get_consola();
        Casilla caida = m.get_tablero().buscar_jugador(this);
        Propiedad p = null;
        if (caida instanceof Propiedad)
            p = (Propiedad) caida;

        Jugador otro = fortuna > 0 || p == null || p.get_propietario() == null ? m.get_banca()
                : p.get_propietario();
        if (otro != m.get_banca()) {
            otro.add_fortuna(fortuna);
        }

        for (Propiedad c : propiedades)
            otro.add_propiedad(c, 0);

        for (Propiedad h : hipotecas)
            otro.add_propiedad(h, 0);

        if (fortuna > 0 || p == null || p.get_propietario() == null) {
            cons.imprimir("el jugador %s se ha declarado en bancarrota, todas sus propiedades pasan a la banca!\n",
                    nombre);
        } else {
            cons.imprimir(
                    "el jugador %s se ha declarado en bancarrota, todas sus propiedades pasan al jugador %s!\n", nombre,
                    otro.get_nombre());
            otro.add_fortuna(this.get_fortuna()); // Esto resta el dinero que no le pudo pagar el deudor al otro jugador
            p.sumar_rentabilidad(this.get_fortuna());
        }
        m.remove_jugador(this);
    }

    // Turnos

    public void turno() {
        contador_carcel = contador_carcel > 0 ? contador_carcel - 1 : 0;
        turnos_penalizacion = turnos_penalizacion > 0 ? turnos_penalizacion - 1 : 0;
        ha_comprado = false;
    }

    public boolean tiene_turno_extra() {
        return turnos_extra > 0;
    }

    public boolean puede_tirar() {
        return contador_carcel == 0 && turnos_penalizacion == 0;
    }

    public boolean puede_comprar() {
        return !ha_comprado;
    }

    // Propiedades

    public boolean tiene_grupo(Grupo grupo) {
        return propiedades.containsAll(grupo.get_casillas());
    }

    public int num_servicios() {
        return (int) propiedades.stream().filter(c -> c instanceof Servicio).count();
    }

    public int num_transportes() {
        return (int) propiedades.stream().filter(c -> c instanceof Transporte).count();
    }

    public boolean es_propietario(Propiedad casilla) {
        return propiedades.contains(casilla);
    }

    public boolean es_hipotecario(Propiedad casilla) {
        return hipotecas.contains(casilla);
    }

    public boolean esta_hipotecada(Propiedad casilla) {
        return hipotecas.contains(casilla);
    }

    // Mover

    public void mover(Casilla actual, int movimiento) {
        if (avatar instanceof Coche coche)
            coche.siguiente_casilla(actual, movimiento);
        else if (avatar instanceof Pelota pelota)
            pelota.siguiente_casilla(actual, movimiento);
        else
            avatar.siguiente_casilla(actual, movimiento);
        turnos_extra = turnos_extra > 0 ? turnos_extra - 1 : 0;
    }

    public void dar_vueltas(int num) {
        Monopoly m = Monopoly.get();

        vueltas += num;
        float media = m.get_tablero().precio_medio();
        for (int i = 0; i < num; i++) {
            add_fortuna(media);
            m.comprobar_vueltas();
            m.get_consola().imprimir("el jugador %s ha pasado por la salida, recibe %.0f\n", get_nombre(), media);
            m.get_stats().of(this).sumar_pasar_salida(media);
        }
    }

    public void paga_alquiler(Jugador propietario, Solar casilla) {
        Monopoly m = Monopoly.get();
        IConsola cons = m.get_consola();
        float alquiler;

        alquiler = casilla.get_alquiler();
        if (propietario.tiene_grupo(casilla.get_grupo()))
            alquiler *= 2;

        if (casilla.noPagaAlquiler(this)) {
            cons.imprimir(
                    "%s%s%s%s no paga %s%s%.0f%s de alquiler de %s%s%s%s a %s%s%s%s, %s%s%d%s turnos restantes de exencion %s%s%s%s\n",
                    Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
                    casilla.get_color(), Color.BOLD, alquiler, Color.RESET,
                    casilla.get_color(), Color.BOLD, casilla.get_nombre(), Color.RESET,
                    Color.AZUL_CLARITO, Color.BOLD, propietario.get_nombre(), Color.RESET,
                    Color.AZUL_CLARITO, Color.BOLD, casilla.getTurnosNoAlquiler(this), Color.RESET);
        } else {
            add_fortuna(alquiler * -1.f);
            propietario.add_fortuna(alquiler);
            if (fortuna >= 0) {
                cons.imprimir("%s%s%s%s paga %s%s%.0f%s de alquiler de %s%s%s%s a %s%s%s%s\n",
                        Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
                        casilla.get_color(), Color.BOLD, alquiler, Color.RESET,
                        casilla.get_color(), Color.BOLD, casilla.get_nombre(), Color.RESET,
                        Color.AZUL_CLARITO, Color.BOLD, propietario.get_nombre(), Color.RESET);
            } else {
                cons.imprimir("%s%s%s%s no puede permitirse pagar %s%s%.0f%s de alquiler de %s%s%s%s a %s%s%s%s\n",
                        Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
                        casilla.get_color(), Color.BOLD, alquiler, Color.RESET,
                        casilla.get_color(), Color.BOLD, casilla.get_nombre(), Color.RESET,
                        Color.AZUL_CLARITO, Color.BOLD, propietario.get_nombre(), Color.RESET);
            }

            m.get_stats().of(this).sumar_pago_alquileres(alquiler);
            m.get_stats().of(propietario).sumar_cobro_alquileres(alquiler);
            casilla.sumar_rentabilidad(alquiler);
        }
    }

    public void paga_servicio_transporte(Jugador propietario, Propiedad casilla) {
        Monopoly m = Monopoly.get();
        IConsola cons = m.get_consola();
        Dados d = m.get_dados();
        float media = m.get_tablero().precio_medio();
        float coste = 0;

        if (casilla instanceof Servicio) {
            coste = (float) Math
                    .floor(media / 200 * (propietario.num_servicios() == 1 ? 4 : 10) * (d.get_a() + d.get_b()));
        } else if (casilla instanceof Transporte) {
            coste = (float) Math.floor(media * propietario.num_transportes() * 0.25);
        }
        this.add_fortuna(coste * -1.f);
        propietario.add_fortuna(coste);

        if (fortuna >= 0) {
            cons.imprimir("%s%s%s%s paga %s%s%.0f%s por el uso de %s%s%s%s a %s%s%s%s\n",
                    Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
                    Color.BOLD, coste, Color.RESET,
                    Color.BOLD, casilla.get_nombre(), Color.RESET,
                    Color.AZUL_CLARITO, Color.BOLD, propietario.get_nombre(), Color.RESET);
        } else {
            cons.imprimir("%s%s%s%s no se puede permitir el coste de %s%s%.0f%s de uso de %s%s%s%s a %s%s%s%s\n",
                    Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
                    Color.BOLD, coste, Color.RESET,
                    Color.BOLD, casilla.get_nombre(), Color.RESET,
                    Color.AZUL_CLARITO, Color.BOLD, propietario.get_nombre(), Color.RESET);
        }

        m.get_stats().of(this).sumar_pago_alquileres(coste);
        m.get_stats().of(propietario).sumar_cobro_alquileres(coste);
        casilla.sumar_rentabilidad(coste);
    }

    // String

    public String toStringMini() {
        if (avatar instanceof Coche coche)
            return String.format("%s%s%s%s - avatar %s%s%s (coche)",
                    Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
                    Color.BOLD, representar(), Color.RESET);
        else if (avatar instanceof Sombrero sombrero)
            return String.format("%s%s%s%s - avatar %s%s%s (sombrero)",
                    Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
                    Color.BOLD, representar(), Color.RESET);
        else if (avatar instanceof Esfinge esfinge)
            return String.format("%s%s%s%s - avatar %s%s%s (esfinge)",
                    Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
                    Color.BOLD, representar(), Color.RESET);

        Pelota pelota = (Pelota) avatar;
        return String.format("%s%s%s%s - avatar %s%s%s (pelota)",
                Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
                Color.BOLD, representar(), Color.RESET);
    }

    public String representar() {
        Avatar avatar = get_avatar();
        if (avatar instanceof Coche coche)
            return coche.representar();
        else if (avatar instanceof Sombrero sombrero)
            return sombrero.representar();
        else if (avatar instanceof Esfinge esfinge)
            return esfinge.representar();
        Pelota pelota = (Pelota) avatar;
        return pelota.representar();
    }
}
