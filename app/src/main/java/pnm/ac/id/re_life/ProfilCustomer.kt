package pnm.ac.id.re_life

import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfilCustomer : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private var isEditing = false // Flag untuk mode Edit/Simpan
    private var isPasswordVisible = false // Flag untuk visibility password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profil_customer)

        // Inisialisasi Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("Users")

        // Ambil UID pengguna yang sedang login
        val userId = firebaseAuth.currentUser?.uid

        // Komponen Layout
        val etNamaLengkap = findViewById<EditText>(R.id.et_nama_lengkap)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etNomorTelepon = findViewById<EditText>(R.id.et_nomor_telepon)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val btnEditProfil = findViewById<Button>(R.id.btn_edit_profil)
        val btnLogout = findViewById<TextView>(R.id.btn_logout)
        val btnTogglePassword = findViewById<ImageView>(R.id.iv_toggle_password)

        // Awalnya EditText tidak bisa diedit
        etNamaLengkap.isEnabled = false
        etEmail.isEnabled = false
        etNomorTelepon.isEnabled = false
        etPassword.isEnabled = false

        // Password awal disembunyikan
        etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        // Mengambil data dari Firebase
        userId?.let { uid ->
            databaseReference.child(uid).get().addOnSuccessListener { snapshot ->
                val namaLengkap = snapshot.child("namaLengkap").value?.toString() ?: ""
                val email = snapshot.child("email").value?.toString() ?: ""
                val nomorTelepon = snapshot.child("nomorTelepon").value?.toString() ?: ""
                val password = snapshot.child("password").value?.toString() ?: ""

                etNamaLengkap.setText(namaLengkap)
                etEmail.setText(email)
                etNomorTelepon.setText(nomorTelepon)
                etPassword.setText(password)

            }.addOnFailureListener {
                Toast.makeText(this, "Gagal mengambil data pengguna.", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol untuk menampilkan/menyembunyikan password
        btnTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                etPassword.inputType = InputType.TYPE_CLASS_TEXT
                btnTogglePassword.setImageResource(R.drawable.visibility_24px) // Icon mata terbuka
            } else {
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                btnTogglePassword.setImageResource(R.drawable.visibility_off_24px) // Icon mata tertutup
            }
            etPassword.setSelection(etPassword.text.length) // Memastikan kursor tetap di
