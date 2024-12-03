package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginService : ComponentActivity(), View.OnClickListener {

    private lateinit var etEmail: EditText
    private lateinit var etPasswd: EditText
    private lateinit var btnLogin: Button
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_service)

        etEmail = findViewById(R.id.et_email)
        etPasswd = findViewById(R.id.et_passwd)
        btnLogin = findViewById(R.id.btnLogin)

        databaseRef = FirebaseDatabase.getInstance().getReference("service")

        btnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLogin -> {
                val email = etEmail.text.toString().trim().lowercase()
                val password = etPasswd.text.toString().trim()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Email atau Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                    return
                }

                databaseRef.orderByChild("email").equalTo(email).get()
                    .addOnSuccessListener { snapshot ->
                        if (snapshot.exists()) {
                            val user = snapshot.children.firstOrNull()
                            val storedPassword = user?.child("password")?.value.toString()

                            if (storedPassword == password) {
                                Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, HomeService::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "Password salah", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, "Email tidak terdaftar", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal mengakses data: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
