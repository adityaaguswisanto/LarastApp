package com.larast.larast.ui.home.home.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.larast.larast.data.helper.formatDate
import com.larast.larast.data.responses.notification.Notification
import com.larast.larast.databinding.NotificationItemBinding

class NotificationAdapter :
    PagingDataAdapter<Notification, NotificationAdapter.MyViewHolder>(NotificationComposer) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            NotificationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class MyViewHolder(private val binding: NotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: Notification) = with(binding) {
            txtTitle.text = notification.title
            txtContent.text = notification.content
            txtDate.text = formatDate(notification.created_at)
        }

    }

    object NotificationComposer : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.id_notifikasi == newItem.id_notifikasi
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }
    }

}