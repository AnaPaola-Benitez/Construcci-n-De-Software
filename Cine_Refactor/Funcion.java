import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Funcion {
    public String titulo;
    public int sala;
    public String hora;

    // TECNICA UTILIZADA: Remove Setting Method
    // REFACTOR: El precio se establece unicamente al crear la funcion y es final,
    // se elimino setPrecio(double), que permitia modificarlo despues.
    public final double precio;

    // TECNICA UTILIZADA: Hide Method
    // REFACTOR: Se dejo de exponer la lista publica de asientos ocupados.
    // Solo puede consultarse como una copia de solo lectura mediante getOcupados().
    private final List<String> ocupados = new ArrayList<>();

    public Funcion(String titulo, int sala, String hora, double precio) {
        this.titulo = titulo;
        this.sala = sala;
        this.hora = hora;
        this.precio = precio;
    }

    // TECNICA UTILIZADA: Separate Query from Modifier
    // REFACTOR: Se separo la consulta de disponibilidad de la modificacion:
    // este metodo unicamente pregunta si TODOS los asientos estan libres.
    public boolean estanDisponibles(SeleccionAsientos seleccion) {
        for (String codigo : seleccion.getCodigos()) {
            if (ocupados.contains(codigo)) {
                return false;
            }
        }
        return true;
    }

    // TECNICA UTILIZADA: Separate Query from Modifier
    // REFACTOR: La operacion que modifica la ocupacion esta separada de la
    // consulta. Primero valida el lote completo y despues ocupa los asientos,
    // por lo que nunca deja un asiento bloqueado a causa de un fallo intermedio.
    public void ocuparAsientos(SeleccionAsientos seleccion) {
        if (!estanDisponibles(seleccion)) {
            throw new IllegalStateException("Uno o más asientos ya están ocupados");
        }
        for (String codigo : seleccion.getCodigos()) {
            ocuparAsiento(codigo);
        }
    }

    // TECNICA UTILIZADA: Hide Method
    // REFACTOR: El antiguo ocupar(fila, numero), que podia llamarse desde fuera,
    // se reemplazo por un metodo privado, las modificaciones pasan por ocuparAsientos().
    private void ocuparAsiento(String codigo) {
        ocupados.add(codigo);
    }

    // TECNICA UTILIZADA: Hide Method
    // REFACTOR: Se ofrece lectura de los asientos sin permitir que otras
    // clases alteren directamente la coleccion interna.
    public List<String> getOcupados() {
        return Collections.unmodifiableList(new ArrayList<>(ocupados));
    }

    // TECNICA UTILIZADA: Hide Method
    // REFACTOR: La liberacion también se realiza mediante una operacion de
    // dominio, sin acceder directamente a la lista ocupados desde SistemaReservas.
    public void liberarAsientos(List<String> asientos) {
        ocupados.removeAll(asientos);
    }
}
