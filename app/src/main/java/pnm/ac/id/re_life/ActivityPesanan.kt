package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ActivityPesanan : AppCompatActivity() {

    private lateinit var etBeratNonDaur: EditText
    private lateinit var etBeratDaur: EditText
    private lateinit var tvTotalHargaDaur: TextView
    private lateinit var tvHargaJasa: TextView
    private lateinit var tvTotalKeseluruhan: TextView
    private lateinit var btnPesanan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesanan)

        // Inisialisasi komponen UI
        etBeratNonDaur = findViewById(R.id.etBeratNonDaur)
        etBeratDaur = findViewById(R.id.etBeratDaur)
        tvTotalHargaDaur = findViewById(R.id.tv_total_harga_daur)
        tvHargaJasa = findViewById(R.id.tv_harga_jasa)
        tvTotalKeseluruhan = findViewById(R.id.tv_total_keseluruhan)
        btnPesanan = findViewById(R.id.btnPesanan)

        // Tambahkan event listener pada tombol
        btnPesanan.setOnClickListener {
            calculateTotal()
            navigateToFinishService()
        }
    }

    private fun calculateTotal() {
        // Ambil nilai input
        val beratNonDaur = etBeratNonDaur.text.toString().toDoubleOrNull() ?: 0.0
        val beratDaur = etBeratDaur.text.toString().toDoubleOrNull() ?: 0.0

        // Konstanta harga
        val hargaJasa = 5000.0
        val hargaDaurPerKg = 1000.0

        // Hitung total harga sampah daur ulang
        val totalHargaDaur = beratDaur * hargaDaurPerKg
        tvTotalHargaDaur.text = "Total Harga Sampah Daur Ulang: Rp ${totalHargaDaur.toInt()}"

        // Hitung total keseluruhan
        val totalKeseluruhan = if (beratDaur > 0) {
            hargaJasa - totalHargaDaur
        } else {
            hargaJasa
        }

        // Perbarui teks pada TextView
        tvHargaJasa.text = "Harga Jasa Pengumpulan Sampah: Rp ${hargaJasa.toInt()}"
        tvTotalKeseluruhan.text = "Total Keseluruhan: Rp ${totalKeseluruhan.toInt()}"
    }

    private fun navigateToFinishService() {
        // Navigasi ke halaman finish_service.kt
        val intent = Intent(this, FinishService::class.java)
        intent.putExtra("BERAT_NON_DAUR", berat_beratnon)
        intent.putExtra("BERAT_DAUR", berat_beratda)
        intent.putExtra("HARGA_DAUR", hargadaur)
        startActivity(intent)
    }
}
