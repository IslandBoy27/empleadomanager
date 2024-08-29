package empleadomanager;

import empleadomanager.EmpleadoManager;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ControlEmpleado {
    private static EmpleadoManager empleadoManager = new EmpleadoManager();
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero("Ingrese una opción: ");

            try {
                switch (opcion) {
                    case 1:
                        agregarEmpleado();
                        break;
                    case 2:
                        empleadoManager.imprimirEmpleadosNoDespedidos();
                        break;
                    case 3:
                        despedirEmpleado();
                        break;
                    case 4:
                        buscarEmpleadoActivo();
                        break;
                    case 5:
                        System.out.println("Saliendo del programa.");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, intente de nuevo.");
                }
            } catch (IOException e) {
                System.out.println("Error en la operación: " + e.getMessage());
            }

        } while (opcion != 5);
    }

    private static void mostrarMenu() {
        System.out.println("");
        System.out.println("**** MENU ****");
        System.out.println("1- Agregar Empleado");
        System.out.println("2- Listar Empleado No Despedidos");
        System.out.println("3- Despedir Empleado");
        System.out.println("4- Buscar Empleado Activo");
        System.out.println("5- Salir");
    }

    private static void agregarEmpleado() throws IOException {
        String nombre = leerCadena("Ingrese el nombre del empleado: ");
        double salario = leerDouble("Ingrese el salario del empleado: ");

        empleadoManager.addEmpleado(nombre, salario);
    }

    private static void despedirEmpleado() throws IOException {
        int codigo = leerEntero("Ingrese el código del empleado a despedir: ");

        if (empleadoManager.despedirEmpleado(codigo)) {
            System.out.println("Empleado despedido exitosamente.");
        } else {
            System.out.println("Empleado no encontrado o ya está despedido.");
        }
    }

    private static void buscarEmpleadoActivo() throws IOException {
        int codigo = leerEntero("Ingrese el código del empleado a buscar: ");

        if (empleadoManager.empleadoActivo(codigo)) {
            empleadoManager.mostrarInformacionEmpleado(codigo);
        } else {
            System.out.println("Empleado no encontrado o está despedido.");
        }
    }

    private static String leerCadena(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        int numero = -1;
        while (numero == -1) {
            try {
                System.out.print(mensaje);
                numero = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor ingrese un número entero.");
            }
        }
        return numero;
    }

    private static double leerDouble(String mensaje) {
        double numero = -1;
        while (numero == -1) {
            try {
                System.out.print(mensaje);
                numero = Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor ingrese un número decimal.");
            }
        }
        return numero;
    }
}
