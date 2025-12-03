package com.example.demospringboot.controller;

import com.example.demospringboot.entity.Dessert;
import com.example.demospringboot.entity.Makanan;
import com.example.demospringboot.entity.Minuman;
import com.example.demospringboot.entity.Pegawai;
import com.example.demospringboot.entity.Pelanggan;
import com.example.demospringboot.entity.Transaksi;
import com.example.demospringboot.repository.PegawaiRepository;
import com.example.demospringboot.repository.PelangganRepository;
import com.example.demospringboot.service.DessertService;
import com.example.demospringboot.service.MakananService;
import com.example.demospringboot.service.MinumanService;
import com.example.demospringboot.service.TransaksiService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class LoginController {

    @Autowired private PegawaiRepository pegawaiRepository;
    @Autowired private PelangganRepository pelangganRepository;

    @Autowired private DessertService dessertService;
    @Autowired private MakananService makananService;
    @Autowired private MinumanService minumanService;
    @Autowired private TransaksiService transaksiService;


    @GetMapping("/")
    public String index() {
        return "index";
    }


    @GetMapping("/login-pegawai")
    public String loginPegawaiPage(HttpSession session) {
        if (session.getAttribute("user") != null && "pegawai".equals(session.getAttribute("role"))) {
            return "redirect:/home";
        }
        return "login"; 
    }

    @PostMapping("/login-pegawai")
    public String processLoginPegawai(@RequestParam String username, 
                                      @RequestParam String password, 
                                      HttpSession session, 
                                      Model model) {
        
        Pegawai pegawai = pegawaiRepository.findByUsername(username);

        if (pegawai != null && pegawai.getPassword().equals(password)) {
            session.setAttribute("user", pegawai);
            session.setAttribute("role", "pegawai");
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Username atau Password Pegawai salah!");
            return "login";
        }
    }

    @GetMapping("/home")
    public String home(HttpSession session) {
        if (session.getAttribute("user") == null || !"pegawai".equals(session.getAttribute("role"))) {
            return "redirect:/";
        }
        return "home";
    }


    @GetMapping("/login-pelanggan")
    public String loginPelangganPage(HttpSession session) {
        if (session.getAttribute("user") != null && "pelanggan".equals(session.getAttribute("role"))) {
            return "redirect:/dashboard-pelanggan";
        }
        return "login-pelanggan";
    }

    @PostMapping("/login-pelanggan")
    public String processLoginPelanggan(@RequestParam String username, 
                                        @RequestParam String password, 
                                        HttpSession session, 
                                        Model model) {
        
        Pelanggan pelanggan = pelangganRepository.findByUsername(username);

        if (pelanggan != null && pelanggan.getPassword().equals(password)) {
            session.setAttribute("user", pelanggan);
            session.setAttribute("role", "pelanggan");
            return "redirect:/dashboard-pelanggan"; 
        } else {
            model.addAttribute("error", "Username atau Password Pelanggan salah!");
            return "login-pelanggan";
        }
    }

   @GetMapping("/dashboard-pelanggan")
    public String dashboardPelanggan(HttpSession session, Model model) {
        if (session.getAttribute("user") == null || !"pelanggan".equals(session.getAttribute("role"))) {
            return "redirect:/";
        }

        Pelanggan user = (Pelanggan) session.getAttribute("user");
        model.addAttribute("pelanggan", user);

        model.addAttribute("listMakanan", makananService.getAllMakanan());
        model.addAttribute("listMinuman", minumanService.getAllMinuman());
        model.addAttribute("listDessert", dessertService.getAll());

        return "dashboard-pelanggan"; 
    }

    @PostMapping("/pesan-pelanggan")
    public String pesanPelanggan(@RequestParam String tipeMenu, 
                                 @RequestParam Long idMenu,     
                                 @RequestParam int jumlah,      
                                 HttpSession session) {
        
        // 1. Cek User Login
        Pelanggan user = (Pelanggan) session.getAttribute("user");
        if (user == null) return "redirect:/";
        
        // Refresh data user dari Database (PENTING biar saldo update)
        user = pelangganRepository.findById(user.getId()).orElse(null);

        String namaMenu = "";
        int hargaSatuan = 0;
        int stokTersedia = 0;

        // 2. LOGIKA MENCARI MENU & CEK STOK
        if (tipeMenu.equals("makanan")) {
            Makanan m = makananService.getMakananById(idMenu);
            if (m != null) {
                namaMenu = m.getNama();
                hargaSatuan = m.getHarga();
                stokTersedia = m.getStock();
                
                // Validasi Stok
                if (stokTersedia < jumlah) return "redirect:/dashboard-pelanggan?error=stok_habis";
                
                // Kurangi Stok
                m.setStock(stokTersedia - jumlah);
                makananService.saveMakanan(m);
            }
        } else if (tipeMenu.equals("minuman")) {
            Minuman m = minumanService.getMinumanById(idMenu);
            if (m != null) {
                namaMenu = m.getNama();
                hargaSatuan = m.getHarga();
                stokTersedia = m.getStock();
                if (stokTersedia < jumlah) return "redirect:/dashboard-pelanggan?error=stok_habis";
                
                m.setStock(stokTersedia - jumlah);
                minumanService.saveMinuman(m);
            }
        } else if (tipeMenu.equals("dessert")) {
            Dessert m = dessertService.getById(idMenu);
            if (m != null) {
                namaMenu = m.getNama();
                hargaSatuan = m.getHarga();
                stokTersedia = m.getStock();
                if (stokTersedia < jumlah) return "redirect:/dashboard-pelanggan?error=stok_habis";
                
                m.setStock(stokTersedia - jumlah);
                dessertService.save(m);
            }
        }

        // 2. HITUNG TOTAL & CEK SALDO (UPDATE BAGIAN INI)
        int totalBayar = hargaSatuan * jumlah;
        
        // Ambil saldo user, jika NULL ganti jadi 0.0 (Anti NullPointerException)
        Double saldoSekarang = (user.getSaldo() == null) ? 0.0 : user.getSaldo();
        
        if (saldoSekarang < totalBayar) {
            return "redirect:/dashboard-pelanggan?error=saldo_kurang";
        }

        // 3. POTONG SALDO
        user.setSaldo(saldoSekarang - totalBayar);
        pelangganRepository.save(user);
        // 4. SIMPAN TRANSAKSI
        Transaksi transaksi = new Transaksi();
        transaksi.setPelanggan(user);
        transaksi.setTanggal(LocalDate.now());
        
        // CATATAN DIBUAT OTOMATIS OLEH SISTEM (Bukan input user lagi)
        transaksi.setCatatan(jumlah + "x " + namaMenu); 
        
        transaksi.setTotalBayar(totalBayar);
        transaksi.setMetodePembayaran("Saldo App");
        
        transaksiService.saveTransaksi(transaksi);

        return "redirect:/dashboard-pelanggan?success=true";
    }

    @GetMapping("/register-pegawai")
    public String registerPegawaiPage(Model model) {
        model.addAttribute("pegawai", new Pegawai());
        return "register-pegawai";
    }

    @PostMapping("/register-pegawai/save")
    public String saveRegisterPegawai(@ModelAttribute Pegawai pegawai, Model model) {
        try {
            if (pegawai.getJabatan() == null || pegawai.getJabatan().isEmpty()) {
                pegawai.setJabatan("Staff");
            }
            
            pegawaiRepository.save(pegawai);
            return "redirect:/login-pegawai?registered=true"; 
        } catch (Exception e) {
            model.addAttribute("error", "Username sudah digunakan atau data tidak lengkap!");
            return "register-pegawai";
        }
    }

    @GetMapping("/register-pelanggan")
    public String registerPelangganPage(Model model) {
        model.addAttribute("pelanggan", new Pelanggan());
        return "register-pelanggan";
    }

    
    @PostMapping("/register-pelanggan/save")
    public String saveRegisterPelanggan(@ModelAttribute Pelanggan pelanggan, Model model) {
        try {
            pelangganRepository.save(pelanggan);
            return "redirect:/login-pelanggan?registered=true"; 
        } catch (Exception e) {
            model.addAttribute("error", "Username sudah digunakan atau data tidak lengkap!");
            return "register-pelanggan";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); 
        return "redirect:/"; 
    }
}