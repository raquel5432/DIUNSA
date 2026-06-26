/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1lab;

import java.util.Scanner;
import java.util.List;

/**
 *
 * @author gpopo
 */
public class SistemaRRHH {
    
    private static final GestorNomina gestor = new GestorNomina();
    private static final Scanner entrada = new Scanner(System.in);
    private static final String LOG_FILE = "log_operaciones.txt";
    
    public static void main(String[] args) {
        
        gestor.registrarLog("Inicio del sistema. Intentando cargar respaldo de empleados.");
        gestor.cargarBackupEmpleados(); 
        
        int opcion = 0;
        
        do {    
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");
            
            switch (opcion) {
                case 1:
                    menuRegistrarEmpleado();
                    break;
                case 2:
                    calcularNomina();
                    break;
                case 3:
                    exportarCSV();
                    break;
                case 4:
                    verHistorialTotales();
                    break;
                case 5:
                    buscarEmpleado();
                    break;
                case 6:
                    gestor.guardarBackupEmpleados();
                    gestor.registrarLog("Cierre del sistema por el usuario.");
                    System.out.println("\nCerrando Programa....");
                    System.out.println("Programa cerrado con exito.");
                    break;
                default:
                    System.out.println("Opcion invalida. Intente de nuevo (1-6).");
            }
            
        } while (opcion != 6);
    }
    
    private static void mostrarMenu() {
        System.out.println("\n==================================================");
        System.out.println("      DIUNSA S.A. - SISTEMA DE GESTION RRHH       ");
        System.out.println("==================================================");
        System.out.println("1. Registrar Empleado");
        System.out.println("2. Calcular Nomina Quincenal");
        System.out.println("3. Exportar Reporte de Nomina (CSV)");
        System.out.println("4. Ver Historial de Totales Acumulados (Auditoria)");
        System.out.println("5. Buscar Empleado por ID");
        System.out.println("6. Salir del Sistema");
        System.out.println("==================================================");
    }
    
    private static void menuRegistrarEmpleado() {
        System.out.println("\n--- REGISTRO DE NUEVO EMPLEADO ---");
        String id = leerCadena("Ingrese el ID unico: ");
        
        if (gestor.buscarPorId(id) != null) {
            System.out.println("Error: Ya existe un empleado con el ID " + id);
            return;
        }

        String nombre = leerCadena("Ingrese el nombre completo: ");
        String depto = leerCadena("Ingrese el departamento: ");
        String fecha = leerCadena("Ingrese la fecha de ingreso (AAAA-MM-DD): ");

        System.out.println("\nTipos de Contrato:");
        System.out.println("1. Tiempo Completo\n2. Medio Tiempo\n3. Por Hora\n4. Temporal");
        int opContrato = leerEntero("Seleccione el tipo de contrato: ");
        TipoContrato contrato;
        switch (opContrato) {
            case 1 -> contrato = TipoContrato.TIEMPO_COMPLETO;
            case 2 -> contrato = TipoContrato.MEDIO_TIEMPO;
            case 3 -> contrato = TipoContrato.POR_HORA;
            default -> contrato = TipoContrato.TEMPORAL;
        }

        System.out.println("\nCategoria de Empleado:");
        System.out.println("1. Por Hora\n2. Fijo Tiempo Completo\n3. Supervisor\n4. Gerente");
        int categoria = leerEntero("Seleccione la categoria: ");

        Empleado nuevoEmpleado = null;
        double sueldoMensual = 0.0; 

        switch (categoria) {
            case 1:
                double tarifa = leerDecimal("Ingrese la tarifa por hora (L.): ");
                int horas = leerEntero("Ingrese las horas trabajadas in la quincena: ");
                nuevoEmpleado = new EmpleadoPorHora(id, nombre, depto, fecha, contrato, 0.0, tarifa, horas);
                break;

            case 2:
                sueldoMensual = leerDecimal("Ingrese el salario mensual base (L.): ");
                nuevoEmpleado = new EmpleadoFijo(sueldoMensual);
                break;

            case 3:
                sueldoMensual = leerDecimal("Ingrese el salario mensual base (L.): ");
                double bono = leerDecimal("Ingrese el porcentaje de bono por productividad (0.00 a 0.20): ");
                nuevoEmpleado = new Supervisor(sueldoMensual, bono);
                break;

            case 4:
                sueldoMensual = leerDecimal("Ingrese el salario mensual base (L.): ");
                double bonoGer = leerDecimal("Ingrese el porcentaje de bono de supervisor (0.00 a 0.20): ");
                double ventas = leerDecimal("Ingrese las ventas totales del departamento (L.): ");
                double comision = leerDecimal("Ingrese el porcentaje de comision por ventas (ej. 0.05 para 5%): ");
                nuevoEmpleado = new Gerente(sueldoMensual, bonoGer, ventas, comision);
                break;

            default:
                System.out.println("Categoria invalida. Registro cancelado.");
                return;
        }

        if (nuevoEmpleado != null) {
            nuevoEmpleado.setId(id);
            nuevoEmpleado.setNombre(nombre);
            nuevoEmpleado.setDepartamento(depto);
            nuevoEmpleado.setFechaIngreso(fecha);
            nuevoEmpleado.setContrato(contrato);
            
            if (categoria == 1) {
                nuevoEmpleado.setSalarioBase(0.0);
            } else {
                nuevoEmpleado.setSalarioBase(sueldoMensual / 2);
            }

            if (gestor.agregarEmpleado(nuevoEmpleado)) {
                System.out.println("!Empleado registrado exitosamente!");
            } else {
                System.out.println("Error: No se pudo registrar. Cupo maximo alcanzado.");
            }
        }
    }

