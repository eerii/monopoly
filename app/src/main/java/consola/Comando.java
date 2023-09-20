package consola;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Comando {
    public Comando(String cmd) {
        String[] args = cmd.split(" ");
        String arg_cmd = args[0];
        String arg_primeiro = args.length > 1 ? args[1] : "";
       
        try {
            this.cmd = Cmd.from_str(arg_cmd, arg_primeiro);
        } catch (IllegalArgumentException e) {
            throw e;
        }

        this.args = Arrays.stream(args, 1, args.length).collect(Collectors.toList());
    }

    Cmd cmd;
    List<String> args;

    public void run() {
        System.out.println(cmd.to_str());
        System.out.println(args);
    }
}
