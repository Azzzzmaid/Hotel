import java.util.ArrayList;
import java.util.Optional;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.io.Serializable;
import java.io.*;

public class Hotel implements  Serializable{

    private String nombre;
    private ArrayList<Habitacion> listaHabitaciones;
    private ArrayList<Cliente> listaClientes;
    private ArrayList<Reserva> listaReservas;

    private static final String nombreDelArchivo="reservas_hotel.dat";

    public void guardarDatos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(nombreDelArchivo))) {
            oos.writeObject(this);
            System.out.println("Datos guardados en " + nombreDelArchivo);

        } catch (IOException e) {
            System.err.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    public static Hotel cargarDatos() {
        File archivo = new File(nombreDelArchivo);
        // Si el archivo no existe o está vacío, creamos una nueva instancia del Hotel
        if (!archivo.exists() || archivo.length() == 0) {
            System.out.println("ℹ️ Archivo de datos no encontrado. Creando nuevo Hotel 'El Cachondo'.");
            return new Hotel("El Cachondo");
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(nombreDelArchivo))) {
            Hotel hotelCargado = (Hotel) ois.readObject();
            System.out.println("Datos cargados exitosamente.");
            return hotelCargado;

        } catch (Exception e) {
            System.err.println("Error al cargar los datos. Creando nuevo Hotel. Detalle: " + e.getMessage());
            return new Hotel("El Cachondo");
        }
    }

    public Hotel(String nombre) {
        this.nombre = nombre;
        this.listaHabitaciones = new ArrayList<>();
        this.listaClientes = new ArrayList<>();
        this.listaReservas = new ArrayList<>();
    }
    public String getNombre() { return nombre; }

    public boolean registrarHabitacion(Habitacion h) {
        boolean existe = listaHabitaciones.stream()
                .anyMatch(hab -> hab.getNumero().equals(h.getNumero()));

        if (existe) {
            System.out.println("Error, la habitacion numero " + h.getNumero()+" ya se encuentra registrada");
            return false;
        }
        listaHabitaciones.add(h);
        guardarDatos();
        return true;
    }

    public boolean registrarCliente(Cliente c) {
        boolean existe = listaClientes.stream().anyMatch(
                cl -> cl.getClienteId().equals(c.getClienteId()) || cl.getEmail().equals(c.getEmail())
        );

        if (existe) {
            System.out.println("El cliente con ID o Email ya está registrado.");
            return false;
        }
        listaClientes.add(c);
        guardarDatos();
        return true;
    }

    public boolean crearReserva(String clienteId, String habitacionNum, LocalDate fechaInicio, LocalDate fechaFin) {
        Optional<Cliente> optCliente = listaClientes.stream()
                .filter(c -> c.getClienteId().equals(clienteId))
                .findFirst();

        if (!optCliente.isPresent()) {
            System.out.println("Cliente no encontrado con ID: " + clienteId);
            return false;
        }
        Cliente cliente = optCliente.get();
        Optional<Habitacion> optHabitacion = listaHabitaciones.stream()
                .filter(h -> h.getNumero().equals(habitacionNum))
                .findFirst();

        if (!optHabitacion.isPresent()) {
            System.out.println("Habitación no encontrada con número: " + habitacionNum);
            return false;
        }
        Habitacion habitacionSeleccionada = optHabitacion.get();
        if (fechaInicio.isAfter(fechaFin)) {
            System.out.println("La fecha de inicio (" + fechaInicio + ") no puede ser posterior a la de fin (" + fechaFin + ").");
            return false;
        }

        long duracionNoches = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        if (duracionNoches <= 0) {
            System.out.println("La reserva debe durar al menos 1 noche. Duración actual: " + duracionNoches + " noches.");
            return false;
        }
        if (!habitacionSeleccionada.isDisponible()) {
            System.out.println("La habitación " + habitacionNum + " ya está ocupada.");
            return false;
        }
        double total = duracionNoches * habitacionSeleccionada.getPrecio();
        Reserva nuevaReserva = new Reserva(cliente, habitacionSeleccionada, fechaInicio, fechaFin, total);

        listaReservas.add(nuevaReserva);
        habitacionSeleccionada.setDisponible(false); // Marcar como ocupada

        System.out.println("Reserva CREADA con éxito. Código: " + nuevaReserva.getCodigoReserva());
        System.out.println("Total a pagar: $" + total + " por " + duracionNoches + " noches.");
        guardarDatos();
        return true;
    }

    public void consultarReservas() {
        System.out.println("LISTADO DE RESERVAS");
        if (listaReservas.isEmpty()) {
            System.out.println("No existen reservas en el sistema.");
            return;
        }
        for (Reserva r : listaReservas) {
            System.out.println(r);
        }
    }

    public void consultarHabitaciones() {
        System.out.println("LISTADO DE HABITACIONES");
        if (listaHabitaciones.isEmpty()) {
            System.out.println("No hay habitaciones registradas.");
            return;
        }
        for (Habitacion h : listaHabitaciones) {
            System.out.println(h);
        }
    }

    public void consultarClientes() {
        System.out.println("LISTADO DE CLIENTES");
        if (listaClientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        for (Cliente c : listaClientes) {
            System.out.println(c);
        }
    }

    public boolean cancelarReserva(String codigoReserva) {
        Optional<Reserva> optReserva = listaReservas.stream()
                .filter(r -> r.getCodigoReserva().equals(codigoReserva))
                .findFirst();

        if (optReserva.isPresent()) {
            Reserva reserva = optReserva.get();
            Habitacion habitacion = reserva.getHabitacion();

            // La habitación vuelve a estar disponible
            habitacion.setDisponible(true);
            listaReservas.remove(reserva);
            guardarDatos();

            System.out.println("Reserva " + codigoReserva + " CANCELADA con éxito. Habitación " + habitacion.getNumero() + " liberada.");
            return true;
        } else {
            System.out.println("Error: Reserva con código " + codigoReserva + " no encontrada.");
            return false;
        }
    }
}
