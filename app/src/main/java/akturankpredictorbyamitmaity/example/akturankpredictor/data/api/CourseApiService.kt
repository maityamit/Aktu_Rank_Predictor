package akturankpredictorbyamitmaity.example.akturankpredictor.data.api

import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.Course
import retrofit2.Call
import retrofit2.http.GET

interface CourseApiService {
    @GET("exams/courses.json")
    fun getCourses(): Call<List<Course>>
}
