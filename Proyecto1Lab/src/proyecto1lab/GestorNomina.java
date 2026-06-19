package proyecto1lab;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class GestorNomina {

    private static final int capacidadMaxima = 100;
    private static final String archivoHistorialBin = "historial_totales.bin";
    private static final String archivoBackupBin = "empleados_backup.bin";
    private static final String archivoLog = "log_operaciones.txt";

    private Empleado[] empleados;
    private int cantidadEmpleados;

    public GestorNomina() {
        this.empleados = new Empleado[capacidadMaxima];
        this.cantidadEmpleados = 0;
    }
    
    public boolean agregarEmpleado(Empleado empleado) {
        if (empleado == null) {
            registrarLog("ERROR: Intento de agregar un empleado nulo.");
            return false;
        }
        if (cantidadEmpleados >= capacidadMaxima) {
            registrarLog("ERROR: No se pudo agregar empleado, arreglo lleno (100/100).");
            return false;
        }
        empleados[cantidadEmpleados] = empleado;
        cantidadEmpleados++;
        registrarLog("ALTA: Empleado agregado (" + empleado.getNombre() + ", id=" + empleado.getId() + ").");
        return true;
    }
    
    public boolean eliminarEmpleado(String id) {
        for (int i = 0; i < cantidadEmpleados; i++) {
            if (empleados[i].getId() != null && empleados[i].getId().equals(id)) {
                String nombre = empleados[i].getNombre();
                for (int j = i; j < cantidadEmpleados - 1; j++) {
                    empleados[j] = empleados[j + 1];
                }
                empleados[cantidadEmpleados - 1] = null;
                cantidadEmpleados--;
                registrarLog("BAJA: Empleado eliminado (" + nombre + ", id=" + id + ").");
                return true;
            }
        }
        registrarLog("ERROR: No se encontro empleado con id=" + id + " para eliminar.");
        return false;
    }
    
    public Empleado buscarPorId(String id) {
        for (int i = 0; i < cantidadEmpleados; i++) {
            if (empleados[i].getId() != null && empleados[i].getId().equals(id)) {
                return empleados[i];
            }
        }
        return null;
    }
    
    public Empleado[] getEmpleados() {
        return empleados;
    }
    
    public int getCantidadEmpleados() {
        return cantidadEmpleados;
    }
    
    public boolean estaLleno() {
        return cantidadEmpleados >= capacidadMaxima;
    }
    
    public double calcularTotalBruto() {
        double total = 0.0;
        for (int i = 0; i < cantidadEmpleados; i++) {
            total += empleados[i].calcularPagoQuincenal();
        }
        return total;
    }
    
    public String generarReporteCSV() {
        String fecha = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String nombreArchivo = "nomina_" + fecha + ".csv";
        
        double totalBruto = 0.0;
        double totalDeducciones = 0.0;
        double totalNeto = 0.0;
        
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            writer.write("id,nombre,departamento,tipo,bruto,deducciones,neto\n");

            for (int i = 0; i < cantidadEmpleados; i++) {
                Empleado e = empleados[i];

                double bruto = e.getSalarioBase();
                double neto = e.calcularPagoQuincenal();
                double deducciones = bruto - neto;
                
                if (bruto == 0.0) {
                    bruto = neto;
                    deducciones = 0.0;
                }
                
                String linea = String.format("%s,%s,%s,%s,%.2f,%.2f,%.2f",
                        safe(e.getId()),
                        safe(e.getNombre()),
                        safe(e.getDepartamento()),
                        e.getCategoria(),
                        bruto,
                        deducciones,
                        neto);
                writer.write(linea + "\n");
                
                totalBruto += bruto;
                totalDeducciones += deducciones;
                totalNeto += neto;
            }
            
            String renglonTotales = String.format("TOTALES,,,,%.2f,%.2f,%.2f", totalBruto, totalDeducciones, totalNeto);
            writer.write(renglonTotales + "\n");
            
            registrarLog("CSV: Reporte de nomina generado en " + nombreArchivo + " (" + cantidadEmpleados + " empleados).");
            return nombreArchivo;
            
        } catch (IOException ex) {
            registrarLog("ERROR: Fallo al generar el CSV (" + nombreArchivo + "): " + ex.getMessage());
            return null;
        }
    }
    
    private String safe(String valor) {
        return valor == null ? "" : valor;
    }
    
    public boolean guardarTotalesBinario(String periodo, double totalBruto, double totalDeducciones, int numEmpleados) {
        try (DataOutputStream out = new DataOutputStream(
                new FileOutputStream(archivoHistorialBin, true))) {
            
            out.writeUTF(periodo);
            out.writeDouble(totalBruto);
            out.writeDouble(totalDeducciones);
            out.writeInt(numEmpleados);
            
            registrarLog("BINARIO: Totales del periodo '" + periodo + "' agregados a "
                    + archivoHistorialBin + " (append).");
            return true;
            
        } catch (IOException ex) {
            registrarLog("ERROR: Fallo al guardar totales binarios: " + ex.getMessage());
            return false;
        }
    }
    
    public boolean guardarTotalesBinario(String periodo) {
        double totalBruto = 0.0;
        double totalDeducciones = 0.0;
        
        for (int i = 0; i < cantidadEmpleados; i++) {
            Empleado e = empleados[i];
            double neto = e.calcularPagoQuincenal();
            double bruto = e.getSalarioBase() == 0.0 ? neto : e.getSalarioBase();
            totalBruto += bruto;
            totalDeducciones += (bruto - neto);
        }
        return guardarTotalesBinario(periodo, totalBruto, totalDeducciones, cantidadEmpleados);
    }
    
    public static class RegistroHistorial {
        public final String periodo;
        public final double totalBruto;
        public final double totalDeducciones;
        public final int numEmpleados;
        
        public RegistroHistorial(String periodo, double totalBruto, double totalDeducciones, int numEmpleados) {
            this.periodo = periodo;
            this.totalBruto = totalBruto;
            this.totalDeducciones = totalDeducciones;
            this.numEmpleados = numEmpleados;
        }
        
        @Override
        public String toString() {
            return String.format("Periodo: %s | Bruto: %.2f | Deducciones: %.2f | Empleados: %d",
                    periodo, totalBruto, totalDeducciones, numEmpleados);
        }
    }
    
    public List<RegistroHistorial> cargarTotalesBinario() {
        List<RegistroHistorial> historial = new ArrayList<>();
        File archivo = new File(archivoHistorialBin);
        
        if (!archivo.exists()) {
            registrarLog("BINARIO: " + archivoHistorialBin + " no existe todavia. No hay historial previo.");
            return historial;
        }
        
        try (DataInputStream in = new DataInputStream(new FileInputStream(archivo))) {
            while (true) {
                String periodo = in.readUTF();
                double totalBruto = in.readDouble();
                double totalDeducciones = in.readDouble();
                int numEmpleados = in.readInt();
                historial.add(new RegistroHistorial(periodo, totalBruto, totalDeducciones, numEmpleados));
            }
        } catch (EOFException finDeArchivo) {
            
        } catch (IOException ex) {
            registrarLog("ERROR: Fallo al leer " + archivoHistorialBin + ": " + ex.getMessage());
        }
        
        registrarLog("BINARIO: " + historial.size() + " registro(s) cargados de " + archivoHistorialBin + ".");
        return historial;
    }
    
    public boolean guardarBackupEmpleados() {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(archivoBackupBin))) {
            
            out.writeInt(cantidadEmpleados);
            out.writeObject(empleados);
            
            registrarLog("BACKUP: " + cantidadEmpleados + " empleado(s) serializados en " + archivoBackupBin + ".");
            return true;
            
        } catch (IOException ex) {
            registrarLog("ERROR: Fallo al guardar backup de empleados: " + ex.getMessage());
            return false;
        }
    }
    
    public boolean cargarBackupEmpleados() {
        File archivo = new File(archivoBackupBin);
        if (!archivo.exists()) {
            registrarLog("BACKUP: " + archivoBackupBin + " no existe. Se inicia con arreglo vacio.");
            return false;
        }
        
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
            int cantidadGuardada = in.readInt();
            Empleado[] arregloLeido = (Empleado[]) in.readObject();

            this.empleados = arregloLeido;
            this.cantidadEmpleados = cantidadGuardada;

            registrarLog("BACKUP: " + cantidadEmpleados + " empleado(s) restaurados desde " + archivoBackupBin + ".");
            return true;
            
        } catch (IOException | ClassNotFoundException ex) {
            registrarLog("ERROR: Fallo al cargar backup de empleados: " + ex.getMessage());
            return false;
        }
    }
    
    public void registrarLog(String mensaje) {
        String marcaTiempo = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        try (FileWriter writer = new FileWriter(archivoLog, true)) {
            writer.write("[" + marcaTiempo + "] " + mensaje + "\n");
        } catch (IOException ex) {
            
            System.out.println("No se pudo escribir en el log: " + ex.getMessage());
        }
    }
    
    public String leerLog() {
        StringBuilder contenido = new StringBuilder();
        File archivo = new File(archivoLog);
        
        if (!archivo.exists()) {
            return "(El log aun no tiene registros.)";
        }
        
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
        } catch (IOException ex) {
            return "Error al leer el log: " + ex.getMessage();
        }
        
        return contenido.toString();
    }
}
