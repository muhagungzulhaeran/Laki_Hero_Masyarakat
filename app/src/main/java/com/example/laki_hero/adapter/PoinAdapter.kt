package com.example.laki_hero.adapter

import HomeAdapter.Companion.DIFF_CALLBACK
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.laki_hero.data.remote.response.ListLaporanSayaItem
import com.example.laki_hero.data.remote.response.ListPoinSayaItem
import com.example.laki_hero.databinding.ItemPoinBinding

class PoinAdapter : ListAdapter<ListPoinSayaItem, PoinAdapter.MyViewHolder>(DIFF_CALLBACK){

    class MyViewHolder(private val binding: ItemPoinBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListPoinSayaItem){
            binding.apply {
                isiTgl.text = item.tanggal
                isiRp.text = item.poin
//                isiTgl.text = item.
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPoinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val poin = getItem(position)
        holder.bind(poin)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListPoinSayaItem>() {
            override fun areItemsTheSame(oldItem: ListPoinSayaItem, newItem: ListPoinSayaItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListPoinSayaItem, newItem: ListPoinSayaItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}