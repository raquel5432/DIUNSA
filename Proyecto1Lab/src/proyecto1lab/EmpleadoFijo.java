
package proyecto1lab;


public class EmpleadoFijo {
    private double salarioMensual; 
        
    public EmpleadoFijo(double salarioMensual){
        this.salarioMensual = salarioMensual; 
    }
    
    public double calcularPagoQuincenal(){
        double pagoBase = salarioMensual / 2; 
        
        double deduccionIHSS = pagoBase * 3.5; 
        double deduccionRAP = pagoBase * 1.5; 
        
        double totalDeDeducciones = deduccionIHSS + deduccionRAP; 
        
                
          return pagoBase - totalDeDeducciones;      
    }
}
