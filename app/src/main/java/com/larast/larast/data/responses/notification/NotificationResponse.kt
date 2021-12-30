package com.larast.larast.data.responses.notification

data class NotificationResponse(
    val current_page: Int,
    val `data`: List<Notification>,
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

data class Notification(
    val content: String,
    val created_at: String,
    val id_notifikasi: Long,
    val status: String,
    val title: String,
    val updated_at: String,
    val user_id: Int
)

data class ReadResponse(
    val message: String
)

data class Link(
    val active: Boolean,
    val label: String,
    val url: Any
)