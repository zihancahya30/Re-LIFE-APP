package pnm.ac.id.re_life.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pnm.ac.id.re_life.R
import pnm.ac.id.re_life.model.Pesanan

class SedangBerlangsungAdapter(private val pesananList: List<Pesanan>) :
    RecyclerView.Adapter<SedangBerlangsungAdapter.PesananViewHolder>() {

    // ViewHolder class untuk menampung tampilan item RecyclerView
    inner class PesananViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textNamaPesanan: TextView = itemView.findViewById(R.id.text_pesanan_title)
        val textAlamatPesanan: TextView = itemView.findViewById(R.id.text_pesanan_alamat)
        val textStatusPesanan: TextView = itemView.findViewById(R.id.text_pesanan_status)
    }

    // Membuat tampilan item RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PesananViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pesanan, parent, false)
        return PesananViewHolder(view)
    }

    // Bind data ke tampilan item RecyclerView
    override fun onBindViewHolder(holder: PesananViewHolder, position: Int) {
        val pesanan = pesananList[position]
        holder.textNamaPesanan.text = pesanan.namaLengkap
        holder.textAlamatPesanan.text = pesanan.alamatLengkap
        holder.textStatusPesanan.text = pesanan.status
    }

    // Menghitung jumlah item dalam daftar pesanan
    override fun getItemCount(): Int = pesananList.size
}