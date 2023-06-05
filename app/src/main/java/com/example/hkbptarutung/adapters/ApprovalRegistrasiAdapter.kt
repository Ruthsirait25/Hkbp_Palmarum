package com.example.hkbptarutung.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.hkbptarutung.R
import com.example.hkbptarutung.databinding.ItemApprovalBinding
import com.example.hkbptarutung.model.ApprovalItem
import com.example.hkbptarutung.utils.FirebaseUtils
import com.example.hkbptarutung.utils.visibleGone

interface ApprovalRegistrasiInterface {
    fun onClickApprovalItem(item: ApprovalItem)
}

class ApprovalRegistrasiAdapter(
    private val listItem: List<ApprovalItem>,
    private val inf: ApprovalRegistrasiInterface,
) :
    RecyclerView.Adapter<ApprovalRegistrasiAdapter.ListViewHolder>() {

    class ListViewHolder(val binding: ItemApprovalBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemApprovalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = listItem[position]
        FirebaseUtils.db().collection(FirebaseUtils.dbUser).document(item.requestorId).get()
            .addOnSuccessListener {
                holder.binding.tvPersonName.text = "${it["fullName"]}"
                val approved = item.approved
                println("aa ini $approved")
                holder.binding.ivApproveIndicator.apply {
                    visibleGone(approved != null)
                    if (approved is Boolean) {
                        if (!approved) {
                            setImageDrawable(
                                AppCompatResources.getDrawable(this.context, R.drawable.clear_24)
                            )
                        }
                    }
                }

            }
        holder.binding.root.setOnClickListener {
            inf.onClickApprovalItem(item)
        }
        holder.binding.tvRegistrasiType.text = item.type
    }
}