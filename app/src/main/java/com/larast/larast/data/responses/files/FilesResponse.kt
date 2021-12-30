package com.larast.larast.data.responses.files

data class FilesResponse(
    val berkas: Berkas
)

data class FileResponse(
    val message: String
)

data class Berkas(
    val agama: String,
    val akte: String,
    val created_at: String,
    val id_berkas: Long,
    val ijazah: String,
    val kk: String,
    val ktpi: String,
    val ktps: String,
    val nikah: String,
    val penduduk: Int,
    val skl: String,
    val updated_at: String
)