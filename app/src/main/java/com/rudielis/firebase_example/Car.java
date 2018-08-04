package com.rudielis.firebase_example;

public class Car {
    public String nombre;
    public String url;
    public Double precio;

    public String getUrl() {
        return url;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
