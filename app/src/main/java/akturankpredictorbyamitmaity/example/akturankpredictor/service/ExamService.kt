package akturankpredictorbyamitmaity.example.akturankpredictor.service

import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.ApiResponse
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.Exam
import akturankpredictorbyamitmaity.example.akturankpredictor.data.repository.ExamRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExamService(private val examRepository: ExamRepository) {
    
    suspend fun getAvailableExams(): ApiResponse<List<Exam>> = withContext(Dispatchers.IO) {
        return@withContext examRepository.getExams()
    }
    
    fun getExamById(exams: List<Exam>, examId: String): Exam? {
        return examRepository.getExamById(exams, examId)
    }
    
    fun getExamByName(exams: List<Exam>, examName: String): Exam? {
        return exams.find { it.name.equals(examName, ignoreCase = true) }
    }
    
    fun validateExamId(examId: String): Boolean {
        return examId.isNotBlank() && examId.matches(Regex("^[a-zA-Z0-9_-]+$"))
    }
}
