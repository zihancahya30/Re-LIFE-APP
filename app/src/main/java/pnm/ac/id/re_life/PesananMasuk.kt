package pnm.ac.id.re_life

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PesananMasuk : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var ordersRef: DatabaseReference
    private var orderId: String? = null  // Menyimpan ID pesanan yang sedang diproses

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
        val detailAlamat = intent.getStringExtra("detail_alamat") ?: "-"
        orderId = intent.getStringExtra("orderId") ?: ""// ID pesanan

        // Menghubungkan komponen UI
        val namaTextView: TextView = findViewById(R.id.tv_nama_pelanggan)
        val teleponTextView: TextView = findViewById(R.id.tv_no_telepon)
        val alamatTujuanTextView: TextView = findViewById(R.id.tv_alamat)
        val detailAlamatTextView: TextView = findViewById(R.id.tv_detail)
        val lokasiSaatIniEditText: EditText = findViewById(R.id.et_lokasi_saat_ini)
        val lihatPetaButton: Button = findViewById(R.id.btn_lihatmap)
        val ambilPesananButton: Button = findViewById(R.id.btnPesanan)
        val backButton: ImageView = findViewById(R.id.iv_back)

        // Menampilkan data ke komponen UI
        namaTextView.text = "Nama: $namaPelanggan"
        teleponTextView.text = "No. Telp: $noTelepon"
        alamatTujuanTextView.text = alamatTujuan
        detailAlamatTextView.text = detailAlamat

        // Tombol "Lihat Peta"
        lihatPetaButton.setOnClickListener {
            val lokasiSaatIni = lokasiSaatIniEditText.text.toString()
            if (lokasiSaatIni.isNotEmpty()) {
                val uri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=$lokasiSaatIni&destination=$alamatTujuan")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.google.android.apps.maps")
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this, "Aplikasi Google Maps tidak tersedia", Toast.LENGTH_SHORT).show()
                }
            } else {
                lokasiSaatIniEditText.error = "Masukkan lokasi Anda saat ini"
            }
        }

        // Tombol "Ambil Pesanan"
        ambilPesananButton.setOnClickListener {
            val lokasiSaatIni = lokasiSaatIniEditText.text.toString()
            if (lokasiSaatIni.isNotEmpty()) {
                // Perbarui status pesanan di database menjadi "Pesanan Diambil"
                if (orderId != null) {
                    ordersRef = database.child("orders").child(orderId!!)
                    ordersRef.child("status").setValue("Pesanan Diambil").addOnCompleteListener {
                        if (it.isSuccessful) {
                            // Beralih ke halaman ActivityPesanan
                            val intent = Intent(this, ActivityPesanan::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Gagal memperbarui status pesanan", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                lokasiSaatIniEditText.error = "Masukkan lokasi Anda saat ini"
            }
        }

        // Tombol kembali
        backButton.setOnClickListener {
            val intent = Intent(this, HomeService::class.java)
            startActivity(intent)
            finish()
        }
    }
}
