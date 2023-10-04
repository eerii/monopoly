package monopoly;

import consola.Consola;

public class App {
    static Consola c;

    public static void main(String[] args) {
        Tablero t = new Tablero();
        Jugador j = new Jugador("hola", new Avatar(Avatar.TipoAvatar.PELOTA));
        Casilla c = t.buscar_casilla("23");
        if (c != null)
            c.add_jugador(j);

        System.out.println(t.toString());

        // c = new Consola();
        // while (true)
        //     c.input();
    }
}
