package com.example.demospringboot.service;
import com.example.demospringboot.entity.Pelanggan;
import com.example.demospringboot.repository.PelangganRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PelangganService {
    @Autowired
    private PelangganRepository pelangganRepository;

    public List<Pelanggan> getAllPelanggan() { return pelangganRepository.findAll(); }
    public Pelanggan getPelangganById(Long id) { return pelangganRepository.findById(id).orElse(null); }
    public void savePelanggan(Pelanggan pelanggan) { pelangganRepository.save(pelanggan); }
    public void deletePelanggan(Long id) { pelangganRepository.deleteById(id); }
}