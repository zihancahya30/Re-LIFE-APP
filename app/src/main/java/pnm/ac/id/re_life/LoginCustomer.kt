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

class LoginCustomer: ComponentActivity(), View.OnClickListener {
    private lateinit var et_email: EditText
    private lateinit var et_passwd: EditText
    private lateinit var btnLogin: Button
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_customer)

        et_email = findViewById(R.id.et_email)
        et_passwd = findViewById(R.id.et_passwd)
        btnLogin = findViewById(R.id.btnLogin)

        databaseRef = FirebaseDatabase.getInstance().getReference("customers")

        btnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLogin -> {
                val email = et_email.text.toString().trim()
                val password = et_passwd.text.toString().trim()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Email atau Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                    return
                }

                // Mengambil data dari Firebase dan memverifikasi login
                databaseRef.orderByChild("email").equalTo(email).get().addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val user = snapshot.children.firstOrNull()
                        val storedPassword = user?.child("password")?.value.toString()

                        if (storedPassword == password) {
                            val pindahIntent = Intent(this, HomeCustomer::class.java)
                            startActivity(pindahIntent)
                        } else {
                            Toast.makeText(this, "Password salah", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Email tidak terdaftar", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Gagal mengakses data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}