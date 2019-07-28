package com.app.calderon.appprestamos.Models;

public class Details {

    private String nombre;
    private String fecha;
    private int cantidad;
    private int type;

    public Details(String fecha,String nombre, int cantidad, int type) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.type = type;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
