package monopoly;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import consola.Color;
import monopoly.Edificio.TipoEdificio;

public class Solar extends CasillaComprable {
    float alquiler;
    Grupo grupo;
    List<Edificio> edificios;

    public Solar(TipoCasilla tipo, String nombre, float precio, Grupo grupo) {
        super(tipo, nombre);

        this.precio = precio;
        this.alquiler = (float)Math.floor(0.1f * precio);
        this.grupo = grupo;
        grupo.add(this);

        this.edificios = new ArrayList<>();
    }
    public float get_alquiler() {
        return alquiler;
    }
    @Override
    public void set_precio(float precio) {
        super.set_precio(precio);
        this.alquiler = (float)Math.floor(0.1f * precio);
    }
    @Override
    public Color get_color() {
        return grupo != null ? grupo.get_color() : Color.NONE;
    }

    public Grupo get_grupo() {
        return grupo;
    }

    public int numero_edificios(TipoEdificio tipo) {
        return (int)edificios.stream().filter(e -> e.get_tipo() == tipo).count();
    }

    public void comprar_edificio(Jugador jugador, TipoEdificio tipo) {
        switch (tipo) {
            case CASA:
                if (numero_edificios(TipoEdificio.CASA) == 4)
                    throw new RuntimeException("no se pueden comprar más casas, ya tienes 4");
                
                break;

            case HOTEL:
                if (numero_edificios(TipoEdificio.CASA) != 4)
                    throw new RuntimeException("no se puede hacer un hotel, no tienes 4 casas");

                edificios = edificios.stream().filter(e -> e.get_tipo() == TipoEdificio.CASA).collect(Collectors.toList());

                break;

            case TERMAS:
                if (numero_edificios(TipoEdificio.HOTEL) < 1 && numero_edificios(TipoEdificio.CASA) < 2)
                    throw new RuntimeException("no se puede comprar unas termas, necesitas al menos 2 casas y un hotel");

                break;

            case PABELLON:
                if (numero_edificios(TipoEdificio.HOTEL) < 2)
                    throw new RuntimeException("no se puede comprar un pabellón, necesitas al menos 2 hoteles");
        }

        // TODO: Límite de edificios por grupo

        edificios.add(new Edificio(tipo));
    }

    // TODO: Vender edificios

    // TODO: Lista de precios de cada combinación de edificos

    @Override
    public String toString() {
        String sn = String.format("%s%s%s%s", Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET);
        String st = String.format("%s%s%s%s", Color.VERDE, Color.BOLD, String.valueOf(tipo), Color.RESET);
        String sj = String.format("%s%s%s", Color.BOLD, lista_jugadores(), Color.RESET);

        Jugador jp = get_propietario();
        String sg = String.format("%s%s%s%s", String.valueOf(grupo.get_color()), Color.BOLD, grupo.get_nombre(), Color.RESET);
        String sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, precio, Color.RESET);
        String sa = String.format("%s%s%.0f%s", Color.ROJO, Color.BOLD, alquiler, Color.RESET);
        String sjp = jp != null ? String.format("%s%s%s%s", Color.AZUL_CLARITO, Color.BOLD, this.get_propietario().get_nombre(), Color.RESET) : ""; 
        
        return String.format("%s - tipo: %s - propietario: %s - grupo: %s - valor: %s - alquiler: %s - jugadores: %s", sn, st, sjp, sg, sp, sa, sj);
    }
}
