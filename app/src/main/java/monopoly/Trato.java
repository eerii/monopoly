package monopoly;

import consola.Color;
import consola.excepciones.ComprarCasillaException;
import consola.excepciones.TratoException;
import monopoly.casilla.Casilla;

import monopoly.casilla.propiedad.Propiedad;
import monopoly.casilla.propiedad.Solar;

public class Trato {
    // ···········
    // Propiedades
    // ···········

    private static int num_tratos = 1;
    private int id;
    private String jugadorPropone;
    private String jugadorRecibe;
    private String da1;
    private String da2;
    private String recibe1;
    private String recibe2;
    private String casillaAlquiler;
    private int turnosAlquiler = 0;

    // ·············
    // Constructores
    // ·············

    public Trato(String trato, String jugadorPropone) {

        String[] dar = new String[0], recibir = new String[0], auxiliar;
        this.jugadorPropone = jugadorPropone;
        String[] partes = trato.split(":");
        this.jugadorRecibe = partes[0];
        if (jugadorPropone.equals(jugadorRecibe))
            throw new RuntimeException("no puedes proponerte un trato a ti mismo");
        String line = partes[1].replace(" cambiar ", "");
        String[] trade = line.split(",");
        if (trade.length == 2) {
            dar = trade[0].replace("(", "").split(" y ");
            recibir = trade[1].replace(")", "").split(" y ");
        } else if (trade.length == 3) {
            dar = trade[0].replace("(", "").split(" y ");
            auxiliar = trade[1].replace(")", "").split(" y noalquiler ");
            recibir = auxiliar[0].split(" y ");
            casillaAlquiler = auxiliar[1].replace("(", "");
            turnosAlquiler = Integer.parseInt(trade[2].replace(" ", "").replace(")", ""));
        }

        if (dar.length == 1) {
            da1 = dar[0].replace(" ", "");
            da2 = "";
        } else {
            da1 = dar[0].replace(" ", "");
            da2 = dar[1].replace(" ", "");
        }
        if (recibir.length == 1) {
            recibe1 = recibir[0].replace(" ", "");
            recibe2 = "";
        } else {
            recibe1 = recibir[0].replace(" ", "");
            recibe2 = recibir[1].replace(" ", "");
        }
        id = num_tratos++;
    }

    // ·······
    // Getters
    // ·······

    public String getJugadorRecibe() {
        return jugadorRecibe;
    }

    public int getId() {
        return id;
    }

    // ················
    // Interfaz pública
    // ················

