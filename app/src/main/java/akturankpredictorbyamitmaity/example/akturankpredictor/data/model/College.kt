package akturankpredictorbyamitmaity.example.akturankpredictor.data.model

import com.google.gson.annotations.SerializedName

data class College(
    val institute: String,
    val course: String,
    val state_quota: String, // HS/OS/AI
    val quota: String, // General/EWS/OBC/SC/ST
    val gender: String, // Both/Male/Female
    val or: Int,
    val cr: Int,
    val state: String
)
