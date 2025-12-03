package com.example.demospringboot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String kode;
    private String name;
    private String noHP;

    public Person() {}

    public Person(String kode, String name, String noHP) {
        this.kode = kode;
        this.name = name;
        this.noHP = noHP;
    }

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNoHP() { return noHP; }
    public void setNoHP(String noHP) { this.noHP = noHP; }
}
