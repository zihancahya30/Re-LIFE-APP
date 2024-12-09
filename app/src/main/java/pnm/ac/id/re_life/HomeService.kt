package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
//import kotlinx.android.synthetic.main.activity_home_service.*

class HomeService : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_service)

        // Handle BottomNavigation
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    true
                }
                R.id.nav_activity -> {
                    startActivity(Intent(this, ActivityService::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileService::class.java))
                    true
                }
                else -> false
            }
        }

//        // Handle Profile Icon Click
//        profile_image.setOnClickListener {
//            startActivity(Intent(this, ProfileService::class.java))
//        }
//
        // Handle "Tata Cara" ImageView Click
        findViewById<ImageView>(R.id.ic_tatacara).setOnClickListener {
            startActivity(Intent(this, PengelolaanService::class.java))
        }
//
//        // Placeholder: Set user data dynamically
//        val userName = "Zihan Cahya Amelia"
//        val userAddress = "Jl. Raya Ponorogo No. 123, Madiun"
//        findViewById<TextView>(R.id.halo_text).text = "Halo, $userName"
//        findViewById<TextView>(R.id.subtitle_text).text = userAddress
//
//        // Placeholder: Implement dynamic map details if needed
    }
}
