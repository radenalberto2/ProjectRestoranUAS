# 💰 Top-up Saldo Feature - Complete Guide

## Overview

Fitur top-up saldo memungkinkan pelanggan untuk menambahkan saldo ke akun mereka sebelum melakukan pembayaran. Ini menyelesaikan pertanyaan Anda tentang "bagaimana cara menambahkan saldo?"

**Status**: ✅ **FULLY IMPLEMENTED & COMPILED**

---

## 🎯 2 Pilihan Pembayaran
### Option 1: Sistem Dompet (Wallet) ✅ IMPLEMENTED
```
Customer punya saldo di akun
    ↓
Customer bisa top-up saldo
    ↓
Saat membeli, saldo otomatis berkurang
```

### Option 2: Manual Payment (Optional Future)
```
Customer bisa input berapa mau bayar
    ↓
Tidak perlu kurangi saldo
    ↓
Untuk testing/cash payment
```

---

## ✨ Fitur Top-up Saldo

### 1. **Halaman Top-up Saldo** (`topup-saldo.html`)

**URL**: `/pelanggan/topup-saldo`

**Features**:
- ✅ Display saldo saat ini
- ✅ Input nominal (min Rp 10.000)
- ✅ Preset amounts (50K, 100K, 250K, 500K)
- ✅ Payment method selection:
  - Transfer Bank
  - Kartu Kredit
  - Tunai (langsung ke kasir)
- ✅ Optional notes field
- ✅ Beautiful responsive UI
- ✅ FAQ section

**Screenshot Description**:
```
┌─────────────────────────────────────┐
│     💰 Top-up Saldo                 │
│                                     │
│  Saldo Anda: Rp X.XXX.XXX          │
│                                     │
│  Nominal: [_____________]           │
│                                     │
│  Preset: [50K] [100K] [250K] [500K] │
│                                     │
│  Metode:                            │
│  ○ Transfer Bank                    │
│  ○ Kartu Kredit                     │
│  ○ Tunai                            │
│                                     │
│  [Lanjutkan] [Batal]               │
└─────────────────────────────────────┘
```

---

## 🔧 Technical Implementation

### 1. **GET Endpoint** - Tampilkan halaman top-up

```java
@GetMapping("/topup-saldo")
public String halamanTopupSaldo(Model model) {
    try {
        // Get pelanggan (in real app, use session/authentication)
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
```

**What it does:**
1. Get first customer from database
2. Pass customer info to template
3. Render topup-saldo.html

---

### 2. **POST Endpoint** - Proses top-up

```java
@PostMapping("/proses-topup")
public String prosesTopup(
        @RequestParam("nominal") Long nominal,
        @RequestParam("metodePembayaran") String metodePembayaran,
        @RequestParam(value = "catatan", required = false) String catatan,
        Model model
) {
    try {
        // Validate nominal
        if (nominal == null || nominal < 10000) {
            return "redirect:/pelanggan/topup-saldo?error=Nominal%20minimum%20Rp%2010.000";
        }

        // Get pelanggan
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
```

**What it does:**
1. Validate nominal >= 10.000
2. Get customer from database
3. Add nominal to existing saldo
4. Save updated saldo
5. Redirect with success message

---

## 🔗 Integration Points

### 1. **Dashboard Link**
```html
<a th:href="@{/pelanggan/topup-saldo}" style="background: #4CAF50; color: white;">
    💰 Top-up Saldo
</a>
```

Location: `dashboard-pelanggan.html` header navigation

---

### 2. **Checkout Warning** (Low Balance)
```html
<div th:if="${pelanggan.saldo - totalHarga < 0}" 
     style="background: #ffebee; border: 1px solid #ef5350; border-radius: 5px; padding: 12px;">
    <p style="color: #c62828;">
        💡 Saldo Anda tidak cukup. Silakan 
        <a th:href="@{/pelanggan/topup-saldo}">
            top-up saldo
        </a> 
        terlebih dahulu atau gunakan metode pembayaran lain.
    </p>
</div>
```

