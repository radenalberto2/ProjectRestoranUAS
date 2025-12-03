package com.example.demospringboot.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transaksi")
public class Transaksi {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "no_transaksi", unique = true)
    private String noTransaksi;
    
    private LocalDate tanggal;
    
    @Column(name = "waktu_transaksi")
    private LocalDateTime waktuTransaksi;
    
    @ManyToOne
    @JoinColumn(name = "pelanggan_id")
    private Pelanggan pelanggan;
    
    @OneToMany(mappedBy = "transaksi", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransaksiDetail> items = new ArrayList<>();
    
    private String catatan;
    
    @Column(name = "total_bayar")
    private Integer totalBayar;
    
    @Column(name = "metode_pembayaran")
    private String metodePembayaran;
    
    private Integer kembalian;
    
    private String status; // PENDING, SUCCESS, FAILED, CANCELLED
    
    @Column(name = "tipe_transaksi")
    private String tipeTransaksi; // PELANGGAN, ADMIN
    
    // ========== CONSTRUCTORS ==========
    
    public Transaksi() {
        this.tanggal = LocalDate.now();
        this.waktuTransaksi = LocalDateTime.now();
        this.status = "PENDING";
        this.noTransaksi = generateNoTransaksi();
        this.tipeTransaksi = "PELANGGAN";
    }
    
    // Constructor untuk transaksi manual dari admin
    public Transaksi(Pelanggan pelanggan, String catatan, Integer totalBayar, String metodePembayaran) {
        this();
        this.pelanggan = pelanggan;
        this.catatan = catatan;
        this.totalBayar = totalBayar;
        this.metodePembayaran = metodePembayaran;
        this.status = "SUCCESS";
        this.tipeTransaksi = "ADMIN";
    }
    
    // ========== BUSINESS LOGIC METHODS ==========
    
    private String generateNoTransaksi() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        return "TRX" + LocalDateTime.now().format(formatter);
    }
    
    public void addItem(TransaksiDetail item) {
        items.add(item);
        item.setTransaksi(this);
        calculateTotal();
    }
    
    public void removeItem(TransaksiDetail item) {
        items.remove(item);
        item.setTransaksi(null);
        calculateTotal();
    }
    
    public void calculateTotal() {
        if (items != null && !items.isEmpty()) {
            this.totalBayar = items.stream()
                    .mapToInt(TransaksiDetail::getSubtotal)
                    .sum();
        }
    }
    
    public void processPayment(Integer jumlahBayar) {
        if (jumlahBayar != null && this.totalBayar != null) {
            this.kembalian = jumlahBayar - this.totalBayar;
            if (this.kembalian >= 0) {
                this.status = "SUCCESS";
            } else {
                this.status = "FAILED";
            }
        }
    }
    
    public boolean isSuccess() {
        return "SUCCESS".equals(this.status);
    }
    
    public boolean isPending() {
        return "PENDING".equals(this.status);
    }
    
    public boolean isFailed() {
        return "FAILED".equals(this.status);
    }
    
    // ========== BUILDER PATTERN ==========
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private Pelanggan pelanggan;
        private String catatan;
        private String metodePembayaran;
        private String tipeTransaksi = "PELANGGAN";
        private List<TransaksiDetail> items = new ArrayList<>();
        
        public Builder pelanggan(Pelanggan pelanggan) {
            this.pelanggan = pelanggan;
            return this;
        }
        
        public Builder catatan(String catatan) {
            this.catatan = catatan;
            return this;
        }
        
        public Builder metodePembayaran(String metodePembayaran) {
            this.metodePembayaran = metodePembayaran;
            return this;
        }
        
        public Builder tipeTransaksi(String tipeTransaksi) {
            this.tipeTransaksi = tipeTransaksi;
            return this;
        }
        
        public Builder addItem(TransaksiDetail item) {
            this.items.add(item);
            return this;
        }
        
        public Transaksi build() {
            Transaksi transaksi = new Transaksi();
            transaksi.setPelanggan(pelanggan);
            transaksi.setCatatan(catatan);
            transaksi.setMetodePembayaran(metodePembayaran);
            transaksi.setTipeTransaksi(tipeTransaksi);
            
            // Add all items
            if (items != null && !items.isEmpty()) {
                items.forEach(transaksi::addItem);
            }
            
            return transaksi;
        }
    }
    
    // ========== GETTERS & SETTERS ==========
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNoTransaksi() {
        return noTransaksi;
    }
    
    public void setNoTransaksi(String noTransaksi) {
        this.noTransaksi = noTransaksi;
    }
    
    public LocalDate getTanggal() {
        return tanggal;
    }
    
    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }
    
    public LocalDateTime getWaktuTransaksi() {
        return waktuTransaksi;
    }
    
    public void setWaktuTransaksi(LocalDateTime waktuTransaksi) {
        this.waktuTransaksi = waktuTransaksi;
    }
    
    public Pelanggan getPelanggan() {
        return pelanggan;
    }
    
    public void setPelanggan(Pelanggan pelanggan) {
        this.pelanggan = pelanggan;
    }
    
    public List<TransaksiDetail> getItems() {
        return items;
    }
    
    public void setItems(List<TransaksiDetail> items) {
        this.items = items;
        if (items != null) {
            items.forEach(item -> item.setTransaksi(this));
        }
        calculateTotal();
    }
    
    public String getCatatan() {
        return catatan;
    }
    
    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }
    
    public Integer getTotalBayar() {
        return totalBayar;
    }
    
    public void setTotalBayar(Integer totalBayar) {
        this.totalBayar = totalBayar;
    }
    
    public String getMetodePembayaran() {
        return metodePembayaran;
    }
    
    public void setMetodePembayaran(String metodePembayaran) {
        this.metodePembayaran = metodePembayaran;
    }
    
    public Integer getKembalian() {
        return kembalian;
    }
    
    public void setKembalian(Integer kembalian) {
        this.kembalian = kembalian;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getTipeTransaksi() {
        return tipeTransaksi;
    }
    
    public void setTipeTransaksi(String tipeTransaksi) {
        this.tipeTransaksi = tipeTransaksi;
    }
    
    @Override
    public String toString() {
        return "Transaksi{" +
                "noTransaksi='" + noTransaksi + '\'' +
                ", tanggal=" + tanggal +
                ", pelanggan=" + (pelanggan != null ? pelanggan.getNama() : "null") +
                ", totalBayar=" + totalBayar +
                ", status='" + status + '\'' +
                '}';
    }
}