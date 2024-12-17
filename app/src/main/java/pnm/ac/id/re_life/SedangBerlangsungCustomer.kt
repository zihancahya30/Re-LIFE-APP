package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class SedangBerlangsungCustomer : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.re_life_berlangsung_customer)

        supportActionBar?.hide()

        // Komponen layout untuk menampilkan detail pesanan
        val tvAlamatLengkap: TextView = findViewById(R.id.tvAlamatLengkap)
        val tvDetailAlamat: TextView = findViewById(R.id.tvDetailAlamat)
        val tvNamaLengkap: TextView = findViewById(R.id.tvNamaLengkap)
        val tvNomorTelepon: TextView = findViewById(R.id.tvNomorTelepon)
        val tvStatus: TextView = findViewById(R.id.tv_status_pengambilan) // Menambahkan TextView untuk Status
        val tvNamaPetugas: TextView = findViewById(R.id.tvNamaPetugas) // TextView untuk menampilkan nama petugas

        // Mengambil data yang dikirim melalui Intent
        val pesananId = intent.getStringExtra("pesananId") // Pastikan ID pesanan dikirim melalui Intent

        if (pesananId != null) {
            // Ambil data pesanan dari Firebase berdasarkan pesananId
            database = FirebaseDatabase.getInstance().getReference("Orders").child(pesananId)

            // Mengambil data pesanan dari Firebase
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val alamatLengkap = snapshot.child("alamatLengkap").value.toString()
                        val detailAlamat = snapshot.child("detailAlamat").value.toString()
                        val namaLengkap = snapshot.child("namaLengkap").value.toString()
                        val nomorTelepon = snapshot.child("nomorTelepon").value.toString()
                        val status = snapshot.child("status").value.toString()
                        val serviceId = snapshot.child("serviceId").value.toString() // Ambil serviceId

                        // Menampilkan data yang diambil dari Firebase ke TextView
                        tvAlamatLengkap.text = alamatLengkap
                        tvDetailAlamat.text = detailAlamat
                        tvNamaLengkap.text = namaLengkap
                        tvNomorTelepon.text = nomorTelepon
                        tvStatus.text = status // Menampilkan status pesanan

                        // Jika statusnya "sedang diproses" maka ambil data petugas
                        if (status == "pesanan diambil" && serviceId.isNotEmpty()) {
                            tampilkanNamaPetugas(serviceId, tvNamaPetugas)
                        } else {
                            tvNamaPetugas.text = "Belum Ada Petugas Yang Menerima Pesanan Anda"
                        }
                    } else {
                        Toast.makeText(this@SedangBerlangsungCustomer, "Pesanan tidak ditemukan.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@SedangBerlangsungCustomer, "Gagal mengambil data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "ID pesanan tidak ditemukan.", Toast.LENGTH_SHORT).show()
        }

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
        val database = FirebaseDatabase.getInstance().getReference("Orders")
        // Menghapus pesanan berdasarkan pesananId
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

    // Fungsi untuk menampilkan nama petugas berdasarkan serviceId
    private fun tampilkanNamaPetugas(serviceId: String, tvNamaPetugas: TextView) {
        val serviceDatabase = FirebaseDatabase.getInstance().getReference("service").child(serviceId)

        // Ambil nama petugas dari database "service"
        serviceDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val namaPetugas = snapshot.child("nama").value.toString() // Ambil nama petugas
                    tvNamaPetugas.text = namaPetugas
                } else {
                    tvNamaPetugas.text = "Petugas tidak ditemukan"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                tvNamaPetugas.text = "Error mengambil nama petugas"
            }
        })
    }
}
