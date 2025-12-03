package com.example.demospringboot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "harga")
@PrimaryKeyJoinColumn(name = "id")
public abstract class Harga extends MenuItem {

    @Column(nullable = false)
    protected int harga;

    @Column(nullable = false)
    protected int stock; // Ini sumber utama stok

    public Harga() {}

    public Harga(String nama, int harga, int stock) {
        super(nama);
        this.harga = harga;
        this.stock = stock;
    }

    public int getHarga() { return harga; }
    public void setHarga(int harga) { this.harga = harga; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    // Method logika pengurangan stock cukup di sini saja (Parent)
    // Anak-anaknya (Makanan/Minuman) otomatis punya method ini.
    public void kurangiStock(int jumlah) {
        if (jumlah <= this.stock) {
            this.stock -= jumlah;
        } else {
            throw new IllegalArgumentException("Stok tidak cukup");
        }
    }

    @Override
    public String deskripsi() {
        // Jika admin isi deskripsi manual, pakai itu. Jika tidak, pakai format default.
        if (getDeskripsiItem() != null && !getDeskripsiItem().isEmpty()) {
            return getDeskripsiItem();
        }
        return getNama() + " - Rp" + harga + " (Stok: " + stock + ")";
    }
}