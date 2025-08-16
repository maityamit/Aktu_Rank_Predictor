package akturankpredictorbyamitmaity.example.akturankpredictor.service

import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.ApiResponse
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.Course
import akturankpredictorbyamitmaity.example.akturankpredictor.data.repository.CourseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CourseService(private val courseRepository: CourseRepository) {
    
    suspend fun getAvailableCourses(): ApiResponse<List<Course>> = withContext(Dispatchers.IO) {
        return@withContext courseRepository.getCourses()
    }
    
    fun getFeaturedCourses(courses: List<Course>): List<Course> {
        return courses.filter { it.is_featured }.take(3)
    }
    
    fun getCoursesByCategory(courses: List<Course>, category: String): List<Course> {
        return courses.filter { course ->
            course.name.contains(category, ignoreCase = true) ||
            course.description.contains(category, ignoreCase = true)
        }
    }
}
