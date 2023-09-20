package consola;

import java.util.Scanner;

public class Consola {
    public void input() {
        Scanner sc = new Scanner(System.in);

        System.out.print("> ");
        String line = sc.nextLine();
    
        try {
            Comando cmd = new Comando(line);
            cmd.run();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        sc.close();
    }
}
