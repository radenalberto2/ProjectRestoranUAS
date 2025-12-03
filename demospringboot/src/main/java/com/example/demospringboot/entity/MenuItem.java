package com.example.demospringboot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "menu_item")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipe_menu")
public abstract class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nama;
    
    // --- INI YANG KEMUNGKINAN HILANG/BELUM ADA ---
    private String deskripsiItem; 

    public MenuItem() {
    }

    public MenuItem(String nama) {
        this.nama = nama;
    }

    // Getter Setter ID & Nama
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    // --- PASTIKAN GETTER & SETTER INI ADA ---
    public String getDeskripsiItem() { return deskripsiItem; }
    public void setDeskripsiItem(String deskripsiItem) { this.deskripsiItem = deskripsiItem; }

    public abstract String deskripsi();
}