package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
//import kotlinx.android.synthetic.main.activity_home_service.*

class ActivityService : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

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
    }
}