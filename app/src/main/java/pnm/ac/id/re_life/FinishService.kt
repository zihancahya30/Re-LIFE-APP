package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FinishService : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.finish_service)

        // Ambil data dari Intent
        val beratNonDaur = intent.getDoubleExtra("BERAT_NON_DAUR", 0.0)
        val beratDaur = intent.getDoubleExtra("BERAT_DAUR", 0.0)
        val hargaDaur = intent.getDoubleExtra("HARGA_DAUR", 0.0)

        // Cari TextView di layout
        val tvBeratNonDaur = findViewById<TextView>(R.id.berat_beratnon)
        val tvBeratDaur = findViewById<TextView>(R.id.berat_beratda)
        val tvHargaDaur = findViewById<TextView>(R.id.hargadaur)

        // Set data ke TextView
        tvBeratNonDaur.text = "Berat Sampah Non-Daur Ulang: $beratNonDaur kg"
        tvBeratDaur.text = "Berat Sampah Daur Ulang: $beratDaur kg"
        tvHargaDaur.text = "Harga Sampah Daur Ulang: Rp $hargaDaur"

        // Menangani klik pada tombol kembali (iv_back)
        val ivBack = findViewById<ImageView>(R.id.iv_back)
        ivBack.setOnClickListener {
            // Arahkan ke ActivityService
            val intent = Intent(this, ActivityService::class.java)
            startActivity(intent)
            finish() // Tutup FinishService
        }
    }
}
