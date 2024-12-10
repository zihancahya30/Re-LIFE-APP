package pnm.ac.id.re_life

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class TipsDaurCustomer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.costumer_tips_daur_ulang)

        // Menghubungkan tombol back dengan aksi kembali ke halaman sebelumnya
        val backButton = findViewById<View>(R.id.back_tips)
        backButton.setOnClickListener {
            // Kembali ke halaman sebelumnya
            onBackPressed()
        }
    }
}