    public void aceptar_trato() throws TratoException {
        Jugador jugador1 = Monopoly.get().buscar_jugador(jugadorPropone);
        if (jugador1 == null)
            throw new TratoException(String.format("%s no existe, no se puede aceptar el trato", jugadorPropone));
        Jugador jugador2 = Monopoly.get().buscar_jugador(jugadorRecibe);
        if (jugador2 == null)
            throw new TratoException(String.format("%s no existe, no se puede aceptar el trato", jugadorRecibe));
        float dineroDa = 0f, dineroRecibe = 0f;
        Propiedad propiedadDa1 = null, propiedadDa2 = null, propiedadRecibe1 = null, propiedadRecibe2 = null;
        try {
            dineroDa = Float.parseFloat(da1);
            if (dineroDa > jugador1.get_fortuna())
                throw new TratoException(
                        String.format("%s no dispone de suficiente dinero para aceptar el trato", jugadorPropone));
            if (!da2.isEmpty()) {
                Casilla casilla = Monopoly.get().get_tablero().buscar_casilla(da2);
                if (!(casilla instanceof Propiedad))
                    throw new RuntimeException(String.format("%s no es una propiedad, no se puede aceptar el trato", da2));
                propiedadDa1 = (Propiedad) casilla;
                if (!jugador1.es_propietario(propiedadDa1))
                    throw new RuntimeException(String.format("%s no es dueño de %s, no se puede aceptar el trato", jugadorPropone, da2));
            }

        } catch (NumberFormatException nfe) {
            Casilla casilla = Monopoly.get().get_tablero().buscar_casilla(da1);
            if (!(casilla instanceof Propiedad))
                throw new TratoException(String.format("%s no es una propiedad, no se puede aceptar el trato", da1));
            propiedadDa1 = (Propiedad) casilla;
            if (!jugador1.es_propietario(propiedadDa1))
                throw new TratoException(String.format("%s no es dueño de %s, no se puede aceptar el trato", jugadorPropone, da1));


            if (!da2.isEmpty()) {
                try {
                    dineroDa = Float.parseFloat(da2);
                    if (dineroDa > jugador1.get_fortuna())
                        throw new RuntimeException(String.format("%s no dispone de suficiente dinero para aceptar el trato", jugadorPropone));

                } catch (NumberFormatException nfe2) {
                    casilla = Monopoly.get().get_tablero().buscar_casilla(da2);
                    if (!(casilla instanceof Propiedad))
                        throw new RuntimeException(String.format("%s no es una propiedad, no se puede aceptar el trato", da2));
                    propiedadDa2 = (Propiedad) casilla;
                    if (!jugador1.es_propietario(propiedadDa2))
                        throw new RuntimeException(String.format("%s no es dueño de %s, no se puede aceptar el trato", jugadorPropone, da2));

                }
            }
        }
        // Ahora la otra parte del trato
        try {
            dineroRecibe = Float.parseFloat(recibe1);
            if (dineroRecibe > jugador2.get_fortuna())
                throw new TratoException(
                        String.format("%s no dispone de suficiente dinero para aceptar el trato", jugadorRecibe));

            if (!recibe2.isEmpty()) {
                Casilla casilla = Monopoly.get().get_tablero().buscar_casilla(recibe2);
                if (!(casilla instanceof Propiedad))
                    throw new TratoException(String.format("%s no es una propiedad, no se puede aceptar el trato", recibe2));
                propiedadRecibe1= (Propiedad) casilla;
                if (!jugador2.es_propietario(propiedadRecibe1))
                    throw new TratoException(String.format("%s no es dueño de %s, no se puede aceptar el trato", jugadorRecibe, recibe2));

            }
        } catch (NumberFormatException nfe) {
            Casilla casilla = Monopoly.get().get_tablero().buscar_casilla(recibe1);
            if (!(casilla instanceof Propiedad))
                throw new TratoException(String.format("%s no es una propiedad, no se puede aceptar el trato", recibe1));
            propiedadRecibe1 = (Propiedad) casilla;
            if (!jugador2.es_propietario(propiedadRecibe1))
                throw new TratoException(
                        String.format("%s no es dueño de %s, no se puede aceptar el trato", jugadorRecibe, recibe1));

            if (!recibe2.isEmpty()) {
                try {
                    dineroRecibe = Float.parseFloat(recibe2);
                    if (dineroRecibe > jugador2.get_fortuna())
                        throw new TratoException(String.format("%s no dispone de suficiente dinero para aceptar el trato", jugadorRecibe));

                } catch (NumberFormatException nfe2) {
                    casilla = Monopoly.get().get_tablero().buscar_casilla(recibe2);
                    if (!(casilla instanceof Propiedad))
                        throw new TratoException(String.format("%s no es una propiedad, no se puede aceptar el trato", recibe2));
                    propiedadRecibe2 = (Propiedad) casilla;
                    if (!jugador2.es_propietario(propiedadRecibe2))
                        throw new TratoException(String.format("%s no es dueño de %s, no se puede aceptar el trato", jugadorRecibe, recibe2));

                }
            }
        }
        jugador1.add_fortuna(dineroDa * -1f);
        jugador2.add_fortuna(dineroDa);

        if(propiedadDa1!=null){
            jugador1.remove_propiedad(propiedadDa1);
            try {
                jugador2.add_propiedad(propiedadDa1, 0f);
            } catch (ComprarCasillaException e) {
                System.out.println("Aviso: " + e.getMessage());
            }
        }

        if(propiedadDa2!=null)
        {
            jugador1.remove_propiedad(propiedadDa2);
            try {
                jugador2.add_propiedad(propiedadDa2, 0f);
            } catch (ComprarCasillaException e) {
                System.out.println("Aviso: " + e.getMessage());
            }
        }

        jugador1.add_fortuna(dineroRecibe);
        jugador2.add_fortuna(dineroRecibe * -1f);

        if(propiedadRecibe1!=null)
        {
            try {
                jugador1.add_propiedad(propiedadRecibe1, 0f);
            } catch (ComprarCasillaException e) {
                System.out.println("Aviso: " + e.getMessage());
            }
            jugador2.remove_propiedad(propiedadRecibe1);
        }

        if(propiedadRecibe2!=null)
        {
            jugador2.remove_propiedad(propiedadRecibe2);
            try {
                jugador1.add_propiedad(propiedadRecibe2, 0f);
            } catch (ComprarCasillaException e) {
                System.out.println("Aviso: " + e.getMessage());
            }
        }



        //Ahora trataremos la parte de no pagar alquiler
        if (turnosAlquiler != 0) {
            Casilla casilla = Monopoly.get().get_tablero().buscar_casilla(casillaAlquiler);
            if (!(casilla instanceof Solar alquiler))
                throw new TratoException(String.format("%s no es una propiedad, no se puede aceptar el trato", casillaAlquiler));
            if (!jugador2.es_propietario(alquiler))
                throw new TratoException(String.format("%s no es dueño de %s, no se puede aceptar el trato", jugadorRecibe, casillaAlquiler));
            alquiler.add_noAlquiler(jugador1, turnosAlquiler);
        }





        //Ahora trataremos la parte de no pagar alquiler
        if (turnosAlquiler != 0) {
            Casilla casilla = Monopoly.get().get_tablero().buscar_casilla(casillaAlquiler);
            if (!(casilla instanceof Solar alquiler))
                throw new TratoException(String.format("%s no es una propiedad, no se puede aceptar el trato", casillaAlquiler));
            if (!jugador2.es_propietario(alquiler))
                throw new TratoException(String.format("%s no es dueño de %s, no se puede aceptar el trato", jugadorRecibe, casillaAlquiler));
            alquiler.add_noAlquiler(jugador1, turnosAlquiler);
        }



        if (recibe2.isEmpty() && da2.isEmpty())
            System.out.printf("has aceptado el siguiente trato con %s, le das %s y te da %s", jugadorPropone, recibe1,
                    da1);
        else if (recibe2.isEmpty())
            System.out.printf("has aceptado el siguiente trato con %s, le das %s y te da %s y %s", jugadorPropone,
                    recibe1, da1, da2);
        else if (da2.isEmpty())
            System.out.printf("has aceptado el siguiente trato con %s, le das %s y %s te da %s", jugadorPropone,
                    recibe1, recibe2, da1);
        else
            System.out.printf("has aceptado el siguiente trato con %s, le das %s y %s y te da %s y %s", jugadorPropone,recibe1,recibe2,da1,da2);
        if(turnosAlquiler!=0)
            System.out.printf(" y no paga alquiler en %s durante %d turnos", casillaAlquiler, turnosAlquiler);
    }

