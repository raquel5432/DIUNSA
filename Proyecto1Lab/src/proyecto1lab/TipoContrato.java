/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package proyecto1lab;

/**
 *
 * @author alira
 */
public enum TipoContrato {
    
    TIEMPO_COMPLETO("Tiempo Completo", 0.30),
    MEDIO_TIEMPO("Medio Tiempo",0.20),
    POR_HORA("Por Horas", 0.01),
    TEMPORAL("Temporal", 0.15);
    
    private final String descripcion;
    private final double factorPrestaciones;
    
    TipoContrato(String descripcion, double factorPrestaciones){
        
        this.descripcion = descripcion;
        this.factorPrestaciones = factorPrestaciones;
    }
    
    public String getDescripcion(){
        return descripcion;
    }
    
    public double getFactorPrestaciones(){
        
        return factorPrestaciones;
    }
    
    @Override
    public String toString() {
        return descripcion + " (Prestaciones: " + (factorPrestaciones * 100) + "%)";
    }
}
