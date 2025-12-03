package com.example.demospringboot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "minuman")
@DiscriminatorValue("MINUMAN")
@PrimaryKeyJoinColumn(name = "id")
public class Minuman extends Harga {

    private String ukuran;

    public Minuman() {}

    public Minuman(String nama, int harga, String ukuran, int stock) {
        super(nama, harga, stock); // Oper ke Parent
        this.ukuran = ukuran;
    }

    public String getUkuran() { return ukuran; }
    public void setUkuran(String ukuran) { this.ukuran = ukuran; }
    
    @Override
    public String deskripsi() {
        if (getDeskripsiItem() != null && !getDeskripsiItem().isEmpty()) {
            return getDeskripsiItem();
        }
        return "Minuman: " + getNama() + " " + ukuran + " (Stok: " + getStock() + ")";
    }
}