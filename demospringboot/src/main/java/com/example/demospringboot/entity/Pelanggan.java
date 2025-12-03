package com.example.demospringboot.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "pelanggan")
@DiscriminatorValue("PELANGGAN")
public class Pelanggan extends People implements Login {

    private String alamatPelanggan;
    private String membershipLevel;
    private int riwayatPesanan;
    private boolean loggedIn;

    private Double saldo;

    @Column(unique = true)
    private String username;
    private String password;

    @OneToMany(mappedBy = "pelanggan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaksi> transaksiList;

    public Pelanggan() {
        super();
        this.saldo = 0.0;
    }

    public Pelanggan(String nama, String noHP, String email, String alamatPelanggan, String username, String password,
            Double saldo) {
        super(nama, noHP, email);
        this.alamatPelanggan = alamatPelanggan;
        this.username = username;
        this.password = password;
        this.saldo = saldo;
        this.membershipLevel = "Bronze";
        this.riwayatPesanan = 0;
    }

    @Override
    public boolean signIn(String u, String p) {
        boolean isSuccess = this.username.equals(u) && this.password.equals(p);
        if (isSuccess)
            this.loggedIn = true;
        return isSuccess;
    }

    @Override
    public void signOut() {
        this.loggedIn = false;
    }

    @Override
    public boolean resetPassword(String newPassword) {
        this.setPassword(newPassword);
        return true;
    }

    public String getAlamatPelanggan() {
        return alamatPelanggan;
    }

    public void setAlamatPelanggan(String alamatPelanggan) {
        this.alamatPelanggan = alamatPelanggan;
    }

    public String getMembershipLevel() {
        return membershipLevel;
    }

    public void setMembershipLevel(String membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    public int getRiwayatPesanan() {
        return riwayatPesanan;
    }

    public void setRiwayatPesanan(int riwayatPesanan) {
        this.riwayatPesanan = riwayatPesanan;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}