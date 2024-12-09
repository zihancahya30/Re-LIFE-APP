package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_customer)

        // Menggunakan findViewById untuk mendapatkan tombol
        val btnKumpulkanSampah = findViewById<Button>(R.id.btnKumpulkanSampah)

        btnKumpulkanSampah.setOnClickListener {
            val intent = Intent(this, RegisterCustomer::class.java)
            startActivity(intent)
        }

        // Tombol Sampah Daur Ulang
        val frameSampahDaurUlang = findViewById<ImageView>(R.id.btnSampahDaurUlang)
        frameSampahDaurUlang.setOnClickListener {
            val intent = Intent(this, TipsDaurCustomer::class.java)
            startActivity(intent)
        }

        // Tombol Sampah Non-Daur Ulang
        val frameSampahNonDaurUlang = findViewById<ImageView>(R.id.btnSampahNonDaurUlang)
        frameSampahNonDaurUlang.setOnClickListener {
            val intent = Intent(this, TipsNonDaurCustomer::class.java)
            startActivity(intent)
        }

        // Tombol Profil
        val btnProfil = findViewById<ImageView>(R.id.btnProfil)
        btnProfil.setOnClickListener {
            // Membuka halaman profil pengguna
            val intent = Intent(this, ProfilCustomer::class.java)
            startActivity(intent)
        }
    }
}
