package consola;

import java.util.Scanner;

public class Consola {
    public Consola() {
        sc = new Scanner(System.in);
    };

    Scanner sc;

    public void limpiar_pantalla() {
        System.out.print("\u001b[33;0H\u001b[1J\u001b[H");
    }

    public void limpiar_resultado() {
        System.out.println("\u001b[1A\u001b[0J");
    }

    public void entrada() {
        System.out.print("> \u001b[K");
        String line = sc.nextLine();
        limpiar_resultado();
    
        try {
            Comando cmd = new Comando(line);
            cmd.run();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    } 
}
