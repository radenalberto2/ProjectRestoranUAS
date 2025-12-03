package com.example.demospringboot.repository;

import com.example.demospringboot.entity.Minuman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MinumanRepository extends JpaRepository<Minuman, Long> {
}