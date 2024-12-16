package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ActivityService : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var layoutContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        // Inisialisasi Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("pesanan")

        // Inisialisasi LinearLayout dari XML
        layoutContainer = findViewById(R.id.layout_container)

        // Load data pesanan dari Firebase
        loadPesanan()
        loadPesananData()
    }

    private fun loadPesanan() {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid

            database.child(userId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Hapus semua view sebelumnya untuk menghindari duplikasi
                    layoutContainer.removeAllViews()

                    for (childSnapshot in snapshot.children) {
                        val pesananId = childSnapshot.key ?: continue
                        val berat = childSnapshot.child("berat").getValue(String::class.java) ?: "0"

                        // Buat LinearLayout baru secara dinamis
                        val pesananLayout = LinearLayout(this@ActivityService).apply {
                            orientation = LinearLayout.VERTICAL
                            setPadding(16, 16, 16, 16)
                            setBackgroundResource(R.drawable.rounded_corner_background)
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(0, 16, 0, 16)
                            }
                        }

                        // Tambahkan TextView untuk informasi pesanan
                        val beratTextView = TextView(this@ActivityService).apply {
                            text = "Berat Sampah: $berat kg"
                            textSize = 16f
                            setTextColor(resources.getColor(R.color.black, theme))
                        }

                        // Tambahkan Button untuk navigasi ke RiwayatService
                        val detailButton = Button(this@ActivityService).apply {
                            text = "Lihat Detail"
                            setOnClickListener {
                                val intent = Intent(this@ActivityService, RiwayatService::class.java)
                                intent.putExtra("pesananId", pesananId)
                                startActivity(intent)
                            }
                        }

                        // Tambahkan TextView dan Button ke dalam pesananLayout
                        pesananLayout.addView(beratTextView)
                        pesananLayout.addView(detailButton)

                        // Tambahkan pesananLayout ke layoutContainer
                        layoutContainer.addView(pesananLayout)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Tangani error Firebase
                    error.toException().printStackTrace()
                }
            })
        }
    }

    private fun loadPesananData() {
        val namaCustomerTextView: TextView = findViewById(R.id.nama_customer)
        val alamatPesananTextView: TextView = findViewById(R.id.alamat_pesanan_text)
        val teleponPesananTextView: TextView = findViewById(R.id.telepon_customer)

        database.child("pesanan").limitToLast(1).addListenerForSingleValueEvent(object :
            ValueEventListener {
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
                        val intent = Intent(this@ActivityService, PesananMasuk::class.java)
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
