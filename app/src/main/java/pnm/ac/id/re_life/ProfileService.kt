package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileService : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var etNamaLengkap: EditText
    private lateinit var etEmail: EditText
    private lateinit var etNomorTelepon: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnEditProfil: Button
    private lateinit var btnLogout: TextView
    private lateinit var btnTogglePassword: ImageView

    private var isEditing = false
    private var isPasswordVisible = false
    private var currentPassword = "\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022" // Placeholder password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_service)

        supportActionBar?.hide()

        // Inisialisasi Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        // Inisialisasi komponen UI
        etNamaLengkap = findViewById(R.id.et_nama_lengkap)
        etEmail = findViewById(R.id.et_email)
        etNomorTelepon = findViewById(R.id.et_nomor_telepon)
        etPassword = findViewById(R.id.et_password)
        btnEditProfil = findViewById(R.id.btn_edit_profil)
        btnLogout = findViewById(R.id.btn_logout)
        btnTogglePassword = findViewById(R.id.iv_toggle_password)

        setEditTextEnabled(false) // Default tidak bisa diedit
        getUserData()

        // Tombol Edit/Simpan
        btnEditProfil.setOnClickListener {
            toggleEditSave()
        }

        // Tombol Logout
        btnLogout.setOnClickListener {
            logoutUser()
        }

        // Tombol Toggle Password Visibility
        btnTogglePassword.setOnClickListener {
            togglePasswordVisibility()
        }

        // Bottom Navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_profil_service)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeService::class.java))
                    true
                }
                R.id.nav_activity -> {
                    startActivity(Intent(this, ActivityService::class.java))
                    true
                }
                R.id.nav_profile -> true
                else -> false
            }
        }
        bottomNav.selectedItemId = R.id.nav_profile
    }

    private fun getUserData() {
        val user = firebaseAuth.currentUser
        user?.let {
            val userId = it.uid
            databaseReference.child("service").child(userId).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val namaLengkap = snapshot.child("name").value?.toString() ?: ""
                    val email = snapshot.child("email").value?.toString() ?: ""
                    val nomorTelepon = snapshot.child("nomorHp").value?.toString() ?: ""

                    etNamaLengkap.setText(namaLengkap)
                    etEmail.setText(email)
                    etNomorTelepon.setText(nomorTelepon)
                    etPassword.setText(currentPassword)
                } else {
                    Toast.makeText(this, "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Gagal mengambil data: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toggleEditSave() {
        if (isEditing) {
            val updatedNamaLengkap = etNamaLengkap.text.toString()
            val updatedEmail = etEmail.text.toString()
            val updatedNomorTelepon = etNomorTelepon.text.toString()
            val updatedPassword = etPassword.text.toString()

            val userId = firebaseAuth.currentUser?.uid
            userId?.let { uid ->
                val userRef = databaseReference.child("service").child(uid)
                val updates = mutableMapOf<String, Any>()

                if (updatedNamaLengkap.isNotEmpty()) updates["name"] = updatedNamaLengkap
                if (updatedEmail.isNotEmpty()) updates["email"] = updatedEmail
                if (updatedNomorTelepon.isNotEmpty()) updates["nomorHp"] = updatedNomorTelepon

                // Perbarui password hanya jika diubah
                if (updatedPassword != currentPassword) {
                    if (isValidPassword(updatedPassword)) {
                        firebaseAuth.currentUser?.updatePassword(updatedPassword)
                            ?.addOnSuccessListener {
                                Toast.makeText(this, "Password berhasil diperbarui", Toast.LENGTH_SHORT).show()
                            }
                            ?.addOnFailureListener {
                                Toast.makeText(this, "Gagal memperbarui password", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Password harus mengandung huruf besar dan angka", Toast.LENGTH_SHORT).show()
                        return
                    }
                }

                userRef.updateChildren(updates)
                    .addOnSuccessListener { Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show() }
                    .addOnFailureListener { Toast.makeText(this, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show() }
            }
            setEditTextEnabled(false)
            btnEditProfil.text = "Edit Profil"
        } else {
            setEditTextEnabled(true)
            btnEditProfil.text = "Simpan"
        }
        isEditing = !isEditing
    }

    private fun setEditTextEnabled(enabled: Boolean) {
        etNamaLengkap.isEnabled = enabled
        etEmail.isEnabled = enabled
        etNomorTelepon.isEnabled = enabled
        etPassword.isEnabled = enabled
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        etPassword.inputType = if (isPasswordVisible) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        etPassword.setSelection(etPassword.text.length)
        btnTogglePassword.setImageResource(
            if (isPasswordVisible) R.drawable.visibility_24px
            else R.drawable.visibility_off_24px
        )
    }

    private fun logoutUser() {
        firebaseAuth.signOut()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[A-Z])(?=.*\\d).+$")
        return password.matches(passwordRegex)
    }
}