package pnm.ac.id.re_life

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pnm.ac.id.re_life.databinding.ItemRiwayatPesananBinding

class RiwayatPesananAdapter(private val riwayatPesanan: List<String>) :
    RecyclerView.Adapter<RiwayatPesananAdapter.RiwayatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        val binding = ItemRiwayatPesananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RiwayatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
        val pesanan = riwayatPesanan[position]
        holder.binding.tvRiwayat.text = pesanan
    }

    override fun getItemCount(): Int = riwayatPesanan.size

    class RiwayatViewHolder(val binding: ItemRiwayatPesananBinding) :
        RecyclerView.ViewHolder(binding.root)
}
