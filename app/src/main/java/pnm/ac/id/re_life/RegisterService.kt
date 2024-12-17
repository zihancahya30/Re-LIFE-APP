package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterService : ComponentActivity(), View.OnClickListener {

    private lateinit var etNama: EditText
    private lateinit var etNomor: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPasswd: EditText
    private lateinit var etKonfPass: EditText
    private lateinit var btnRegister: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_service)

        // Inisialisasi FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        etNama = findViewById(R.id.et_nama)
        etNomor = findViewById(R.id.et_nomor)
        etEmail = findViewById(R.id.et_email)
        etPasswd = findViewById(R.id.et_passwd)
        etKonfPass = findViewById(R.id.et_konfpass)
        btnRegister = findViewById(R.id.btnRegister)
        btnRegister.setOnClickListener(this)

        val ivBack: ImageView = findViewById(R.id.back_login)
        ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        registerUser()
    }

    private fun registerUser() {
        val name = etNama.text.toString().trim()
        val nomorHp = etNomor.text.toString().trim()
        val email = etEmail.text.toString().trim().lowercase()
        val password = etPasswd.text.toString().trim()
        val konfPass = etKonfPass.text.toString().trim()

        // Validasi input
        if (name.isEmpty()) {
            etNama.error = "Nama wajib diisi"
            return
        }
        if (nomorHp.isEmpty()) {
            etNomor.error = "Nomor HP wajib diisi"
            return
        }
        if (email.isEmpty()) {
            etEmail.error = "Email wajib diisi"
            return
        }
        if (!email.endsWith("@gmail.com")) {
            etEmail.error = "Email harus menggunakan @gmail.com"
            return
        }
        if (password.isEmpty()) {
            etPasswd.error = "Password wajib diisi"
            return
        }
        if (password.length < 8) {
            etPasswd.error = "Password minimal 8 karakter"
            return
        }
        if (!isValidPassword(password)) {
            etPasswd.error = "Password harus mengandung huruf besar dan angka"
            return
        }
        if (konfPass.isEmpty()) {
            etKonfPass.error = "Konfirmasi password wajib diisi"
            return
        }
        if (password != konfPass) {
            etKonfPass.error = "Password tidak cocok"
            return
        }

        // Registrasi ke Firebase Authentication
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveAdditionalData(name, nomorHp, email)
                } else {
                    Toast.makeText(this, "Registrasi gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun isValidPassword(password: String): Boolean {
        val hasUppercase = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        return hasUppercase && hasDigit
    }

    private fun saveAdditionalData(name: String, nomorHp: String, email: String) {
        val serviceData = hashMapOf(
            "name" to name,
            "nomorHp" to nomorHp,
            "email" to email
        )

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("service")
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            ref.child(userId).setValue(serviceData)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomeService::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Gagal menyimpan data: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "User ID tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }
}
