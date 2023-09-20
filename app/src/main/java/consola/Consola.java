package consola;

import java.util.Scanner;

public class Consola {
    public Consola() {
        sc = new Scanner(System.in);
    };

    Scanner sc; // TODO: Close

    public void input() {
        System.out.print("> ");
        String line = sc.nextLine();
    
        try {
            Comando cmd = new Comando(line);
            cmd.run();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
