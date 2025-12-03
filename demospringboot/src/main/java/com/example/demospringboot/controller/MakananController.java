package com.example.demospringboot.controller;

import com.example.demospringboot.entity.Makanan;
import com.example.demospringboot.service.MakananService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/makanan")
public class MakananController {

    @Autowired
    private MakananService makananService;

    @GetMapping
    public String listMakanan(Model model) {
        model.addAttribute("listMakanan", makananService.getAllMakanan());
        return "makanan-list";
    }

    @GetMapping("/new")
    public String formTambah(Model model) {
        model.addAttribute("makanan", new Makanan());
        return "makanan-form";
    }

    @PostMapping("/save")
    public String simpanMakanan(@ModelAttribute("makanan") Makanan makanan) {
        makananService.saveMakanan(makanan);
        return "redirect:/makanan";
    }

    @GetMapping("/edit/{id}")
    public String formEdit(@PathVariable Long id, Model model) {
        Makanan m = makananService.getMakananById(id);
        model.addAttribute("makanan", m);
        return "makanan-form";
    }

    @GetMapping("/delete/{id}")
    public String hapusMakanan(@PathVariable Long id) {
        makananService.deleteMakanan(id);
        return "redirect:/makanan";
    }
}
