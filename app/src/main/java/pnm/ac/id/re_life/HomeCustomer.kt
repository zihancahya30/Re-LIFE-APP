package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class HomeCustomer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_customer)

        supportActionBar?.hide()

        val btnKumpulkanSampah = findViewById<Button>(R.id.btnKumpulkanSampah)

        btnKumpulkanSampah.setOnClickListener { view: View? ->
            val intent = Intent(
                this@HomeCustomer,
                RegisterCustomer::class.java
            )
            startActivity(intent)
        }

        val frameSampahDaurUlang = findViewById<ImageView>(R.id.btnSampahDaurUlang)
        frameSampahDaurUlang.setOnClickListener { view: View? ->
            val intent = Intent(
                this@HomeCustomer,
                TipsDaurCustomer::class.java
            )
            startActivity(intent)
        }

        val frameSampahNonDaurUlang = findViewById<ImageView>(R.id.btnSampahNonDaurUlang)
        frameSampahNonDaurUlang.setOnClickListener { view: View? ->
            val intent = Intent(
                this@HomeCustomer,
                TipsNonDaurCustomer::class.java
            )
            startActivity(intent)
        }


    }
}