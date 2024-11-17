package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity

class Register : ComponentActivity(), View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)

        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button2 -> {
                val pindahIntent = Intent(this, RegisterCustomer::class.java)
                startActivity(pindahIntent)
            }
            R.id.button3 -> {
                val pindahIntent = Intent(this, RegisterService::class.java)
                startActivity(pindahIntent)
            }
        }
    }
}