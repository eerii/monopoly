package monopoly;

import consola.Color;

import java.util.stream.Collectors;

public class EstadisticasJugador {
    float dineroInvertido = 0;
    float pagoTasaseImpuestos = 0;
    float pagoAlquileres = 0;
    float cobroAlquileres = 0;
    float pasarPorSalida = 0;
    float premiosInversionesOBote = 0;
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

    public void add_premioInversionesOBote(float valor){
        premiosInversionesOBote = premiosInversionesOBote + valor;
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
        String s1 = String.format("%s%sdinero Invertido%s: %s%.2f%s",Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, dineroInvertido, Color.RESET);
        String s2 = String.format("%s%spago de Tasas e Impuestos%s: %s%.2f%s",Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, pagoTasaseImpuestos, Color.RESET);
        String s3 = String.format("%s%spago de alquileres%s: %s%.2f%s",Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, pagoAlquileres, Color.RESET);
        String s4 = String.format("%s%scobro de alquileres%s: %s%.2f%s",Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, cobroAlquileres, Color.RESET);
        String s5 = String.format("%s%sdinero reclamado en la salida%s: %s%.2f%s",Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, pasarPorSalida, Color.RESET);
        String s6 = String.format("%s%spremio de inversiones o bote%s: %s%.2f%s",Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, premiosInversionesOBote, Color.RESET);
        String s7 = String.format("%s%snumero de veces en la carcel%s: %s%d%s",Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, vecesEnLaCarcel, Color.RESET);
        return String.format("%s,\n%s,\n%s,\n%s,\n%s,\n%s,\n%s\n",s1,s2,s3,s4,s5,s6,s7);
    }

}
