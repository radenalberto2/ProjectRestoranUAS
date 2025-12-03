package com.example.demospringboot.repository;

import com.example.demospringboot.entity.Makanan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MakananRepository extends JpaRepository<Makanan, Long> {
}