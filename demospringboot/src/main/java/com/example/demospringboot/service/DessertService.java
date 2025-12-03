package com.example.demospringboot.service;

import com.example.demospringboot.entity.Dessert;
import com.example.demospringboot.repository.DessertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DessertService {
    @Autowired private DessertRepository repo;
    public List<Dessert> getAll() { return repo.findAll(); }
    public void save(Dessert d) { repo.save(d); }
    public Dessert getById(Long id) { return repo.findById(id).orElse(null); }
    public void delete(Long id) { repo.deleteById(id); }
}
