package monopoly;

import consola.Color;

import java.util.stream.Collectors;

public class EstadisticasJugador {
    float dineroInvertido = 0;
    float pagoTasaseImpuestos = 0;
    float pagoAlquileres = 0;
    float cobroAlquileres = 0;
    float pasarPorSalida = 0;
    int vecesEnLaCarcel = 0;

    public void add_dineroInvertido(float valor){
        dineroInvertido = dineroInvertido + valor;
    }

    public void add_pagoTasaseImpuestos(float valor){
        pagoTasaseImpuestos = pagoTasaseImpuestos + valor;
    }

    public void add_pagoAlquileres(float valor){
        pagoAlquileres = pagoAlquileres + valor;
    }

    public void add_cobroAlquileres(float valor){
        cobroAlquileres = cobroAlquileres + valor;
    }

    public void add_pasarPorSalida(float valor){
        pasarPorSalida = pasarPorSalida + valor;
    }

    public void sumar_vecesEnLaCarcel()
    {
        vecesEnLaCarcel = vecesEnLaCarcel + 1;
    }

    @Override
    public String toString(){
        String s1 = String.format("%s%sDinero Invertido%s: %s%f%s\n",Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, dineroInvertido, Color.RESET);
        String s2 = String.format("%s%sPago de Tasas e Impuestos%s: %s%f%s\n",Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, pagoTasaseImpuestos, Color.RESET);
        String s3 = String.format("%s%sPago de alquileres%s: %s%f%s\n",Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, pagoAlquileres, Color.RESET);
        String s4 = String.format("%s%sCobro de alquileres%s: %s%f%s\n",Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, cobroAlquileres, Color.RESET);
        String s5 = String.format("%s%sDinero reclamado en la salida%s: %s%f%s\n",Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, pasarPorSalida, Color.RESET);
        String s6 = String.format("%s%sNumero de veces en la carcel%s: %s%d%s\n",Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, dineroInvertido, Color.RESET);
        return String.format(
                "%s%s%s%s: %s%s%f%s\n " ,
                Color.AZUL_CLARITO, Color.BOLD, nombre, Color.RESET,
                Color.BOLD, representar(), Color.RESET,
                avatar.get_tipo(),
                Color.AMARILLO, Color.BOLD, fortuna, Color.RESET,
                propiedades.stream().map(p -> p.toStringMini()).collect(Collectors.toList()),
                hipotecas.stream().map(h -> h.get_nombre()).collect(Collectors.toList()),
                this.get_edificios().stream().map(e -> e.representar()).collect(Collectors.toList())
        );
    }
}
