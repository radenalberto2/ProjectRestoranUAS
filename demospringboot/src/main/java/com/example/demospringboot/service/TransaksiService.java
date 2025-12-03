package com.example.demospringboot.service;

import com.example.demospringboot.entity.*;
import com.example.demospringboot.repository.TransaksiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransaksiService {

    @Autowired
    private TransaksiRepository transaksiRepository;

    public List<Transaksi> getAllTransaksi() {
        return transaksiRepository.findAll();
    }

    public Transaksi getTransaksiById(Long id) {
        return transaksiRepository.findById(id).orElse(null);
    }

    @Transactional
    public Transaksi saveTransaksi(Transaksi transaksi) {
        // Hitung total otomatis dari items
        if (transaksi.getItems() != null && !transaksi.getItems().isEmpty()) {
            transaksi.calculateTotal();
        }
        
        // Update timestamp jika transaksi baru
        if (transaksi.getId() == null) {
            transaksi.setWaktuTransaksi(java.time.LocalDateTime.now());
        }
        
        return transaksiRepository.save(transaksi);
    }

    @Transactional
    public void deleteTransaksi(Long id) {
        transaksiRepository.deleteById(id);
    }

    // Method untuk membuat transaksi dari pelanggan (nanti diintegrasikan dengan keranjang)
    @Transactional
    public Transaksi createTransaksiFromPelanggan(Long pelangganId, List<TransaksiDetail> details, 
                                                  String metodePembayaran) {
        // Implementasi nanti setelah ada keranjang
        return null;
    }
    
    // Method untuk membuat transaksi dari keranjang belanja
    @Transactional
    public Transaksi createTransaksiFromKeranjang(Pelanggan pelanggan, List<KeranjangItem> keranjangItems,
                                                   String metodePembayaran, String catatan) {
        // Gunakan builder pattern untuk membuat transaksi
        Transaksi.Builder builder = Transaksi.builder()
            .pelanggan(pelanggan)
            .metodePembayaran(metodePembayaran)
            .catatan(catatan);
        
        // Konversi setiap KeranjangItem menjadi TransaksiDetail dan tambahkan ke transaksi
        for (KeranjangItem item : keranjangItems) {
            TransaksiDetail detail = new TransaksiDetail(
                item.getTipeMenu(),
                item.getIdMenu(),
                item.getNamaMenu(),
                item.getHarga(),
                item.getJumlah()
            );
            builder.addItem(detail);
        }
        
        // Build transaksi
        Transaksi transaksi = builder.build();
        
        // Set status berdasarkan metode pembayaran
        if ("SALDO".equals(metodePembayaran)) {
            transaksi.setStatus("SUCCESS");
        } else if ("TUNAI".equals(metodePembayaran)) {
            transaksi.setStatus("PENDING");
        }
        
        // Hitung total
        transaksi.calculateTotal();
        
        // Simpan ke database
        return transaksiRepository.save(transaksi);
    }

    public List<Transaksi> getTransaksiByDate(LocalDate date) {
        return transaksiRepository.findByTanggal(date);
    }

    public List<Transaksi> getTransaksiByStatus(String status) {
        return transaksiRepository.findByStatus(status);
    }

    public List<Transaksi> getSuccessfulTransactions() {
        return transaksiRepository.findSuccessfulTransactions();
    }
}