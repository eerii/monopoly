package consola;

import java.util.Scanner;

public class Consola {
    public void input() {
        Scanner sc = new Scanner(System.in);

        System.out.print("> ");
        String cmd = sc.nextLine();

        // TODO: xestionar comando
        System.out.println(cmd);

        sc.close();
    }
}
