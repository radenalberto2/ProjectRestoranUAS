package com.example.demospringboot.controller;

import com.example.demospringboot.entity.*;
import com.example.demospringboot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pelanggan")
public class PelangganController {

    @Autowired
    private PelangganService pelangganService;
    
    @Autowired
    private MakananService makananService;
    
    @Autowired
    private MinumanService minumanService;
    
    @Autowired
    private DessertService dessertService;
    
    @Autowired
    private KeranjangBelanja keranjangBelanja;
    
    @Autowired
    private TransaksiService transaksiService;

    // Dashboard pelanggan
    @GetMapping("/dashboard")
    public String dashboardPelanggan(Model model) {
        // Ambil pelanggan pertama sebagai contoh (nanti diganti dengan login session)
        List<Pelanggan> allPelanggan = pelangganService.getAllPelanggan();
        if (!allPelanggan.isEmpty()) {
            Pelanggan pelanggan = allPelanggan.get(0);
            model.addAttribute("pelanggan", pelanggan);
            // Set pelanggan ID ke session keranjang
            keranjangBelanja.setPelangganId(pelanggan.getId());
        }
        
        // Data menu untuk ditampilkan
        model.addAttribute("listMakanan", makananService.getAllMakanan());
        model.addAttribute("listMinuman", minumanService.getAllMinuman());
        model.addAttribute("listDessert", dessertService.getAll());
        
        // Info keranjang untuk badge
        model.addAttribute("keranjangItemCount", keranjangBelanja.getTotalItemCount());
        
        return "dashboard-pelanggan";
    }
    
    // Tambah item ke keranjang
    @PostMapping("/tambah-keranjang")
    public String tambahKeKeranjang(
            @RequestParam("tipeMenu") String tipeMenu,
            @RequestParam("idMenu") Long idMenu,
            @RequestParam("namaMenu") String namaMenu,
            @RequestParam("harga") int harga,
            @RequestParam("stok") int stok,
            @RequestParam("jumlah") int jumlah,
            Model model) {
        
        try {
            // Validasi stok
            if (stok <= 0) {
                return "redirect:/pelanggan/dashboard?error=Stok%20habis";
            }
            
            if (jumlah > stok) {
                jumlah = stok; // Batasi jumlah dengan stok tersedia
            }
            
            // Buat item keranjang
            KeranjangItem item = new KeranjangItem(
                tipeMenu.toUpperCase(), 
                idMenu, 
                namaMenu, 
                harga, 
                jumlah, 
                stok
            );
            
            // Tambah ke keranjang
            keranjangBelanja.tambahItem(item);
            
            return "redirect:/pelanggan/dashboard?success=Item%20berhasil%20ditambahkan%20ke%20keranjang";
            
        } catch (Exception e) {
            return "redirect:/pelanggan/dashboard?error=Terjadi%20kesalahan%20sistem";
        }
    }
    
    // Halaman keranjang
    @GetMapping("/keranjang")
    public String lihatKeranjang(Model model) {
        // Ambil data pelanggan
        List<Pelanggan> allPelanggan = pelangganService.getAllPelanggan();
        if (!allPelanggan.isEmpty()) {
            model.addAttribute("pelanggan", allPelanggan.get(0));
        }
        
        // Data keranjang
        model.addAttribute("keranjangItems", keranjangBelanja.getItems());
        model.addAttribute("totalHarga", keranjangBelanja.getTotalHarga());
        model.addAttribute("totalItemCount", keranjangBelanja.getTotalItemCount());
        
        return "keranjang";
    }
    
    // Update jumlah item di keranjang
    @PostMapping("/update-keranjang")
    public String updateKeranjang(
            @RequestParam("tipeMenu") String tipeMenu,
            @RequestParam("idMenu") Long idMenu,
            @RequestParam("jumlah") int jumlah) {
        
        try {
            keranjangBelanja.updateJumlah(tipeMenu, idMenu, jumlah);
            return "redirect:/pelanggan/keranjang?success=Jumlah%20berhasil%20diupdate";
        } catch (Exception e) {
            return "redirect:/pelanggan/keranjang?error=Gagal%20mengupdate%20jumlah";
        }
    }
    
