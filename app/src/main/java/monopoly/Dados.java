package monopoly;

import java.util.Random;

public class Dados {

    public int tirarDados() {
        int dado1,dado2;
        Random r = new Random();
        dado1=(r.nextInt(6) + 1);
        dado2=(r.nextInt(6) + 1);
        return  dado1 + dado2;
    }

}