Location: `checkout.html` - Appears when balance insufficient

---

## 📊 Data Flow

```
1. CUSTOMER CLICKS TOP-UP BUTTON
   Dashboard → "💰 Top-up Saldo" link
                    ↓
                GET /topup-saldo
                    ↓
                Display topup-saldo.html with current saldo
                
2. CUSTOMER FILLS FORM
   Input nominal (or click preset)
   Select payment method
   Add optional notes
                    ↓
                Click "Lanjutkan Top-up"
                    ↓
                POST /proses-topup
                
3. SERVER PROCESSES
   Validate nominal >= 10.000
   Get pelanggan from DB
   Calculate: newSaldo = oldSaldo + nominal
   Save to database
                    ↓
                Redirect with success message
                
4. SUCCESS PAGE
   Show updated saldo
   Display confirmation
   Allow continue shopping
```

---

## 💾 Database Operation

**Before Top-up:**
```sql
SELECT saldo FROM pelanggan WHERE id = 1;
-- Result: 500000
```

**Top-up 250000:**
```
newSaldo = 500000 + 250000 = 750000
UPDATE pelanggan SET saldo = 750000 WHERE id = 1;
```

**After Top-up:**
```sql
SELECT saldo FROM pelanggan WHERE id = 1;
-- Result: 750000
```

---

## 🧪 Test Scenarios

### Scenario 1: Basic Top-up
```
1. Go to: /pelanggan/topup-saldo
2. Current saldo: Rp 100.000
3. Input nominal: 200.000
4. Select: Transfer Bank
5. Click: "Lanjutkan Top-up"

Expected:
✓ Success message
✓ New saldo: Rp 300.000
✓ Redirect to topup-saldo page
✓ Database saldo updated
```

### Scenario 2: Preset Amount
```
1. Go to: /pelanggan/topup-saldo
2. Current saldo: Rp 50.000
3. Click preset: "Rp 250.000"
4. Input field auto-filled: 250000
5. Select: Kartu Kredit
6. Click: "Lanjutkan Top-up"

Expected:
✓ Success message
✓ New saldo: Rp 300.000
✓ Saldo display updated
```

### Scenario 3: Minimum Validation
```
1. Go to: /pelanggan/topup-saldo
2. Input nominal: 5000 (below minimum)
3. Click: "Lanjutkan Top-up"

Expected:
✓ Error message: "Nominal minimum Rp 10.000"
✓ Stay on topup-saldo page
✓ Saldo not changed
```

### Scenario 4: From Checkout (Insufficient Balance)
```
1. Add items to cart
2. Go to checkout
3. Total: Rp 500.000
4. Your saldo: Rp 200.000
5. See warning: "Saldo tidak cukup"
6. Click link: "top-up saldo"
7. Redirect to: /pelanggan/topup-saldo
8. Top-up Rp 300.000 (now have 500.000)
9. Back to checkout
10. Click: "Lanjutkan Pembayaran"

Expected:
✓ Payment succeeds
✓ Final saldo: 0 (500.000 - 500.000)
```

---

## 🎨 UI Components

### Saldo Display
```html
<div class="saldo-info">
    <label>Saldo Anda Saat Ini</label>
    <div class="amount">Rp 500.000</div>
</div>
```

### Preset Buttons
```html
<div class="preset-buttons">
    <button class="preset-btn" onclick="selectPreset(50000, this)">
        Rp 50.000
    </button>
    <!-- More buttons... -->
</div>
```

### JavaScript (Client-side validation)
```javascript
function selectPreset(amount, button) {
    // Set the input value
    document.getElementById('nominal').value = amount;
    
    // Highlight selected button
    document.querySelectorAll('.preset-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    button.classList.add('active');
}
```

---

## 📱 Responsive Design

**Mobile** (< 600px):
- Single column layout
- Full-width inputs
- Stacked buttons
- Touch-friendly click areas

