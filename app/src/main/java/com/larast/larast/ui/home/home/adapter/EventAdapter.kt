package com.larast.larast.ui.home.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.larast.larast.data.helper.loadImage
import com.larast.larast.data.helper.urlAssets
import com.larast.larast.data.responses.home.Event
import com.larast.larast.databinding.EventItemBinding

class EventAdapter : PagingDataAdapter<Event, EventAdapter.MyViewHolder>(EventComposer) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            EventItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class MyViewHolder(private val binding: EventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event) = with(binding) {
            ivImage.loadImage("${urlAssets()}${event.foto}")
            txtTitle.text = event.acara
            txtLocation.text = event.lokasi
            txtDate.text = event.mulai
        }

    }

    object EventComposer : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id_acara == newItem.id_acara
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }
}