package com.example.demospringboot.controller;

import com.example.demospringboot.entity.Transaksi;
import com.example.demospringboot.service.PelangganService;
import com.example.demospringboot.service.TransaksiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/transaksi")
public class TransaksiController {

    @Autowired
    private TransaksiService transaksiService;

    @Autowired
    private PelangganService pelangganService;

    @GetMapping
    public String listTransaksi(@RequestParam(value = "status", required = false) String status, Model model) {
        if (status != null && !status.isEmpty()) {
            model.addAttribute("listTransaksi", transaksiService.getTransaksiByStatus(status));
        } else {
            model.addAttribute("listTransaksi", transaksiService.getAllTransaksi());
        }
        return "transaksi-list";
    }

    @GetMapping("/new")
    public String formTransaksi(Model model) {
        model.addAttribute("transaksi", new Transaksi());
        model.addAttribute("listPelanggan", pelangganService.getAllPelanggan());
        return "transaksi-form";
    }

    @PostMapping("/save")
    public String saveTransaksi(@ModelAttribute Transaksi transaksi) {
        // Set status default untuk transaksi manual
        if (transaksi.getStatus() == null || transaksi.getStatus().isEmpty()) {
            transaksi.setStatus("SUCCESS");
        }
        
        transaksiService.saveTransaksi(transaksi);
        return "redirect:/transaksi";
    }

    @GetMapping("/delete/{id}")
    public String deleteTransaksi(@PathVariable Long id) {
        transaksiService.deleteTransaksi(id);
        return "redirect:/transaksi";
    }

    /**
     * Menampilkan halaman transaction history dengan filter status dan customer
     */
    @GetMapping("/history")
    public String transaksiHistory(@RequestParam(value = "status", required = false) String status, Model model) {
        if (status != null && !status.isEmpty()) {
            model.addAttribute("transaksis", transaksiService.getTransaksiByStatus(status));
        } else {
            model.addAttribute("transaksis", transaksiService.getAllTransaksi());
        }
        return "transaksi-history";
    }

    /**
     * Menampilkan kitchen display system - hanya transaksi yang berhasil (SUCCESS)
     * yang perlu disiapkan di dapur
     */
    @GetMapping("/kitchen-display")
    public String kitchenDisplay(Model model) {
        // Ambil hanya transaksi dengan status SUCCESS yang belum selesai
        var successfulOrders = transaksiService.getTransaksiByStatus("SUCCESS");
        model.addAttribute("successfulOrders", successfulOrders);
        model.addAttribute("pendingOrders", successfulOrders);
        return "kitchen-display";
    }

    /**
     * Update status transaksi via API (untuk kitchen display)
     */
    @PatchMapping("/api/transaksi/{id}/status")
    @ResponseBody
    public String updateTransaksiStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest request) {
        try {
            Transaksi transaksi = transaksiService.getTransaksiById(id);
            if (transaksi != null) {
                transaksi.setStatus(request.getStatus());
                transaksiService.saveTransaksi(transaksi);
                return "{\"success\": true}";
            }
            return "{\"success\": false, \"message\": \"Transaksi tidak ditemukan\"}";
        } catch (Exception e) {
            return "{\"success\": false, \"message\": \"" + e.getMessage() + "\"}";
        }
    }

    // Inner class untuk request body
    public static class StatusUpdateRequest {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}