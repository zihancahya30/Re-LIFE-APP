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

class Register : ComponentActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        val RegisCustomer: Button = findViewById(R.id.btnRegisterCustomer)
        val RegisService: Button = findViewById(R.id.btnRegisterService)
        val btnLoginCustomer: Button = findViewById(R.id.btnLoginCustomer)
        val btnLoginService: Button = findViewById(R.id.btnLoginService)

        RegisCustomer.setOnClickListener(this)
        RegisService.setOnClickListener(this)
        btnLoginCustomer.setOnClickListener(this)
        btnLoginService.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnRegisterCustomer -> {
                val pindahIntent = Intent(this, RegisterCustomer::class.java)
                startActivity(pindahIntent)
            }
            R.id.btnRegisterService -> {
                val pindahIntent = Intent(this, RegisterService::class.java)
                startActivity(pindahIntent)
            }
            R.id.btnLoginCustomer -> {
                val pindahIntent = Intent(this, LoginCustomer::class.java)
                startActivity(pindahIntent)
            }
            R.id.btnLoginService -> {
                val pindahIntent = Intent(this, LoginService::class.java)
                startActivity(pindahIntent)
            }
        }
    }
}