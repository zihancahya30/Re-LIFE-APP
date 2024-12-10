package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth

class LoginCustomer : ComponentActivity(), View.OnClickListener {

    private lateinit var etEmail: EditText
    private lateinit var etPasswd: EditText
    private lateinit var btnLogin: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_customer)

        etEmail = findViewById(R.id.et_email)
        etPasswd = findViewById(R.id.et_passwd)
        btnLogin = findViewById(R.id.btnLogin)

        // Inisialisasi FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener(this)
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
                if (password.isEmpty()) {
                    etPasswd.error = "Password tidak boleh kosong"
                    return
                }

                // Proses login menggunakan Firebase Authentication
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Login berhasil
                            Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, HomeCustomer::class.java)
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
}
