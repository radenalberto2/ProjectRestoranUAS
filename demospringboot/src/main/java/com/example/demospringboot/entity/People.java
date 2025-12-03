package com.example.demospringboot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "people")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class People {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Tetap Long biar database aman

    private String nama;
    private String noHP; // Sesuaikan dengan diagram (P besar)
    private String email;

    public People() {}

    public People(String nama, String noHP, String email) {
        this.nama = nama;
        this.noHP = noHP;
        this.email = email;
    }

    // Getter Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getNoHP() { return noHP; }
    public void setNoHP(String noHP) { this.noHP = noHP; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}