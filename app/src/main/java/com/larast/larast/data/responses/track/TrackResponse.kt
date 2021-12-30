package com.larast.larast.data.responses.track

import android.os.Parcelable
import com.larast.larast.data.responses.family.Family
import com.larast.larast.data.responses.letter.Pelayanan
import kotlinx.parcelize.Parcelize

data class TrackResponse(
    val current_page: Int,
    val `data`: List<Track>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val links: List<Link>,
    val next_page_url: Any,
    val path: String,
    val per_page: Int,
    val prev_page_url: Any,
    val to: Int,
    val total: Int
)

@Parcelize
data class Track(
    val berkas: String,
    val catatan: String,
    val created_at: String,
    val hp: String,
    val id_pengantar: Long,
    val keterangan: String,
    val kode: String?,
    val pdf: String,
    val pelayanan: Pelayanan,
    val pemohon: Family,
    val status: String,
    val updated_at: String,
    val user_id: Int
) : Parcelable

data class Link(
    val active: Boolean,
    val label: String,
    val url: Any
)