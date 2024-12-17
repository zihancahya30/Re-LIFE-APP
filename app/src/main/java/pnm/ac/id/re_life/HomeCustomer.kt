package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeCustomer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_customer)

        supportActionBar?.hide()

        // Tombol untuk "Kumpulkan Sampah"
        val btnKumpulkanSampah = findViewById<Button>(R.id.btnKumpulkanSampah)
        btnKumpulkanSampah.setOnClickListener {
            // Pastikan BuatPesanan adalah nama class yang benar
            val intent = Intent(this, BuatPesanan::class.java)
            startActivity(intent)
        }

        // Tombol untuk "Sampah Daur Ulang"
        val btnSampahDaurUlang = findViewById<ImageView>(R.id.btnSampahDaurUlang)
        btnSampahDaurUlang.setOnClickListener {
            // Pastikan TipsDaurCustomer adalah nama class yang benar
            val intent = Intent(this, TipsDaurCustomer::class.java)
            startActivity(intent)
        }

        // Tombol untuk "Sampah Non-Daur Ulang"
        val btnSampahNonDaurUlang = findViewById<ImageView>(R.id.btnSampahNonDaurUlang)
        btnSampahNonDaurUlang.setOnClickListener {
            // Pastikan TipsNonDaurCustomer adalah nama class yang benar
            val intent = Intent(this, TipsNonDaurCustomer::class.java)
            startActivity(intent)
        }

        // Menambahkan fungsi untuk BottomNavigationView
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_home)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Tetap di halaman Home
                    true
                }
//                R.id.nav_relife -> {
//                    // Pindah ke halaman RE-Life
//                    val intent = Intent(this, BuatPesanan::class.java)
//                    startActivity(intent)
//                    true
//                }
                R.id.nav_activity -> {
                    // Pindah ke halaman Aktivitas
                    val intent = Intent(this, AktivitasCustomer::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    // Pindah ke halaman Profil
                    val intent = Intent(this, ProfilCustomer::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Untuk memastikan item navbar yang aktif sesuai halaman saat ini
        bottomNav.selectedItemId = R.id.nav_home
    }
}
