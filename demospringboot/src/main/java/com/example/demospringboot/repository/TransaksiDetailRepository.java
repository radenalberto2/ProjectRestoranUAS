package com.example.demospringboot.repository;

import com.example.demospringboot.entity.TransaksiDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaksiDetailRepository extends JpaRepository<TransaksiDetail, Long> {
    List<TransaksiDetail> findByTransaksiId(Long transaksiId);
}