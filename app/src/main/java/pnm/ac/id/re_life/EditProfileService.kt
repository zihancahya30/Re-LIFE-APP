package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditProfileService : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etNomor: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etKonfirmasiPassword: EditText
    private lateinit var btnSimpan: Button

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile_service)

        // Inisialisasi FirebaseAuth dan DatabaseReference
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Inisialisasi EditText dan Button
        etNama = findViewById(R.id.et_nama_lengkap)
        etNomor = findViewById(R.id.et_nomor_telepon)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        etKonfirmasiPassword = findViewById(R.id.et_konfirmasi_password)
        btnSimpan = findViewById(R.id.btn_simpanprofile)

        // Ambil data pengguna dari Firebase
        getUserData()

        // Tampilkan/menghilangkan field password berdasarkan input email
        etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    etPassword.visibility = EditText.VISIBLE
                    etKonfirmasiPassword.visibility = EditText.VISIBLE
                } else {
                    etPassword.visibility = EditText.GONE
                    etKonfirmasiPassword.visibility = EditText.GONE
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Menangani tombol Simpan
        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val nomor = etNomor.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val konfirmasiPassword = etKonfirmasiPassword.text.toString().trim()

            // Validasi input
            if (nama.isEmpty() || nomor.isEmpty() || email.isEmpty() ||
                (etPassword.visibility == EditText.VISIBLE && (password.isEmpty() || konfirmasiPassword.isEmpty()))) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!email.endsWith("@gmail.com")) {
                Toast.makeText(this, "Email harus menggunakan domain @gmail.com", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password harus minimal 6 karakter", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != konfirmasiPassword) {
                Toast.makeText(this, "Password dan konfirmasi password tidak cocok", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Update data pengguna
            val user = firebaseAuth.currentUser
            user?.let { currentUser ->
                val credential = EmailAuthProvider.getCredential(currentUser.email!!, password)

                // Reauthenticate user sebelum update email/password
                currentUser.reauthenticate(credential).addOnSuccessListener {
                    // Update email
                    if (email != currentUser.email) {
                        currentUser.updateEmail(email).addOnFailureListener {
                            Toast.makeText(this, "Gagal memperbarui email: ${it.message}", Toast.LENGTH_SHORT).show()
                            return@addOnFailureListener
                        }
                    }

                    // Update password
                    if (password.isNotEmpty()) {
                        currentUser.updatePassword(password).addOnFailureListener {
                            Toast.makeText(this, "Gagal memperbarui password: ${it.message}", Toast.LENGTH_SHORT).show()
                            return@addOnFailureListener
                        }
                    }

                    // Update data lainnya di Realtime Database
                    updateUserData(currentUser.uid, nama, nomor, email)
                }.addOnFailureListener {
                    Toast.makeText(this, "Reauthenticate gagal: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Fungsi untuk mengupdate data pengguna di Realtime Database
    private fun updateUserData(userId: String, nama: String, nomor: String, email: String) {
        val userData = mapOf(
            "name" to nama,
            "nomorHp" to nomor,
            "email" to email
        )

        database.child("service").child(userId).updateChildren(userData)
            .addOnSuccessListener {
                Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                // Kembali ke halaman ProfileService
                val intent = Intent(this, ProfileService::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Gagal memperbarui data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Fungsi untuk mengambil data pengguna dari Firebase
    private fun getUserData() {
        val user = firebaseAuth.currentUser
        user?.let {
            val userId = it.uid
            database.child("service").child(userId).get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val name = snapshot.child("name").value.toString()
                        val nomorHp = snapshot.child("nomorHp").value.toString()
                        val email = snapshot.child("email").value.toString()
                        etNama.setText(name)
                        etNomor.setText(nomorHp)
                        etEmail.setText(email)
                    } else {
                        Toast.makeText(this, "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Gagal mengambil data: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
