package pnm.ac.id.re_life

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class RegisterCustomer : ComponentActivity(), View.OnClickListener {

    private lateinit var et_nama: EditText
    private lateinit var et_nomor: EditText
    private lateinit var et_email: EditText
    private lateinit var et_passwd: EditText
    private lateinit var et_konfpass: EditText
    private lateinit var btnRegister: Button
    //    private lateinit var civProfileImage: CircleImageView
    private lateinit var ivEdit: ImageView
    private lateinit var ivBack: ImageView

//    private var selectedImageUri: Uri? = null
//    private val IMAGE_PICK_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_customer)

        et_nama = findViewById(R.id.et_nama)
        et_nomor = findViewById(R.id.et_nomor)
        et_email = findViewById(R.id.et_email)
        et_passwd = findViewById(R.id.et_passwd)
        et_konfpass = findViewById(R.id.et_konfpass)
        btnRegister = findViewById(R.id.btnRegister)
//        civProfileImage = findViewById(R.id.civ_profile_image)
        ivEdit = findViewById(R.id.iv_edit)
        ivBack = findViewById(R.id.iv_back)

        btnRegister.setOnClickListener(this)
//        ivEdit.setOnClickListener { openGallery() }
        ivBack.setOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {
        saveData()
    }

//    private fun openGallery() {
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = "image/*"
//        startActivityForResult(intent, IMAGE_PICK_CODE)
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
//            selectedImageUri = data?.data
//            civProfileImage.setImageURI(selectedImageUri) // Menampilkan gambar yang dipilih
//        }
//    }

    private fun saveData() {
        val name = et_nama.text.toString().trim()
        val nomorhp = et_nomor.text.toString().trim()
        val email = et_email.text.toString().trim()
        val password = et_passwd.text.toString().trim()
        val konfpass = et_konfpass.text.toString().trim()

        if (name.isEmpty()) {
            et_nama.error = "Belum mengisi nama kamu"
            return
        }
        if (nomorhp.isEmpty()) {
            et_nomor.error = "Belum mengisi nomor HP kamu"
            return
        }
        if (email.isEmpty()) {
            et_email.error = "Belum mengisi email kamu"
            return
        }
        if (password.isEmpty()) {
            et_passwd.error = "Belum mengisi password kamu"
            return
        }
        if (konfpass.isEmpty()) {
            et_konfpass.error = "Belum konfirmasi password kamu"
            return
        }
        if (password != konfpass) {
            et_konfpass.error = "Password tidak cocok"
            return
        }
        saveCustomerData(name, nomorhp, email, password)

//        if (selectedImageUri == null) {
//            Toast.makeText(this, "Harap pilih foto profil", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        uploadProfileImage(name, nomorhp, email, password)
    }

//    private fun uploadProfileImage(name: String, nomorhp: String, email: String, password: String) {
//        val storageRef = FirebaseStorage.getInstance().getReference("profile_images/${UUID.randomUUID()}")
//        selectedImageUri?.let { uri ->
//            storageRef.putFile(uri)
//                .addOnSuccessListener {
//                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
//                        saveCustomerData(name, nomorhp, email, password, downloadUrl.toString())
//                    }
//                }
//                .addOnFailureListener {
//                    Toast.makeText(this, "Gagal mengunggah foto: ${it.message}", Toast.LENGTH_SHORT).show()
//                }
//        }
//    }

    //    private fun saveCustomerData(name: String, nomorhp: String, email: String, password: String, profileImageUrl: String) {
//        val customerData = hashMapOf(
//            "name" to name,
//            "nomorhp" to nomorhp,
//            "email" to email,
//            "password" to password,
//            "profileImageUrl" to profileImageUrl
//        )
    private fun saveCustomerData(name: String, nomorhp: String, email: String, password: String) {
        val customerData = hashMapOf(
            "name" to name,
            "nomorhp" to nomorhp,
            "email" to email,
            "password" to password,
        )

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("customers")
        ref.push().setValue(customerData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, HomeService::class.java)
                    startActivity(intent)
                    finish()

                    clearFields()
                } else {
                    Toast.makeText(this, "Gagal menyimpan data: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun clearFields() {
        et_nama.text.clear()
        et_nomor.text.clear()
        et_email.text.clear()
        et_passwd.text.clear()
        et_konfpass.text.clear()
//        civProfileImage.setImageResource(R.drawable.default_profile) // Reset ke gambar default
//        selectedImageUri = null
    }
}