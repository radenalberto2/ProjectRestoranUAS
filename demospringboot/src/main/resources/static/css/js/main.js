document.addEventListener("DOMContentLoaded", function() {
    console.log("Aplikasi Restoran Siap.");

    // Efek 1: Konfirmasi Delete yang lebih cantik (Optional)
    const deleteButtons = document.querySelectorAll('.btn-del');
    deleteButtons.forEach(btn => {
        btn.addEventListener('click', function(e) {
            if(!confirm("Apakah Anda yakin ingin menghapus data ini? Data tidak bisa dikembalikan.")) {
                e.preventDefault();
            }
        });
    });

    // Efek 2: Highlight Menu Aktif di Navbar
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.nav-links a');
    
    navLinks.forEach(link => {
        if (link.getAttribute('href') === currentPath) {
            link.classList.add('active');
        }
    });
});