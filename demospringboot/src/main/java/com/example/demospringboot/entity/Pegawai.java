package com.example.demospringboot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pegawai")
@DiscriminatorValue("PEGAWAI")
public class Pegawai extends People implements Login {

    private String jabatan;
    private Double gaji; // Sesuai diagram (Double)
    private boolean loggedIn; // Sesuai diagram

    @Column(unique = true)
    private String username;
    private String password;

    public Pegawai() { super(); }

    public Pegawai(String nama, String noHP, String email, String jabatan, Double gaji, String username, String password) {
        super(nama, noHP, email);
        this.jabatan = jabatan;
        this.gaji = gaji;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean signIn(String u, String p) {
        boolean isSuccess = this.username.equals(u) && this.password.equals(p);
        if(isSuccess) this.loggedIn = true;
        return isSuccess;
    }

    @Override
    public void signOut() { this.loggedIn = false; }

    @Override
    public boolean resetPassword(String newPassword) {
        this.password = newPassword;
        return true;
    }

    // Getter Setter
    public String getJabatan() { return jabatan; }
    public void setJabatan(String jabatan) { this.jabatan = jabatan; }
    public Double getGaji() { return gaji; }
    public void setGaji(Double gaji) { this.gaji = gaji; }
    public boolean isLoggedIn() { return loggedIn; }
    public void setLoggedIn(boolean loggedIn) { this.loggedIn = loggedIn; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}