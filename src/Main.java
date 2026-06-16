import java.time.LocalDate;
import java.util.Scanner;
import java.time.format.DateTimeParseException;

public class Main {
    public static LocalDate leerFecha(Scanner s, String mensaje){
        LocalDate fecha=null;
        boolean valido=false;
        while (!valido){
            System.out.println(mensaje+" (YYYY-MM-DD): ");
            String fechaStr=s.nextLine();
            try {
                fecha=LocalDate.parse(fechaStr);
                valido=true;
            }catch (DateTimeParseException e){
                System.out.println("Ingrese fecha en formato valido");
            }
        }
        return fecha;
    }
    public static void main(String[] args) {
        Hotel hotel = Hotel.cargarDatos();
        Scanner s=new Scanner(System.in);
        int opcionMenu;
        boolean salir=false;
        do {
            System.out.println("\n===== GESTION DE HOTEL "+ hotel.getNombre());
            System.out.println("1. Registrar Cliente");
            System.out.println("2. Registrar Habitacion");
            System.out.println("3. Crear Reserva");
            System.out.println("4. Consultar Reservas");
            System.out.println("5. Consultar Habitaciones");
            System.out.println("6. Cancelar Reserva");
            System.out.println("7. Guardar Datos y Salir");
            System.out.print("Seleccione una opcion: ");
            if (s.hasNextInt()){
                opcionMenu=s.nextInt();
                s.nextLine();
                switch (opcionMenu){
                    case 1:
                        System.out.print("ID Cliente (Ej. C101): ");
                        String id = s.nextLine();
                        System.out.print("Nombre: ");
                        String nombre = s.nextLine();
                        System.out.print("Telefono: ");
                        String telefono = s.nextLine();
                        System.out.print("Email: ");
                        String email = s.nextLine();
                        if (id.isEmpty()||nombre.isEmpty()||telefono.isEmpty()||email.isEmpty()){
                            System.out.println("Introduzca los datos del usuario");
                            break;
                        }
                        Cliente clienteNuevo=new Cliente(id, nombre, telefono, email);
                        if (hotel.registrarCliente(clienteNuevo)) {
                            System.out.println("Cliente registrado con exito.");
                        }
                        break;

                    case 2: // Registrar Habitacion
                        System.out.print("Numero de Habitacion (Ej. 302): ");
                        String numHab = s.nextLine();
                        System.out.print("Tipo (Doble/Suite/Individual): ");
                        String tipoHab = s.nextLine();
                        System.out.print("Precio por noche: ");
                        if (s.hasNextDouble()) {
                            double precioHab = s.nextDouble();
                            s.nextLine();
                            Habitacion nuevaHab = new Habitacion(numHab, tipoHab, precioHab);
                            if (hotel.registrarHabitacion(nuevaHab)) {
                                System.out.println("Habitacion registrada con exito.");
                            }
                        } else {
                            System.out.println("Precio invalido.");
                            s.next();
                        }
                        break;
                    case 3:
                        hotel.consultarClientes();
                        System.out.println("Ingrese el id del Cliente");
                        String idClienteReserva=s.nextLine();
                        hotel.consultarHabitaciones();
                        System.out.println("Ingrese numero de habitacion");
                        String numHabReserva=s.nextLine();
                        LocalDate fechaI = leerFecha(s, "Fecha de Inicio");
                        LocalDate fechaF = leerFecha(s, "Fecha de Fin");
                        hotel.crearReserva(idClienteReserva, numHabReserva, fechaI, fechaF);
                        break;
                    case 4:
                        hotel.consultarReservas();
                        break;
                    case 5:
                        hotel.consultarHabitaciones();
                        break;
                    case 6:
                        hotel.consultarReservas();
                        System.out.println("Ingrese el codigo de reserva a cancelar");
                        String codigo=s.next();
                        hotel.cancelarReserva(codigo);
                        break;
                    case 7:
                        System.out.println("Guardando datos y saliendo del sistema...");
                        hotel.guardarDatos();
                        salir=true;
                        break;
                }
            }else {
                System.out.println("Ingrese un valor valido");
            }
        }while (!salir);
        s.close();
    }
}
