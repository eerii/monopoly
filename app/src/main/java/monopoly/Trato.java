package monopoly;

import consola.Color;
import monopoly.casilla.Casilla;
import monopoly.casilla.Propiedad;

public class Trato {
    static int num_tratos = 1;
    int id;
    String jugadorPropone;
    String jugadorRecibe;
    String da1;
    String recibe1;
    String num_turnos;

    public Trato(String trato, String jugadorPropone){
        id = num_tratos++;
        this.jugadorPropone = jugadorPropone;
        String[] partes = trato.split(" ");
        String nombreJugador = partes[0].replace(":","");
        this.jugadorRecibe = nombreJugador;
        if(!partes[1].equals("cambiar"))
            return;
        da1 = partes[2].replace(",", "").replace("(","");
        recibe1 = partes[3].replace(")","");
    }

    public String getJugadorRecibe() {
        return jugadorRecibe;
    }
    public void aceptar_trato(){
        Jugador jugador1 = Monopoly.get().buscar_jugador(jugadorPropone);
        Jugador jugador2 = Monopoly.get().buscar_jugador(jugadorRecibe);
        try {
            float dinero = Float.parseFloat(da1);
            if(dinero > jugador1.get_fortuna())
                throw new RuntimeException(String.format("%s no dispone de suficiente dinero para aceptar el trato", jugadorPropone));
            jugador1.add_fortuna(dinero * -1f);
            jugador2.add_fortuna(dinero);
        } catch (NumberFormatException nfe) {
            Casilla casilla1 = Monopoly.get().get_tablero().buscar_casilla(da1);
            if (!(casilla1 instanceof Propiedad))
                throw new RuntimeException(String.format("%s no es una propiedad, no se puede aceptar el trato", da1));
            Propiedad propiedad = (Propiedad) casilla1;
            
            if(!jugador1.es_propietario(propiedad))
                throw new RuntimeException(String.format("%s no es dueño de %s, no se puede aceptar el trato", jugadorPropone,da1));
            jugador1.remove_propiedad(propiedad);
            jugador2.add_propiedad(propiedad,0f);
        }
        try {
            float dinero = Float.parseFloat(recibe1);
            if(dinero > jugador2.get_fortuna())
                throw new RuntimeException(String.format("%s no dispone de suficiente dinero para aceptar el trato", jugadorRecibe));
            jugador1.add_fortuna(dinero);
            jugador2.add_fortuna(dinero * -1f);
        } catch (NumberFormatException nfe) {
            Casilla casilla2 = Monopoly.get().get_tablero().buscar_casilla(recibe1);
            if (!(casilla2 instanceof Propiedad))
                throw new RuntimeException(String.format("%s no es una propiedad, no se puede aceptar el trato", da1));
            Propiedad propiedad = (Propiedad) casilla2;
            
            if(!jugador2.es_propietario(propiedad))
                throw new RuntimeException(String.format("%s no es dueño de %s, no se puede aceptar el trato", jugadorRecibe,recibe1));
            jugador1.add_propiedad(propiedad,0);
            jugador2.remove_propiedad(propiedad);
        }
        System.out.printf("has aceptado el siguiente trato con %s, le das %s y te da %s", jugadorPropone,recibe1,da1);

    }
    @Override
    public String toString(){
        return String.format(
                "%s%s%s%s, te doy %s%s%s%s y me das %s%s%s%s\n",
                Color.AZUL_CLARITO, Color.BOLD, jugadorRecibe, Color.RESET,
                Color.AMARILLO,Color.BOLD,da1 , Color.RESET,
                Color.AMARILLO, Color.BOLD, recibe1, Color.RESET);
    }

    public String representar(){
        return String.format(
                "trato propuesto por: %s%s%s%s\ncambiar: %s%s%s%s por %s%s%s%s\n",
                Color.AZUL_CLARITO, Color.BOLD, jugadorRecibe, Color.RESET,
                Color.AMARILLO,Color.BOLD,da1 , Color.RESET,
                Color.AMARILLO, Color.BOLD, recibe1, Color.RESET);
    }

}
