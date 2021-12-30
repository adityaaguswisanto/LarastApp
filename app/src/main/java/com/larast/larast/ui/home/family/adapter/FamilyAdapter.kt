package com.larast.larast.ui.home.family.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.larast.larast.data.responses.family.Family
import com.larast.larast.databinding.FamilyItemBinding

class FamilyAdapter (
    private val listener: OnItemClickListener
) : PagingDataAdapter<Family, FamilyAdapter.MyViewHolder>(FamilyComparator) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            FamilyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class MyViewHolder(private val binding: FamilyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ivFile.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(family: Family) = with(binding) {
            txtName.text = family.nama
            txtNik.text = family.nik.toString()
        }
    }

    object FamilyComparator : DiffUtil.ItemCallback<Family>() {
        override fun areItemsTheSame(oldItem: Family, newItem: Family): Boolean {
            return oldItem.id_penduduk == newItem.id_penduduk
        }

        override fun areContentsTheSame(oldItem: Family, newItem: Family): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemClick(family: Family)
    }

}