package monopoly;

import consola.Consola;

public class App {
    static Consola c;
    static Monopoly m;

    public static void main(String[] args) {
        m = new Monopoly();
        System.out.println(m.get_tablero().toString());

        c = new Consola();
        while (true)
            c.input();
    }
}
