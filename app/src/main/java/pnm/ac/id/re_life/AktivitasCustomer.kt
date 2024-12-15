package pnm.ac.id.re_life

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class AktivitasCustomer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.aktivitas_customer)

        supportActionBar?.hide()

        // Menambahkan fungsi untuk BottomNavigationView
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_aktivitas)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Pindah ke halaman HomeCustomer dan menutup AktivitasCustomer
                    val intent = Intent(this, HomeCustomer::class.java)
                    startActivity(intent)
                    finish() // Menutup aktivitas AktivitasCustomer
                    true
                }
                R.id.nav_relife -> {
                    // Tidak perlu aksi jika sudah di halaman RE-Life
                    true
                }
                R.id.nav_activity -> {
                    // Pindah ke halaman AktivitasCustomer, tidak perlu menambah aktivitas
                    true
                }
                R.id.nav_profile -> {
                    // Pindah ke halaman ProfilCustomer dan menutup AktivitasCustomer
                    val intent = Intent(this, ProfilCustomer::class.java)
                    startActivity(intent)
                    finish() // Menutup aktivitas AktivitasCustomer
                    true
                }
                else -> false
            }
        }

        bottomNav.selectedItemId = R.id.nav_activity // Menetapkan item yang aktif
    }
}
