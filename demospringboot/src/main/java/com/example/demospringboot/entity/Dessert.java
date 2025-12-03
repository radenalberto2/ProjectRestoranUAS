package com.example.demospringboot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dessert")
@DiscriminatorValue("DESSERT")
@PrimaryKeyJoinColumn(name = "id")
public class Dessert extends Harga {

    private String topping;

    public Dessert() {}

    public Dessert(String nama, int harga, String topping, int stock) {
        super(nama, harga, stock); // Oper ke Parent
        this.topping = topping;
    }

    public String getTopping() { return topping; }
    public void setTopping(String topping) { this.topping = topping; }

    @Override
    public String deskripsi() {
        if (getDeskripsiItem() != null && !getDeskripsiItem().isEmpty()) {
            return getDeskripsiItem();
        }
        return "Dessert: " + getNama() + " + " + topping + " (Stok: " + getStock() + ")";
    }
}