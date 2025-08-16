package akturankpredictorbyamitmaity.example.akturankpredictor.data.api

import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.College
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CollegeApiService {
    @GET("exams/{examId}.json")
    fun getColleges(@Path("examId") examId: String): Call<List<College>>
}
