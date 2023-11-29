package monopoly.casilla;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import consola.Color;
import consola.excepciones.ComprarEdificioException;
import monopoly.Jugador;
import monopoly.casilla.Edificio.TipoEdificio;

public class Solar extends Propiedad {
    float alquiler;
    Grupo grupo;
    List<Edificio> edificios;

    public Solar(TipoCasilla tipo, String nombre, float precio, Grupo grupo) {
        super(tipo, nombre);

        this.precio = precio;
        this.alquiler = (float) Math.floor(0.1f * precio);
        this.grupo = grupo;
        this.edificios = new ArrayList<>();
        grupo.add(this);
    }

    @Override
    public void set_precio(float precio) {
        super.set_precio(precio);
        this.alquiler = (float) Math.floor(0.1f * precio);
    }

    public List<Edificio> get_edificios() {
        return edificios;
    }

    @Override
    public Color get_color() {
        return grupo.get_color();
    }

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

    public Grupo get_grupo() {
        return grupo;
    }

    public int numero_edificios(TipoEdificio tipo) {
        return (int) edificios.stream().filter(e -> e.get_tipo() == tipo).count();
    }

    public int numero_edificios_grupo(TipoEdificio tipo) {
        int total = 0;
        List<Solar> casillas = this.get_grupo().get_casillas();
        for (Solar s : casillas) {
            for (Edificio e : s.get_edificios()) {
                if (e.get_tipo() == tipo)
                    total += 1;
            }
        }
        return total;
    }

    public void comprar_edificio(Jugador jugador, TipoEdificio tipo) throws ComprarEdificioException {
        int num = this.get_grupo().get_casillas().size();
        switch (tipo) {
            case CASA:
                if (numero_edificios_grupo(TipoEdificio.CASA) == num
                        && numero_edificios_grupo(TipoEdificio.HOTEL) == num)
                    throw new ComprarEdificioException("ya tienes el numero máximo de casas y hoteles permitidos en el grupo");
                if (numero_edificios(TipoEdificio.CASA) == 4)
                    throw new ComprarEdificioException("no se pueden comprar más casas, ya tienes 4");
                break;

            case HOTEL:
                if (numero_edificios_grupo(TipoEdificio.HOTEL) == num)
                    throw new ComprarEdificioException("ya tienes el maximo numero de hoteles permitidos en el grupo");
                if (numero_edificios(TipoEdificio.CASA) != 4)
                    throw new ComprarEdificioException("no se puede hacer un hotel, no tienes 4 casas");

                edificios = edificios.stream().filter(e -> e.get_tipo() != TipoEdificio.CASA)
                        .collect(Collectors.toList());
                break;

            case TERMAS:
                if (numero_edificios_grupo(TipoEdificio.TERMAS) == num)
                    throw new ComprarEdificioException("ya tienes el maximo numero de termas permitidos en el grupo");
                if (numero_edificios(TipoEdificio.HOTEL) < 1 && numero_edificios(TipoEdificio.CASA) < 2)
                    throw new ComprarEdificioException(
                            "no se puede comprar unas termas, necesitas al menos 2 casas y un hotel");
                break;

            case PABELLON:
                if (numero_edificios_grupo(TipoEdificio.PABELLON) == num)
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

    public void vender_edificio(Jugador jugador, TipoEdificio tipo) throws ComprarEdificioException {
        Edificio e = edificios.stream().filter(ed -> ed.get_tipo() == tipo).findFirst().orElse(null);
        if (e == null)
            throw new ComprarEdificioException(
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

    // String
    public String lista_edificios() {
        List<String> l = edificios.stream().map(e -> e.representar()).collect(Collectors.toList());
        return String.join("", l);
    }

    @Override
    public String toString() {
        String sn = String.format("%s%s%s%s", Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET);
        String st = String.format("%s%s%s%s", Color.VERDE, Color.BOLD, String.valueOf(tipo), Color.RESET);
        String sj = String.format("%s%s%s", Color.BOLD, lista_jugadores(), Color.RESET);
        String sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, precio, Color.RESET);
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
    }

    @Override
    public String toStringMini() {
        return String.format("%s %s", nombre, lista_edificios());
    }
}
