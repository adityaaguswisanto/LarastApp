package com.larast.larast.data.responses.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class LoginResponse(
    val user: User,
    val token: String
)

data class UserResponse(
    val user: User
)

data class ProfileResponse(
    val message: String
)

data class LogoutResponse(
    val message: String
)

data class ForgotResponse(
    val message: String
)

@Parcelize
data class User(
    val created_at: String,
    val email: String,
    val foto: String?,
    val hp: String,
    val id: Int,
    val kodwil: String,
    val nama: String,
    val role: String,
    val status: String,
    val updated_at: String,
    val username: String
): Parcelable