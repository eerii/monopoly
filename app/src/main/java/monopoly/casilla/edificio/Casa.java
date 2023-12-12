package monopoly.casilla.edificio;

import monopoly.casilla.propiedad.Solar;

public class Casa extends Edificio {
    // ·············
    // Constructores
    // ·············

    public Casa(Solar casilla) {
        super(casilla, "Casa", "");
    }

    // ·········
    // Overrides
    // ·········

    @Override
    public String toString() {
        return String.format("casa - propietario: %s - casilla: %s - grupo: %s - coste: %.2f\n",
                super.get_casilla().get_propietario().get_nombre(), super.get_casilla().get_nombre(),
                super.get_casilla().get_grupo().get_nombre(),
                coste());
    }
}
