package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class PesananDibatalkan : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pesanan_dibatalkan) // Pastikan nama file XML sesuai

        supportActionBar?.hide()

        // Tombol Back
        val btnBack: ImageView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            // Kembali ke halaman Home
            val intent = Intent(this, HomeCustomer::class.java)
            startActivity(intent)
            finish() // Tutup halaman ini
        }

        // Tombol "Kembali"
        val btnKembali: Button = findViewById(R.id.btn_kembali)
        btnKembali.setOnClickListener {
            // Kembali ke halaman Home
            val intent = Intent(this, HomeCustomer::class.java)
            startActivity(intent)
            finish() // Tutup halaman ini
        }
    }
}
