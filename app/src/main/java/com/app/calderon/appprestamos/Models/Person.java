package com.app.calderon.appprestamos.Models;

public class Person {

    private String name;
    private int quantity;
    private int plazos;
    private int pagos;
    private int saldo;
    private String fechaInicial;
    private String fechaFinal;
    private int positionID;
    private int added;
    private int payment;

    public Person(String name, int quantity, int plazos, int pagos, int saldo, String fechaInicial, String fechaFinal, int positionID, int added, int payment) {
        this.name = name;
        this.quantity = quantity;
        this.plazos = plazos;
        this.pagos = pagos;
        this.saldo = saldo;
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
        this.positionID = positionID;
        this.added = added;
        this.payment = payment;
    }

    public Person(String name, int quantity, int plazos, int pagos, int saldo, String fechaInicial, String fechaFinal, int positionID, int added) {
        this.name = name;
        this.quantity = quantity;
        this.plazos = plazos;
        this.pagos = pagos;
        this.saldo = saldo;
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
        this.positionID = positionID;
        this.added = added;
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

    public String getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(String fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public int getPositionID() {
        return positionID;
    }

    public void setPositionID(int positionID) {
        this.positionID = positionID;
    }

    public int getAdded() {
        return added;
    }

    public void setAdded(int added) {
        this.added = added;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ",\n quantity=" + quantity +
                ",\n plazos=" + plazos +
                ",\n pagos=" + pagos +
                ",\n saldo=" + saldo +
                ",\n fechaInicial='" + fechaInicial + '\'' +
                ",\n fechaFinal='" + fechaFinal + '\'' +
                ",\n positionID=" + positionID +
                ", added=" + added +
                '}';
    }
}
