package com.example.demospringboot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "transaksi_detail")
public class TransaksiDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "transaksi_id")
    private Transaksi transaksi;
    
    @Column(name = "tipe_menu")
    private String tipeMenu; // MAKANAN, MINUMAN, DESSERT
    
    @Column(name = "menu_id")
    private Long menuId;
    
    @Column(name = "nama_menu")
    private String namaMenu;
    
    @Column(name = "harga_satuan")
    private Integer hargaSatuan;
    
    private Integer jumlah;
    
    private Integer subtotal;
    
    // ========== CONSTRUCTORS ==========
    
    public TransaksiDetail() {}
    
    public TransaksiDetail(String tipeMenu, Long menuId, String namaMenu, Integer hargaSatuan, Integer jumlah) {
        this.tipeMenu = tipeMenu;
        this.menuId = menuId;
        this.namaMenu = namaMenu;
        this.hargaSatuan = hargaSatuan;
        this.jumlah = jumlah;
        calculateSubtotal();
    }
    
    // ========== BUSINESS LOGIC METHODS ==========
    
    public void calculateSubtotal() {
        if (hargaSatuan != null && jumlah != null) {
            this.subtotal = hargaSatuan * jumlah;
        }
    }
    
    public void increaseQuantity(int amount) {
        if (amount > 0) {
            this.jumlah += amount;
            calculateSubtotal();
        }
    }
    
    public void decreaseQuantity(int amount) {
        if (amount > 0) {
            this.jumlah = Math.max(0, this.jumlah - amount);
            calculateSubtotal();
        }
    }
    
    // ========== BUILDER PATTERN ==========
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String tipeMenu;
        private Long menuId;
        private String namaMenu;
        private Integer hargaSatuan;
        private Integer jumlah = 1;
        
        public Builder tipeMenu(String tipeMenu) {
            this.tipeMenu = tipeMenu;
            return this;
        }
        
        public Builder menuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }
        
        public Builder namaMenu(String namaMenu) {
            this.namaMenu = namaMenu;
            return this;
        }
        
        public Builder hargaSatuan(Integer hargaSatuan) {
            this.hargaSatuan = hargaSatuan;
            return this;
        }
        
        public Builder jumlah(Integer jumlah) {
            this.jumlah = jumlah;
            return this;
        }
        
        public TransaksiDetail build() {
            TransaksiDetail detail = new TransaksiDetail();
            detail.setTipeMenu(tipeMenu);
            detail.setMenuId(menuId);
            detail.setNamaMenu(namaMenu);
            detail.setHargaSatuan(hargaSatuan);
            detail.setJumlah(jumlah);
            detail.calculateSubtotal();
            return detail;
        }
    }
    
    // ========== GETTERS & SETTERS ==========
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Transaksi getTransaksi() {
        return transaksi;
    }
    
    public void setTransaksi(Transaksi transaksi) {
        this.transaksi = transaksi;
    }
    
    public String getTipeMenu() {
        return tipeMenu;
    }
    
    public void setTipeMenu(String tipeMenu) {
        this.tipeMenu = tipeMenu;
    }
    
    public Long getMenuId() {
        return menuId;
    }
    
    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
    
    public String getNamaMenu() {
        return namaMenu;
    }
    
    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }
    
    public Integer getHargaSatuan() {
        return hargaSatuan;
    }
    
    public void setHargaSatuan(Integer hargaSatuan) {
        this.hargaSatuan = hargaSatuan;
        calculateSubtotal();
    }
    
    public Integer getJumlah() {
        return jumlah;
    }
    
    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
        calculateSubtotal();
    }
    
    public Integer getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(Integer subtotal) {
        this.subtotal = subtotal;
    }
    
    @Override
    public String toString() {
        return "TransaksiDetail{" +
                "tipeMenu='" + tipeMenu + '\'' +
                ", namaMenu='" + namaMenu + '\'' +
                ", hargaSatuan=" + hargaSatuan +
                ", jumlah=" + jumlah +
                ", subtotal=" + subtotal +
                '}';
    }
}