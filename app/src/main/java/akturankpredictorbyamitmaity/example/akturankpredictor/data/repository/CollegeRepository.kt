package akturankpredictorbyamitmaity.example.akturankpredictor.data.repository

import akturankpredictorbyamitmaity.example.akturankpredictor.data.api.ApiClient
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.ApiResponse
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.College
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CollegeRepository {
    
    suspend fun getColleges(examId: String): ApiResponse<List<College>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val call = ApiClient.collegeApiService.getColleges(examId)
            val response = call.execute()
            
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!)
            } else {
                ApiResponse.Error("Failed to fetch colleges: ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResponse.Error("Network error: ${e.message}")
        }
    }
    
    fun filterColleges(
        colleges: List<College>,
        userPreferences: UserPreferences
    ): List<College> {
        return colleges.filter { college ->
            // Check if user's rank is within the college's rank range
            val rankInRange = userPreferences.rank <= college.cr
            
            // Check state preference using state_quota
            val stateMatch = when (userPreferences.state) {
                "All" -> true
                else -> {
                    when (userPreferences.state) {
                        college.state -> {
                            // If input state matches college state, check for HS or AI
                            college.state_quota == "HS" || college.state_quota == "AI"
                        }
                        else -> {
                            // If input state doesn't match college state, check for OS or AI
                            college.state_quota == "OS" || college.state_quota == "AI"
                        }
                    }
                }
            }
            
            // Check quota preference
            val quotaMatch = when (userPreferences.quota) {
                "All" -> true
                "General" -> college.quota == "General"
                "Ews" -> college.quota == "Ews"
                "Obc" -> college.quota == "Obc"
                "Sc" -> college.quota == "Sc"
                "St" -> college.quota == "St"
                else -> college.quota == userPreferences.quota
            }
            
            // Check gender preference
            val genderMatch = when (userPreferences.gender) {
                "All" -> college.gender == "Both" || college.gender == "Female"
                "Male" -> college.gender == "Both"
                "Female" -> college.gender == "Female"
                else -> true
            }
            
            rankInRange && stateMatch && quotaMatch && genderMatch
        }.sortedBy { it.cr }
    }
}