    // Hapus item dari keranjang
    @PostMapping("/hapus-keranjang")
    public String hapusDariKeranjang(
            @RequestParam("tipeMenu") String tipeMenu,
            @RequestParam("idMenu") Long idMenu) {
        
        try {
            keranjangBelanja.hapusItem(tipeMenu, idMenu);
            return "redirect:/pelanggan/keranjang?success=Item%20berhasil%20dihapus";
        } catch (Exception e) {
            return "redirect:/pelanggan/keranjang?error=Gagal%20menghapus%20item";
        }
    }
    
    // Kosongkan keranjang
    @PostMapping("/kosongkan-keranjang")
    public String kosongkanKeranjang() {
        keranjangBelanja.kosongkan();
        return "redirect:/pelanggan/keranjang?success=Keranjang%20berhasil%20dikosongkan";
    }
    
    // Halaman checkout
    @GetMapping("/checkout")
    public String halamanCheckout(Model model) {
        // Validasi keranjang tidak kosong
        if (keranjangBelanja.isEmpty()) {
            return "redirect:/pelanggan/keranjang?error=Keranjang%20belanja%20kosong";
        }
        
        // Ambil data pelanggan
        List<Pelanggan> allPelanggan = pelangganService.getAllPelanggan();
        if (allPelanggan.isEmpty()) {
            return "redirect:/pelanggan/keranjang?error=Pelanggan%20tidak%20ditemukan";
        }
        
        Pelanggan pelanggan = allPelanggan.get(0);
        
        // Validasi saldo cukup
        int totalHarga = keranjangBelanja.getTotalHarga();
        if (pelanggan.getSaldo() < totalHarga) {
            return "redirect:/pelanggan/keranjang?error=Saldo%20tidak%20cukup";
        }
        
        // Validasi stok cukup untuk semua item
        for (KeranjangItem item : keranjangBelanja.getItems()) {
            // Cek stok untuk setiap item
            int stokTersedia = getStokMenu(item.getTipeMenu(), item.getIdMenu());
            if (stokTersedia < item.getJumlah()) {
                return "redirect:/pelanggan/keranjang?error=Stok%20" + item.getNamaMenu() + "%20tidak%20cukup";
            }
        }
        
        // Kirim data ke view
        model.addAttribute("pelanggan", pelanggan);
        model.addAttribute("keranjangItems", keranjangBelanja.getItems());
        model.addAttribute("totalHarga", totalHarga);
        model.addAttribute("totalItemCount", keranjangBelanja.getTotalItemCount());
        
        return "checkout";
    }
    
    // Proses pembayaran
    @PostMapping("/proses-pembayaran")
    public String prosesPembayaran(
            @RequestParam("metodePembayaran") String metodePembayaran,
            @RequestParam(value = "catatan", required = false) String catatan) {
        
        try {
            // Ambil data pelanggan
            List<Pelanggan> allPelanggan = pelangganService.getAllPelanggan();
            if (allPelanggan.isEmpty()) {
                return "redirect:/pelanggan/checkout?error=Pelanggan%20tidak%20ditemukan";
            }
            
            Pelanggan pelanggan = allPelanggan.get(0);
            
            // Validasi keranjang tidak kosong
            if (keranjangBelanja.isEmpty()) {
                return "redirect:/pelanggan/keranjang?error=Keranjang%20kosong";
            }
            
            int totalHarga = keranjangBelanja.getTotalHarga();
            
            // Validasi metode pembayaran SALDO
            if ("SALDO".equals(metodePembayaran) && pelanggan.getSaldo() < totalHarga) {
                return "redirect:/pelanggan/checkout?error=Saldo%20tidak%20cukup";
            }
            
            // Buat transaksi dari keranjang
            Transaksi transaksi = transaksiService.createTransaksiFromKeranjang(
                pelanggan,
                keranjangBelanja.getItems(),
                metodePembayaran,
                catatan
            );
            
            // Update stok untuk setiap item
            for (KeranjangItem item : keranjangBelanja.getItems()) {
                updateStokMenu(item.getTipeMenu(), item.getIdMenu(), -item.getJumlah());
            }
            
            // Update saldo pelanggan jika metode pembayaran SALDO
            if ("SALDO".equals(metodePembayaran)) {
                pelanggan.setSaldo(pelanggan.getSaldo() - totalHarga);
                pelangganService.savePelanggan(pelanggan);
            }
            
            // Kosongkan keranjang
            keranjangBelanja.kosongkan();
            
            // Redirect ke halaman konfirmasi
            return "redirect:/pelanggan/konfirmasi-transaksi/" + transaksi.getId();
            
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/pelanggan/checkout?error=Terjadi%20kesalahan%20saat%20memproses%20pembayaran";
        }
    }
    
