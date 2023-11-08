package consola;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Consola {
    Scanner sc;
    ByteArrayOutputStream os;
    PrintStream ps, out;

    public Consola() {
        sc = new Scanner(System.in);
        os = new ByteArrayOutputStream();
        ps = new PrintStream(os);
        out = System.out;
    };


    public void limpiar_pantalla() {
        System.out.print("\u001b[33;0H\u001b[1J\u001b[H");
    }

    public void limpiar_resultado() {
        System.out.println("\u001b[1A\u001b[0J");
    }

    public boolean entrada() {
        System.out.print("> \u001b[K");
        String line = sc.nextLine();
        limpiar_resultado();

        System.setOut(ps);
    
        try {
            Comando cmd = new Comando(line);
            cmd.run();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        System.out.flush();
        System.setOut(out);

        String salida = os.toString();
        int lines = salida.split("\r\n|\r|\n").length;

        System.out.println(salida);
        os.reset();

        boolean pausa = lines > 10;
        if (pausa) {
            System.out.println("pulsa <enter> para continuar");
            sc.nextLine();
        }
        return pausa;
    } 
}
