package consola;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

import consola.excepciones.ConsolaException;
import monopoly.Monopoly;

public class Consola {
    // TODO: Cambiar a interfaz consola con métodos para leer y escribir
    // Aquí podemos implementar automáticamente colores para x cosas
    // Hay que cambiar todos los System.out.print por c.print o lo que sea

    private Scanner sc;
    private ByteArrayOutputStream os;
    private PrintStream ps, out;

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

    public String get_raw() {
        System.out.flush();
        System.setOut(out);

        String salida = os.toString();
        os.reset();

        limpiar_pantalla();
        System.out.println(Monopoly.get().get_tablero());

        System.out.print("\u001b[1B");
        System.out.println(salida);
        System.out.print("\u001b[36;0H> \u001b[K");

        String line = sc.nextLine();
        limpiar_resultado();

        System.setOut(ps);

        return line;
    }
}
