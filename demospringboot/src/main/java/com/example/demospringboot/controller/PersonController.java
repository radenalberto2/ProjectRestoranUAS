package com.example.demospringboot.controller;

import com.example.demospringboot.entity.Pelanggan;
import com.example.demospringboot.service.PelangganService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pelanggan")
public class PersonController {

    @Autowired
    private PelangganService pelangganService;

    @GetMapping
    public String listPelanggan(Model model) {
        model.addAttribute("listPelanggan", pelangganService.getAllPelanggan());
        return "pelanggan-list";
    }

    @GetMapping("/new")
    public String formPelanggan(Model model) {
        model.addAttribute("pelanggan", new Pelanggan());
        return "pelanggan-form";
    }

    @PostMapping("/save")
    public String savePelanggan(@ModelAttribute Pelanggan pelanggan) {
        pelangganService.savePelanggan(pelanggan);
        return "redirect:/pelanggan";
    }

    @GetMapping("/edit/{id}")
    public String editPelanggan(@PathVariable Long id, Model model) {
        model.addAttribute("pelanggan", pelangganService.getPelangganById(id));
        return "pelanggan-form";
    }

    @GetMapping("/delete/{id}")
    public String deletePelanggan(@PathVariable Long id) {
        pelangganService.deletePelanggan(id);
        return "redirect:/pelanggan";
    }
}
