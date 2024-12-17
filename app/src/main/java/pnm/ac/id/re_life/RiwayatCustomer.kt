package pnm.ac.id.re_life

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import pnm.ac.id.re_life.model.Pesanan

class RiwayatCustomer : AppCompatActivity() {

    private lateinit var db: DatabaseReference

    private lateinit var namaPetugasTV: TextView
    private lateinit var nomorTeleponTV: TextView
    private lateinit var detailAlamatTV: TextView
    private lateinit var statusTV: TextView
    private lateinit var beratSampahDaurUlangTV: TextView
    private lateinit var beratSampahNonDaurUlangTV: TextView
    private lateinit var btnHapusRiwayat: Button
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.riwayat_customer)

        // Inisialisasi Firebase Database
        db = FirebaseDatabase.getInstance().getReference("Orders")

        // Ambil id pesanan yang dikirim dari AktivitasCustomer
        val pesananId = intent.getStringExtra("PESANAN_ID")

        supportActionBar?.hide()

        // Inisialisasi tampilan
        namaPetugasTV = findViewById(R.id.tvNamaPetugas)
        nomorTeleponTV = findViewById(R.id.tvNomorTeleponPetugas)
        detailAlamatTV = findViewById(R.id.tvDetailAlamat)
        statusTV = findViewById(R.id.tv_status_pengambilan)
        beratSampahDaurUlangTV = findViewById(R.id.berat_sampah_daur_ulang)
        beratSampahNonDaurUlangTV = findViewById(R.id.berat_sampah_non_daur_ulang)
        btnHapusRiwayat = findViewById(R.id.btn_hapus_riwayat)
        btnBack = findViewById(R.id.btn_back) // Inisialisasi tombol back

        // Tombol Back
        btnBack.setOnClickListener {
            // Kembali ke halaman AktivitasCustomer
            onBackPressed() // atau finish() jika ingin menutup activity
        }

        // Ambil data pesanan dari Firebase
        if (pesananId != null) {
            db.child(pesananId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val pesanan = snapshot.getValue(Pesanan::class.java)
                    if (pesanan != null) {
                        // Menampilkan data pesanan jika ada
                        namaPetugasTV.text = pesanan.namaPetugas?.takeIf { it.isNotEmpty() } ?: "Belum ada keterangan"
                        nomorTeleponTV.text = pesanan.nomorPetugas?.takeIf { it.isNotEmpty() } ?: "Belum ada keterangan"
                        detailAlamatTV.text = pesanan.detailAlamat?.takeIf { it.isNotEmpty() } ?: "Belum ada keterangan"
                        statusTV.text = pesanan.status?.takeIf { it.isNotEmpty() } ?: "Belum ada keterangan"
                        beratSampahDaurUlangTV.text = pesanan.beratSampahDaurUlang?.takeIf { it.isNotEmpty() } ?: "Belum ada keterangan"
                        beratSampahNonDaurUlangTV.text = pesanan.beratSampahNonDaurUlang?.takeIf { it.isNotEmpty() } ?: "Belum ada keterangan"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@RiwayatCustomer, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Pesanan ID tidak ditemukan", Toast.LENGTH_SHORT).show()
        }

        // Tombol Hapus Riwayat
        btnHapusRiwayat.setOnClickListener {
            if (pesananId != null) {
                db.child(pesananId).removeValue()
                Toast.makeText(this, "Riwayat berhasil dihapus", Toast.LENGTH_SHORT).show()
                finish() // Menutup activity setelah menghapus riwayat
            }
        }
    }
}
