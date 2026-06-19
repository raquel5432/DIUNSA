
package proyecto1lab;


public class EmpleadoFijo extends Empleado{
    private double salarioMensual; 
        
    public EmpleadoFijo(double salarioMensual){
        this.salarioMensual = salarioMensual; 
    }
    
    @Override
    public double calcularPagoQuincenal(){
        double pagoBase = salarioMensual / 2; 
        
        double deduccionIHSS = pagoBase * 0.035; 
        double deduccionRAP = pagoBase * 0.015; 
        
        double totalDeDeducciones = deduccionIHSS + deduccionRAP; 
        
                
          return pagoBase - totalDeDeducciones;      
    }

    @Override
    public String getCategoria() {
    return "Fijo"; 
    }
}
