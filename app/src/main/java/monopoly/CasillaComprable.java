package monopoly;

import consola.Color;

public class CasillaComprable extends Casilla {
    float alquiler;
    Boolean en_venta;
    Boolean hipotecado;

    public CasillaComprable(TipoCasilla tipo, String nombre) {
        super(tipo, nombre);

        this.alquiler = (float)Math.floor(0.1f * precio);
        this.en_venta = true;
        this.hipotecado = false;
    }

    @Override
    public void add_jugador(Jugador jugador, boolean ignorar) {
        jugadores.add(jugador);
        if (!ignorar) {
            if(!en_venta) {
                Jugador j = this.get_propietario();
                if(j != null)
                jugador.paga_alquiler(j, this);
            }
        }
    }

    @Override
    public void set_precio(float precio) {
        super.set_precio(precio);
        this.alquiler = (float)Math.floor(0.1f * precio);
    }

    public boolean get_en_venta() {
        return en_venta;
    }

    public float get_alquiler() {
        return alquiler;
    }

    public Jugador get_propietario() {
        Monopoly m = Monopoly.get();
        for(Jugador j : m.get_jugadores()) {
            if(j.es_propietario(nombre))
                return j;
        }
        return null;
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

    public void incrementar_precio() {
        precio = (float)Math.floor(precio * 1.05f);
    }

    @Override
    public String toString() {
        String sn = String.format("%s%s%s%s", Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET);
        String st = String.format("%s%s%s%s", Color.VERDE, Color.BOLD, String.valueOf(tipo), Color.RESET);
        String sj = String.format("%s%s%s", Color.BOLD, lista_jugadores(), Color.RESET);
        String sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, precio, Color.RESET);
        String sa = String.format("%s%s%.0f%s", Color.ROJO, Color.BOLD, alquiler, Color.RESET);
        
        return String.format("%s - tipo: %s - valor: %s - alquiler: %s - jugadores: %s", sn, st, sp, sa, sj);
    }
}
