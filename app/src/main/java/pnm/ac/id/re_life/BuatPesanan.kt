package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class BuatPesanan : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.re_life_customer) // Pastikan nama layout sesuai

        // Inisialisasi Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val ordersReference = firebaseDatabase.getReference("Orders")

        // Ambil UID pengguna yang sedang login
        val userId = firebaseAuth.currentUser?.uid

        // Komponen layout
        val etAlamatLengkap = findViewById<EditText>(R.id.alamatLengkap)
        val etDetailAlamat = findViewById<EditText>(R.id.addressDetails)
        val etNamaLengkap = findViewById<EditText>(R.id.namaLengkap)
        val etNomorTelepon = findViewById<EditText>(R.id.nomorTelfon)
        val btnPesanSekarang = findViewById<Button>(R.id.messageButton)
        val btnBack = findViewById<ImageView>(R.id.back_tips)

        // Ambil data user dari Firebase dan isi ke kolom nama dan nomor telepon
        userId?.let { uid ->
            val usersReference = firebaseDatabase.getReference("Users")
            usersReference.child(uid).get().addOnSuccessListener { snapshot ->
                val namaLengkap = snapshot.child("namaLengkap").value?.toString() ?: ""
                val nomorTelepon = snapshot.child("nomorTelepon").value?.toString() ?: ""

                etNamaLengkap.setText(namaLengkap)
                etNomorTelepon.setText(nomorTelepon)
            }.addOnFailureListener {
                Toast.makeText(this, "Gagal mengambil data profil pengguna.", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol Pesan Sekarang
        btnPesanSekarang.setOnClickListener {
            val alamatLengkap = etAlamatLengkap.text.toString().trim()
            val detailAlamat = etDetailAlamat.text.toString().trim()
            val namaLengkap = etNamaLengkap.text.toString().trim()
            val nomorTelepon = etNomorTelepon.text.toString().trim()

            if (alamatLengkap.isEmpty() || detailAlamat.isEmpty() || namaLengkap.isEmpty() || nomorTelepon.isEmpty()) {
                Toast.makeText(this, "Harap lengkapi semua data.", Toast.LENGTH_SHORT).show()
            } else {
                val orderId = ordersReference.push().key // Generate unique ID untuk pesanan
                val orderData = mapOf(
                    "orderId" to orderId,
                    "userId" to userId,
                    "alamatLengkap" to alamatLengkap,
                    "detailAlamat" to detailAlamat,
                    "namaLengkap" to namaLengkap,
                    "nomorTelepon" to nomorTelepon,
                    "status" to "Sedang Diproses" // Status awal pesanan
                )

                // Simpan data ke Firebase
                orderId?.let {
                    ordersReference.child(it).setValue(orderData).addOnSuccessListener {
                        Toast.makeText(this, "Pesanan berhasil dibuat!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SedangBerlangsungCustomer::class.java) // Ganti dengan halaman "sedang berlangsung"
                        startActivity(intent)
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Gagal membuat pesanan.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Tombol Back
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeCustomer::class.java) // Ganti dengan halaman Home
            startActivity(intent)
            finish()
        }
    }
}