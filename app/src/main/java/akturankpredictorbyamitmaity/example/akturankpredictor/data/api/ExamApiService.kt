package akturankpredictorbyamitmaity.example.akturankpredictor.data.api

import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.Exam
import retrofit2.Call
import retrofit2.http.GET

interface ExamApiService {
    @GET("exams.json")
    fun getExams(): Call<List<Exam>>
}
