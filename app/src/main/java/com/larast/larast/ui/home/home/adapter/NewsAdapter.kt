package com.larast.larast.ui.home.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.larast.larast.data.helper.formatDate
import com.larast.larast.data.helper.loadImage
import com.larast.larast.data.helper.urlAssets
import com.larast.larast.data.responses.home.News
import com.larast.larast.databinding.NewsItemBinding

class NewsAdapter(
    private val listener: OnItemClickListener
) : PagingDataAdapter<News, NewsAdapter.MyViewHolder>(NewsComposer) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            NewsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class MyViewHolder(private val binding: NewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(news: News) = with(binding) {
            ivImage.loadImage("${urlAssets()}${news.foto}")
            txtTitle.text = news.judul
            txtDate.text = formatDate(news.created_at)
        }

    }

    object NewsComposer : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.id_berita == newItem.id_berita
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemClick(news: News)
    }

}