    @Override
    public String toString(){
        String alquiler = "";
        if(turnosAlquiler!=0)
        {
            alquiler = " y no pago alquiler en " + casillaAlquiler + " durante " + turnosAlquiler + " turnos";
        }
        if(recibe2.isEmpty() && da2.isEmpty())
            return String.format("%s%s%s%s, te doy %s%s%s%s y me das %s%s%s%s",
                    Color.AZUL_CLARITO, Color.BOLD, jugadorRecibe, Color.RESET,
                    Color.AMARILLO,Color.BOLD,da1 , Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe1, Color.RESET) + alquiler + "\n";
        else if(recibe2.isEmpty())
            return String.format("%s%s%s%s, te doy %s%s%s%s y %s%s%s%s y me das %s%s%s%s",
                    Color.AZUL_CLARITO, Color.BOLD, jugadorRecibe, Color.RESET,
                    Color.AMARILLO, Color.BOLD, da1, Color.RESET,
                    Color.AMARILLO, Color.BOLD, da2, Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe1, Color.RESET) + alquiler + "\n";
        else if(da2.isEmpty())
            return String.format("%s%s%s%s, te doy %s%s%s%s y me das %s%s%s%s y %s%s%s%s",
                    Color.AZUL_CLARITO, Color.BOLD, jugadorRecibe, Color.RESET,
                    Color.AMARILLO, Color.BOLD, da1, Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe1, Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe2, Color.RESET) + alquiler + "\n";
        else
            return String.format("%s%s%s%s, te doy %s%s%s%s y %s%s%s%s y me das %s%s%s%s y %s%s%s%s",
                    Color.AZUL_CLARITO, Color.BOLD, jugadorRecibe, Color.RESET,
                    Color.AMARILLO, Color.BOLD, da1, Color.RESET,
                    Color.AMARILLO, Color.BOLD, da2, Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe1, Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe2, Color.RESET) + alquiler + "\n";
    }

