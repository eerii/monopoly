package monopoly.casilla;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import consola.Color;
import consola.excepciones.ComprarEdificioException;
import monopoly.Jugador;
import monopoly.casilla.edificio.*;

public class Solar extends Propiedad {
    private float alquiler;
    private Grupo grupo;
    private List<Edificio> edificios;

    public Solar(TipoCasilla tipo, String nombre, float precio, Grupo grupo) {
        super(tipo, nombre);
        super.set_precio(precio);
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
        int casas = numero_casas();
        float alquiler = this.alquiler;

        for (Edificio e : edificios) {
            if(e instanceof Casa)
                    alquiler += this.alquiler * alquileres[casas-1];
            else if (e instanceof Hotel)
                    alquiler += this.alquiler * 70;
            else if (e instanceof Termas || e instanceof Pabellon)
                    alquiler += this.alquiler * 25;
        }

        return alquiler;
    }

    public Grupo get_grupo() {
        return grupo;
    }

    public int numero_casas() {
        int contador = 0;
        for (Edificio e: edificios) {
            if (e instanceof Casa)
                contador ++;
        }
        System.out.format("numero de casas: %d\n",contador);
        return contador;
    }

    public int numero_hoteles() {
        int contador = 0;
        for (Edificio e: edificios) {
            if (e instanceof Hotel)
                contador ++;
        }
        return contador;
    }

    public int numero_pabellones() {
        int contador = 0;
        for (Edificio e: edificios) {
            if (e instanceof Pabellon)
                contador ++;
        }
        return contador;
    }

    public int numero_termas() {
        int contador = 0;
        for (Edificio e: edificios) {
            if (e instanceof Termas)
                contador ++;
        }
        return contador;
    }

    public int numero_casas_grupo() {
        int total = 0;
        List<Solar> casillas = this.get_grupo().get_casillas();
        for (Solar s : casillas) {
            for (Edificio e : s.get_edificios()) {
                if (e instanceof Casa)
                    total += 1;
            }
        }
        return total;
    }

    public int numero_hoteles_grupo() {
        int total = 0;
        List<Solar> casillas = this.get_grupo().get_casillas();
        for (Solar s : casillas) {
            for (Edificio e : s.get_edificios()) {
                if (e instanceof Hotel)
                    total += 1;
            }
        }
        return total;
    }

    public int numero_termas_grupo() {
        int total = 0;
        List<Solar> casillas = this.get_grupo().get_casillas();
        for (Solar s : casillas) {
            for (Edificio e : s.get_edificios()) {
                if (e instanceof Termas)
                    total += 1;
            }
        }
        return total;
    }

    public int numero_pabellones_grupo() {
        int total = 0;
        List<Solar> casillas = this.get_grupo().get_casillas();
        for (Solar s : casillas) {
            for (Edificio e : s.get_edificios()) {
                if (e instanceof Pabellon)
                    total += 1;
            }
        }
        return total;
    }

    public void comprar_casa(Jugador jugador) throws ComprarEdificioException {
        int num = this.get_grupo().get_casillas().size();
            if (numero_casas_grupo() == num && numero_hoteles_grupo() == num)
                throw new ComprarEdificioException("ya tienes el numero máximo de casas y hoteles permitidos en el grupo");
            if (numero_casas() == 4)
                throw new ComprarEdificioException("no se pueden comprar más casas, ya tienes 4");
            Casa c = new Casa(this);
            float coste = c.coste();

        if (jugador.get_fortuna() < coste)
            throw new ComprarEdificioException(String.format("no se puede comprar una casa por %.0f", coste));

        jugador.add_fortuna(coste * -1.f);
        edificios.add(c);

        System.out.format(
                "el jugador %s%s%s%s compra una casa en la casilla %s%s%s%s por %s. Su fortuna actual es de %s%s%.0f%s\n",
                Color.ROJO, Color.BOLD, jugador.get_nombre(), Color.RESET,
                Color.AZUL_OSCURO, Color.BOLD, this.get_nombre(), Color.RESET, coste,
                Color.ROSA, Color.BOLD, jugador.get_fortuna(), Color.RESET);
    }

