package com.example.demospringboot.repository;

import com.example.demospringboot.entity.Transaksi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransaksiRepository extends JpaRepository<Transaksi, Long> {
    
    // Cari transaksi berdasarkan status
    List<Transaksi> findByStatus(String status);
    
    // Cari transaksi berdasarkan tanggal
    List<Transaksi> findByTanggal(LocalDate tanggal);
    
    // Cari transaksi berdasarkan pelanggan
    List<Transaksi> findByPelangganId(Long pelangganId);
    
    // Cari transaksi berdasarkan rentang tanggal
    @Query("SELECT t FROM Transaksi t WHERE t.tanggal BETWEEN :startDate AND :endDate")
    List<Transaksi> findBetweenDates(@Param("startDate") LocalDate startDate, 
                                     @Param("endDate") LocalDate endDate);
    
    // Cari transaksi sukses
    @Query("SELECT t FROM Transaksi t WHERE t.status = 'SUCCESS' ORDER BY t.waktuTransaksi DESC")
    List<Transaksi> findSuccessfulTransactions();
}