    // Helper method untuk update stok menu
    private void updateStokMenu(String tipeMenu, Long idMenu, int quantityChange) {
        try {
            switch (tipeMenu.toUpperCase()) {
                case "MAKANAN":
                    Makanan makanan = makananService.getMakananById(idMenu);
                    if (makanan != null) {
                        makanan.setStock(makanan.getStock() + quantityChange);
                        makananService.saveMakanan(makanan);
                    }
                    break;
                case "MINUMAN":
                    Minuman minuman = minumanService.getMinumanById(idMenu);
                    if (minuman != null) {
                        minuman.setStock(minuman.getStock() + quantityChange);
                        minumanService.saveMinuman(minuman);
                    }
                    break;
                case "DESSERT":
                    Dessert dessert = dessertService.getById(idMenu);
                    if (dessert != null) {
                        dessert.setStock(dessert.getStock() + quantityChange);
                        dessertService.save(dessert);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Halaman konfirmasi transaksi
    @GetMapping("/konfirmasi-transaksi/{id}")
    public String konfirmasiTransaksi(@PathVariable("id") Long transaksiId, Model model) {
        try {
            Transaksi transaksi = transaksiService.getTransaksiById(transaksiId);
            
            if (transaksi == null) {
                return "redirect:/pelanggan/dashboard?error=Transaksi%20tidak%20ditemukan";
            }
            
            model.addAttribute("transaksi", transaksi);
            model.addAttribute("pelanggan", transaksi.getPelanggan());
            
            return "transaksi-sukses";
            
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/pelanggan/dashboard?error=Terjadi%20kesalahan%20saat%20menampilkan%20konfirmasi";
        }
    }

    // Helper method untuk mendapatkan stok menu
    private int getStokMenu(String tipeMenu, Long idMenu) {
        try {
            switch (tipeMenu.toUpperCase()) {
                case "MAKANAN":
                    Makanan makanan = makananService.getMakananById(idMenu);
                    return makanan != null ? makanan.getStock() : 0;
                case "MINUMAN":
                    Minuman minuman = minumanService.getMinumanById(idMenu);
                    return minuman != null ? minuman.getStock() : 0;
                case "DESSERT":
                    Dessert dessert = dessertService.getById(idMenu);
                    return dessert != null ? dessert.getStock() : 0;
                default:
                    return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    // ============ TOP-UP SALDO FEATURES ============

    // Halaman top-up saldo
    @GetMapping("/topup-saldo")
    public String halamanTopupSaldo(Model model) {
        try {
            // Get first customer (in real app, use session/authentication)
            List<Pelanggan> allPelanggan = pelangganService.getAllPelanggan();
            if (!allPelanggan.isEmpty()) {
                Pelanggan pelanggan = allPelanggan.get(0);
                model.addAttribute("pelanggan", pelanggan);
            }
            
            return "topup-saldo";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/pelanggan/dashboard?error=Gagal%20membuka%20halaman%20top-up";
        }
    }

    // Proses top-up saldo
    @PostMapping("/proses-topup")
    public String prosesTopup(
            @RequestParam("nominal") Long nominal,
            @RequestParam("metodePembayaran") String metodePembayaran,
            @RequestParam(value = "catatan", required = false) String catatan,
            Model model
    ) {
        try {
            // Validasi nominal
            if (nominal == null || nominal < 10000) {
                return "redirect:/pelanggan/topup-saldo?error=Nominal%20minimum%20Rp%2010.000";
            }

            // Get pelanggan (in real app, use session/authentication)
            List<Pelanggan> allPelanggan = pelangganService.getAllPelanggan();
            if (allPelanggan.isEmpty()) {
                return "redirect:/pelanggan/topup-saldo?error=Pelanggan%20tidak%20ditemukan";
            }

            Pelanggan pelanggan = allPelanggan.get(0);

            // Add nominal to saldo
            Double newSaldo = pelanggan.getSaldo() + nominal;
            pelanggan.setSaldo(newSaldo);

            // Save to database
            pelangganService.savePelanggan(pelanggan);

            // Success response
            model.addAttribute("pelanggan", pelanggan);
            model.addAttribute("success", true);

            return "redirect:/pelanggan/topup-saldo?success=true&nominal=" + nominal;

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/pelanggan/topup-saldo?error=Gagal%20memproses%20top-up";
        }
    }
}