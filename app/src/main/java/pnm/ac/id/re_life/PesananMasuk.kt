package pnm.ac.id.re_life

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PesananMasuk : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pesanan_masuk)

        // Menghapus ActionBar
        supportActionBar?.hide()

        // Inisialisasi Firebase Realtime Database
        database = FirebaseDatabase.getInstance().reference

        // Mendapatkan data dari Intent
        val namaPelanggan = intent.getStringExtra("nama") ?: "-"
        val noTelepon = intent.getStringExtra("telepon") ?: "-"
        val alamatTujuan = intent.getStringExtra("alamat") ?: "-"

        // Menghubungkan komponen UI
        val namaTextView: TextView = findViewById(R.id.tv_nama_pelanggan)
        val teleponTextView: TextView = findViewById(R.id.tv_no_telepon)
        val alamatTujuanTextView: TextView = findViewById(R.id.tv_alamat_tujuan)
        val lokasiSaatIniEditText: EditText = findViewById(R.id.et_lokasi_saat_ini)
        val lihatPetaButton: Button = findViewById(R.id.btn_lihatmap)
        val ambilPesananButton: Button = findViewById(R.id.btnPesanan)

        // Menampilkan data ke komponen UI
        namaTextView.text = "Nama: $namaPelanggan"
        teleponTextView.text = "No. Telp: $noTelepon"
        alamatTujuanTextView.text = alamatTujuan

        // Tombol "Lihat Peta"
        lihatPetaButton.setOnClickListener {
            val lokasiSaatIni = lokasiSaatIniEditText.text.toString()
            if (lokasiSaatIni.isNotEmpty()) {
                val uri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=$lokasiSaatIni&destination=$alamatTujuan")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.google.android.apps.maps")
                startActivity(intent)
            } else {
                lokasiSaatIniEditText.error = "Masukkan lokasi Anda saat ini"
            }
        }

        // Tombol "Ambil Pesanan"
        ambilPesananButton.setOnClickListener {
            val lokasiSaatIni = lokasiSaatIniEditText.text.toString()
            if (lokasiSaatIni.isNotEmpty()) {
                // Simpan data ke tabel "pesanan_berlangsung"
                val pesananBerlangsungRef = database.child("pesanan_berlangsung").push()
                pesananBerlangsungRef.setValue(
                    mapOf(
                        "nama" to namaPelanggan,
                        "telepon" to noTelepon,
                        "alamat" to alamatTujuan,
                        "lokasi_saat_ini" to lokasiSaatIni
                    )
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        // Beralih ke halaman SedangBerlangsung
                        val intent = Intent(this, SedangBerlangsung::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Tangani error jika ada
                        lokasiSaatIniEditText.error = "Gagal menyimpan data. Coba lagi."
                    }
                }
            } else {
                lokasiSaatIniEditText.error = "Masukkan lokasi Anda saat ini"
            }
        }

        // Tombol kembali
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }
    }
}
