
package proyecto1lab;

/**
 *
 * @author spodi
 */
public class Gerente extends Supervisor{
private double ventasDepartamento ; 
private double porcentajeComision; 
    public Gerente(double SalarioMensual, double porcentajeBono, 
            double ventasDepartamento, double porcentajeComision) {
        super(SalarioMensual, porcentajeBono);
        this.porcentajeComision = porcentajeComision; 
        this.ventasDepartamento = ventasDepartamento; 
    }

    public double calculcarPagoQuincenal(){
        double pagoSupervisor = super.calcularPagoQuincenal(); 
        double comision = ventasDepartamento * porcentajeComision;
        return pagoSupervisor + comision; 
    }
}
