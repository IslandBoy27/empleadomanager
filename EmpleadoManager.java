package empleadomanager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EmpleadoManager {
    private RandomAccessFile rcods, remps;
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    public EmpleadoManager() {
        try {
            // Asegurar que el folder Company existe
            File mf = new File("Company");
            mf.mkdir();

            // Instanciar los RAFs dentro de Company
            rcods = new RandomAccessFile("Company/codigos.emp", "rw");
            remps = new RandomAccessFile("Company/empleados.emp", "rw");

            // Inicializar el archivo de códigos
            initCodes();
        } catch (IOException e) {
            System.out.println("No debería de pasar esto: " + e.getMessage());
        }
    }

    private void initCodes() throws IOException {
        if (rcods.length() == 0)
            rcods.writeInt(1);
    }

    private int getCode() throws IOException {
        rcods.seek(0);
        int code = rcods.readInt();
        rcods.seek(0);
        rcods.writeInt(code + 1);
        return code;
    }

    public void addEmpleado(String name, double salario) throws IOException {
        /*
        Formato:
        codigo - int
        nombre - String
        salario - double
        fecha contratacion - Calendar - long
        fecha despido - Calendar - long (0 si no está despedido)
        */
        remps.seek(remps.length());
        int code = getCode();

        // Guardar los detalles del empleado
        remps.writeInt(code);  // Código
        remps.writeUTF(name);  // Nombre
        remps.writeDouble(salario);  // Salario
        remps.writeLong(Calendar.getInstance().getTimeInMillis());  // Fecha de contratación
        remps.writeLong(0);  // Fecha de despido (0 si no está despedido)

        System.out.println("Empleado agregado exitosamente. Código: " + code);
    }

    public void imprimirEmpleadosNoDespedidos() throws IOException {
        System.out.println("**** LISTA DE EMPLEADOS ****");
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        remps.seek(0);  // Inicia desde el comienzo del archivo
        int contador = 1;

        while (remps.getFilePointer() < remps.length()) {
            int codigo = remps.readInt();
            String nombre = remps.readUTF();
            double salario = remps.readDouble();
            long fechaContratacion = remps.readLong();
            long fechaDespido = remps.readLong();

            if (fechaDespido == 0) {  // Si el empleado no está despedido
                System.out.printf("%d. Código: %d - Nombre: %s - Salario Lps: %.2f - Fecha Contratación: %s%n",
                        contador++, codigo, nombre, salario, sdf.format(new Date(fechaContratacion)));
            }
        }
    }

    public boolean empleadoActivo(int codigo) throws IOException {
        remps.seek(0);
        while (remps.getFilePointer() < remps.length()) {
            int codigoLeido = remps.readInt();
            String nombre = remps.readUTF();
            double salario = remps.readDouble();
            long fechaContratacion = remps.readLong();
            long fechaDespido = remps.readLong();

            if (codigoLeido == codigo) {
                return fechaDespido == 0;  // Devuelve true si el empleado no está despedido
            }
        }
        return false;
    }

    public boolean despedirEmpleado(int codigo) throws IOException {
        remps.seek(0);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        while (remps.getFilePointer() < remps.length()) {
            int codigoLeido = remps.readInt();
            String nombre = remps.readUTF();
            double salario = remps.readDouble();
            long fechaContratacion = remps.readLong();
            long fechaDespidoPos = remps.getFilePointer();

            long fechaDespido = remps.readLong();
            if (codigoLeido == codigo && fechaDespido == 0) {
                // Actualiza la fecha de despido del empleado
                Date fechaActual = Calendar.getInstance().getTime();
                remps.seek(fechaDespidoPos);
                remps.writeLong(fechaActual.getTime());

                System.out.println("Empleado despedido: " + nombre);
                System.out.println("Fecha de despido: " + sdf.format(fechaActual));  // Mostrar la fecha de despido

                return true;
            }
        }
        return false;
    }

    public void mostrarInformacionEmpleado(int codigo) throws IOException {
        remps.seek(0);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        while (remps.getFilePointer() < remps.length()) {
            int codigoLeido = remps.readInt();
            String nombre = remps.readUTF();
            double salario = remps.readDouble();
            long fechaContratacion = remps.readLong();
            long fechaDespido = remps.readLong();

            if (codigoLeido == codigo) {
                System.out.println("Información del empleado:");
                System.out.println("Código: " + codigoLeido);
                System.out.println("Nombre: " + nombre);
                System.out.println("Salario Lps: " + salario);
                System.out.println("Fecha Contratación: " + sdf.format(new Date(fechaContratacion)));
                if (fechaDespido != 0) {
                    System.out.println("Fecha Despido: " + sdf.format(new Date(fechaDespido)));
                } else {
                    System.out.println("Estado: Activo");
                }
                return;
            }
        }
        System.out.println("Empleado no encontrado.");
    }
}
