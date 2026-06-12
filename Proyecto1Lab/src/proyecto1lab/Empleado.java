/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1lab;

import java.io.Serializable;

/**
 *
 * @author alira
 */
public abstract class Empleado implements Remunerado, Serializable {
    
    private static final long serialVersionUID = 1L;
 
    private String id;
    private String nombre;
    private String departamento;
    private String fechaIngreso;
    private TipoContrato contrato;
    private double salarioBase;
 
    public Empleado() {
    }
 
    public Empleado(String id, String nombre, String departamento,
                    String fechaIngreso, TipoContrato contrato, double salarioBase) {
        this.id = id;
        this.nombre = nombre;
        this.departamento = departamento;
        this.fechaIngreso = fechaIngreso;
        this.contrato = contrato;
        this.salarioBase = salarioBase;
    }
 
    public String getId() {
        return id;
    }
 
    public void setId(String id) {
        this.id = id;
    }
 
    public String getNombre() {
        return nombre;
    }
 
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
 
    public String getDepartamento() {
        return departamento;
    }
 
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
 
    public String getFechaIngreso() {
        return fechaIngreso;
    }
 
    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
 
    public TipoContrato getContrato() {
        return contrato;
    }
 
    public void setContrato(TipoContrato contrato) {
        this.contrato = contrato;
    }
 
    public double getSalarioBase() {
        return salarioBase;
    }
 
    public void setSalarioBase(double salarioBase) {
        this.salarioBase = salarioBase;
    }
 
    @Override
    public abstract double calcularPagoQuincenal();
 
    public abstract String getCategoria();
 
    @Override
    public String generarLineaNomina() {
        return id + "," + nombre + "," + departamento + "," + getCategoria() + ","
                + contrato + "," + salarioBase + "," + calcularPagoQuincenal();
    }
 
    @Override
    public String toString() {
        return "Empleado{"
                + "id='" + id + '\''
                + ", nombre='" + nombre + '\''
                + ", departamento='" + departamento + '\''
                + ", fechaIngreso='" + fechaIngreso + '\''
                + ", contrato=" + contrato
                + ", salarioBase=" + salarioBase
                + '}';
    }
}
