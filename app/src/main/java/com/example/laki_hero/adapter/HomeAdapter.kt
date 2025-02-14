import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.laki_hero.R
import com.example.laki_hero.data.remote.response.ListLaporanSayaItem
import com.example.laki_hero.databinding.ItemRecycleBinding
import com.example.laki_hero.view.DetailLaporanActivity

class HomeAdapter : ListAdapter<ListLaporanSayaItem, HomeAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRecycleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val laporan = getItem(position)
        holder.bind(laporan)
    }

    class MyViewHolder(private val binding: ItemRecycleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListLaporanSayaItem) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(item.gambar)
                    .into(iv)

                isiPlace.text = item.tempat
                isiDeskripsi.text = item.deskripsi
                isiTgl.text = item.createdAt

                // Mengatur warna latar belakang berdasarkan status
                when (item.status.toLowerCase()) {
                    "monitored" -> isiStatus.setBackgroundResource(R.drawable.background_monitored)
                    "approved" -> isiStatus.setBackgroundResource(R.drawable.background_approved)
                    "rejected" -> isiStatus.setBackgroundResource(R.drawable.background_rejected)
                    else -> isiStatus.setBackgroundResource(R.drawable.background_abu2)
                }
                isiStatus.text = item.status
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailLaporanActivity::class.java)
                intent.putExtra(DetailLaporanActivity.DETAIL_LAPORAN, item)
                itemView.context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity).toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListLaporanSayaItem>() {
            override fun areItemsTheSame(oldItem: ListLaporanSayaItem, newItem: ListLaporanSayaItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListLaporanSayaItem, newItem: ListLaporanSayaItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
