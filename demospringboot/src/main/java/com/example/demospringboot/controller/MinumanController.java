package com.example.demospringboot.controller;

import com.example.demospringboot.entity.Minuman;
import com.example.demospringboot.service.MinumanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/minuman")
public class MinumanController {

    @Autowired
    private MinumanService minumanService;

    @GetMapping
    public String listMinuman(Model model) {
        model.addAttribute("listMinuman", minumanService.getAllMinuman());
        return "minuman-list";
    }

    @GetMapping("/new")
    public String formTambah(Model model) {
        model.addAttribute("minuman", new Minuman());
        return "minuman-form";
    }

    @PostMapping("/save")
    public String simpanMinuman(@ModelAttribute("minuman") Minuman minuman) {
        minumanService.saveMinuman(minuman);
        return "redirect:/minuman";
    }

    @GetMapping("/edit/{id}")
    public String formEdit(@PathVariable Long id, Model model) {
        Minuman m = minumanService.getMinumanById(id);
        model.addAttribute("minuman", m);
        return "minuman-form";
    }

    @GetMapping("/delete/{id}")
    public String hapusMinuman(@PathVariable Long id) {
        minumanService.deleteMinuman(id);
        return "redirect:/minuman";
    }
}
