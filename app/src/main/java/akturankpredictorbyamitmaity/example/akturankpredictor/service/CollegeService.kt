package akturankpredictorbyamitmaity.example.akturankpredictor.service

import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.ApiResponse
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.College
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.UserPreferences
import akturankpredictorbyamitmaity.example.akturankpredictor.data.repository.CollegeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CollegeService(private val collegeRepository: CollegeRepository) {
    
    suspend fun getCollegesForUser(
        examId: String,
        userPreferences: UserPreferences
    ): ApiResponse<List<College>> = withContext(Dispatchers.IO) {
        return@withContext when (val response = collegeRepository.getColleges(examId)) {
            is ApiResponse.Success -> {
                val filteredColleges = collegeRepository.filterColleges(
                    response.data,
                    userPreferences
                )
                ApiResponse.Success(filteredColleges)
            }
            is ApiResponse.Error -> response
            is ApiResponse.Loading -> response
        }
    }
    
    fun searchColleges(colleges: List<College>, query: String): List<College> {
        return if (query.isBlank()) {
            colleges
        } else {
            colleges.filter { college ->
                college.institute.contains(query, ignoreCase = true) ||
                college.course.contains(query, ignoreCase = true) ||
                college.state.contains(query, ignoreCase = true)
            }
        }
    }
    
    fun validateUserPreferences(userPreferences: UserPreferences): Boolean {
        return userPreferences.rank > 0 &&
               userPreferences.state.isNotBlank() &&
               userPreferences.gender.isNotBlank() &&
               userPreferences.quota.isNotBlank()
    }
}
