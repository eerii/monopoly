package monopoly.casilla;

import consola.Color;
import consola.excepciones.*;
import monopoly.Jugador;
import monopoly.Monopoly;

public class Propiedad extends Casilla {
    float rentabilidad = 0;

    public Propiedad(TipoCasilla tipo, String nombre) {
        super(tipo, nombre);
    }

    public void sumar_rentabilidad(float valor) {
        rentabilidad += valor;
    }

    public boolean en_venta() {
        return Monopoly.get().get_banca().es_propietario(this);
    }

    public float get_hipoteca() {
        return (float) Math.floor(0.5f * precio);
    }

    public Jugador get_propietario() {
        Monopoly m = Monopoly.get();
        for (Jugador j : m.get_jugadores()) {
            if (j.es_propietario(this))
                return j;
        }
        return null;
    }

    public float get_rentabilidad() {
        return rentabilidad;
    }

    public Jugador get_hipotecario() {
        Monopoly m = Monopoly.get();
        for (Jugador j : m.get_jugadores()) {
            if (j.es_hipotecario(this))
                return j;
        }
        return null;
    }

    public void comprar(Jugador jugador) throws ComprarCasillaException {
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

    public void hipotecar(Jugador jugador) throws HipotecaException {
        if (jugador.esta_hipotecada(this))
            throw new HipotecaException(
                    String.format("%s no puede hipotecar %s, ya la tiene hipotecada", jugador.get_nombre(), nombre));

        if (!jugador.es_propietario(this))
            throw new HipotecaException(
                    String.format("%s no puede hipotecar %s, no le pertenece", jugador.get_nombre(), nombre));

        jugador.remove_propiedad(this);
        jugador.add_hipoteca(this, this.get_hipoteca());
    }

    public void deshipotecar(Jugador jugador) throws HipotecaException {
        if (!jugador.esta_hipotecada(this)) {
            if (!jugador.es_propietario(this))
                throw new HipotecaException(
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

    // String

    @Override
    public String toString() {
        String sn = String.format("%s%s%s%s", Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET);
        String st = String.format("%s%s%s%s", Color.VERDE, Color.BOLD, String.valueOf(tipo), Color.RESET);
        String sj = String.format("%s%s%s", Color.BOLD, lista_jugadores(), Color.RESET);
        String sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, precio, Color.RESET);

        return String.format("%s - tipo: %s - valor: %s - jugadores: %s", sn, st, sp, sj);
    }
}
