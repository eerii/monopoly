package monopoly;

import consola.Color;
import consola.excepciones.ComprarCasillaException;
import consola.excepciones.TratoException;
import monopoly.casilla.Casilla;
import monopoly.casilla.Propiedad;

public class Trato {
    private static int num_tratos = 1;
    private int id;
    private String jugadorPropone;
    private String jugadorRecibe;
    private String da1;
    private String da2;
    private String recibe1;
    private String recibe2;


    public Trato(String trato, String jugadorPropone) {

        this.jugadorPropone = jugadorPropone;
        String[] partes = trato.split(":");
        this.jugadorRecibe = partes[0];
        if(jugadorPropone.equals(jugadorRecibe))
            throw new RuntimeException("no puedes proponerte un trato a ti mismo");
        String line = partes[1].replace(" cambiar ","");
        String[] trade = line.split(",");
        String[] dar = trade[0].replace("(","").split(" y ");
        String[] recibir = trade[1].replace(")","").split(" y ");
        if(dar.length == 1){
            da1 = dar[0].replace(" ","");
            da2 = "";
        }
        else{
            da1 = dar[0].replace(" ","");
            da2 = dar[1].replace(" ","");
        }
        if(recibir.length == 1){
            recibe1 = recibir[0].replace(" ","");
            recibe2 = "";
        }
        else{
            recibe1 = recibir[0].replace(" ","");
            recibe2 = recibir[1].replace(" ","");
        }
        id = num_tratos++;
    }

    public String getJugadorRecibe() {
        return jugadorRecibe;
    }

    public int getId() {
        return id;
    }

