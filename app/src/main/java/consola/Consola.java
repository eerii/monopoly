package consola;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

import consola.excepciones.ConsolaException;
import monopoly.Monopoly;

public class Consola implements IConsola {
    // TODO: Cambiar todos los System.out.println y System.out.format por Monopoly.get().get_consola().imprimir()
    // Las dos funcionan igual con nuestro imprimir
    // Para los System.out.print a secas, mirar si se puede cambiar por un println, si no hay que buscar alternativa

    // ···········
    // Propiedades
    // ···········

    private Scanner sc;
    private ByteArrayOutputStream os;
    private PrintStream ps, out;

    // ·············
    // Constructores
    // ·············

    public Consola() {
        sc = new Scanner(System.in);
        os = new ByteArrayOutputStream();
        ps = new PrintStream(os);
        out = System.out;
    };

    // ················
    // Interfaz pública
    // ················

    public void imprimir(String s, Object... args) {
        if (args.length == 0) {
            System.out.println(s);
        } else if (args.length == 1 && args[0] == null) {
            System.out.print(s);
        } else {
            System.out.format(s, args);
        }
    }

    public String leer() {
        System.out.flush();
        System.setOut(out);

        String salida = os.toString();
        os.reset();

        limpiar_pantalla();
        System.out.println(Monopoly.get().get_tablero().representar());

        System.out.print("\u001b[1B");
        System.out.println(salida);
        System.out.print("\u001b[36;0H> \u001b[K");

        String line = sc.nextLine();
        limpiar_resultado();

        System.setOut(ps);

        return line;
    }

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
        } catch (ConsolaException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Comando no reconocido, " + e.getMessage());
            // TODO: Arreglar esto para múltiples tipos de argumentos inválidos
            System.out
                    .println("Opciones: "
                            + String.join(", ", Arrays.stream(Cmd.values()).map(Cmd::toString).toArray(String[]::new)));
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
