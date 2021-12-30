package com.larast.larast.ui.home.track.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.larast.larast.R
import com.larast.larast.data.helper.formatDate
import com.larast.larast.data.helper.gone
import com.larast.larast.data.helper.visible
import com.larast.larast.data.responses.track.Track
import com.larast.larast.databinding.TrackItemBinding

class TrackAdapter(
    private val listener: OnItemClickListener
) : PagingDataAdapter<Track, TrackAdapter.MyViewHolder>(TrackComparator) {

    override fun onBindViewHolder(holder: TrackAdapter.MyViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackAdapter.MyViewHolder {
        return MyViewHolder(
            TrackItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class MyViewHolder(private val binding: TrackItemBinding) :
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

        @SuppressLint("SetTextI18n")
        fun bind(track: Track) = with(binding) {

            when (track.kode) {
                null -> {
                    when (track.berkas) {
                        "1" -> {
                            txtCitizens.text = track.pemohon.nama
                            txtService.text = track.pelayanan.pelayanan
                            txtDate.text = formatDate(track.created_at)
                            txtDesc.text = "Keterangan : Menunggu Konfirmasi RT atau RW"
                            txtStateFile.text = "Offline"
                        }
                        else -> {
                            txtCitizens.text = track.pemohon.nama
                            txtService.text = track.pelayanan.pelayanan
                            txtDate.text = formatDate(track.created_at)
                            txtDesc.text = "Keterangan : Menunggu Konfirmasi RT atau RW"
                            txtStateFile.text = "Online"
                        }
                    }
                }
            }

            when (track.status) {
                "2" -> {
                    when (track.berkas) {
                        "1" -> {
                            txtCode.text = track.kode
                            txtCitizens.text = track.pemohon.nama
                            txtService.text = track.pelayanan.pelayanan
                            txtRtRw.setBackgroundResource(R.drawable.bg_process)
                            txtRtRw.setTextColor(Color.WHITE)
                            txtDesc.text = "Keterangan : Ketuk surat ini untuk langkah selanjutnya"
                            txtDate.text = formatDate(track.created_at)
                            txtStateFile.text = "Offline"
                        }
                        else -> {
                            txtCode.text = track.kode
                            txtCitizens.text = track.pemohon.nama
                            txtService.text = track.pelayanan.pelayanan
                            txtRtRw.setBackgroundResource(R.drawable.bg_process)
                            txtRtRw.setTextColor(Color.WHITE)
                            txtDesc.text = "Keterangan : Ketuk surat ini untuk langkah selanjutnya"
                            txtDate.text = formatDate(track.created_at)
                            txtStateFile.text = "Online"
                        }
                    }
                }
                "3" -> {
                    when (track.berkas) {
                        "1" -> {
                            txtCode.text = track.kode
                            txtCitizens.text = track.pemohon.nama
                            txtService.text = track.pelayanan.pelayanan
                            txtKel.setBackgroundResource(R.drawable.bg_process)
                            txtKel.setTextColor(Color.WHITE)
                            txtDesc.text = "Keterangan : Berkas sedang di proses"
                            txtDate.text = formatDate(track.created_at)
                            txtStateFile.text = "Offline"
                        }
                        else -> {
                            txtCode.text = track.kode
                            txtCitizens.text = track.pemohon.nama
                            txtService.text = track.pelayanan.pelayanan
                            txtKel.setBackgroundResource(R.drawable.bg_process)
                            txtKel.setTextColor(Color.WHITE)
                            txtDesc.text = "Keterangan : Berkas sedang di proses"
                            txtDate.text = formatDate(track.created_at)
                            txtStateFile.text = "Online"
                        }
                    }
                }
                "4" -> {
                    when (track.berkas) {
                        "1" -> {
                            txtCode.text = track.kode
                            txtCitizens.text = track.pemohon.nama
                            txtService.text = track.pelayanan.pelayanan
                            txtFile.setBackgroundResource(R.drawable.bg_success)
                            txtFile.setTextColor(Color.WHITE)
                            txtDesc.text = "Keterangan : Silahkan ambil berkas di kelurahan"
                            txtDesc.setTextColor(Color.GREEN)
                            txtDate.text = formatDate(track.created_at)
                            txtStateFile.text = "Offline"
                        }
                        else -> {
                            txtCode.text = track.kode
                            txtCitizens.text = track.pemohon.nama
                            txtService.text = track.pelayanan.pelayanan
                            txtFile.setBackgroundResource(R.drawable.bg_success)
                            txtFile.setTextColor(Color.WHITE)
                            txtDesc.text = "Keterangan : Silahkan ambil berkas di kelurahan"
                            txtDesc.setTextColor(Color.GREEN)
                            txtDate.text = formatDate(track.created_at)
                            txtStateFile.text = "Online"
                        }
                    }
                }
                "6" -> {
                    when (track.berkas) {
                        "1" -> {
                            txtCode.text = track.kode
                            txtCitizens.text = track.pemohon.nama
                            txtService.text = track.pelayanan.pelayanan
                            txtFail.setBackgroundResource(R.drawable.bg_fail)
                            txtFail.setTextColor(Color.WHITE)
                            txtDesc.text = "Keterangan : ${track.catatan}"
                            txtDesc.setTextColor(Color.RED)
                            txtDate.text = formatDate(track.created_at)
                            txtStateFile.text = "Offline"
                        }
                        else -> {
                            txtCode.text = track.kode
                            txtCitizens.text = track.pemohon.nama
                            txtService.text = track.pelayanan.pelayanan
                            txtFail.setBackgroundResource(R.drawable.bg_fail)
                            txtFail.setTextColor(Color.WHITE)
                            txtDesc.text = "Keterangan : ${track.catatan}"
                            txtDesc.setTextColor(Color.RED)
                            txtDate.text = formatDate(track.created_at)
                            txtStateFile.text = "Online"
                        }
                    }
                }
            }

            ivDown.setOnClickListener {
                expandedMenu.visible()
                ivUp.visible()
                ivDown.gone()
            }

            ivUp.setOnClickListener {
                expandedMenu.gone()
                ivDown.visible()
                ivUp.gone()
            }
        }

    }

    object TrackComparator : DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.id_pengantar == newItem.id_pengantar
        }

        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemClick(track: Track)
    }

}