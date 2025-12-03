package com.example.demospringboot.entity;

public class KeranjangItem {
    private String tipeMenu;
    private Long idMenu;
    private String namaMenu;
    private int harga;
    private int jumlah;
    private int stokTersedia;

    public KeranjangItem() {}

    public KeranjangItem(String tipeMenu, Long idMenu, String namaMenu, int harga, int jumlah, int stokTersedia) {
        this.tipeMenu = tipeMenu;
        this.idMenu = idMenu;
        this.namaMenu = namaMenu;
        this.harga = harga;
        this.jumlah = jumlah;
        this.stokTersedia = stokTersedia;
    }

    // Getter & Setter
    public String getTipeMenu() { return tipeMenu; }
    public void setTipeMenu(String tipeMenu) { this.tipeMenu = tipeMenu; }
    
    public Long getIdMenu() { return idMenu; }
    public void setIdMenu(Long idMenu) { this.idMenu = idMenu; }
    
    public String getNamaMenu() { return namaMenu; }
    public void setNamaMenu(String namaMenu) { this.namaMenu = namaMenu; }
    
    public int getHarga() { return harga; }
    public void setHarga(int harga) { this.harga = harga; }
    
    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    
    public int getStokTersedia() { return stokTersedia; }
    public void setStokTersedia(int stokTersedia) { this.stokTersedia = stokTersedia; }
    
    public int getSubtotal() { return harga * jumlah; }
}