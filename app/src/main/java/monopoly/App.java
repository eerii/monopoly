package monopoly;

import consola.Consola;

public class App {
    static Consola c;

    public static void main(String[] args) {
        c = new Consola();
        while (true)
            c.input();
    }
}
