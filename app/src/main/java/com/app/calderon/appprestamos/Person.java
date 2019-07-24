package com.app.calderon.appprestamos;

public class Person {

    private String name;
    private int quantity;
    private int plazos;
    private int pagos;
    private int saldo;
    private String fecha;
    private int positionID;



    public Person(String name, int quantity, int plazos, int pagos, int saldo, String fecha, int positionID) {
        this.name = name;
        this.quantity = quantity;
        this.plazos = plazos;
        this.pagos = pagos;
        this.saldo = saldo;
        this.fecha = fecha;
        this.positionID = positionID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public int getPlazos() {
        return plazos;
    }

    public void setPlazos(int plazos) {
        this.plazos = plazos;
    }

    public int getPagos() {
        return pagos;
    }

    public void setPagos(int pagos) {
        this.pagos = pagos;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getPositionID() {
        return positionID;
    }

    public void setPositionID(int positionID) {
        this.positionID = positionID;
    }
}
