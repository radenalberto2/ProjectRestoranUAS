package com.example.demospringboot.service;

import com.example.demospringboot.entity.Makanan;
import com.example.demospringboot.repository.MakananRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MakananService {

    @Autowired
    private MakananRepository makananRepository;

    // Ambil semua data
    public List<Makanan> getAllMakanan() {
        return makananRepository.findAll();
    }

    // Ambil satu data by ID
    public Makanan getMakananById(Long id) {
        return makananRepository.findById(id).orElse(null);
    }

    // Simpan atau Update data
    public void saveMakanan(Makanan makanan) {
        makananRepository.save(makanan);
    }

    // Hapus data
    public void deleteMakanan(Long id) {
        makananRepository.deleteById(id);
    }
}
