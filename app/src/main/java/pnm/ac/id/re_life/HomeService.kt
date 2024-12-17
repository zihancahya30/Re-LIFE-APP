package pnm.ac.id.re_life

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*

class HomeService : ComponentActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var ordersRecyclerView: RecyclerView
    private lateinit var ordersAdapter: OrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_service)

        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Inisialisasi RecyclerView
        ordersRecyclerView = findViewById(R.id.recycler_view_orders)
        ordersRecyclerView.layoutManager = LinearLayoutManager(this)

        // Memuat data orders dari Firebase
        loadAllOrdersData()

        // Menavigasi ke halaman PengelolaanService jika ic_tatacara diklik
        val tatacaraCard: ImageView = findViewById(R.id.ic_tatacara)
        tatacaraCard.setOnClickListener {
            val intent = Intent(this, PengelolaanService::class.java)
            startActivity(intent)
        }

        // Bottom Navigation
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_nav_home_service)
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Tetap di halaman HomeService
                    Toast.makeText(this, "Kamu sudah di Home", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_activity -> {
                    val intent = Intent(this, ActivityService::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileService::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun loadAllOrdersData() {
        // Mengakses data dari path "Orders" di Firebase
        database.child("Orders").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders = mutableListOf<Order>() // List untuk menyimpan data orders
                for (orderSnapshot in snapshot.children) {
                    // Konversi snapshot menjadi objek Order
                    val order = orderSnapshot.getValue(Order::class.java)
                    if (order != null) {
                        orders.add(order)
                    }
                }

                if (orders.isEmpty()) {
                    Toast.makeText(this@HomeService, "Tidak ada pesanan tersedia.", Toast.LENGTH_SHORT).show()
                }

                // Update RecyclerView Adapter
                ordersAdapter = OrdersAdapter(this@HomeService, orders) { selectedOrder ->
                    navigateToOrderDetails(selectedOrder)
                }
                ordersRecyclerView.adapter = ordersAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@HomeService,
                    "Gagal memuat data: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun navigateToOrderDetails(order: Order) {
        val intent = Intent(this, PesananMasuk::class.java)
        intent.putExtra("nama", order.namaLengkap)
        intent.putExtra("telepon", order.nomorTelepon)
        intent.putExtra("alamat", order.alamatLengkap)
        intent.putExtra("detail_alamat", order.detailAlamat)
        startActivity(intent)
    }
}

// Data Model
data class Order(
    val id: String = "",
    val namaLengkap: String = "",
    val alamatLengkap: String = "",
    val nomorTelepon: String = "",
    val detailAlamat: String = ""
)

// Adapter untuk RecyclerView
class OrdersAdapter(
    private val context: ComponentActivity, // Tambahkan context
    private val orders: List<Order>,
    private val onOrderClick: (Order) -> Unit
) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    // ViewHolder untuk menyimpan referensi ke tampilan item
    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaCustomer: TextView = itemView.findViewById(R.id.nama_customer)
        val alamatPesanan: TextView = itemView.findViewById(R.id.alamat_pesanan_text)
        val teleponPesanan: TextView = itemView.findViewById(R.id.telepon_customer)
        val nextArrow: ImageView = itemView.findViewById(R.id.ic_next_arrow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.namaCustomer.text = order.namaLengkap
        holder.alamatPesanan.text = order.alamatLengkap
        holder.teleponPesanan.text = order.nomorTelepon

        // Klik pada ikon nextArrow untuk membuka detail order
        holder.nextArrow.setOnClickListener {
            val intent = Intent(context, PesananMasuk::class.java)
            intent.putExtra("nama", order.namaLengkap)
            intent.putExtra("telepon", order.nomorTelepon)
            intent.putExtra("alamat", order.alamatLengkap)
            intent.putExtra("detail_alamat", order.detailAlamat)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = orders.size
}