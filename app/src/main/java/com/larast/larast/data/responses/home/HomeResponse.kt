package com.larast.larast.data.responses.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class NewsResponse(
    val current_page: Int,
    val `data`: List<News>,
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

data class EventResponse(
    val current_page: Int,
    val `data`: List<Event>,
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
data class News(
    val created_at: String,
    val foto: String,
    val id_berita: Int,
    val judul: String,
    val kategori: Kategori,
    val keterangan: String,
    val updated_at: String,
    val user_id: Int
): Parcelable

data class Event(
    val acara: String,
    val created_at: String,
    val foto: String,
    val id_acara: Int,
    val lokasi: String,
    val mulai: String,
    val status: String,
    val updated_at: String,
    val user_id: Int
)

@Parcelize
data class Kategori(
    val created_at: String,
    val id_kategori: Int,
    val kategori: String,
    val updated_at: String
): Parcelable

data class Link(
    val active: Boolean,
    val label: String,
    val url: Any
)