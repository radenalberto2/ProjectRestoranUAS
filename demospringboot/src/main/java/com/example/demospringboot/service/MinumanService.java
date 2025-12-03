package com.example.demospringboot.service;

import com.example.demospringboot.entity.Minuman;
import com.example.demospringboot.repository.MinumanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MinumanService {

    @Autowired
    private MinumanRepository minumanRepository;

    public List<Minuman> getAllMinuman() {
        return minumanRepository.findAll();
    }

    public Minuman getMinumanById(Long id) {
        return minumanRepository.findById(id).orElse(null);
    }

    public void saveMinuman(Minuman minuman) {
        // Pastikan validasi ukuran berjalan sebelum save
        minuman.setUkuran(minuman.getUkuran()); 
        minumanRepository.save(minuman);
    }

    public void deleteMinuman(Long id) {
        minumanRepository.deleteById(id);
    }
}