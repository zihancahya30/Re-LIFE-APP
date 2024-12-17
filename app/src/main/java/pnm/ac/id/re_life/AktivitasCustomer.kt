package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import pnm.ac.id.re_life.adapter.SedangBerlangsungAdapter
import pnm.ac.id.re_life.model.Pesanan

class AktivitasCustomer : AppCompatActivity() {

    private lateinit var sedangBerlangsungAdapter: SedangBerlangsungAdapter
    private lateinit var riwayatAdapter: SedangBerlangsungAdapter

    private lateinit var recyclerSedangBerlangsung: RecyclerView
    private lateinit var recyclerRiwayat: RecyclerView
    private lateinit var noSedangBerlangsung: TextView
    private lateinit var noRiwayat: TextView

    private lateinit var database: DatabaseReference
    private val pesananListSedang = mutableListOf<Pesanan>()
    private val pesananListRiwayat = mutableListOf<Pesanan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mengatur agar tidak ada Action Bar
        supportActionBar?.hide()
        setContentView(R.layout.aktivitas_customer)

        // Inisialisasi RecyclerView dan TextView
        recyclerSedangBerlangsung = findViewById(R.id.recycler_sedang_berlangsung)
        recyclerRiwayat = findViewById(R.id.recycler_riwayat)
        noSedangBerlangsung = findViewById(R.id.text_no_sedang_berlangsung)
        noRiwayat = findViewById(R.id.text_no_riwayat)

        // Mengatur layout manager untuk RecyclerView
        recyclerSedangBerlangsung.layoutManager = LinearLayoutManager(this)
        recyclerRiwayat.layoutManager = LinearLayoutManager(this)

        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance().getReference("Orders")

        // Fungsi untuk mengambil data pesanan
        fetchPesanan()

        // Menambahkan fungsi untuk BottomNavigationView
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_aktivitas)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Pindah ke halaman HomeCustomer dan menutup AktivitasCustomer
                    val intent = Intent(this, HomeCustomer::class.java)
                    startActivity(intent)
                    finish() // Menutup aktivitas AktivitasCustomer
                    true
                }
                R.id.nav_relife -> {
                    // Pindah ke halaman RE-Life
                    val intent = Intent(this, BuatPesanan::class.java)
                    startActivity(intent)
                    finish() // Menutup aktivitas AktivitasCustomer
                    true
                }
                R.id.nav_activity -> {
                    // Tetap di halaman AktivitasCustomer
                    true
                }
                R.id.nav_profile -> {
                    // Pindah ke halaman ProfilCustomer dan menutup AktivitasCustomer
                    val intent = Intent(this, ProfilCustomer::class.java)
                    startActivity(intent)
                    finish() // Menutup aktivitas AktivitasCustomer
                    true
                }
                else -> false
            }
        }

        // Menetapkan item yang aktif di BottomNavigationView
        bottomNav.selectedItemId = R.id.nav_activity
    }

    private fun fetchPesanan() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pesananListSedang.clear()
                pesananListRiwayat.clear()

                for (data in snapshot.children) {
                    val pesanan = data.getValue(Pesanan::class.java)
                    if (pesanan != null) {
                        if (pesanan.status == "Belum Diambil") {
                            pesananListSedang.add(pesanan)
                        } else if (pesanan.status == "Sudah Diambil") {
                            pesananListRiwayat.add(pesanan)
                        }
                    }
                }

                // Adapter untuk Sedang Berlangsung
                sedangBerlangsungAdapter = SedangBerlangsungAdapter(pesananListSedang) { pesanan ->
                    val intent = Intent(this@AktivitasCustomer, SedangBerlangsungCustomer::class.java)
                    intent.putExtra("PESANAN_ID", pesanan.pesananId)
                    startActivity(intent)
                }

                // Adapter untuk Riwayat
                riwayatAdapter = SedangBerlangsungAdapter(pesananListRiwayat) { pesanan ->
                    val intent = Intent(this@AktivitasCustomer, RiwayatCustomer::class.java)
                    intent.putExtra("PESANAN_ID", pesanan.pesananId) // Menambahkan ID pesanan
                    startActivity(intent)
                }

                recyclerSedangBerlangsung.adapter = sedangBerlangsungAdapter
                recyclerRiwayat.adapter = riwayatAdapter

                // Menangani tampilan jika tidak ada data
                noSedangBerlangsung.visibility = if (pesananListSedang.isEmpty()) View.VISIBLE else View.GONE
                noRiwayat.visibility = if (pesananListRiwayat.isEmpty()) View.VISIBLE else View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AktivitasCustomer, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
