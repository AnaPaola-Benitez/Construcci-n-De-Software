import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Reserva {
    public final int id;
    public final Cliente cliente;
    public final Funcion funcion;
    public final List<String> asientos;
    public double precio;
    private boolean cancelada;
    public final String canal;

    // TECNICA UTILIZADA: Add Parameter
    // REFACTOR: Se agrego String canal al constructor para guardar en cada
    // reserva si la compra se hizo por App, Taquilla u otro canal, antes se ignoraba.
    public Reserva(int id, Cliente cliente, Funcion funcion, List<String> asientos,
                   String canal) {
        this.id = id;
        this.cliente = cliente;
        this.funcion = funcion;
        this.asientos = Collections.unmodifiableList(new ArrayList<>(asientos));
        this.canal = canal;
    }

    public boolean estaCancelada() {
        return cancelada;
    }

    // TECNICA UTILIZADA: Remove Setting Method
    // REFACTOR: Se elimino setCancelada(boolean) y se sustituyo por cancelar(),
    // que expresa la unica transicion valida en la logica de la reserva.
    public void cancelar() {
        cancelada = true;
    }
}
