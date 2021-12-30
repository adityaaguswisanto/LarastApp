package com.larast.larast.data.responses.family

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class FamilyResponse(
    val current_page: Int,
    val `data`: List<Family>,
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
data class Family(
    val agama: Int,
    val alamat: String,
    val ayah: String,
    val created_at: String,
    val ibu: String,
    val id_penduduk: Int,
    val jk: String,
    val kabupaten: Int,
    val kecamatan: Int,
    val kelurahan: Int,
    val kepala: Int,
    val kitas_kitap: String,
    val kk: Long,
    val kode_pos: Int,
    val nama: String,
    val negara: String,
    val nik: Long,
    val paspor: String,
    val pekerjaan: Int,
    val pendidikan: Int,
    val perkawinan: Int,
    val provinsi: Int,
    val shdk: Int,
    val tgl: String,
    val tpl: String,
    val updated_at: String
): Parcelable

data class Link(
    val active: Boolean,
    val label: String,
    val url: Any
)