package com.larast.larast.data.responses.letter

import android.os.Parcelable
import com.larast.larast.data.responses.family.Family
import kotlinx.parcelize.Parcelize

data class LetterResponse(
    val message: String
)

data class CitizensResponse(
    val pemohon: List<Family>
)

data class ServiceResponse(
    val pelayanan: List<Pelayanan>
)

@Parcelize
data class Pelayanan(
    val created_at: String,
    val id_pelayanan: Int,
    val pelayanan: String,
    val updated_at: String
): Parcelable