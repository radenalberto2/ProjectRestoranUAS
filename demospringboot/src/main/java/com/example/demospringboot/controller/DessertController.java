package com.example.demospringboot.controller;

import com.example.demospringboot.entity.Dessert;
import com.example.demospringboot.service.DessertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/dessert")
public class DessertController {

    @Autowired private DessertService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("listDessert", service.getAll());
        return "dessert-list";
    }

    @GetMapping("/new")
    public String add(Model model) {
        model.addAttribute("dessert", new Dessert());
        return "dessert-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Dessert dessert) {
        // Logika sederhana: jika ID ada -> Update, jika tidak -> Insert
        // Service.save() biasanya sudah menghandle insert/update di JPA
        service.save(dessert);
        return "redirect:/dessert";
    }
    
    // --- TAMBAHAN UNTUK EDIT ---
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Dessert d = service.getById(id);
        model.addAttribute("dessert", d);
        return "dessert-form";
    }
    // ---------------------------

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/dessert";
    }
}