    public void aceptar_trato() throws TratoException {
        Jugador jugador1 = Monopoly.get().buscar_jugador(jugadorPropone);
        Jugador jugador2 = Monopoly.get().buscar_jugador(jugadorRecibe);

        try {
            float dinero = Float.parseFloat(da1);
            if (dinero > jugador1.get_fortuna())
                throw new TratoException(
                        String.format("%s no dispone de suficiente dinero para aceptar el trato", jugadorPropone));
            jugador1.add_fortuna(dinero * -1f);
            jugador2.add_fortuna(dinero);
            if(!da2.isEmpty()){
                Casilla casilla = Monopoly.get().get_tablero().buscar_casilla(da2);
                if (!(casilla instanceof Propiedad propiedad))
                    throw new RuntimeException(String.format("%s no es una propiedad, no se puede aceptar el trato", da2));
                if(!jugador1.es_propietario(propiedad))
                    throw new RuntimeException(String.format("%s no es dueño de %s, no se puede aceptar el trato", jugadorPropone,da2));
                jugador1.remove_propiedad(propiedad);

                try {
                    jugador2.add_propiedad(propiedad, 0f);
                } catch (ComprarCasillaException e) {
                    System.out.println("Aviso: " + e.getMessage());
                }
            }
        } catch (NumberFormatException nfe) {
            Casilla casilla = Monopoly.get().get_tablero().buscar_casilla(da1);
            if (!(casilla instanceof Propiedad propiedad))
                throw new TratoException(String.format("%s no es una propiedad, no se puede aceptar el trato", da1));

            if (!jugador1.es_propietario(propiedad))
                throw new TratoException(String.format("%s no es dueño de %s, no se puede aceptar el trato", jugadorPropone, da1));
            jugador1.remove_propiedad(propiedad);

            try {
                jugador2.add_propiedad(propiedad, 0f);
            } catch (ComprarCasillaException e) {
                System.out.println("Aviso: " + e.getMessage());
            }

            if(!da2.isEmpty()){
                try{
                    float dinero = Float.parseFloat(da2);
                    if(dinero > jugador1.get_fortuna())
                        throw new RuntimeException(String.format("%s no dispone de suficiente dinero para aceptar el trato", jugadorPropone));
                    jugador1.add_fortuna(dinero * -1f);
                    jugador2.add_fortuna(dinero);
                } catch (NumberFormatException nfe2) {
                    casilla = Monopoly.get().get_tablero().buscar_casilla(da2);
                    if (!(casilla instanceof Propiedad))
                        throw new RuntimeException(String.format("%s no es una propiedad, no se puede aceptar el trato", da2));
                    propiedad = (Propiedad) casilla;
                    if(!jugador1.es_propietario(propiedad))
                        throw new RuntimeException(String.format("%s no es dueño de %s, no se puede aceptar el trato", jugadorPropone,da2));
                    jugador1.remove_propiedad(propiedad);
                    try {
                        jugador2.add_propiedad(propiedad, 0f);
                    } catch (ComprarCasillaException e) {
                        System.out.println("Aviso: " + e.getMessage());
                    }
                }
            }
        }
        // Ahora la otra parte del trato
        try {
            float dinero = Float.parseFloat(recibe1);
            if (dinero > jugador2.get_fortuna())
                throw new TratoException(
                        String.format("%s no dispone de suficiente dinero para aceptar el trato", jugadorRecibe));
            jugador1.add_fortuna(dinero);
            jugador2.add_fortuna(dinero * -1f);
            if(!recibe2.isEmpty()){
                Casilla casilla = Monopoly.get().get_tablero().buscar_casilla(recibe2);
                if (!(casilla instanceof Propiedad propiedad))
                    throw new TratoException(String.format("%s no es una propiedad, no se puede aceptar el trato", recibe2));
                if(!jugador2.es_propietario(propiedad))
                    throw new TratoException(String.format("%s no es dueño de %s, no se puede aceptar el trato", jugadorRecibe,recibe2));
                try {
                    jugador1.add_propiedad(propiedad, 0f);
                } catch (ComprarCasillaException e) {
                    System.out.println("Aviso: " + e.getMessage());
                }
                jugador2.remove_propiedad(propiedad);
            }
        } catch (NumberFormatException nfe) {
            Casilla casilla = Monopoly.get().get_tablero().buscar_casilla(recibe1);
            if (!(casilla instanceof Propiedad propiedad))
                throw new TratoException(String.format("%s no es una propiedad, no se puede aceptar el trato", recibe1));

            if (!jugador2.es_propietario(propiedad))
                throw new TratoException(
                        String.format("%s no es dueño de %s, no se puede aceptar el trato", jugadorRecibe, recibe1));
            jugador2.remove_propiedad(propiedad);

            try {
                jugador1.add_propiedad(propiedad, 0f);
            } catch (ComprarCasillaException e) {
                System.out.println("Aviso: " + e.getMessage());
            }

            if(!recibe2.isEmpty()){
                try{
                    float dinero = Float.parseFloat(recibe2);
                    if(dinero > jugador2.get_fortuna())
                        throw new TratoException(String.format("%s no dispone de suficiente dinero para aceptar el trato", jugadorRecibe));
                    jugador1.add_fortuna(dinero);
                    jugador2.add_fortuna(dinero*-1f);
                } catch (NumberFormatException nfe2) {
                    casilla = Monopoly.get().get_tablero().buscar_casilla(recibe2);
                    if (!(casilla instanceof Propiedad))
                        throw new TratoException(String.format("%s no es una propiedad, no se puede aceptar el trato", recibe2));
                    propiedad = (Propiedad) casilla;
                    if(!jugador2.es_propietario(propiedad))
                        throw new TratoException(String.format("%s no es dueño de %s, no se puede aceptar el trato", jugadorRecibe,recibe2));
                    try {
                        jugador1.add_propiedad(propiedad, 0f);
                    } catch (ComprarCasillaException e) {
                        System.out.println("Aviso: " + e.getMessage());
                    }
                    jugador2.remove_propiedad(propiedad);
                }
            }
        }

        if(recibe2.isEmpty() && da2.isEmpty())
            System.out.printf("has aceptado el siguiente trato con %s, le das %s y te da %s", jugadorPropone,recibe1,da1);
        else if(recibe2.isEmpty())
            System.out.printf("has aceptado el siguiente trato con %s, le das %s y te da %s y %s", jugadorPropone,recibe1,da1,da2);
        else if(da2.isEmpty())
            System.out.printf("has aceptado el siguiente trato con %s, le das %s y %s te da %s", jugadorPropone,recibe1,recibe2,da1);
        else
            System.out.printf("has aceptado el siguiente trato con %s, le das %s y %s y te da %s y %s", jugadorPropone,recibe1,recibe2,da1,da2);

    }
    @Override
    public String toString(){
        if(recibe2.isEmpty() && da2.isEmpty())
            return String.format("%s%s%s%s, te doy %s%s%s%s y me das %s%s%s%s\n",
                    Color.AZUL_CLARITO, Color.BOLD, jugadorRecibe, Color.RESET,
                    Color.AMARILLO,Color.BOLD,da1 , Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe1, Color.RESET);
        else if(recibe2.isEmpty())
            return String.format("%s%s%s%s, te doy %s%s%s%s y %s%s%s%s y me das %s%s%s%s\n",
                    Color.AZUL_CLARITO, Color.BOLD, jugadorRecibe, Color.RESET,
                    Color.AMARILLO,Color.BOLD, da1 , Color.RESET,
                    Color.AMARILLO, Color.BOLD, da2, Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe1, Color.RESET);
        else if(da2.isEmpty())
            return String.format("%s%s%s%s, te doy %s%s%s%s y me das %s%s%s%s y %s%s%s%s\n",
                    Color.AZUL_CLARITO, Color.BOLD, jugadorRecibe, Color.RESET,
                    Color.AMARILLO,Color.BOLD,da1 , Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe1, Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe2, Color.RESET);
        else
            return String.format("%s%s%s%s, te doy %s%s%s%s y %s%s%s%s y me das %s%s%s%s y %s%s%s%s\n",
                    Color.AZUL_CLARITO, Color.BOLD, jugadorRecibe, Color.RESET,
                    Color.AMARILLO,Color.BOLD,da1 , Color.RESET,
                    Color.AMARILLO, Color.BOLD, da2, Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe1, Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe2, Color.RESET);
    }

