import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.here.sdk.core.Engine
import com.here.sdk.core.GeoCoordinate
import com.here.sdk.mapview.Map
import com.here.sdk.mapview.MapView
import com.here.sdk.mapview.MapScheme
import com.here.sdk.mapview.Camera
import com.here.sdk.mapview.MapMarker
import com.here.sdk.mapview.MapObject

class MainActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var engine: Engine
    private lateinit var map: Map

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_relife)

        // Menginisialisasi MapView
        mapView = findViewById(R.id.mapview)

        // Inisialisasi Here SDK Engine
        engine = Engine(applicationContext)
        engine.setApiKey(getString(R.string.here_api_key))

        // Tunggu hingga engine siap dan buat peta
        engine.onReady {
            map = mapView.map
            map.setMapScheme(MapScheme.NORMAL_DAY)  // Pilih skema peta (map style)

            // Set koordinat awal untuk peta (misalnya koordinat Berlin)
            val initialLocation = GeoCoordinate(52.530932, 13.384915)  // Latitude, Longitude
            map.camera.lookAt(initialLocation, 1500.0)  // Jarak zoom peta dalam meter

            // Tambahkan marker pada koordinat
            addMarker(initialLocation)
        }

        // Pastikan peta siap
        mapView.onCreate(savedInstanceState)
    }

    private fun addMarker(geoCoordinate: GeoCoordinate) {
        // Membuat marker pada peta
        val marker = MapMarker(geoCoordinate)
        map.addMapObject(marker)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}
