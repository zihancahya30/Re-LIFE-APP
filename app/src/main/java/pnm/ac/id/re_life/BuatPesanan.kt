package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class BuatPesanan : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.re_life_customer)

        supportActionBar?.hide()

        // Inisialisasi Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val ordersReference = firebaseDatabase.getReference("Orders")

        // Ambil UID pengguna yang sedang login
        val userId = firebaseAuth.currentUser?.uid

        // Komponen layout
        val etAlamatLengkap = findViewById<EditText>(R.id.et_alamat_lengkap)
        val etDetailAlamat = findViewById<EditText>(R.id.et_detail_alamat)
        val etNamaLengkap = findViewById<EditText>(R.id.namaLengkap)
        val etNomorTelepon = findViewById<EditText>(R.id.nomorTelfon)
        val btnPesanSekarang = findViewById<Button>(R.id.btn_pesan_sekarang)
        val btnBack = findViewById<ImageView>(R.id.back_tips)

        // Tombol Pesan Sekarang
        btnPesanSekarang.setOnClickListener {
            val alamatLengkap = etAlamatLengkap.text.toString().trim()
            val detailAlamat = etDetailAlamat.text.toString().trim()
            val namaLengkap = etNamaLengkap.text.toString().trim()
            val nomorTelepon = etNomorTelepon.text.toString().trim()

            if (alamatLengkap.isEmpty() || detailAlamat.isEmpty() || namaLengkap.isEmpty() || nomorTelepon.isEmpty()) {
                Toast.makeText(this, "Harap lengkapi semua data.", Toast.LENGTH_SHORT).show()
            } else {
                val pesananId = ordersReference.push().key // Generate unique ID untuk pesanan
                val orderData = mapOf(
                    "pesananId" to pesananId,
                    "userId" to userId,
                    "alamatLengkap" to alamatLengkap,
                    "detailAlamat" to detailAlamat,
                    "namaLengkap" to namaLengkap,
                    "nomorTelepon" to nomorTelepon,
                    "status" to "Sedang Diproses" // Status awal pesanan
                )

                // Simpan data ke Firebase
                pesananId?.let {
                    ordersReference.child(it).setValue(orderData).addOnSuccessListener {
                        Toast.makeText(this, "Pesanan berhasil dibuat!", Toast.LENGTH_SHORT).show()

                        // Mengirim data ke halaman Sedang Berlangsung menggunakan Intent
                        val intent = Intent(this, SedangBerlangsungCustomer::class.java).apply {
                            putExtra("alamatLengkap", alamatLengkap)
                            putExtra("detailAlamat", detailAlamat)
                            putExtra("namaLengkap", namaLengkap)
                            putExtra("nomorTelepon", nomorTelepon)
                            putExtra("pesananId", pesananId)
                        }
                        startActivity(intent)
                        finish() // Menutup halaman Buat Pesanan setelah berhasil
                    }.addOnFailureListener {
                        Toast.makeText(this, "Gagal membuat pesanan.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Menambahkan fungsi untuk BottomNavigationView
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_buat_pesanan)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, HomeCustomer::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_relife -> {
                    true // Tidak perlu aksi jika sudah di halaman RE-Life
                }
                R.id.nav_activity -> {
                    // Pindah ke halaman Aktivitas
                    val intent = Intent(this, AktivitasCustomer::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    // Pindah ke halaman Profil
                    val intent = Intent(this, ProfilCustomer::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        bottomNav.selectedItemId = R.id.nav_relife

        // Tombol Back
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeCustomer::class.java) // Ganti dengan halaman Home
            startActivity(intent)
            finish()
        }
    }
}