    public void comprar_hotel(Jugador jugador) throws ComprarEdificioException {
        int num = this.get_grupo().get_casillas().size();
        if (numero_hoteles_grupo() == num)
            throw new ComprarEdificioException("ya tienes el maximo numero de hoteles permitidos en el grupo");
        if (numero_casas() != 4)
            throw new ComprarEdificioException("no se puede hacer un hotel, no tienes 4 casas");

        List<Edificio> lista = new ArrayList<>();
        for (Edificio e : edificios) {
            if (!(e instanceof Casa))
                lista.add(e);
        }
        edificios = lista;
        Hotel h = new Hotel(this);
        float coste = h.coste();

        if (jugador.get_fortuna() < coste)
            throw new ComprarEdificioException(String.format("no se puede comprar un hotel por %.0f", coste));

        jugador.add_fortuna(coste * -1.f);
        edificios.add(h);

        System.out.format(
                "el jugador %s%s%s%s compra un hotel en la casilla %s%s%s%s por %s. Su fortuna actual es de %s%s%.0f%s\n",
                Color.ROJO, Color.BOLD, jugador.get_nombre(), Color.RESET,
                Color.AZUL_OSCURO, Color.BOLD, this.get_nombre(), Color.RESET, coste,
                Color.ROSA, Color.BOLD, jugador.get_fortuna(), Color.RESET);
    }

    public void comprar_termas(Jugador jugador) throws ComprarEdificioException {
        int num = this.get_grupo().get_casillas().size();
        if (numero_termas_grupo() == num)
            throw new ComprarEdificioException("ya tienes el maximo numero de termas permitidos en el grupo");
        if (numero_hoteles() < 1 && numero_casas() < 2)
            throw new ComprarEdificioException(
                    "no se puede comprar unas termas, necesitas al menos 2 casas y un hotel");
        Termas t = new Termas(this);
        float coste = t.coste();

        if (jugador.get_fortuna() < coste)
            throw new ComprarEdificioException(String.format("no se puede comprar unas termas por %.0f", coste));

        jugador.add_fortuna(coste * -1.f);
        edificios.add(t);

        System.out.format(
                "el jugador %s%s%s%s compra unas termas en la casilla %s%s%s%s por %s. Su fortuna actual es de %s%s%.0f%s\n",
                Color.ROJO, Color.BOLD, jugador.get_nombre(), Color.RESET,
                Color.AZUL_OSCURO, Color.BOLD, this.get_nombre(), Color.RESET, coste,
                Color.ROSA, Color.BOLD, jugador.get_fortuna(), Color.RESET);
    }

    public void comprar_pabellon(Jugador jugador) throws ComprarEdificioException {
        int num = this.get_grupo().get_casillas().size();
        if (numero_pabellones_grupo() == num)
            throw new ComprarEdificioException("ya tienes el maximo numero de pabellones permitidos en el grupo");
        if (numero_hoteles() < 2)
            throw new ComprarEdificioException("no se puede comprar un pabellón, necesitas al menos 2 hoteles");
        Pabellon p = new Pabellon(this);
        float coste = p.coste();

        if (jugador.get_fortuna() < coste)
            throw new ComprarEdificioException(String.format("no se puede comprar un pabellón por %.0f", coste));

        jugador.add_fortuna(coste * -1.f);
        edificios.add(p);

        System.out.format(
                "el jugador %s%s%s%s compra un pabellón en la casilla %s%s%s%s por %s. Su fortuna actual es de %s%s%.0f%s\n",
                Color.ROJO, Color.BOLD, jugador.get_nombre(), Color.RESET,
                Color.AZUL_OSCURO, Color.BOLD, this.get_nombre(), Color.RESET, coste,
                Color.ROSA, Color.BOLD, jugador.get_fortuna(), Color.RESET);
    }

