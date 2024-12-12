package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog

class SedangBerlangsungCustomer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.re_life_berlangsung_customer)

        // Tombol Back
        val btnBack: ImageView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            finish() // Kembali ke halaman sebelumnya
        }

        // Tombol Batalkan Pesanan
        val btnBatalkan: Button = findViewById(R.id.btn_batalkan)
        btnBatalkan.setOnClickListener {
            // Tampilkan dialog konfirmasi
            showCancelConfirmationDialog()
        }

        // Menampilkan detail petugas (dummy)
        val petugasName: TextView = findViewById(R.id.petugas_name)
        petugasName.text = "Petugas: Andi" // Ganti dengan data dari server
    }

    private fun showCancelConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi Pembatalan")
        builder.setMessage("Apakah kamu yakin ingin membatalkan pesanan?")
        builder.setPositiveButton("Ya") { _, _ ->
            // Pindah ke halaman "Pesanan Dibatalkan"
            val intent = Intent(this, PesananDibatalkan::class.java)
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss() // Tutup dialog
        }
        builder.show()
    }
}
