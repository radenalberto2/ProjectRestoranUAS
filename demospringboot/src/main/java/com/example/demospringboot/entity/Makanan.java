package com.example.demospringboot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "makanan")
@DiscriminatorValue("MAKANAN")
@PrimaryKeyJoinColumn(name = "id")
public class Makanan extends Harga { 

    // HAPUS 'private int stock;' DISINI!
    // Karena Makanan sudah mewarisi 'stock' dari Harga.

    public Makanan() {}

    public Makanan(String nama, int harga, int stock) {
        super(nama, harga, stock); // Oper ke Parent (Harga)
    }

    // Tidak perlu getter/setter stock lagi disini
    // Tidak perlu kurangiStock lagi disini
    // Semuanya sudah diurus oleh class Harga (Parent)
    
    @Override
    public String deskripsi() {
        if (getDeskripsiItem() != null && !getDeskripsiItem().isEmpty()) {
            return getDeskripsiItem();
        }
        return "Makanan: " + getNama() + " (Stok: " + getStock() + ")";
    }
}