    public void vender_casa(Jugador jugador) throws ComprarEdificioException {
        Edificio e = edificios.stream().filter(ed -> ed instanceof Casa).findFirst().orElse(null);
        if (e == null)
            throw new ComprarEdificioException("no se puede vender una casa, no tienes ninguna");

        float coste = (float) Math.floor(e.coste() * 0.8f);
        edificios.remove(e);
        jugador.add_fortuna(coste);
        System.out.format(
                "el jugador %s%s%s%s vende una casa en la casilla %s%s%s%s por %s. Su fortuna actual es de %s%s%.0f%s\n",
                Color.ROJO, Color.BOLD, jugador.get_nombre(), Color.RESET,
                Color.AZUL_OSCURO, Color.BOLD, this.get_nombre(), Color.RESET,
                coste,
                Color.ROSA, Color.BOLD, jugador.get_fortuna(), Color.RESET);
    }
    public void vender_hotel(Jugador jugador) throws ComprarEdificioException {
        Edificio e = edificios.stream().filter(ed -> ed instanceof Hotel).findFirst().orElse(null);
        if (e == null)
            throw new ComprarEdificioException("no se puede vender un hotel, no tienes ninguno");

        float coste = (float) Math.floor(e.coste() * 0.8f);
        edificios.remove(e);
        jugador.add_fortuna(coste);
        System.out.format(
                "el jugador %s%s%s%s vende un hotel en la casilla %s%s%s%s por %s. Su fortuna actual es de %s%s%.0f%s\n",
                Color.ROJO, Color.BOLD, jugador.get_nombre(), Color.RESET,
                Color.AZUL_OSCURO, Color.BOLD, this.get_nombre(), Color.RESET,
                coste,
                Color.ROSA, Color.BOLD, jugador.get_fortuna(), Color.RESET);
    }

    public void vender_termas(Jugador jugador) throws ComprarEdificioException {
        Edificio e = edificios.stream().filter(ed -> ed instanceof Termas).findFirst().orElse(null);
        if (e == null)
            throw new ComprarEdificioException("no se puede vender unas termas, no tienes ninguna");

        float coste = (float) Math.floor(e.coste() * 0.8f);
        edificios.remove(e);
        jugador.add_fortuna(coste);
        System.out.format(
                "el jugador %s%s%s%s vende unas termas en la casilla %s%s%s%s por %s. Su fortuna actual es de %s%s%.0f%s\n",
                Color.ROJO, Color.BOLD, jugador.get_nombre(), Color.RESET,
                Color.AZUL_OSCURO, Color.BOLD, this.get_nombre(), Color.RESET,
                coste,
                Color.ROSA, Color.BOLD, jugador.get_fortuna(), Color.RESET);
    }

    public void vender_pabellon(Jugador jugador) throws ComprarEdificioException {
        Edificio e = edificios.stream().filter(ed -> ed instanceof Pabellon).findFirst().orElse(null);
        if (e == null)
            throw new ComprarEdificioException("no se puede vender un pabellón, no tienes ninguno");

        float coste = (float) Math.floor(e.coste() * 0.8f);
        edificios.remove(e);
        jugador.add_fortuna(coste);
        System.out.format(
                "el jugador %s%s%s%s vende un pabellón en la casilla %s%s%s%s por %s. Su fortuna actual es de %s%s%.0f%s\n",
                Color.ROJO, Color.BOLD, jugador.get_nombre(), Color.RESET,
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
        String sn = String.format("%s%s%s%s", Color.AZUL_CLARITO, Color.BOLD, this.get_nombre(), Color.RESET);
        String st = String.format("%s%s%s%s", Color.VERDE, Color.BOLD, String.valueOf(this.get_tipo()), Color.RESET);
        String sj = String.format("%s%s%s", Color.BOLD, lista_jugadores(), Color.RESET);
        String sp = String.format("%s%s%.0f%s", Color.AMARILLO, Color.BOLD, this.get_precio(), Color.RESET);
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
        return String.format("%s %s", this.get_nombre(), lista_edificios());
    }
}
