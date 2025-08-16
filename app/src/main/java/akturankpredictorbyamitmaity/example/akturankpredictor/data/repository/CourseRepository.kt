package akturankpredictorbyamitmaity.example.akturankpredictor.data.repository

import akturankpredictorbyamitmaity.example.akturankpredictor.data.api.ApiClient
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.ApiResponse
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.Course
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CourseRepository {
    
    suspend fun getCourses(): ApiResponse<List<Course>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val call = ApiClient.courseApiService.getCourses()
            val response = call.execute()
            
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!)
            } else {
                ApiResponse.Error("Failed to fetch courses: ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResponse.Error("Network error: ${e.message}")
        }
    }
}