    public String representar(){
        String alquiler = "";
        if(turnosAlquiler!=0)
            alquiler = ", y " + jugadorPropone + " no paga alquiler en " + casillaAlquiler + " durante " + turnosAlquiler + " turnos";
        if(recibe2.isEmpty() && da2.isEmpty())
            return String.format("id: %s%s%s%s\ntrato propuesto por: %s%s%s%s\ncambiar: obtienes %s%s%s%s por %s%s%s%s",
                    Color.AZUL_CLARITO, Color.BOLD, id, Color.RESET,
                    Color.AZUL_CLARITO, Color.BOLD, jugadorPropone, Color.RESET,
                    Color.AMARILLO,Color.BOLD,da1 , Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe1, Color.RESET) + alquiler + "\n";
        else if(recibe2.isEmpty())
            return String.format("id: %s%s%s%s\ntrato propuesto por: %s%s%s%s\ncambiar: obtienes %s%s%s%s y %s%s%s%s por %s%s%s%s",
                    Color.AZUL_CLARITO, Color.BOLD, id, Color.RESET,
                    Color.AZUL_CLARITO, Color.BOLD, jugadorPropone, Color.RESET,
                    Color.AMARILLO, Color.BOLD, da1, Color.RESET,
                    Color.AMARILLO, Color.BOLD, da2, Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe1, Color.RESET) + alquiler + "\n";
        else if(da2.isEmpty())
            return String.format("id: %s%s%s%s\ntrato propuesto por: %s%s%s%s\ncambiar: obtienes %s%s%s%s por %s%s%s%s y %s%s%s%s",
                    Color.AZUL_CLARITO, Color.BOLD, id, Color.RESET,
                    Color.AZUL_CLARITO, Color.BOLD, jugadorPropone, Color.RESET,
                    Color.AMARILLO, Color.BOLD, da1, Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe1, Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe2, Color.RESET)  + alquiler + "\n";
        else
            return String.format("id %s%s%s%s\ntrato propuesto por: %s%s%s%s\ncambiar: obtienes %s%s%s%s y %s%s%s%s por %s%s%s%s y %s%s%s%s",
                    Color.AZUL_CLARITO, Color.BOLD, id, Color.RESET,
                    Color.AZUL_CLARITO, Color.BOLD, jugadorPropone, Color.RESET,
                    Color.AMARILLO, Color.BOLD, da1, Color.RESET,
                    Color.AMARILLO, Color.BOLD, da2, Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe1, Color.RESET,
                    Color.AMARILLO, Color.BOLD, recibe2, Color.RESET) + alquiler + "\n";

    }

}
