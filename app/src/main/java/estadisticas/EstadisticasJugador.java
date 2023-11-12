package estadisticas;

import consola.Color;

public class EstadisticasJugador {
    float dinero_invertido = 0;
    float pago_tasa_impuestos = 0;
    float pago_alquileres = 0;
    float cobro_alquileres = 0;
    float pasar_salida = 0;
    float premios = 0;
    int veces_carcel = 0;

    public void sumar_dinero_invertido(float valor){
        dinero_invertido += valor;
    }

    public void sumar_pago_tasa_impuestos(float valor){
        pago_tasa_impuestos += valor;
    }

    public void sumar_pago_alquileres(float valor){
        pago_alquileres += valor;
    }

    public void sumar_cobro_alquileres(float valor){
        cobro_alquileres += valor;
    }

    public void sumar_premios(float valor){
        premios += valor;
    }

    public void summar_pasar_salida(float valor){
        pasar_salida += valor;
    }

    public void sumar_veces_carcel() {
        veces_carcel += 1;
    }

    @Override
    public String toString(){
        String s1 = String.format("%s%sdinero invertido%s: %s%.2f%s",Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, dinero_invertido, Color.RESET);
        String s2 = String.format("%s%spago de tasas e impuestos%s: %s%.2f%s",Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, pago_tasa_impuestos, Color.RESET);
        String s3 = String.format("%s%spago de alquileres%s: %s%.2f%s",Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, pago_alquileres, Color.RESET);
        String s4 = String.format("%s%scobro de alquileres%s: %s%.2f%s",Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, cobro_alquileres, Color.RESET);
        String s5 = String.format("%s%sdinero reclamado en la salida%s: %s%.2f%s",Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, pasar_salida, Color.RESET);
        String s6 = String.format("%s%spremio de inversiones o bote%s: %s%.2f%s",Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, premios, Color.RESET);
        String s7 = String.format("%s%snumero de veces en la carcel%s: %s%d%s",Color.AZUL_CLARITO, Color.BOLD, Color.RESET,
                Color.BOLD, veces_carcel, Color.RESET);
        return String.format("%s,\n%s,\n%s,\n%s,\n%s,\n%s,\n%s\n",s1,s2,s3,s4,s5,s6,s7);
    }

}
