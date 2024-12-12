package pnm.ac.id.re_life

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ActivityPesanan : AppCompatActivity() {

    private lateinit var BeratNonDaur: EditText
    private lateinit var BeratDaur: EditText
    private lateinit var tvTotalHargaDaur: TextView
    private lateinit var tvHargaJasa: TextView
    private lateinit var tvTotalKeseluruhan: TextView
    private lateinit var btnPesanan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesanan)

        BeratNonDaur = findViewById(R.id.nilai_nondaur)
        BeratDaur = findViewById(R.id.nilai_daur)
        tvTotalHargaDaur = findViewById(R.id.tv_total_harga_daur)
        tvHargaJasa = findViewById(R.id.tv_harga_jasa)
        tvTotalKeseluruhan = findViewById(R.id.tv_total_keseluruhan)
        btnPesanan = findViewById(R.id.btnPesanan)

        btnPesanan.setOnClickListener {
            calculateTotal()
        }
    }

    private fun calculateTotal() {
        val beratNonDaur = etBeratNonDaur.text.toString().toDoubleOrNull() ?: 0.0
        val beratDaur = etBeratDaur.text.toString().toDoubleOrNull() ?: 0.0

        val hargaJasa = 5000.0
        val hargaDaurPerKg = 1000.0

        val totalHargaDaur = beratDaur * hargaDaurPerKg
        tvTotalHargaDaur.text = "Total Harga Sampah Daur Ulang: Rp ${totalHargaDaur.toInt()}"

        val totalKeseluruhan = when {
            beratDaur > 0 && beratNonDaur > 0 -> hargaJasa - totalHargaDaur // Both types of waste
            beratDaur > 0 -> hargaJasa - totalHargaDaur // Only recyclable waste
            else -> hargaJasa // Only non-recyclable waste or both are zero
        }

        tvHargaJasa.text = "Harga Jasa Pengumpulan Sampah: Rp ${hargaJasa.toInt()}"
        tvTotalKeseluruhan.text = "Total Keseluruhan: Rp ${totalKeseluruhan.toInt()}"
    }
}
