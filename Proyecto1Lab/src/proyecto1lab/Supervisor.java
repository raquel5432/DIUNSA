
package proyecto1lab;

public class Supervisor extends EmpleadoFijo{
    private double porcentajeBono; 
    
    public Supervisor(double SalarioMensual, double porcentajeBono) {
        super(SalarioMensual);
        
        if(porcentajeBono > 0.20){
            this.porcentajeBono = 0.20; 
            
        }else if (porcentajeBono < 0){
            this.porcentajeBono = 0;
        } else{
            this.porcentajeBono = porcentajeBono;
        }
        
    }
     public double calcularPagoQuincenal(){
        double pagoBase = super.calcularPagoQuincenal();
        double bono = pagoBase * porcentajeBono; 
        return pagoBase + bono; 
    }
    
   
}
