package com.example.hkbptarutung.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hkbptarutung.R
import com.example.hkbptarutung.databinding.ItemRegistrasiLandingBinding
import com.example.hkbptarutung.registrasi.ActivityApprovalRegistrasi
import com.example.hkbptarutung.registrasi.RegistrasiPage
import com.example.hkbptarutung.utils.PreferenceUtils

class RegistrasiTipeAdapter(private val listItem: Array<String>) :
    RecyclerView.Adapter<RegistrasiTipeAdapter.ListViewHolder>() {
    class ListViewHolder(val binding: ItemRegistrasiLandingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemRegistrasiLandingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = listItem[position]
        holder.binding.tvRvRegislandingTitle.text = item
        holder.binding.root.setOnClickListener {
            it.context.apply {
                if (PreferenceUtils.isAdmin(this)) {
                    startActivity(Intent(this, ActivityApprovalRegistrasi::class.java).apply {
                        putExtra("type", convertType(item))
                    })
                } else {
                    startActivity(Intent(this, RegistrasiPage::class.java).apply {
                        putExtra("title", item)
                    })
                }
            }
        }
    }

    private fun Context.convertType(from: String): String? {
        when (from) {
            getString(R.string.registrasi_baptis) -> {
                return getString(R.string.menu_baptis)
            }

            getString(R.string.registrasi_pindah_huria) -> {
                return getString(R.string.menu_pindah_huria)
            }

            getString(R.string.menu_nikah) -> {
                return getString(R.string.menu_nikah)
            }

            getString(R.string.registrasi_martupol) -> {
                return getString(R.string.menu_martumpol)
            }

            getString(R.string.registrasi_sidi) -> {
                return getString(R.string.menu_sidi)
            }

            getString(R.string.registrasi_jemaat_baru) -> {
                return getString(R.string.menu_jemaat_baru)
            }
        }
        return null
    }
}