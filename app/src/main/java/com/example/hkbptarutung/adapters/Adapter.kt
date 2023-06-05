package com.example.hkbptarutung.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hkbptarutung.databinding.ItemBinding
import com.example.hkbptarutung.model.DataItem

class Adapter(private val listItem: ArrayList<DataItem>):RecyclerView.Adapter<Adapter.ListViewHolder>() {
    class ListViewHolder(val binding: ItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (item) = listItem[position]
        holder.binding.tvItem.text = item.toString()
    }
}