package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class SedangBerlangsungCustomer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.re_life_berlangsung_customer)

        supportActionBar?.hide()

        // Komponen layout untuk menampilkan detail pesanan
        val tvAlamatLengkap: TextView = findViewById(R.id.tvAlamatLengkap)
        val tvDetailAlamat: TextView = findViewById(R.id.tvDetailAlamat)
        val tvNamaLengkap: TextView = findViewById(R.id.tvNamaLengkap)
        val tvNomorTelepon: TextView = findViewById(R.id.tvNomorTelepon)

        // Mengambil data yang dikirim melalui Intent
        val alamatLengkap = intent.getStringExtra("alamatLengkap")
        val detailAlamat = intent.getStringExtra("detailAlamat")
        val namaLengkap = intent.getStringExtra("namaLengkap")
        val nomorTelepon = intent.getStringExtra("nomorTelepon")
        val pesananId = intent.getStringExtra("pesananId") // Pastikan ID pesanan dikirim melalui Intent

        // Tampilkan data di TextView
        tvAlamatLengkap.text = alamatLengkap ?: "Alamat tidak ditemukan"
        tvDetailAlamat.text = detailAlamat ?: "Detail alamat tidak ditemukan"
        tvNamaLengkap.text = namaLengkap ?: "Nama tidak ditemukan"
        tvNomorTelepon.text = nomorTelepon ?: "Nomor telepon tidak ditemukan"

        // Tombol Back
        val btnBack: ImageView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            finish() // Kembali ke halaman sebelumnya
        }

        // Tombol Batalkan Pesanan
        val btnBatalkan: Button = findViewById(R.id.btn_batalkan)
        btnBatalkan.setOnClickListener {
            // Tampilkan dialog konfirmasi untuk pembatalan pesanan
            showCancelConfirmationDialog(pesananId)
        }
    }

    private fun showCancelConfirmationDialog(pesananId: String?) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi Pembatalan")
        builder.setMessage("Apakah kamu yakin ingin membatalkan pesanan?")
        builder.setPositiveButton("Ya") { _, _ ->
            if (pesananId != null) {
                hapusPesananDariDatabase(pesananId)
            } else {
                Toast.makeText(this, "ID pesanan tidak ditemukan.", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss() // Tutup dialog
        }
        builder.show()
    }

    private fun hapusPesananDariDatabase(pesananId: String) {
        val database = FirebaseDatabase.getInstance().getReference("pesanan")
        database.child(pesananId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Pesanan berhasil dibatalkan.", Toast.LENGTH_SHORT).show()

                // Pindah ke halaman "Pesanan Dibatalkan"
                val intent = Intent(this, PesananDibatalkan::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Gagal membatalkan pesanan: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
