package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RiwayatService : AppCompatActivity() {

    private lateinit var tvAlamatRumah: TextView
    private lateinit var etBeratNonDaur: TextInputEditText
    private lateinit var etBeratDaur: TextInputEditText
    private lateinit var tvTotalHargaDaur: TextView
    private lateinit var tvTotalHargaJasa: TextView
    private lateinit var tvTotalKeseluruhan: TextView
    private lateinit var btnHapusRiwayat: Button

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.riwayat_service)

        // Initialize views
        tvAlamatRumah = findViewById(R.id.tv_alamat_rumah)
        etBeratNonDaur = findViewById(R.id.et_berat_non_daur)
        etBeratDaur = findViewById(R.id.et_berat_daur)
        tvTotalHargaDaur = findViewById(R.id.tv_total_harga_daur)
        tvTotalHargaJasa = findViewById(R.id.tv_total_harga_jasa)
        tvTotalKeseluruhan = findViewById(R.id.tv_total_keseluruhan)
        btnHapusRiwayat = findViewById(R.id.btnHapusRiwayat)

        // Initialize Firebase reference
        database = FirebaseDatabase.getInstance().getReference("Pesanan")

        val ivBack: ImageView = findViewById(R.id.iv_back)
        ivBack.setOnClickListener {
            val intent = Intent(this, ActivityService::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        // Load data from Firebase (assumes "Pesanan" contains the data)
        database.child("customer_id") // Replace with actual customer ID key
            .get().addOnSuccessListener { snapshot ->
                val alamatRumah = snapshot.child("alamatRumah").value.toString()
                val beratNonDaur = snapshot.child("beratNonDaur").value.toString()
                val beratDaur = snapshot.child("beratDaur").value.toString()
                val totalHargaDaur = snapshot.child("totalHargaDaur").value.toString()
                val totalHargaJasa = snapshot.child("totalHargaJasa").value.toString()
                val totalKeseluruhan = snapshot.child("totalKeseluruhan").value.toString()

                // Set data to views
                tvAlamatRumah.text = alamatRumah
                etBeratNonDaur.setText(beratNonDaur)
                etBeratDaur.setText(beratDaur)
                tvTotalHargaDaur.text = totalHargaDaur
                tvTotalHargaJasa.text = totalHargaJasa
                tvTotalKeseluruhan.text = totalKeseluruhan
            }

        // Set up delete button
        btnHapusRiwayat.setOnClickListener {
            // Delete data from Firebase
            database.child("customer_id") // Replace with actual customer ID key
                .removeValue().addOnSuccessListener {
                    // Navigate back to service activity
                    val intent = Intent(this, ActivityService::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                    // Handle failure (e.g., show a Toast message)
                }
        }
    }
}
