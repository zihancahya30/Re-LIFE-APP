// Updated HomeService.kt
package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeService : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_service)

        // Menghapus ActionBar
        supportActionBar?.hide()

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Mengambil nama user dari Firebase Auth
        val haloTextView: TextView = findViewById(R.id.halo_text)
        val user = FirebaseAuth.getInstance().currentUser

        user?.uid?.let { uid ->
            database.child("users").child(uid).child("name").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.getValue(String::class.java)
                    haloTextView.text = "Halo, $name"
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }

        // Menambahkan fungsi untuk BottomNavigationView
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_home_service)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Tetap di halaman Home
                    true
                }
                R.id.nav_activity -> {
                    // Pindah ke halaman Aktivitas
                    val intent = Intent(this, ActivityService::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    // Pindah ke halaman Profil
                    val intent = Intent(this, ProfileService::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Untuk memastikan item navbar yang aktif sesuai halaman saat ini
        bottomNav.selectedItemId = R.id.nav_home

        // Handle "Tata Cara" ImageView Click
        findViewById<ImageView>(R.id.ic_tatacara).setOnClickListener {
            startActivity(Intent(this, PengelolaanService::class.java))
        }

        // Load data pesanan dari Firebase
        loadPesananData()
    }

    private fun loadPesananData() {
        val namaCustomerTextView: TextView = findViewById(R.id.nama_customer)
        val alamatPesananTextView: TextView = findViewById(R.id.alamat_pesanan_text)
        val teleponPesananTextView: TextView = findViewById(R.id.telepon_customer)

        database.child("pesanan").limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (pesananSnapshot in snapshot.children) {
                    val namaCustomer = pesananSnapshot.child("nama").getValue(String::class.java) ?: "-"
                    val alamatPesanan = pesananSnapshot.child("alamat").getValue(String::class.java) ?: "-"
                    val teleponPesanan = pesananSnapshot.child("telepon").getValue(String::class.java) ?: "-"

                    namaCustomerTextView.text = namaCustomer
                    alamatPesananTextView.text = alamatPesanan
                    teleponPesananTextView.text = teleponPesanan

                    // Navigate to PesananMasuk.kt on arrow click
                    findViewById<ImageView>(R.id.iv_back).setOnClickListener {
                        val intent = Intent(this@HomeService, PesananMasuk::class.java)
                        intent.putExtra("nama", namaCustomer)
                        intent.putExtra("alamat", alamatPesanan)
                        intent.putExtra("telepon", teleponPesanan)
                        startActivity(intent)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
