package com.example.demospringboot.repository;

import com.example.demospringboot.entity.Pelanggan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PelangganRepository extends JpaRepository<Pelanggan, Long> {
    Pelanggan findByUsername(String username);
}