    public String representar(){
        if(recibe2.isEmpty() && da2.isEmpty())
            return String.format("id: %s%s%s%s\ntrato propuesto por: %s%s%s%s\ncambiar: %s%s%s%s por %s%s%s%s\n",
                    Color.AZUL_CLARITO, Color.BOLD, id, Color.RESET,
                    Color.AZUL_CLARITO, Color.BOLD, jugadorPropone, Color.RESET,
                    Color.AMARILLO,Color.BOLD,da1 , Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe1, Color.RESET);
        else if(recibe2.isEmpty())
            return String.format("id: %s%s%s%s\ntrato propuesto por: %s%s%s%s\ncambiar: %s%s%s%s y %s%s%s%s por %s%s%s%s\n",
                    Color.AZUL_CLARITO, Color.BOLD, id, Color.RESET,
                    Color.AZUL_CLARITO, Color.BOLD, jugadorPropone, Color.RESET,
                    Color.AMARILLO,Color.BOLD,da1 , Color.RESET,
                    Color.AMARILLO, Color.BOLD, da2, Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe1, Color.RESET);
        else if(da2.isEmpty())
            return String.format("id: %s%s%s%s\ntrato propuesto por: %s%s%s%s\ncambiar: %s%s%s%s por %s%s%s%s y %s%s%s%s\n",
                    Color.AZUL_CLARITO, Color.BOLD, id, Color.RESET,
                    Color.AZUL_CLARITO, Color.BOLD, jugadorPropone, Color.RESET,
                    Color.AMARILLO,Color.BOLD,da1 , Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe1, Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe2, Color.RESET);
        else
            return String.format("id %s%s%s%s\ntrato propuesto por: %s%s%s%s\ncambiar: %s%s%s%s y %s%s%s%s por %s%s%s%s y %s%s%s%s\n",
                    Color.AZUL_CLARITO, Color.BOLD, id, Color.RESET,
                    Color.AZUL_CLARITO, Color.BOLD, jugadorPropone, Color.RESET,
                    Color.AMARILLO,Color.BOLD,da1 , Color.RESET,
                    Color.AMARILLO, Color.BOLD, da2, Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe1, Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe2, Color.RESET);

    }

}