    private static void calcularNomina() {
        System.out.println("\n--- PROCESAMIENTO DE NOMINA ---");
        if (gestor.getCantidadEmpleados() == 0) {
            System.out.println("No hay empleados en la plantilla para calcular la nomina.");
            return;
        }
        
        String periodo = leerCadena("Ingrese el periodo actual (Ej: 2026-06-Q2): ");
        double totalBruto = gestor.calcularTotalBruto();
        
        System.out.printf("El calculo de la nomina para el periodo %s ha sido completado.\n", periodo);
        System.out.printf("Monto bruto proyectado: L. %,.2f\n", totalBruto);
        
        if (gestor.guardarTotalesBinario(periodo)) {
            System.out.println("Totales de periodo acumulados correctamente en el historial binario.");
        }
    }

    private static void exportarCSV() {
        System.out.println("\n--- EXPORTACION DE REPORTES ---");
        if (gestor.getCantidadEmpleados() == 0) {
            System.out.println("No hay empleados registrados para exportar.");
            return;
        }
        
        String archivoGenerado = gestor.generarReporteCSV();
        if (archivoGenerado != null) {
            System.out.println("Reporte CSV quincenal exportado con exito: " + archivoGenerado);
        }
    }

    private static void verHistorialTotales() {
        System.out.println("\n--- HISTORIAL DE AUDITORIA (BINARIO) ---");
        List<GestorNomina.RegistroHistorial> historial = gestor.cargarTotalesBinario();
        
        if (historial.isEmpty()) {
            System.out.println("El historial de auditoria binaria se encuentra vacio.");
        } else {
            for (GestorNomina.RegistroHistorial registro : historial) {
                System.out.println(registro);
            }
        }
    }

    private static void buscarEmpleado() {
        System.out.println("\n--- BUSQUEDA DE EMPLEADO ---");
        String id = leerCadena("Ingrese el ID a buscar: ");
        Empleado emp = gestor.buscarPorId(id);
        
        if (emp != null) {
            System.out.println("\nInformacion del Empleado:");
            System.out.println("---------------------------------------------");
            System.out.println("ID: " + emp.getId());
            System.out.println("Nombre: " + emp.getNombre());
            System.out.println("Departamento: " + emp.getDepartamento());
            System.out.println("Categoria: " + emp.getCategoria());
            System.out.println("Contrato: " + emp.getContrato().getDescripcion());
            System.out.printf("Pago de esta Quincena: L. %,.2f\n", emp.calcularPagoQuincenal());
            System.out.println("---------------------------------------------");
        } else {
            System.out.println("Empleado con ID " + id + " no fue encontrado en el sistema.");
        }
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(entrada.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un numero entero valido.");
            }
        }
    }

    private static double leerDecimal(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Double.parseDouble(entrada.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un valor numerico decimal valido.");
            }
        }
    }

    private static String leerCadena(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String valor = entrada.nextLine().trim();
            if (!valor.isEmpty()) {
                return valor;
            }
            System.out.println("Error: El campo no puede quedar vacio.");
        }
    }
}
