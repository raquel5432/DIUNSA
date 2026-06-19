
package proyecto1lab;

public class EmpleadoPorHora extends Empleado{

    private int horas; 
    private double tarifa; 
    private int extras; 
    
    
    public EmpleadoPorHora(int horas, double tarifa, int extras){
        this.horas = horas; 
        this.tarifa = tarifa; 
        this.extras = extras; 
    }
    
    @Override
    public double calcularPagoQuincenal(){
        double pago = tarifa * horas; 
        if(extras > 80 ){
        double horasExtrasSobreLimite = extras - 80; 
        pago += horasExtrasSobreLimite * tarifa * 1.5;
    }
        return pago; 
    }

    @Override
    public String getCategoria() {
        return "Por Hora"; 
    }
}
