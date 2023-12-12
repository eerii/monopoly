package monopoly.casilla.propiedad;

import consola.Color;
import consola.excepciones.*;
import monopoly.Jugador;
import monopoly.Monopoly;
import monopoly.casilla.*;

public abstract class Propiedad extends Casilla {
    // ···········
    // Propiedades
    // ···········

    private float rentabilidad = 0;

    // ·············
    // Constructores
    // ·············

    public Propiedad(String nombre) {
        super(nombre);
    }

    // ·········
    // Overrides
    // ·········

    @Override
    public void add(Jugador jugador, boolean ignorar) {
        super.add(jugador, ignorar);
        if (!ignorar) {
            Monopoly m = Monopoly.get();

            Propiedad p = (Propiedad) this;
            if (p.en_venta()) {
                if (jugador.get_fortuna() < this.get_precio() || !jugador.puede_comprar())
                    return;

                System.out.format("¿Quieres comprar %s por %.0f? (s/N)\n", this.get_nombre(), this.get_precio());
                String respuesta = m.get_consola().get_raw().trim();
                if (respuesta.equalsIgnoreCase("s")) {
                    try {
                        p.comprar(jugador);
                    } catch (ComprarCasillaException e) {
                        System.out.println(e.getMessage());
                        return;
                    }
                    System.out.format(
                            "el jugador %s%s%s%s compra la casilla %s%s%s%s por %s%s%.0f%s. Su fortuna actual es de %s%s%.0f%s\n",
                            Color.ROJO, Color.BOLD, jugador.get_nombre(), Color.RESET,
                            Color.AZUL_OSCURO, Color.BOLD, this.get_nombre(), Color.RESET,
                            Color.AMARILLO, Color.BOLD, this.get_precio(), Color.RESET,
                            Color.ROSA, Color.BOLD, jugador.get_fortuna(), Color.RESET);
                    return;
                }
                return;
            }

            Jugador propietario = p.get_propietario();
            if (propietario != null && propietario != jugador) {
                if (p instanceof Solar)
                    jugador.paga_alquiler(propietario, (Solar) p);
                else
                    jugador.paga_servicio_transporte(propietario, p);
            }
        }
    }

    @Override
    public Color get_color() {
        return Color.BOLD;
    }

    @Override
    public String toString() {
        String sn = String.format("%s%s%s%s", Color.AZUL_CLARITO, Color.BOLD, this.get_nombre(), Color.RESET);
        String st = String.format("%s%s%s%s", Color.VERDE, Color.BOLD, "TODO", Color.RESET);
        String sj = String.format("%s%s%s", Color.BOLD, this.lista_jugadores(), Color.RESET);
        String sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, this.get_precio(), Color.RESET);

        return String.format("%s - tipo: %s - valor: %s - jugadores: %s", sn, st, sp, sj);
    }

    // ·······
    // Getters
    // ·······

    public float get_hipoteca() {
        return (float) Math.floor(0.5f * this.get_precio());
    }

    public float get_rentabilidad() {
        return rentabilidad;
    }

    public Jugador get_propietario() {
        Monopoly m = Monopoly.get();
        for (Jugador j : m.get_jugadores()) {
            if (j.es_propietario(this))
                return j;
        }
        return null;
    }

    public Jugador get_hipotecario() {
        Monopoly m = Monopoly.get();
        for (Jugador j : m.get_jugadores()) {
            if (j.es_hipotecario(this))
                return j;
        }
        return null;
    }

    // ················
    // Interfaz pública
    // ················

    public boolean en_venta() {
        return Monopoly.get().get_banca().es_propietario(this);
    }

    public void comprar(Jugador jugador) throws ComprarCasillaException {
        Monopoly m = Monopoly.get();

        if (!en_venta())
            throw new ComprarCasillaException(String.format("la casilla %s no está en venta", this.get_nombre()));

        if (!this.get_jugadores().contains(jugador))
            throw new ComprarCasillaException(
                    String.format("el jugador %s no está en la casilla %s por lo que no puede comprarla",
                            jugador.get_nombre(), this.get_nombre()));

        if (jugador.get_fortuna() < this.get_precio())
            throw new ComprarCasillaException(
                    String.format("el jugador %s no puede permitirse comprar la casilla %s por %.0f",
                            jugador.get_nombre(), this.get_nombre(), this.get_precio()));

        if (!jugador.puede_comprar())
            throw new ComprarCasillaException(
                    String.format("el jugador %s no puede comprar la casilla %s, ya lo ha hecho este turno",
                            jugador.get_nombre(), this.get_nombre()));

        m.get_banca().remove_propiedad(this);
        jugador.add_propiedad(this, this.get_precio());
        m.get_stats().of(jugador).sumar_dinero_invertido(this.get_precio());
    }

    public void hipotecar(Jugador jugador) throws HipotecaException {
        if (jugador.esta_hipotecada(this))
            throw new HipotecaException(
                    String.format("%s no puede hipotecar %s, ya la tiene hipotecada", jugador.get_nombre(),
                            this.get_nombre()));

        if (!jugador.es_propietario(this))
            throw new HipotecaException(
                    String.format("%s no puede hipotecar %s, no le pertenece", jugador.get_nombre(),
                            this.get_nombre()));

        jugador.remove_propiedad(this);
        jugador.add_hipoteca(this, this.get_hipoteca());
    }

    public void deshipotecar(Jugador jugador) throws HipotecaException {
        if (!jugador.esta_hipotecada(this)) {
            if (!jugador.es_propietario(this))
                throw new HipotecaException(
                        String.format("%s no puede deshipotecar %s, no le pertenece", jugador.get_nombre(),
                                this.get_nombre()));
            throw new HipotecaException(
                    String.format("%s no puede deshipotecar %s, no la tiene hipotecada", jugador.get_nombre(),
                            this.get_nombre()));
        }

        jugador.remove_hipoteca(this);
        try {
            jugador.add_propiedad(this, this.get_hipoteca() * 1.1f);
        } catch (ComprarCasillaException e) {
            throw new HipotecaException(e.getMessage());
        }
    }

    public void sumar_rentabilidad(float valor) {
        rentabilidad += valor;
    }

    public void incrementar_precio() {
        this.set_precio(get_precio() * 1.05f);
    }
}
