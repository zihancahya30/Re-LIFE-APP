package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class ActivityPesanan : AppCompatActivity() {

    private lateinit var etBeratNonDaur: EditText
    private lateinit var etBeratDaur: EditText
    private lateinit var tvTotalDaur: TextView
    private lateinit var tvTotalKeseluruhan: TextView
    private lateinit var btnPesanan: Button
    private lateinit var ivBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesanan)

        // Inisialisasi komponen UI
        etBeratNonDaur = findViewById(R.id.etBeratNonDaur)
        etBeratDaur = findViewById(R.id.etBeratDaur)
        tvTotalDaur = findViewById(R.id.tv_total_harga_daur)
        tvTotalKeseluruhan = findViewById(R.id.tv_total_keseluruhan)
        btnPesanan = findViewById(R.id.btnPesanan)

        ivBack.setOnClickListener {
            onBackPressed()
        }

        // Set listener untuk tombol pesanan
        btnPesanan.setOnClickListener {
            calculateTotal() // Menghitung total harga
            saveOrderToFirebase() // Menyimpan pesanan ke Firebase
            updateOrderStatus() // Mengubah status pesanan menjadi "Sudah diambil"
            navigateToFinishService() // Navigasi ke halaman selesai
        }
    }

    private fun calculateTotal() {
        // Ambil input dari user
        val beratNonDaur = etBeratNonDaur.text.toString().toDoubleOrNull() ?: 0.0
        val beratDaur = etBeratDaur.text.toString().toDoubleOrNull() ?: 0.0

        // Hitung total harga
        val hargaPerKgDaur = 3000.0
        val hargaJasa = 5000.0
        val totalHargaDaur = beratDaur * hargaPerKgDaur
        val totalKeseluruhan = totalHargaDaur + hargaJasa

        // Tampilkan hasil perhitungan pada UI
        tvTotalDaur.text = "Rp %.2f".format(totalHargaDaur)
        tvTotalKeseluruhan.text = "Rp %.2f".format(totalKeseluruhan)
    }

    private fun saveOrderToFirebase() {
        // Ambil nilai input dari user
        val beratNonDaur = etBeratNonDaur.text.toString().toDoubleOrNull() ?: 0.0
        val beratDaur = etBeratDaur.text.toString().toDoubleOrNull() ?: 0.0
        val totalHargaDaur = tvTotalDaur.text.toString()
        val totalKeseluruhan = tvTotalKeseluruhan.text.toString()

        val order = mapOf(
            "beratNonDaur" to beratNonDaur,
            "beratDaur" to beratDaur,
            "totalHargaDaur" to totalHargaDaur,
            "hargaJasa" to 5000.0,
            "totalKeseluruhan" to totalKeseluruhan,
            "status" to "Belum diambil", // Tambahkan status awal
            "timestamp" to System.currentTimeMillis() // Waktu pesanan dibuat
        )

        // Simpan ke Firebase Database
        val database = FirebaseDatabase.getInstance()
        val ordersRef = database.getReference("orders")

        ordersRef.push().setValue(order).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Pesanan berhasil disimpan!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Gagal menyimpan pesanan. Coba lagi!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateOrderStatus() {
        // Ambil referensi Firebase
        val database = FirebaseDatabase.getInstance()
        val ordersRef = database.getReference("orders")

        // Cari pesanan terakhir (contoh ini hanya memperbarui satu item tertentu, pastikan ada ID unik di implementasi nyata)
        ordersRef.limitToLast(1).get().addOnSuccessListener { snapshot ->
            if (snapshot.children.count() > 0) {
                val lastOrderKey = snapshot.children.first().key
                if (lastOrderKey != null) {
                    ordersRef.child(lastOrderKey).child("status").setValue("Sudah diambil")
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Status pesanan diperbarui menjadi 'Sudah diambil'", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Gagal memperbarui status. Coba lagi!", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            } else {
                Toast.makeText(this, "Tidak ada pesanan untuk diperbarui.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal mengambil data pesanan.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToFinishService() {
        // Navigasi ke halaman selesai
        val intent = Intent(this, FinishService::class.java)
        startActivity(intent)
    }
}