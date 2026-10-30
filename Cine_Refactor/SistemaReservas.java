import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class SistemaReservas {
    private final Map<Integer, Object> db = new HashMap<>();
    private int contador = 1;

    // TECNICA UTILIZADA: Rename Method
    // REFACTOR: Se renombro hacer() a reservarAsientos(), un nombre que
    // explica con claridad la operacion de negocio.
    // TECNICA UTILIZADA: Remove Parameter
    // REFACTOR: Se quito boolean imprimir porque no se utilizaba en el metodo.
    // TECNICA UTILIZADA: Preserve Whole Object
    // REFACTOR: En vez de nombre, email y socio por separado, se recibe Cliente.
    // TECNICA UTILIZADA: Introduce Parameter Object
    // REFACTOR: Se sustituyeron char[] filas e int[] numeros por SeleccionAsientos.
    public int reservarAsientos(Cliente cliente, Funcion funcion,
                               SeleccionAsientos asientos, String canal) {
        // TECNICA UTILIZADA: Replace Error Code with Exception
        // REFACTOR: Los datos incompletos provocan IllegalArgumentException,
        // ya no se devuelve -1 o -2 para representar fallos.
        if (cliente == null || cliente.nombre == null || funcion == null
                || asientos == null || canal == null || canal.trim().isEmpty()) {
            throw new IllegalArgumentException("Datos insuficientes para reservar");
        }

        // TECNICA UTILIZADA: Replace Exception with Test
        // REFACTOR: Antes de intentar ocupar, se consulta la disponibilidad de
        // TODO el lote, ya no se usa try/catch como control del flujo normal.
        // TECNICA UTILIZADA: Replace Error Code with Exception
        // REFACTOR: Si uno esta ocupado se lanza un error controlado, no -3.
        if (!funcion.estanDisponibles(asientos)) {
            throw new IllegalStateException("No se puede reservar: hay asientos ocupados");
        }

        // Ocupar solo despues de validar evita el bloqueo parcial de F7 de Luis.
        funcion.ocuparAsientos(asientos);

        // TECNICA UTILIZADA: Add Parameter
        // REFACTOR: El canal recibido se pasa al nuevo parametro del constructor
        // de Reserva, donde se conserva como dato de la transaccion.
        Reserva reserva = new Reserva(contador, cliente, funcion,
                asientos.getCodigos(), canal);
        reserva.precio = calcularPrecio(reserva);
        db.put(contador, reserva);
        contador++;
        return reserva.id;
    }

    // TECNICA UTILIZADA: Replace Parameter with Method
    // REFACTOR: Se elimino el parametro String tipo de calcularPrecio(r, tipo),
    // porque la condicion de socio ya puede obtenerse de r.cliente.socio.
    public double calcularPrecio(Reserva reserva) {
        if (reserva.cliente.socio) {
            return calcularPrecioSocio(reserva);
        }
        return calcularPrecioGeneral(reserva);
    }

    // TECNICA UTILIZADA: Replace Parameter with Explicit Methods
    // REFACTOR: Se reemplazo la seleccion por textos "general"/"socio" por
    // dos metodos explicitos para cada tipo de tarifa.
    public double calcularPrecioGeneral(Reserva reserva) {
        return reserva.funcion.precio * reserva.asientos.size();
    }

    // TECNICA UTILIZADA: Replace Parameter with Explicit Methods
    // REFACTOR: La tarifa de socios tiene su operacion propia y mantiene el
    // descuento original del 15 % (24.0 -> 20.4 en el caso de Ana).
    public double calcularPrecioSocio(Reserva reserva) {
        return aplicarDescuento(calcularPrecioGeneral(reserva), 0.15);
    }

    // TECNICA UTILIZADA: Parameterize Method
    // REFACTOR: descuentoDel15 y descuentoDel25 tenian la misma formula,
    // se sustituyeron por un solo metodo con porcentaje como parametro.
    public double aplicarDescuento(double precioBase, double porcentaje) {
        return precioBase - precioBase * porcentaje;
    }

    // TECNICA UTILIZADA: Replace Error Code with Exception
    // REFACTOR: Cancelar una reserva inexistente genera NoSuchElementException
    // y cancelar una reserva ya cancelada genera IllegalStateException; no -1/-2.
    // TECNICA UTILIZADA: Remove Setting Method
    // REFACTOR: Se invoca reserva.cancelar() en lugar de setCancelada(true).
    public void cancelar(int id) {
        Reserva reserva = buscar(id);
        if (reserva == null) {
            throw new NoSuchElementException("No existe la reserva con id " + id);
        }
        if (reserva.estaCancelada()) {
            throw new IllegalStateException("La reserva " + id + " ya fue cancelada");
        }
        reserva.funcion.liberarAsientos(reserva.asientos);
        reserva.cancelar();
    }

    // TECNICA UTILIZADA: Encapsulate Downcast
    // REFACTOR: El unico casteo de Object a Reserva se concentro aqui;
    // Main y cancelar() reciben directamente una Reserva y nunca castean.
    public Reserva buscar(int id) {
        return (Reserva) db.get(id);
    }
}
