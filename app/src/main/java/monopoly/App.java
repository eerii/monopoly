package monopoly;

import consola.Consola;
import monopoly.Avatar.TipoAvatar;

public class App {
    static Consola c;

    public static void main(String[] args) {
        Avatar a = new Avatar(TipoAvatar.PELOTA);
        System.out.println(a.get_id() + " " + a.get_tipo().toString());

        c = new Consola();
        while (true)
            c.input();
    }
}
