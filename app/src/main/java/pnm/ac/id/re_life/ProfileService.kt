package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileService : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etEmail: EditText
    private lateinit var etNomor: EditText
    private lateinit var btnEdit: Button
    private lateinit var tvLogout: TextView

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_service)

        // Menghapus ActionBar
        supportActionBar?.hide()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    true
                }
                R.id.nav_activity -> {
                    startActivity(Intent(this, ActivityService::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileService::class.java))
                    true
                }
                else -> false
            }
        }

        // Inisialisasi FirebaseAuth dan DatabaseReference
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Inisialisasi EditText, Button dan TextView
        etNama = findViewById(R.id.et_nama)
        etNomor = findViewById(R.id.et_notelp)
        etEmail = findViewById(R.id.et_email)
        btnEdit = findViewById(R.id.btnEdit)
        tvLogout = findViewById(R.id.tvLogout)

        // Ambil data pengguna dari Firebase
        getUserData()

        // Nonaktifkan EditText jika tidak ingin pengguna mengubah data
        etNama.isEnabled = false
        etNomor.isEnabled = false
        etEmail.isEnabled = false

        // Menangani tombol Edit
        btnEdit.setOnClickListener {
            val intent = Intent(this, EditProfileService::class.java)
            startActivity(intent)
        }

        // Menangani tombol Logout
        tvLogout.setOnClickListener {
            val user = firebaseAuth.currentUser
            user?.let { firebaseUser ->
                // Menghapus data pengguna dari Firebase Realtime Database
                val userId = firebaseUser.uid
                database.child("service").child(userId).removeValue().addOnSuccessListener {
                    // Setelah data berhasil dihapus, logout dari Firebase Auth
                    firebaseUser.delete().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Logout dan pindah ke halaman Register
                            firebaseAuth.signOut()
                            val intent = Intent(this, Register::class.java) // Arahkan ke halaman Register
                            startActivity(intent)
                            finish() // Tutup activity ProfileService setelah logout
                        } else {
                            Toast.makeText(this, "Gagal menghapus akun: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(this, "Gagal menghapus data: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getUserData() {
        val user = firebaseAuth.currentUser
        user?.let {
            val userId = it.uid
            // Ambil data pengguna dari Firebase Realtime Database
            database.child("service").child(userId).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val name = snapshot.child("name").value.toString()
                    val nomorHp = snapshot.child("nomorHp").value.toString()
                    val email = snapshot.child("email").value.toString()

                    // Tampilkan data ke EditText
                    etNama.setText(name)
                    etNomor.setText(nomorHp)
                    etEmail.setText(email)
                } else {
                    Toast.makeText(this, "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "Gagal mengambil data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
