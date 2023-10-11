package consola;

import java.util.Scanner;

public class Consola {
    public Consola() {
        sc = new Scanner(System.in);
    };

    Scanner sc;

    public void input() {
        System.out.print("> ");
        String line = sc.nextLine();
    
        try {
            Comando cmd = new Comando(line);
            cmd.run();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
