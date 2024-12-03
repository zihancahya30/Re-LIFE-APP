package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.database.FirebaseDatabase

class RegisterService : ComponentActivity(), View.OnClickListener {

    private lateinit var etNama: EditText
    private lateinit var etNomor: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPasswd: EditText
    private lateinit var etKonfPass: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_customer)

        etNama = findViewById(R.id.et_nama)
        etNomor = findViewById(R.id.et_nomor)
        etEmail = findViewById(R.id.et_email)
        etPasswd = findViewById(R.id.et_passwd)
        etKonfPass = findViewById(R.id.et_konfpass)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        saveData()
    }

    private fun saveData() {
        val name = etNama.text.toString().trim()
        val nomorHp = etNomor.text.toString().trim()
        val email = etEmail.text.toString().trim().lowercase() // Normalisasi email ke huruf kecil
        val password = etPasswd.text.toString().trim()
        val konfPass = etKonfPass.text.toString().trim()

        if (name.isEmpty()) {
            etNama.error = "Belum mengisi nama kamu"
            return
        }
        if (nomorHp.isEmpty()) {
            etNomor.error = "Belum mengisi nomor HP kamu"
            return
        }
        if (email.isEmpty()) {
            etEmail.error = "Belum mengisi email kamu"
            return
        }
        if (password.isEmpty()) {
            etPasswd.error = "Belum mengisi password kamu"
            return
        }
        if (konfPass.isEmpty()) {
            etKonfPass.error = "Belum konfirmasi password kamu"
            return
        }
        if (password != konfPass) {
            etKonfPass.error = "Password tidak cocok"
            return
        }

        val serviceData = hashMapOf(
            "name" to name,
            "nomorHp" to nomorHp,
            "email" to email,
            "password" to password
        )

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("service")
        ref.push().setValue(serviceData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeService::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Gagal menyimpan data: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
