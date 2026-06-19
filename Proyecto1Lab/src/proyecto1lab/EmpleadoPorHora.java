package proyecto1lab;

public class EmpleadoPorHora extends Empleado {

    private double tarifa;
    private int horasTrabajadas; 

    public EmpleadoPorHora(String id, String nombre, String departamento,
                           String fechaIngreso, TipoContrato contrato,
                           double salarioBase, double tarifa, int horasTrabajadas) {
        super(id, nombre, departamento, fechaIngreso, contrato, salarioBase);
        this.tarifa          = tarifa;
        this.horasTrabajadas = horasTrabajadas;
    }

    @Override
    public double calcularPagoQuincenal() {
        double pago;

        if (horasTrabajadas <= 80) {
         
            pago = horasTrabajadas * tarifa;
        } else {
           
            double horasNormales = 80 * tarifa;
            double horasExtras   = (horasTrabajadas - 80) * tarifa * 1.5;
            pago = horasNormales + horasExtras;
        }

        return pago;
    }

    @Override
    public String getCategoria() {
        return "Por Hora";
    }
}