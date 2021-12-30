package com.larast.larast.data.network

import com.larast.larast.data.responses.auth.*
import com.larast.larast.data.responses.family.FamilyResponse
import com.larast.larast.data.responses.files.FileResponse
import com.larast.larast.data.responses.files.FilesResponse
import com.larast.larast.data.responses.home.EventResponse
import com.larast.larast.data.responses.home.NewsResponse
import com.larast.larast.data.responses.letter.CitizensResponse
import com.larast.larast.data.responses.letter.LetterResponse
import com.larast.larast.data.responses.letter.ServiceResponse
import com.larast.larast.data.responses.notification.NotificationResponse
import com.larast.larast.data.responses.notification.ReadResponse
import com.larast.larast.data.responses.track.TrackResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface MyApi {

    @FormUrlEncoded
    @POST("api/auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("fcm") fcm: String,
    ): LoginResponse

    @GET("api/auth/user")
    suspend fun user(
        @Header("Authorization") token: String
    ): UserResponse

    @GET("api/auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): LogoutResponse

    @FormUrlEncoded
    @POST("api/auth/forgot")
    suspend fun forgot(
        @Field("email") email: String,
    ): ForgotResponse

    @Multipart
    @POST("api/auth/profile")
    suspend fun profile(
        @Header("Authorization") token: String,
        @Part foto: MultipartBody.Part,
    ): ProfileResponse

    @FormUrlEncoded
    @POST("api/auth/user")
    suspend fun update(
        @Header("Authorization") token: String,
        @Field("nama") nama: String,
        @Field("email") email: String,
        @Field("hp") hp: String,
    ): ProfileResponse

    @GET("api/pengantar/pemohon")
    suspend fun citizens(
        @Header("Authorization") token: String
    ): CitizensResponse

    @GET("api/pengantar/pelayanan")
    suspend fun services(
        @Header("Authorization") token: String
    ): ServiceResponse

    @FormUrlEncoded
    @POST("api/pengantar/")
    suspend fun letter(
        @Header("Authorization") token: String,
        @Field("pemohon") pemohon: Int,
        @Field("pelayanan") pelayanan: Int,
        @Field("hp") hp: String,
        @Field("keterangan") keterangan: String,
        @Field("berkas") berkas: String
    ): LetterResponse

    @GET("api/pengantar/index")
    suspend fun track(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): TrackResponse

    @GET("api/berkas/keluarga")
    suspend fun family(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): FamilyResponse

    @GET("api/berkas/{id}")
    suspend fun files(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): FilesResponse

    @Multipart
    @POST("api/berkas/kk/{id}")
    suspend fun kk(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part kk: MultipartBody.Part
    ): FileResponse

    @Multipart
    @POST("api/berkas/akte/{id}")
    suspend fun akte(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part akte: MultipartBody.Part,
    ): FileResponse

    @Multipart
    @POST("api/berkas/ktps/{id}")
    suspend fun ktps(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part ktps: MultipartBody.Part,
    ): FileResponse

    @Multipart
    @POST("api/berkas/ktpi/{id}")
    suspend fun ktpi(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part ktpi: MultipartBody.Part,
    ): FileResponse

    @Multipart
    @POST("api/berkas/nikah/{id}")
    suspend fun nikah(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part nikah: MultipartBody.Part,
    ): FileResponse

    @Multipart
    @POST("api/berkas/ijazah/{id}")
    suspend fun ijazah(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part ijazah: MultipartBody.Part,
    ): FileResponse

    @Multipart
    @POST("api/berkas/skl/{id}")
    suspend fun skl(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part skl: MultipartBody.Part,
    ): FileResponse

    @Multipart
    @POST("api/berkas/agama/{id}")
    suspend fun agama(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part agama: MultipartBody.Part,
    ): FileResponse

    @GET("api/pengantar/state/{id}")
    suspend fun state(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
    ): LetterResponse

    @GET("api/berita/")
    suspend fun news(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): NewsResponse

    @GET("api/berita/acara")
    suspend fun event(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): EventResponse

    @GET("api/notifikasi/")
    suspend fun notification(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): NotificationResponse

    @GET("api/notifikasi/read")
    suspend fun read(
        @Header("Authorization") token: String,
    ): ReadResponse

}