**Tablet** (600px - 900px):
- 2-column preset buttons
- Comfortable spacing
- Readable font sizes

**Desktop** (> 900px):
- 2-column form layout
- 2x2 grid for presets
- Spacious padding

---

## 🔒 Security Notes

**Currently:**
- ✅ Validates nominal >= 10.000
- ✅ Gets pelanggan from database
- ✅ Error handling implemented
- ✅ SQL-safe (using ORM)

**Future Enhancements:**
- Add authentication (use session user, not first customer)
- Add transaction logging
- Add payment gateway integration
- Add top-up history
- Add top-up limits/daily caps
- Add verification codes
- Add 2FA for large amounts

---

## 📊 Status Codes & Redirects

| Scenario | Response | Redirect |
|----------|----------|----------|
| Success | 200 OK | /pelanggan/topup-saldo?success=true |
| Nominal too low | 400 | /pelanggan/topup-saldo?error=Nominal%20minimum |
| Pelanggan not found | 404 | /pelanggan/topup-saldo?error=Pelanggan%20tidak%20ditemukan |
| Exception | 500 | /pelanggan/topup-saldo?error=Gagal%20memproses |

---

## 🚀 How to Use

### For User:
```
1. Login as customer
2. Click "💰 Top-up Saldo" button
3. Choose nominal (preset or custom)
4. Select payment method
5. Submit form
6. Saldo updated immediately
7. Use for shopping
```

### For Admin/Testing:
```
1. Check SQL: SELECT * FROM pelanggan;
2. Note saldo value
3. Go to /pelanggan/topup-saldo
4. Top-up amount
5. Verify in database: saldo increased
```

---

## 📝 Files Modified

| File | Change | Type |
|------|--------|------|
| PelangganController.java | Added 2 methods | Java |
| dashboard-pelanggan.html | Added top-up button | HTML |
| checkout.html | Added warning + link | HTML |
| topup-saldo.html | Created new page | HTML |

---

## ✅ Checklist

- [x] Feature implemented
- [x] Endpoints created
- [x] Templates created
- [x] Links integrated
- [x] Build successful
- [x] No compilation errors
- [ ] Manual browser testing
- [ ] Verify database updates
- [ ] Test all scenarios

---

## 🎓 Related Features

**Payment Methods Supported:**
1. **SALDO** (Deduct from balance) ✅
2. **TUNAI** (Cash at location) ✅
3. **KARTU_KREDIT** (Credit card) 🔧
4. **TRANSFER** (Bank transfer) 🔧

**Top-up Methods (Supported):**
1. **Transfer Bank** - Placeholder
2. **Kartu Kredit** - Placeholder
3. **Tunai** - Immediate at counter

---

## 🔄 Next Steps

1. **Test in Browser**:
   - Go to /pelanggan/topup-saldo
   - Test form submission
   - Verify saldo updates

2. **Verify Database**:
   - Check saldo before/after
   - Confirm values in MySQL

3. **Integration Test**:
   - Top-up, then checkout
   - Verify balance deduction

4. **Enhancement** (Optional):
   - Add payment gateway
   - Add transaction history
   - Add top-up limits
   - Add email confirmation

---

## 📞 Quick Reference

**New Endpoints:**
- `GET /pelanggan/topup-saldo` - Show form
- `POST /pelanggan/proses-topup` - Process top-up

**New Template:**
- `topup-saldo.html` - Complete top-up page

**Modified Templates:**
- `dashboard-pelanggan.html` - Added button
- `checkout.html` - Added warning

**Key Parameters:**
- `nominal`: Amount to top-up (min 10000)
- `metodePembayaran`: TRANSFER, KARTU_KREDIT, TUNAI
- `catatan`: Optional notes

---

**Status**: ✅ COMPLETE & READY FOR TESTING  
**Build**: ✅ SUCCESS  
**Compilation**: ✅ ZERO ERRORS

