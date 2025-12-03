package com.example.demospringboot.entity;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class KeranjangBelanja {
    
    private List<KeranjangItem> items = new ArrayList<>();
    private Long pelangganId;

    // Tambah item - bisa ada duplikat menu dengan jumlah berbeda
    public void tambahItem(KeranjangItem newItem) {
        items.add(newItem);
    }
    
    // Hapus item
    public void hapusItem(String tipeMenu, Long idMenu) {
        items.removeIf(item -> 
            item.getTipeMenu().equals(tipeMenu) && 
            item.getIdMenu().equals(idMenu)
        );
    }
    
    // Update jumlah
    public void updateJumlah(String tipeMenu, Long idMenu, int jumlahBaru) {
        for (KeranjangItem item : items) {
            if (item.getTipeMenu().equals(tipeMenu) && 
                item.getIdMenu().equals(idMenu)) {
                item.setJumlah(jumlahBaru);
                break;
            }
        }
    }
    
    // Kosongkan
    public void kosongkan() {
        items.clear();
    }
    
    // Getter
    public List<KeranjangItem> getItems() {
        return new ArrayList<>(items);
    }
    
    public int getTotalHarga() {
        int total = 0;
        for (KeranjangItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }
    
    public int getTotalItemCount() {
        int total = 0;
        for (KeranjangItem item : items) {
            total += item.getJumlah();
        }
        return total;
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    public Long getPelangganId() { return pelangganId; }
    public void setPelangganId(Long pelangganId) { this.pelangganId = pelangganId; }
}