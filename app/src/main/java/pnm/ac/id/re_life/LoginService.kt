package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth

class LoginService : ComponentActivity(), View.OnClickListener {

    private lateinit var etEmail: EditText
    private lateinit var etPasswd: EditText
    private lateinit var btnLogin: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_service)

        // Inisialisasi elemen UI
        etEmail = findViewById(R.id.et_email)
        etPasswd = findViewById(R.id.et_passwd)
        btnLogin = findViewById(R.id.btnLogin)
        val backLogin: ImageView = findViewById(R.id.back_login)

        // Inisialisasi FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Set listener untuk tombol login dan back
        btnLogin.setOnClickListener(this)
        backLogin.setOnClickListener {
            // Navigasi kembali ke halaman register service
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLogin -> {
                val email = etEmail.text.toString().trim().lowercase()
                val password = etPasswd.text.toString().trim()

                // Validasi input
                if (email.isEmpty()) {
                    etEmail.error = "Email tidak boleh kosong"
                    return
                }
                if (!email.endsWith("@gmail.com")) {
                    etEmail.error = "Email harus menggunakan @gmail.com"
                    return
                }
                if (password.isEmpty()) {
                    etPasswd.error = "Password tidak boleh kosong"
                    return
                }
                if (!isPasswordValid(password)) {
                    etPasswd.error = "Password harus mengandung minimal 1 huruf besar dan 1 angka"
                    return
                }

                // Proses login menggunakan Firebase Authentication
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Login berhasil
                            Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, HomeService::class.java)
                            Log.d("LoginService", "Berhasil login, mengarahkan ke HomeService")
                            startActivity(intent)
                            finish()
                        } else {
                            // Login gagal
                            Toast.makeText(this, "Login gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        // Cek apakah password mengandung minimal satu huruf besar dan satu angka
        val containsUpperCase = password.any { it.isUpperCase() }
        val containsDigit = password.any { it.isDigit() }
        return containsUpperCase && containsDigit
    }
}
